package com.example.john.foodtruck;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 2017-11-18.
 */

public class SalesMenuAdapter extends BaseAdapter {

    private Context context;
    private List<SalesMenuList> menuList;
    private ArrayList<String> items;

    public SalesMenuAdapter(Context context, List<SalesMenuList> menuList) {
        this.context = context;
        this.menuList = menuList;
    }


    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Object getItem(int i) {
        return menuList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.sales_menu, null);

        TextView FT_menu_name = (TextView) v.findViewById(R.id.FT_info_intro);
        TextView FT_menu_price = (TextView) v.findViewById(R.id.FT_menu_price);
        TextView FT_menu_ingredients = (TextView) v.findViewById(R.id.FT_menu_ingredients);
        final TextView sellNumber = (TextView) v.findViewById(R.id.sellNumber);

        Button minusButton = (Button) v.findViewById(R.id.minusButton);
        Button plusButton  = (Button) v.findViewById(R.id.plusButton);

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 마이너스
                menuList.get(i).setNums(String.valueOf(Integer.parseInt(menuList.get(i).getNums()) - 1));
                sellNumber.setText(menuList.get(i).getNums());
                //Log.d("minus", menuList.get(i).getNums());
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 플러스
                menuList.get(i).setNums(String.valueOf(Integer.parseInt(menuList.get(i).getNums()) + 1));
                sellNumber.setText(menuList.get(i).getNums());
                //Log.d("plus", menuList.get(i).getNums());
            }
        });

        FT_menu_name.setText(menuList.get(i).getName());
        FT_menu_price.setText(menuList.get(i).getPrice()+" 원");
        FT_menu_ingredients.setText(menuList.get(i).getIngredients());
        sellNumber.setText(menuList.get(i).getNums());
        //Log.d("sellNumber", menuList.get(i).getNums());
        return v;
    }
}
