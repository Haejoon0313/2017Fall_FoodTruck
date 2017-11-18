package com.example.john.foodtruck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        Button favoriteButton = (Button) findViewById(R.id.FT_mytruckButton);
        Button noticeButton = (Button) findViewById(R.id.noticeButton);
        Button settingButton = (Button) findViewById(R.id.settingButton);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                MainActivity.this.startActivity(searchIntent);
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent favoriteIntent = new Intent(MainActivity.this, favoriteActivity.class);
                MainActivity.this.startActivity(favoriteIntent);
            }
        });

        noticeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent noticeIntent = new Intent(MainActivity.this, NoticeActivity.class);
                MainActivity.this.startActivity(noticeIntent);
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                MainActivity.this.startActivity(settingIntent);
            }
        });
    }
}
