package com.example.john.foodtruck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class FT_InfoActivity extends AppCompatActivity {

    private ListView FT_info_menu;
    private MenuAdapter adapter;
    private List<FT_MenuList> menuList;

    private ListView FT_info_review;
    private ReviewAdapter adapter2;
    private List<FT_ReviewList> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__info);

        Intent intent = getIntent();
        final String FT_info_menulist = intent.getStringExtra("MenuList");
        final String FT_info_reviewlist = intent.getStringExtra("ReviewList");

        FT_info_menu = (ListView) findViewById(R.id.FT_info_menu);
        menuList = new ArrayList<FT_MenuList>();

        final MyApplication myApp = (MyApplication) getApplication();

        TabHost tabHost1 = (TabHost) findViewById(android.R.id.tabhost);

        final TextView FT_info_name = (TextView) findViewById(R.id.FT_info_name);
        final TextView FT_info_phone = (TextView) findViewById(R.id.FT_info_phone);
        final TextView FT_info_area = (TextView) findViewById(R.id.FT_info_area);
        final TextView FT_info_ctg = (TextView) findViewById(R.id.FT_info_ctg);
        final TextView FT_info_intro = (TextView) findViewById(R.id.FT_info_intro);

        tabHost1.setup() ;
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1") ;
        ts1.setContent(R.id.FT_info) ;
        ts1.setIndicator("상세정보") ;

        FT_info_name.setText(myApp.getTempFTname());
        FT_info_phone.setText(myApp.getTempFTphone());
        FT_info_intro.setText(myApp.getTempFTintro());

        String[] tempArea = getResources().getStringArray(R.array.areaSpinnerArray);
        FT_info_area.setText(tempArea[myApp.getTempFTarea()]);

        String[] tempCtg = getResources().getStringArray(R.array.ctgSpinnerArray);
        FT_info_ctg.setText(tempCtg[myApp.getTempFTctg()]);

        try {
            JSONArray menuarr = new JSONArray(FT_info_menulist);

            String name, price, ingredients;

            for (int i = 0; i < menuarr.length(); i++){
                name = menuarr.getJSONObject(i).getString("name");
                price = menuarr.getJSONObject(i).getString("price");
                ingredients= menuarr.getJSONObject(i).getString("ingredients");

                menuList.add(new FT_MenuList(name, price, ingredients));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new MenuAdapter(getApplicationContext(), menuList);
        FT_info_menu.setAdapter(adapter);

        Button myMenuButton = (Button) findViewById(R.id.myMenuButton);
        Button FT_editButton = (Button) findViewById(R.id.FT_editButton);

        myMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuIntent = new Intent(FT_InfoActivity.this, FT_MenuViewActivity.class);
                menuIntent.putExtra("MenuList", FT_info_menulist);
                FT_InfoActivity.this.startActivity(menuIntent);
            }
        });

        FT_editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(FT_InfoActivity.this, FT_CreateActivity.class);
                FT_InfoActivity.this.startActivity(editIntent);
            }
        });

        tabHost1.addTab(ts1) ;
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2") ;
        ts2.setContent(R.id.FT_review) ;
        ts2.setIndicator("리뷰") ;
        tabHost1.addTab(ts2) ;

        FT_info_review = (ListView) findViewById(R.id.FT_info_review);
        reviewList = new ArrayList<FT_ReviewList>();

        try {
            JSONArray reviewarr = new JSONArray(FT_info_reviewlist);

            String w_id, rating, detail;

            for (int i = 0; i < reviewarr.length(); i++){
                w_id = reviewarr.getJSONObject(i).getString("w_id");
                rating = reviewarr.getJSONObject(i).getString("rating");
                detail= reviewarr.getJSONObject(i).getString("detail");

                reviewList.add(new FT_ReviewList(w_id, rating, detail));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter2 = new ReviewAdapter(getApplicationContext(), reviewList);
        FT_info_review.setAdapter(adapter2);

    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
