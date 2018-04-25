package com.evgkit.reviews;

import com.evgkit.reviews.dao.ItemDao;
import com.evgkit.reviews.dao.ReviewDao;
import com.evgkit.reviews.dao.Sql2oItemDao;
import com.evgkit.reviews.dao.Sql2oReviewDao;
import com.evgkit.reviews.exc.ApiError;
import com.evgkit.reviews.model.Item;
import com.evgkit.reviews.model.Review;
import com.google.gson.Gson;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.Map;

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
        ReviewDao reviewDao = new Sql2oReviewDao(sql2o);

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
                    Item item = itemDao.findById(id);
                    if (item == null) {
                        throw new ApiError(404, "Could not found item with id " + id);
                    }
                    return item;
                }, gson::toJson);

        post("/items/:itemId/reviews", "application/json",
                (request, response) -> {
                    int itemId = Integer.parseInt(request.params("itemId"));
                    if (itemDao.findById(itemId) == null) {
                        throw new ApiError(404, "Could not found item with id " + itemId);
                    }
                    Review review = gson.fromJson(request.body(), Review.class);
                    review.setItemId(itemId);
                    reviewDao.add(review);
                    response.status(201);
                    return review;
                }, gson::toJson);

        get("/items/:itemId/reviews", "application/json",
                (request, response) -> {
                    int itemId = Integer.parseInt(request.params("itemId"));
                    if (itemDao.findById(itemId) == null) {
                        throw new ApiError(404, "Could not found item with id " + itemId);
                    }
                    return reviewDao.findByItemId(itemId);
                }, gson::toJson);

        after((request, response) -> {
            response.type("application/json");
        });

        exception(ApiError.class, (exception, request, response) -> {
            ApiError error = (ApiError) exception;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", error.getStatus());
            jsonMap.put("errorMessage", error.getMessage());
            response.type("Application/json");
            response.status(error.getStatus());
            response.body(gson.toJson(jsonMap));
        });
    }
}
