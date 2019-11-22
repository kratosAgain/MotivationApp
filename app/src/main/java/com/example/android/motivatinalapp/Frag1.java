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
    public static int caloriesPerDay, carbsPerDay, fatPerDay, proteinPerDay,sodiumPerDay,ironPerDay;
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
        caloriesperday.setText(Integer.toString(caloriesPerDay));
        TextView carbsperday = (TextView)view.findViewById(R.id.carbsperday);
        carbsperday.setText(Integer.toString(carbsPerDay));
        TextView fatperday = (TextView)view.findViewById(R.id.fat);
        fatperday.setText(Integer.toString(fatPerDay));
        TextView protientperday = (TextView)view.findViewById(R.id.protient);
        protientperday.setText(Integer.toString(proteinPerDay));

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
        caloriesPerDay = 200;
        carbsPerDay = 350;
        proteinPerDay = 62;
        fatPerDay = 80;
        ironPerDay = 10;
        sodiumPerDay = 15;
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
