package com.example.usersapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabase extends SQLiteOpenHelper{
    //Initialize important constants
    public static final String TABLE_NAME = "Users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_DESIGNATION = "Designation";
    public static final String COLUMN_LOCATION = "Location";
    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
            + " VARCHAR," + COLUMN_DESIGNATION + " VARCHAR," + COLUMN_LOCATION + " VARCHAR)";
    public static final String MY_DATABASE_NAME = "Users database";
    public static final int MY_DATABASE_VERSION = 1;

    /*To make sure only 1 connection is made to the database, the Constructor is made private to
    make sure objects of this class can only be made inside it. A myDatabase attribute is made
    (myDatabaseInstance). It will     be assigned the database object. A public static method is then
    created which will be accessible to other classes e.g MainActivity because it is public.
    It creates a single database object if and only if it doesn't exist and returns it
     */


    private MyDatabase(@Nullable Context context) {
        super(context, MY_DATABASE_NAME, null, MY_DATABASE_VERSION);
    }
    private static MyDatabase myDatabaseInstance;
    public static MyDatabase getMyDatabaseInstance(Context context){
        if(myDatabaseInstance == null){
            myDatabaseInstance = new MyDatabase(context);
        }
        return myDatabaseInstance;
    }

    //Database methods
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //CRUD Operations
    public long insertUser(String userName,String designation,String location){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,userName);
        contentValues.put(COLUMN_DESIGNATION,designation);
        contentValues.put(COLUMN_LOCATION,location);
        Long id = db.insert(TABLE_NAME,COLUMN_NAME,contentValues);
        return id;
    }
    public Cursor queryUsers(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resultCursor = db.query(TABLE_NAME,new String[]{COLUMN_ID,COLUMN_NAME,COLUMN_DESIGNATION,COLUMN_LOCATION},null,null,null,null,null,null);
        return resultCursor;

    }
    public int updateUser(int userId,String userDesignation){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DESIGNATION,userDesignation);
        int rowsAffected = db.update(TABLE_NAME,contentValues,COLUMN_ID + "=" + userId,null);
        return rowsAffected;
    }
    public int deleteUser(int userId){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NAME,COLUMN_ID + "=" + userId,null);
        return rowsAffected;
    }

}
