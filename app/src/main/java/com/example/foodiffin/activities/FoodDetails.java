package com.example.foodiffin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodiffin.R;
import com.example.foodiffin.models.HomepageFoodListingModel;
import com.example.foodiffin.ui.home.HomeFragment;

public class FoodDetails extends AppCompatActivity {

    ImageView tiffinImageViewDetail;
    TextView tiffinTitleTextViewDetail, tiffinDescriptionTextViewDetail, tiffinPriceTextViewDetail,tiffinMenuTextViewDetail;
    HomepageFoodListingModel homepageFoodListingModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        tiffinImageViewDetail = findViewById(R.id.tiffinImageViewDetail);
        tiffinTitleTextViewDetail = findViewById(R.id.tiffinTitleTextViewDetail);
        tiffinDescriptionTextViewDetail = findViewById(R.id.tiffinDescriptionTextViewDetail);
        tiffinPriceTextViewDetail = findViewById(R.id.tiffinPriceTextViewDetail);
        tiffinMenuTextViewDetail = findViewById(R.id.tiffinMenuTextViewDetail);

        final Object object = getIntent().getSerializableExtra("detail");
        if(object instanceof HomepageFoodListingModel)
        {
            homepageFoodListingModel = (HomepageFoodListingModel) object;
        }
        if(homepageFoodListingModel != null)
        {
            Glide.with(getApplicationContext()).load(homepageFoodListingModel.getFoodImage()).into(tiffinImageViewDetail);
            tiffinTitleTextViewDetail.setText(homepageFoodListingModel.getFoodTitle());
            tiffinDescriptionTextViewDetail.setText(homepageFoodListingModel.getFoodDescription());
            tiffinMenuTextViewDetail.setText("> " + homepageFoodListingModel.getFoodMenu());
            tiffinPriceTextViewDetail.setText("$"+homepageFoodListingModel.getFoodPrice()+"/Per Month");
        }
    }

    public void placeOrder(View view)
    {
        Intent intent = new Intent(this, OrderFood.class);
        intent.putExtra("detail",homepageFoodListingModel);
        this.startActivity(intent);
    }
}