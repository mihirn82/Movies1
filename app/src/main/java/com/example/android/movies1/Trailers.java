package com.example.android.movies1;

/**
 * Created by mihirnewalkar on 9/10/17.
 */

public class Trailers {

    //Id of the Trailer
    private String mTrailerId;

    //Key of the Trailer
    private String mKey;

    /*
    * Create a new Trailer object.
    *
    * @param trailerId
    *@param key
    * */
    public Trailers(String trailerId, String key) {
        mTrailerId = trailerId;
        mKey = key;
    }

    public String getTrailerId() {return mTrailerId;}
    public String getKey() {return mKey;}
}
