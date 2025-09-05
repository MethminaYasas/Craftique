package com.s23010903.craftique;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class WishlistActivity extends AppCompatActivity {

    private EditText editWishlistItem;
    private Button btnAddToWishlist;
    private LinearLayout wishlistContainer;
    private WishlistDatabaseHelper dbHelper;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        editWishlistItem = findViewById(R.id.editWishlistItem);
        btnAddToWishlist = findViewById(R.id.btnAddToWishlist);
        wishlistContainer = findViewById(R.id.wishlistContainer);

        dbHelper = new WishlistDatabaseHelper(this);

        // Load username from SharedPreferences (fallback to "guest" if none)
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUser = prefs.getString("username", "guest");

        btnAddToWishlist.setOnClickListener(v -> {
            String item = editWishlistItem.getText().toString().trim();
            if (item.isEmpty()) {
                Toast.makeText(this, "Please enter an item", Toast.LENGTH_SHORT).show();
                return;
            }
            long result = dbHelper.addWishlistItem(currentUser, item);
            if (result != -1) {
                Toast.makeText(this, "Item added to wishlist", Toast.LENGTH_SHORT).show();
                editWishlistItem.setText("");
                loadWishlist();
            } else {
                Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        });

        loadWishlist();
    }

    private void loadWishlist() {
        wishlistContainer.removeAllViews();
        Cursor cursor = dbHelper.getWishlistItems(currentUser);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(WishlistDatabaseHelper.COLUMN_ID));
                String item = cursor.getString(cursor.getColumnIndexOrThrow(WishlistDatabaseHelper.COLUMN_ITEM));

                TextView itemView = new TextView(this);
                itemView.setText("• " + item);
                itemView.setTextSize(16);
                itemView.setPadding(16, 16, 16, 16);

                // Click to delete the item
                itemView.setOnClickListener(view -> {
                    dbHelper.deleteWishlistItem(id);
                    Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show();
                    loadWishlist();
                });

                wishlistContainer.addView(itemView);
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            TextView emptyView = new TextView(this);
            emptyView.setText("Your wishlist is empty.");
            emptyView.setPadding(16, 16, 16, 16);
            wishlistContainer.addView(emptyView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWishlist();
    }
}
