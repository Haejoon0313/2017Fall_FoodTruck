package com.example.john.foodtruck;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
    String ftName = "";
    String ftPhone = "";
    String ftArea = "";
    String ftCtg = "";
    String ftIntro = "";
    String currentID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__create);

        final MyApplication myApp = (MyApplication) getApplication();

        final EditText nameText = (EditText) findViewById(R.id.nameText);
        final EditText phoneText = (EditText) findViewById(R.id.phoneText);
        final EditText introText = (EditText) findViewById(R.id.introText);

        if(!myApp.getTempFTphone().equals("-1")){
            nameText.setText(myApp.getTempFTname());
            phoneText.setText(myApp.getTempFTphone());
            introText.setText(myApp.getTempFTintro());
        }

        currentID = myApp.getcurrentID();

        String[] tempArea = getResources().getStringArray(R.array.areaSpinnerArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tempArea);
        final Spinner areaSpinner = (Spinner) findViewById(R.id.areaSpinner);
        areaSpinner.setAdapter(adapter);

        if(!myApp.getTempFTphone().equals("-1")){
            areaSpinner.setSelection(myApp.getTempFTarea());
        }

        areaSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ftArea = String.valueOf(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        String[] tempCtg = getResources().getStringArray(R.array.ctgSpinnerArray);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tempCtg);
        final Spinner ctgSpinner = (Spinner) findViewById(R.id.ctgSpinner);
        ctgSpinner.setAdapter(adapter2);

        if(!myApp.getTempFTphone().equals("-1")){
            ctgSpinner.setSelection(myApp.getTempFTctg());
        }

        ctgSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ftCtg = String.valueOf(position);
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
                r = postJsonToServer(ftName, ftPhone, ftArea, ftCtg, ftIntro, currentID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }

        @Override
        protected void onPostExecute(Integer result) {
            AlertDialog.Builder alert = new AlertDialog.Builder(FT_CreateActivity.this);

            switch (result){
                case 0 :
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(FT_CreateActivity.this, Main2Activity.class);
                            FT_CreateActivity.this.startActivity(intent);
                        }
                    });
                    alert.setMessage("푸드트럭 정보가 저장되었습니다.");
                    alert.show();
                    break;
                case 1:
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("이미 등록된 매장 번호입니다.");
                    alert.show();
                    break;
                case 2:
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("주 활동 지역을 선택하여 주십시오.");
                    alert.show();
                    break;
                case 3:
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("모든 칸을 입력하여 주십시오.");
                    alert.show();
                    break;
                case 4:
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("카테고리를 선택하여 주십시오.");
                    alert.show();
                    break;
            }
        }
    }

    public int postJsonToServer(String name, String phone, String area, String ctg, String introduction, String currentID) throws IOException {

        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("name", name));
        registerInfo.add(new BasicNameValuePair("phone", phone));
        registerInfo.add(new BasicNameValuePair("area", area));
        registerInfo.add(new BasicNameValuePair("ctg", ctg));
        registerInfo.add(new BasicNameValuePair("introduction", introduction));
        registerInfo.add(new BasicNameValuePair("id", currentID));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient = new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/foodtruck_enroll");

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
                //Log.d("성", responseString);
                return 0;
            } else if (responseString.contains("1")) {
                //Log.d("휴대폰", responseString);
                return 1;
            } else if (responseString.contains("2")) {
                //Log.d("지역", responseString);
                return 2;
            } else if (responseString.contains("3")) {
                //Log.d("빈칸", responseString);
                return 3;
            } else if (responseString.contains("4")) {
                //Log.d("카테고리", responseString);
                return 4;
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


