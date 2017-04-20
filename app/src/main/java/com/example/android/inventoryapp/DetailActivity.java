package com.example.android.inventoryapp;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;


/**
 * Created by Christi on 19.04.2017.
 */

public class DetailActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {


    // Identify the data loader
    private static final int EXISTING_PRODUCT_LOADER = 0;

    // Content URI for the existing product (null if it's a new product)
    private Uri mCurrentProductUri;

    // Product name text view
    private TextView mProductNameTextView;

    // Description text view
    private TextView mDescriptionTextView;

    // Price text view
    private TextView mPriceTextView;

    // Quantity text view
    private TextView mQuantityTextView;

    // Reorder rate text view
    private TextView mReorderRateTextView;

    // Supplier email text view
    private TextView mSupplierEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get the content URI from the intent that was used to launch this activity.
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        // Initialize a loader to read the pet data from the database
        // and display the current values in the editor
        getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Projection that contains all columns from the products table
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_DESCRIPTION,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_REORDER_RATE,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentProductUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of product attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_DESCRIPTION);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int reorderRateColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_REORDER_RATE);
            int supplierEmailColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String description = cursor.getString(descriptionColumnIndex);
            double price = cursor.getDouble(descriptionColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int reorderRate = cursor.getInt(reorderRateColumnIndex);
            String supplierEmail = cursor.getString(supplierEmailColumnIndex);

            // Update the views on the screen with the values from the database
            mProductNameTextView.setText(name);
            mDescriptionTextView.setText(description);
            mPriceTextView.setText(Double.toString(price));
            mQuantityTextView.setText(Integer.toString(quantity));
            mReorderRateTextView.setText(Integer.toString(reorderRate));
            mSupplierEmailTextView.setText(supplierEmail);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mProductNameTextView.setText("");
        mDescriptionTextView.setText("");
        mPriceTextView.setText(Double.toString(0.0));
        mQuantityTextView.setText(Integer.toString(0));
        mReorderRateTextView.setText(Integer.toString(0));
        mSupplierEmailTextView.setText("");
    }

}