package com.example.controltime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    public Button btnReset,btnVolver;
    public EditText email;
    public TextView errorEmail;
    private FirebaseAuth mAuth;
    String emailEscrito;
    Utils utils;

 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        utils = new Utils();
        btnReset = findViewById(R.id.btnReset);
        btnVolver = findViewById(R.id.btnVolver);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.editTextEmail);
        errorEmail = findViewById(R.id.textErrorEmail);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorEmail.setText("");
                emailEscrito = email.getText().toString();
                if (emailEscrito.isEmpty()){
                    errorEmail.setText("El email no puede estar vacío");
                } else if(!utils.validarEmail(emailEscrito)){
                    errorEmail.setText("Introduzca un email correcto");
                } else{
                    mAuth.sendPasswordResetEmail(emailEscrito).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                final CharSequence[] opciones = {"Hemos enviado un correo a su correo electronico"};
                                final AlertDialog.Builder alertInfo = new AlertDialog.Builder(ResetPasswordActivity.this);

                                alertInfo.setTitle("Advertencia");

                                alertInfo.setItems(opciones, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                alertInfo.show();
                            } else{
                                Toast.makeText(ResetPasswordActivity.this,"El correo no se ha podido enviar correctamente",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}