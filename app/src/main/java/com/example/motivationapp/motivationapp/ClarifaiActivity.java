package com.example.motivationapp.motivationapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.request.model.PredictRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.Model;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ClarifaiActivity extends AppCompatActivity {

    private final String CLARIFY_KEY = "19c0718c6f82456885467e35c8b72d9f";

    public ClarifaiClient clarifaiclient;
    private TextView textView;
    private String pictureFilePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        clarifaiclient = new ClarifaiBuilder(CLARIFY_KEY).buildSync();
//                new ClarifaiBuilder(CLARIFY_KEY).client(new OkHttpClient.Builder()
//                .readTimeout(5, TimeUnit.SECONDS)
//                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//                    @Override public void log(String logString) {
//                        Log.i("Clarify_Connection",logString);
//                    }
//                }).setLevel(HttpLoggingInterceptor.Level.BODY))
//                .build()
//        )
//                .buildSync();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clarifai);

        this.textView = findViewById(R.id.textView);

        Intent intent = getIntent();
        pictureFilePath = intent.getStringExtra("pictureFilePath");

        ClarifaiTask ca = new ClarifaiTask();
        ca.execute();

    }



    private  class ClarifaiTask extends AsyncTask<File, Integer, Boolean> {

        private String[] useless = {"food", "fruit", "juicy", "healthy", "person", "no person", "breakfast", "dark", "delicioous", "refreshment"
                , "still life", "life", "tasty", "desktop", "sweet", "salty", "lunch", "dinner", "fast", "traditional", "unhealthy", "blur", "indoors", "bright"
                , "paper", "one", "two", "contemporary", "nutrition", "healthcare", "still", "still life", "business", "work", "vitamin", "protein"

        };

        protected Boolean doInBackground(File... images) {
            //info.setText("Processing...");
            // Connect to Clarifai using your API token

//            List<ClarifaiOutput<Concept>> predictionResults;

            // For each photo we pass, send it off to Clarifai
            List<String> uselesslist = Arrays.asList(useless);


            try {
//                ClarifaiClient client = new ClarifaiBuilder(CLARIFY_KEY).buildSync();


                Model<Concept> generalModel = clarifaiclient.getDefaultModels().generalModel();
                PredictRequest<Concept> predictionResult = generalModel.predict().withInputs(
                        ClarifaiInput.forImage(new File(pictureFilePath))
                );


                // Check if Clarifai thinks the photo contains the object we are looking for
                Log.i("CLARIFY", "before the results");
                String max = "";
                float maxval = 0;
                List<ClarifaiOutput<Concept>> results = predictionResult.executeSync().get();
                for (ClarifaiOutput<Concept> result : results)
                    for (Concept c : result.data()) {
                        if (maxval < c.value() && !uselesslist.contains(c.name())) {
                            System.out.println(c.name() + "  " + c.value());
                            maxval = c.value();
                            max = c.name();
                        }
                        Log.i("CLARIFY", c.toString());
                    }

                textView.setText(max + " " + maxval);
            } catch (Exception e) {
                Log.i("CLARIFY_ERROR", "ERROR " + e.toString());
            }

            return true;
    }

        protected void onPostExecute(Boolean result) {

        }
    }



}
