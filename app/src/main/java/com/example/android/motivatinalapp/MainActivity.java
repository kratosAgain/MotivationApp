package com.example.android.motivatinalapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.*;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText userText;
    TextView register;
    EditText passtext;
    DatabaseHelper helper = new DatabaseHelper(this);





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = (TextView) findViewById(R.id.textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                userText = (EditText) findViewById(R.id.editText2);
                passtext =(EditText)findViewById(R.id.editText3);
                String str = userText.getText().toString();
                String pass = passtext.getText().toString();

                String password = helper.searchdatabase(str);
                if (pass.equals(password))
                {
                    Intent intent = new Intent(MainActivity.this, TrackActivity1.class);
                    intent.putExtra("Username", str);

                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Going to main", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Credential Mismatch", Toast.LENGTH_SHORT).show();
                }




            }
        });

        register = (TextView) findViewById(R.id.registerid);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("in the sometwf","  sdfsf");
                Intent i = new Intent(MainActivity.this, Signup.class);
                startActivity(i);
                Toast.makeText(MainActivity.this, "Going to signup page", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
