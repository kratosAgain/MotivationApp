package com.example.android.motivatinalapp;

import java.util.ArrayList;

public class CheatData {

    ArrayList<String> allFoods ;
    String foodString;
    String username;
    public CheatData(String username, ArrayList<String> food){
        this.foodString = "";
        this.allFoods = food;
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public String  getFoods(){
        if(this.allFoods.size()==0){
            return "";
        }

        for(int i=0;i<this.allFoods.size();i++){
            if(i==allFoods.size()-1){
                this.foodString +=this.allFoods.get(i);
            }else{
                this.foodString +=(this.allFoods.get(i)+"_");
            }
        }
        return this.foodString;
    }
}
