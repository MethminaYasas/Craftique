package com.s23010903.craftique;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OrderManagementActivity extends AppCompatActivity {

    LinearLayout ordersContainer;
    TextView tvNoOrders;
    CustomOrderDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        ordersContainer = findViewById(R.id.ordersContainer);
        tvNoOrders = findViewById(R.id.tvNoOrders);

        dbHelper = new CustomOrderDatabaseHelper(this);

        loadOrders();
    }

    private void loadOrders() {
        ordersContainer.removeAllViews();

        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT * FROM " + CustomOrderDatabaseHelper.TABLE_CUSTOM_ORDERS, null);

        if (cursor != null && cursor.moveToFirst()) {
            tvNoOrders.setVisibility(TextView.GONE);

            do {
                String orderName = cursor.getString(cursor.getColumnIndexOrThrow("orderName"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("estimatedPrice"));

                TextView orderView = new TextView(this);
                orderView.setText("\u2022 " + orderName + "\n" +
                        description + "\nEstimated Price: Rs. " + price);
                orderView.setTextSize(16);
                orderView.setPadding(0, 16, 0, 16);

                ordersContainer.addView(orderView);

            } while (cursor.moveToNext());

            cursor.close();
        } else {
            // No orders found
            tvNoOrders.setVisibility(TextView.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
}
