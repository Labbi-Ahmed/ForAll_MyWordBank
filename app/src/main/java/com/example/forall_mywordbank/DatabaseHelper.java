package com.example.forall_mywordbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "WordList.db";
    public static final String TABLE_NAME = "MyWord_table";
    public static final String TABLE_NAME2 = "AllMyWord_table_";
    //public static final String COL_1 = "ID";
    public static final String COL_2 = "ENGLISH";
    public static final String COL_3 = "MEANING";
    private MainActivity mainActivity;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME2 + "(ENGLISH TEXT PRIMARY KEY, MEANING TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME2);
        onCreate(db);
    }


    public boolean insertData(String English, String Meaning){
        //Toast.makeText(mainActivity,"hi",Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,English);
        contentValues.put(COL_3,Meaning);

        Long result =db.insert(TABLE_NAME2, null, contentValues);

        if(result == -1)
            return  false;
        else
            return true;
        //return true;
    }
    // create method for view data
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME2,null);

        return res;
    }


    public Integer deleteData(String word){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME2, "ENGLISH = ?", new String[]{word});
    }

//    public boolean updateData(String id,String English, String Meaning){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        return false;
//
//    }

}
