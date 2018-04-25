package com.evgkit.reviews.dao;

import com.evgkit.reviews.exc.DaoException;
import com.evgkit.reviews.model.Review;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oReviewDao implements ReviewDao {
    private final Sql2o sql2o;

    public Sql2oReviewDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Review review) throws DaoException {
        try (Connection connection = sql2o.open()) {
            int id = (int) connection
                    .createQuery("INSERT INTO reviews (item_id, rating, comment) VALUES (:itemId, :rating, :comment)")
                    .bind(review)
                    .executeUpdate()
                    .getKey();
            review.setId(id);
        } catch (Sql2oException e) {
            throw new DaoException(e, "Problem adding review");
        }
    }

    @Override
    public List<Review> findAll() {
        try (Connection connection = sql2o.open()) {
            return connection
                    .createQuery("SELECT * FROM reviews")
                    .executeAndFetch(Review.class);
        }
    }

    @Override
    public List<Review> findByItemId(int itemId) {
        try (Connection connection = sql2o.open()) {
            return connection
                    .createQuery("SELECT * FROM reviews WHERE item_id = :itemId")
                    .addColumnMapping("ITEM_ID", "itemId")
                    .addParameter("itemId", itemId)
                    .executeAndFetch(Review.class);
        }
    }
}
