package com.example.android.movies1;

import java.net.MalformedURLException;
import java.net.URL;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by mihirnewalkar on 7/4/17.
 */

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link Movies} object by parsing out information
     * about the first news from the input newsJSON string.
     */
    private static List<Movies> extractFeatureFromJson(String moviesJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(moviesJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding movies to
        List<Movies> movies = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            JSONObject baseJsonResponse = new JSONObject(moviesJSON);
            JSONArray moviesArray = baseJsonResponse.getJSONArray("results");

            // If there are results in the features array
            for (int i = 0; i < moviesArray.length(); i++) {
                // Extract out the first feature
                JSONObject currentMovies = moviesArray.getJSONObject(i);

                // Extract movie details
                String id = currentMovies.getString("id");
                String title = currentMovies.getString("title");
                String posterTitle = currentMovies.getString("poster_path");
                String releaseDate = currentMovies.getString("release_date");
                String voteAverage = currentMovies.getString("vote_average");
                String plotSynopsis = currentMovies.getString("overview");

                if (posterTitle!="null") {
                    Movies movies1 = new Movies(id,title,posterTitle,releaseDate,voteAverage,plotSynopsis);
                    movies.add(movies1);
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG,   "Problem parsing the news JSON results", e);
        }
        return movies;
    }

    /**
     * Query the themoviedb dataset and return a list of {@link Movies} objects.
     */
    public static List<Movies> fetchMoviesData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Movies}
        List<Movies> movies = extractFeatureFromJson(jsonResponse);

        Log.v("QueryUtils.java","Inside fetchMoviesData");
        // Return the list of {@link Movies}
        return movies;
    }

    public static List<Reviews> fetchReviews(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Movies}
        List<Reviews> reviews = extractReviewsFromJson(jsonResponse);

        Log.v("QueryUtils.java","Inside fetchReviews");
        // Return the list of {@link Movies}
        return reviews;
    }

    /**
     * Return an {@link Movies} object by parsing out information
     * about the first news from the input newsJSON string.
     */
    private static List<Reviews> extractReviewsFromJson(String reviewsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(reviewsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding reviews to
        List<Reviews> reviews = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            JSONObject baseJsonResponse = new JSONObject(reviewsJSON);
            JSONArray reviewsArray = baseJsonResponse.getJSONArray("results");

            // If there are results in the features array
            for (int i = 0; i < reviewsArray.length(); i++) {
                // Extract out the first feature
                JSONObject currentMovieReviews = reviewsArray.getJSONObject(i);

                // Extract review details
                String id = currentMovieReviews.getString("id");
                String author = currentMovieReviews.getString("author");
                String content = currentMovieReviews.getString("content");

                Log.i(LOG_TAG,id + " " + author + " " + content);

                Reviews reviews1 = new Reviews(id,author,content);
                reviews.add(reviews1);
            }

            if (reviewsArray.length() == 0) {
                String content = "No reviews yet";

                Log.i(LOG_TAG, content);

                Reviews reviews1 = new Reviews(null,null,content);
                reviews.add(reviews1);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG,   "Problem parsing the news JSON results", e);
        }
        return reviews;
    }


    public static List<Trailers> fetchTrailers(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Movies}
        List<Trailers> trailers = extractTrailersFromJson(jsonResponse);

        Log.v(LOG_TAG,"Inside fetchTrailers");
        // Return the list of {@link Movies}
        return trailers;
    }


    /**
     * Return an {@link Movies} object by parsing out information
     * about the first news from the input newsJSON string.
     */
    private static List<Trailers> extractTrailersFromJson(String reviewsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(reviewsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding reviews to
        List<Trailers> trailers = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            JSONObject baseJsonResponse = new JSONObject(reviewsJSON);
            JSONArray reviewsArray = baseJsonResponse.getJSONArray("results");

            // If there are results in the features array
            for (int i = 0; i < reviewsArray.length(); i++) {
                // Extract out the first feature
                JSONObject currentMovieTrailers = reviewsArray.getJSONObject(i);

                // Extract trailer details
                String id = currentMovieTrailers.getString("id");
                String key = currentMovieTrailers.getString("key");

                Log.i(LOG_TAG,id + " " + key);

                Trailers trailers1 = new Trailers(id,key);
                trailers.add(trailers1);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG,   "Problem parsing the news JSON results", e);
        }
        return trailers;
    }
}
