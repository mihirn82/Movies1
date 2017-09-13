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
 * Created by mihirnewalkar on 9/10/17.
 */

public class TrailersAdapter extends ArrayAdapter<Trailers> {

    private List<Trailers> mTrailersList;
    private static final String LOG_TAG = TrailersAdapter.class.getSimpleName();

    public TrailersAdapter(Activity context, ArrayList<Trailers> trailers) {
        super(context,0,trailers);
        mTrailersList = trailers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_trailers, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Log.i(LOG_TAG,Integer.toString(position));
        Trailers currentTrailers = getItem(position);

        TextView trailerTv = (TextView) listItemView.findViewById(R.id.trailer_text_view);
        int trailerNo = position + 1;
        String sTrailer = "Trailer " + trailerNo;

        trailerTv.setText(sTrailer);
        Log.i ("sTrailer =",sTrailer,null);

        return listItemView;
    }

    void swapTrailersData(List<Trailers> trailers) {
        mTrailersList.clear();
        mTrailersList.addAll(trailers);
        notifyDataSetChanged();
    }
}
