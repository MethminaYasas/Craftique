package com.s23010903.craftique;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RatingsDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "RatingsDBHelper";
    private static final String DATABASE_NAME = "craftique.db";
    private static final int DATABASE_VERSION = 2;  // Bumped version to force recreate
    public static final String TABLE_RATINGS = "Ratings";

    public RatingsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_RATINGS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "rating INTEGER NOT NULL, " +
                "feedback TEXT)";
        db.execSQL(createTable);
        Log.d(TAG, "Ratings table created successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table and recreate for upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);
        onCreate(db);
        Log.d(TAG, "Database upgraded from version " + oldVersion + " to " + newVersion);
    }

    public long insertRating(String username, int rating, String feedback) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("rating", rating);
        values.put("feedback", feedback);

        long result = db.insert(TABLE_RATINGS, null, values);

        if (result == -1) {
            Log.e(TAG, "Insert failed for user: " + username + ", rating: " + rating + ", feedback: " + feedback);
        } else {
            Log.d(TAG, "Insert success for user: " + username);
        }

        return result;
    }

    public Cursor getAllRatings() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RATINGS + " ORDER BY id DESC", null);
    }

    public Cursor getRatingsByUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_RATINGS + " WHERE username = ? ORDER BY id DESC",
                new String[]{username});
    }
}
