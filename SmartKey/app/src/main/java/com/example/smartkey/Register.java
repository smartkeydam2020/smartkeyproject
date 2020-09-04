package com.example.smartkey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etPhone;
    private Button btContinue;
    private String telefono;
    private static final String ARCHIVO = "SmartKeyPreferences";
    public static final String TELEFONO_PREFERENCES = "PhoneSmartKeyPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnected()){
            finish();
        }

        if(mAuth.getCurrentUser() != null){
            finish();
        }
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        etPhone = findViewById(R.id.etTelefono);

        SharedPreferences sharedPreferences = getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
        String telefonopreference = sharedPreferences.getString(TELEFONO_PREFERENCES, "not found");

        if(!telefonopreference.equals("not found")){
            etPhone.setText(telefonopreference);
        }

        btContinue = findViewById(R.id.btContinuar);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telefono = etPhone.getText().toString();
                if(telefono.isEmpty()){
                    Toast.makeText(Register.this, getString(R.string.porfavorTelefono), Toast.LENGTH_LONG).show();
                }else{
                    if(!telefono.substring(0,1).equals("+")){
                        Toast.makeText(Register.this, getString(R.string.recuerdaprefijo), Toast.LENGTH_LONG).show();
                    }else{
                        comprobarCuenta();
                    }
                }
            }
        });
    }

    public void comprobarCuenta() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("phones").orderByKey().equalTo(telefono).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SharedPreferences sharedPref = getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(TELEFONO_PREFERENCES, telefono);
                editor.commit();

                if(dataSnapshot.exists()){
                    Intent intent = new Intent(Register.this, DoVerification.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Register.this, Email.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Register.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Register.this.CONNECTIVITY_SERVICE);
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