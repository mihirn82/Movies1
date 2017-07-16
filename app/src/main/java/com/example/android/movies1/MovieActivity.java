package com.example.android.movies1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import static com.example.android.movies1.MainActivity.KEY_PLOT_SYNOPSIS;
import static com.example.android.movies1.MainActivity.KEY_POSTER_TITLE;
import static com.example.android.movies1.MainActivity.KEY_RELEASE_DATE;
import static com.example.android.movies1.MainActivity.KEY_TITLE;
import static com.example.android.movies1.MainActivity.KEY_VOTE_AVG;
import static java.lang.System.load;

/**
 * Created by mihirnewalkar on 7/15/17.
 */

public class MovieActivity extends AppCompatActivity{

    private static final String base_url = "http://image.tmdb.org/t/p/w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        String title = "";
        String poster = "";
        String release_date = "";
        String vote_average = "";
        String plot_synopsis = "";

        Intent intent = getIntent();

        if (intent != null) {
            title = intent.getStringExtra(KEY_TITLE);
            poster = intent.getStringExtra(KEY_POSTER_TITLE);
            release_date = intent.getStringExtra(KEY_RELEASE_DATE);
            vote_average = intent.getStringExtra(KEY_VOTE_AVG);
            vote_average = vote_average + "/10";
            plot_synopsis = intent.getStringExtra(KEY_PLOT_SYNOPSIS);
        }
        Log.v("MovieActivity.java",title + poster + release_date + vote_average + plot_synopsis);

        TextView titleTV = (TextView) findViewById(R.id.title_text);
        titleTV.setText(title);

        TextView releaseDateTV = (TextView) findViewById(R.id.release_date);
        releaseDateTV.setText(release_date);

        TextView voteAverageTV = (TextView) findViewById(R.id.vote_average);
        voteAverageTV.setText(vote_average);

        TextView plotSynopsisTV = (TextView) findViewById(R.id.plot_synopsis);
        plotSynopsisTV.setText(plot_synopsis);

        ImageView titleView = (ImageView) findViewById(R.id.title_image_view);
        poster = base_url + poster;
        Picasso.with(getApplicationContext()).load(poster).into(titleView);
    }
}
