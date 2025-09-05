package com.s23010903.craftique;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MyRatingsActivity extends AppCompatActivity {

    ListView listViewRatings;
    RatingsDatabaseHelper dbHelper;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ratings);

        listViewRatings = findViewById(R.id.listViewRatings);
        dbHelper = new RatingsDatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUser = prefs.getString("username", "Guest");

        loadUserRatings();
    }

    private void loadUserRatings() {
        Cursor cursor = dbHelper.getRatingsByUser(currentUser);

        if (cursor != null && cursor.moveToFirst()) {
            ArrayList<String> ratingsList = new ArrayList<>();

            do {
                int rating = cursor.getInt(cursor.getColumnIndexOrThrow("rating"));
                String feedback = cursor.getString(cursor.getColumnIndexOrThrow("feedback"));
                String displayText = "Rating: " + rating + "/5\nFeedback: " + feedback;
                ratingsList.add(displayText);
            } while (cursor.moveToNext());

            cursor.close();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, ratingsList);
            listViewRatings.setAdapter(adapter);

        } else {
            Toast.makeText(this, "No ratings found for: " + currentUser, Toast.LENGTH_SHORT).show();
        }
    }
}
