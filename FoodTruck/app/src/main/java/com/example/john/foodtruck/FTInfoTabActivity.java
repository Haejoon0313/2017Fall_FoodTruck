package com.example.john.foodtruck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class FTInfoTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftinfo_tab);

        Toast.makeText(getApplicationContext(), "상세정보 탭", Toast.LENGTH_LONG).show();

    }
}
