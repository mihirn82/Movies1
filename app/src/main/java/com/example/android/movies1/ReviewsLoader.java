package com.example.android.movies1;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.net.Uri;
import android.util.Log;

import java.util.List;

/**
 * Created by mihirnewalkar on 9/7/17.
 */

public class ReviewsLoader extends AsyncTaskLoader<List<Reviews>> {

    /** Tag for log messages */
    private static final String LOG_TAG = ReviewsLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public ReviewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG,"Inside onStartLoading");
        forceLoad();
    }

    @Override
    public List<Reviews> loadInBackground() {
        Log.v(LOG_TAG,"Inside loadInBackground");
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl == null) {
            return null;
        }
        List <Reviews> reviews = QueryUtils.fetchReviews(mUrl);
        return reviews;
    }
}
