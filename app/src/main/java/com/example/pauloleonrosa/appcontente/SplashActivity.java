package com.example.pauloleonrosa.appcontente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by pauloleonrosa on 14/11/16.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}