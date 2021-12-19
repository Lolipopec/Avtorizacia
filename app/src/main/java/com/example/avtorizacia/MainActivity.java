package com.example.avtorizacia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    }
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnLog:
                if (TextUtils.isEmpty(etLogin.getText().toString()) || TextUtils.isEmpty(etPass.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Необходимо заполнить все поля", Toast.LENGTH_LONG).show();
                } else {
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setUsername(etLogin.getText().toString());
                    loginRequest.setPassword(etPass.getText().toString());
                    loginUsers(loginRequest);
                }
                break;
            case R.id.btnReg:
                startActivity(new Intent(this, registr_actv.class));
                break;
        }

    }
    public void loginUsers(LoginRequest loginRequest) {
        Call<LoginResponse> loginResponseCall = ApiClient.getService().loginUser(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (etLogin.getText().toString().equals("admin")) {
                        startActivity(new Intent(MainActivity.this, AdminPanel.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                    }
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
}