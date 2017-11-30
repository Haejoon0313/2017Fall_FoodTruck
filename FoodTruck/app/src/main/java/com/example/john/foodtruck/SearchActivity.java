package com.example.john.foodtruck;

import android.app.FragmentManager;
import android.graphics.Point;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.app.SearchableInfo;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback{
    GoogleMap mMap;
    double latitude;
    double longitude;
    String keyword="";
    String location = "";
    String distance="";
    String lat = "";
    String lon = "";
    int updatedValue = 500;
    TextView distanceText;
    SeekBar distanceSeekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final EditText keywordText = (EditText) findViewById(R.id.keywordText);
        final Button searchButton = (Button) findViewById(R.id.searchButton);

        Button backButton = (Button) findViewById(R.id.backButton);
        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("FoodTruck");

        distanceSeekbar = (SeekBar) findViewById(R.id.seekBar);
        distanceText = (TextView) findViewById(R.id.distanceText);
        distanceText.setText("주변 "+initSeekbar(distanceSeekbar)+"M 검색");

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();

        latitude=intent.getDoubleExtra("latitude",100);
        longitude=intent.getDoubleExtra("longitude",100);

        searchButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                lat = Double.toString(latitude);
                lon = Double.toString(longitude);
                location="("+lat+","+lon+")";
                keyword = keywordText.getText().toString();
                distance = Integer.toString(initSeekbar(distanceSeekbar));
                new sTask().execute();
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
                Intent loginintent =  new Intent(SearchActivity.this, LoginActivity.class);
                SearchActivity.this.startActivity(loginintent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        Marker marker1;
        mMap=map;
        LatLng location = new LatLng(latitude,longitude );

        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
        markerOptions.title("현재 위치 주변 검색");
        marker1=mMap.addMarker(markerOptions);
        marker1.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Marker marker1;
                Point screenPt = mMap.getProjection().
                        toScreenLocation(latLng);
                LatLng a = mMap.getProjection().
                        fromScreenLocation(screenPt);
                mMap.clear();
                markerOptions.position(a) ;
                markerOptions.title("변경된 위치 주변 검색");
                marker1=mMap.addMarker(markerOptions);
                marker1.showInfoWindow();
                latitude=a.latitude;
                longitude=a.longitude;
            }
        });
    }


    private class sTask extends AsyncTask<String, Void, String> {
        String r = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                r = postJsonToServer(location,keyword,distance);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }
        @Override
        protected void onPostExecute(String result) {

            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
            intent.putExtra("결과",result);
            startActivity(intent);
        }
    }

    public int initSeekbar(SeekBar seekbar){
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    if (i <= 4) {
                        updatedValue = (i * 100) + 100;
                    } else {
                        updatedValue = (i - 3) * 500;
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distanceText.setText("주변 "+initSeekbar(distanceSeekbar)+"M 검색");
            }
        });
        return updatedValue;
    }
    public String postJsonToServer(String location, String keyword, String distance) throws IOException {

        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("location", location));
        registerInfo.add(new BasicNameValuePair("keyword", keyword));
        registerInfo.add(new BasicNameValuePair("distance", distance));

        // 연결 HttpClient 객체 생성
        HttpClient httpClient= new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/search");

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

