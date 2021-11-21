package com.example.avtorizacia;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DBHelper  extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "catalogDb";
    public static final String TABLE_CATALOG = "catalog";
    public static final String TABLE_USERS= "users";

    public static final String KEY_ID = "_id";
    public static final String KEY_PRODUCT = "product";
    public static final String KEY_PRICE = "price";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CATALOG + "(" + KEY_ID
                + " integer primary key," + KEY_PRODUCT + " text," + KEY_PRICE + " text"+")");
        db.execSQL("create table " + TABLE_USERS + "(" + KEY_ID  + " integer primary key," + KEY_LOGIN + " text," + KEY_PASSWORD + " text" +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CATALOG);
        db.execSQL("drop table if exists " + TABLE_USERS);
        onCreate(db);

    }
}