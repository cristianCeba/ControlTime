package com.example.controltime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MenuPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        getSupportActionBar().hide();
    }
}