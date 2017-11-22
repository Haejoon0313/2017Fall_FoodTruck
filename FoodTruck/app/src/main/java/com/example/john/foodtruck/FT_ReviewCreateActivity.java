package com.example.john.foodtruck;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

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

public class FT_ReviewCreateActivity extends AppCompatActivity {

    String reviewWriter = "";
    String foodtruckID = "";
    String reviewRating = "";
    String reviewDetail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__review_create);

        final Intent intent = getIntent();

        final String id=intent.getStringExtra("id");

        foodtruckID = id;

        final RatingBar reviewRatingBar = (RatingBar) findViewById(R.id.reviewRating);
        reviewRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int i = (int) v;
                reviewRating = String.valueOf(i);
            }
        });

        final EditText reviewDetailText = (EditText) findViewById(R.id.nameText);

        final MyApplication myApp = (MyApplication) getApplication();
        reviewWriter = myApp.getcurrentID();


        Button registerBtn = (Button) findViewById(R.id.registerButton);
        registerBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                reviewDetail = reviewDetailText.getText().toString();

                new rTask().execute();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    private class rTask extends AsyncTask<String, Void, String> {
        String r;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                r = postJsonToServer(foodtruckID, reviewWriter, reviewRating,  reviewDetail);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }

        @Override
        protected void onPostExecute(String result) {
            final Intent intent = getIntent();

            final String area=intent.getStringExtra("area");
            final String name=intent.getStringExtra("name");
            final String id=intent.getStringExtra("id");
            final String intro=intent.getStringExtra("introduction");
            final String phone=intent.getStringExtra("phone");

            final String FT_menulist = intent.getStringExtra("MenuList");

            Intent editIntent;
            AlertDialog.Builder alert = new AlertDialog.Builder(FT_ReviewCreateActivity.this);

            String status = "";
            String reviewlist = "";

            JSONObject obj = null;
            try {
                obj = new JSONObject(result);

                status = obj.getString("status");

                JSONArray arr  = obj.getJSONArray("reviewlist");
                reviewlist = arr.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (status){
                case "0" :
                    editIntent = new Intent(FT_ReviewCreateActivity.this, SearchFTInfoActivity.class);

                    editIntent.putExtra("area", area);
                    editIntent.putExtra("name", name);
                    editIntent.putExtra("id", id);
                    editIntent.putExtra("introduction", intro);
                    editIntent.putExtra("phone", phone);

<<<<<<< HEAD
                    editIntent.putExtra("MenuList", FT_menulist);
                    editIntent.putExtra("ReviewList", reviewlist);
                    finish();
=======
                    editIntent.putExtra("menulist", FT_menulist);
                    editIntent.putExtra("reviewlist", reviewlist);

>>>>>>> 79f270c3053338cae061a4e768c7564e327214db
                    FT_ReviewCreateActivity.this.startActivity(editIntent);
                    break;
                case "1":
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("모든 칸을 입력하여 주십시오.");
                    alert.show();
                    break;
            }
        }
    }

    public String postJsonToServer(String ft_id, String w_id, String rating, String detail) throws IOException {

        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("ft_id", ft_id));
        registerInfo.add(new BasicNameValuePair("w_id", w_id));
        registerInfo.add(new BasicNameValuePair("rating", rating));
        registerInfo.add(new BasicNameValuePair("detail", detail));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient = new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/review_write");

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
            String responseString;
            responseString= EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

            return responseString;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
