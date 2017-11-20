package com.example.john.foodtruck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private GpsInfo gps;
    double latitude ;
    double longitude ;
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
                gps = new GpsInfo(MainActivity.this);
                // GPS 사용유무 가져오기
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                if (gps.isGetLocation() && latitude>30 && latitude<45 && longitude>120 && longitude<135 ) {
                    Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                    searchIntent.putExtra("latitude", latitude);
                    searchIntent.putExtra("longitude", longitude);
                    MainActivity.this.startActivity(searchIntent);
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }

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
