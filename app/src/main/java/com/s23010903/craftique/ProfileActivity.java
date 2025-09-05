package com.s23010903.craftique;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView tvUsername;
    EditText editNewPassword;
    Button btnUpdatePassword, btnLogout;
    DatabaseHelper dbHelper;

    String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUsername = findViewById(R.id.tvUsername);
        editNewPassword = findViewById(R.id.editNewPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        btnLogout = findViewById(R.id.btnLogout);
        dbHelper = new DatabaseHelper(this);

        // Get current username (you can also pass via intent or SharedPreferences)
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUsername = prefs.getString("username", null);

        if (currentUsername == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvUsername.setText(currentUsername);

        btnUpdatePassword.setOnClickListener(v -> {
            String newPass = editNewPassword.getText().toString().trim();

            if (newPass.isEmpty()) {
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.updatePassword(currentUsername, newPass);
            if (success) {
                Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show();
                editNewPassword.setText("");
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(v -> {
            // Clear session and go to login
            prefs.edit().clear().apply();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
