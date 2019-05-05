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
import java.text.SimpleDateFormat;
import java.util.*;
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

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 1337;

    static final int REQUEST_TAKE_PHOTO = 1;
    private final String CLARIFY_KEY = "19c0718c6f82456885467e35c8b72d9f";

    private Button buttonNutrition, searchImageButton, googleSearchButton, clarifyit;
    private EditText searchQuery;
//    private TextView textview;
    private GetNutritionRequest nutritionRequest;
    private ImageView imageview2, imageview1 ;
    private String photoPath = null;
    private String object;
    private String pictureFilePath;



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
        this.searchImageButton = findViewById(R.id.searchImageButton);
        this.clarifyit = findViewById(R.id.clarifyit);
        this.googleSearchButton = findViewById(R.id.googleSearchButton);
        this.searchQuery = findViewById(R.id.query);
        this.imageview2 =  findViewById(R.id.imageView2);
        this.imageview1 =  findViewById(R.id.imageView1);
//        this.textview = findViewById(R.id.textView);




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


        this.searchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * this code opens the camera and takes image
                 */
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                takePicture(v);
//                ClarifyTextToImage img = new ClarifyTextToImage();
//                img.run();
            }
        });

        this.clarifyit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                clarifyTask();
            }
        });


        this.googleSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleImageSearchTask task = new GoogleImageSearchTask();

                task.execute(searchQuery.getText().toString());
            }
        });

        this.imageview1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                doNothing();
                //do nothing
            }
        });





        this.takePermission();
    }

    private void doNothing(){
        Toast.makeText(this,
                "Photo clicked",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            File imgFile = new  File(pictureFilePath);
            if(imgFile.exists())            {
                imageview1.setImageURI(Uri.fromFile(imgFile));
            }
        }
    }


    public void takePicture(View view) {
        // Create intent to open camera app
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

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1);

            File pictureFile = null;
            try {
                pictureFile = getPictureFile();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getApplicationContext().getApplicationContext().getPackageName() + ".provider",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, 1);
            }
        }
    }


    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "foodimage_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }


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



    public void clarifyTask() {



        // If we've taken a photo, send it off to Clarifai to check
        if (pictureFilePath != null && pictureFilePath.length()!=0) {

            Intent myIntent = new Intent(MainActivity.this, ClarifaiActivity.class);
            myIntent.putExtra("pictureFilePath", pictureFilePath);
            MainActivity.this.startActivity(myIntent);
        }
    }


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


}
