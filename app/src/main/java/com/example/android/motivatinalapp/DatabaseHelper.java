package com.example.android.motivatinalapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME ="contacts.db";
    private static final String TABLE_NAME_CONTACTS = "contacts";
    private static final String TABLE_NAME_USERDATA = "userdata";
    private static final String COLUMN_ID ="id";
    private static final String COLUMN_NAME ="name";
    private static final String COLUMN_EMAIL ="email";
    private static final String COLUMN_UNAME ="uname";
    private static final String COLUMN_PASS ="pass";


    private static final String FOOD_NAME = "food_name";
    private static final String CALORIES = "calories";
    private static final String CARBS = "carbs";
    private static final String FAT = "fat";
    private static final String PROTEIN = "protein";
    private static final String SODIUM = "sodium";
    private static final String IRON = "real";
    private static final String DATE = "date";
    private static final String GIMAGE = "gimage";
    private static final String BMI = "bmi";

    private static final String TABLE_CREATE_CONTACTS ="create table contacts("+
    "uname text primary key not null, name text not null, email text not null,  pass text not null)";

    private static final String TABLE_CREATE_USERDATA = "create table userdata("+
     "uname text not null, food_name text not null, calories real not null, carbs real, fat real, protein real, sodium real, iron real, bmi real, date text, gimage blob)";




    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }






    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_CONTACTS);
        db.execSQL(TABLE_CREATE_USERDATA);

        this.db=db;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query ="DROP TABLE IF EXISTS " +this.TABLE_NAME_CONTACTS;
        db.execSQL(query);
        query ="DROP TABLE IF EXISTS " +this.TABLE_NAME_USERDATA;
        db.execSQL(query);

        this.onCreate(db);
    }

    public void insertContact(Contact c)
    {
        db = this.getWritableDatabase();
        ContentValues values=new ContentValues();
//        String q ="select * from "+this.TABLE_NAME_CONTACTS;
//        Cursor cursor =db.rawQuery(q,null);
//        int count = cursor.getCount();
//        values.put(COLUMN_ID,count +1);
        values.put(COLUMN_NAME, c.getNam());
        values.put(COLUMN_EMAIL, c.getEmail());
        values.put(COLUMN_UNAME,c.getUname());
        values.put(COLUMN_PASS,c.getPass());
        db.insert(this.TABLE_NAME_CONTACTS,null, values);
        db.close();

    }

    public void insertUserData(UserData u){
        db = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(this.COLUMN_UNAME, u.userName);
        values.put(this.FOOD_NAME, u.foodName);
        values.put(this.CALORIES, u.calories);
        values.put(this.FAT, u.fat);
        values.put(this.IRON, u.iron);
        values.put(this.SODIUM, u.sodium);
        values.put(this.PROTEIN, u.protein);
        values.put(this.CARBS, u.carbs);
        values.put(this.GIMAGE, u.image);
        values.put(this.DATE, "datetime('now','localtime'");
        values.put(this.BMI, u.bmi);
        db.insert(this.TABLE_NAME_USERDATA,null, values);
        db.close();
    }



    public String searchdatabase(String str)
    {
        Log.d("INside the search","inside");
        db = this.getReadableDatabase();
        Log.d("After db","connect");
        String query = "select * from "+ this.TABLE_NAME_CONTACTS+" WHERE uname = ?";
//        String query = "select * from contacts";
        Cursor c =db.rawQuery(query,new String[] {str});
        String user;
        String password ="Not Found";
        try {
            if (c.moveToFirst()) {
                user = c.getString(0);
                Log.i("USER_NAME", user);
                password = c.getColumnName(3);
            }
        }catch(Exception e){
            Log.e("CURSOR_ERROR", "error in database handling");
            e.printStackTrace();
            password ="Not Found or NULL";
        }

//        if(c.moveToFirst())
//        {
//            do {
//                a= c.getString(3);
//                Log.d("The username is:",  a);
//
//                if(a.equals(str))
//                {
//                    b=c.getString(4);
//                    break;
//                }
//
//
//
//            }
//            while(c.moveToNext());
//        }
        return password;

    }

    public Cursor searchUserDatabase(String username){
        db = this.getReadableDatabase();
        String query = "select * from "+ this.TABLE_NAME_USERDATA+" WHERE uname = ?";
        Cursor c  = null;
        try{
            c =db.rawQuery(query,new String[] {username});
        }catch(Exception e){
            Log.e("CURSOR_ERROR", "error in userdata database handling");
            e.printStackTrace();
        }

        return c;
    }
}
