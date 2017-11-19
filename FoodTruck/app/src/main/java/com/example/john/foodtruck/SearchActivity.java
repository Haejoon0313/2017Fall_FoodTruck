package com.example.john.foodtruck;

import android.app.SearchableInfo;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private GpsInfo gps;

    double latitude = 0;
    double longitude = 0;
    String keyword="";
    String location = "";
    String lat = "";
    String lon = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText keywordText = (EditText) findViewById(R.id.keywordText);
        final Button searchButton = (Button) findViewById(R.id.searchButton);

        // GPS 정보를 보여주기 위한 이벤트 클래스 등록
        searchButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                gps = new GpsInfo(SearchActivity.this);
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
/*
                    Toast.makeText(
                            getApplicationContext(),
                            "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                            Toast.LENGTH_LONG).show();
                            */
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }

                lat = Double.toString(latitude);
                lon = Double.toString(longitude);
                location="("+lat+","+lon+")";

                keyword = keywordText.getText().toString();
                new sTask().execute();
            }
        });
    }

    private class sTask extends AsyncTask<String, Void, String> {
        String r = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
/*
            try {
                r = postJsonToServer(location,keyword);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
*/
            return "{\"status\": 3,\"data\": [{\"area\": \"1\",\"id\": \"ddehun1\",\"introduction\": \"이건푸드트럭1\",\"name\":\"박채훈의 푸드트럭1\",\"phone\": \"06123432435123\"},{\"area\": \"2\",\"id\": \"ddehun2\",\"introduction\": \"이건푸키키드트럭2\",\"name\": \"김요한의 푸드트럭2\",\"phone\": \"06133242345234\"},{\"area\": \"3\",\"id\": \"ddehun3\",\"introduction\": \"이건푸키키드트럭3\",\"name\": \"김요한의 푸드트럭3\",\"phone\": \"06133242345345\"}]}";
        }
        @Override
        protected void onPostExecute(String result) {

            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
            intent.putExtra("결과",result);
            startActivity(intent);
        }
    }
    public String postJsonToServer(String location, String keyword) throws IOException {

        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("location", location));
        registerInfo.add(new BasicNameValuePair("keyword", keyword));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient= new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/search");

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

            Log.d("d",responseString);
            return responseString;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

