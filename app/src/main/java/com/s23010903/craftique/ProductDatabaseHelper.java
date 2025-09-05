// ProductDatabaseHelper.java
package com.s23010903.craftique;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "craftique.db";
    private static final int DATABASE_VERSION = 2; // <-- increase if modifying schema
    public static final String TABLE_PRODUCTS = "Products";

    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "description TEXT," +
                "price REAL)";
        db.execSQL(createTable);
        insertSampleProducts(db); // <- make sure this is called
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    public long insertProduct(String name, String description, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    private void insertSampleProducts(SQLiteDatabase db) {
        insertDirect(db, "Handmade Jute Bag", "Eco-friendly handwoven bag made from jute fibers", 1800.00);
        insertDirect(db, "Crochet Teddy Bear", "Soft handmade crochet toy for children", 1200.00);
        insertDirect(db, "Beaded Necklace", "Colorful handmade necklace with wooden beads", 950.00);
        insertDirect(db, "Resin Coaster Set", "Set of 4 handmade resin coasters with dried flowers", 1500.00);
        insertDirect(db, "Macrame Wall Hanging", "Boho style cotton macrame decor piece", 2200.00);
        insertDirect(db, "Hand-painted Clay Pot", "Mini flower pot painted with floral patterns", 800.00);
    }

    private void insertDirect(SQLiteDatabase db, String name, String description, double price) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        db.insert(TABLE_PRODUCTS, null, values);
    }
}
