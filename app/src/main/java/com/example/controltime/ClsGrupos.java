package com.example.controltime;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClsGrupos {
    int id;
    String Grupo;

    public ClsGrupos(int id, String grupo) {
        this.Grupo=grupo;
        this.id=id;

    }
    public ClsGrupos() {
        this.Grupo="";
        this.id=0;

    }
    public String getGrupo() {
        return Grupo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGrupo(String grupo) {
        Grupo = grupo;
    }

    @Override
    public String toString() {
        return  Grupo ;
    }


    /*Metodo que devuelve todos los departamento*/
    public static ArrayList<ClsGrupos> getDepartamento ( ){
        List<ClsGrupos> array = new ArrayList<>() ;

        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("Select * from ct_departamento");
            while (rs.next()) {
                int id= Integer.parseInt(rs.getString("departamentoId"));
                String descripcion=rs.getString("descripcion");
                array.add(new ClsGrupos( id,descripcion));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ClsGrupos>) array;
    }

    /*Metodo que devuelve todos un tipo de usuario por Id*/
    public static ArrayList<ClsGrupos> getDepartamentoXId ( int idDepartamento){
        List<ClsGrupos> array = new ArrayList<>() ;

        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("Select * from ct_departamento WHERE departamentoId="+idDepartamento+"");
            while (rs.next()) {
                int id= Integer.parseInt(rs.getString("departamentoId"));
                String descripcion=rs.getString("descripcion");
                array.add(new ClsGrupos( id,descripcion));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ClsGrupos>) array;
    }
}
