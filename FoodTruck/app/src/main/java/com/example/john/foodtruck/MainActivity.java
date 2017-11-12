package com.example.john.foodtruck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button FTButton = (Button) findViewById(R.id.FT_create);

        FTButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent FTcreateIntent = new Intent(MainActivity.this, FT_CreateActivity.class);
                MainActivity.this.startActivity(FTcreateIntent);
            }
        });
    }
}
