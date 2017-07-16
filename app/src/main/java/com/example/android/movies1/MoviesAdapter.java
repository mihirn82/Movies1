package com.example.android.movies1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mihirnewalkar on 7/4/17.
 */

public class MoviesAdapter extends ArrayAdapter<Movies> {

    private static final String LOG_TAG = Movies.class.getSimpleName();

    private static final String base_url = "http://image.tmdb.org/t/p/w500";

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param movies A List of news objects to display in a list
     */
    public MoviesAdapter(Activity context, ArrayList<Movies> movies) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movies);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Movies currentMovies = getItem(position);

        ImageView titleView = (ImageView) listItemView.findViewById(R.id.title_image_view);
        String sPosterTitle = currentMovies.getPosterTitle();
        sPosterTitle = base_url + sPosterTitle;
        Log.v("MoviesAdapter.java",sPosterTitle);
        Picasso.with(getContext()).load(sPosterTitle).into(titleView);

        // Return the whole list item layout
        // so that it can be shown in the ListView
        return listItemView;
    }
}
