package com.example.android.motivatinalapp;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;




import com.google.api.services.customsearch.model.Result;


import org.json.JSONObject;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.api.request.model.PredictRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.Model;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

import static android.R.layout.simple_list_item_1;
import static android.app.Activity.RESULT_OK;

public class Frag2 extends Fragment  {
    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final String CLARIFY_KEY = "19c0718c6f82456885467e35c8b72d9f";

    static final int REQUEST_TAKE_PHOTO = 1;
    private Button buttonNutrition, searchImageButton, googleSearchButton, refresh, takeapic;
    private static EditText searchQuery;
    public static String searchString="";
    private GetNutritionRequest nutritionRequest;
    private ImageView imageview2 ;
    ListView listview = null;
    private String photoPath = null;
    private String object;
//    private SearchView searchView = null;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    public static UserData userDataMain;
    DatabaseHelper db = null;
    String[] list;
    List<UserData> userlist;
    CustomListAdapter adapter;
    private String pictureFilePath;
    private String clarifyString="";
    private PopupWindow POPUP_WINDOW_SCORE = null;
    public static boolean searchFlag= true;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2_layout, container, false);
        buttonNutrition =view.findViewById(R.id.buttonnutrition);
        refresh = view.findViewById(R.id.refresh);
//        googleSearchButton = (Button)view.findViewById(R.id.googleSearchButton);
        searchQuery = view.findViewById(R.id.query);
        searchString = "";
//        imageview2 =  (ImageView)view.findViewById(R.id.imageView2);
        listview = view.findViewById(R.id.records);
        takeapic = view.findViewById(R.id.takeapic);
        Log.i("CURRENT_USERNAME",currentUser.currentUserName);

        userDataMain = new UserData();

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
                try {
                    GoogleImageSearchTask task = new GoogleImageSearchTask();

                        task.execute(searchQuery.getText().toString());

                }catch(Exception e){
                    Log.i("Motivation", "Error in google request");
                    e.printStackTrace();
                }


                //popup for asking user
                popupToAddFoodOrNot();


            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });

        db =new DatabaseHelper(getActivity());
        userlist = new ArrayList<>();
        userlist = db.getUserDataList(currentUser.currentUserName);
        adapter = new CustomListAdapter(getActivity(), db, userlist);
        this.listview.setAdapter(adapter);
        this.listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FootDetails.class);
                UserData u = userlist.get(position);
                intent.putExtra("userdata", u);
                getActivity().startActivity(intent);

            }
        });

        this.takeapic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                takePicture(v);
            }
        });



        takePermission();

//        updateData();

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1888 && resultCode == RESULT_OK) {
            if (pictureFilePath != null && pictureFilePath.length()!=0) {

                try {
                    ClarifaiTask ca = new ClarifaiTask();
                    ca.execute();
                }catch(Exception e){
                    Log.i("CLARIFY", "Error in clarify , in activity result");
                    e.printStackTrace();
                }

            }
            Log.i("CLARIFY","clairifystring "+clarifyString);
        }
    }


    public void takePicture(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1);

            File pictureFile = null;
            try {
                pictureFile = getPictureFile();
            } catch (IOException ex) {
                Toast.makeText(getActivity(),
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        getActivity().getApplicationContext().getApplicationContext().getPackageName() + ".provider",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, 1888);
            }
        }
    }

    private File getPictureFile() throws IOException {
//        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "foodimage";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }



    private void reload(){
        Intent intent = getActivity().getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getActivity().finish();
        startActivity(intent);
    }

//    private void updateData() {
//        userlist = db.getUserDataList(currentUser.currentUserName);
//        adapter.setUserList(userlist);
//
//
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                adapter.notifyDataSetChanged();
//            }
//        });
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

            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.CAMERA}, 101);
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

    public Bitmap getImageFromUser(){
        try {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(userDataMain.image);
            Bitmap foodImage = BitmapFactory.decodeStream(imageStream);
//            this.imageView.setImageBitmap(foodImage);
            imageStream.close();
            return foodImage;
        }catch(Exception e){
            Log.e("ERROR_FOODDETAILS","Error in image");
            e.printStackTrace();

        }
        return null;
    }

    public void popupToAddFoodOrNot(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.popup_forfood);
        dialog.setTitle("Really");
        TextView warning = (TextView) dialog.findViewById(R.id.layout_popup_txtMessage);
        warning.setText("This food has "+userDataMain.calories+"cal\nGoal : "+Frag1.carbsPerDay*10);

        ImageView image = (ImageView) dialog.findViewById(R.id.layout_popup_image);

        Bitmap foodImage = getImageFromUser();
        if(foodImage!=null) {
            image.setImageBitmap(foodImage);
        }

        Button dialogButtonNo = (Button) dialog.findViewById(R.id.layout_popup_No);
        // if button is clicked, close the custom dialog
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        Button dialogButtonYes = (Button) dialog.findViewById(R.id.layout_popup_Yes);
        // if button is clicked, close the custom dialog
        dialogButtonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    db.insertUserData(userDataMain);

                }catch(Exception e){
                    Log.i("Motivation", "Error in userdata base writing inside popup");
                    e.printStackTrace();
                }finally{
                    dialog.dismiss();
                }
            }
        });

        dialog.show();

    }

    public static class GetUrlContentTask extends AsyncTask<String, Integer, String> {
        private GetNutritionRequest nutritionRequestInner;
        public void fetchNutritionApiData(GetNutritionRequest nutritionRequest) {
            try {
                String qr = searchQuery.getText().toString();

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

//                try{
//                    db.insertUserData(userDataMain);
//
//                }catch(Exception e){
//                    Log.i("Motivation", "Error in userdata base writing");
//                    e.printStackTrace();
//                }

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
//                File storagePath = Environment.getExternalStorageDirectory();
//                OutputStream output = new FileOutputStream(new File(storagePath,query+".png"));
//                byte[] buffer = new byte[512];
//                int bytesRead = 0;
//                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
//                    output.write(buffer, 0, bytesRead);
//                }
//                output.close();
                input.close();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bimage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte imageInByte[] = stream.toByteArray();
                userDataMain.setImage(imageInByte);
                stream.close();

//                Bitmap x;
//
//                /**
//                 * create drawable from Google URL, we can also open the file we save above, its just another way
//                 * of doing it.
//                 */
//
//                HttpURLConnection connection = (HttpURLConnection) new URL(urlstr).openConnection();
//                connection.connect();
//                InputStream in = connection.getInputStream();
//
//                x = BitmapFactory.decodeStream(in);
                drawable = new BitmapDrawable(getContext().getResources(),bimage);
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
//                drawImageInView(drawable);
            }
        }


    public class ClarifaiTask extends AsyncTask<String, Integer, ClarifaiResponse<List<ClarifaiOutput<Concept>>>> {



        PredictRequest<Concept> predictionResult=null;


        protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(String... images) {

            // For each photo we pass, send it off to Clarifai
            ClarifaiResponse<List<ClarifaiOutput<Concept>>> results=null;


            try {
                ClarifaiClient clarifaiclient = new ClarifaiBuilder(CLARIFY_KEY).buildSync();


                Model<Concept> generalModel = clarifaiclient.getDefaultModels().generalModel();
                predictionResult = generalModel.predict().withInputs(
                        ClarifaiInput.forImage(new File(pictureFilePath))
                );
                results = predictionResult.executeSync();

                // Check if Clarifai thinks the photo contains the object we are looking for
                Log.i("CLARIFY", "before the results");
                Log.i("CLARIFY", predictionResult.toString());


            } catch (Exception e) {
                Log.i("CLARIFY_ERROR", "ERROR " + e.toString());
            }

            return results;
        }

        protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
            String[] useless = {"food", "fruit", "juicy", "healthy", "person", "no person", "breakfast", "dark", "delicioous", "refreshment"
                    , "still life", "life", "tasty", "desktop", "sweet", "salty", "lunch", "dinner", "fast", "traditional", "unhealthy", "blur", "indoors", "bright"
                    , "paper", "one", "two", "contemporary", "nutrition", "healthcare", "still", "still life", "business", "work", "vitamin", "protein" , "delicious", "grow",
                    "indoors", "ready", "baking", "wool","meal","ingredients"

            };
            String max = "french";
            List<ClarifaiOutput<Concept>> results = response.get();
            try {

                float maxval = 0;
                List<String> uselesslist = Arrays.asList(useless);
                if (results != null) {
                    int i = 0;
                    for (ClarifaiOutput<Concept> r : results) {
                        for (Concept c : r.data()) {
                            if (maxval < c.value() && !uselesslist.contains(c.name())) {

                                maxval = c.value();
                                max = c.name();
                            }
                            Log.i("CLARIFY", c.toString());
                            i++;
                            if(i>10){
                                break;
                            }
                        }

                        Log.i("CLARIFY", "first concept");
                        break;
                    }
                } else {
                    Log.i("CLARIFY", "results is null");
                }
            }catch(Exception e){
                Log.i("CLARIFY", "Exception in max value");
                e.printStackTrace();
            }
            Log.i("CLARIFY", "Done   "+max);
            clarifyString = max;
            searchQuery.invalidate();
            searchQuery.setText(clarifyString);
            buttonNutrition.invalidate();
//            buttonNutrition.performClick();
            Log.i("CLARIFY", "after click  string  "+clarifyString);
//            textView.setText(max + " " + maxval);
        }
    }




}




