package com.example.android.movies1;

/**
 * Created by mihirnewalkar on 9/7/17.
 */

public class Reviews {

    //Id of the Review
    private String mreviewId;

    //Author of the Review
    private String mAuthor;

    //Content of the Review
    private String mContent;

    /*
    * Create a new Reviews object.
    *
    * @param id
    *@param author
    *@param content
    * */
    public Reviews(String reviewId, String author, String content) {
        mreviewId = reviewId;
        mAuthor = author;
        mContent = content;
    }

    public String getReviewId() {return mreviewId;}
    public String getAuthor() {return mAuthor;}
    public String getContent() {return mContent;}
}
