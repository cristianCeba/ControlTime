package com.example.controltime.Clases;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ClsUsuarioHorario implements Serializable {

    String nombre;
    String fecha;
    String horaInicioJornada;
    String horaFinJornada;
    String horaInicioDescanso;
    String horaFinDescanso;
    String correo;
    String idImagen;
    public int idFichaje;

    public ClsUsuarioHorario(){

    }

    /***
     * Propiedades de la clase
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getcodigo() {
        return idFichaje;
    }

    public void setNombre(int codigo) {
        this.idFichaje = codigo;
    }

    public void sethoraInicioJornada(String horaInicioJornada) {
        this.horaInicioJornada = horaInicioJornada;
    }

    public String gethoraInicioJornada() {
        return horaInicioJornada;
    }

    public void sethoraFinJornada(String horaFinJornada) {
        this.horaFinJornada = horaFinJornada;
    }

    public String gethoraFinJornada() {
        return horaFinJornada;
    }

    public void sethoraInicioDescanso(String horaInicioDescanso) {
        this.horaInicioDescanso = horaInicioDescanso;
    }

    public String gethoraInicioDescanso() {
        return horaInicioDescanso;
    }

    public void sethoraFinDescanso(String horaFinDescanso) {
        this.horaFinDescanso = horaFinDescanso;
    }

    public String gethoraFinDescanso() {
        return horaFinDescanso;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(String idImagen) {
        this.idImagen = idImagen;
    }

    public int getIdFichaje() {
        return idFichaje;
    }

    public void setIdFichaje(int idFichaje) {
        this.idFichaje = idFichaje;
    }

    /***
     * Metodo que busca por departamento , descartando el usuario jefe
     * @param idUsuarioJefe el jefe del departamento
     * @param departamentoId id del departamento
     * @return devuelve un array con todos los empleados del departamento
     */
    public static ArrayList<ClsUsuarioHorario> buscarHorarioPorUsuario(int idUsuarioJefe, int departamentoId){
        ArrayList<ClsUsuarioHorario> usuarios = new ArrayList<ClsUsuarioHorario>();


        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("select ct_usuarios.nombre,ct_usuarios.apellido1, ct_usuarios.imagenId,ct_usuarios.email,ct_fichajes.horaEntrada,ct_fichajes.horaSalida,ct_fichajes.fichajeId," +
                    "ct_descansos.horaIni,ct_descansos.horaFin,ct_fichajes.dia " +
                    "from ct_usuarios " +
                    "inner join ct_fichajes on ct_usuarios.usuarioId = ct_fichajes.usuarioId " +
                    "inner join ct_descansos on ct_descansos.idFich = ct_fichajes.fichajeId " +
                    "where ct_usuarios.usuarioId not in ('"+idUsuarioJefe+"') and ct_usuarios.departamentoId = "+departamentoId+" and ct_fichajes.estadoFichajeId = 0");
            while (rs.next()) {
                ClsUsuarioHorario usuario = new ClsUsuarioHorario();

                usuario.setNombre(rs.getString("ct_usuarios.nombre") + " " + rs.getString("ct_usuarios.apellido1"));
                usuario.setIdImagen(rs.getString("ct_usuarios.imagenId"));
                usuario.sethoraInicioJornada(rs.getString("ct_fichajes.horaEntrada"));
                usuario.sethoraFinJornada(rs.getString("ct_fichajes.horaSalida"));
                usuario.sethoraInicioDescanso(rs.getString("ct_descansos.horaIni"));
                usuario.sethoraFinDescanso(rs.getString("ct_descansos.horaFin"));
                usuario.setCorreo(rs.getString("ct_usuarios.email"));
                usuario.setIdFichaje(rs.getInt("ct_fichajes.fichajeId"));
                usuario.setFecha(rs.getString("ct_fichajes.dia"));
                usuarios.add(usuario);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    /***
     * Metodo qe carga todos los usuarios menos que usuario jefe
     * @param idUsuarioJefe usuario jefe
     * @param departamentoId
     * @return
     */
    public static ArrayList<ClsUsuarioHorario> buscarTodosLosUsuarios (int idUsuarioJefe,int departamentoId){
        ArrayList<ClsUsuarioHorario> usuarios = new ArrayList<ClsUsuarioHorario>();


        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("select ct_usuarios.nombre,ct_usuarios.apellido1, ct_usuarios.imagenId,ct_usuarios.email,ct_fichajes.horaEntrada,ct_fichajes.horaSalida,ct_fichajes.fichajeId," +
                    "ct_descansos.horaIni,ct_descansos.horaFin,ct_fichajes.dia " +
                    "from ct_usuarios " +
                    "inner join ct_fichajes on ct_usuarios.usuarioId = ct_fichajes.usuarioId " +
                    "inner join ct_descansos on ct_descansos.idFich = ct_fichajes.fichajeId " +
                    "where ct_usuarios.usuarioId not in ('"+idUsuarioJefe+"') and ct_fichajes.estadoFichajeId = 0");
            while (rs.next()) {
                ClsUsuarioHorario usuario = new ClsUsuarioHorario();

                usuario.setNombre(rs.getString("ct_usuarios.nombre") + " " + rs.getString("ct_usuarios.apellido1"));
                usuario.setIdImagen(rs.getString("ct_usuarios.imagenId"));
                usuario.sethoraInicioJornada(rs.getString("ct_fichajes.horaEntrada"));
                usuario.sethoraFinJornada(rs.getString("ct_fichajes.horaSalida"));
                usuario.sethoraInicioDescanso(rs.getString("ct_descansos.horaIni"));
                usuario.sethoraFinDescanso(rs.getString("ct_descansos.horaFin"));
                usuario.setCorreo(rs.getString("ct_usuarios.email"));
                usuario.setIdFichaje(rs.getInt("ct_fichajes.fichajeId"));
                usuario.setFecha(rs.getString("ct_fichajes.dia"));
                usuarios.add(usuario);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    /***
     * Metodo que modifica el estado del fichaje
     * @param idFichaje id del fichaje
     */
    public static void validarHorario (int idFichaje){
        try {

            System.out.println("id Fichaje = " + idFichaje);
             String sql= "Update ct_fichajes set estadoFichajeId = 1 WHERE fichajeId =" + idFichaje +"";


            // 4. Obtenga el ps utilizado para enviar sentencias SQL a la base de datos
            PreparedStatement ps= DbConnection.connection.prepareStatement(sql);

            ps.execute(sql);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}
