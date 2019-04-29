package com.example.motivationapp.motivationapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.api.services.customsearch.model.Result;

import java.io.*;
import java.net.*;
import java.util.*;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 1337;

    static final int REQUEST_TAKE_PHOTO = 1;


    private Button buttonNutrition, searchImageButton, googleSearchButton;
    private EditText searchQuery;
    private GetNutritionRequest nutritionRequest;
    private ImageView imageview2 ;
    private String photoPath = null;
    private String object;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    /**
     * Function to initialize button and set on click listener
     */
    private void initialize() {
        this.buttonNutrition = findViewById(R.id.buttonnutrition);
//        this.searchImageButton = findViewById(R.id.searchImageButton);
        this.googleSearchButton = findViewById(R.id.googleSearchButton);
        this.searchQuery = findViewById(R.id.query);
        this.imageview2 =  findViewById(R.id.imageView2);



        this.buttonNutrition.setOnClickListener(new View.OnClickListener() {
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


//        this.searchImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /**
//                 * this code opens the camera and takes image
//                 */
////                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
////                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
//                takePicture(v);
////                ClarifyTextToImage img = new ClarifyTextToImage();
////                img.run();
//            }
//        });


        this.googleSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleImageSearchTask task = new GoogleImageSearchTask();

                task.execute(searchQuery.getText().toString());
            }
        });
        this.takePermission();
    }


//    public void takePicture(View view) {
//        // Create intent to open camera app
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Proceed only if there is a camera app
//        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//
//            // Attempt to allocate a file to store the photo
//            File photoFile;
//            try {
//                File storageDir = getFilesDir();
//                photoFile = File.createTempFile("SNAPSHOT", ".jpg", storageDir);
//                photoPath = photoFile.getAbsolutePath();
//            } catch (IOException ex) { return; }
//            // Send off to the camera app to get a photo
////            Uri photoURI = FileProvider.getUriForFile(this, "com.example.motivationapp.motivationapp", photoFile);
////            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//        //}
//    }

    /**
     * Function for taking permission to write to file
     */
    public void takePermission(){

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Permission denied to access external storage.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        // If we've taken a photo, send it off to Clarifai to check
//        if (photoPath != null) {
//            new ClarifaiTask().execute(new File(photoPath));
//        }
    }

//    /**
//     * this function puts the imaga taken from camera to imageview
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        if (requestCode == CAMERA_PIC_REQUEST) {
////            Bitmap image = (Bitmap) data.getExtras().get("data");
////            ImageView imageview = (ImageView) findViewById(R.id.imageView1); //sets imageview as the bitmap
////            imageview.setImageBitmap(image);
////        }
//
//    }

    /**
     * Put a drawable in an imageview
     * @param drawable
     */
    public void drawImageInView(Drawable drawable){

        this.imageview2.setImageDrawable(drawable);
    }


    /**
     * Android recommends to run HTTP requests as Async Task so that it cant interfere with the main thread
     */
    private static class GetUrlContentTask extends AsyncTask<String, Integer, String> {
        private GetNutritionRequest nutritionRequestInner;


        /**
         * Function which triggers our nutritionX API
         *
         * @param nutritionRequest
         */
        public void fetchNutritionApiData(GetNutritionRequest nutritionRequest) {
            try {
                nutritionRequest.MyGETRequest("Milk, 1% Lowfat Aldi");
            } catch (Exception e) {
                Log.i("Motivation", "Error in get request send");
                e.printStackTrace();
            }
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


        /**
         * Android recommends to run HTTP requests as Async Task so that it cant interfere with the main thread
         * This is specific to google search from text
         */
        private class GoogleImageSearchTask extends AsyncTask<String, Integer, String> {
            private GoogleImageSearch search;
            Drawable drawable;;


            /**
             * Function to trigger google search API
             *
             * @param query
             */
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

            /**
             * Google search gives a URL image, this function takes that URL and downloads the image to the external storage
             * @param urlstr
             * @param query
             * @throws Exception
             */

            public void saveImage(String urlstr, String query) throws Exception {
                int c = 0;
                URL url = new URL(urlstr);
                InputStream input = url.openStream();
                File storagePath = Environment.getExternalStorageDirectory();
                OutputStream output = new FileOutputStream (new File(storagePath,query+".png"));
                byte[] buffer = new byte[512];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }
                output.close();
                input.close();

                Bitmap x;

                /**
                 * create drawable from Google URL, we can also open the file we save above, its just another way
                 * of doing it.
                 */

                HttpURLConnection connection = (HttpURLConnection) new URL(urlstr).openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();

                x = BitmapFactory.decodeStream(in);
                drawable = new BitmapDrawable(x);
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
                // this is executed on the main thread after the process is over
                // update your UI here
                drawImageInView(drawable);

            }
        }

//    private class ClarifaiTask extends AsyncTask<File, Integer, Boolean> {
//
//        protected Boolean doInBackground(File... images) {
//            //info.setText("Processing...");
//            // Connect to Clarifai using your API token
//            ClarifaiClient client = new ClarifaiBuilder(CLARIFY_KEY).buildSync();
//            List<ClarifaiOutput<Concept>> predictionResults;
//            // For each photo we pass, send it off to Clarifai
//            for (File image : images) {
//                predictionResults = client.getDefaultModels().generalModel().predict()
//                        //.withInputs(ClarifaiInput.forImage()).executeSync().get();
//                        .withInputs(ClarifaiInput.forImage(image)).executeSync().get();
//
//                // Check if Clarifai thinks the photo contains the object we are looking for
//                for (ClarifaiOutput<Concept> result : predictionResults)
//                    for (Concept datum : result.data())
//                        Log.i("RESULT", datum.toString());
//            }
//            return false;
//        }
//
//        protected void onPostExecute(Boolean result) {
//            // Delete photo
//            (new File(photoPath)).delete();
//            photoPath = null;
//
//            // If image contained object, close the AlarmActivity
//            if (result) {
////                info.setText("Success!");
//                Log.i("Success","sucess");
//                finish();
//            } else Log.i("Again","Try Again");
//        }
//    }
//



}
