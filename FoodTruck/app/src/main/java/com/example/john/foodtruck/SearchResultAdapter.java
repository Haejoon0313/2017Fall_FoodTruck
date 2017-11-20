package com.example.john.foodtruck;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by John on 2017-11-18.
 */

public class SearchResultAdapter extends BaseAdapter {

    private Context context;
    private List<SearchResult> resultList;

    public SearchResultAdapter(Context context, List<SearchResult> resultList) {
        this.context = context;
        this.resultList=resultList;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int i) {
        return resultList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.search_result, null);
        TextView ctgText = (TextView) v.findViewById(R.id.ctgText);
        TextView nameText = (TextView) v.findViewById(R.id.nameText);
        TextView phoneText = (TextView) v.findViewById(R.id.phoneText);

        String[] tempCtg = context.getResources().getStringArray(R.array.ctgSpinnerArray);

        ctgText.setText(tempCtg[Integer.parseInt(resultList.get(i).getCtg())]);
        nameText.setText(resultList.get(i).getName());
        phoneText.setText(resultList.get(i).getPhone());

        return v;
    }

}
