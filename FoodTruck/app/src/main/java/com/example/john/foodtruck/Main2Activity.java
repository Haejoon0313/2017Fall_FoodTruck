package com.example.john.foodtruck;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class Main2Activity extends AppCompatActivity {
    private long pressedTime;
    private GpsInfo gps;
    double latitude ;
    double longitude ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        Button FT_mytruckButton = (Button) findViewById(R.id.FT_mytruckButton);
        Button salesButton = (Button) findViewById(R.id.salesButton);
        Button settingButton = (Button) findViewById(R.id.settingButton);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gps = new GpsInfo(Main2Activity.this);
                // GPS 사용유무 가져오기
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                if (gps.isGetLocation() && latitude>30 && latitude<45 && longitude>120 && longitude<135 ) {
                    Intent searchIntent = new Intent(Main2Activity.this, SearchActivity.class);
                    searchIntent.putExtra("latitude", latitude);
                    searchIntent.putExtra("longitude", longitude);
                    Main2Activity.this.startActivity(searchIntent);
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
            }
        });

        FT_mytruckButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                new cTask().execute();
            }
        });

        salesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent noticeIntent = new Intent(Main2Activity.this, SalesActivity.class);
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
    @Override
    public void onBackPressed() {

        if (pressedTime==0){
            Toast.makeText(Main2Activity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
            pressedTime=System.currentTimeMillis();
        }
        else{
            int seconds = (int)(System.currentTimeMillis()-pressedTime);
            if(seconds>2000){
                pressedTime=0;
            }
            else{
                finish();
            }
        }
    }

    private class cTask extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            MyApplication myApp = (MyApplication) getApplication();
            try {
                result = postJsonToServer(myApp.getcurrentID());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            Intent intent;

            String status = "";
            String name = "";
            String phone = "";
            String area = "";
            String ctg = "";
            String introduction = "";
            String menulist = "";
            String reviewlist = "";

            final MyApplication myApp = (MyApplication) getApplication();

            JSONObject obj = null;
            try {
                obj = new JSONObject(result);

                status = obj.getString("status");
                name = obj.getString("name");
                phone = obj.getString("phone");
                area = obj.getString("area");
                ctg = obj.getString("ctg");
                introduction = obj.getString("introduction");

                JSONArray menuarr  = obj.getJSONArray("menulist");
                menulist = menuarr.toString();

                JSONArray reviewarr  = obj.getJSONArray("reviewlist");
                reviewlist = reviewarr.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (status){
                case "0":
                    // 등록된 푸드트럭 있을 시, 기존 정보 global 저장
                    //Log.d("case 0", "동일한 푸드트럭");
                    myApp.setTempFTname(name);
                    myApp.setTempFTphone(phone);
                    myApp.setTempFTarea(area);
                    myApp.setTempFTctg(ctg);
                    myApp.setTempFTintro(introduction);

                    gps = new GpsInfo(Main2Activity.this);
                    // GPS 사용유무 가져오기
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    if (gps.isGetLocation() && latitude>30 && latitude<45 && longitude>120 && longitude<135 ) {
                        intent = new Intent(Main2Activity.this, FT_InfoActivity.class);
                        intent.putExtra("MenuList", menulist);
                        intent.putExtra("ReviewList", reviewlist);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);

                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);

                        Main2Activity.this.startActivity(intent);
                    } else {
                        // GPS 를 사용할수 없으므로
                        gps.showSettingsAlert();
                    }
                    break;
                case "-1":
                    // 등록된 푸드트럭 없을 시, 새로 등록 페이지 오픈, global 전화번호 -1인 상태 유지
                    //Log.d("case -1", "노존재");
                    intent = new Intent(Main2Activity.this, FT_CreateActivity.class);
                    Main2Activity.this.startActivity(intent);
                    break;
            }
        }
    }

    public String postJsonToServer(String currentID) throws IOException {


        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("id", currentID));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient= new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/foodtruck_enroll");

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
