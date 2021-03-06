package com.example.controltime.Clases;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClsEstadosFichajes {
    int id;
    String descripcion;
    /***
     * Constructor de la clase
     * @param id id del estado del fichaje
     * @param descripcion descripcion del estado del fichaje
     */
    public ClsEstadosFichajes(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }
    /***
     * Propiedades de la clase
     * @return
     */
    public int getId() {return id;    }
    public void setId(int id) {this.id = id;    }
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;    }
    public String getDescripcion() {return descripcion;}
    /***
     * Metodo que devuelve todos los estados del fichaje
     * @return devuelve un array con todos los valores
     */
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
    /***
     * Metodo que devuelve  un estado por Id
     * @param idEstadoFichaje id del estado
     * @return devuelve un array con los datos de ese estado
     */
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

    public static String NombrePermiso ( int idTipoEstado){

        String descripcion="";
        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("Select * from ct_estadosfichajes WHERE estadoFichajeId=" +idTipoEstado+ "");
            while (rs.next()) {

                descripcion=rs.getString("descripcion");

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return descripcion;
    }
}
