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
    private static final String TABLE_NAME ="contacts";
    private static final String COLUMN_ID ="id";
    private static final String COLUMN_NAME ="name";
    private static final String COLUMN_EMAIL ="email";
    private static final String COLUMN_UNAME ="uname";
    private static final String COLUMN_PASS ="pass";
    private static final String TABLE_CREATE ="create table contacts(id INTEGER primary key NOT NULL , "+
    "name text not null, email text not null, uname text not null, pass text not null);";


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }






    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db=db;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query ="DROP TABLE IF EXISTS " +TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }
    public void insertContact(Contact c)
    {
        db = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        String q ="select * from contacts";
        Cursor cursor =db.rawQuery(q,null);
        int count = cursor.getCount();
        values.put(COLUMN_ID,count +1);
        values.put(COLUMN_NAME, c.getNam());
        values.put(COLUMN_EMAIL, c.getEmail());
        values.put(COLUMN_UNAME,c.getUname());
        values.put(COLUMN_PASS,c.getPass());
        db.insert(TABLE_NAME,null, values);
        db.close();

    }
    public String searchdatabase(String str)
    {
        Log.d("INside the search","inside");
        db = this.getReadableDatabase();
        Log.d("After db","connect");
        String query = "select * from "+ TABLE_NAME;
        Cursor c =db.rawQuery(query,null);
        String a, b;
        b="Not Found";
        if(c.moveToFirst())
        {
            do {
                a= c.getString(3);
                Log.d("The username is:",  a);

                if(a.equals(str))
                {
                    b=c.getString(4);
                    break;
                }



            }
            while(c.moveToNext());
        }
        return b;

    }
}
