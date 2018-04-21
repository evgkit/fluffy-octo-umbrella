package com.evgkit.reviews.model;

public class Review {
    private int id;
    private int itemId;
    private int rating;
    private String comment;

    public Review(int itemId, int rating, String comment) {
        this.itemId = itemId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (id != review.id) return false;
        if (itemId != review.itemId) return false;
        if (rating != review.rating) return false;
        return comment != null ? comment.equals(review.comment) : review.comment == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + itemId;
        result = 31 * result + rating;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}
