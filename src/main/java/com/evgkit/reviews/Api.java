package com.evgkit.reviews;

import com.evgkit.reviews.dao.ItemDao;
import com.evgkit.reviews.dao.Sql2oItemDao;
import com.evgkit.reviews.model.Item;
import com.google.gson.Gson;
import org.sql2o.Sql2o;

import static spark.Spark.*;

public class Api {

    public static void main(String[] args) {
        String datasource = "jdbc:h2:~/items.db";
        if (args.length > 0) {
            if (args.length != 2) {
                System.out.println("java Api <port> <datasource>");
                System.exit(0);
            }
            port(Integer.parseInt(args[0]));
            datasource = args[1];
        }
        Sql2o sql2o = new Sql2o(
                datasource + ";INIT=RUNSCRIPT from 'classpath:db/init.sql'",
                "",
                "");
        ItemDao itemDao = new Sql2oItemDao(sql2o);

        Gson gson = new Gson();

        post("/items", "application/json",
                (request, response) -> {
                    Item item = gson.fromJson(request.body(), Item.class);
                    itemDao.add(item);

                    response.status(201);
                    return item;
                }, gson::toJson);

        get("/items", "application/json",
                (request, response) -> itemDao.findAll(), gson::toJson);

        get("/items/:id", "application/json",
                (request, response) -> {
                    int id = Integer.parseInt(request.params("id"));
                    return itemDao.findById(id);
                }, gson::toJson);

        after((request, response) -> {
            response.type("application/json");
        });
    }
}
