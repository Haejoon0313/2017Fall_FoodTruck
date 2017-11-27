package com.example.john.foodtruck;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

import java.io.IOException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private long pressedTime;
    String userID= "";
    String userPassword="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final MyApplication myApp = (MyApplication) getApplication();

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);

        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final Button registerButton = (Button) findViewById(R.id.registerButton);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("로그인");

        loginButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                userID = idText.getText().toString();
                userPassword = passwordText.getText().toString();
                myApp.setcurrentID(userID);
                new lTask().execute();
            }
        });

        registerButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });


    }

    @Override
    public void onBackPressed() {

        if (pressedTime==0){
            Toast.makeText(LoginActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
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

    private class lTask extends AsyncTask<String, Void, Integer> {

        int r = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            try {
                r = postJsonToServer(userID,userPassword);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }


        @Override
        protected void onPostExecute(Integer result) {
            AlertDialog.Builder builder;
            Intent intent;
            switch (result){
                case 0 :
                    //myApp.setcurrentID(userID);
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                    finish();
                    break;
                case 1 :
                    builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("모든 칸을 입력해주십시오.")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    break;
                case 2 :
                    builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("존재하지 않는 아이디입니다.")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    break;
                case 3 :
                    builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("패스워드가 틀립니다.")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    break;
                case 4 :
                    //myApp.setcurrentID(userID);
                    intent = new Intent(LoginActivity.this, Main2Activity.class);
                    LoginActivity.this.startActivity(intent);
                    finish();
                    break;
            }
        }
    }

    public int postJsonToServer(String id, String pw) throws IOException {


        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("id", id));
        registerInfo.add(new BasicNameValuePair("password", pw));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient= new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/login");

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

            if (responseString.contains("0")){
                return 0;
            }
            else if (responseString.contains("1")){
                return 1;
            }
            else if (responseString.contains("2")){
                return 2;
            }
            else if (responseString.contains("3")){
                return 3;
            }
            else if (responseString.contains("4")){
                return 4;
            }
            else{
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
