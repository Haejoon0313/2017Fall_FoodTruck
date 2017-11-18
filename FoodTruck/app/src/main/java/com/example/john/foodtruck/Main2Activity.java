package com.example.john.foodtruck;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        Button FT_mytruckButton = (Button) findViewById(R.id.FT_mytruckButton);
        Button noticeButton = (Button) findViewById(R.id.noticeButton);
        Button settingButton = (Button) findViewById(R.id.settingButton);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(Main2Activity.this, SearchActivity.class);
                Main2Activity.this.startActivity(searchIntent);
            }
        });

        FT_mytruckButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                new cTask().execute();
            }
        });

        noticeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent noticeIntent = new Intent(Main2Activity.this, NoticeActivity.class);
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

                    intent = new Intent(Main2Activity.this, FT_InfoActivity.class);
                    Main2Activity.this.startActivity(intent);
                    break;
                case "-1":
                    // 등록된 푸드트럭 없을 시, 새로 등록 페이지 오픈, globla 전화번호 -1인 상태 유지
                    //Log.d("case -1", "노존재");
                    intent = new Intent(Main2Activity.this, FT_CreateActivity.class);
                    Main2Activity.this.startActivity(intent);
                    break;
                default: // 차후 삭제 예정
                    //Log.d("case 42234234", "망");
                    intent = new Intent(Main2Activity.this, FT_InfoActivity.class);
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
