package com.example.android.motivatinalapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.services.customsearch.model.Result;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Frag1 extends Fragment {
    public EditText height;
    public TextView result;
    public EditText weight;
    public TextView effectiveweight;
    public ListView cheatlistview;
    List<UserData> cheatlist ;
    public static double caloriesPerDay, carbsPerDay, fatPerDay, proteinPerDay,sodiumPerDay,ironPerDay;

    public static double protein,fat, carbs,iron;
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
        double bmi = currentUser.currenUserBMI;
        TextView username=(TextView) view.findViewById(R.id.profilename);
        username.setText(name);
        TextView bmitext = (TextView)view.findViewById(R.id.textbmi);
        bmitext.setText(Double.toString(bmi));
        TextView caloriesperday = (TextView)view.findViewById(R.id.calorieperday);
        caloriesperday.setText(""+Double.toString(caloriesPerDay));
        TextView carbsperday = (TextView)view.findViewById(R.id.carbsperday);
        carbsperday.setText(Double.toString(carbsPerDay));
        TextView fatperday = (TextView)view.findViewById(R.id.fat);
        fatperday.setText(Double.toString(fatPerDay));
        TextView protientperday = (TextView)view.findViewById(R.id.protient);
        protientperday.setText(Double.toString(proteinPerDay));

        //cheat food start
        userDataMain = new UserData();
        db =new DatabaseHelper(getActivity());
        cheatlistview = view.findViewById(R.id.cheat_foodlist);
        cheatlist = new ArrayList<>();
        this.showCheatList();

        //cheat food end
        return view;
            }

    //setting data according to a male 25 year, 170 pounds , 6 feet tall, lightly active lifestyle
    public static void setNutrientValues(){
        //calories = 2000 , divided by 10 for scaling
        calculatenurtient();
        caloriesPerDay = 200;
//        carbsPerDay = 350;
//        proteinPerDay = 62;
//        fatPerDay = 80;
//        ironPerDay = 10;
        sodiumPerDay = 15;
    }

    public static void calculatenurtient() {
        if (currentUser.liefstyle == "working"){
            proteinPerDay = currentUser.weight / 2.2 * 1.7;
            carbsPerDay = currentUser.weight/ 3.5;
        }
        else if (currentUser.liefstyle == "Active") {
            proteinPerDay = currentUser.weight / 2.2 * 0.8;
            carbsPerDay = currentUser.weight/ 3.0;

        }
        else if (currentUser.liefstyle == "Moderate"){
            proteinPerDay = currentUser.weight / 2.2 * 0.6;
            carbsPerDay = currentUser.weight/ 2.25;

        }

        else if (currentUser.liefstyle == "Light activity") {
            proteinPerDay = currentUser.weight / 2.2 * 0.5;
            carbsPerDay = currentUser.weight/ 2.0;

        }
        else{
            proteinPerDay = currentUser.weight / 2.2 * 0.3;
            carbsPerDay = currentUser.weight/ 1.25;


        }

        fatPerDay = caloriesPerDay % 0.3;
        if (currentUser.gender=="Female")
            ironPerDay = 18;
        else
            ironPerDay =8;
    }

    public void showCheatList(){
        DatabaseHelper db =new DatabaseHelper(getActivity());
        cheatlist = db.getUserCheatList(currentUser.currentUserName);
        CustomListAdapter adapter = new CustomListAdapter(getActivity(), db, cheatlist);
        this.cheatlistview.setAdapter(adapter);
        this.cheatlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FootDetails.class);
                UserData u = cheatlist.get(position);
                intent.putExtra("userdata", u);
                getActivity().startActivity(intent);

            }
        });

    }



}
