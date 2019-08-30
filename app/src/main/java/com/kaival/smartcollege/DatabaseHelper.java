package com.kaival.smartcollege;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME="notification_data";
    public static final String COL1="ID";
    public static final String COL2="NAME";
    public static final String COL3="VALUE";

    public DatabaseHelper(Context context)
    {
        super(context,TABLE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
     String createTable="CREATE TABLE "+TABLE_NAME+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+COL2+" TEXT,"+COL3+" TEXT)";
     db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP IF TABLE EXISTS "+ TABLE_NAME);
    onCreate(db);
    }
    public boolean addData(String name,String value)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL2,name);
        contentValues.put(COL3,value);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
        {
            System.out.println("Stored Failed!");
            return false;
        }
        else
        {
            System.out.println("Stored Success!");
            return true;
        }
    }
    public Cursor getData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query="SELECT * FROM "+TABLE_NAME;
        Cursor data=db.rawQuery(query,null);
        return data;
    }
}

