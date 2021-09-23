package com.example.controltime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    public Button btnIniciarSesion;
    public EditText editTextContraseña;
    public EditText editTextCorreo;
    public ImageButton btnInfo;
    public TextView textCorreo,textContraseña,resetPassword;
    private FirebaseAuth mAuth;
    public String correo;
    public String contraseña;
    public Button btnInsertarUser;
    Utils utils;


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
        utils = new Utils();

        mAuth = FirebaseAuth.getInstance();

        btnInsertarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),InsertUserActivity.class);
                startActivity(intent);
            }
        });

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
                final AlertDialog.Builder alertInfo = new AlertDialog.Builder(LoginActivity.this);

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
        mAuth.signInWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Usuario encontrado",
                                    Toast.LENGTH_SHORT).show();
                            User.UsuarioPreferencesApp(correo,getApplicationContext());
                            Intent intent = new Intent(getApplicationContext(),MenuPrincipalActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            final CharSequence[] opciones = {"Ususario o contraseña invalida"};
                            final AlertDialog.Builder alertInfo = new AlertDialog.Builder(LoginActivity.this);

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
        Intent intent = new Intent(getApplicationContext(),ResetPasswordActivity.class);
        startActivity(intent);
    }
}