package com.example.foodiffin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodiffin.R;
import com.example.foodiffin.models.HomepageFoodListingModel;

import java.util.HashMap;
import java.util.Locale;

public class OrderFood extends AppCompatActivity {
    HomepageFoodListingModel homepageFoodListingModel = null;
    Button btnPromoCode,confirmOrderButton;
    TextView ThaliNametextView, ThaliPricetextView,totalAmountTextView,serviceFeeTextView,taxTextView,promoCodeHeading,promoCodePrice;
    HashMap<String,Integer> promoCodes = new HashMap<String, Integer>();
    EditText promoEditText;
    double tax, serviceFee, deliveryFee,priceOfFood,totalAmount;
    boolean promoStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food);

        ThaliNametextView = findViewById(R.id.ThaliNametextView);
        ThaliPricetextView = findViewById(R.id.ThaliPricetextView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        serviceFeeTextView = findViewById(R.id.serviceFeeTextView);
        btnPromoCode = findViewById(R.id.btnPromoCode);
        promoEditText = findViewById(R.id.promoEditText);
        taxTextView = findViewById(R.id.taxTextView);
        promoCodeHeading = findViewById(R.id.promoCodeHeading);
        promoCodePrice = findViewById(R.id.promoCodePrice);
        confirmOrderButton = findViewById(R.id.confirmOrderButton);


        promoCodePrice.setVisibility(View.GONE);
        promoCodeHeading.setVisibility(View.GONE);

        promoCodes.put("WEEKLY10",10);
        promoCodes.put("DAILY5",5);
        promoCodes.put("SUMMER5",5);
        promoCodes.put("FRIENDLY10",10);
        promoCodes.put("GOOD15",15);

        final Object object = getIntent().getSerializableExtra("detail");
        if(object instanceof HomepageFoodListingModel)
        {
            homepageFoodListingModel = (HomepageFoodListingModel) object;
        }
        if(homepageFoodListingModel != null)
        {
            priceOfFood = homepageFoodListingModel.getFoodPrice();
            tax = Math.round((priceOfFood*0.13)*100.0)/100.0;
            deliveryFee = 4.99;
            serviceFee = 4.00;
            totalAmount = priceOfFood + tax + serviceFee + deliveryFee;
            ThaliNametextView.setText(homepageFoodListingModel.getFoodTitle());
            ThaliPricetextView.setText("$ "+priceOfFood+"/Per Month");
            totalAmountTextView.setText("$ "+Math.round(totalAmount*100.0)/100.0);
            serviceFeeTextView.setText("$ "+serviceFee);
            taxTextView.setText("$ "+tax);
        }

        btnPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!promoEditText.getText().toString().isEmpty())
                {
                    checkCode(promoEditText.getText().toString());
                }else
                {
                    Toast.makeText(OrderFood.this,"Please enter Promo Code", Toast.LENGTH_SHORT).show();
                }

            }
        });

        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homepageFoodListingModel.setTotalAmount(totalAmount);
                Intent paymentIntent = new Intent(OrderFood.this,PaymentActivity.class);
                paymentIntent.putExtra("detail",homepageFoodListingModel);
                startActivity(paymentIntent);
            }
        });
    }

    private void checkCode(String userPromoCode) {
        for(String promoCode : promoCodes.keySet())
        {
            if(promoCode.equals(userPromoCode.toUpperCase(Locale.ROOT)))
            {
                totalAmount = totalAmount - promoCodes.get(promoCode);
                promoCodePrice.setVisibility(View.VISIBLE);
                promoCodePrice.setText("$ "+promoCodes.get(promoCode));
                promoCodeHeading.setVisibility(View.VISIBLE);
                promoCodeHeading.setText(promoCode.toUpperCase(Locale.ROOT));
                totalAmountTextView.setText("$ "+Math.round(totalAmount*100.0)/100.0);
                promoEditText.setEnabled(false);
                promoEditText.setFocusable(false);
                btnPromoCode.setEnabled(false);
                btnPromoCode.getBackground().setAlpha(75);
                promoStatus = true;
                break;
            }
        }
        if(!(promoStatus == true))
        {
            Toast.makeText(OrderFood.this,"Please enter valid Promo Code", Toast.LENGTH_SHORT).show();
        }
    }
}