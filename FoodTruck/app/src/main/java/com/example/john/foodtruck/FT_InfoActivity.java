package com.example.john.foodtruck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;


public class FT_InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__info);

        final MyApplication myApp = (MyApplication) getApplication();

        final TextView FT_info_name = (TextView) findViewById(R.id.FT_info_name);
        final TextView FT_info_phone = (TextView) findViewById(R.id.FT_info_phone);
        final TextView FT_info_area = (TextView) findViewById(R.id.FT_info_area);
        final TextView FT_info_ctg = (TextView) findViewById(R.id.FT_info_ctg);
        final TextView FT_info_intro = (TextView) findViewById(R.id.FT_info_intro);

        FT_info_name.setText(myApp.getTempFTname());
        FT_info_phone.setText(myApp.getTempFTphone());
        FT_info_intro.setText(myApp.getTempFTintro());

        String[] tempArea = getResources().getStringArray(R.array.areaSpinnerArray);
        FT_info_area.setText(tempArea[myApp.getTempFTarea()]);

        String[] tempCtg = getResources().getStringArray(R.array.ctgSpinnerArray);
        FT_info_ctg.setText(tempCtg[myApp.getTempFTctg()]);


        Button myMenuButton = (Button) findViewById(R.id.myMenuButton);
        Button FT_editButton = (Button) findViewById(R.id.FT_editButton);

        myMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuIntent = new Intent(FT_InfoActivity.this, FT_MenuModifyActivity.class);
                FT_InfoActivity.this.startActivity(menuIntent);
            }
        });

        FT_editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(FT_InfoActivity.this, FT_CreateActivity.class);
                FT_InfoActivity.this.startActivity(editIntent);
            }
        });
    }
}
