package com.example.controltime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClsUser {
    public int usuarioId;
    public String correoElectronico;
    public String contraseña;
    public String Nombre;
    public String Ape;
    public String Ape2;
    public int TipoUsuario;
    public int Grupo;//departamento
    public String idImagen;
    public boolean bloqueado;
    static ClsFichaje fichajeUsuario;
    private ClsUser objUser;
    RequestQueue requestQueue;


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
        this.TipoUsuario=0;
        this.Grupo =0;
        this.usuarioId=0;
        this.idImagen="";
        this.bloqueado=false;
    }

    public ClsUser(String coreo, String contraseña) {
        this.correoElectronico = coreo;
        this.contraseña = contraseña;
    }

    /*CONSTRUCTOR DE LA CLASE*/
    public ClsUser( String Nombre, String Ape, String correo,int TipoUsuario,int Grupo ) {

        this.Nombre = Nombre;
        this.Ape = Ape;
        this.correoElectronico = correo;
        this.TipoUsuario=TipoUsuario;
        this.Grupo =Grupo;

    }

    /*CONSTRUCTOR DE LA CLASE*/
    public ClsUser(int usuarioId, String Nombre, String Ape, String Ape2, String correo,int TipoUsuario,int Grupo,String idImagen,boolean bloqueado ) {

        this.Nombre = Nombre;
        this.Ape = Ape;
        this.Ape2 = Ape2;
        this.correoElectronico = correo;
        this.TipoUsuario=TipoUsuario;
        this.Grupo =Grupo;
        this.usuarioId=usuarioId;
    this.idImagen=idImagen;
        this.bloqueado=bloqueado;
    }

    public static void UsuarioPreferencesApp(String usuario,String contrasena,int usuarioId,Context contex){
        SharedPreferences prefe = contex.getSharedPreferences("usuarioApp",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =prefe.edit();
        editor.putString("usuario",usuario .toString());
        editor.putString("contraseña",contrasena .toString());

        editor.putString("usuarioId", String.valueOf(usuarioId));


        editor.commit();
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
    public static String UsuarioIdApp(Context contex){
        SharedPreferences preferencias = contex.getSharedPreferences("usuarioApp", Context.MODE_PRIVATE);
        return preferencias.getString("usuarioId","No hay ID usuario");
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
                        ClsUser user = ds.getValue(ClsUser.class);
                        Arrayusuario.add(user);
                       /* String correo=ds.getKey();
                        String nombre=ds.child("Nombre").getValue().toString();
                        String apellido=ds.child("Ape").getValue().toString();
                        String grupoUsuario=ds.child("Grupo").getValue().toString();
                        String tipoUsuario=ds.child("TipoUsuario").getValue().toString();
                        Arrayusuario.add(new ClsUser(nombre,apellido,correo,tipoUsuario,grupoUsuario));
                   */
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


    /**METODO QUE CARGA LOS USUARIOS POR GRUPO AL QUE PERTENECE en una SPINNER*/
    public  void ListaUsuariosPorGrupoYTipo(    Context context,String grupoS,String Tipo,Spinner spnUsuarios){
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
                                Arrayusuario.add(user);
                            }
                            ArrayAdapter<ClsUser> arrayAdapter= new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,Arrayusuario);
                            spnUsuarios.setAdapter(arrayAdapter);
                        }

                    }


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**METODO QUE CARGA el USUARIOS POR EMAIL*/
    public  ArrayList<ClsUser> ListaUsuariosXEmail(    Context context,  String Ruta, RequestQueue requestQueue){
        List<ClsUser>    Arrayusuario=new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Ruta,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject = null;
                        for (int i = 0 ;i< response.length(); i++) {
                            try{
                                //[{"0":"1","usuarioId":"1","1":"olga","nombre":"olga","2":"sa","apellido1":"sa","3":
                                // //"her","apellido2":"her","4":"olgasanche@gmail.com","email":"olgasanche@gmail.com",
                                // //"5":"0","bloqueado":"0","6":"0","departamentoId":"0","7":"0","tipoUsuarioId":"0","8":"","imagenId":""}]
                                jsonObject=response.getJSONObject(i);
                                int usuarioId= Integer.parseInt(jsonObject.getString("usuarioId"));
                                String email=jsonObject.getString("email");
                                String nombre=jsonObject.getString("usuarioId");
                                String apellido1=jsonObject.getString("apellido1");
                                String apellido2=jsonObject.getString("apellido2");
                                int tipoUsuarioId= Integer.parseInt(jsonObject.getString("tipoUsuarioId"));
                                int departamentoId= Integer.parseInt(jsonObject.getString("departamentoId"));
                                boolean bloqueado= Boolean.parseBoolean(jsonObject.getString("bloqueado"));
                                String imagenId=jsonObject.getString("imagenId");

                                Arrayusuario.add(new ClsUser(usuarioId,nombre,apellido1,apellido2,email,tipoUsuarioId,departamentoId,imagenId,bloqueado));
                            }catch (JSONException e){
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
        return (ArrayList<ClsUser>) Arrayusuario;
    }







    /**METODO QUE CARGA todos los USUARIOS  EN UN SPINNER*/
    public void ListaUsuarios(Context context,Spinner spnUsuarios ){
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
                            ClsUser user = ds.getValue(ClsUser.class);
                            Arrayusuario.add(user);

                        }

                    }
                    ArrayAdapter<ClsUser> arrayAdapter= new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,Arrayusuario);
                    spnUsuarios.setAdapter(arrayAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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



