package com.s23010903.craftique;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {

    EditText editMessage;
    Button btnSend;

    // Replace with your business WhatsApp number in international format (no '+' or dashes)
    private final String businessNumber = "94740224207"; // Example: +94 740 224 207 (Sri Lanka)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> {
            String message = editMessage.getText().toString().trim();

            if (message.isEmpty()) {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
                return;
            }

            sendToWhatsApp(message);
        });
    }

    private void sendToWhatsApp(String message) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String url = "https://wa.me/" + businessNumber + "?text=" + Uri.encode(message);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
