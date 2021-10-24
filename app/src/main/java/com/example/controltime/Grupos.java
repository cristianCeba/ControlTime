package com.example.controltime;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Grupos {
    String id;
    String Grupo;

    public Grupos(String id, String grupo) {
        this.Grupo=grupo;
        this.id=id;

    }
    public Grupos() {
        this.Grupo="";
        this.id="";

    }
    public String getGrupo() {
        return Grupo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGrupo(String grupo) {
        Grupo = grupo;
    }

    @Override
    public String toString() {
        return  Grupo ;
    }
    public void CargarGrupo(DatabaseReference mDataBase, Spinner spnGrupo, Context context){
        List<Grupos> grupos=new ArrayList<>();
        mDataBase.child("Grupos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String id=ds.getKey();
                        String grupo=ds.child("Grupo").getValue().toString();
                        grupos.add(new Grupos(id,grupo));
                    }

                    ArrayAdapter<Grupos> arrayAdapter= new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,grupos);
                    spnGrupo.setAdapter(arrayAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
