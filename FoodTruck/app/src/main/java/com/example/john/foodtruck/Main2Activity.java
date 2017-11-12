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
        setContentView(R.layout.activity_main2);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        Button FT_createButton = (Button) findViewById(R.id.FT_createButton);
        Button noticeButton = (Button) findViewById(R.id.noticeButton);
        Button settingButton = (Button) findViewById(R.id.settingButton);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(Main2Activity.this, SearchActivity.class);
                Main2Activity.this.startActivity(searchIntent);
            }
        });

        FT_createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent FT_createIntent = new Intent(Main2Activity.this, FT_CreateActivity.class);
                Main2Activity.this.startActivity(FT_createIntent);
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
