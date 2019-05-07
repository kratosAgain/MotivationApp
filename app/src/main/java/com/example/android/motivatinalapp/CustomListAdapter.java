package com.example.android.motivatinalapp;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter {

    //to reference the Activity
    private final Activity context;

    List<UserData> userList ;

    public CustomListAdapter(Activity context, DatabaseHelper db, List<UserData> userList){
        super(context,R.layout.listviewitem , userList);
        this.userList = userList;
        this.context=context;
    }

    public View getView(int position, View view, ViewGroup parent) {
        View rowView = null;
        try {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.listviewitem, null, true);

            //this code gets references to objects in the listview_row.xml file
            TextView foodField = (TextView) rowView.findViewById(R.id.food_name);
            TextView carbsField = (TextView) rowView.findViewById(R.id.food_carbs);
            TextView fatField = (TextView) rowView.findViewById(R.id.food_fat);
            TextView calorieField = (TextView) rowView.findViewById(R.id.food_calories);
            TextView proteinField = (TextView) rowView.findViewById(R.id.food_protein);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.food_image);

            //this code sets the values of the objects to values from the arrays
            foodField.setText(userList.get(position).foodName);
            carbsField.setText("" + userList.get(position).carbs);
            calorieField.setText("" + userList.get(position).calories);
            fatField.setText("" + userList.get(position).fat);
            proteinField.setText("" + userList.get(position).protein);


            ByteArrayInputStream imageStream = new ByteArrayInputStream(userList.get(position).image);
            Bitmap foodImage = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(foodImage);
            imageStream.close();
        }catch(Exception e){
            Log.e("ListViewError","Error in list view get View");
            e.printStackTrace();
        }

        return rowView;

    };

}
