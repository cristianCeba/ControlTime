package com.example.controltime;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

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
    public String TipoUsuario;
    public String Grupo;
    static Fichaje fichajeUsuario;

    /*CONSTRUCTOR , INICIALIZAMOS LA CLASE*/
    public User() {
        this.contraseña = "";
        this.Nombre = "";
        this.Ape = "";
        this.correoElectronico = "";
        this.TipoUsuario="";
        this.Grupo="";
    }

    public User(String coreo, String contraseña) {
        this.correoElectronico = coreo;
        this.contraseña = contraseña;
    }

    /*CONSTRUCTOR DE LA CLASE*/
    public User( String Nombre, String Ape, String correo,String TipoUsuario,String Grupo ) {

        this.contraseña = contraseña;
        this.Nombre = Nombre;
        this.Ape = Ape;
        this.correoElectronico = correo;
        this.TipoUsuario=TipoUsuario;
        this.Grupo=Grupo;


    }
public static void UsuarioPreferencesApp(String usuario,Context contex){
    SharedPreferences prefe = contex.getSharedPreferences("usuarioApp",Context.MODE_PRIVATE);
    SharedPreferences.Editor editor =prefe.edit();
    editor.putString("usuario",usuario .toString());

    editor.commit();
    }

public static String UsuarioConectadoApp(Context contex){
    SharedPreferences preferencias = contex.getSharedPreferences("usuarioApp", Context.MODE_PRIVATE);
    return preferencias.getString("usuario","No hay usuario conectado");
}

public static void guardarFichajeUsuario (Fichaje fichaje){
        fichajeUsuario = fichaje;
}

public static Fichaje DevolverFichajeUsuario (){
        return fichajeUsuario;
}

}

