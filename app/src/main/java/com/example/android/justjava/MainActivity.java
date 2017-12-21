/**
 * IMPORTANT: Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

package com.example.android.justjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;
    int priceOfCup = 3;
    String name = "Raivo Lapins";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void increment(View view) {
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    public void decrement(View view) {
        if (quantity <= 0) {
            quantity = 0;
        } else {
            quantity = quantity - 1;
            displayQuantity(quantity);
            int priceOfCup = 3;
            calculatePrice(quantity, priceOfCup);
        }
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
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        int priceTotal = calculatePrice(quantity, priceOfCup);
        displayMessage(createOrderSummary(priceTotal));
    }

    /**
     * Create summary of the order.
     * @param price of the order
     * @return text summary
     */
    private String createOrderSummary(int price) {
        String priceMessage = "Name: " + name;
        priceMessage += "\nQuantity: " + quantity;
        priceMessage += "\nTotal: " + NumberFormat.getCurrencyInstance().format(price);
        priceMessage += "\nThank you!";
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */

    private void displayQuantity(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    private void displayMessage(String message) {
        TextView priceTextView = findViewById(R.id.price_number_view);
        priceTextView.setText(message);
    }
}