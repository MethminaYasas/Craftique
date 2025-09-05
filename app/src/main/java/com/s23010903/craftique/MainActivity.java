package com.s23010903.craftique;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    EditText editUsername, editPassword;
    Button btnLogin;
    Button btnFingerprintLogin;  // New button for fingerprint login
    DatabaseHelper dbHelper;

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnFingerprintLogin = findViewById(R.id.btnFingerprintLogin); // add in your layout
        dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String user = editUsername.getText().toString().trim();
            String pass = editPassword.getText().toString().trim();

            if (dbHelper.checkUser(user, pass)) {
                saveSessionAndGoDashboard(user);
            } else if (dbHelper.isUserExists(user)) {
                Toast.makeText(this, "Incorrect password!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "User not registered. Redirecting to Register...", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, RegisterActivity.class);
                i.putExtra("username", user);
                startActivity(i);
            }
        });

        // Setup BiometricPrompt
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Fingerprint recognized!", Toast.LENGTH_SHORT).show();

                // Optional: retrieve stored username or require user to enter username
                // For simplicity, let's assume a fixed user or stored in SharedPreferences
                SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                String savedUser = prefs.getString("username", null);

                if (savedUser != null) {
                    saveSessionAndGoDashboard(savedUser);
                } else {
                    Toast.makeText(MainActivity.this, "Please login once using username/password to enable fingerprint login.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Fingerprint not recognized. Try again.", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Login")
                .setSubtitle("Use your fingerprint to login")
                .setNegativeButtonText("Cancel")
                .build();

        btnFingerprintLogin.setOnClickListener(v -> {
            biometricPrompt.authenticate(promptInfo);
        });
    }

    private void saveSessionAndGoDashboard(String username) {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        prefs.edit().putString("username", username).apply();

        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }
}
