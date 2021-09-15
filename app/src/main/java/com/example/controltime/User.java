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


}

