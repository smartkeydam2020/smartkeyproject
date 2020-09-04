package com.example.smartkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class UserPIN extends AppCompatActivity {

    private Button btContinue;
    private Button btVolver;
    private EditText etPIN, etRepPIN;
    private final String code = "pinscreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_userpin);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnected())
            finish();
    }

    private void init() {
        etPIN = findViewById(R.id.etPIN);
        etRepPIN = findViewById(R.id.etRepPIN);

        btVolver = findViewById(R.id.btVolver);
        btVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btContinue = findViewById(R.id.btContinuar);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPIN.getText().toString().length() < 4 || etPIN.getText().toString().isEmpty()){
                    Toast.makeText(UserPIN.this, getString(R.string.longitudpin), Toast.LENGTH_LONG).show();
                }else{
                    if(etRepPIN.getText().toString().length() < 4 || etRepPIN.getText().toString().isEmpty()){
                        Toast.makeText(UserPIN.this, getString(R.string.longitudreppin), Toast.LENGTH_LONG).show();
                    }else{
                        if(!etPIN.getText().toString().equals(etRepPIN.getText().toString())){
                            Toast.makeText(UserPIN.this, getString(R.string.coincidirpin), Toast.LENGTH_LONG).show();
                        }else{
                            Intent intent = new Intent(UserPIN.this, CodeVerfication.class);
                            intent.putExtra(code, etPIN.getText().toString());
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(UserPIN.this.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}