package com.example.motivationapp.motivationapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class constructs a query for to send to nutritionX API
 */


public class MakeQuery {
    private String APP_ID = "98d149d7";
    private String APP_KEY = "a1c52f33e09a32698a049614fa0a4848";
    private String food_name;
    private JSONObject json;

    public MakeQuery(String food_name) {
        this.food_name = food_name;
        this.json = new JSONObject();
        this.setJSONAuth();
        this.setJSONData();
    }


    public void setJSONAuth() {

        try {
            json.put("appId", this.APP_ID);
            json.put("appKey", this.APP_KEY);
        } catch (JSONException e) {

            e.printStackTrace();
        }


    }

    private void setJSONData() {
        JsonParser jsonParser = new JsonParser();


        String fieldsArr[] = {
                "item_name",
                "brand_name",
                "nf_calories",
                "nf_sodium",
                "nf_iron_dv",
                "nf_total_fat",
                "nf_total_carbohydrate",
                "nf_protein",
                "nf_serving_size_qty",
                "nf_serving_weight_grams",
                "images_front_full_url",
                "item_type"
        };

        int offset = 0;
        int limit = 1;
        double min_score = 0.5;


        List<String> list = Arrays.asList(fieldsArr);

        System.out.println(list);
        try {

            json.put("query", this.food_name);


            json.put("fields", new JSONArray(list));

            json.put("limit", limit);
            json.put("min_score", min_score);
            json.put("offset", offset);

            JSONObject sort = new JSONObject();
            sort.put("field", "_score");
            sort.put("order", "desc");

            json.put("sort", sort);

        } catch (JSONException e) {

            e.printStackTrace();
        }


    }

    public JSONObject getJson() {
        return this.json;
    }

}
