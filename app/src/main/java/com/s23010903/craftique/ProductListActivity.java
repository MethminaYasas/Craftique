// ProductListActivity.java
package com.s23010903.craftique;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProductListActivity extends AppCompatActivity {

    private LinearLayout productContainer;
    private Button btnAddProduct;
    private ProductDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        productContainer = findViewById(R.id.productContainer);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        dbHelper = new ProductDatabaseHelper(this);

        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        loadProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    private void loadProducts() {
        productContainer.removeAllViews();

        Cursor cursor = null;
        try {
            cursor = dbHelper.getAllProducts();

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));

                    TextView textView = new TextView(this);
                    textView.setText(name + "\n" + description + "\nPrice: Rs." + price);
                    textView.setPadding(16, 16, 16, 16);
                    textView.setTextSize(16f);

                    productContainer.addView(textView);
                } while (cursor.moveToNext());
            } else {
                TextView empty = new TextView(this);
                empty.setText("No products found.");
                empty.setPadding(16, 16, 16, 16);
                productContainer.addView(empty);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TextView errorView = new TextView(this);
            errorView.setText("Error loading products: " + e.getMessage());
            errorView.setPadding(16, 16, 16, 16);
            productContainer.addView(errorView);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}