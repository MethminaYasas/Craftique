package com.s23010903.craftique;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RatingsActivity extends AppCompatActivity {

    private static final String TAG = "RatingsActivity";

    RatingBar ratingBar;
    EditText editFeedback;
    Button btnSubmitRating, btnViewMyReviews;
    RatingsDatabaseHelper dbHelper;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        // Initialize views
        ratingBar = findViewById(R.id.ratingBar);
        editFeedback = findViewById(R.id.editFeedback);
        btnSubmitRating = findViewById(R.id.btnSubmitRating);
        btnViewMyReviews = findViewById(R.id.btnViewMyReviews);

        // Initialize database helper
        dbHelper = new RatingsDatabaseHelper(this);

        // Get logged-in username or default to "Guest"
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUser = prefs.getString("username", "Guest");

        if (currentUser == null || currentUser.trim().isEmpty()) {
            currentUser = "Guest";
        }

        Log.d(TAG, "Current User: " + currentUser);

        if (currentUser.equals("Guest")) {
            Toast.makeText(this, "You're not logged in. Reviews will be saved under 'Guest'.", Toast.LENGTH_LONG).show();
        }

        // Handle Submit button
        btnSubmitRating.setOnClickListener(v -> {
            int rating = (int) ratingBar.getRating();
            String feedback = editFeedback.getText().toString().trim();

            if (rating == 0) {
                Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
                return;
            }

            if (feedback.isEmpty()) {
                feedback = "(No feedback provided)";
            }

            // Log what we're trying to insert
            Log.d(TAG, "Trying to insert - User: " + currentUser + ", Rating: " + rating + ", Feedback: " + feedback);

            long result = dbHelper.insertRating(currentUser, rating, feedback);

            if (result != -1) {
                Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                ratingBar.setRating(5);
                editFeedback.setText("");
            } else {
                Toast.makeText(this, "Failed to save rating", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Insert failed - returned ID: " + result);
            }
        });

        // Handle View My Reviews button
        btnViewMyReviews.setOnClickListener(v -> {
            Intent intent = new Intent(RatingsActivity.this, MyRatingsActivity.class);
            startActivity(intent);
        });
    }
}
