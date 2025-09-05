package com.s23010903.craftique;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

public class RegisterActivity extends AppCompatActivity {

    EditText editUsername, editPassword;
    Button btnRegister;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnRegister = findViewById(R.id.btnRegister);
        dbHelper = new DatabaseHelper(this);

        // Pre-fill from login if user already typed
        String passedUsername = getIntent().getStringExtra("username");
        if (passedUsername != null) {
            editUsername.setText(passedUsername);
        }

        btnRegister.setOnClickListener(v -> {
            String user = editUsername.getText().toString();
            String pass = editPassword.getText().toString();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.registerUser(user, pass);
            if (success) {
                Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
                finish(); // Go back to login screen
            } else {
                Toast.makeText(this, "Username already exists!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
