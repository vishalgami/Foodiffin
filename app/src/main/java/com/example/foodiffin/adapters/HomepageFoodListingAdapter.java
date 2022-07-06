package com.example.foodiffin.adapters;


import android.content.Context;

import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodiffin.R;
import com.example.foodiffin.activities.FoodDetails;
import com.example.foodiffin.models.HomepageFoodListingModel;

import java.util.ArrayList;
import java.util.List;


public class HomepageFoodListingAdapter extends RecyclerView.Adapter<HomepageFoodListingAdapter.ViewHolder> {
    Context context;
    List<HomepageFoodListingModel> foodList;

    public HomepageFoodListingAdapter(Context context, List<HomepageFoodListingModel> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public HomepageFoodListingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomepageFoodListingAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_food_listing,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomepageFoodListingAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(foodList.get(position).getFoodImage()).into(holder.tiffinImageView);
        holder.tiffinTitleTextView.setText(foodList.get(position).getFoodTitle());
        holder.tiffinDescriptionTextView.setText(foodList.get(position).getFoodDescription());
        holder.tiffinPriceTextView.setText("$"+foodList.get(position).getFoodPrice() +"/per Month");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FoodDetails.class);
                intent.putExtra("detail",foodList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView tiffinImageView;
        TextView tiffinTitleTextView;
        TextView tiffinDescriptionTextView;
        TextView tiffinPriceTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tiffinImageView = itemView.findViewById(R.id.tiffinImageView);
            tiffinTitleTextView = itemView.findViewById(R.id.tiffinTitleTextView);
            tiffinDescriptionTextView = itemView.findViewById(R.id.tiffinDescriptionTextView);
            tiffinPriceTextView = itemView.findViewById(R.id.tiffinPriceTextView);
        }
    }

}
