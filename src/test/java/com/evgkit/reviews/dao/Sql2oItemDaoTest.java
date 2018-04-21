package com.evgkit.reviews.dao;

import com.evgkit.reviews.model.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oItemDaoTest {

    private Sql2oItemDao dao;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        dao = new Sql2oItemDao(sql2o);
        connection = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void testAdd_adding_item_sets_id() throws Exception {
        Item item = new Item("Test", "https://test.com");
        int originalItemId = item.getId();

        dao.add(item);

        assertNotEquals(originalItemId, item.getId());
    }

    @Test
    public void testFindAll_added_items_are_returned_from_find_all() throws Exception {
        Item item = new Item("Test", "https://test.com");

        dao.add(item);

        assertEquals(1, dao.findAll().size());
    }

    @Test
    public void testFindAll_no_items_returns_empty_list() throws Exception {
        assertEquals(0, dao.findAll().size());
    }
}