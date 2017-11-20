package com.example.john.foodtruck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FT_MenuViewActivity extends AppCompatActivity {

    private ListView FT_menuview_list;
    private MenuAdapter adapter;
    private List<FT_MenuList> menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__menu_view);

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
    }
}
