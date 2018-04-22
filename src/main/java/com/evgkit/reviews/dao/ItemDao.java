package com.evgkit.reviews.dao;

import com.evgkit.reviews.exc.DaoException;
import com.evgkit.reviews.model.Item;

import java.util.List;

public interface ItemDao {
    void add(Item item) throws DaoException;

    List<Item> findAll();

    Item findById(int id);
}
