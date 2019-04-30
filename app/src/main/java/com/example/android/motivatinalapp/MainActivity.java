package com.example.android.motivatinalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView)findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, TrackActivity1.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"", Toast.LENGTH_SHORT).show();


            }
        });





    }



}
