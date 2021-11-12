package com.example.controltime;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClsUser {

    public String correoElectronico;
    public String contraseña;
    public String Nombre;
    public String Ape;
    public String TipoUsuario;
    public String Grupo;
    public String idImagen;
    static ClsFichaje fichajeUsuario;
    private ClsUser objUser;



    @Override
    public String toString() {
        return   correoElectronico  ;
    }

    /*CONSTRUCTOR , INICIALIZAMOS LA CLASE*/
    public ClsUser() {
        this.contraseña = "";
        this.Nombre = "";
        this.Ape = "";
        this.correoElectronico = "";
        this.TipoUsuario="";
        this.Grupo ="";
    }

    public ClsUser(String coreo, String contraseña) {
        this.correoElectronico = coreo;
        this.contraseña = contraseña;
    }

    /*CONSTRUCTOR DE LA CLASE*/
    public ClsUser( String Nombre, String Ape, String correo,String TipoUsuario,String Grupo ) {

        this.Nombre = Nombre;
        this.Ape = Ape;
        this.correoElectronico = correo;
        this.TipoUsuario=TipoUsuario;
        this.Grupo =Grupo;

    }

public static void UsuarioPreferencesApp(String usuario,String contrasena,String tipo,String grupo,Context contex){
    SharedPreferences prefe = contex.getSharedPreferences("usuarioApp",Context.MODE_PRIVATE);
    SharedPreferences.Editor editor =prefe.edit();
    editor.putString("usuario",usuario .toString());
    editor.putString("contraseña",contrasena .toString());


   editor.putString("Tipousuario",tipo .toString());
    editor.putString("Grupo",grupo .toString());

    editor.commit();
    }
    public static String TipoUsuarioConectadoApp(Context contex){
        SharedPreferences preferencias = contex.getSharedPreferences("usuarioApp", Context.MODE_PRIVATE);
        return preferencias.getString("Tipousuario","No hay Tipo usuario");
    }

    public static String GruposuarioConectadoApp(Context contex){
        SharedPreferences preferencias = contex.getSharedPreferences("usuarioApp", Context.MODE_PRIVATE);
        return preferencias.getString("Grupo","No hay Grupo usuario");
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

    /**METODO QUE CARGA todos los USUARIOS */
public ArrayList<ClsUser> ListaUsuarios(Context context ){
    List<ClsUser>    Arrayusuario=new ArrayList<>();
    DatabaseReference mDataBase;
    mDataBase = FirebaseDatabase.getInstance().getReference();
    mDataBase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.getKey().equals(ClsUser.UsuarioConectadoApp(context).replace(".", "_").trim())){
                        //Toast.makeText(context,ds.getKey(),Toast.LENGTH_LONG).show();
                    }else{
                        String correo=ds.getKey();
                        String nombre=ds.child("Nombre").getValue().toString();
                        String apellido=ds.child("Ape").getValue().toString();
                        String grupoUsuario=ds.child("Grupo").getValue().toString();
                        String tipoUsuario=ds.child("TipoUsuario").getValue().toString();
                        Arrayusuario.add(new ClsUser(nombre,apellido,correo,tipoUsuario,grupoUsuario));
                    }
               }
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    return (ArrayList<ClsUser>) Arrayusuario;
}

    /**METODO QUE CARGA LOS USUARIOS POR GRUPO AL QUE PERTENECE*/
    public ArrayList<ClsUser> ListaUsuariosPorGrupoYTipo(    Context context,String grupoS,String Tipo){
        List<ClsUser>    Arrayusuario=new ArrayList<>();
      //  usuario=new ArrayList<>();
        DatabaseReference mDataBase;
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        if(ds.child("Grupo").getValue().toString().equals(grupoS) && ds.child("TipoUsuario").getValue().toString().equals(Tipo)){
                            {
                                ClsUser user = ds.getValue(ClsUser.class);
                                /*
                                String correo=ds.getKey();
                                String nombre=ds.child("Nombre").getValue().toString();
                                String apellido=ds.child("Ape").getValue().toString();
                                String grupoUsuario=ds.child("Grupo").getValue().toString();
                                String tipoUsuario=ds.child("TipoUsuario").getValue().toString();
                                Arrayusuario.add(new ClsUser(nombre,apellido,correo,tipoUsuario,grupoUsuario));*/
                                Arrayusuario.add(user);
                            }
                        }

                    }


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return (ArrayList<ClsUser>) Arrayusuario;
    }

    public String[] GetNombreYApellido(DatabaseReference mDataBase,   String UsuarioApp){
        String[] Nombre = {null};
        mDataBase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        if(ds.getKey().equals(UsuarioApp)){
                            Nombre[0] =ds.child("Nombre").getValue().toString() + " " + ds.child("Ape").getValue().toString();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull   DatabaseError error) {
            }
        });
        return  Nombre;
    }



public static void guardarFichajeUsuario (ClsFichaje fichaje){
        fichajeUsuario = fichaje;
}

public static ClsFichaje DevolverFichajeUsuario (){
        return fichajeUsuario;
}



}



