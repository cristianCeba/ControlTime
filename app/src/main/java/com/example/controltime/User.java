package com.example.controltime;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class User {

    public String correoElectronico;
    public String contraseña;
    public String Nombre;
    public String Ape;
    public String TipoUsuario;
    private String Grupo;
    static Fichaje fichajeUsuario;

    public String getGrupo() {
        return Grupo;
    }

    public void setGrupo(String grupo) {
        Grupo = grupo;
    }

    /*CONSTRUCTOR , INICIALIZAMOS LA CLASE*/
    public User() {
        this.contraseña = "";
        this.Nombre = "";
        this.Ape = "";
        this.correoElectronico = "";
        this.TipoUsuario="";
        this.Grupo ="";
    }

    public User(String coreo, String contraseña) {
        this.correoElectronico = coreo;
        this.contraseña = contraseña;
    }

    /*CONSTRUCTOR DE LA CLASE*/
    public User( String Nombre, String Ape, String correo,String TipoUsuario,String Grupo ) {

        this.Nombre = Nombre;
        this.Ape = Ape;
        this.correoElectronico = correo;
        this.TipoUsuario=TipoUsuario;
        this.Grupo =Grupo;

    }

public static void UsuarioPreferencesApp(String usuario,String contrasena,Context contex){
    SharedPreferences prefe = contex.getSharedPreferences("usuarioApp",Context.MODE_PRIVATE);
    SharedPreferences.Editor editor =prefe.edit();
    editor.putString("usuario",usuario .toString());
    editor.putString("contraseña",contrasena .toString());

    editor.commit();
    }

public static String UsuarioConectadoApp(Context contex){
    SharedPreferences preferencias = contex.getSharedPreferences("usuarioApp", Context.MODE_PRIVATE);
    return preferencias.getString("usuario","No hay usuario conectado");
}

public static String UsuarioContrasenaConectadoApp(Context contex){
    SharedPreferences preferencias = contex.getSharedPreferences("usuarioApp", Context.MODE_PRIVATE);
    return preferencias.getString("contraseña","No hay contraseña");
}

public static void CerrarSesion(Context contex){
    SharedPreferences preferencias = contex.getSharedPreferences("usuarioApp", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor =preferencias.edit();
    editor.putString("usuario",null);
    editor.putString("contraseña",null);

    editor.commit();
}
    public void GrupoUsuario(DatabaseReference mDataBase ,String Usuario){
        List<User> usuario=new ArrayList<>();
        mDataBase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){

                       if(ds.getKey()==Usuario){
                           String correo=ds.getKey();
                           String nombre=ds.child("Nombre").getValue().toString();
                           String apellido=ds.child("Ape").getValue().toString();
                           String grupoUsuario=ds.child("Grupo").getValue().toString();
                           String tipoUsuario=ds.child("TipoUsuario").getValue().toString();
                           setGrupo(grupoUsuario);
                       }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void CargarUsuariosPorGrupo(DatabaseReference mDataBase, Spinner spnUsuario, Context context,String grupo){
        List<User> usuario=new ArrayList<>();
        mDataBase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String correo=ds.getKey();
                        String nombre=ds.child("Nombre").getValue().toString();
                        String apellido=ds.child("Ape").getValue().toString();
                        String grupoUsuario=ds.child("Grupo").getValue().toString();
                        String tipoUsuario=ds.child("TipoUsuario").getValue().toString();
                        if (grupoUsuario==grupo){
                            usuario.add(new User(nombre,apellido,correo,tipoUsuario,grupoUsuario));
                        }

                    }

                    ArrayAdapter<User> arrayAdapter= new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,usuario);
                    spnUsuario.setAdapter(arrayAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

public static void guardarFichajeUsuario (Fichaje fichaje){
        fichajeUsuario = fichaje;
}

public static Fichaje DevolverFichajeUsuario (){
        return fichajeUsuario;
}



}



