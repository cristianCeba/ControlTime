package com.example.controltime;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClsFragmentoConfiguracion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClsFragmentoConfiguracion extends Fragment {

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClsFragmentoConfiguracion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClsFragmentoConfiguracion.
     */
    // TODO: Rename and change types and number of parameters
    public static ClsFragmentoConfiguracion newInstance(String param1, String param2) {
        ClsFragmentoConfiguracion fragment = new ClsFragmentoConfiguracion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cls_fragmento_configuracion, container, false);
        correoElectronico = view.findViewById(R.id.TextViewMostrarCorreo);
        nombre = view.findViewById(R.id.EditTextNombre);
        cambiarContrasena = view.findViewById(R.id.BtnCambiarContrasena);
        imagen = view.findViewById(R.id.imagenUsuario);
        usuarioAplicacion = ClsUser.UsuarioConectadoApp(getContext()).replace(".", "_").trim();
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
                            Toast.makeText(getContext(), "El email se ha enviado correctamente.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "El correo no se ha podido enviar correctamente", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Cambiamos el nombre del usuario
        if (nombre.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri rui = data.getData();
        StorageReference filePath = storage.child("imagenes").child(ClsUser.UsuarioConectadoApp(getContext())).child(rui.getLastPathSegment());

        filePath.putFile(rui).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                usuario.idImagen = rui.getLastPathSegment();
                mDataBase.child("users").child(usuarioAplicacion).setValue(usuario);
                Toast.makeText(getContext(),"Imagen subida correctamente",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Recuperamos la imagen del usuario para mostrarla en la aplicación.
     */
    public void recuperarImagen () {
        idImagen = usuario.idImagen;
        if (idImagen != null){

            String usuario1 = ClsUser.UsuarioConectadoApp(getContext());
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