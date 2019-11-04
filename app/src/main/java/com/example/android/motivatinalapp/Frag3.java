package com.example.android.motivatinalapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.api.client.util.Data;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Frag3 extends Fragment {



    public GraphView linegraph , bargraph;
    public BarChart nutrientsDayCompareBarchart;
    public BarChart nutrients_week_compare_barchart;
    DatabaseHelper db = null;


    Map<String, Double> nutrientValues = null;

    public Frag3() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_frag3, container, false);
        db = new DatabaseHelper(getActivity());

//        this.linegraph = (GraphView) view.findViewById(R.id.line_graph);
//        this.bargraph = (GraphView) view.findViewById(R.id.bar_graph);
        this.nutrientsDayCompareBarchart = view.findViewById(R.id.nutrients_day_compare_barchart);
        this.nutrients_week_compare_barchart = view.findViewById(R.id.nutrients_week_compare_barchart);
//        this.makegraph();
//        this.lastAllHoursGraphOfCalories();



        Frag1.setNutrientValues();
        this.nutrientValues = new HashMap() {{
            put("calories", Frag1.caloriesPerDay*1.0);
            put("protein", Frag1.proteinPerDay*1.0);
            put("fat", Frag1.fatPerDay*1.0);
            put("carbs", Frag1.carbsPerDay*1.0);
            put("sodium", Frag1.sodiumPerDay*1.0);
            put("iron", Frag1.ironPerDay*1.0);
        }};
        this.makeNutrientsComparisonBarChart();

        this.weeklyGraphOfCalories();


        return view;
    }


    public void makegraph(){
        LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        linegraph.addSeries(lineSeries);
    }


    public void lastAllHoursGraphOfCalories(){
        Cursor cursor = db.searchUserDatabase(currentUser.currentUserName);
        int i = 0;
        if (cursor==null) {
            Log.i("Cursor","Cursor is Null");
            return;
        }

        //get calories and hour using cursor
        //current time
        Date currentTime = Calendar.getInstance().getTime();
        long currentHour  = currentTime.getHours();

        HashMap<Long, Double> hourCalorieMap = new HashMap();
        long l =0;
        for(l=0;l<=currentHour;l++){
            hourCalorieMap.put(l,0.0);
        }

        while(cursor.moveToNext()){
            try{
                long hour = db.getDate(cursor).getHours();
                double calories = db.getNutrientValue(cursor, "calories");

                if(hour <= currentHour){
                    hourCalorieMap.put(hour, hourCalorieMap.getOrDefault(hour,0.0) + calories);
                }

            }catch(Exception e){
                Log.e("CURSOR_ERROR","error in making map from cursor");
                e.printStackTrace();
            }
        }
        //closing cursor
        cursor.close();
        DataPoint[] points = new DataPoint[hourCalorieMap.size()];
        i = 0;
        for(long key : hourCalorieMap.keySet()){
            DataPoint dp = new DataPoint(key, hourCalorieMap.get(key));
            points[i++] = dp;
        }

        BarGraphSeries<DataPoint> barData = new BarGraphSeries<>(points);
        this.bargraph.setTitle("Calories consumed today (hours)");
        this.bargraph.addSeries(barData);
    }

    @TargetApi(26)
    public void weeklyGraphOfCalories(){
        Cursor cursor = db.searchUserDatabase(currentUser.currentUserName);
        int i = 0;
        if (cursor==null) {
            Log.i("Cursor","Cursor is Null");
            return;
        }
        List<BarEntry> entriesRecommended = new ArrayList<>();
        List<BarEntry> entriesRecorded = new ArrayList<>();
        List<BarEntry> entriesmaxvals = new ArrayList<>();
        List<BarEntry> entriesminvals = new ArrayList<>();

        HashMap<String, Double> nutrientsMap = new HashMap();
        ArrayList<String> nutrientsList = new ArrayList<>();
        HashMap<String,Double> maxvals = new HashMap<>();
        HashMap<String,Double> minvals = new HashMap<>();

        for(String s:this.nutrientValues.keySet()){
            nutrientsMap.put(s,0.0);
            nutrientsList.add(s);
            maxvals.put(s,20.0);
            minvals.put(s,40.0);
        }

        //here
        Calendar currenttime = Calendar.getInstance();
        currenttime.add(Calendar.DATE,-7);
        Date curtime = currenttime.getTime();
        String strtime = curtime.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateObject = sdf.format(curtime);
        String todaydate = sdf.format(Calendar.DATE);

        double nut = 0;
        while(cursor.moveToNext()){
            try{
                Date day = db.getDate(cursor);
                String presentday = sdf.format(day);
                Log.i("presentday cursor",presentday);
                Log.i("todaydate",todaydate );
                if(presentday!=dateObject)
                    continue;
                if(presentday== todaydate)
                    break;

                for(String s: this.nutrientValues.keySet()) {
                    nut= db.getNutrientValue(cursor, s);
//                    if (maxvals.getOrDefault(s,0.0)<nut)
//                        maxvals.put(s,nut);
//                    if (minvals.getOrDefault(s,0.0)>nut)
//                        minvals.put(s,nut);
                    if (s.equals("sodium")) {
                        nut = nut / 10.0;
                    }else if(s.equals("calories")){
                        nut = nut/10.0;
                    }
                    nutrientsMap.put(s, nutrientsMap.get(s) + nut);
                }
            }catch(Exception e){
                Log.e("CURSOR_ERROR","error in making bar chart from cursor");
                e.printStackTrace();
            }
        }
        cursor.close();


        for(String s:this.nutrientValues.keySet()) {
            entriesRecommended.add(new BarEntry(i, 5*this.nutrientValues.get(s).floatValue()));
            entriesRecorded.add(new BarEntry(i, 7*nutrientsMap.get(s).floatValue()));
            entriesmaxvals.add(new BarEntry(i,maxvals.get(s).floatValue()));
            entriesminvals.add(new BarEntry(i,minvals.get(s).floatValue()));
            i++;
        }


        //making bar chart start
        BarDataSet set1 = new BarDataSet(entriesRecommended, "Recommended");
        set1.setColor(Color.rgb(0, 155, 0));
        BarDataSet set2 = new BarDataSet(entriesRecorded, "Taken");
        set2.setColor(Color.rgb(155, 0, 0));
        BarDataSet set3 = new BarDataSet(entriesmaxvals,"Max value taken");
        set3.setColor(android.R.color.holo_blue_dark);
        BarDataSet set4 = new BarDataSet(entriesminvals,"Min value taken");
        set3.setColor(android.R.color.black);


        float groupSpace = 0.06f;
        float barSpace = 0.01f; // x2 dataset
        float barWidth = 0.40f; // x2 dataset

        BarData data = new BarData(set1, set2,set3,set4);
        data.setBarWidth(barWidth); // set the width of each bar
        this.nutrients_week_compare_barchart.setData(data);
        this.nutrients_week_compare_barchart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
        XAxis xA = this.nutrients_week_compare_barchart.getXAxis();
        xA.setValueFormatter(new ValueFormatter(){
            public String getFormattedValue(float value){
                return nutrientsList.get((int)value);
            }
        } );
        this.nutrients_week_compare_barchart.setFitBars(true);
        this.nutrients_week_compare_barchart.invalidate();

        //here


    }

    public void makeNutrientsComparisonBarChart(){

        Cursor cursor = db.searchUserDatabase(currentUser.currentUserName);
        int i = 0;
        if (cursor==null) {
            Log.i("Cursor","Cursor is Null");
            return;
        }

        List<BarEntry> entriesRecommended = new ArrayList<>();
        List<BarEntry> entriesRecorded = new ArrayList<>();

        HashMap<String, Double> nutrientsMap = new HashMap();
        ArrayList<String> nutrientsList = new ArrayList<>();


        for(String s:this.nutrientValues.keySet()){
            nutrientsMap.put(s,0.0);
            nutrientsList.add(s);
        }

        //taking out nutrients for the person for this day
        Date currentTime = Calendar.getInstance().getTime();
        long currentDay  = currentTime.getDay();

        while(cursor.moveToNext()){
            try{
                long day = db.getDate(cursor).getDay();
                if(day!=currentDay)
                    continue;

                for(String s: this.nutrientValues.keySet()) {
                    double nut = db.getNutrientValue(cursor, s);
                    if (s.equals("sodium")) {
                        nut = nut / 10.0;
                    }else if(s.equals("calories")){
                        nut = nut/10.0;
                    }
                    nutrientsMap.put(s, nutrientsMap.get(s) + nut);
                }
            }catch(Exception e){
                Log.e("CURSOR_ERROR","error in making bar chart from cursor");
                e.printStackTrace();
            }
        }
        cursor.close();


        for(String s:this.nutrientValues.keySet()) {
            entriesRecommended.add(new BarEntry(i, this.nutrientValues.get(s).floatValue()));
            entriesRecorded.add(new BarEntry(i, nutrientsMap.get(s).floatValue()));
            i++;
        }


        //making bar chart start
        BarDataSet set1 = new BarDataSet(entriesRecommended, "Recommended");
        set1.setColor(Color.rgb(0, 155, 0));
        BarDataSet set2 = new BarDataSet(entriesRecorded, "Taken");
        set2.setColor(Color.rgb(155, 0, 0));

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset

        BarData data = new BarData(set1, set2);
        data.setBarWidth(barWidth); // set the width of each bar
        this.nutrientsDayCompareBarchart.setData(data);
        this.nutrientsDayCompareBarchart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
        XAxis xA = this.nutrientsDayCompareBarchart.getXAxis();
        xA.setValueFormatter(new ValueFormatter(){
            public String getFormattedValue(float value){
                return nutrientsList.get((int)value);
            }
        } );
        this.nutrientsDayCompareBarchart.setFitBars(true);
        this.nutrientsDayCompareBarchart.invalidate();

        //making bar chart ends

    }

}
