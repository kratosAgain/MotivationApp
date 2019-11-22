package com.example.android.motivatinalapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.api.services.customsearch.model.Result;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CheatFood extends AppCompatActivity {

    DatabaseHelper db = null;
    private Spinner dropdown;
    public String searchString;
    public static UserData userDataMain;
    public List<String> foodList=null;
    public static String[] cheatFoods = {"Pepperoni Pizza","Hamburger","Oreo Cake"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat_food);
        db =new DatabaseHelper(this);
        this.dropdown = findViewById(R.id.dropdown);
        this.addItemsInDropDown();
        this.addListenerOnSpinnerItemSelection();
        this.dropdown.setPrompt("Choose a cheat food");



        userDataMain = new UserData();
        db =new DatabaseHelper(this);

        this.foodList = new ArrayList();

    }

    public void Done(View v){
        Intent intent = new Intent(CheatFood.this,MainActivity.class);
        startActivity(intent);
    }

    public void addListenerOnSpinnerItemSelection() {
        //this.dropdown = (Spinner) findViewById(R.id.dropdown);
        this.dropdown.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void addItemsInDropDown(){
        List<String> cheatFoods = new ArrayList<>();

        cheatFoods.add("Select cheat food, upto 3");
        cheatFoods.add("Pepperoni Pizza");
        cheatFoods.add("HamBurger");
        cheatFoods.add("Chicken Parmesan");
        cheatFoods.add("Fried chicken");
        cheatFoods.add("Chesse cake");
        cheatFoods.add("Oreo Cake");
        cheatFoods.add("Fudge Brownie");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cheatFoods);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.dropdown.setAdapter(dataAdapter);
    }

    class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if(parent.getItemAtPosition(pos).toString().contains("Select")){
                //do nothing
            }
            else if(foodList.size()>=3){
                Toast.makeText(parent.getContext(),
                        " Can't add more than 3 cheat foods" ,
                        Toast.LENGTH_SHORT).show();
            }else {
                foodList.add(parent.getItemAtPosition(pos).toString());
                makeCheatData(parent.getItemAtPosition(pos).toString());
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }

    public void makeCheatData(String food){

        searchString = food;
        try {
            GetUrlContentTask task = new GetUrlContentTask();
            task.execute();
        } catch (Exception e) {
            Log.i("Motivation", "Error in get request initialization");
            e.printStackTrace();
        }
        try {
            GoogleImageSearchTask task = new GoogleImageSearchTask();

            task.execute(searchString);

        } catch (Exception e) {
            Log.i("Motivation", "Error in google request");
            e.printStackTrace();
        }

        db.insertCheatData(userDataMain);


    }



    public  class GetUrlContentTask extends AsyncTask<String, Integer, String> {
        private GetNutritionRequest nutritionRequestInner;
        public void fetchNutritionApiData(GetNutritionRequest nutritionRequest) {
            try {
                String qr =searchString;

                JSONObject obj = nutritionRequest.MyGETRequest(qr);
                userDataMain.setCalories(Double.parseDouble(obj.getString("nf_calories")));
                userDataMain.setFat(Double.parseDouble(obj.getString("nf_total_fat")));
                userDataMain.setIron(Double.parseDouble(obj.getString("nf_iron_dv")));
                userDataMain.setCarbs(Double.parseDouble(obj.getString("nf_total_carbohydrate")));
                userDataMain.setProtein(Double.parseDouble(obj.getString("nf_protein")));
                userDataMain.setSodium(Double.parseDouble(obj.getString("nf_sodium")));
                userDataMain.setFoodName(obj.getString("item_name"));
                userDataMain.setUserName(currentUser.currentUserName);
                userDataMain.setBmi(currentUser.currenUserBMI);
            } catch (Exception e) {
                Log.i("Motivation", "Error in get request send");
                e.printStackTrace();
            }
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(String... strings) {
            nutritionRequestInner = new GetNutritionRequest();
            fetchNutritionApiData(nutritionRequestInner);
            Log.i("Motivation", "Request Done");
            return "Request Done";
        }

        protected void onPostExecute(String result) {
            // this is executed on the main thread after the process is over
            // update your UI here

        }
    }


    public  class GoogleImageSearchTask extends AsyncTask<String, Integer, String> {
        private GoogleImageSearch search;
        Drawable drawable;
        private void searchImages(String query) throws Exception {

            List<Result> results = search.searchImages(query);
            int count = 0;
            for (Result r : results) {
                String storeLink = r.getImage().getThumbnailLink();
                Log.i("GoogleSearch", r.getDisplayLink());
                Log.i("GoogleSearch", r.getTitle());
                count++;
                saveImage(storeLink, query);

                if (count == 1)
                    break;
            }
        }

        public void saveImage(String urlstr, String query) throws Exception {
            int c = 0;
            URL url = new URL(urlstr);
            InputStream input = url.openStream();
            Bitmap bimage = null;
            bimage = BitmapFactory.decodeStream(input);
            input.close();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bimage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte imageInByte[] = stream.toByteArray();
            userDataMain.setImage(imageInByte);
            stream.close();
            drawable = new BitmapDrawable(getResources(),bimage);
        }


        protected void onProgressUpdate(Integer... progress) {

        }

        /**
         * .execute() runs this function
         *
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings){
            search = new GoogleImageSearch();
            try {
                if (strings.length > 0) {
                    searchImages(strings[0]);
                    Log.i("Motivation", "Request Done");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return "Request Done";
        }

        /**
         * This function puts the first image in imagebox
         * @param result
         */
        protected void onPostExecute(String result) {
        }
    }

}
