package com.example.john.foodtruck;

/**
 * Created by John on 2017-11-18.
 */

public class SearchResult {
    private String area;
    private String id;
    private String intro;
    private String name;
    private String phone;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public SearchResult(String area, String id, String intro, String name, String phone) {
        this.area = area;
        this.id = id;
        this.intro = intro;
        this.name = name;
        this.phone = phone;
    }
}

