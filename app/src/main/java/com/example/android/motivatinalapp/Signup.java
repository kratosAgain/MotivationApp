package com.example.android.motivatinalapp;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.api.services.customsearch.model.Result;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Signup extends Activity{

    DatabaseHelper db = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        db =new DatabaseHelper(this);

    }

    public void onSignup(View v)
    {
        if(v.getId()==R.id.button)
        {
            EditText name = (EditText)findViewById(R.id.nameid);

            EditText email = (EditText)findViewById(R.id.emailid);
            EditText username = (EditText)findViewById(R.id.usernameid);
            EditText password = (EditText)findViewById(R.id.passwordid);
            EditText cpassword = (EditText)findViewById(R.id.cpasswordid);
            EditText weight=(EditText)findViewById(R.id.heightinput);
            EditText height=(EditText)findViewById(R.id.weightinput);



            String namestr =name.getText().toString();
            Log.d("NAME", namestr);
            String usernamestr = username.getText().toString();
            Log.d("userNAME", usernamestr);
            String emailstr =email.getText().toString();
            Log.d("emailstr", emailstr);
            String passstr =password.getText().toString();
            String weightstr = weight.getText().toString();
            String heightstr = height.getText().toString();


            String cpassstr =cpassword.getText().toString();
            if(!passstr.equals(cpassstr))
            {
                Log.d("in IF", "in the if");
                Toast.makeText(this.getApplicationContext(), "Password do not match", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //insert into database
                Contact c =new Contact();
                c.setNam(namestr);
                c.setEmail(emailstr);
                c.setPass(passstr);
                calculatebmi(heightstr,weightstr);
                c.setUname(usernamestr);
                db.insertContact(c);
                currentUser.currentUserName = usernamestr;
                db.close();
            }

            Intent intent = new Intent(Signup.this,CheatFood.class);
            startActivity(intent);
        }

    }


    public void calculatebmi(String h, String w){
        float fh =Float.parseFloat(h)/100;
        float fw =Float.parseFloat(w);
        float bmi = fw/(fh*fh);
        Log.d("log bmi", Float.toString(bmi));
        currentUser.currenUserBMI = bmi;
    }





}
