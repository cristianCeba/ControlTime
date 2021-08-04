package com.example.controltime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertUserActivity extends AppCompatActivity {
EditText txtNombre;
EditText txtApe1;
EditText txtEmail;
EditText txtMovil;
EditText txtPass;
TextView txtUsuario;
Button btnInsert;
Conexion conexion;

private DatabaseReference mDataBase;
 private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_user);
        txtNombre = (EditText) findViewById(R.id.editTextName);
        txtApe1 = (EditText) findViewById(R.id.editTextApe);
        txtEmail = (EditText) findViewById(R.id.editTextEmail);
        txtMovil = (EditText) findViewById(R.id.editTextMovil);
        txtPass = (EditText) findViewById(R.id.editTextPass);
        txtUsuario = (TextView) findViewById(R.id.textUsuario);
        btnInsert = (Button) findViewById(R.id.btnRegistro);
        mDataBase = FirebaseDatabase.getInstance().getReference();

         txtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                txtUsuario.setText(txtNombre.getText() + "." + txtApe1.getText());
            }
        });


    }


    public void Inserta(View view) {
        String usuario="" + txtNombre.getText() + "." + txtApe1.getText();
        Toast.makeText(this, "USUARIO: " + usuario, Toast.LENGTH_SHORT).show();

        User use = new User  (txtPass.getText().toString(),txtNombre.getText().toString(),txtApe1.getText().toString(),txtEmail.getText().toString(),txtMovil.getText().toString());
       // mDataBase.child("users").child(usuario).setValue(use);
         mDataBase.child("users").child("ggggg").setValue(use);



    }
}