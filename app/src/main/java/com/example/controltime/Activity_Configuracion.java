package com.example.controltime;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;

public class Activity_Configuracion extends AppCompatActivity {

    TextView correoElectronico;
    EditText nombre;
    Button cambiarContrasena;
    StorageReference storage;
    String usuarioAplicacion, correo, idImagen;
    ClsUser usuario;
    ImageView imagen;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private static final int PICK_PDF_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__configuracion);

        correoElectronico = findViewById(R.id.TextViewMostrarCorreo);
        nombre = findViewById(R.id.EditTextNombre);
        cambiarContrasena = findViewById(R.id.BtnCambiarContrasena);
        imagen = findViewById(R.id.imagenUsuario);
        usuarioAplicacion = ClsUser.UsuarioConectadoApp(getApplicationContext()).replace(".", "_").trim();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri rui = null;
                openDirectory();
            }
        });

        // Guardamos en la variable usuario el usuario de la aplicación
        Query query = mDataBase.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            /**
             * Buscamos en base de datos al usuario.
             */
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    ClsUser user = ds.getValue(ClsUser.class);
                    if (ds.getKey().equals(usuarioAplicacion)) {
                        usuario = user;
                        recuperarImagen();
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
        // Botón cambiar contraseña
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
        //Cambiamos el nombre del usuario
        if (nombre.getText().toString().isEmpty()) {
            Toast.makeText(Activity_Configuracion.this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
        } else {
            usuario.Nombre = nombre.getText().toString();

            mDataBase.child("users").child(usuarioAplicacion).setValue(usuario);

        }
        return super.onOptionsItemSelected(item);
    }
    // Abrimos el directorio de imagenes
    public void openDirectory() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PDF_FILE);
    }

    @Override // Subimos la imagen a firebase y guardamos el id de la imagen en la base de datos.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri rui = data.getData();
        StorageReference filePath = storage.child("imagenes").child(ClsUser.UsuarioConectadoApp(getApplication())).child(rui.getLastPathSegment());

        filePath.putFile(rui).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                usuario.idImagen = rui.getLastPathSegment();
                mDataBase.child("users").child(usuarioAplicacion).setValue(usuario);
                Toast.makeText(getApplicationContext(),"Imagen subida correctamente",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Recuperamos la imagen del usuario para mostrarla en la aplicación.
     */
    public void recuperarImagen () {
        idImagen = usuario.idImagen;
        if (idImagen != null){

            String usuario1 = ClsUser.UsuarioConectadoApp(getApplicationContext());
            StorageReference pathReference = storage.child("imagenes").child(usuario1).child(idImagen);

            pathReference.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    imagen.setImageBitmap(bitMap);
                }
            });

        }

    }

}

