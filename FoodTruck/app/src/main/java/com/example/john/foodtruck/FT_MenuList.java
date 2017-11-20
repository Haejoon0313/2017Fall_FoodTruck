package com.example.john.foodtruck;

/**
 * Created by John on 2017-11-18.
 */

public class FT_MenuList {
    private String name;
    private String price;
    private String ingredients;



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

    public FT_MenuList(String name, String price, String ingredients) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }
}

