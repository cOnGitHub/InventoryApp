package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.android.inventoryapp.data.ProductContract;
import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

import java.io.File;

/**
 * Created by Christi on 20.04.2017.
 */

public class DbUpdateUtils {

    private Uri storedContentUri;

    /**
     * Private empty constructor; there is no need to
     * have objects created for this class
     */
    private DbUpdateUtils() {

    }

    /**
     * Helper method that reduces the quantity value of
     * a product
     *
     * @param contentUri
     */
    public static int reduceQuantity(Uri contentUri, Context context) {

        // Number of affected rows of the update
        int rowsAffected = 0;

        // Projection that contains all columns from the products table
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_IMAGE_URI,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_REORDER_RATE,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL};

        // Query the database table
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);

        // Move cursor to first, otherwise it will be at position -1 and this will throw an error in the next line
        cursor.moveToFirst();

        // Get the quantity of the product
        int quantity = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));

        if (quantity > 1) {

            // Decrement quantity by 1
            quantity = quantity - 1;

            // Defines an object to contain the updated values
            ContentValues updateValues = new ContentValues();
            updateValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);

            // Update row in the table of the database
            rowsAffected = context.getContentResolver().update(contentUri, updateValues, null, null);
        }
        return rowsAffected;
    }

    /**
     * Helper method that increases the quantity value of
     * a product
     *
     * @param contentUri
     */
    public static int increaseQuantity(Uri contentUri, Context context) {

        // Number of affected rows of the update
        int rowsAffected = 0;

        // Projection that contains all columns from the products table
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_IMAGE_URI,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_REORDER_RATE,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL};

        // Query the database table
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);

        // Move cursor to first, otherwise it will be at position -1 and this will throw an error in the next line
        cursor.moveToFirst();

        // Get the quantity of the product
        int quantity = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));

        // increase quantity by 1
        quantity = quantity + 1;

        // Defines an object to contain the updated values
        ContentValues updateValues = new ContentValues();
        updateValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);

        // Update row in the table of the database
        rowsAffected = context.getContentResolver().update(contentUri, updateValues, null, null);


        return rowsAffected;
    }

    public static Bitmap loadImage(String pathToImage) {
        Bitmap myBitmap = null;
        File imgFile = new File(pathToImage);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //imageView.setImageBitmap(myBitmap);
        }
        return myBitmap;
    }

}
