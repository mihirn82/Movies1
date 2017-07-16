package com.example.android.movies1;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by mihirnewalkar on 7/4/17.
 */

public class MoviesLoader extends AsyncTaskLoader<List<Movies>>{

    /** Tag for log messages */
    private static final String LOG_TAG = MoviesLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link MoviesLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */

    public MoviesLoader(Context context,String url) {
        super(context);
        mUrl = url;

    }

    @Override
    protected void onStartLoading() {
        Log.v("MoviesLoader.java","Inside onStartLoading");
        forceLoad();
    }

    @Override
    public List<Movies> loadInBackground() {

        Log.v("MoviesLoader.java","Inside loadInBackground");
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl == null) {
            return null;
        }

        List<Movies> movies = QueryUtils.fetchMoviesData(mUrl);
        return movies;
    }
}
