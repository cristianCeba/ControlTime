package com.example.controltime.Clases;

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

/***Metodo que devuelve todos los tipos de permisos*/
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

    /***Metodo que devuelve todos un tipo de permisos por Id*/
    public static ArrayList<ClsTipoPermiso> getTipoPermisoXId ( int idTipoPermiso){
        List<ClsTipoPermiso> objPer = new ArrayList<>() ;

        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("Select * from ct_tipopermisos WHERE tipoPermisosId=" +idTipoPermiso+ "");
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

}
