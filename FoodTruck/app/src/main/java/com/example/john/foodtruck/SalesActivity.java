package com.example.john.foodtruck;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SalesActivity extends AppCompatActivity {

    private ListView salesMenu;
    private SalesMenuAdapter adapter;
    private List<SalesMenuList> salesmenuList;

    String id = "";
    String location = "";
    String status = "";
    String total_price = "";

    private GpsInfo gps;
    double latitude ;
    double longitude ;
    String lat;
    String lon;
    int temp_price = 0;
    int temp_nums = 0;
    int temp_total_price = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        Intent intent = getIntent();
        final String menulist = intent.getStringExtra("MenuList");

        MyApplication myApp = (MyApplication) getApplication();
        id = myApp.getcurrentID();

        salesMenu = (ListView) findViewById(R.id.salesMenuList);
        salesmenuList = new ArrayList<SalesMenuList>();
        Button salesEndButton = (Button) findViewById(R.id.salesEndButton);

        try {
            JSONArray menuarr = new JSONArray(menulist);

            String name, price, ingredients;
            String nums = "0";

            for (int i = 0; i < menuarr.length(); i++){
                name = menuarr.getJSONObject(i).getString("name");
                price = menuarr.getJSONObject(i).getString("price");
                ingredients= menuarr.getJSONObject(i).getString("ingredients");


                salesmenuList.add(new SalesMenuList(name, price, ingredients, nums));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new SalesMenuAdapter(getApplicationContext(), salesmenuList);
        salesMenu.setAdapter(adapter);

        salesEndButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                gps = new GpsInfo(SalesActivity.this);
                // GPS 사용유무 가져오기
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                if (gps.isGetLocation() && latitude>30 && latitude<45 && longitude>120 && longitude<135 ) {
                    lat = Double.toString(latitude);
                    lon = Double.toString(longitude);
                    location = "(" + lat + "," + lon + ")";

                    status = "0";

                    for (int i=0; i < salesmenuList.size(); i++){
                        temp_price = Integer.parseInt(salesmenuList.get(i).getPrice());
                        temp_nums = Integer.parseInt(salesmenuList.get(i).getNums());

                        temp_total_price += (temp_price * temp_nums);
                    }

                    total_price = String.valueOf(temp_total_price);

                    new sTask().execute();
                }
            }
        });
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
                r = postJsonToServer(id, location, status, total_price);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }

        @Override
        protected void onPostExecute(Integer result) {
            AlertDialog.Builder alert = new AlertDialog.Builder(SalesActivity.this);

            switch (result){
                case 0 :
                    // 영업 종료
                    Toast.makeText(SalesActivity.this, "영업이 종료되었습니다.", Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 1:
                    // 영업 시작
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("영업이 중복 시작되는 에러가 발생하였습니다.");
                    alert.show();
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

    public int postJsonToServer(String id, String location, String status, String total_price) throws IOException {

        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("id", id));
        registerInfo.add(new BasicNameValuePair("location", location));
        registerInfo.add(new BasicNameValuePair("status", status));
        registerInfo.add(new BasicNameValuePair("total_price", total_price));

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
                // 영업 종료 status
                return 0;
            } else if (responseString.contains("1")) {
                // 영업 시작 status
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
