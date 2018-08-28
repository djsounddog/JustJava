package com.example.android.justjava;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int quantity = 1;

    /**
     * This method is called when the order button is clicked. It calculates the total price and
     * sends an email to the coffee shop.
     */
    public void submitOrder(View view) {
        //This retrieves the users name
        String name = getCustomerName();

        //This checks if the user wants whipped cream
        boolean whippedCream = hasWhippedCream();

        //This checks if the user wants chocolate
        boolean chocolate = hasChocolate();

        //This calculates the full price of the order
        int price = calculatePrice(hasWhippedCream(), hasChocolate());

        String orderSummary = createOrderSummary(price, whippedCream, chocolate, name);

        String subject = getString(R.string.subject, name);

        composeEmail("coffee@coffee.com",subject,orderSummary);

    }

    /**
     * This method retrieves the customers name from the form
     */
    private String getCustomerName() {
        EditText editText = findViewById(R.id.name);
        return editText.getText().toString();
    }


    /**
     * This method triggers when a checkbox state is changed; it then recalculates teh total price
     * and updates it in the users view.
     *
     */
    public void onCheckboxClicked(View view) {
        switch(view.getId()) {
            case R.id.whipped_cream:
                displayPrice(calculatePrice(hasWhippedCream(), hasChocolate()));
                break;
            case R.id.chocolate:
                displayPrice(calculatePrice(hasWhippedCream(), hasChocolate()));
                break;
        }
    }


    /**
     * Calculates the price of the order
     *
     * @param addWhippedCream is whether the user wants whipped cream
     * @param addChocolate    is whether the user wants chocolate
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        //Price of 1 cup of coffee
        int basePrice = 5;
        //Add $1 if user wants whipped cream
        if (addWhippedCream) {
            basePrice += 1;
        }
        //Add $2 if user wants chocolate
        if (addChocolate) {
            basePrice += 2;
        }
        //calculate the total order price for the number of coffees ordered
        return quantity * basePrice;
    }

    /**
     * Checks if whipped cream is to be added
     */
    private boolean hasWhippedCream() {
        CheckBox checkBox = findViewById(R.id.whipped_cream);
        return checkBox.isChecked();
    }

    /**
     * Checks if chocolate is to be added
     */
    private boolean hasChocolate() {
        CheckBox checkBox = findViewById(R.id.chocolate);
        return checkBox.isChecked();
    }

    /**
     * Creates a summary of the completed order
     *
     * @param price of the full order
     * @return text summary version of order
     */
    private String createOrderSummary(int price, boolean whippedCream, boolean chocolate, String name) {
        String priceMessage = getString(R.string.order_summary_name, name);
        priceMessage += "\n" + getString(R.string.order_summary_quantity, quantity);
        priceMessage += "\n" + getString(R.string.order_summary_whipped_cream, yesNo(whippedCream));
        priceMessage += "\n" + getString(R.string.order_summary_chocolate, yesNo(chocolate));
        priceMessage += "\n" + getString(R.string.order_summary_price,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.order_summary_thanks);
        return priceMessage;
    }

    /**
     * Changes a boolean value to a more human readable string for form output
     *
     * @param value is boolean to convert to string Yes/No
     * @return is Yes for true, No for false
     */
    private String yesNo(boolean value) {
        if (value) {
            return getString(R.string.yes);
        } else {
            return getString(R.string.no);
        }
    }

    /**
     * This method increases the quantity.
     */
    public void increment(View view) {
        if (quantity != 100) {
            quantity++;
            displayQuantity(quantity);
            displayPrice(calculatePrice(hasWhippedCream(), hasChocolate()));
        }
    }

    /**
     * This method decreases the quantity.
     */
    public void decrement(View view) {
        if (quantity != 1) {
            quantity--;
            displayQuantity(quantity);
            displayPrice(calculatePrice(hasWhippedCream(), hasChocolate()));
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    @SuppressLint("SetTextI18n")
    private void displayQuantity(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given price value on the screen.
     */
    private void displayPrice(int price) {
        TextView viewPrice = findViewById(R.id.price_view);
        viewPrice.setText(NumberFormat.getCurrencyInstance().format(price));
    }

    /**
     * This method composes an email to send the order to the coffee shop.
     *
     * @param address is the shops email address
     * @param subject is teh email subject line
     * @param orderText is the email body text
     */
    public void composeEmail(String address, String subject, String orderText) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("*/*");
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, address);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, orderText);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}