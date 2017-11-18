package com.example.john.foodtruck;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

public class SearchFTInfoActivity extends TabActivity{
    TabHost mTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_foodtruck_info);

        Intent intent = getIntent(); // 보내온 Intent를 얻는다
        String a=intent.getStringExtra("area");
        String b=intent.getStringExtra("name");
        String c=intent.getStringExtra("id");
        String d=intent.getStringExtra("introduction");
        String e=intent.getStringExtra("phone");

        Toast.makeText(getApplicationContext(), a+"/"+b+"/"+c+"/"+d+"/"+e, Toast.LENGTH_LONG).show();

        mTab = getTabHost();
        TabHost.TabSpec spec;
        Intent intent1;


        intent1 = new Intent(this,FTInfoTabActivity.class);
        spec = mTab.newTabSpec("FirstTab").setIndicator("상세정보")
                .setContent(intent);
        mTab.addTab(spec);

        intent1 = new Intent(this,FTReviewTabActivity.class);
        spec = mTab.newTabSpec("SecondTab").setIndicator("리뷰")
                .setContent(intent);
        mTab.addTab(spec);
    }
}
