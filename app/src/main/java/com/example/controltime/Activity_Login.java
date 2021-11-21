package com.example.controltime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Activity_Login extends AppCompatActivity {

    public Button btnIniciarSesion;
    public EditText editTextContraseña;
    public EditText editTextCorreo;
    public ImageButton btnInfo;
    public TextView textCorreo,textContraseña,resetPassword;
    private FirebaseAuth mAuth;
    public String correo;
    public String contraseña;
    public Button btnInsertarUser;
    RequestQueue requestQueue;
    ClsUtils utils;
    ClsUser usuario=new ClsUser();
  //  private static  final String URL_RECUPERAR_DATOS_SOCIO_EMAIL="https://us-central1-controltime-b575f.cloudfunctions.net/GetUsuarioXEmail?email=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btnInsertarUser = findViewById(R.id.btnRegistro);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        editTextContraseña = findViewById(R.id.editTextTextPassword);
        editTextCorreo = findViewById(R.id.editTextTextCorreo);
        textContraseña = findViewById(R.id.textViewContraseña);
        textCorreo = findViewById(R.id.textViewCorreo);
        btnInfo = findViewById(R.id.btnInfo);
        resetPassword = findViewById(R.id.textResetPassword);
        utils = new ClsUtils();

       mAuth = FirebaseAuth.getInstance();
        sesionGuardada();



        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textContraseña.setText("");
                textCorreo.setText("");
                correo = editTextCorreo.getText().toString();
                contraseña = editTextContraseña.getText().toString();
                if (correo.isEmpty()){
                    textCorreo.setText("Por favor, introduce un email");
                } else if (!utils.validarEmail(correo)) {
                    textCorreo.setText("Introduzca un email correcto");
                }else if (contraseña.isEmpty()) {
                    textContraseña.setText("Por favor, introduce una contraseña");
                }else{
                    RevisarLogin();



                }

            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] opciones = {"\n1. Un caracter en minúscula","\n2. Un caracter en mayúscula","\n3. Un caracter especial","\n4. Un número","\n5. Sin espacios entre los caracteres de la contraseña","\n6. Mínimo 8 caracteres"};
                final AlertDialog.Builder alertInfo = new AlertDialog.Builder(Activity_Login.this);

                alertInfo.setTitle("La contraseña debe de contener al menos : ");

                alertInfo.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertInfo.show();
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetearContraseña();
            }
        });


    }

    public void RevisarLogin (){
        DatabaseReference nDataBase;
        mAuth.signInWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // cargar objusuario
                            List<ClsUser>  Arrayusuario=new ArrayList<>();
                          //  URL_RECUPERAR_DATOS_SOCIO_EMAIL--------------------------------------

                           /* String Ruta =URL_RECUPERAR_DATOS_SOCIO_EMAIL  + correo;
                            Arrayusuario=objUse.ListaUsuariosXEmail(Activity_Login.this,Ruta,requestQueue);
                            for(int i=0;i<=Arrayusuario.size()-1;i++){
                                ClsUser.UsuarioPreferencesApp(correo,contraseña,Arrayusuario.get(i).usuarioId,getApplicationContext());
                            }*/

                            buscarUsuario(correo);
                            ClsUser.UsuarioPreferencesApp(correo,contraseña,usuario.usuarioId,getApplicationContext());
                            Intent intent = new Intent(getApplicationContext(), Activity_Navegador.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            final CharSequence[] opciones = {"Ususario o contraseña invalida"};
                            final AlertDialog.Builder alertInfo = new AlertDialog.Builder(Activity_Login.this);

                            alertInfo.setTitle("Advertencia");

                            alertInfo.setItems(opciones, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertInfo.show();
                        }
                    }
                });
    }

    private void resetearContraseña () {
        Intent intent = new Intent(getApplicationContext(), Activity_ResetPassword.class);
        startActivity(intent);
    }

    private void sesionGuardada (){
        String correo = ClsUser.UsuarioConectadoApp(getApplicationContext());
        String contrasena = ClsUser.UsuarioContrasenaConectadoApp(getApplicationContext());

        mAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                           Intent intent = new Intent(getApplicationContext(), Activity_Navegador.class);
                           startActivity(intent);
                        }
                    }
                });
    }
    public void buscarUsuario (String correo){
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()){
                    usuario = ClsUser.getUsuario(correo);
                    DbConnection.cerrarConexion();
                }

            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}