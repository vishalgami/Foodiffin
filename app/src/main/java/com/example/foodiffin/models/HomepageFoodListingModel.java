package com.example.foodiffin.models;

import android.util.Log;

import java.io.Serializable;

public class HomepageFoodListingModel implements Serializable {
    String foodTitle,foodDescription,foodImage,foodMenu;
    float foodPrice;
    double totalAmount;

    public HomepageFoodListingModel()
    {

    }
    public HomepageFoodListingModel(String foodTitle,String foodDescription,float foodPrice,String foodImage,String foodMenu)
    {
        this.foodImage = foodImage;
        this.foodTitle = foodTitle;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
        this.foodMenu = foodMenu;
    }
    public String getFoodImage()
    {
        return foodImage;
    }
    public String getFoodTitle(){ return foodTitle;}
    public String getFoodDescription(){
        return foodDescription;
    }
    public float getFoodPrice(){
        return foodPrice;
    }
    public String getFoodMenu(){
        return foodMenu;
    }
    public void setTotalAmount(double amount){
        this.totalAmount = amount;
    }
    public double getTotalAmount(){
        return totalAmount;
    }
}
