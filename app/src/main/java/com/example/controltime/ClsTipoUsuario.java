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

import java.util.ArrayList;
import java.util.List;

public class ClsTipoUsuario {
    String id;
    String Tipo;

    public ClsTipoUsuario(String id, String tipo) {
        this.id=id;
        this.Tipo=tipo;

    }
    public ClsTipoUsuario() {
        this.id="";
        this.Tipo="";

    }
    public String getId() {
        return id;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    @Override
    public String toString() {
        return  Tipo ;
    }

   /* public void GetTipoXId(DatabaseReference mDataBase, TextView Nombre, String Id){
        mDataBase.child("TipoUsuario").addListenerForSingleValueEvent(new ValueEventListener() {
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
    }

    public void CargarTipoUsuario(DatabaseReference mDataBase, Spinner spnTipoUsuario, Context context){
        List<ClsTipoUsuario> tipo=new ArrayList<>();
        mDataBase.child("TipoUsuario").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String id=ds.getKey();
                        String TipoUsuario=ds.child("Tipo").getValue().toString();
                        tipo.add(new ClsTipoUsuario(id,TipoUsuario));
                    }

                    ArrayAdapter<ClsTipoUsuario> arrayAdapter= new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,tipo);
                    spnTipoUsuario.setAdapter(arrayAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}
