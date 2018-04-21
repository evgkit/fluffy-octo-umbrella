package com.evgkit.reviews.dao;

import com.evgkit.reviews.exc.DaoException;
import com.evgkit.reviews.model.Item;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oItemDao implements ItemDao {

    private final Sql2o sql2o;

    public Sql2oItemDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Item item) throws DaoException {
        String sql = "INSERT INTO items(name, url) VALUES (:name, :url)";
        try (Connection connection = sql2o.open()) {
            int id = (int) connection
                .createQuery(sql)
                .bind(item)
                .executeUpdate()
                .getKey();
            item.setId(id);
        } catch (Sql2oException ex) {
            throw new DaoException(ex, "Problem adding item");
        }
    }

    @Override
    public List<Item> findAll() {
        try (Connection connection = sql2o.open()) {
            return connection
                .createQuery("SELECT * FROM items")
                .executeAndFetch(Item.class);
        }
    }
}
