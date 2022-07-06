package com.example.foodiffin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodiffin.R;
import com.example.foodiffin.models.HomepageFoodListingModel;

import java.util.Calendar;

public class PaymentActivity extends AppCompatActivity {

    HomepageFoodListingModel homepageFoodListingModel = null;
    Button btnPayement;
    DatePickerDialog picker;
    EditText nameEditTextPayment,phoneEditTextPayment,addressEditTextPayment,cardNumberEditTextPayment,cardExpiryEditTextPayment,postalCodeEditTextPayment,cvvEditTextPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        nameEditTextPayment = findViewById(R.id.nameEditTextPayment);
        phoneEditTextPayment = findViewById(R.id.phoneEditTextPayment);
        addressEditTextPayment = findViewById(R.id.addressEditTextPayment);
        cardNumberEditTextPayment = findViewById(R.id.cardNumberEditTextPayment);
        cardExpiryEditTextPayment = findViewById(R.id.cardExpiryEditTextPayment);
        postalCodeEditTextPayment = findViewById(R.id.postalCodeEditTextPayment);
        cvvEditTextPayment = findViewById(R.id.cvvEditTextPayment);
        btnPayement = findViewById(R.id.btnPayement);

        final Object object = getIntent().getSerializableExtra("detail");
        String totalAmount = getIntent().getStringExtra("totalAmount");
        if(object instanceof HomepageFoodListingModel)
        {
            homepageFoodListingModel = (HomepageFoodListingModel) object;
        }
        if(homepageFoodListingModel != null)
        {
        }

        cardExpiryEditTextPayment.setInputType(InputType.TYPE_NULL);
        cardExpiryEditTextPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(PaymentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cardExpiryEditTextPayment.setText((monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(cldr.getTimeInMillis());
                picker.show();
            }
        });

        btnPayement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nameEditTextPayment.getText().toString().isEmpty() && !phoneEditTextPayment.getText().toString().isEmpty() && !addressEditTextPayment.getText().toString().isEmpty()
                        && !cardNumberEditTextPayment.getText().toString().isEmpty() && !cardExpiryEditTextPayment.getText().toString().isEmpty() && !postalCodeEditTextPayment.getText().toString().isEmpty()
                && !cvvEditTextPayment.getText().toString().isEmpty()){
                    Intent finalReviewIntent = new Intent(PaymentActivity.this,OrderAcknowledge.class);
                    finalReviewIntent.putExtra("detail",homepageFoodListingModel);
                    startActivity(finalReviewIntent);
                }else
                {
                    Toast.makeText(PaymentActivity.this,"Please enter all the payment details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}