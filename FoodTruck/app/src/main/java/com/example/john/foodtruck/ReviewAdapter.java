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

public class ReviewAdapter extends BaseAdapter {

    private Context context;
    private List<FT_ReviewList> reviewList;

    public ReviewAdapter(Context context, List<FT_ReviewList> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }


    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Object getItem(int i) {
        return reviewList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.foodtruck_review, null);

        TextView FT_review_writer = (TextView) v.findViewById(R.id.FT_review_writer);
        RatingBar FT_review_star = (RatingBar) v.findViewById(R.id.FT_review_rating);
        TextView FT_review_detail = (TextView) v.findViewById(R.id.FT_review_detail);

        FT_review_writer.setText("ID: "+reviewList.get(i).getW_id());
        FT_review_star.setRating(Integer.parseInt(reviewList.get(i).getRating()));
        FT_review_detail.setText(reviewList.get(i).getDetail());

        return v;
    }
}
