package com.example.john.foodtruck;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

    String currentID = "";
    String ftLocation = "";
    String ftStatus = "";

    double latitude;
    double longitude;
    String lat = "";
    String lon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__info);

        Intent intent = getIntent();
        final String FT_info_menulist = intent.getStringExtra("MenuList");
        final String FT_info_reviewlist = intent.getStringExtra("ReviewList");
        latitude=intent.getDoubleExtra("latitude",100);
        longitude=intent.getDoubleExtra("longitude",100);
        lat = Double.toString(latitude);
        lon = Double.toString(longitude);
        Log.d("latitude",lat);
        Log.d("longitude", lon);
        ftLocation="("+lat+","+lon+")";

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
        final ToggleButton FT_startButton = (ToggleButton) findViewById(R.id.FT_startButton);

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

        FT_startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FT_startButton.isChecked()){
                    ftStatus = "1";
                    new sTask().execute();
                }else{
                    ftStatus = "0";
                    new sTask().execute();
                }
            }
        });




        // 리뷰 탭

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

    private class sTask extends AsyncTask<String, Void, Integer> {
        int r = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            try {
                r = postJsonToServer(currentID, ftLocation, ftStatus);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }

        @Override
        protected void onPostExecute(Integer result) {
            AlertDialog.Builder alert = new AlertDialog.Builder(FT_InfoActivity.this);

            switch (result){
                case 0 :
                    Toast.makeText(FT_InfoActivity.this, "푸드트럭 영업이 종료되었습니다.", Toast.LENGTH_LONG).show();
                    break;
                case 1 :
                    Toast.makeText(FT_InfoActivity.this, "푸드트럭 영업이 시작되었습니다.", Toast.LENGTH_LONG).show();
                    break;
                case -1:
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("알 수 없는 에러가 발생하였습니다.");
                    alert.show();
                    break;
            }
        }
    }

    public int postJsonToServer(String currentID, String location, String status) throws IOException {

        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("id", currentID));
        registerInfo.add(new BasicNameValuePair("location", location));
        registerInfo.add(new BasicNameValuePair("status", status));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient = new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/sale_state");

        //HttpPost httpPost = new HttpPost("http://143.248.199.31:8081/foodtruck_enroll");

        // 객체 연결 설정 부분, 연결 최대시간 등등
        //HttpParams params = client.getParams();
        //HttpConnectionParams.setConnectionTimeout(params, 5000);
        //HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성


        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(registerInfo, "UTF-8");
            httpPost.setEntity(entity);
            //httpClient.execute(httpPost);

            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

            if (responseString.contains("0")) {
                //Log.d("영업 종료", responseString);
                return 0;
            } else if (responseString.contains("1")) {
                //Log.d("영업 개시", responseString);
                return 1;
            } else {
                Log.d("Unknown Error", responseString);
                return -1;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }


}
