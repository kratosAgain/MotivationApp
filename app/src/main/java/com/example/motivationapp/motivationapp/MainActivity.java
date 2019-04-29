package com.example.motivationapp.motivationapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button buttonNutrition ;
    private GetNutritionRequest nutritionRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    /**
     * Function to initialize button and set on click listener
     */
    private void initialize(){
        this.buttonNutrition = findViewById(R.id.buttonnutrition);
        this.buttonNutrition.setOnClickListener(new View.OnClickListener(){
            /**
             * Trigger API on button click
             * @param v
             */
            @Override
            public void onClick(View v) {
               nutritionRequest = new GetNutritionRequest();
                try {

                    GetUrlContentTask task = new GetUrlContentTask();
                    task.execute();

                } catch (Exception e) {
                    Log.i("Motivation", "Error in get request initialization");
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Android recommends to run HTTP requests as Async Task so that it cant interfere with the main thread
     */
    private static class GetUrlContentTask extends AsyncTask<String, Integer, String> {
        private GetNutritionRequest nutritionRequestInner;


        /**
         * Function which triggers our nutritionX API
         * @param nutritionRequest
         */
        public void fetchNutritionApiData(GetNutritionRequest nutritionRequest)  {
            try {
                nutritionRequest.MyGETRequest("Milk, 1% Lowfat Aldi");
            }catch(Exception e){
                Log.i("Motivation", "Error in get request send");
                e.printStackTrace();
            }
        }


        protected void onProgressUpdate(Integer... progress) {

        }

        /**
         * .execute() runs this function
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            nutritionRequestInner = new GetNutritionRequest();
            fetchNutritionApiData(nutritionRequestInner);
            Log.i("Motivation","Request Done");
            return "Request Done";
        }

        protected void onPostExecute(String result) {
            // this is executed on the main thread after the process is over
            // update your UI here

        }
    }


}
