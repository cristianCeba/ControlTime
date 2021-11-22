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

public class ClsTipoUsuario {
    int id;
    String Tipo;

    public ClsTipoUsuario(int id, String tipo) {
        this.id=id;
        this.Tipo=tipo;

    }
    public ClsTipoUsuario() {
        this.id=0;
        this.Tipo="";

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

    /***Metodo que devuelve todos los tipos de Usuario*/
    public static ArrayList<ClsTipoUsuario> getTipoUsuario ( ){
        List<ClsTipoUsuario> array = new ArrayList<>() ;

        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("Select * from ct_tipousuario");
            while (rs.next()) {
                int id= Integer.parseInt(rs.getString("tipoId"));
                String descripcion=rs.getString("descripcion");
                array.add(new ClsTipoUsuario( id,descripcion));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ClsTipoUsuario>) array;
    }

    /***Metodo que devuelve todos un tipo de usuario por Id*/
    public static ArrayList<ClsTipoUsuario> getTipoUsuarioXId ( int idTipoUsuario){
        List<ClsTipoUsuario> array = new ArrayList<>() ;

        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("Select * from ct_tipousuario WHERE tipoId="+ idTipoUsuario +" ");
            while (rs.next()) {
                int id= Integer.parseInt(rs.getString("tipoId"));
                String descripcion=rs.getString("descripcion");
                array.add(new ClsTipoUsuario( id,descripcion));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ClsTipoUsuario>) array;
    }
}
