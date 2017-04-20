package com.example.android.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Content provider for the inventory app.
 *
 * Created by Christi on 19.04.2017.
 */

public class ProductProvider extends ContentProvider {

    // Log tag
    public static final String LOG_TAG = ContentProvider.class.getSimpleName();

    // URI matcher code for the content URI for the products table
    private static final int PRODUCTS = 100;

    // URI matcher code for the content URI for a single product in the table
    private static final int PRODUCT_ID = 101;

    /**
     * The UriMatcher which maps a content URI to a corresponding code.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.products/products" will map to the
        // integer code PRODUCTS.
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS, PRODUCTS);

        // The content URI of the form "content://com.example.android.products/products/#" will map to the
        // integer code PRODUCT_ID.
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
    }

    // Helper object for the database
    private ProductDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // The cursor used in the query
        Cursor cursor;

        // Try to match the URI to its integer
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                // query the database for the whole table
                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PRODUCT_ID:
                // Get the selection with its ID
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // Query the database for this ID
                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Supply the cursor with the notification URI so that changes can be notified later
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a product into the database and return the new content URI of this product
     */
    private Uri insertProduct(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product requires a name");
        }

        // Check that the price is not null and that it's greater than or equal to 0.0
        Double price = values.getAsDouble(ProductEntry.COLUMN_PRODUCT_PRICE);
        if (price == null || price < 0.0) {
            throw new IllegalArgumentException("Product requires a valid price");
        }

        // Check that the quantity is not null and that it's greater than or equal to 0
        Integer quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Product requires a valid quantity");
        }

        // Check that the reorder rate is valid
        Integer reorderRate = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_REORDER_RATE);
        if (!ProductEntry.isValidReorderRate(reorderRate)) {
            throw new IllegalArgumentException("Product requires valid reorder rate");
        }

        // Check that the supplier email is not null and that it's valid
        String supplierEmail = values.getAsString(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL);
        if (supplierEmail == null || !ProductEntry.isValidSupplierEmail(supplierEmail)) {
            throw new IllegalArgumentException("Product requires a valid supplier email");
        }

        // No need to check the description, any value is valid (including null).

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new product into the table
        long id = database.insert(ProductEntry.TABLE_NAME, null, values);
        // A return of -1 indicates an error.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify each one listening to changes on the database for the content URI in question
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the content URI for the inserted data row, together with its ID
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                // Get selection together with the ID for the row to be updated
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update the products in the rows selected. Return the number of rows changed.
     */
    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the product name key is present, check that the name value is not null
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }

        // If the price key is present, check that the price value is not null
        // and that it's greater than or equal to 0.0
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE)) {
            Double price = values.getAsDouble(ProductEntry.COLUMN_PRODUCT_PRICE);
            if (price == null || price < 0.0) {
                throw new IllegalArgumentException("Product requires a valid price");
            }
        }

        // If the quantity key is present, check that the quantity value is not null
        // and that it's greater than or equal to 0
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY)) {
            Integer quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity == null || quantity < 0) {
                throw new IllegalArgumentException("Product requires a valid quantity");
            }
        }

        // If the reorder rate key is present, check that the reorder rate value is valid
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_REORDER_RATE)) {
            Integer reorderRate = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_REORDER_RATE);
            if (!ProductEntry.isValidReorderRate(reorderRate)) {
                throw new IllegalArgumentException("Product requires valid reorder rate");
            }
        }

        // If the supplier email key is present, check that the supplier email value is not null
        // and that it's valid
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL)) {
            String supplierEmail = values.getAsString(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL);
            if (supplierEmail == null || !ProductEntry.isValidSupplierEmail(supplierEmail)) {
                throw new IllegalArgumentException("Product requires a valid supplier email");
            }
        }

        // No need to check the description, any value is valid (including null).

        // If there are no values contained, return 0, that is without any changes
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of updated rows
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // The number of row that got deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                // Delete a single row given by the ID in the URI
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

}
