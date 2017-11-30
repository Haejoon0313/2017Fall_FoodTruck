package com.example.john.foodtruck;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.List;

/**
 * Created by John on 2017-11-18.
 */

public class SearchResultActivity extends AppCompatActivity {
    String f_id1="";
    String area1,introduction1,name1,phone1,ctg1,reviewlist1,menulist1;
    String photoText="";
    String check_favorite = "";
    private ListView listView;
    private SearchResultAdapter adapter;
    private List<SearchResult> resultList;
    private TextView resultCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        String result = intent.getStringExtra("결과");

        String menulist="";
        String reviewlist="";

        listView = (ListView) findViewById(R.id.searchResult);
        resultCount = (TextView) findViewById(R.id.resultCount);

        Button backButton = (Button) findViewById(R.id.backButton);
        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("FoodTruck");

        resultList = new ArrayList<SearchResult>();

        try {
            JSONObject resultJson = new JSONObject(result);

            String sta = resultJson.getString("status");
            JSONArray arr = resultJson.getJSONArray("data");
            String area,id,intro,name,phone,ctg,distance;
            resultCount.setText("검색결과 : "+sta+" 개");

            for (int i = 0; i < arr.length(); i++){
                area = arr.getJSONObject(i).getString("area");
                id= arr.getJSONObject(i).getString("id");
                intro= arr.getJSONObject(i).getString("introduction");
                name= arr.getJSONObject(i).getString("name");
                phone= arr.getJSONObject(i).getString("phone");
                ctg= arr.getJSONObject(i).getString("ctg");
                distance= arr.getJSONObject(i).getString("distance");

                JSONArray menuarr  = arr.getJSONObject(i).getJSONArray("menulist");
                menulist = menuarr.toString();
                Log.d("menu", menulist);

                JSONArray reviewarr  = arr.getJSONObject(i).getJSONArray("reviewlist");
                reviewlist = reviewarr.toString();
                Log.d("review", reviewlist);

                resultList.add(new SearchResult(area,id,intro,name,phone, ctg,menulist,reviewlist,distance));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new SearchResultAdapter(getApplicationContext(),resultList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                f_id1=resultList.get(position).getId();
                area1=resultList.get(position).getArea();
                introduction1=resultList.get(position).getIntro();
                name1=resultList.get(position).getName();
                phone1=resultList.get(position).getPhone();
                ctg1=resultList.get(position).getCtg();
                reviewlist1=resultList.get(position).getReviewlist();
                menulist1=resultList.get(position).getMenulist();

                new pTask().execute();
                 // 다음넘어갈 화면
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
                Intent loginintent =  new Intent(SearchResultActivity.this, LoginActivity.class);
                SearchResultActivity.this.startActivity(loginintent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    private class pTask extends AsyncTask<String, Void, String> {
        String r = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            MyApplication myApp = (MyApplication) getApplication();
            try {
                r = postJsonToServer(f_id1, myApp.getcurrentID());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }
        @Override
        protected void onPostExecute(String result) {

            JSONObject obj = null;

            try {
                obj = new JSONObject(result);

                photoText = obj.getString("photo");
                check_favorite = obj.getString("check");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(SearchResultActivity.this,SearchFTInfoActivity.class);
            intent.putExtra("area", area1);
            intent.putExtra("id", f_id1);
            intent.putExtra("introduction", introduction1);
            intent.putExtra("name", name1);
            intent.putExtra("phone",phone1);
            intent.putExtra("ctg", ctg1);
            intent.putExtra("reviewlist", reviewlist1);
            intent.putExtra("menulist", menulist1);
            intent.putExtra("photo", photoText);
            intent.putExtra("favorite", check_favorite);
            startActivity(intent);
        }
    }

    public String postJsonToServer(String f_id, String u_id) throws IOException {

        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("f_id", f_id));
        registerInfo.add(new BasicNameValuePair("u_id", u_id));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient= new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/fd_photo");

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
