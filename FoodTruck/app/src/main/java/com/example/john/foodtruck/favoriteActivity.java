package com.example.john.foodtruck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class favoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        Button backButton = (Button) findViewById(R.id.backButton);
        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("FoodTruck");

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
                Intent loginintent =  new Intent(favoriteActivity.this, LoginActivity.class);
                favoriteActivity.this.startActivity(loginintent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
