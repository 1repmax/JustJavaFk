/**
 * IMPORTANT: Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    //Declare keys for storing and restoring of essential information
    static final String QUANTITY_TEXT = "quantityTextview";
    static final String PRICE_TOTAL = "priceTotal";
    static final String WHIPPED_CREAM = "whippedCream";
    static final String CHOCOLATE_TOPPING = "chocolateTopping";
    static final String HAS_CREAM_BOOL = "hasCream";
    static final String HAS_CHOCO_BOOL = "hasChoco";
    static final String USERS_NAME = "username";

    int quantity = 1;
    int priceOfCup = 3;
    String name;
    TextView quantityTextview, priceTextview;
    Button incrementAmount, decrementAmount, submitOrder;
    CheckBox whippedCreamCheckbox, chocolateCheckbox;
    EditText nameField;
    boolean hasWhippedCream;
    boolean hasChocolate;


    //Save data into bundle
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(QUANTITY_TEXT, quantity);
        savedInstanceState.putInt(PRICE_TOTAL, calculatePrice(quantity, priceOfCup));
        savedInstanceState.putBoolean(WHIPPED_CREAM, hasWhippedCream);
        savedInstanceState.putBoolean(CHOCOLATE_TOPPING, hasChocolate);
        savedInstanceState.putBoolean(HAS_CREAM_BOOL, whippedCreamCheckbox.isChecked());
        savedInstanceState.putBoolean(HAS_CHOCO_BOOL, chocolateCheckbox.isChecked());
        savedInstanceState.putString(USERS_NAME, nameField.getText().toString());

    }

    //Restore states of CheckBoxes and TextViews
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        quantity = savedInstanceState.getInt(QUANTITY_TEXT);
        displayQuantity(quantity);
        int priceTotal = savedInstanceState.getInt(PRICE_TOTAL);
        hasWhippedCream = savedInstanceState.getBoolean(WHIPPED_CREAM);
        hasChocolate = savedInstanceState.getBoolean(CHOCOLATE_TOPPING);
        nameField.setText(savedInstanceState.getString(USERS_NAME));

        //Restore values of Checkbox state booleans
        boolean whippedCreamState = savedInstanceState.getBoolean(HAS_CREAM_BOOL);
        boolean chocoState = savedInstanceState.getBoolean(HAS_CHOCO_BOOL);

        //Pass the values of booleans to checkboxes to make sure they are
        whippedCreamCheckbox.setChecked(whippedCreamState);
        chocolateCheckbox.setChecked(chocoState);

        createOrderSummary(priceTotal, hasWhippedCream, hasChocolate, name);
    }

    //Creates the applications UI and initializes it's Views.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quantityTextview = findViewById(R.id.quantity_text_view);
        priceTextview = findViewById(R.id.price_number_view);
        submitOrder = findViewById(R.id.submit_order);
        incrementAmount = findViewById(R.id.increment_id);
        decrementAmount = findViewById(R.id.decrement_id);
        whippedCreamCheckbox = findViewById(R.id.whipped_checkbox);
        chocolateCheckbox = findViewById(R.id.chocolate_checkbox);
        nameField = findViewById(R.id.name_field);
        nameField.setHint(R.string.namefield_hint);

        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = nameField.getText().toString();
                createOrderSummary(calculatePrice(quantity, priceOfCup), hasWhippedCream, hasChocolate, name);
            }



        });

        whippedCreamCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
                if (whippedCreamCheckbox.isChecked()) {
                    hasWhippedCream = true;
                } else {
                    hasWhippedCream = false;
                }
                createOrderSummary(calculatePrice(quantity, priceOfCup), hasWhippedCream, hasChocolate, name);
            }
        });

        chocolateCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
                if (chocolateCheckbox.isChecked()) {
                    hasChocolate = true;
                } else {
                    hasChocolate = false;
                }
                createOrderSummary(calculatePrice(quantity, priceOfCup), hasWhippedCream, hasChocolate, name);
            }
        });

        /**
         * This method is called when the order button is clicked.
         */
        submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (whippedCreamCheckbox.isChecked()) {
                    hasWhippedCream = true;
                } else {
                    hasWhippedCream = false;
                }

                if (chocolateCheckbox.isChecked()) {
                    hasChocolate = true;
                } else {
                    hasChocolate = false;
                }

                int priceTotal = calculatePrice(quantity, priceOfCup);
                createOrderSummary(priceTotal, hasWhippedCream, hasChocolate, name);

                // Create a new intent to send confirmation email with a email app
                Intent sendConfirmationEmail = new Intent(Intent.ACTION_SENDTO);
                sendConfirmationEmail.setType("*/*");
                sendConfirmationEmail.setData(Uri.parse("mailto:"));
                sendConfirmationEmail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.caffeinn_order, name));
                sendConfirmationEmail.putExtra(Intent.EXTRA_TEXT, createOrderSummary(calculatePrice(quantity, priceOfCup), hasWhippedCream, hasChocolate, name));

                if (sendConfirmationEmail.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendConfirmationEmail);
                }
            }
        });

        /**
         * This method is called when the increment button is clicked. Adds amount of coffees ordered by 1.
         */
        incrementAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = quantity + 1;
                displayQuantity(quantity);
                createOrderSummary(calculatePrice(quantity, priceOfCup), hasWhippedCream, hasChocolate, name);
            }
        });

        /**
         * This method is called when the decrement button is clicked. Removes amount of coffees ordered by 1.
         */
        decrementAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity <= 1) {
                    quantity = 1;
                    Context context = getApplicationContext();
                    CharSequence text = getString(R.string.toast_message);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                } else {
                    quantity = quantity - 1;
                    displayQuantity(quantity);
                    int priceOfCup = 3;
                    calculatePrice(quantity, priceOfCup);
                    createOrderSummary(calculatePrice(quantity, priceOfCup), hasWhippedCream, hasChocolate, name);
                }
            }
        });
    }


    /**
     * Calculates the price of the order.
     *
     * @return total price
     */
    private int calculatePrice(int quantity, int priceOfCup) {

        if(hasChocolate && hasWhippedCream) {
            priceOfCup = 5;
        } else if (hasWhippedCream) {
            priceOfCup = 4;
        } else if (hasChocolate) {
            priceOfCup = 4;
        }

        return quantity * priceOfCup;
    }


    /**
     * Create summary of the order.
     *
     * @param price of the order
     * @return text summary
     */
    private String createOrderSummary(int price, boolean addWhippedCream, boolean addChocolate, String name) {
        String addWhippedCreamString;
        String addChocolateString;
        name = nameField.getText().toString();
        if(addWhippedCream){
            addWhippedCreamString = getString(R.string.order_summary_whipped_cream, addWhippedCream);
        }
        else {
            addWhippedCreamString = getString(R.string.order_summary_whipped_cream, addWhippedCream);
        }

        if(addChocolate){
            addChocolateString = getString(R.string.order_summary_chocolate, addChocolate);
        }
        else {
            addChocolateString = getString(R.string.order_summary_chocolate, addChocolate);
        }
        String priceMessage = "";


        if (quantity >= 1) {
            priceMessage = getString(R.string.user_name, name);
            priceMessage += "\n" + addWhippedCreamString;
            priceMessage += "\n" + addChocolateString;
            priceMessage += "\n" + getString(R.string.quantity_justjava, quantity);
            priceMessage += "\n" + getString(R.string.total_order_summary, NumberFormat.getCurrencyInstance().format(price));
            priceMessage += "\n" + getString(R.string.thank_you);
            return priceMessage;
        }
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */

    private void displayQuantity(int number) {
        quantityTextview.setText("" + number);
    }

//    private void displayMessage(String message) {
//        priceTextview.setText(message);
//    }
}