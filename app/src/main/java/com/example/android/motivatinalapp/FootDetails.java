package com.example.android.motivatinalapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;

public class FootDetails extends AppCompatActivity {

    private TextView textname, textfood, textcalories,textfat,textprotein,textsodium, textiron, textcarbs;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_details);

        initialize();
    }

    private void initialize(){
        this.textname = findViewById(R.id.food_name);
        this.textfat = findViewById(R.id.food_fat);
        this.textprotein = findViewById(R.id.food_protein);
        this.textcalories = findViewById(R.id.food_calories);
        this.textcarbs = findViewById(R.id.food_carbs);
        this.textiron = findViewById(R.id.food_iron);
        this.textsodium = findViewById(R.id.food_sodium);
        this.imageView = findViewById(R.id.food_image);

        Intent intent = getIntent();
        UserData userData = intent.getParcelableExtra("userdata");
        try {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(userData.image);
            Bitmap foodImage = BitmapFactory.decodeStream(imageStream);
            this.imageView.setImageBitmap(foodImage);
            imageStream.close();
        }catch(Exception e){
            Log.e("ERROR_FOODDETAILS","Error in image");
            e.printStackTrace();
        }
        this.textname.setText(userData.foodName);
        this.textfat.setText(""+userData.fat);
        this.textcalories.setText(""+userData.calories);
        this.textprotein.setText(""+userData.protein);
        this.textsodium.setText(""+userData.sodium);
        this.textiron.setText(""+userData.iron);
        this.textcarbs.setText(""+userData.carbs);
    }
}
