package com.example.controltime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class MenuPrincipalActivity extends AppCompatActivity {

    ImageButton btnFichaje;
    ImageButton btnPermiso;
    ImageButton btnVerFichaje;
    EditText usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        getSupportActionBar().hide();// quito la barra de arriba
        usuario=(EditText)findViewById(R.id.edtUsuarioApp);

        usuario.setText(User.UsuarioConectadoApp(getApplicationContext()));
        btnPermiso=(ImageButton) findViewById(R.id.btnPermiso);
        btnVerFichaje=(ImageButton) findViewById(R.id.btnVerFichaje);
        btnPermiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PermisoActivity.class);
                startActivity(intent);
            }
        });


        btnFichaje = findViewById(R.id.btnFichaje);

        btnFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Activity_fichaje.class);
                startActivity(intent);
            }
        });

        btnVerFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VerFichajeActivity.class);
                startActivity(intent);
            }
        });
    }
}