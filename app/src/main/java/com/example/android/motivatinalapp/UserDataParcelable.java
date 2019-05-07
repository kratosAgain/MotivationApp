package com.example.android.motivatinalapp;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDataParcelable implements Parcelable {


    public String userName, foodName, date;
    public double calories, fat, carbs, iron, sodium , protein, bmi;
    public byte[] image;

    public UserDataParcelable (

            String userName,
            String foodName,
            String date,
            double calories,
            double fat,
            double carbs,
            double iron,
            double protein,
            double bmi,
            byte[] image){
        this.userName = userName;
        this.foodName = foodName;
        this.date = date;
        this.calories = calories;
        this.fat = fat;
        this.carbs = carbs;
        this.iron = iron;
        this.protein = protein;
        this.bmi = bmi;
        this.image = image;
    }


    public UserDataParcelable(Parcel parcel){
        //read and set saved values from parcel
        this.userName = parcel.readString();
        this.foodName = parcel.readString();
        this.calories = parcel.readDouble();
        this.carbs = parcel.readDouble();
        this.fat = parcel.readDouble();
        this.protein = parcel.readDouble();
        this.sodium = parcel.readDouble();
        this.iron = parcel.readDouble();
        this.bmi = parcel.readDouble();
        this.date = parcel.readString();
        this.image = new byte[parcel.readInt()];
        parcel.readByteArray(this.image);
    }




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
    public void setDate(String date){
        this.date = date;
    }


    @Override
    public int describeContents() {
        return hashCode();
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.foodName);
        dest.writeDouble(this.calories);
        dest.writeDouble(this.carbs);
        dest.writeDouble(this.fat);
        dest.writeDouble(this.protein);
        dest.writeDouble(this.sodium);
        dest.writeDouble(this.iron);
        dest.writeDouble(this.bmi);
        dest.writeString(this.date);
        dest.writeInt(this.image.length);
        dest.writeByteArray(this.image);
    }


    public static final Parcelable.Creator<UserDataParcelable> CREATOR = new Parcelable.Creator<UserDataParcelable>(){

        @Override
        public UserDataParcelable createFromParcel(Parcel parcel) {
            return new UserDataParcelable(parcel);
        }

        @Override
        public UserDataParcelable[] newArray(int size) {
            return new UserDataParcelable[0];
        }
    };


}
