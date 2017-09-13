package com.example.android.movies1;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mihirnewalkar on 9/8/17.
 */

public class ReviewsAdapter extends ArrayAdapter<Reviews> {

    private List<Reviews> mReviewsList;
    private static final String LOG_TAG = ReviewsAdapter.class.getSimpleName();

    public ReviewsAdapter(Activity context, ArrayList<Reviews>reviews) {
        super(context,0,reviews);
        mReviewsList = reviews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_reviews, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Log.i(LOG_TAG,Integer.toString(position));
        Reviews currentReviews = getItem(position);

        TextView reviewTv = (TextView) listItemView.findViewById(R.id.review_text_view);
        String sReview = currentReviews.getContent();

        TextView reviewAuthorTv = (TextView) listItemView.findViewById(R.id.review_author_text_view);
        String sAuthor = currentReviews.getAuthor();

        reviewAuthorTv.setText(sAuthor);
        reviewTv.setText(sReview);

        Log.i ("sReview =",sReview);

        return listItemView;
    }

    void swapReviewsData(List<Reviews> reviews) {
        mReviewsList.clear();
        mReviewsList.addAll(reviews);
        notifyDataSetChanged();
    }
}