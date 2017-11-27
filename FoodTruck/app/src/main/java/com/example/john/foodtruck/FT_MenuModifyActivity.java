package com.example.john.foodtruck;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class FT_MenuModifyActivity extends AppCompatActivity {

    String menuName = "";
    String menuPrice = "";
    String menuIngredients = "";
    String currentID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__menu_modify);

        final EditText nameText = (EditText) findViewById(R.id.nameText);
        final EditText priceText = (EditText) findViewById(R.id.priceText);
        final EditText ingredientsText = (EditText) findViewById(R.id.ingredientsText);

        final MyApplication myApp = (MyApplication) getApplication();
        currentID = myApp.getcurrentID();


        Button registerBtn = (Button) findViewById(R.id.registerButton);
        Button backButton = (Button) findViewById(R.id.backButton);
        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("FoodTruck");

        registerBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                menuName = nameText.getText().toString();
                menuPrice = priceText.getText().toString();
                menuIngredients = ingredientsText.getText().toString();

                new rTask().execute();
            }
        });

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
                Intent loginintent =  new Intent(FT_MenuModifyActivity.this, LoginActivity.class);
                FT_MenuModifyActivity.this.startActivity(loginintent);
                finish();
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
                r = postJsonToServer(menuName, menuPrice, menuIngredients, currentID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent;
            AlertDialog.Builder alert = new AlertDialog.Builder(FT_MenuModifyActivity.this);

            String status = "";
            String menulist = "";

            JSONObject obj = null;
            try {
                obj = new JSONObject(result);

                status = obj.getString("status");

                JSONArray arr  = obj.getJSONArray("menulist");
                menulist = arr.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (status){
                case "0" :
                    intent = new Intent(FT_MenuModifyActivity.this, FT_MenuViewActivity.class);
                    intent.putExtra("MenuList", menulist);
                    FT_MenuModifyActivity.this.startActivity(intent);
                    finish();
                    break;
                case "1":
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("이미 등록된 메뉴 이름입니다.");
                    alert.show();
                    break;
                case "2":
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

    public String postJsonToServer(String menuName, String menuPrice, String menuIngredients, String currentID) throws IOException {

        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("name", menuName));
        registerInfo.add(new BasicNameValuePair("price", menuPrice));
        registerInfo.add(new BasicNameValuePair("ingredients", menuIngredients));
        registerInfo.add(new BasicNameValuePair("id", currentID));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient = new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/menu_enroll");

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
