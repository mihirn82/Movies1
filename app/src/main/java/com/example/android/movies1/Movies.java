package com.example.android.movies1;

/**
 * Created by mihirnewalkar on 7/4/17.
 */

public class Movies {

    //Id of the movie
    private String mId;

    //Title of the movie.
    private String mTitle;

    //Poster Title of the movie.
    private String mPosterTitle;

    //Release Date of the movie.
    private String mReleaseDate;

    //Vote average of the movie.
    private String mVoteAverage;

    //Plot synopsis of the movie.
    private String mPlotSynopsis;

    /*
    * Create a new Movies object.
    *
    * @param id
    *@param title
    *@param posterTitle
    *@param releaseDate
    *@param voteAverage
    *@param plotSynopsis
    * */
    public Movies(String id, String title, String posterTitle, String releaseDate, String voteAverage, String plotSynopsis) {
        mId = id;
        mTitle = title;
        mPosterTitle = posterTitle;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
        mPlotSynopsis = plotSynopsis;
    }

    public String getId() {return mId; }
    public String getTitle() {return mTitle; }
    public String getPosterTitle() {return mPosterTitle; }
    public String getReleaseDate() {return mReleaseDate; }
    public String getVoteAverage() {return mVoteAverage; }
    public String getPlotSynopsis() {return mPlotSynopsis; }
}
