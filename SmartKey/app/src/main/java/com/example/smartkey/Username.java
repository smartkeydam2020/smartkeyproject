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

public class Username extends AppCompatActivity {

    private Button btContinue;
    private Button btVolver;
    private EditText etUsername;
    private static final String ARCHIVO = "SmartKeyPreferences";
    public static final String NAME_PREFERENCES = "NameSmartKeyPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_username);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnected())
            finish();
    }

    private void init() {
        etUsername = findViewById(R.id.etUsername);

        SharedPreferences sharedPreferences = getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
        String namepreferences = sharedPreferences.getString(NAME_PREFERENCES, "not found");

        if(!namepreferences.equals("not found")){
            etUsername.setText(namepreferences);
        }

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
                String username = etUsername.getText().toString();
                if(username.length() < 5 || !isValidName(username)){
                    Toast.makeText(Username.this, getString(R.string.nombrevalido), Toast.LENGTH_LONG).show();
                }else{
                    SharedPreferences sharedPref = getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(NAME_PREFERENCES, username);
                    editor.commit();
                    Intent intent = new Intent(Username.this, UserPIN.class);
                    startActivity(intent);
                }
            }
        });
    }

    public final static boolean isValidName(String target) {
        return Pattern.compile("^[^-\\s][a-zA-ZáéíóúÁÉÍÓÚ -]+$").matcher(target).matches();
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Username.this.CONNECTIVITY_SERVICE);
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