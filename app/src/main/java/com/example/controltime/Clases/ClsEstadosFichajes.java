package com.example.controltime.Clases;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClsEstadosFichajes {
    int id;
    String descripcion;

    public ClsEstadosFichajes(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /*Metodo que devuelve todos los estados del fichaje*/
    public static ArrayList<ClsEstadosFichajes> getEstadoFichaje ( ){
        List<ClsEstadosFichajes> array = new ArrayList<>() ;

        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("Select * from ct_estadosfichajes");
            while (rs.next()) {
                int id= Integer.parseInt(rs.getString("estadoFichajeId"));
                String descripcion=rs.getString("descripcion");
                array.add(new ClsEstadosFichajes( id,descripcion));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ClsEstadosFichajes>) array;
    }

    /*Metodo que devuelve  un estado por Id*/
    public static ArrayList<ClsEstadosFichajes> getEstadoFichajeXId ( int idEstadoFichaje){
        List<ClsEstadosFichajes> array = new ArrayList<>() ;

        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("Select * from ct_estadosfichajes WHERE estadoFichajeId="+idEstadoFichaje+"");
            while (rs.next()) {
                int id= Integer.parseInt(rs.getString("estadoFichajeId"));
                String descripcion=rs.getString("descripcion");
                array.add(new ClsEstadosFichajes( id,descripcion));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ClsEstadosFichajes>) array;
    }
}
