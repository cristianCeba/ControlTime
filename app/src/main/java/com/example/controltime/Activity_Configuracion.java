package com.example.controltime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Activity_Configuracion extends AppCompatActivity {

    TextView correoElectronico;
    EditText nombre;
    Button cambiarContrasena;
    String usuarioAplicacion;
    ClsUser usuario;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__configuracion);

        correoElectronico = findViewById(R.id.TextViewMostrarCorreo);
        nombre = findViewById(R.id.EditTextNombre);
        cambiarContrasena = findViewById(R.id.BtnCambiarContrasena);
        usuarioAplicacion = ClsUser.UsuarioConectadoApp(getApplicationContext()).replace(".", "_").trim();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        Query query = mDataBase.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            /**
             * Buscamos en base de datos si el usuario ha registrado algún fichaje del día, y dependiendo de lo que el usuario ha registrado
             * habilitamos y deshabilitamos los botones de fichajes.
             */
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    ClsUser user = ds.getValue(ClsUser.class);

                    if (ds.getKey().equals(usuarioAplicacion)) {
                        usuario = user;
                        nombre.setText(user.Nombre.toString());
                        correoElectronico.setText(user.correoElectronico.toString());
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("4");
            }
        });

        cambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.sendPasswordResetEmail(correoElectronico.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Activity_Configuracion.this, "El email se ha enviado correctamente.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Activity_Configuracion.this, "El correo no se ha podido enviar correctamente", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_configuracion, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (nombre.getText().toString().isEmpty()) {
            Toast.makeText(Activity_Configuracion.this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
        } else {
            usuario.Nombre = nombre.getText().toString();

            mDataBase.child("users").child(usuarioAplicacion).setValue(usuario);

        }
        return super.onOptionsItemSelected(item);
    }
}

