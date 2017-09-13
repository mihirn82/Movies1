package com.example.android.movies1.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.android.movies1.MoviesAdapter;
import com.example.android.movies1.R;
import com.squareup.picasso.Picasso;

import static com.example.android.movies1.data.MoviesContract.*;

/**
 * Created by mihirnewalkar on 9/13/17.
 */

public class MoviesCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = MoviesCursorAdapter.class.getSimpleName();

    private static final String base_url = "http://image.tmdb.org/t/p/w500";


    public MoviesCursorAdapter(Context context, Cursor c) {
        super(context,c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        ImageView titleView = (ImageView) view.findViewById(R.id.title_image_view);

        // Find the columns of product attributes that we're interested in
        int posterColumnIndex = cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_POSTER);

        // Extract properties from cursor
        String sPosterTitle = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_POSTER));
        sPosterTitle = base_url + sPosterTitle;
        Log.v(LOG_TAG,sPosterTitle);
        Picasso.with(context).load(sPosterTitle).into(titleView);
    }
}
