package com.example.android.movies1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.movies1.data.MoviesContract.*;

/**
 * Created by mihirnewalkar on 9/10/17.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "store.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String BLOB_TYPE = " BLOB";
    private static final String TEXT_NOT_NULL = " NOT NULL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                    MoviesEntry._ID + " INTEGER DEFAULT 0," +
                    MoviesEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY," +
                    MoviesEntry.COLUMN_MOVIE_TITLE + TEXT_TYPE + TEXT_NOT_NULL + COMMA_SEP +
                    MoviesEntry.COLUMN_MOVIE_POSTER + TEXT_TYPE + COMMA_SEP +
                    MoviesEntry.COLUMN_MOVIE_SYNOPSIS + TEXT_TYPE + COMMA_SEP +
                    MoviesEntry.COLUMN_MOVIE_RELEASE_DATE + TEXT_TYPE + COMMA_SEP +
                    MoviesEntry.COLUMN_MOVIE_RATING + TEXT_TYPE + " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}