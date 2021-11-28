package com.example.controltime.Clases;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClsGrupos {
    int id;
    String Grupo;

    /***
     * constructor de la clase
     * @param id id del grupo
     * @param grupo descripcion del grupo
     */
    public ClsGrupos(int id, String grupo) {
        this.Grupo=grupo;
        this.id=id;
}
    /***
     * Inicializa
     */
    public ClsGrupos() {
        this.Grupo="";
        this.id=0;
    }

    /***
     * Propiedades de la clase
     * @return
     */
    public String getGrupo() {return Grupo;}
    public void setId(int id) {this.id = id;}
    public void setGrupo(String grupo) {Grupo = grupo;}

    /***
     * Metodo toString sobrecargado
     * @return
     */
    @Override
    public String toString() {
        return  Grupo ;
    }

    /***
     * Metodo que devuelve todos los departamento
     * @return devuelve un array del tipo ClsGrupo
     */
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

    /***
     * Metodo que devuelve todos un tipo de usuario por Id
     * @param idDepartamento id del departamento/Grupo por el que se va a buscar
     * @return devuelve un array del tipo ClsGrupo
     */
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
