package com.example.android.movies1;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.movies1.MainActivity.KEY_ID;
import static com.example.android.movies1.MainActivity.KEY_PLOT_SYNOPSIS;
import static com.example.android.movies1.MainActivity.KEY_POSTER_TITLE;
import static com.example.android.movies1.MainActivity.KEY_RELEASE_DATE;
import static com.example.android.movies1.MainActivity.KEY_TITLE;
import static com.example.android.movies1.MainActivity.KEY_VOTE_AVG;
import static com.example.android.movies1.data.MoviesContract.*;

/**
 * Created by mihirnewalkar on 7/15/17.
 */

public class MovieActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    /** URL for movies data from the themoviedb dataset */
    private static final String MOVIES_REQUEST_URL = "https://api.themoviedb.org/3/movie/";

    private String videos = "/videos";
    private String reviews = "/reviews";
    private String myApi_key = "?api_key=xxx";

    private String REVIEWS_URL;
    private String TRAILERS_URL;

    private static final String base_342_url = "http://image.tmdb.org/t/p/w342";

    public static final String LOG_TAG = MovieActivity.class.getName();

    /**
     * Constant value for the movies loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int REVIEWS_LOADER_ID = 2;
    private static final int TRAILERS_LOADER_ID = 3;
    private static final int CURSOR_LOADER_ID = 4;

    /** Adapter for the list of movies */
    private ReviewsAdapter mReviewsAdapter;
    private TrailersAdapter mTrailersAdapter;


    private String id = "";
    private String title = "";
    private String poster_key = "";
    private String release_date = "";
    private String vote_average = "";
    private String plot_synopsis = "";

    private Uri currentMovieUri;
    private ToggleButton favoriteButton;
    Boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent intent = getIntent();

        if (intent != null) {
            id = intent.getStringExtra(KEY_ID);
            title = intent.getStringExtra(KEY_TITLE);
            poster_key = intent.getStringExtra(KEY_POSTER_TITLE);
            release_date = intent.getStringExtra(KEY_RELEASE_DATE);
            vote_average = intent.getStringExtra(KEY_VOTE_AVG);
            vote_average = vote_average + "/10";
            plot_synopsis = intent.getStringExtra(KEY_PLOT_SYNOPSIS);
        }
        Log.v("MovieActivity.java",title + poster_key + release_date + vote_average + plot_synopsis);

        currentMovieUri = ContentUris.withAppendedId (MoviesEntry.CONTENT_URI,Integer.parseInt(id));


        TextView titleTV = (TextView) findViewById(R.id.title_text);
        titleTV.setText(title);

        TextView releaseDateTV = (TextView) findViewById(R.id.release_date);
        releaseDateTV.setText(release_date);

        TextView voteAverageTV = (TextView) findViewById(R.id.vote_average);
        voteAverageTV.setText(vote_average);

        TextView plotSynopsisTV = (TextView) findViewById(R.id.plot_synopsis);
        plotSynopsisTV.setText(plot_synopsis);

        ImageView titleView = (ImageView) findViewById(R.id.title_image_view);
        String poster = base_342_url + poster_key;
        Picasso.with(getApplicationContext()).load(poster).into(titleView);

        // Setup favorite button to mark movie as favorite
        favoriteButton = (ToggleButton) findViewById(R.id.favorite_button);

        isFavorite = readState(currentMovieUri);

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG_TAG,"I clicked favorite button");

                isFavorite = readState(currentMovieUri);

                if (! isFavorite) {

                    ContentValues values = new ContentValues();
                    values.put(MoviesEntry.COLUMN_MOVIE_ID, id);
                    values.put(MoviesEntry.COLUMN_MOVIE_TITLE,title);
                    values.put(MoviesEntry.COLUMN_MOVIE_POSTER,poster_key);
                    values.put(MoviesEntry.COLUMN_MOVIE_SYNOPSIS,plot_synopsis);
                    values.put(MoviesEntry.COLUMN_MOVIE_RELEASE_DATE,release_date);
                    values.put(MoviesEntry.COLUMN_MOVIE_RATING,vote_average);

                    // Insert a new row into the provider, returning the content URI for the new movie.
                    Uri newUri = getContentResolver().insert(MoviesEntry.CONTENT_URI, values);
                    Log.i ("New Uri------", newUri.toString());

                    favoriteButton.setBackgroundColor(Color.DKGRAY);
                    favoriteButton.setText(R.string.textOff);
                    favoriteButton.setTextColor(Color.WHITE);
                    Toast.makeText(getApplicationContext(),R.string.toast_mark_favorite,Toast.LENGTH_SHORT).show();

                    if (newUri == null) {
                        Log.i(LOG_TAG,"Insert Product failed");
                    }

                } else {
                    // Delete the row
                    int rowsDeleted = getContentResolver().delete(currentMovieUri,null,null);
                    Log.i ("Rows Deleted------", Integer.toString(rowsDeleted));

                    favoriteButton.setBackgroundColor(Color.LTGRAY);
                    favoriteButton.setText(R.string.textOn);
                    favoriteButton.setTextColor(Color.BLACK);
                    Toast.makeText(getApplicationContext(),R.string.toast_unmark_favorite,Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Find a reference to the {@link ListView} in the layout
        final ListView reviewsListView = (ListView) findViewById(R.id.list_view_review);
        final ListView trailersListView = (ListView) findViewById(R.id.list_view_trailer);

        // Create a new {@link ArrayAdapter} of movies
        mReviewsAdapter = new ReviewsAdapter(this, new ArrayList<Reviews>());
        mTrailersAdapter = new TrailersAdapter(this, new ArrayList<Trailers>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        reviewsListView.setAdapter(mReviewsAdapter);
        trailersListView.setAdapter(mTrailersAdapter);


        trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String baseUrl = "https://youtu.be/";

                Trailers currentTrailers = mTrailersAdapter.getItem(position);
                String youtubeUrl = baseUrl + currentTrailers.getKey();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
                startActivity(intent);
            }
        });

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(CURSOR_LOADER_ID, null, this);
        Log.v(LOG_TAG,"Done initLoader for Cursor");

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            REVIEWS_URL = MOVIES_REQUEST_URL + id + reviews + myApi_key;
            TRAILERS_URL = MOVIES_REQUEST_URL + id + videos + myApi_key;
            Log.v("Reviews URL = ", REVIEWS_URL);
            Log.v("Trailers URL = ", TRAILERS_URL);

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(REVIEWS_LOADER_ID, null, this);
            Log.v(LOG_TAG,"Done initLoader for Reviews");

            loaderManager.initLoader(TRAILERS_LOADER_ID, null, this);
            Log.v(LOG_TAG,"Done initLoader for Trailers");
        }
//        else {
//            mProgress.setVisibility(View.GONE);
//            mEmptyStateTextView.setText(R.string.no_movies);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.share_video:

                String baseUrl = "https://youtu.be/";
                Trailers currentTrailers = mTrailersAdapter.getItem(0);
                String youtubeUrl = baseUrl + currentTrailers.getKey();
                Intent intent = new Intent(Intent.ACTION_SEND).setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,youtubeUrl);
                startActivity(intent);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean readState(Uri currentMovieUri) {
        int movieId = Integer.parseInt(id);
        Log.i("Movie Id = ",String.valueOf(movieId));

        Log.i("Movie Uri = ",currentMovieUri.toString());
        Cursor cursor = this.getContentResolver().query(currentMovieUri, null, null, null,null);

        Log.i(LOG_TAG,Integer.toString(cursor.getCount()));

        if (cursor != null && cursor.getCount()>0) {

            cursor.moveToFirst();
            int dbId = cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_ID);
            String retId = cursor.getString(dbId);
            Log.i("Movie ID is = ",retId);

            favoriteButton.setBackgroundColor(Color.DKGRAY);
            favoriteButton.setText(R.string.textOff);
            favoriteButton.setTextColor(Color.WHITE);
            return true;
        }


        if (cursor == null) {
            Log.i(LOG_TAG,"Cursor is NULL");
        }

        favoriteButton.setBackgroundColor(Color.LTGRAY);
        favoriteButton.setText(R.string.textOn);
        favoriteButton.setTextColor(Color.BLACK);
        return false;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG,"Inside onCreateLoader");

        Uri baseUri;
        Uri.Builder uriBuilder;

        switch(id) {
            case REVIEWS_LOADER_ID:
                baseUri = Uri.parse(REVIEWS_URL);
                uriBuilder = baseUri.buildUpon();
                return new ReviewsLoader(this, uriBuilder.toString());

            case TRAILERS_LOADER_ID:
                baseUri = Uri.parse(TRAILERS_URL);
                uriBuilder = baseUri.buildUpon();
                return new TrailersLoader(this, uriBuilder.toString());

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
                        currentMovieUri,     // Query the content URI for the current product
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

        switch (loader.getId()) {
            case REVIEWS_LOADER_ID:
                mReviewsAdapter.swapReviewsData((List<Reviews>) data);
                break;

            case TRAILERS_LOADER_ID:
                mTrailersAdapter.swapTrailersData((List<Trailers>) data);
                break;

            case CURSOR_LOADER_ID:
                Cursor cursor = (Cursor) data;

                Log.i("OnLoadFinished",Integer.toString(cursor.getCount()));
                // Bail early if the cursor is null or there is less than 1 row in the cursor
                if (cursor == null || cursor.getCount() < 1) {
                    return;
                }

                if (cursor.moveToFirst()) {
                    // Find the columns of product attributes that we're interested in
                    int idColumnIndex = cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_ID);
                    int titleColumnIndex = cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_TITLE);

                    // Extract out the value from the Cursor for the given column index
                    String id = cursor.getString(idColumnIndex);
                    String title = cursor.getString(titleColumnIndex);

                    Log.i("OnLoadFinished id: ", id);
                    Log.i("OnLoadFinished title: ", title);
                }
                break;

            default: break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.v(LOG_TAG,"Inside onLoaderReset");
        mReviewsAdapter.clear();
        mTrailersAdapter.clear();
    }
}