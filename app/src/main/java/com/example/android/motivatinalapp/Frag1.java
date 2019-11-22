package com.example.android.motivatinalapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Frag1 extends Fragment {
    public EditText height;
    public TextView result;
    public TextView username;
    public EditText weight;
    public TextView effectiveweight;
    public static int caloriesPerDay, carbsPerDay, fatPerDay, proteinPerDay,sodiumPerDay,ironPerDay;

    public static double protein,fat;
    public String searchString;
    public static UserData userDataMain;
    DatabaseHelper db = null;

    public static String[] cheatFoods = {"Pepperoni Pizza","Hamburger","Oreo Cake"};
    public RadioGroup radiobutton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag1_layout, container, false);
        setNutrientValues();

        String name = currentUser.currentUserName;

        username=(TextView) view.findViewById(R.id.textView2);
        username.setText(name);

//        weight=(EditText)view.findViewById(R.id.editText);
//        height=(EditText)view.findViewById(R.id.editText4);
//        result=(TextView)view.findViewById(R.id.textView8);
//        effectiveweight=(TextView)view.findViewById(R.id.textView9);
//        final Button button = (Button)view.findViewById(R.id.button2);
//        final RadioGroup radioSexGroup = (RadioGroup)view.findViewById(R.id.radiogroup);
//
//        button.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//
//                String h =height.getText().toString();
//                Log.d("log height", h);
//
//                String w =weight.getText().toString();
//                Log.d("log weight", w);
//                if(h!=null && w!=null)
//                {
//                    float fh =Float.parseFloat(h)/100;
//                    float fw =Float.parseFloat(w);
//                    float bmi = fw/(fh*fh);
//                    Log.d("log bmi", Float.toString(bmi));
//                    displayBMI(bmi);
//                    currentUser.currenUserBMI = bmi;
//
//                }
//
//
//            }
//
//
//
//            public void displayBMI(float bmi)
//            {
//                String bmilable="";
//                if(Float.compare(bmi,15f)<=0){
//                    bmilable="very Severly Underweight";
//                }
//                else if(Float.compare(bmi,15f)>0 && Float.compare(bmi,16f)<=0)
//                {
//                    bmilable="very Underweight"; }
//                else if(Float.compare(bmi,16f)>0 && Float.compare(bmi,18.5f)<=0)
//                {
//                    bmilable="Underweight"; }
//                else if(Float.compare(bmi,18.5f)>0 && Float.compare(bmi,25f)<=0)
//                {
//                    bmilable="Normal"; }
//                else if(Float.compare(bmi,25f)>0 && Float.compare(bmi,30f)<=0)
//                {
//                    bmilable="Overweight"; }
//                else if(Float.compare(bmi,30f)>0 && Float.compare(bmi,35f)<=0)
//                {
//                    bmilable="Obese_class_i"; }
//                else if(Float.compare(bmi,35f)>0 && Float.compare(bmi,40f)<=0)
//                {
//                    bmilable="Obese_class_ii"; }
//                else{
//                    bmilable="Obese_class_iii";
//                }
//                result.setText(bmilable);
//            }
//        });
//            radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//
//                @Override
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                    switch (checkedId) {
//                        case R.id.radiofemale:
//                            String h1 = height.getText().toString();
//                            int eh = Integer.parseInt(h1) - 100;
//                            double ewf = (eh - (eh * (0.15)));
//                            effectiveweight.setText("You Ideal Weigh should be: "+" "+String.valueOf(ewf));
//
//                            Log.d("log eeeeeeeewf", String.valueOf(ewf));
//
//                            break;
//                        case R.id.radiomale:
//                            String h2 = height.getText().toString();
//                            int eh1 = Integer.parseInt(h2) - 100;
//                            Log.d("log eeeeeeeemale", "insidemale");
//                            double ewm = (eh1 - (eh1 * (0.10)));
//                            effectiveweight.setText("You Ideal Weigh should be:"+" "+ String.valueOf(ewm));
//                            Log.d("log eeeeeeeemale....", String.valueOf(ewm));
//
//                            break;
//                    }
//                }
//            });



        return view;
            }


    //setting data according to a male 25 year, 170 pounds , 6 feet tall, lightly active lifestyle
    public static void setNutrientValues(){
        //calories = 2000 , divided by 10 for scaling
        caloriesPerDay = 200;
        carbsPerDay = 350;
        proteinPerDay = 62;
        fatPerDay = 80;
        ironPerDay = 10;
        sodiumPerDay = 15;
    }

    public static void calculatenurtient(){
        if (currentUser.liefstyle=="working")
            protein = currentUser.weight/2.2 *1.7;
        else if(currentUser.liefstyle=="Active")
            protein = currentUser.weight/2.2 *0.8;
        else if(currentUser.liefstyle=="Moderate")
            protein = currentUser.weight/2.2 *0.6;
        else if(currentUser.liefstyle=="Light activity")
            protein = currentUser.weight/2.2 *0.5;
        else
            protein = currentUser.weight/2.2 * 0.3;

        fat = caloriesPerDay % 0.3;


    }



}
