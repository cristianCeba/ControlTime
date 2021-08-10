package com.example.controltime;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class User {

    public String correoElectronico;
    public String contraseña;
    public String Nombre;
    public String Ape;



    DatabaseReference mDataBase;
    FirebaseAuth mAuth;
    boolean existe;
    boolean Registrado ;

    public boolean isExiste() {
        return existe;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
    }

    public boolean isRegistrado() {
        return Registrado;
    }

    public void setRegistrado(boolean registrado) {
        Registrado = registrado;
    }
    /*CONSTRUCTOR , INICIALIZAMOS LA CLASE*/
    public User() {
        this.contraseña = "";
        this.Nombre = "";
        this.Ape = "";
        this.correoElectronico = "";


    }

    public User(String coreo, String contraseña) {
        this.correoElectronico = coreo;
        this.contraseña = contraseña;
    }

    /*CONSTRUCTOR DE LA CLASE*/
    public User( String Nombre, String Ape, String correo ) {

        this.contraseña = contraseña;
        this.Nombre = Nombre;
        this.Ape = Ape;
        this.correoElectronico = correo;


    }

    /***
     * METODO PARA INSERTAR EL USUARIO EN LA BASE DE DATOS(USERS Y AUTHENTICATION)
     * @param context
     * @param contraseña
     * @param correo
     * @return
     */

    public void RegistrarUsuario(Context context, String contraseña, String correo) {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();// REFERENCIA A LA bbdd
        //INSERTO EL USUARIO EN AUTHENTICATION
        mAuth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Utils.MostrarMensajes(context, "USUARIO AUTHENTICATION GRABADO CORRECTAMENTE", "REGISTRO");
                            return;
                        }
                        else {
                            Utils.MostrarMensajes(context, "NO SE HA PODIDO GRABAR EL USUARIO EN AUTENTHICATION", "REGISTRO");
                        }
                    }
                });

    }

    public void GrabaUsuario(Context context , String nombre,
                             String ape, String correo){

        Utils.MostrarMensajes(context, "ENTRA", "GRABA USUARIO");
        User use = new User(nombre, ape, correo );
        String email = correo.replace(".", "_");
        mDataBase.child("users").child(email).setValue(use).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if (task2.isSuccessful()) {
                    Utils.MostrarMensajes(context, "USUARIO GRABADO CORRECTAMENTE", "GRABA USUARIO");
                } else {
                    setRegistrado(false);
                    Utils.MostrarMensajes(context, "NO SE HA PODIDO GRABAR EL USUARIO ", "GRABA USUARIO");
                }
            }
        });

    }

    /***
     * METODO QUE BUSCA SI EXISTE EL CORREO EL LA BASE DE DATOS USERS
     * @param usuario CORREO
     * @return DEVUELVE TRUE O FALSE
     */
    public void    BuscaUsuario(Context context,String usuario) {

        mDataBase = FirebaseDatabase.getInstance().getReference();

        Query query=mDataBase.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){

                        Query query1=mDataBase.child("users").child(usuario);
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull   DataSnapshot snapshot2) {
                                if(snapshot2.exists()){
                                    Utils.MostrarMensajes(context, "EL USUARIO EXISTE" + isExiste(), " USUARIO");
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }



    /***
     * METODO QUE COMPRUEBA SI ESTA UN USUARIO EN LA BASE DE DATOS (AUTHENTICATION)
     * @param context
     * @param contraseña CONTRASEÑA
     * @param correo CORREO
     * @return DEVUELVE TRUE O FALSE
     */
    public boolean BuscaUsuarioAutentication(Context context, String contraseña, String correo) {
        existe = false;
        mAuth.signInWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())      existe =true;
                    }
                });
        return existe;
    }
}

