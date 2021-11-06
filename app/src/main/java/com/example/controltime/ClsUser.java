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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClsUser {

    public String correoElectronico;
    public String contraseña;
    public String Nombre;
    public String Ape;
    public String TipoUsuario;
    private String Grupo;
    static ClsFichaje fichajeUsuario;
    private ClsUser objUser;


    public String getGrupo() {
        return Grupo;
    }

    public void setGrupo(String grupo) {
        Grupo = grupo;
    }

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

  //  ClsUser.CargarUsuario(usuario.replace(".", "_").trim(),contex);
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
/*
public static void CargarUsuario(String Usuario, Context context){
    List<ClsUser> usuario=new ArrayList<>();
    DatabaseReference mDataBase;
    mDataBase = FirebaseDatabase.getInstance().getReference();
    mDataBase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                for(DataSnapshot ds: snapshot.getChildren()){
                    if (ds.getKey().equals(Usuario)){
                        String correo=ds.getKey();
                        String nombre=ds.child("Nombre").getValue().toString();
                        String apellido=ds.child("Ape").getValue().toString();
                        String grupoUsuario=ds.child("Grupo").getValue().toString();
                        String tipoUsuario=ds.child("TipoUsuario").getValue().toString();
                        usuario.add(new ClsUser(nombre,apellido,correo,tipoUsuario,grupoUsuario));
                    }
                }
               for(int i=0;i<=usuario.size()-1;i++){
                   Toast.makeText(context,"GRUPO: " + usuario.get(i).Grupo + "  TIPO: " +  usuario.get(i).TipoUsuario,Toast.LENGTH_LONG).show();
                   SharedPreferences prefe = context.getSharedPreferences("usuarioApp",Context.MODE_PRIVATE);
                   SharedPreferences.Editor editor =prefe.edit();

                   editor.putString("Tipousuario",usuario.get(i).TipoUsuario .toString());
                   editor.putString("Grupo",usuario.get(i).Grupo .toString());
               }



            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}*/


    public void CargarUsuariosPorGrupo( Spinner spnUsuario, Context context,String grupoS){
        List<ClsUser> usuario=new ArrayList<>();
        DatabaseReference mDataBase;
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        if (ds.child("Grupo").getValue().toString().equals(grupoS)){
                            String correo=ds.getKey();
                            String nombre=ds.child("Nombre").getValue().toString();
                            String apellido=ds.child("Ape").getValue().toString();
                            String grupoUsuario=ds.child("Grupo").getValue().toString();
                            String tipoUsuario=ds.child("TipoUsuario").getValue().toString();
                            usuario.add(new ClsUser(nombre,apellido,correo,tipoUsuario,grupoUsuario));
                        }
                    }
                    ArrayAdapter<ClsUser> arrayAdapter= new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,usuario);
                    spnUsuario.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
public static void guardarFichajeUsuario (ClsFichaje fichaje){
        fichajeUsuario = fichaje;
}

public static ClsFichaje DevolverFichajeUsuario (){
        return fichajeUsuario;
}



}



