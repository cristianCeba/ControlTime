package com.example.controltime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
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
    public TextView textCorreo;
    public TextView textContraseña;
    private FirebaseAuth mAuth;
    public String correo;
    public String contraseña;
    public Button btnInsertarUser;

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

        btnInsertarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),InsertUserActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo = editTextCorreo.getText().toString();
                contraseña = editTextContraseña.getText().toString();
                if (correo.isEmpty()){
                    textContraseña.setText("");
                    textCorreo.setText("Por favor, introduce un email");
                } else if (!validarEmail(correo)) {
                    textContraseña.setText("");
                    textCorreo.setText("Introduzca un email correcto");
                }else if (contraseña.isEmpty()) {
                    textCorreo.setText("");
                    textContraseña.setText("Por favor, introduce una contraseña");
                } else if (validarContraseña(contraseña)){
                    textCorreo.setText("");
                    textContraseña.setText("Contraseña incorrecta");
                }else{
                    textContraseña.setText("");
                    textCorreo.setText("");
                    RevisarLogin();
                }

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
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean validarContraseña (String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        return password.matches(pattern);
    }
}