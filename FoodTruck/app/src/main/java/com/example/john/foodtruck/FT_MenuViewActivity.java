package com.example.john.foodtruck;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FT_MenuViewActivity extends AppCompatActivity {

    private ListView FT_menuview_list;
    private MenuAdapter adapter;
    private List<FT_MenuList> menuList;
    String currentID = "";
    String menuToRemove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__menu_view);

        final MyApplication myApp = (MyApplication) getApplication();
        currentID = myApp.getcurrentID();

        Intent intent = getIntent();
        final String FT_info_menulist = intent.getStringExtra("MenuList");

        FT_menuview_list = (ListView) findViewById(R.id.FT_menuview_list);
        menuList = new ArrayList<FT_MenuList>();

        try {
            JSONArray menuarr = new JSONArray(FT_info_menulist);

            String name, price, ingredients;

            for (int i = 0; i < menuarr.length(); i++){
                name = menuarr.getJSONObject(i).getString("name");
                price = menuarr.getJSONObject(i).getString("price");
                ingredients= menuarr.getJSONObject(i).getString("ingredients");

                menuList.add(new FT_MenuList(name, price, ingredients));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new MenuAdapter(getApplicationContext(), menuList);
        FT_menuview_list.setAdapter(adapter);

        Button FT_menuenrollButton = (Button) findViewById(R.id.FT_menuview_enroll);

        FT_menuenrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(FT_MenuViewActivity.this, FT_MenuModifyActivity.class);
                FT_MenuViewActivity.this.startActivity(editIntent);
            }
        });
        Button successButton = (Button) findViewById(R.id.successButton);
        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent editIntent = new Intent(FT_MenuViewActivity.this, Main2Activity.class);
                FT_MenuViewActivity.this.startActivity(editIntent);
                Toast.makeText(getApplicationContext(),"메뉴 수정이 완료되었습니다",Toast.LENGTH_LONG).show();
            }
        });

        FT_menuview_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(FT_MenuViewActivity.this);
                alertDlg.setTitle("알림");

                // '예' 버튼이 클릭되면
                alertDlg.setPositiveButton("삭제", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        menuToRemove=menuList.get(position).getName();
                        menuList.remove(position);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();  // AlertDialog를 닫는다.
                        new sTask().execute();
                        //adapter = new MenuAdapter(getApplicationContext(), menuList);
                        //FT_menuview_list.setAdapter(adapter);
                    }
                });

                // '아니오' 버튼이 클릭되면
                alertDlg.setNegativeButton("취소", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        dialog.dismiss();  // AlertDialog를 닫는다.
                    }
                });
                alertDlg.setMessage("해당 메뉴를 삭제하시겠습니까?");
                alertDlg.show();
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent editIntent = new Intent(FT_MenuViewActivity.this, Main2Activity.class);
        FT_MenuViewActivity.this.startActivity(editIntent);
        Toast.makeText(getApplicationContext(),"메뉴 수정이 완료되었습니다",Toast.LENGTH_LONG).show();
    }
    private class sTask extends AsyncTask<String, Void, Void> {
        String r = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                r = postJsonToServer(currentID,menuToRemove);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public String postJsonToServer(String currentID, String menu) throws IOException {

        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("id", currentID));
        registerInfo.add(new BasicNameValuePair("name", menu));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient= new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/menu_remove");

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
