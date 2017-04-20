package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract;
import com.example.android.inventoryapp.data.ProductContract.ProductEntry;


/**
 * Accept data input from the user in order to add a new product to the database.
 * <p>
 * Created by Christi on 19.04.2017.
 */
public class AddProductActivity extends AppCompatActivity {

    // Content URI for the existing product (null if it's a new product)
    private Uri mCurrentProductUri;

    // EditText field to enter the product's name
    private EditText mNameEditText;

    // EditText field to enter the product's description
    private EditText mDescriptionEditText;

    // EditText field to enter the product's price
    private EditText mPriceEditText;

    // EditText field to enter the product's quantity
    private EditText mQuantityEditText;

    // EditText field to enter the products's reorder rate
    private Spinner mReorderRateSpinner;

    // EditText field to enter the product's supplier email
    private EditText mSupplierEmailEditText;

    // Button to save the product to the database
    private Button mSaveButton;

    /**
     * Reorder rate of the pet. The possible valid values are:
     * {@link ProductEntry#REORDER_DAILY}, {@link ProductEntry#REORDER_WEEKLY},
     * {@link ProductEntry#REORDER_MONTHLY}, or {@link ProductEntry#REORDER_YEARLY}.
     */
    private int mReorderRate = ProductContract.ProductEntry.REORDER_WEEKLY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new pet or editing an existing one.
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mDescriptionEditText = (EditText) findViewById(R.id.edit_product_description);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_product_quantity);
        mReorderRateSpinner = (Spinner) findViewById(R.id.spinner_reorder_rate);
        mSupplierEmailEditText = (EditText) findViewById(R.id.edit_product_supplier_email);
        mSaveButton = (Button) findViewById(R.id.save_product_button);

        setupSpinner();

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });

    }

    /**
     * Setup the dropdown spinner that allows the user to select the reorder rate of the product.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_reorder_rate_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mReorderRateSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mReorderRateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.reorder_rate_daily))) {
                        mReorderRate = ProductEntry.REORDER_DAILY;
                    } else if (selection.equals(getString(R.string.reorder_rate_weekly))) {
                        mReorderRate = ProductEntry.REORDER_WEEKLY;
                    } else if (selection.equals(getString(R.string.reorder_rate_monthly))) {
                        mReorderRate = ProductEntry.REORDER_MONTHLY;
                    } else {
                        mReorderRate = ProductEntry.REORDER_YEARLY;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mReorderRate = ProductEntry.REORDER_WEEKLY;
            }
        });
    }

    /**
     * Get user input from add product page and save product into database.
     */
    private void saveProduct() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String descriptionString = mDescriptionEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierEmailString = mSupplierEmailEditText.getText().toString().trim();

        // Check if any of the fields for the name, price or supplier email is empty
        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(priceString) ||
                TextUtils.isEmpty(supplierEmailString)) {
            // Since these fields cannot be null in the database, we can let the user know
            // and return then
            Toast.makeText(this, getString(R.string.fields_are_still_empty),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and product attributes from the add product page are the values.
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(ProductEntry.COLUMN_PRODUCT_DESCRIPTION, descriptionString);
        values.put(ProductEntry.COLUMN_PRODUCT_REORDER_RATE, mReorderRate);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, supplierEmailString);


        // Parse the price string into a double value
        double price = Double.parseDouble(priceString);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);

        // If the quantity is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int quantity = 0;
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);

        // Insert the product into the provider and return the content URI for the new product.
        Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.add_product_page_insert_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.add_product_page_insert_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
