package com.example.android.inventoryapp;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    // Log tag of the class
    private final String LOG_TAG = this.getClass().getSimpleName();

    // Identify the product data loader
    private static final int PRODUCT_LOADER = 0;

    // Adapter for the list view
    private ProductCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup button to open AddProductActivity
        Button addProductButton = (Button) findViewById(R.id.add_button);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        // Find the list view
        ListView productListView = (ListView) findViewById(R.id.list_view);

        // Find empty view and set it to the list view
        View emptyView = findViewById(R.id.empty_state_text_view);
        productListView.setEmptyView(emptyView);

        // Setup the cursor adapter
        mCursorAdapter = new ProductCursorAdapter(this, null);
        productListView.setAdapter(mCursorAdapter);

        // Setup the OnItemClickListener
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to open the DetailActivity
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);

                // Add the ID of the row to the content URI
                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentProductUri);

                // Launch the DetailsActivity to display the data for the current product
                startActivity(intent);
            }
        });

        // Start the loader
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);

    }

    /**
     * Helper method to insert product data into the database.
     */
    public void insertProduct(Context context) {

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Banana");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE_URI, "Banana from South Africa - biological");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 1.23);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 100);
        values.put(ProductEntry.COLUMN_PRODUCT_REORDER_RATE, ProductEntry.REORDER_WEEKLY);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, "fruits.from_SA@web.southafrica.com");

        // Insert a new row for the data in the ContenValues and get the new content URI
        // of the new product
        Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // The projection indicating the columns of the table
        String[] projection = {ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_IMAGE_URI,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_REORDER_RATE,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL};

        // Uncomment the next two lines if you want to use selection together with selecitonArgs
        // String selection = ProductEntry.COLUMN_PRODUCT_REORDER_RATE + "=?";
        // String[] selectionArgs = new String[]{String.valueOf(ProductEntry.REORDER_DAILY)};

        // Return the loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                ProductEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the cursor when loading has finished
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Set cursor to null on reset
        mCursorAdapter.swapCursor(null);
    }
}

