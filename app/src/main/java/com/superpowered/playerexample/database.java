package com.superpowered.playerexample;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "file_paths_db";
    private static final String TABLE_NAME = "file_paths_table";
    private static final String COLUMN_PATH = "file_path";

    public database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_PATH + " TEXT PRIMARY KEY)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void storeFilePaths(ArrayList<String> filePaths) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (String path : filePaths) {
            values.put(COLUMN_PATH, path);
            db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            values.clear();
        }

        db.close();
    }

    public ArrayList<String> retrieveFilePaths() {
        ArrayList<String> filePaths = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(COLUMN_PATH));
                filePaths.add(path);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return filePaths;
    }

}
