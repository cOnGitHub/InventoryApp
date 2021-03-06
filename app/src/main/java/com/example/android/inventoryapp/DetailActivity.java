package com.example.android.inventoryapp;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

import java.io.File;


/**
 * DetailActivitiy for displaying details of a selected product.
 * <p>
 * Created by Christi on 19.04.2017.
 */
public class DetailActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    // Identify the data loader
    private static final int EXISTING_PRODUCT_LOADER = 0;

    // Content URI for the existing product (null if it's a new product)
    private Uri mCurrentProductUri;

    // Delete button
    private Button mDeleteButton;

    // Order button
    private Button mOrderButton;

    // Product name text view
    private TextView mProductNameTextView;

    // Image URI text view
    private ImageView mImageView;

    // Price text view
    private TextView mPriceTextView;

    // Quantity text view
    private TextView mQuantityTextView;

    // Shipment button ('+')
    private Button mShipmentButton;

    // Sales button ('-')
    private Button mSalesButton;

    // Reorder rate text view
    private TextView mReorderRateTextView;

    // Supplier email text view
    private TextView mSupplierEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Delete button
        mDeleteButton = (Button) findViewById(R.id.details_delete_button);

        // Order button
        mOrderButton = (Button) findViewById(R.id.details_order_button);

        // Product name text view
        mProductNameTextView = (TextView) findViewById(R.id.details_product_name_text_view);

        // Image URI text view
        mImageView = (ImageView) findViewById(R.id.details_image_view);

        // Price text view
        mPriceTextView = (TextView) findViewById(R.id.details_price_text_view);

        // Quantity text view
        mQuantityTextView = (TextView) findViewById(R.id.details_quantity_text_view);

        // Shipment button ('+')
        mShipmentButton = (Button) findViewById(R.id.details_shipment_button);

        // Sales button ('-')
        mSalesButton = (Button) findViewById(R.id.details_sales_button);

        // Reorder rate text view
        mReorderRateTextView = (TextView) findViewById(R.id.details_reorder_rate_text_view);

        // Supplier email text view
        mSupplierEmailTextView = (TextView) findViewById(R.id.details_supplier_email_text_view);

        // Get the content URI from the intent that was used to launch this activity.
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        // Initialize a loader to read the product data from the database
        // and display the current values in the editor
        getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);

        // Set OnClickListener on shipment button
        mShipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Execute helper method for increase of quantity
                int rowsAffected = DbUpdateUtils.increaseQuantity(mCurrentProductUri, getApplication());
            }
        });

        // Set OnClickListener on sales button
        mSalesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Execute helper method for reduction of quantity
                int rowsAffected = DbUpdateUtils.reduceQuantity(mCurrentProductUri, getApplication());
            }
        });

        // Set OnClickListener on delete button
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailActivity.this);
                builder1.setTitle(R.string.details_delete_alert_heading);
                builder1.setMessage(R.string.details_delete_alert_information);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        R.string.details_delete_alert_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // delete the product
                                int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
                                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                                startActivity(intent);

                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        R.string.details_delete_alert_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }

        });

        // Set OnClickListener on order button
        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/html");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, mSupplierEmailTextView.getText());
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Order");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, please send me your products list so that I can execute any order. Regards");

                startActivity(Intent.createChooser(emailIntent, "Send Email"));
            }
        });


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Projection that contains all columns from the products table
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_IMAGE_URI,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_REORDER_RATE,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentProductUri,         // Query the content URI for the current product
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
            int imageUriColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE_URI);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int reorderRateColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_REORDER_RATE);
            int supplierEmailColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String imageUriString = cursor.getString(imageUriColumnIndex);
            double price = cursor.getDouble(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int reorderRate = cursor.getInt(reorderRateColumnIndex);
            String supplierEmail = cursor.getString(supplierEmailColumnIndex);

            // Update the views on the screen with the values from the database
            mProductNameTextView.setText(name);

            // Set Bitmap for image view
            File imgFile = new File(imageUriString);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mImageView.setImageBitmap(myBitmap);
            }
            mPriceTextView.setText(Double.toString(price));
            mQuantityTextView.setText(Integer.toString(quantity));
            // select string corresponding to reorder rate
            String reorderRateString;
            switch (reorderRate) {
                case 1:
                    reorderRateString = getResources().getString(R.string.reorder_rate_weekly);
                case 2:
                    reorderRateString = getResources().getString(R.string.reorder_rate_monthly);
                case 3:
                    reorderRateString = getResources().getString(R.string.reorder_rate_yearly);
                default:
                    reorderRateString = getResources().getString(R.string.reorder_rate_daily);
            }
            mReorderRateTextView.setText(reorderRateString);
            mSupplierEmailTextView.setText(supplierEmail);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mProductNameTextView.setText("");
        mImageView.invalidate();
        mPriceTextView.setText("");
        mQuantityTextView.setText("");
        mReorderRateTextView.setText("");
        mSupplierEmailTextView.setText("");
    }

}