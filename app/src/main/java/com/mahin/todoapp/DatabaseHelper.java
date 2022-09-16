package com.mahin.todoapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME= "toDoList";
    public static final String LIST_TABLE_NAME= "itemList";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(
                    "create table " + LIST_TABLE_NAME + "(id INTEGER PRIMARY KEY, item text)"
            );
        }
        catch(SQLiteException e){
            try{
                throw new IOException(e);
            } catch(IOException e1){
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LIST_TABLE_NAME);
        onCreate(db);
    }

    public ArrayList getAllAcontacts(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery("select * from "+LIST_TABLE_NAME, null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndexOrThrow("item")));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
    public boolean insert(String s){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("item", s);
        db.replace(LIST_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean update(String s, int pos){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        List<Integer> database_ids= new ArrayList<Integer>();

        contentValues.put("item", s);

        Cursor c = db.rawQuery("SELECT*FROM "+LIST_TABLE_NAME,null);
        while(c.moveToNext()){
            database_ids.add(Integer.parseInt(c.getString(0)));
        }

        db.update(LIST_TABLE_NAME, contentValues, " id=?",
                new String[] { String.valueOf(database_ids.get(pos)) });
        c.close();
        return true;
    }

    public void deleteItem(int pos) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Integer> database_ids= new ArrayList<Integer>();

        Cursor c = db.rawQuery("SELECT*FROM "+LIST_TABLE_NAME,null);
        while(c.moveToNext()){
            database_ids.add(Integer.parseInt(c.getString(0)));
        }

        db.delete(LIST_TABLE_NAME, " id=?",
                new String[] { String.valueOf(database_ids.get(pos)) });
        c.close();
    }



}
