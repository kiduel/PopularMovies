package com.example.android.popular_movie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popular_movie.data.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "moviesDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;

    // Constructor
    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID            + " INTEGER UNIQUE PRIMARY KEY , " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_LIKED + " INTEGER, " +
                MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
