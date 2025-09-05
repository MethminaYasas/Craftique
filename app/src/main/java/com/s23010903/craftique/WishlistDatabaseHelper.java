package com.s23010903.craftique;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WishlistDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "craftique.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_WISHLIST = "Wishlist";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_ITEM = "item";

    public WishlistDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_WISHLIST + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT NOT NULL, " +
                COLUMN_ITEM + " TEXT NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLIST);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle downgrade safely
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLIST);
        onCreate(db);
    }

    // Add a wishlist item
    public long addWishlistItem(String username, String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_ITEM, item);
        return db.insert(TABLE_WISHLIST, null, values);
    }

    // Get all wishlist items for a specific user
    public Cursor getWishlistItems(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_WISHLIST + " WHERE " + COLUMN_USERNAME + "=? ORDER BY " + COLUMN_ID + " DESC",
                new String[]{username});
    }

    // Delete wishlist item by id
    public int deleteWishlistItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_WISHLIST, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }
}
