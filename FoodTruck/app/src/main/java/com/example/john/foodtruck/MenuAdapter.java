package com.example.john.foodtruck;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by John on 2017-11-18.
 */

public class MenuAdapter extends BaseAdapter {

    private Context context;
    private List<FT_MenuList> menuList;

    public MenuAdapter(Context context, List<FT_MenuList> menuList) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.foodtruck_menu, null);

        TextView FT_menu_name = (TextView) v.findViewById(R.id.FT_review_detail);
        TextView FT_menu_price = (TextView) v.findViewById(R.id.FT_menu_price);
        TextView FT_menu_ingredients = (TextView) v.findViewById(R.id.FT_menu_ingredients);

        FT_menu_name.setText(menuList.get(i).getName());
        FT_menu_price.setText(menuList.get(i).getPrice()+"원");
        FT_menu_ingredients.setText(menuList.get(i).getIngredients());

        return v;
    }
}
