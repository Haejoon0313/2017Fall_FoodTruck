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
    private String ctg;
    private String menulist;
    private String reviewlist;
    private String distance;


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

    public SearchResult(String area, String id, String intro, String name, String phone, String ctg, String menulist, String reviewlist, String distance) {
        this.area = area;
        this.id = id;
        this.intro = intro;
        this.name = name;
        this.phone = phone;
        this.ctg = ctg;
        this.menulist=menulist;
        this.reviewlist=reviewlist;
        this.distance=distance;
    }

    public String getMenulist() {
        return menulist;
    }

    public void setMenulist(String menulist) {
        this.menulist = menulist;
    }

    public String getReviewlist() {
        return reviewlist;
    }

    public void setReviewlist(String reviewlist) {
        this.reviewlist = reviewlist;
    }

    public String getCtg() {
        return ctg;
    }

    public void setCtg(String ctg) {
        this.ctg = ctg;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}

