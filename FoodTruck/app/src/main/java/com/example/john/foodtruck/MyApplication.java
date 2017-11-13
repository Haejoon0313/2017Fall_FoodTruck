package com.example.john.foodtruck;

import android.app.Application;

/**
 * Created by 정해준 on 2017-11-13.
 */

public class MyApplication extends Application {
    private String currentID;

    // 타 class에서 MyApplication class를 통해 해당 variable 값을 참조
    public String getcurrentID(){
        return currentID;
    }

    // 타 class에서 변경한 valuable을 MyApplication 에 저장
    public void setcurrentID(String newID){
        this.currentID = newID;
    }
}
