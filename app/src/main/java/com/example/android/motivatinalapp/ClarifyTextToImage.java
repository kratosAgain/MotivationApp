package com.example.android.motivatinalapp;
import android.os.Environment;

import java.io.*;
import java.util.*;


import clarifai2.api.*;
import clarifai2.dto.input.*;
import clarifai2.dto.*;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.request.input.SearchClause;
import clarifai2.api.request.model.PredictRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.Model;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

public class ClarifyTextToImage {
    private String[] useless = {"food", "fruit","juicy","healthy","person","no person", "breakfast", "dark", "delicioous", "refreshment"
            ,"still life", "life", "tasty","desktop", "sweet","salty", "lunch", "dinner", "fast", "traditional", "unhealthy"

    };
    private String key = "19c0718c6f82456885467e35c8b72d9f";

    public void run(){

        List<String> list = Arrays.asList(useless);
        ClarifaiClient client = new ClarifaiBuilder(key)
                .buildSync();

        Model<Concept> generalModel = client.getDefaultModels().generalModel();
        //Model<Concept> mod = client.getModelByID("food").

        File storagePath = Environment.getExternalStorageDirectory();

        PredictRequest<Concept> request = generalModel.predict()
                .withInputs(
                        ClarifaiInput.forImage(new File(storagePath,"apple.png"))
                        //ClarifaiInput.forImage("images/2image.png")
                );



        List<ClarifaiOutput<Concept>> result = request.executeSync().get();

//        client.searchInputs(SearchClause.matchConcept(Concept.forName("people")))
//        .getPage(1)
//        .executeSync().get();

        String max = "";
        float maxval = 0;
        for(ClarifaiOutput<Concept> cc:result) {

            for(Concept c:cc.data()) {

                if(maxval < c.value() && !list.contains(c.name()) ) {
                    System.out.println(c.name() +"  "+ c.value());
                    maxval = c.value();
                    max = c.name();
                }
            }
        }
        System.out.println("Maximum concept is "+max+" value = "+maxval);
    }
}
