package com.example.controltime.Clases;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClsTipoPermiso {
    int id;
    String Tipo;

    /***
     * Constructor de la clase
     * @param id id del tipo de permiso
     * @param tipoPermisos descripcion permiso
     */
    public ClsTipoPermiso(int id, String tipoPermisos) {
        this.id=id;
        this.Tipo=tipoPermisos;
    }
    /***
     * Clase inicializada
     */
    public ClsTipoPermiso() {
        this.id=0;
        this.Tipo="tipoPermisos";
    }

    public int getId() {return id;}
    public String getTipo() {return Tipo;}
    public void setId(int id) {this.id = id;}
    public void setTipo(String tipo) {Tipo = tipo;
    }

    /***
     * Metodo toString sobrecargado
     * @return devuelve la descripcion del tipo de permiso
     */
    @Override
    public String toString() {
        return  Tipo ;
    }

    /***
     * Metodo que devuelve todos los tipos de permisos
     * @return devuelve un array del tipo ClsTipoPermiso
     */
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
    /***
     * Metodo que devuelve todos un tipo de permisos por Id
     * @param idTipoPermiso id por el que se va a hacer la busqueda
     * @return   devuelve un array del tipo ClsTipoPermiso
     */
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

    /***
     *
     * @param idTipoPermiso
     * @return
     */
    public static String NombrePermiso ( int idTipoPermiso){

        String descripcion="";
        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("Select * from ct_tipopermisos WHERE tipoPermisosId=" +idTipoPermiso+ "");
            while (rs.next()) {

                  descripcion=rs.getString("descripcion");

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return descripcion;
    }
}
