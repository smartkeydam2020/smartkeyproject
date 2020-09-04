package com.example.smartkey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void init() {
            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(isConnected()){
                        if (firebaseAuth.getCurrentUser() != null) {
                            Intent intent = new Intent(MainActivity.this, ShowPIN.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(MainActivity.this, Register.class);
                            startActivity(intent);
                        }
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(getString(R.string.offlinetitle));
                        builder.setMessage(getString(R.string.offline));
                        builder.setPositiveButton(getString(R.string.closeapp), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                    }
                }
            };
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(MainActivity.this.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

}
