package com.example.smartkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class CodeVerfication extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String phonenumber;
    private String username;
    private String useremail;
    private EditText etCode;
    private Button btVerify;
    private Button btBack;
    private DatabaseReference reference;
    private String mVerificationId;
    private final String code = "pinscreen";
    private String codepreviousscreen;
    private static final String ARCHIVO = "SmartKeyPreferences";
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onStart() {
        super.onStart();
        if(!isConnected()){
            finish();
        }

        if(mAuth.getCurrentUser() != null){
            finish();
        }else{
            mAuth.addAuthStateListener(mAuthListener);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_code_verification);
        init();
    }

    private void init() {
        SharedPreferences sharedPreferences = getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
        phonenumber = sharedPreferences.getString(Register.TELEFONO_PREFERENCES, "no encontramos el nombre");
        username = sharedPreferences.getString(Username.NAME_PREFERENCES, "no encontramos la direccion");
        useremail = sharedPreferences.getString(Email.EMAIL_PREFERENCES, "no encontramos el correo");
        codepreviousscreen = getIntent().getStringExtra(code);

        if(phonenumber == null || username == null || useremail == null || codepreviousscreen == null){
            Toast.makeText(CodeVerfication.this, getString(R.string.faltandatos), Toast.LENGTH_LONG).show();
            finish();
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    requestCode();
                }
            }
        };

        etCode = findViewById(R.id.etCodigo);

        btVerify = findViewById(R.id.btVerificar);
        btVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodeVerfication.this.signIn(v);
            }
        });

        btBack = findViewById(R.id.btVolver);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void requestCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber, 60, TimeUnit.SECONDS, CodeVerfication.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(CodeVerfication.this, getString(R.string.verificationinvalid), Toast.LENGTH_LONG).show();
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(CodeVerfication.this, getString(R.string.verificationquota), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(CodeVerfication.this, getString(R.string.verificationFail), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        //super.onCodeSent(verificationId, forceResendingToken);
                        Toast.makeText(CodeVerfication.this, "6", Toast.LENGTH_LONG).show();

                        mVerificationId = verificationId;
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String verificationId) {
                        super.onCodeAutoRetrievalTimeOut(verificationId);
                        //Este método se llama después de que haya pasado el tiempo de espera
                        //especificado en verifyPhoneNumber sin que se active onVerificationCompleted primero
                    }
                }
        );
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(CodeVerfication.this, getString(R.string.credentialfail), Toast.LENGTH_LONG).show();
                        }else{
                            reference = FirebaseDatabase.getInstance().getReference();

                            reference.child("user/"+mAuth.getCurrentUser().getUid()).child("phone").setValue(phonenumber);
                            reference.child("user/"+mAuth.getCurrentUser().getUid()).child("code").setValue(codepreviousscreen);
                            reference.child("user/"+mAuth.getCurrentUser().getUid()).child("name").setValue(username);
                            reference.child("user/"+mAuth.getCurrentUser().getUid()).child("mail").setValue(useremail);

                            reference.child("phones/"+phonenumber).setValue("completed");

                            SharedPreferences sharedPreferences = getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove(Register.TELEFONO_PREFERENCES);
                            editor.remove(Username.NAME_PREFERENCES);
                            editor.remove(Email.EMAIL_PREFERENCES);
                            editor.commit();

                            mAuth.addAuthStateListener(null);
                        }
                    }
                });
    }

    public void signIn(View view) {
        String code = etCode.getText().toString();
        if (TextUtils.isEmpty(code))
            return;

        signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId, code));
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(CodeVerfication.this.CONNECTIVITY_SERVICE);
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