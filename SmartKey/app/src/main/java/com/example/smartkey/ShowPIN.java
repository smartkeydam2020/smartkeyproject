package com.example.smartkey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowPIN extends AppCompatActivity {

    private Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt0, btCambiar;
    private TextView tvPIN;
    private ImageButton ibx;
    private int contador = 0;
    private int maxnumPIN = 4;
    private String pass = "";
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String checkpass = "";
    private String usermail = "";
    private String newcode = "";
    public final String website = "https://smartkeyproject.000webhostapp.com/";
    private WebView wvSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_pin);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!isConnected()){
            finish();
        }

        if(mAuth.getCurrentUser() == null){
            finish();
        }
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            finish();
        }

        reference = FirebaseDatabase.getInstance().getReference().child("user/"+mAuth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String code = snapshot.child("code").getValue().toString();
                String mail = snapshot.child("mail").getValue().toString();
                if(code != null && mail != null){
                    checkpass = snapshot.child("code").getValue().toString();
                    usermail = snapshot.child("mail").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PIN Error", error.getMessage());
            }
        });

        wvSender = findViewById(R.id.wvLoadMailSender);

        tvPIN = findViewById(R.id.tvPIN);
        tvPIN.setText("");

        bt1 = findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador < maxnumPIN){
                    pass += "1";
                    tvPIN.setText(tvPIN.getText().toString() + " *");
                    contador++;
                }
                if(contador == maxnumPIN){
                    comprobarPIN();
                }
            }
        });

        bt2 = findViewById(R.id.bt2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador < maxnumPIN){
                    pass += "2";
                    tvPIN.setText(tvPIN.getText().toString() + " *");
                    contador++;
                }
                if(contador == maxnumPIN){
                    comprobarPIN();
                }
            }
        });

        bt3 = findViewById(R.id.bt3);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador < maxnumPIN){
                    pass += "3";
                    tvPIN.setText(tvPIN.getText().toString() + " *");
                    contador++;
                }
                if(contador == maxnumPIN){
                    comprobarPIN();
                }
            }
        });

        bt4 = findViewById(R.id.bt4);
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador < maxnumPIN){
                    pass += "4";
                    tvPIN.setText(tvPIN.getText().toString() + " *");
                    contador++;
                }
                if(contador == maxnumPIN){
                    comprobarPIN();
                }
            }
        });

        bt5 = findViewById(R.id.bt5);
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador < maxnumPIN){
                    pass += "5";
                    tvPIN.setText(tvPIN.getText().toString() + " *");
                    contador++;
                }
                if(contador == maxnumPIN){
                    comprobarPIN();
                }
            }
        });

        bt6 = findViewById(R.id.bt6);
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador < maxnumPIN){
                    pass += "6";
                    tvPIN.setText(tvPIN.getText().toString() + " *");
                    contador++;
                }
                if(contador == maxnumPIN){
                    comprobarPIN();
                }
            }
        });

        bt7 = findViewById(R.id.bt7);
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador < maxnumPIN){
                    pass += "7";
                    tvPIN.setText(tvPIN.getText().toString() + " *");
                    contador++;
                }
                if(contador == maxnumPIN){
                    comprobarPIN();
                }
            }
        });

        bt8 = findViewById(R.id.bt8);
        bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador < maxnumPIN){
                    pass += "8";
                    tvPIN.setText(tvPIN.getText().toString() + " *");
                    contador++;
                }
                if(contador == maxnumPIN){
                    comprobarPIN();
                }
            }
        });

        bt9 = findViewById(R.id.bt9);
        bt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador < maxnumPIN){
                    pass += "9";
                    tvPIN.setText(tvPIN.getText().toString() + " *");
                    contador++;
                }
                if(contador == maxnumPIN){
                    comprobarPIN();
                }
            }
        });

        bt0 = findViewById(R.id.bt0);
        bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador < maxnumPIN){
                    pass += "0";
                    tvPIN.setText(tvPIN.getText().toString() + " *");
                    contador++;
                }
                if(contador == maxnumPIN){
                    comprobarPIN();
                }
            }
        });

        ibx = findViewById(R.id.ibx);
        ibx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador > 0){
                    String contenido = tvPIN.getText().toString();
                    tvPIN.setText(contenido.substring(0, contenido.length()-2));
                    contador--;
                }
            }
        });

        btCambiar = findViewById(R.id.btCambiar);
        btCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected() && (mAuth.getCurrentUser() != null )){
                    if(!usermail.isEmpty()){
                        new AlertDialog.Builder(ShowPIN.this)
                                .setTitle(getString(R.string.titulocambiarpin))
                                .setMessage(getString(R.string.mensajecambiarpin))
                                .setPositiveButton(getString(R.string.confirmarcambiarpin), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        newcode = "";
                                        generarcodigo();
                                        cambiarPIN();

                                        String webSender = website+"?co="+usermail+"&c="+newcode;
                                        wvSender.setWebViewClient(new WebViewClient());
                                        wvSender.loadUrl(webSender);
                                    }
                                })
                                .setNegativeButton(getString(R.string.cancelarcambiarpin), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show().setCanceledOnTouchOutside(false);
                    }
                }else{
                    mAuth.signOut();
                    finish();
                }
            }
        });
    }

    private void cambiarPIN() {
        if(isConnected() && (mAuth.getCurrentUser() != null)){
            checkpass = newcode;
            reference.child("code").setValue(newcode);
        }else{
            mAuth.signOut();
            finish();
        }
    }

    private void generarcodigo() {
        for(int i = 0; i<maxnumPIN; i++){
            newcode += (int)(Math.floor(Math.random()*6+1));
        }
    }

    private void comprobarPIN() {
        if(isConnected() && !checkpass.isEmpty() && (mAuth.getCurrentUser() != null)){
            if(pass.equals(checkpass)){
                Intent intent = new Intent(ShowPIN.this, Logged.class);
                startActivity(intent);
            }else{
                pass="";
                new AlertDialog.Builder(ShowPIN.this)
                        .setTitle(getString(R.string.pinincorrecto))
                        .setMessage(getString(R.string.textopinincorrecto))
                        .setPositiveButton(getString(R.string.confirmacionpinincorrecto), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                tvPIN.setText("");
                                contador = 0;
                            }
                        }).show().setCanceledOnTouchOutside(false);
            }
        }else{
            finish();
        }

    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(ShowPIN.this.CONNECTIVITY_SERVICE);
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
