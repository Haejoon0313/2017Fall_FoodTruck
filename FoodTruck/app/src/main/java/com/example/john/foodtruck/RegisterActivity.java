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
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

public class RegisterActivity extends AppCompatActivity {
    String userID= "";
    String userPassword="";
    String userName="";
    String userNumber="";
    String userType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final EditText nameText = (EditText) findViewById(R.id.reviewDetail);
        final EditText numberText = (EditText) findViewById(R.id.numberText);
        final RadioGroup rg = (RadioGroup) findViewById(R.id.rg);
        final RadioButton sellerbutton = (RadioButton) findViewById(R.id.seller);
        rg.check(sellerbutton.getId());

        Button registerBtn = (Button) findViewById(R.id.registerButton);
        registerBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                userID = idText.getText().toString();
                userPassword = passwordText.getText().toString();
                userName = nameText.getText().toString();
                userNumber = numberText.getText().toString();
                int id = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);


                switch (rb.getText().toString()) {
                    case "판매자":
                        userType = "Seller";
                        break;
                    case "구매자":
                        userType = "Purchaser";
                        break;
                }
                new rTask().execute();
            }
        });
    }


    private class rTask extends AsyncTask<String, Void, Integer> {
        int r = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            try {
                r = postJsonToServer(userID,userPassword,userName,userNumber,userType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }
        @Override
        protected void onPostExecute(Integer result) {
            AlertDialog.Builder builder;
            switch (result){
                case 0 :
                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("회원 등록에 성공하셨습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    RegisterActivity.this.startActivity(intent);
                                }
                            })
                            .create()
                            .show();
                    break;
                case 1 :
                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("모든 칸을 입력해주십시오.")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    break;
                case 2 :
                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("중복된 아이디입니다.")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    break;
                case 3 :
                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("이미 등록된 사용자입니다.")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    break;
                }
        }
    }
    public int postJsonToServer(String id, String pw, String name, String phone, String type) throws IOException {

        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("id", id));
        registerInfo.add(new BasicNameValuePair("password", pw));
        registerInfo.add(new BasicNameValuePair("name", name));
        registerInfo.add(new BasicNameValuePair("phone", phone));
        registerInfo.add(new BasicNameValuePair("type", type));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient= new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/sign_in");

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
            else{
                return 3;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}