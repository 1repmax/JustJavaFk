/**
 * IMPORTANT: Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

package com.example.android.justjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    int quantity = 0;
    int priceOfCup = 3;

    String name = "Raivo Lapins";
    TextView quantityTextview, priceTextview;
    Button incrementAmount, decrementAmount, submitOrder;
    CheckBox whippedCreamCheckbox, chocolateCheckbox;
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
    }

    //Restore states of CheckBoxes and TextViews
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        quantity = savedInstanceState.getInt(QUANTITY_TEXT);
        displayQuantity(quantity);
        int priceTotal = savedInstanceState.getInt(PRICE_TOTAL);
        hasWhippedCream = savedInstanceState.getBoolean(WHIPPED_CREAM);
        hasChocolate = savedInstanceState.getBoolean(CHOCOLATE_TOPPING);
        displayMessage(createOrderSummary(priceTotal, hasWhippedCream, hasChocolate));
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

        whippedCreamCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
                if (whippedCreamCheckbox.isChecked())
                {
                    hasWhippedCream = true;
                }
                else{
                    hasWhippedCream = false;
                }
            }
        });

        chocolateCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
                if (chocolateCheckbox.isChecked())
                {
                    hasChocolate = true;
                }
                else{
                    hasChocolate = false;
                }
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
                displayMessage(createOrderSummary(priceTotal, hasWhippedCream, hasChocolate));
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
            }
        });

        /**
         * This method is called when the decrement button is clicked. Removes amount of coffees ordered by 1.
         */
        decrementAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity <= 0) {
                    quantity = 0;
                } else {
                    quantity = quantity - 1;
                    displayQuantity(quantity);
                    int priceOfCup = 3;
                    calculatePrice(quantity, priceOfCup);
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
        return quantity * priceOfCup;
    }

    /**
     * Create summary of the order.
     *
     * @param price of the order
     * @return text summary
     */
    private String createOrderSummary(int price, boolean addWhippedCream, boolean addChocolate) {

        String priceMessage = "Name: " + name;
        priceMessage += "\nAdd whipped cream? " + addWhippedCream;
        priceMessage += "\nAdd chocolate? " + addChocolate;
        priceMessage += "\nQuantity: " + quantity;
        priceMessage += "\nTotal: " + NumberFormat.getCurrencyInstance().format(price);
        priceMessage += "\nThank you!";
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */

    private void displayQuantity(int number) {
        quantityTextview.setText("" + number);
    }

    private void displayMessage(String message) {
        priceTextview.setText(message);
    }
}