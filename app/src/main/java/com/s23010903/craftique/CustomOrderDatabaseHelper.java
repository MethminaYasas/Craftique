package com.s23010903.craftique;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CustomOrderDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "craftique.db";
    private static final int DATABASE_VERSION = 3; // ⚠️ bump this if not already

    public static final String TABLE_CUSTOM_ORDERS = "CustomOrders";

    public CustomOrderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_CUSTOM_ORDERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "orderName TEXT," +
                "description TEXT," +
                "estimatedPrice REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOM_ORDERS);
        onCreate(db);
    }

    public long insertCustomOrder(String orderName, String description, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("orderName", orderName);
        values.put("description", description);
        values.put("estimatedPrice", price);
        return db.insert(TABLE_CUSTOM_ORDERS, null, values);
    }
}
