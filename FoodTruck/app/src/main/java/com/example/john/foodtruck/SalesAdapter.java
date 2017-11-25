package com.example.john.foodtruck;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by John on 2017-11-18.
 */

public class SalesAdapter extends BaseAdapter {

    private Context context;
    private List<SalesList> salesList;

    public SalesAdapter(Context context, List<SalesList> salesList) {
        this.context = context;
        this.salesList = salesList;
    }


    @Override
    public int getCount() {
        return salesList.size();
    }

    @Override
    public Object getItem(int i) {
        return salesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.sales_info, null);

        TextView dateText = (TextView) v.findViewById(R.id.dateText);
        TextView timeText = (TextView) v.findViewById(R.id.timeText);
        TextView locationText = (TextView) v.findViewById(R.id.locationText);
        TextView salesText= (TextView) v.findViewById(R.id.saleText);

        dateText.setText(salesList.get(i).getDate());
        timeText.setText(salesList.get(i).getStart()+" ~ "+salesList.get(i).getEnd());
        locationText.setText(salesList.get(i).getLocation());
        salesText.setText(salesList.get(i).getSales()+"원");

        return v;
    }
}
