package com.example.avtorizacia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLog, btnReg;
    EditText etLogin, etPass;
    DBHelper dbHelper;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLog = (Button) findViewById(R.id.btnLog);
        btnLog.setOnClickListener(this);

        btnReg = (Button) findViewById(R.id.btnReg);
        btnReg.setOnClickListener(this);

        etLogin = (EditText) findViewById(R.id.etLog);
        etPass = (EditText) findViewById(R.id.etPass);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_LOGIN, "admin");
        contentValues.put(DBHelper.KEY_PASSWORD, "admin");
        database.insert(DBHelper.TABLE_USERS,null, contentValues);
    }
    public void onClick(View view) {
        Boolean f = false;
        switch(view.getId())
        {
            case R.id.btnLog:
                Cursor cursor = database.query(DBHelper.TABLE_USERS, null,null,null,null,null,null);
                Boolean check = false;
                if(cursor.moveToFirst()){
                    int loginIndex = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
                    int passIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);
                    do{ if (etLogin.getText().toString().equals("admin") && etPass.getText().toString().equals("admin")) {
                        if (etLogin.getText().toString().equals(cursor.getString(loginIndex)) && etPass.getText().toString().equals(cursor.getString(passIndex))) {
                            startActivity(new Intent(this, AdminPanel.class));
                            check = true;
                            break;
                        }
                    } else if (etLogin.getText().toString().equals(cursor.getString(loginIndex)) && etPass.getText().toString().equals(cursor.getString(passIndex))) {
                        startActivity(new Intent(this, Catalog.class));
                        check = true;
                        break;
                    }


                    }while (cursor.moveToNext());
                }
                cursor.close();

                if (check == false) Toast.makeText(this,"Такой пользователь не зарегестрирован", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnReg:
                Cursor cursorRegProvLog = database.query(DBHelper.TABLE_USERS, null,null,null,null,null,null);

                Boolean checkRegProvLog   = false;

                if(cursorRegProvLog.moveToFirst()){
                    int loginIndex = cursorRegProvLog.getColumnIndex(DBHelper.KEY_LOGIN);

                    do{
                        if (etLogin.getText().toString().equals(cursorRegProvLog.getString(loginIndex))) {
                            Toast.makeText(this,"Введенный логин уже занят", Toast.LENGTH_LONG).show();
                            checkRegProvLog = true;
                            break;
                        }

                    }while (cursorRegProvLog.moveToNext());
                }
                if (checkRegProvLog == false)
                {ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_LOGIN, etLogin.getText().toString());
                    contentValues.put(DBHelper.KEY_PASSWORD, etPass.getText().toString());
                    database.insert(DBHelper.TABLE_USERS,null, contentValues);
                    Toast.makeText(this,"Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                }
                cursorRegProvLog.close();

                break;
        }

    }
}