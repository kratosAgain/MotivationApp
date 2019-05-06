package com.example.android.motivatinalapp;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class GetNutritionRequest {
    private String nutritionxURL = "https://api.nutritionix.com/v1_1/search";

    public JSONObject MyGETRequest(String food) throws Exception {
        URL urlForGetRequest = new URL(this.nutritionxURL);
        String readLine = null;
        JSONObject fields=null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        conection.setDoOutput(true);
        conection.setRequestProperty("Content-Type", "application/json");
        conection.setRequestProperty("Accept", "application/json");


        MakeQuery mq = new MakeQuery(food);
        DataOutputStream wr = new DataOutputStream(conection.getOutputStream());
        wr.write(mq.getJson().toString().getBytes());

        int responseCode = conection.getResponseCode();
        Log.i("Motivation","Response code is "+responseCode);


        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conection.getInputStream()));

            StringBuffer response = new StringBuffer();
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();

            JSONObject hits =  (JSONObject) (new JSONObject(response.toString())).getJSONArray("hits").get(0);

            /**
             * API results is in the fields
             */
            fields = hits.getJSONObject("fields");

            Log.i("Motivation","Fields :"+fields.toString());

        } else {

            Log.i("Motivation","GET NOT WORKED");
        }

        return fields;

    }

}
