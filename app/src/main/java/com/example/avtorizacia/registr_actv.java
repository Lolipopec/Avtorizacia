package com.example.avtorizacia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class registr_actv extends AppCompatActivity {

    Button btnSignUp, btnSignIn;
    EditText edName, edMail, edPass, edRePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registr_actv);

        btnSignIn = findViewById(R.id.login);
        btnSignUp = findViewById(R.id.btn_register);
        edName = (EditText) findViewById(R.id.et_name);
        edMail = (EditText) findViewById(R.id.et_email);
        edPass = (EditText) findViewById(R.id.et_password);
        edRePass = (EditText) findViewById(R.id.et_repassword);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registr_actv.this, MainActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edMail.getText().toString()) || TextUtils.isEmpty(edName.getText().toString()) || TextUtils.isEmpty(edPass.getText().toString()) || TextUtils.isEmpty(edRePass.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Необходимо заполнить все поля", Toast.LENGTH_LONG).show();
                } else {
                    RegisterRequest registerRequest = new RegisterRequest();
                    registerRequest.setEmail(edMail.getText().toString());
                    registerRequest.setPassword(edPass.getText().toString());
                    registerRequest.setUsername(edName.getText().toString());

                    registerUsers(registerRequest);
                }
            }
        });

    }

    public void registerUsers(RegisterRequest registerRequest) {


        Call<RegisterResponse> registerResponseCall = ApiClient.getService().registerUsers(registerRequest);
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Поздравляю с успешной регистрацией!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(registr_actv.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Ты чего-то перепутал...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
}