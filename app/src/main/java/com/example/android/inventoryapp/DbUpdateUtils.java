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

    /**
     * Helper method to insert product data into the database.
     */
    public static void fillDatabase(Context context) {

        // Create ContentValues for a product
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Bananas");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE_URI, "file://sdcard/256px-Bananas.jpg");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 1.23);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 100);
        values.put(ProductEntry.COLUMN_PRODUCT_REORDER_RATE, ProductEntry.REORDER_WEEKLY);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, "fruits.from_SA@web.southafrica.com");

        // Insert the product into the database table
        context.getContentResolver().insert(ProductEntry.CONTENT_URI, values);

        // Create ContentValues for a product
        values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Green Olives");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE_URI, "file://sdcard/green_olives.jpg");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 1.56);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 50);
        values.put(ProductEntry.COLUMN_PRODUCT_REORDER_RATE, ProductEntry.REORDER_MONTHLY);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, "best.offers@greek-olives.gr");

        // Insert the product into the database table
        context.getContentResolver().insert(ProductEntry.CONTENT_URI, values);

        // Create ContentValues for a product
        values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "German Bread");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE_URI, "file://sdcard/german-bread.jpg");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 2.80);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 80);
        values.put(ProductEntry.COLUMN_PRODUCT_REORDER_RATE, ProductEntry.REORDER_WEEKLY);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, "Brotangebote-weltweit@Baeckerei-Meyer.de");

        // Insert the product into the database table
        context.getContentResolver().insert(ProductEntry.CONTENT_URI, values);

        // Create ContentValues for a product
        values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Red Vine");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE_URI, "file://sdcard/red-vine.jpg");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 8.20);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 30);
        values.put(ProductEntry.COLUMN_PRODUCT_REORDER_RATE, ProductEntry.REORDER_MONTHLY);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, "agriculture-biologique@vin-rouges.fr");

        // Insert the product into the database table
        context.getContentResolver().insert(ProductEntry.CONTENT_URI, values);

        // Create ContentValues for a product
        values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Pesto rosso");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE_URI, "file://sdcard/pesto-rosso.jpg");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 3.50);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 70);
        values.put(ProductEntry.COLUMN_PRODUCT_REORDER_RATE, ProductEntry.REORDER_WEEKLY);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, "delicatessi@vitaitalia.it");

        // Insert the product into the database table
        context.getContentResolver().insert(ProductEntry.CONTENT_URI, values);

        // Create ContentValues for a product
        values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Budweiser beer");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE_URI, "file://sdcard/budweise-beer.jpg");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 1.00);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 150);
        values.put(ProductEntry.COLUMN_PRODUCT_REORDER_RATE, ProductEntry.REORDER_WEEKLY);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, "traditional-beer@czech-beer.cz");

        // Insert the product into the database table
        context.getContentResolver().insert(ProductEntry.CONTENT_URI, values);

        // Create ContentValues for a product
        values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Spaghetti");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE_URI, "file://sdcard/spaghetti.jpg");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 0.80);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 400);
        values.put(ProductEntry.COLUMN_PRODUCT_REORDER_RATE, ProductEntry.REORDER_DAILY);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, "italian-food-store@don-pedro.it");

        // Insert the product into the database table
        context.getContentResolver().insert(ProductEntry.CONTENT_URI, values);

        // Create ContentValues for a product
        values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Frutti di mare");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE_URI, "file://sdcard/frutti-di-mare.jpg");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 12.70);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 40);
        values.put(ProductEntry.COLUMN_PRODUCT_REORDER_RATE, ProductEntry.REORDER_WEEKLY);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, "frutti-di-mare@italianstore.com");

        // Insert the product into the database table
        context.getContentResolver().insert(ProductEntry.CONTENT_URI, values);

        // Create ContentValues for a product
        values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Häagen Dasz");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE_URI, "file://sdcard/danish-ice.jpg");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 2.19);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 200);
        values.put(ProductEntry.COLUMN_PRODUCT_REORDER_RATE, ProductEntry.REORDER_WEEKLY);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, "ice-dreams@haeagen-dasz.dm");

        // Insert the product into the database table
        context.getContentResolver().insert(ProductEntry.CONTENT_URI, values);

        // Create ContentValues for a product
        values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Red Tomatoes");
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE_URI, "file://sdcard/red-tomatoes.jpg");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 0.90);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 300);
        values.put(ProductEntry.COLUMN_PRODUCT_REORDER_RATE, ProductEntry.REORDER_DAILY);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, "fresh-foods@hollandia.nl");

        // Insert the product into the database table
        context.getContentResolver().insert(ProductEntry.CONTENT_URI, values);

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
