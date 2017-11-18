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
        TextView areaText = (TextView) v.findViewById(R.id.areaText);
        TextView idText = (TextView) v.findViewById(R.id.idText);
        TextView introText = (TextView) v.findViewById(R.id.introText);
        TextView nameText = (TextView) v.findViewById(R.id.nameText);
        TextView phoneText = (TextView) v.findViewById(R.id.phoneText);

        areaText.setText("지역 : "+resultList.get(i).getArea());
        idText.setText("아이디 : "+resultList.get(i).getId());
        introText.setText("소개 : "+resultList.get(i).getIntro());
        nameText.setText("이름 : "+resultList.get(i).getName());
        phoneText.setText("전화번호 : "+resultList.get(i).getPhone());

        return v;
    }
}
