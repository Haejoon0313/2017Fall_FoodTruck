package com.example.john.foodtruck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 2017-11-18.
 */

public class SearchResultActivity extends AppCompatActivity {

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

        listView = (ListView) findViewById(R.id.searchResult);
        resultCount = (TextView) findViewById(R.id.resultCount);
        resultList = new ArrayList<SearchResult>();

        try {
            JSONObject resultJson = new JSONObject(result);

            String sta = resultJson.getString("status");
            JSONArray arr = resultJson.getJSONArray("data");

            String area,id,intro,name,phone;
            resultCount.setText("결과 : "+sta+" 개");

            for (int i = 0; i < arr.length(); i++){
                area = arr.getJSONObject(i).getString("area");
                id= arr.getJSONObject(i).getString("id");
                intro= arr.getJSONObject(i).getString("introduction");
                name= arr.getJSONObject(i).getString("name");
                phone= arr.getJSONObject(i).getString("phone");

                resultList.add(new SearchResult(area,id,intro,name,phone));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new SearchResultAdapter(getApplicationContext(),resultList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchResultActivity.this,SearchFTInfoActivity.class); // 다음넘어갈 화면

                intent.putExtra("area", resultList.get(position).getArea());
                intent.putExtra("id", resultList.get(position).getId());
                intent.putExtra("introduction", resultList.get(position).getIntro());
                intent.putExtra("name", resultList.get(position).getName());
                intent.putExtra("phone", resultList.get(position).getPhone());

                startActivity(intent);

            }
        });
    }
}
