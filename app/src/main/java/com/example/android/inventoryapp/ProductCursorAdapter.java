package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ProductContract;

/**
 * Cursor Adapter class for populating the list view from the database.
 * Created by Christi on 19.04.2017.
 */

public class ProductCursorAdapter extends CursorAdapter {

    /**
     * Constructor of the ProductCursorAdapter.
     *
     * @param context in which the object is created
     * @param c       is the Curser used to get data from a database
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);

    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find price text view
        TextView priceTextView = (TextView) view.findViewById(R.id.price_text_view);

        // Find product name text view
        TextView productNameTextView = (TextView) view.findViewById(R.id.product_name_text_view);

        // Find quantity text view
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity_text_view);

        // Extract properties from cursor
        Double price = cursor.getDouble(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE));
        String productName = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
        int quantity = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));

        // Set data to the text views
        priceTextView.setText(price.toString());
        productNameTextView.setText(productName);
        quantityTextView.setText(Integer.toString(quantity));
    }
}
