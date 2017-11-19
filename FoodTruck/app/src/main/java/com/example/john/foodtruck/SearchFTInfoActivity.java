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
import android.widget.TextView;
import android.widget.Toast;

public class SearchFTInfoActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_foodtruck_info);

        Intent intent = getIntent();
        String area=intent.getStringExtra("area");
        String name=intent.getStringExtra("name");
        String id=intent.getStringExtra("id");
        String intro=intent.getStringExtra("introduction");
        String phone=intent.getStringExtra("phone");

        TextView title = (TextView) findViewById(R.id.FTTitle);
        title.setText(name);

        TabHost tabHost1 = (TabHost) findViewById(android.R.id.tabhost);

        tabHost1.setup() ;
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1") ;
        ts1.setContent(R.id.FTInfo) ;
        ts1.setIndicator("상세정보") ;

        //상세정보 탭 관련 내용
        TextView area1 = (TextView) findViewById(R.id.areaText);
        TextView phone1 = (TextView) findViewById(R.id.phoneText);
        TextView id1 = (TextView) findViewById(R.id.idText);
        TextView intro1 = (TextView) findViewById(R.id.introText);

        area1.setText("지역"+area);
        phone1.setText("전화번호"+phone);
        id1.setText("아이디"+id);
        intro1.setText("소개"+intro);


        tabHost1.addTab(ts1) ;
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2") ;
        ts2.setContent(R.id.FTReview) ;
        ts2.setIndicator("리뷰") ;
        tabHost1.addTab(ts2) ;

        //리뷰 탭 관련 내용
    }
}
