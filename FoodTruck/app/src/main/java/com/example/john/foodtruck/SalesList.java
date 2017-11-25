package com.example.john.foodtruck;

/**
 * Created by John on 2017-11-25.
 */

public class SalesList {
    private String date;
    private String start;
    private String end;
    private String sales;
    private String location;

    public SalesList(String date, String start, String end, String sales, String location) {
        this.date = date;
        this.start = start;
        this.end = end;
        this.sales = sales;
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}