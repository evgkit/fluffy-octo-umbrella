package com.evgkit.reviews;

import com.evgkit.reviews.dao.ItemDao;
import com.evgkit.reviews.dao.Sql2oItemDao;
import com.evgkit.reviews.model.Item;
import com.google.gson.Gson;
import org.sql2o.Sql2o;

import static spark.Spark.after;
import static spark.Spark.post;
import static spark.Spark.get;

public class Api {
    public static void main(String[] args) {
      Sql2o sql2o = new Sql2o(
          "jdbc:h2:~/items.db;INIT=RUNSCRIPT from 'classpath:db/init.sql'",
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
