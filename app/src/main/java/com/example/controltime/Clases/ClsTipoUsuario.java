package com.example.controltime.Clases;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClsTipoUsuario {
    int id;
    String Tipo;

    /***
     * Constructor de la clase
     * @param id id del tipo de usuario
     * @param tipo descripcion de tipo
     */
    public ClsTipoUsuario(int id, String tipo) {
        this.id=id;
        this.Tipo=tipo;
    }
    /***
     * Constructor inicializado
     */
    public ClsTipoUsuario() {
        this.id=0;
        this.Tipo="";
    }

    /***
     * Propiedades de la clase
     * @return
     */
    public int getId() {return id;}
    public String getTipo() {return Tipo;}
    public void setId(int id) {this.id = id;}
    public void setTipo(String tipo) {Tipo = tipo;}

    /***
     * Metodo toString sobrecargado
     * @return
     */
    @Override
    public String toString() {
        return  Tipo ;
    }

    /***
     * Metodo que devuelve todos los tipos de Usuario
     * @return devuelve un array del tipo ClsTipoUsuario
     */
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

    /***
     * Metodo que devuelve todos un tipo de usuario por Id
     * @param idTipoUsuario id por el que se hacer la busqueda
     * @return  devuelve un array del tipo ClsTipoUsuario
     */
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
