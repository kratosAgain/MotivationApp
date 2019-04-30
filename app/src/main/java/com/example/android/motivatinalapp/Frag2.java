package com.example.android.motivatinalapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.R.layout;
import android.widget.Toast;
import android.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;

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
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;



import static android.R.layout.simple_list_item_1;

public class Frag2 extends Fragment  {
    private static final int CAMERA_PIC_REQUEST = 1337;

    static final int REQUEST_TAKE_PHOTO = 1;
    private Button buttonNutrition, searchImageButton, googleSearchButton;
    private EditText searchQuery;
    private GetNutritionRequest nutritionRequest;
    private ImageView imageview2 ;
    private String photoPath = null;
    private String object;
//    private SearchView searchView = null;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    String[] list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2_layout, container, false);
        buttonNutrition =(Button)view.findViewById(R.id.buttonnutrition);
        googleSearchButton = (Button)view.findViewById(R.id.googleSearchButton);
        searchQuery = (EditText)view.findViewById(R.id.query);
        imageview2 =  (ImageView)view.findViewById(R.id.imageView2);

        buttonNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nutritionRequest = new GetNutritionRequest();
                try{
                    GetUrlContentTask task = new GetUrlContentTask();
                    task.execute();
                }
                catch (Exception e) {
                    Log.i("Motivation", "Error in get request initialization");
                    e.printStackTrace();
                }
            }
        });
        googleSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleImageSearchTask task = new GoogleImageSearchTask();

                task.execute(searchQuery.getText().toString());
            }


        });
        takePermission();


        return view;

    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        initialize();
//
//    }
//
//    public void initialize()
//    {
//
//
//
//    }

    private void takePermission()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this.getActivity(),
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
                    Toast.makeText(this.getContext(), "Permission denied to access external storage.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void drawImageInView(Drawable drawable){

        this.imageview2.setImageDrawable(drawable);
    }

    private static class GetUrlContentTask extends AsyncTask<String, Integer, String> {
        private GetNutritionRequest nutritionRequestInner;
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

    private class GoogleImageSearchTask extends AsyncTask<String, Integer, String> {
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
                File storagePath = Environment.getExternalStorageDirectory();
                OutputStream output = new FileOutputStream(new File(storagePath,query+".png"));
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
                drawable = new BitmapDrawable(getContext().getResources(),x);
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




