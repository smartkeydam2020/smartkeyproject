package com.example.smartkey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.example.smartkey.view.UserLoggedGridAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class Logged extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private UserLoggedGridAdapter recyclewViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_logged);
        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        if(!isConnected() || (mAuth.getCurrentUser() == null)){
            finish();
        }

        recyclewViewAdapter = new UserLoggedGridAdapter(Logged.this);
        recyclerView = findViewById(R.id.rvUserLogged);
        recyclerView.setLayoutManager(new GridLayoutManager(Logged.this, 2));
        recyclerView.setAdapter(recyclewViewAdapter);
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Logged.this.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            mAuth.signOut();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}