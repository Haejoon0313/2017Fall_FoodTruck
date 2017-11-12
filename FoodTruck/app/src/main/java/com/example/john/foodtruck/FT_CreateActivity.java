package com.example.john.foodtruck;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

public class FT_CreateActivity extends AppCompatActivity {
    String ftName="";
    String ftPhone="";
    String ftArea="";
    String ftIntro="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__create);

        final EditText nameText = (EditText) findViewById(R.id.nameText);
        final EditText phoneText = (EditText) findViewById(R.id.phoneText);
        final EditText introText = (EditText) findViewById(R.id.introText);

        String[] tempArea = getResources().getStringArray(R.array.areaSpinnerArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tempArea);
        final Spinner areaSpinner = (Spinner) findViewById(R.id.areaSpinner);
        areaSpinner.setAdapter(adapter);

        areaSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ftArea = String.valueOf(position);
                        Log.d("msg", String.valueOf(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        Button registerBtn = (Button) findViewById(R.id.registerButton);
        registerBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ftName = nameText.getText().toString();
                ftPhone = phoneText.getText().toString();
                ftIntro = introText.getText().toString();

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
                r = postJsonToServer(ftName, ftPhone, ftArea, ftIntro);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }
    }
    public static int postJsonToServer(String name,String phone,String area,String introduction) throws IOException {

        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("name", name));
        registerInfo.add(new BasicNameValuePair("phone", phone));
        registerInfo.add(new BasicNameValuePair("area", area));
        registerInfo.add(new BasicNameValuePair("introduction", introduction));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient= new DefaultHttpClient();

        // server url 받기
        /*String serverURL = getResources().getString(R.string.serverURL);

        HttpPost httpPost = new HttpPost(serverURL + "/foodtruck_enroll");*/

        HttpPost httpPost = new HttpPost("http://143.248.244.24:8081/foodtruck_enroll");

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

            if (responseString.contains("2")){

                Log.d("성", responseString);
                return 1;
            }
            else{
                Log.d("실", responseString);
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
