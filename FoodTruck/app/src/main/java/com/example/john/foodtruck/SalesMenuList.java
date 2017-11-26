package com.example.john.foodtruck;

/**
 * Created by John on 2017-11-18.
 */

public class SalesMenuList {
    private String name;
    private String price;
    private String ingredients;
    private String nums;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public SalesMenuList(String name, String price, String ingredients, String nums) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.nums = nums;
    }
}

