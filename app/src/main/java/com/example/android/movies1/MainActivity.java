package com.example.android.movies1;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.movies1.data.MoviesCursorAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.movies1.data.MoviesContract.*;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks {

    /** URL for movies data from the themoviedb dataset */
    private static final String MOVIES_REQUEST_URL = "https://api.themoviedb.org/3/movie/";

    private String sortOrder = "popular";
    private String myApi_key = "?api_key=xxx";
    private String FINAL_REQUEST_URL;

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Constant value for the movies loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int MOVIES_LOADER_ID = 1;
    private static final int CURSOR_LOADER_ID = 4;

    /** Adapter for the list of movies */
    private MoviesAdapter mAdapter;
    private MoviesCursorAdapter mCursorAdapter;

    public static final String KEY_ID="movie_id";
    public static final String KEY_TITLE="movie_title";
    public static final String KEY_POSTER_TITLE="movie_poster";
    public static final String KEY_RELEASE_DATE="movie_release_date";
    public static final String KEY_VOTE_AVG="movie_vote_avg";
    public static final String KEY_PLOT_SYNOPSIS="movie_overview";

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    private ProgressBar mProgress;

    private GridView moviesListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        moviesListView = (GridView) findViewById(R.id.grid_view);

        mProgress = (ProgressBar) findViewById(R.id.ProgressBar);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        moviesListView.setEmptyView(mEmptyStateTextView);

        // Create a new {@link ArrayAdapter} of movies
        mAdapter = new MoviesAdapter(this, new ArrayList<Movies>());
        mCursorAdapter = new MoviesCursorAdapter(this, null);

        if (sortOrder != " ") {
            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            moviesListView.setAdapter(mAdapter);

        } else {

            moviesListView.setAdapter(mCursorAdapter);
        }



        moviesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent (MainActivity.this, MovieActivity.class);

                if (sortOrder != " ") {
                    Movies currentMovie = mAdapter.getItem(position);

                    intent.putExtra(KEY_ID, currentMovie.getId());
                    intent.putExtra(KEY_TITLE, currentMovie.getTitle());
                    intent.putExtra(KEY_POSTER_TITLE, currentMovie.getPosterTitle());
                    intent.putExtra(KEY_RELEASE_DATE, currentMovie.getReleaseDate());
                    intent.putExtra(KEY_VOTE_AVG, currentMovie.getVoteAverage());
                    intent.putExtra(KEY_PLOT_SYNOPSIS, currentMovie.getPlotSynopsis());
                    Log.v("MainActivity.java",currentMovie.getTitle());

                } else {
                    Cursor cursor = (Cursor) mCursorAdapter.getItem(position);

                    intent.putExtra(KEY_ID,cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_ID)));
                    intent.putExtra(KEY_TITLE, cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_TITLE)));
                    intent.putExtra(KEY_POSTER_TITLE, cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_POSTER)));
                    intent.putExtra(KEY_RELEASE_DATE, cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_RELEASE_DATE)));
                    intent.putExtra(KEY_VOTE_AVG, cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_RATING)));
                    intent.putExtra(KEY_PLOT_SYNOPSIS, cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_SYNOPSIS)));
                }

                startActivity(intent);
            }
        });

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            FINAL_REQUEST_URL = MOVIES_REQUEST_URL + sortOrder + myApi_key;

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(MOVIES_LOADER_ID, null, this);
            Log.v("MainActivity.java","Done initLoader");
        }
        else {
            mProgress.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_movies);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch(item.getItemId()) {

            case R.id.most_popular:
                // Respond to user click of most popular; sort movies by most popular
                sortOrder = "popular";
                break;

            case R.id.top_rated:
                // Respond to user click of top rated; sort movies by top rated
                sortOrder = "top_rated";
                break;

            case R.id.favorite:
                // Respond to user click of favorite; sort movies by top rated
                sortOrder = " ";
                break;
        }
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            if (sortOrder != " ") {
                FINAL_REQUEST_URL = MOVIES_REQUEST_URL + sortOrder + myApi_key;
                Log.v("MainActivity.java",FINAL_REQUEST_URL);
                mAdapter.clear();
                moviesListView.setAdapter(mAdapter);
                getLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
            } else {
                mCursorAdapter.swapCursor(null);
                moviesListView.setAdapter(mCursorAdapter);
                getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
            }
        }
        else {
            if (sortOrder != " ") {
                mProgress.setVisibility(View.GONE);
                mEmptyStateTextView.setText(R.string.no_movies);
            } else {
                mCursorAdapter.swapCursor(null);
                moviesListView.setAdapter(mCursorAdapter);
                getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        Log.v(LOG_TAG,"Inside onCreateLoader");

        switch(id) {
            case MOVIES_LOADER_ID:
                Uri baseUri = Uri.parse(FINAL_REQUEST_URL);
                Uri.Builder uriBuilder = baseUri.buildUpon();
                return new MoviesLoader(this, uriBuilder.toString());

            case CURSOR_LOADER_ID:
                // Since the Movie Activity shows all movie attributes, define a projection that contains
                // all columns from the product table
                String[] projection = {
                        MoviesEntry._ID,
                        MoviesEntry.COLUMN_MOVIE_ID,
                        MoviesEntry.COLUMN_MOVIE_TITLE,
                        MoviesEntry.COLUMN_MOVIE_POSTER,
                        MoviesEntry.COLUMN_MOVIE_SYNOPSIS,
                        MoviesEntry.COLUMN_MOVIE_RELEASE_DATE,
                        MoviesEntry.COLUMN_MOVIE_RATING};

                String selection = "";

                // This loader will execute the ContentProvider's query method on a background thread
                return new CursorLoader(this,   // Parent activity context
                        MoviesEntry.CONTENT_URI,     // Query the content URI for the current product
                        projection,             // Columns to include in the resulting Cursor
                        selection,              // No selection clause
                        null,                   // No selection arguments
                        null);                  // Default sort order

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        Log.v(LOG_TAG,"Inside onLoadFinished");

        mProgress.setVisibility(View.GONE);

        // Set empty state text to display "No movies found."
        mEmptyStateTextView.setText(R.string.no_movies);

        switch (loader.getId()){
            case MOVIES_LOADER_ID:
                mAdapter.swapData((List<Movies>) data);
                break;

            case CURSOR_LOADER_ID:

                Cursor cursor = (Cursor) data;

                Log.i("OnLoadFinished",Integer.toString(cursor.getCount()));
                // Bail early if the cursor is null or there is less than 1 row in the cursor

                if (cursor == null || cursor.getCount() < 1) {
                    mAdapter.clear();
                    return;
                }

                mCursorAdapter.swapCursor(cursor);
                break;

            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.v(LOG_TAG,"Inside onLoaderReset");

        switch (loader.getId()) {

            case MOVIES_LOADER_ID:
                mAdapter.clear();
                break;

            case CURSOR_LOADER_ID:
                mCursorAdapter.swapCursor(null);
                break;

            default:
                break;
        }
    }
}