package com.example.controltime;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ClsUsuarioPermiso implements Serializable {

    String nombre;
    String tipoPemriso;
    String inicioPermiso;
    String finPermiso;
    String correo;
    String tipoUsuario;
    String idPermiso;
    int idImage;

    public ClsUsuarioPermiso (){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoPemriso() {
        return tipoPemriso;
    }

    public void setTipoPemriso(String tipoPemriso) {
        this.tipoPemriso = tipoPemriso;
    }

    public String getInicioPermiso() {
        return inicioPermiso;
    }

    public void setInicioPermiso(String inicioPermiso) {
        this.inicioPermiso = inicioPermiso;
    }

    public String getFinPermiso() {
        return finPermiso;
    }

    public void setFinPermiso(String finPermiso) {
        this.finPermiso = finPermiso;
    }

    public int getidImage() {
        return idImage;
    }

    public void setidImage(int codigo) {
        this.idImage = codigo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(String idPermiso) {
        this.idPermiso = idPermiso;
    }

    public static ArrayList<ClsUsuarioPermiso> BuscarPermisoPorUsuario(int idUsuarioJefe, int departamentoId){
        ArrayList<ClsUsuarioPermiso> usuarios = new ArrayList<ClsUsuarioPermiso>();


        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("select ct_usuarios.email,ct_usuarios.imagenId, ct_usuarios.nombre,ct_usuarios.apellido1,ct_tipopermisos.descripcion,ct_permisos.desdeFecha, ct_permisos.hastaFecha,ct_permisos.permisosID " +
                    "from ct_usuarios " +
                    "inner join ct_permisos on ct_permisos.usuarioId = ct_usuarios.usuarioId " +
                    "inner join ct_tipopermisos on ct_tipopermisos.tipoPermisosId = ct_permisos.tipoPermiso " +
                    "where ct_usuarios.usuarioId not in ("+idUsuarioJefe+") and ct_permisos.estadoPermisoId = 0 and ct_usuarios.departamentoId ="+departamentoId+"");
            while (rs.next()) {
                ClsUsuarioPermiso usuario = new ClsUsuarioPermiso();

                usuario.setNombre(rs.getString("ct_usuarios.nombre") + " " + rs.getString("ct_usuarios.apellido1"));
                usuario.setidImage(rs.getInt("ct_usuarios.imagenId"));
                usuario.setInicioPermiso(rs.getString("ct_permisos.desdeFecha"));
                usuario.setFinPermiso(rs.getString("ct_permisos.hastaFecha"));
                usuario.setTipoPemriso(rs.getString("ct_tipopermisos.descripcion"));
                usuario.setIdPermiso(rs.getString("ct_permisos.permisosID"));
                usuario.setCorreo(rs.getString("ct_usuarios.email"));

                usuarios.add(usuario);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public static ArrayList<ClsUsuarioPermiso> BuscarTodosLosUsuarios(int idUsuarioJefe, int departamentoId){
        ArrayList<ClsUsuarioPermiso> usuarios = new ArrayList<ClsUsuarioPermiso>();


        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("select ct_usuarios.email,ct_usuarios.imagenId, ct_usuarios.nombre,ct_usuarios.apellido1,ct_tipopermisos.descripcion,ct_permisos.desdeFecha, ct_permisos.hastaFecha,ct_permisos.permisosID " +
                    "from ct_usuarios " +
                    "inner join ct_permisos on ct_permisos.usuarioId = ct_usuarios.usuarioId " +
                    "inner join ct_tipopermisos on ct_tipopermisos.tipoPermisosId = ct_permisos.tipoPermiso " +
                    "where ct_usuarios.usuarioId not in ("+idUsuarioJefe+") and ct_permisos.estadoPermisoId = 0");

            while (rs.next()) {
                ClsUsuarioPermiso usuario = new ClsUsuarioPermiso();

                usuario.setNombre(rs.getString("ct_usuarios.nombre") + " " + rs.getString("ct_usuarios.apellido1"));
                usuario.setidImage(rs.getInt("ct_usuarios.imagenId"));
                usuario.setInicioPermiso(rs.getString("ct_permisos.desdeFecha"));
                usuario.setFinPermiso(rs.getString("ct_permisos.hastaFecha"));
                usuario.setTipoPemriso(rs.getString("ct_tipopermisos.descripcion"));
                usuario.setIdPermiso(rs.getString("ct_permisos.permisosID"));
                usuario.setCorreo(rs.getString("ct_usuarios.email"));
                System.out.println("Añadimos permiso");
                usuarios.add(usuario);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public static void validarPermiso (String idPermiso){
        try {

            System.out.println("id Fichaje = " + idPermiso);
            String sql= "Update ct_permisos set estadoPermisoId = 1 WHERE permisosID ='" + idPermiso +"'";


            // 4. Obtenga el ps utilizado para enviar sentencias SQL a la base de datos
            PreparedStatement ps= DbConnection.connection.prepareStatement(sql);

            ps.execute(sql);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
