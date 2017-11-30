package com.example.john.foodtruck;

import android.app.Activity;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class SearchFTInfoActivity extends AppCompatActivity{
    public static Activity InfoActivity;

    private ListView FT_menuview_list;
    private MenuAdapter adapter2;
    private List<FT_MenuList> menuList;

    private ListView FT_reviewview_list;
    private ReviewAdapter adapter;
    private List<FT_ReviewList> reviewList;

    String favorite_state = "0";
    String ftID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_foodtruck_info);

        InfoActivity=SearchFTInfoActivity.this;

        final Intent intent = getIntent();
        final String area=intent.getStringExtra("area");
        final String name=intent.getStringExtra("name");
        final String id=intent.getStringExtra("id");
        final String intro=intent.getStringExtra("introduction");
        final String phone=intent.getStringExtra("phone");
        final String photo=intent.getStringExtra("photo");
        final String favorite_check=intent.getStringExtra("favorite");

        final String FT_menulist = intent.getStringExtra("menulist");
        final String FT_reviewlist = intent.getStringExtra("reviewlist");

        ftID = id;

        /*Button backButton = (Button) findViewById(R.id.backButton);
        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("FoodTruck");*/

        TextView fttitle = (TextView) findViewById(R.id.FTTitle);
        fttitle.setText(name);

        ImageView ftphoto=(ImageView) findViewById(R.id.ftphoto);
        byte[] encodebytearray = Base64.decode(photo,Base64.DEFAULT);
        Bitmap encodebitmap = BitmapFactory.decodeByteArray(encodebytearray,0,encodebytearray.length);
        ftphoto.setImageBitmap(encodebitmap);

        final ToggleButton favoriteToggle = (ToggleButton) findViewById(R.id.favoriteToggle);

        if(favorite_check.equals("1")){
            favoriteToggle.setChecked(true);
            favoriteToggle.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.favoritebutton_on)
            );
        }

        favoriteToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoriteToggle.isChecked()){
                    favoriteToggle.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.favoritebutton_on)
                    );

                    favorite_state = "1";

                }else{
                    favoriteToggle.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.favoritebutton_off)
                    );

                    favorite_state = "0";
                }

                new fTask().execute();

            }
        });

        TabHost tabHost1 = (TabHost) findViewById(android.R.id.tabhost);
        tabHost1.setup() ;
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1") ;
        ts1.setContent(R.id.FTInfo) ;
        ts1.setIndicator("상세정보") ;

        //상세정보 탭 관련 내용
        TextView area1 = (TextView) findViewById(R.id.areaText);
        TextView phone1 = (TextView) findViewById(R.id.phoneText);
        TextView intro1 = (TextView) findViewById(R.id.introText);

        String[] tempArea = getResources().getStringArray(R.array.areaSpinnerArray);

        area1.setText("지역          "+tempArea[Integer.parseInt(area)]);
        phone1.setText("전화번호    "+phone);
        intro1.setText("소개          "+intro);

        // 메뉴 목록 추가해야함


        FT_menuview_list = (ListView) findViewById(R.id.menuList);
        menuList = new ArrayList<FT_MenuList>();

        try {
            JSONArray menuarr = new JSONArray(FT_menulist);

            String menu_name, menu_price, menu_ingredients;

            for (int i = 0; i < menuarr.length(); i++){
                menu_name = menuarr.getJSONObject(i).getString("name");
                menu_price = menuarr.getJSONObject(i).getString("price");
                menu_ingredients= menuarr.getJSONObject(i).getString("ingredients");

                menuList.add(new FT_MenuList(menu_name, menu_price, menu_ingredients));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter2 = new MenuAdapter(getApplicationContext(), menuList);
        FT_menuview_list.setAdapter(adapter2);

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
                editIntent.putExtra("photo", photo);
                editIntent.putExtra("MenuList", FT_menulist);
                editIntent.putExtra("ReviewList", FT_reviewlist);
                editIntent.putExtra("favorite", favorite_state);

                SearchFTInfoActivity.this.startActivity(editIntent);
            }
        });

        /*backButton.setOnClickListener(new View.OnClickListener(){
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
                Intent loginintent =  new Intent(SearchFTInfoActivity.this, LoginActivity.class);
                SearchFTInfoActivity.this.startActivity(loginintent);
                finish();
            }
        });*/
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    private class fTask extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            MyApplication myApp = (MyApplication) getApplication();
            try {
                result = postJsonToServer(myApp.getcurrentID(), ftID, favorite_state);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            String status = "";

            JSONObject obj = null;

            try {
                obj = new JSONObject(result);

                status = obj.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            switch (status){
                case "0":
                    // 즐겨찾기 해제

                    Toast.makeText(SearchFTInfoActivity.this, "즐겨찾기가 해제되었습니다.", Toast.LENGTH_SHORT).show();

                    break;
                case "1":
                    // 즐겨찾기 등록

                    Toast.makeText(SearchFTInfoActivity.this, "즐겨찾기에 등록되었습니다.", Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    }

    public String postJsonToServer(String userID, String ftID, String status) throws IOException {


        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("user_id", userID));
        registerInfo.add(new BasicNameValuePair("f_id", ftID));
        registerInfo.add(new BasicNameValuePair("status", status));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient= new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/favorite_change");

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
            String responseString;
            responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

            return responseString;


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
