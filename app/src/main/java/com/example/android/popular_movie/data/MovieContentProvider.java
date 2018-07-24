package com.example.android.popular_movie.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.popular_movie.data.MovieContract.MovieEntry.TABLE_NAME;


public class MovieContentProvider extends ContentProvider {

    // Define final integer constants for the directory of movies and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int MOVIES = 100;                //for inserting bulk
    public static final int MOVIE_WITH_ID = 101;         //for quering items with fav == true

    // CDeclare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match
    /**
     Initialize a new matcher object without any matches,
     then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the movie directory and a single item by ID.
         */
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_TASKS, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_TASKS + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    // Member variable for a TaskDbHelper that's initialized in the onCreate() method
    private MovieDbHelper mMovieDbHelper;

    /* onCreate() is where I should initialize anything I’ll need to setup
    my underlying data source.
    In this case, I'm working with a SQLite database, so I’ll need to
    initialize a DbHelper to gain access to it.
     */

    @Override
    public boolean onCreate() {
        // Complete onCreate() and initialize a TaskDbhelper on startup
        // We need the helper class, so we define it here

        Context context = getContext();
        mMovieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        //Get access to the movies database (to write new data to)
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        //Write URI matching code to identify the match for the movies directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case MOVIES:
                // COMPLETED (3) Insert new values into the database
                // Inserting values into movies table
                long id = db.insert(TABLE_NAME, null, contentValues);

                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // COMPLETED (4) Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // COMPLETED (5) Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        // Get writeable database
        SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(MovieContract.MovieEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;

    }


}
