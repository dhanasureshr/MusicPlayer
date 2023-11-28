package com.superpowered.playerexample;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "file_paths_db";
    private static final String TABLE_NAME = "file_paths_table";
    private static final String COLUMN_PATH = "file_path";

    private static final String COLUMN_PLAYLIST_NAME = "playlist_name";

    public database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_PATH + " TEXT, " +
                COLUMN_PLAYLIST_NAME + " TEXT)";
        db.execSQL(createTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void storeFilePaths(ArrayList<String> filePaths, ArrayList<String> playlistName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (String path : filePaths) {
            values.put(COLUMN_PATH, path);
            values.put(COLUMN_PLAYLIST_NAME, playlistName);
            db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            values.clear();
        }

        db.close();
    }
    // Retrieve stored file paths and associated playlist names from the database
    public Map<String, ArrayList<String>> retrievePlaylists() {
        Map<String, ArrayList<String>> playlistsData = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(COLUMN_PATH));
                String playlistName = null;// = cursor.getString(cursor.getColumnIndex(COLUMN_PLAYLIST_NAME));
                int columnIndex = cursor.getColumnIndex(COLUMN_PLAYLIST_NAME);
                if(columnIndex >= 0)
                {
                    playlistName = cursor.getString(columnIndex);
                }
                // Add file path to the corresponding playlist
                if (!playlistsData.containsKey(playlistName)) {
                    playlistsData.put(playlistName, new ArrayList<>());
                }
                playlistsData.get(playlistName).add(path);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return playlistsData;
    }

}
/*Now, you need to make sure to create instances of both the PlayListManager and database classes in your main application code
 and pass them to each other as needed. The PlayListManager constructor initializes the playlists and loads the data from the database.
 When the application is closed, you can call savePlaylistsToDatabase to save the playlists data back to the database.

 Integration into the Main Application Code (e.g., MainActivity):

In your main application class where you initialize the PlayListManager and database,
 you need to ensure that you call the savePlaylistsToDatabase method when your application is closing and the loadPlaylistsFromDatabase method when your application is starting.
 Here's how you can modify your MainActivity:
 Handle Saving and Loading in the Appropriate Lifecycle Methods:

Ensure that you call savePlaylistsToDatabase when your application is closing or transitioning to the background.
 This could be in the onDestroy method of your main activity or in a service if your app runs in the background.

Additionally, call loadPlaylistsFromDatabase when your application is starting, such as in the onCreate method of your main activity.
 */