package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;
import com.example.android.inventoryapp.data.ProductDbHelper;


public class MainActivity extends AppCompatActivity {

    // Log tag of the class
    private final String LOG_TAG = this.getClass().getSimpleName();

    // Pet database helper
    private ProductDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        Button addProductButton = (Button) findViewById(R.id.add_button);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        insertProduct(this);
        mDbHelper = new ProductDbHelper(this);
        displayDatabaseInfo();
    }


    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_DESCRIPTION,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_REORDER_RATE,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL};
        String selection = ProductEntry.COLUMN_PRODUCT_REORDER_RATE + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ProductEntry.REORDER_DAILY)};

        Cursor cursor = db.query(ProductEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        // Display the number of rows in the Cursor (which reflects the number of rows in the
        // table in the database).
        // Find ListView to populate
        ListView listView = (ListView) findViewById(R.id.list_view);

        // Setup cursor adapter using cursor from last step
        ProductCursorAdapter productAdapter = new ProductCursorAdapter(this, cursor);

        // Attach cursor adapter to the ListView
        listView.setAdapter(productAdapter);
    }


    /**
     * Helper method to insert product data into the database.
     */
    public static void insertProduct(Context context) {

        // Database helper
        ProductDbHelper mDbHelper = new ProductDbHelper(context);

        // Get the database repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Banana");
        values.put(ProductEntry.COLUMN_PRODUCT_DESCRIPTION, "Banana from South Africa - biological");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 1.23);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 100);
        values.put(ProductEntry.COLUMN_PRODUCT_REORDER_RATE, ProductEntry.REORDER_WEEKLY);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, "fruits.from_SA@web.southafrica.com");

        // Insert the new row,returning primary kay value of the new row
        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);
    }


}
