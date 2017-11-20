package com.example.john.foodtruck;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SearchFTInfoActivity extends AppCompatActivity{

    private ListView FT_reviewview_list;
    private ReviewAdapter adapter;
    private List<FT_ReviewList> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_foodtruck_info);

        final Intent intent = getIntent();
        final String area=intent.getStringExtra("area");
        final String name=intent.getStringExtra("name");
        final String id=intent.getStringExtra("id");
        final String intro=intent.getStringExtra("introduction");
        final String phone=intent.getStringExtra("phone");

        final String FT_menulist = intent.getStringExtra("MenuList");
        final String FT_reviewlist = intent.getStringExtra("ReviewList");

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

        // 메뉴 목록 추가해야함

        tabHost1.addTab(ts1) ;
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2") ;
        ts2.setContent(R.id.FTReview) ;
        ts2.setIndicator("리뷰") ;
        tabHost1.addTab(ts2) ;

        //리뷰 탭 관련 내용
        FT_reviewview_list = (ListView) findViewById(R.id.FT_reviewview_list);
        reviewList = new ArrayList<FT_ReviewList>();

        try {
            JSONArray reviewarr = new JSONArray(FT_reviewlist);

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

        adapter = new ReviewAdapter(getApplicationContext(), reviewList);
        FT_reviewview_list.setAdapter(adapter);

        Button FT_reviewview_enroll = (Button) findViewById(R.id.FT_reviewview_enroll);

        FT_reviewview_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(SearchFTInfoActivity.this, FT_ReviewCreateActivity.class);

                editIntent.putExtra("area", area);
                editIntent.putExtra("name", name);
                editIntent.putExtra("id", id);
                editIntent.putExtra("introduction", intro);
                editIntent.putExtra("phone", phone);

                editIntent.putExtra("MenuList", FT_menulist);
                editIntent.putExtra("ReviewList", FT_reviewlist);

                SearchFTInfoActivity.this.startActivity(editIntent);
            }
        });
    }
}
