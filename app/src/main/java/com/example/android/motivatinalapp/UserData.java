package com.example.android.motivatinalapp;

public class UserData {

    public String userName, foodName;
    public double calories, fat, carbs, iron, sodium , protein, bmi;
    public byte[] image;


    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setFoodName(String foodName){
        this.foodName = foodName;
    }
    public void setFat(double fat){
        this.fat = fat;
    }
    public void setCarbs(double carbs){
        this.carbs = carbs;
    }
    public void setSodium(double sodium){
        this.sodium = sodium;
    }
    public void setIron(double iron){
        this.iron = iron;
    }
    public void setProtein(double protien){
        this.protein = protien;
    }
    public void setCalories(double calories){
        this.calories = calories;
    }
    public void setBmi(double bmi){
        this.bmi = bmi;
    }

    public void setImage(byte[] image){
        this.image = image;
    }


}
