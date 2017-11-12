package com.example.john.foodtruck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        Button FTButton = (Button) findViewById(R.id.FT_create);
        Button noticeButton = (Button) findViewById(R.id.noticeButton);
        Button settingButton = (Button) findViewById(R.id.settingButton);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(Main2Activity.this, SearchActivity.class);
                Main2Activity.this.startActivity(searchIntent);
            }
        });

        FTButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent FTcreateIntent = new Intent(Main2Activity.this, FT_CreateActivity.class);
                Main2Activity.this.startActivity(FTcreateIntent);
            }
        });

        noticeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent noticeIntent = new Intent(Main2Activity.this, NoticeActivity.class);
                Main2Activity.this.startActivity(noticeIntent);
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent settingIntent = new Intent(Main2Activity.this, SettingActivity.class);
                Main2Activity.this.startActivity(settingIntent);
            }
        });
    }
}
