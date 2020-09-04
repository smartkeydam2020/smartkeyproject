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
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email extends AppCompatActivity {

    private Button btContinue;
    private Button btVolver;
    private EditText etEmail;
    private static final String ARCHIVO = "SmartKeyPreferences";
    public static final String EMAIL_PREFERENCES = "EmailSmartKeyPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_email);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnected())
            finish();
    }

    private void init() {
        etEmail = findViewById(R.id.etEmail);

        SharedPreferences sharedPreferences = getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
        String emailpreferences = sharedPreferences.getString(EMAIL_PREFERENCES, "not found");

        if(!emailpreferences.equals("not found")){
            etEmail.setText(emailpreferences);
        }

        btVolver = findViewById(R.id.btVolver);

        btContinue = findViewById(R.id.btContinuar);
        btVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                if(email.length() == 0 || isEmailValid(email) == false){
                    Toast.makeText(Email.this, getString(R.string.correovalido), Toast.LENGTH_LONG).show();
                }else{
                    SharedPreferences sharedPref = getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(EMAIL_PREFERENCES, email);
                    editor.commit();
                    Intent intent = new Intent(Email.this, Username.class);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Email.this.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isEmailValid(String email){
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}