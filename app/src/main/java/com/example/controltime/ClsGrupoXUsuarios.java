package com.example.controltime;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClsGrupoXUsuarios {
    List<ClsGrupoXUsuarios> GrupoUsu;
    String id;
    String Usuario;
    String Grupo;
    String TiposUsuario;
    String valor;
    public ClsGrupoXUsuarios(String id, String Usuario, String grupo, String tiposUsuario) {
        this.id = id;
        this.Usuario = Usuario;
        this.Grupo = grupo;
        this.TiposUsuario = tiposUsuario;
    }

    public ClsGrupoXUsuarios() {
        this.id = "";
        Usuario = "";
        Grupo = "";
        TiposUsuario = "";
    }

    public String getId() {
        return id;
    }

    public String getUsuario() {
        return Usuario;
    }

    public String getGrupo() {
        return Grupo;
    }

    public String getTiposUsuario() {
        return TiposUsuario;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public void setGrupo(String grupo) {
        this.Grupo = grupo;
    }

    public void setTiposUsuario(String tiposUsuario) {
        this.TiposUsuario = tiposUsuario;
    }

    @Override
    public String toString() {
        return id ;
    }

    public void CargarGrupoXUsuario(DatabaseReference mDataBase, Spinner spnId, Context context) {
       List<ClsGrupoXUsuarios> GrupoUsu=new ArrayList<>();
       List<Integer>Listid= new ArrayList<>();
        mDataBase.child("GruposXUsuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String idGrupo = ds.getKey();
                        String Usuario = ds.child("usuario").getValue().toString();
                        String Grupo = ds.child("grupo").getValue().toString();
                        String TiposUsuario = ds.child("tiposUsuario").getValue().toString();
                        String id = ds.child("id").getValue().toString();
                        GrupoUsu.add(new ClsGrupoXUsuarios(id,Usuario,Grupo,TiposUsuario));
                        Listid.add(Integer.valueOf(idGrupo));

                    }
                    Collections.sort(Listid);
                    Toast.makeText(context,String.valueOf(Collections.max(Listid)),Toast.LENGTH_LONG).show();
                       setId(String.valueOf(Collections.max(Listid)));
                    ArrayAdapter<Integer> arrayAdapter= new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,Listid);
                   //ArrayAdapter<GrupoXUsuarios> arrayAdapter= new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,GrupoUsu);
                   spnId.setAdapter(arrayAdapter);
                   // setId(String.valueOf(GrupoUsu.get(GrupoUsu.size()-1)));

                   
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}