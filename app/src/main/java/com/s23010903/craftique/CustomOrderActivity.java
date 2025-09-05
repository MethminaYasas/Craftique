package com.s23010903.craftique;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CustomOrderActivity extends AppCompatActivity {

    EditText editOrderName, editOrderDescription, editOrderPrice;
    Button btnSubmitOrder;
    CustomOrderDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_order);

        editOrderName = findViewById(R.id.editOrderName);
        editOrderDescription = findViewById(R.id.editOrderDescription);
        editOrderPrice = findViewById(R.id.editOrderPrice);
        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);

        dbHelper = new CustomOrderDatabaseHelper(this);

        btnSubmitOrder.setOnClickListener(v -> {
            String name = editOrderName.getText().toString().trim();
            String description = editOrderDescription.getText().toString().trim();
            String priceStr = editOrderPrice.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                long result = dbHelper.insertCustomOrder(name, description, price);
                if (result != -1) {
                    Toast.makeText(this, "Order submitted successfully", Toast.LENGTH_SHORT).show();
                    editOrderName.setText("");
                    editOrderDescription.setText("");
                    editOrderPrice.setText("");
                } else {
                    Toast.makeText(this, "Failed to submit order", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
