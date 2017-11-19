package com.example.john.foodtruck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class FTReviewTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftreview_tab);
        Toast.makeText(getApplicationContext(), "리뷰 탭", Toast.LENGTH_LONG).show();
    }
}
