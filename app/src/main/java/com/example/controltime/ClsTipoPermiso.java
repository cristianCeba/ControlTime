package com.example.controltime;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClsTipoPermiso {
    int id;
    String Tipo;

    public ClsTipoPermiso(int id, String tipoPermisos) {
        this.id=id;
        this.Tipo=tipoPermisos;
    }

    public ClsTipoPermiso() {
        this.id=0;
        this.Tipo="tipoPermisos";
    }

    public int getId() {
        return id;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    @Override
    public String toString() {
        return  Tipo ;
    }


    public static ArrayList<ClsTipoPermiso> getPermisos ( ){
        List<ClsTipoPermiso> objPer = new ArrayList<>() ;

        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("Select * from ct_tipopermisos");
            while (rs.next()) {
              int id= Integer.parseInt(rs.getString("tipoPermisosId"));
              String descripcion=rs.getString("descripcion");
                objPer.add(new ClsTipoPermiso( id,descripcion));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ClsTipoPermiso>) objPer;
    }

   /* public void CargarTipoPermisos(DatabaseReference mDataBase, Spinner spnTipoPermisos, Context context){
     final   List<ClsTipoPermiso> tipoPermisos=new ArrayList<>();
     mDataBase.child("TipoPermisos").addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull   DataSnapshot snapshot) {
             if (snapshot.exists()){
                 for (DataSnapshot ds:snapshot.getChildren()){
                     String id =ds.getKey();
                     String tipoPer=ds.child("tipo").getValue().toString();
                     tipoPermisos.add(new ClsTipoPermiso(id,tipoPer));
                 }
                 ArrayAdapter<ClsTipoPermiso> adapter=new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,tipoPermisos);
                 spnTipoPermisos.setAdapter(adapter);
             }
         }

         @Override
         public void onCancelled(@NonNull   DatabaseError error) {

         }
     });

    }

    public static void GetNombrePermisoXId(DatabaseReference mDataBase, TextView Nombre, String Id){
        mDataBase.child("TipoPermisos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        if(ds.getKey().equals(Id)){
                            Nombre.setText(ds.child("Tipo").getValue().toString());
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull   DatabaseError error) {
            }
        });
    }*/

}
