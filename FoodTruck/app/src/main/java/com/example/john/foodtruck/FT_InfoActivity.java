package com.example.john.foodtruck;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FT_InfoActivity extends AppCompatActivity {

    private ListView FT_info_menu;
    private MenuAdapter adapter;
    private List<FT_MenuList> menuList;

    private ListView FT_info_review;
    private ReviewAdapter adapter2;
    private List<FT_ReviewList> reviewList;

    private ListView FT_info_sales;
    private SalesAdapter adapter3;
    private List<SalesList> salesList;

    String currentID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_ft__info);

        Intent intent = getIntent();
        final String FT_info_menulist = intent.getStringExtra("MenuList");
        final String FT_info_reviewlist = intent.getStringExtra("ReviewList");
        final String FT_info_saleslist = intent.getStringExtra("saleslist");
        final MyApplication myApp = (MyApplication) getApplication();
        currentID = myApp.getcurrentID();

        FT_info_menu = (ListView) findViewById(R.id.FT_info_menu);
        menuList = new ArrayList<FT_MenuList>();

        TabHost tabHost1 = (TabHost) findViewById(android.R.id.tabhost);

        final TextView FT_info_name = (TextView) findViewById(R.id.FT_info_name);
        final TextView FT_info_phone = (TextView) findViewById(R.id.FT_info_phone);
        final TextView FT_info_area = (TextView) findViewById(R.id.FT_info_area);
        final TextView FT_info_ctg = (TextView) findViewById(R.id.FT_info_ctg);
        final TextView FT_info_intro = (TextView) findViewById(R.id.FT_info_intro);
        final ImageView FT_info_photo = (ImageView) findViewById(R.id.FTImage);

        /*Button backButton = (Button) findViewById(R.id.backButton);
        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("FoodTruck");

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // backpress와 똑같이
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final MyApplication myApp = (MyApplication) getApplication();
                myApp.setcurrentID("");
                Intent loginintent =  new Intent(FT_InfoActivity.this, LoginActivity.class);
                FT_InfoActivity.this.startActivity(loginintent);
                finish();
            }
        });*/



        // 상세정보 탭

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

        byte[] encodebytearray = Base64.decode(myApp.getTempFTphoto(),Base64.DEFAULT);
        Bitmap encodebitmap = BitmapFactory.decodeByteArray(encodebytearray,0,encodebytearray.length);
        FT_info_photo.setImageBitmap(encodebitmap);

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


        // 리뷰 탭

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

        // Tab 3

        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3") ;
        ts3.setContent(R.id.FT_sales) ;
        ts3.setIndicator("매출") ;
        tabHost1.addTab(ts3) ;

        FT_info_sales = (ListView) findViewById(R.id.FT_info_sales);
        salesList = new ArrayList<SalesList>();

        try {

            JSONArray salesarr = new JSONArray(FT_info_saleslist);
            String date, start, end, location, sales;

            for (int i = 0; i < salesarr.length(); i++){
                date = salesarr.getJSONObject(i).getString("date");
                start = salesarr.getJSONObject(i).getString("begin");
                end= salesarr.getJSONObject(i).getString("end");
                location= salesarr.getJSONObject(i).getString("location");
                sales= salesarr.getJSONObject(i).getString("total_price");
                salesList.add(new SalesList(date, start, end, sales, location));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter3 = new SalesAdapter(getApplicationContext(), salesList);
        FT_info_sales.setAdapter(adapter3);

    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
