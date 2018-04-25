package com.evgkit.reviews;

import static org.junit.Assert.*;

import com.evgkit.reviews.dao.Sql2oItemDao;
import com.evgkit.reviews.model.Item;
import com.evgkit.testing.ApiClient;
import com.evgkit.testing.ApiResponse;
import com.google.gson.Gson;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

public class ApiTest {

    public static final String PORT = "4568";
    public static final String TEST_DATASOURCE = "jdbc:h2:mem:testing";

    private Connection connection;
    private ApiClient client;
    private Gson gson;
    private Sql2oItemDao itemDao;

    @BeforeClass
    public static void startServer() {
        Api.main(new String[]{PORT, TEST_DATASOURCE});
    }

    @AfterClass
    public static void stopServer() {
        Spark.stop();
    }

    @Before
    public void setUp() throws Exception {
        Sql2o sql2o = new Sql2o(
                TEST_DATASOURCE + ";INIT=RUNSCRIPT from 'classpath:db/init.sql'",
                "",
                "");

        itemDao = new Sql2oItemDao(sql2o);

        connection = sql2o.open();

        client = new ApiClient("http://localhost:" + PORT);

        gson = new Gson();
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void adding_items_returns_created_status() throws Exception {
        Map<String, String> values = new HashMap<>();
        values.put("name", "Test");
        values.put("url", "http://test.com");

        ApiResponse response = client.request("POST", "/items", gson.toJson(values));
        assertEquals(201, response.getStatus());
    }

    @Test
    public void items_can_be_accessed_by_id() throws Exception {
        Item item = newTestItem();
        itemDao.add(item);

        ApiResponse response = client.request("GET", "/items/" + item.getId());
        Item retrieved = gson.fromJson(response.getBody(), Item.class);

        assertEquals(item, retrieved);
    }

    @Test
    public void missing_utems_return_not_found_status() {
        ApiResponse response = client.request("GET", "/items/100500");
        assertEquals(404, response.getStatus());
    }

    private Item newTestItem() {
        return new Item("Test", "https://test.com");
    }
}