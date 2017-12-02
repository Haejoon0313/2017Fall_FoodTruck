package com.example.john.foodtruck;

import android.app.Application;

/**
 * Created by 정해준 on 2017-11-13.
 */

public class MyApplication extends Application {
    private String currentID = "";
    private String usertype = "";

    private String tempFTname;
    private String tempFTphone = "-1";
    private int tempFTarea;
    private int tempFTctg;
    private String tempFTintro;
    private String tempFTphoto;

    // 타 class에서 MyApplication class를 통해 해당 variable 값을 참조
    public String getcurrentID(){
        return currentID;
    }

    // 타 class에서 변경한 valuable을 MyApplication 에 저장
    public void setcurrentID(String newID){
        this.currentID = newID;
    }

    // 타 class에서 MyApplication class를 통해 해당 variable 값을 참조
    public String getUsertype(){
        return usertype;
    }

    // 타 class에서 변경한 valuable을 MyApplication 에 저장
    public void setUsertype(String usertype){
        this.usertype = usertype;
    }

    public String getTempFTname(){
        return tempFTname;
    }

    public void setTempFTname(String newFTname){
        this.tempFTname = newFTname;
    }

    public String getTempFTphone(){
        return tempFTphone;
    }

    public void setTempFTphone(String newFTphone){
        this.tempFTphone = newFTphone;
    }

    public int getTempFTarea(){
        return tempFTarea;
    }

    public void setTempFTarea(String newFTarea){
        this.tempFTarea = Integer.parseInt(newFTarea);
    }

    public int getTempFTctg(){
        return tempFTctg;
    }

    public void setTempFTctg(String newFTctg){
        this.tempFTctg = Integer.parseInt(newFTctg);
    }

    public String getTempFTintro(){
        return tempFTintro;
    }

    public void setTempFTintro(String newFTintro){
        this.tempFTintro = newFTintro;
    }

    public String getTempFTphoto() {
        return tempFTphoto;
    }

    public void setTempFTphoto(String tempFTphoto) {
        this.tempFTphoto = tempFTphoto;
    }
}
