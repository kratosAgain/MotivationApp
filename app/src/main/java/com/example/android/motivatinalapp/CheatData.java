package com.example.android.motivatinalapp;



import android.database.Cursor;
import android.util.Log;


import java.util.*;

public class CheatData {

    private DatabaseHelper db;
    private String username;

    public CheatData(String username, DatabaseHelper db){
        this.db = db;
        this.username = username;
    }

    public ArrayList<UserData> getPossibleCheatFood(String compareKey){
        ArrayList<UserData> cheatList = new ArrayList();
        HashMap<String,Double> dateToCalorieMap = new HashMap();

        Cursor cursor = db.searchUserDatabase(currentUser.currentUserName);

        if (cursor==null || cursor.getCount()==0) {
            Log.i("Cursor","Cursor is Null or Empty");
            return cheatList;
        }

        //current date
        Date currentTime = Calendar.getInstance().getTime();
        int currentDate  = currentTime.getDate();



        //making hashmap for last seven days
        for(int gap = 6; gap >= 0; gap--){
            String key = "";
            Calendar lastCal = Calendar.getInstance();
            lastCal.add(Calendar.DATE,-gap);
            Date lastTime = lastCal.getTime();
            key = getKey(lastTime);
            dateToCalorieMap.put(key,0.0);
        }

        //traversing the cursor now
        while(cursor.moveToNext()){
            try{
                Date date = db.getDate(cursor);
                if( inLastWeek(dateToCalorieMap,date)){
                    double calories = db.getNutrientValue(cursor, compareKey);
                    dateToCalorieMap.put(getKey(date), dateToCalorieMap.getOrDefault(getKey(date),0.0)+calories);
                }

            }catch(Exception e){
                Log.e("CURSOR_ERROR","error in putting values in day hashmap");
                e.printStackTrace();
            }
        }
//        Log.i("MAP",""+dateToCalorieMap);

        //figure out which cheatfood applies
        double totalCals = getTotalCal(dateToCalorieMap);
        //total recommended
        double recommended = Frag1.caloriesPerDay*10.0*7.0;

        double award = recommended - totalCals;

        if(award<=0){
            return cheatList;
        }


        //now traversing cheat database to figure out which cheat food is valid
        List<UserData> cheatFoodList = db.getUserCheatList(currentUser.currentUserName);

        for(UserData u:cheatFoodList){
            if(u.calories <= award){
                cheatList.add(u);
            }
        }

        return cheatList;
    }


    public boolean inLastWeek(HashMap<String,Double> dateToCalorieMap, Date checkDate){
        for(String d:dateToCalorieMap.keySet()){
            String[] parts = d.split("_");
            if(parts[0].contains(Integer.toString(checkDate.getDate())) && parts[1].contains(Integer.toString(checkDate.getMonth()))){
                return true;
            }
        }
        return false;
    }

    public double getTotalCal(HashMap<String,Double> dateToCalorieMap){
        double sum = 0.0;
        for(String d:dateToCalorieMap.keySet()){
            sum = dateToCalorieMap.get(d);
        }
        return sum;
    }

    public String getKey(Date date){
        return ""+date.getDate()+"_"+date.getMonth();
    }


}
