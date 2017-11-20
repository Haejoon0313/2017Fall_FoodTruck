package com.example.john.foodtruck;

/**
 * Created by John on 2017-11-18.
 */

public class FT_ReviewList {
    private String w_id;
    private String rating;
    private String detail;



    public String getW_id() {
        return w_id;
    }

    public void setW_id(String w_id) {
        this.w_id = w_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public FT_ReviewList(String w_id, String rating, String detail) {
        this.w_id = w_id;
        this.rating = rating;
        this.detail = detail;
    }
}

