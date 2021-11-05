package com.example.controltime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Activity_MenuPrincipal extends AppCompatActivity {

    Button btnInforme, btnFichaje,btnPermiso,btnVerFichaje,btnCambiarHorario,btnConfiguración,btnCerrarSesion,btnValidar;
  //  EditText usuario;
    TextView edtGrupoApp;
    TextView edtTipoUsuarioApp;
    TextView edtUsuarioApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        getSupportActionBar().hide();// quito la barra de arriba

/*** * MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/
        edtGrupoApp=(TextView) findViewById(R.id.edtGrupoApp);
        edtTipoUsuarioApp=(TextView)findViewById(R.id.edtTipoUsuarioApp);
        edtUsuarioApp=(TextView) findViewById(R.id.edtUsuarioApp);
        edtUsuarioApp.setText(ClsUser.UsuarioConectadoApp(getApplication()));

        edtTipoUsuarioApp.setText("TIPO: " + ClsUser.TipoUsuarioConectadoApp(getApplication()));
        edtGrupoApp.setText("DEPARTAMENTO: " + ClsUser.GruposuarioConectadoApp(getApplication()));
        /***FIN MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/
        btnPermiso=(Button) findViewById(R.id.btnPermiso);
        btnVerFichaje=(Button) findViewById(R.id.btnVerFichaje);
        btnCambiarHorario = findViewById(R.id.btnSolicitarCambio);
        btnConfiguración = findViewById(R.id.btnConfiguracion);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnInforme=findViewById(R.id.btnInforme);
        btnValidar = findViewById(R.id.btnValidar);
        btnInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Activity_Informe.class);
                startActivity(intent);
            }
        });

        btnPermiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_Permiso.class);
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
                Intent intent = new Intent(getApplicationContext(), Activity_VerFichaje.class);
                startActivity(intent);
            }
        });

        btnCambiarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Activity_ModificarFichaje.class);
                startActivity(intent);
            }
        });

        btnConfiguración.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Activity_Configuracion.class);
                startActivity(intent);
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClsUser.CerrarSesion(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), Activity_Login.class);
                startActivity(intent);
            }
        });

        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_Validar.class);
                startActivity(intent);
            }
        });
    }
}