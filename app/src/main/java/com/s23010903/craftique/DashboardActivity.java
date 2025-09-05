package com.s23010903.craftique;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    Button btnProducts, btnCustomOrder, btnOrderManagement, btnChat, btnProfile, btnLocation, btnWishlist, btnRatings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnProducts = findViewById(R.id.btnProducts);
        btnCustomOrder = findViewById(R.id.btnCustomOrder);
        btnOrderManagement = findViewById(R.id.btnOrderManagement);
        btnChat = findViewById(R.id.btnChat);
        btnProfile = findViewById(R.id.btnProfile);
        btnLocation = findViewById(R.id.btnLocation);
        btnWishlist = findViewById(R.id.btnWishlist);
        btnRatings = findViewById(R.id.btnRatings);

        btnProducts.setOnClickListener(v -> launchActivity(ProductListActivity.class, "Products"));
        btnCustomOrder.setOnClickListener(v -> launchActivity(CustomOrderActivity.class, "Custom Orders"));
        btnOrderManagement.setOnClickListener(v -> launchActivity(OrderManagementActivity.class, "Order Management"));
        btnChat.setOnClickListener(v -> launchActivity(ChatActivity.class, "Chat"));
        btnProfile.setOnClickListener(v -> launchActivity(ProfileActivity.class, "Profile"));
        btnLocation.setOnClickListener(v -> launchActivity(LocationActivity.class, "Location"));
        btnWishlist.setOnClickListener(v -> launchActivity(WishlistActivity.class, "Wishlist"));
        btnRatings.setOnClickListener(v -> launchActivity(RatingsActivity.class, "Ratings"));
    }

    private void launchActivity(Class<?> cls, String activityName) {
        Toast.makeText(this, "Opening " + activityName + "...", Toast.LENGTH_SHORT).show();
        try {
            Log.d("DashboardActivity", "Starting " + activityName + "Activity...");
            startActivity(new Intent(this, cls));
            Log.d("DashboardActivity", activityName + "Activity started");
        } catch (Exception e) {
            Toast.makeText(this, "Error opening " + activityName + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
