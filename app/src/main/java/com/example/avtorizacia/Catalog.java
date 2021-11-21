package com.example.avtorizacia;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
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
import android.widget.Toast;

public class Catalog extends AppCompatActivity implements View.OnClickListener {
    Button btnBack;
    SQLiteDatabase database;
    ContentValues contentValues;
    DBHelper dbHelper;
    TextView tvBasket,tvBasket2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        tvBasket = (TextView) findViewById(R.id.tvBasket);
        tvBasket.setOnClickListener(this);
        tvBasket2 = (TextView) findViewById(R.id.tvBasket2);
        tvBasket2.setOnClickListener(this);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        UpdateTable();
    }
    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_CATALOG, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int dishIndex = cursor.getColumnIndex(DBHelper.KEY_PRODUCT);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            TableLayout dpOutput = findViewById(R.id.dpOutput);
            dpOutput.removeAllViews();
            do {
                TableRow dpOutputRow = new TableRow(this);
                dpOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                TextView outputDish = new TextView(this);
                params.weight = 3.0f;
                outputDish.setLayoutParams(params);
                outputDish.setText(cursor.getString(dishIndex));
                dpOutputRow.addView(outputDish);

                TextView outputPrice = new TextView(this);
                params.weight = 3.0f;
                outputPrice.setLayoutParams(params);
                outputPrice.setText(cursor.getString(priceIndex));
                dpOutputRow.addView(outputPrice);

                Button buyBtn = new Button(this);
                buyBtn.setOnClickListener(this);
                params.weight = 1.0f;
                buyBtn.setLayoutParams(params);
                buyBtn.setText("Купить");
                buyBtn.setId(cursor.getInt(idIndex));
                dpOutputRow.addView(buyBtn);

                dpOutput.addView(dpOutputRow);

            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    @Override
    public void onClick(View v) {

        dbHelper = new DBHelper(this);
        switch (v.getId()) {
            case R.id.btnBack:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.tvBasket:
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Сумма "+tvBasket.getText(), Toast.LENGTH_SHORT);
                toast.show();
                tvBasket.setText("0");
                break;
            case R.id.tvBasket2:
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Сумма "+tvBasket.getText(), Toast.LENGTH_SHORT);
                toast1.show();
                tvBasket.setText("0");
                break;
            default:
                Button btn = (Button) v;
                switch (btn.getText().toString()) {
                    case "Купить":
                        String selection = "_id = ?";
                        Cursor c = database.query(DBHelper.TABLE_CATALOG, null, selection, new String[]{String.valueOf(v.getId())}, null, null, null);
                        float sum = Float.valueOf(tvBasket.getText().toString());
                        float s = 0;
                        if (c.moveToFirst()) {
                            int Price = c.getColumnIndex(DBHelper.KEY_PRICE);
                            do {
                                s = c.getFloat(Price);
                            } while (c.moveToNext());
                        }
                        c.close();
                        sum= sum + s;
                        tvBasket.setText(String.valueOf(sum));
                        break;
                    case "Удалить":
                        View outputDBRow = (View) v.getParent();
                        ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                        outputDB.removeView(outputDBRow);
                        outputDB.invalidate();
                        database.delete(DBHelper.TABLE_CATALOG, dbHelper.KEY_ID + " = ?", new String[]{String.valueOf(v.getId())});
                        contentValues = new ContentValues();
                        Cursor cursorUpdater = database.query(DBHelper.TABLE_CATALOG, null, null, null, null, null, null);
                        if (cursorUpdater.moveToFirst()) {
                            int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                            int dishIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRODUCT);
                            int priceIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRICE);
                            int realID = 1;
                            do {
                                if (cursorUpdater.getInt(idIndex) > realID) {
                                    contentValues.put(DBHelper.KEY_ID, realID);
                                    contentValues.put(DBHelper.KEY_PRODUCT, cursorUpdater.getString(dishIndex));
                                    contentValues.put(DBHelper.KEY_PRICE, cursorUpdater.getString(priceIndex));
                                    database.replace(DBHelper.TABLE_CATALOG, null, contentValues);
                                }
                                realID++;
                            } while (cursorUpdater.moveToNext());
                            if (cursorUpdater.moveToLast() && v.getId() != realID) {
                                database.delete(dbHelper.TABLE_CATALOG, DBHelper.KEY_ID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                            }
                            UpdateTable();
                        }
                        break;
                }
        }
        dbHelper.close();
    }
}