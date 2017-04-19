package com.example.android.inventoryapp.data;

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
     * Inner class that defines constant values for the products database table.
     * Each entry in the table represents a single product.
     */
    public static final class ProductEntry implements BaseColumns {

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
    }

}

