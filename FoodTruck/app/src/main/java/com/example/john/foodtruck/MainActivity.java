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
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {
    private long pressedTime;
    private GpsInfo gps;
    double latitude;
    double longitude;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        Button favoriteButton = (Button) findViewById(R.id.FT_mytruckButton);
        Button noticeButton = (Button) findViewById(R.id.noticeButton);
        Button settingButton = (Button) findViewById(R.id.settingButton);

        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("FoodTruck");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gps = new GpsInfo(MainActivity.this);
                // GPS 사용유무 가져오기
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                if (gps.isGetLocation() && latitude>30 && latitude<45 && longitude>120 && longitude<135 ) {
                    new searchTask().execute();

                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }

            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gps = new GpsInfo(MainActivity.this);
                // GPS 사용유무 가져오기
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                if (gps.isGetLocation() && latitude>30 && latitude<45 && longitude>120 && longitude<135 ) {

                    location = "("+Double.toString(latitude)+","+Double.toString(longitude)+")";

                    new fTask().execute();

                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
            }
        });

        noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent noticeIntent = new Intent(MainActivity.this, NoticeActivity.class);
                MainActivity.this.startActivity(noticeIntent);
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                MainActivity.this.startActivity(settingIntent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MyApplication myApp = (MyApplication) getApplication();
                myApp.setcurrentID("");
                Intent loginintent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(loginintent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (pressedTime == 0) {
            Toast.makeText(MainActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis();
        } else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);
            if (seconds > 2000) {
                pressedTime = 0;
            } else {
                finish();
            }
        }
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
                result = postJsonToServer(myApp.getcurrentID(), location);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            Intent intent;

            String status = "";
            String dataStr = "";

            final MyApplication myApp = (MyApplication) getApplication();

            JSONObject obj = null;

            try {
                obj = new JSONObject(result);

                status = obj.getString("status");

                JSONArray data  = obj.getJSONArray("data");
                dataStr=data.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (status){
                case "0":
                    // 성공

                    intent = new Intent(MainActivity.this, favoriteActivity.class);

                    intent.putExtra("data", dataStr);

                    MainActivity.this.startActivity(intent);
                    break;
                case "-1":
                    // 에러
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

    public String postJsonToServer(String currentID, String location) throws IOException {


        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("id", currentID));
        registerInfo.add(new BasicNameValuePair("location", location));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient= new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/favorite_list");

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

    private class searchTask extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = postJsonToServerL("("+Double.toString(latitude)+","+Double.toString(longitude)+")");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
            searchIntent.putExtra("latitude", latitude);
            searchIntent.putExtra("longitude", longitude);
            searchIntent.putExtra("keyword",result);
            MainActivity.this.startActivity(searchIntent);
        }
    }

    public String postJsonToServerL(String location) throws IOException {


        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("location", location));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient= new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/keyword_recommend");

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