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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.*;

public  class MainActivity extends AppCompatActivity {
    Button textView;
    EditText userText;
    Button register;
    EditText passtext;
    DatabaseHelper helper = null;
    currentUser cu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new DatabaseHelper(this);
        cu = new currentUser();
        textView = (Button) findViewById(R.id.textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                userText = (EditText) findViewById(R.id.editText2);
                passtext =(EditText)findViewById(R.id.editText3);
                String str = userText.getText().toString();
                String pass = passtext.getText().toString();


                String password = helper.searchdatabase(str);
                if (true || pass.equals(password))
                {
                    currentUser.currentUserName = str;
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

        register = (Button) findViewById(R.id.registerid);
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
