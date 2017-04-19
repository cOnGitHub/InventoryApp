package com.example.android.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API contract for the Inventory App.
 *
 * Created by Christi on 19.04.2017.
 */
public final class ProductContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private ProductContract() {}

    /**
     * The content provider.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";

    /**
     * The base content URI for the app.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path leading to the product data.
     */
    public static final String PATH_PRODUCTS = "products";


    /**
     * Inner class that defines constant values for the products database table.
     * Each entry in the table represents a single product.
     */
    public static final class ProductEntry implements BaseColumns {

        /**
         * MIME type of the CONTENT_URI of a list of products.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * MIME type of the CONTENT_URI of a single product.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /** Name of database table for products */
        public static final String TABLE_NAME = "products";

        /**
         * Unique ID number for the product (only for use in the database table).
         *
         * Type: INTEGER
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Name of the product.
         *
         * Type: TEXT
         */
        public static final String COLUMN_PRODUCT_NAME ="name";

        /**
         * Description of the product.
         *
         * Type: TEXT
         */
        public static final String COLUMN_PRODUCT_DESCRIPTION = "description";

        /**
         * Price of the product.
         *
         * Type: INTEGER
         */
        public static final String COLUMN_PRODUCT_PRICE = "price";

        /**
         * Quantity of the product.
         *
         * Type: INTEGER
         */
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";

        /**
         * Reorder rate of the product.
         *
         * Possible values are {@link #REORDER_DAILY}, {@link #REORDER_WEEKLY},
         * {@link #REORDER_MONTHLY} or {@link #REORDER_YEARLY}.
         *
         * Type: INTEGER
         */
        public static final String COLUMN_PRODUCT_REORDER_RATE = "reorder_rate";

        /**
         * Supplier email for the product.
         *
         * Type: TEXT
         */
        public static final String COLUMN_PRODUCT_SUPPLIER_EMAIL = "supplier_email";

        /**
         * Possible values for the Reorder rate of the product.
         */
        public static final int REORDER_DAILY = 0;
        public static final int REORDER_WEEKLY = 1;
        public static final int REORDER_MONTHLY = 2;
        public static final int REORDER_YEARLY = 3;

        /**
         * Returns whether or not the given reorder rate is valid.
         */
        public static boolean isValidReorderRate(int reorderRate) {
            if (reorderRate == REORDER_DAILY || reorderRate == REORDER_WEEKLY || reorderRate == REORDER_MONTHLY || reorderRate == REORDER_YEARLY) {
                return true;
            }
            return false;
        }

        /**
         * Returns whether or not the given supplier email is valid.
         * Note that email validation is simplified, for demonstration purposes only.
         */
        public static boolean isValidSupplierEmail(String supplierEmail) {
            String[] splitAtAtSymbol = supplierEmail.split("@");
            // Only if array contains exactly two parts, the email contains exactly one at-symbol
            // which does not occur at the beginning nor at the end
            if (splitAtAtSymbol.length == 2) {
                // Only if email part after the at-symbol contanins only one dot
                // which does not occur at the beginnning nor at the end
                if (splitAtAtSymbol[1].split("\\.").length == 2) {
                    return true;
                }
            }
            return false;
        }
    }

}

