package com.evgkit.reviews.dao;

import com.evgkit.reviews.exc.DaoException;
import com.evgkit.reviews.model.Review;

import java.util.List;

public interface ReviewDao {
    void add(Review review) throws DaoException;

    List<Review> findAll();

    List<Review> findByItemId(int itemId);
}
