package com.example.android.movies1;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by mihirnewalkar on 9/7/17.
 */

public class TrailersLoader extends AsyncTaskLoader<List<Trailers>> {

    /** Tag for log messages */
    private static final String LOG_TAG = TrailersLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public TrailersLoader(Context context,String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG,"Inside onStartLoading");
        forceLoad();
    }

    @Override
    public List<Trailers> loadInBackground() {
        Log.v(LOG_TAG,"Inside loadInBackground");
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl == null) {
            return null;
        }
        List <Trailers> trailers = QueryUtils.fetchTrailers(mUrl);
        return trailers;
    }
}
