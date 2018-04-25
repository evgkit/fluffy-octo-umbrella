package com.evgkit.reviews.dao;

import com.evgkit.reviews.exc.DaoException;
import com.evgkit.reviews.model.Item;
import com.evgkit.reviews.model.Review;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oReviewDaoTest {

    private Sql2oReviewDao reviewDao;
    private Sql2oItemDao itemDao;
    private Connection connection;
    private Item item;

    @Before
    public void setUp() throws Exception {
        Sql2o sql2o = new Sql2o(
                "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'",
                "",
                "");
        reviewDao = new Sql2oReviewDao(sql2o);
        itemDao = new Sql2oItemDao(sql2o);
        connection = sql2o.open();

        item = newTestItem();
        itemDao.add(item);
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void adding_review_sets_id() throws Exception {
        Review review = new Review(item.getId(), 5, "Test comment");
        int originalReviewId = review.getId();
        reviewDao.add(review);

        assertNotEquals(originalReviewId, review.getId());
    }

    @Test
    public void added_reviews_are_returned_from_find_all() throws Exception {
        reviewDao.add(new Review(item.getId(), 5, "Test comment 1"));
        reviewDao.add(new Review(item.getId(), 1, "Test comment 2"));

        assertEquals(2, reviewDao.findByItemId(item.getId()).size());
    }

    @Test(expected = DaoException.class)
    public void adding_a_review_to_a_non_existing_item_fails() throws Exception {
        reviewDao.add(new Review(100500, 5, "Test comment 1"));
    }

    private Item newTestItem() {
        return new Item("Test", "https://test.com");
    }
}