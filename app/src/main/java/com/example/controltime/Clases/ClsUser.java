package com.example.controltime.Clases;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ClsUser {
    public int usuarioId;
    public String correoElectronico;
    public String contraseña;
    public String Nombre;
    public String Ape;
    public String Ape2;
    public int TipoUsuario;
    public int Grupo;//departamento
    public String idImagen;
    public boolean bloqueado;
    static ClsFichaje fichajeUsuario;
    private ClsUser objUser;
    RequestQueue requestQueue;

    /***
     * Metodo toString sobrecargado
     * @return devuelve el id, nombre y apellido
     */
    @Override
    public String toString() {
        return  usuarioId + "-"+ Nombre +" "+ Ape;
    }
    /***
     * CONSTRUCTOR , INICIALIZAMOS LA CLASE
     */
    public ClsUser() {
        this.contraseña = "";
        this.Nombre = "";
        this.Ape = "";
        this.correoElectronico = "";
        this.TipoUsuario=0;
        this.Grupo =0;
        this.usuarioId=0;
        this.idImagen="";
        this.bloqueado=false;
    }
     /***
     * CONSTRUCTOR DE LA CLASE
     * @param usuarioId id usuario
     * @param Nombre nombre del usuario
     * @param Ape apellido del usuario
     * @param Ape2 2º apellido del usuario
     * @param correo correo del usuario
     * @param TipoUsuario tipo usuario al que pertenece
     * @param Grupo grupo/departamento al que pertenece
     * @param idImagen id de la imagen
     * @param bloqueado si esta bloqueado
     */
    public ClsUser(int usuarioId, String Nombre, String Ape, String Ape2, String correo,int TipoUsuario,int Grupo,String idImagen,boolean bloqueado ) {

        this.Nombre = Nombre;
        this.Ape = Ape;
        this.Ape2 = Ape2;
        this.correoElectronico = correo;
        this.TipoUsuario=TipoUsuario;
        this.Grupo =Grupo;
        this.usuarioId=usuarioId;
        this.idImagen=idImagen;
        this.bloqueado=bloqueado;
    }

    /***
     * Metodo que guarda el usuario conectada
     * @param usuario el cooreo del usuario
     * @param contrasena la contraseña
     * @param usuarioId el id de usuario
     * @param contex
     */
    public static void UsuarioPreferencesApp(String usuario,String contrasena,int usuarioId,Context contex){
        SharedPreferences prefe = contex.getSharedPreferences("usuarioApp",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =prefe.edit();
        editor.putString("usuario",usuario .toString());
        editor.putString("contraseña",contrasena .toString());
        editor.putString("usuarioId", String.valueOf(usuarioId));
        editor.commit();
    }


    public static String UsuarioIdApp(Context contex){
        SharedPreferences preferencias = contex.getSharedPreferences("usuarioApp", Context.MODE_PRIVATE);
        return preferencias.getString("usuarioId","No hay ID usuario");
    }

public static String UsuarioConectadoApp(Context contex){
    SharedPreferences preferencias = contex.getSharedPreferences("usuarioApp", Context.MODE_PRIVATE);
    return preferencias.getString("usuario","No hay usuario conectado");
}

public static String UsuarioContrasenaConectadoApp(Context contex){
    SharedPreferences preferencias = contex.getSharedPreferences("usuarioApp", Context.MODE_PRIVATE);
    return preferencias.getString("contraseña","No hay contraseña");
}

    /***
     * Metodo que inicializa los datos del usuario conectado
     * @param contex
     */
    public static void CerrarSesion(Context contex){
    SharedPreferences preferencias = contex.getSharedPreferences("usuarioApp", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor =preferencias.edit();
    editor.putString("usuario",null);
    editor.putString("contraseña",null);

    editor.commit();
}

    /***
     * metodo que devuelve todos los datos del usuario por el email
     * @param correo correo del usuario
     * @return
     */
    public static ClsUser getUsuario (String correo){
        ClsUser usuario = new ClsUser();

        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("Select * from ct_usuarios where email = '"+correo+"'");
            while (rs.next()) {
                usuario.Nombre = (rs.getString("nombre"));
                usuario.Ape = (rs.getString("apellido1"));
                usuario.Ape2= (rs.getString("apellido2"));
                usuario.correoElectronico= (rs.getString("email"));
                usuario.bloqueado= Boolean.parseBoolean((rs.getString("bloqueado")));
                usuario.Grupo= Integer.parseInt((rs.getString("departamentoId")));
                usuario.TipoUsuario= Integer.parseInt((rs.getString("tipoUsuarioId")));
                usuario.idImagen = (rs.getString("imagenId"));
                usuario.usuarioId= Integer.parseInt((rs.getString("usuarioId")));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }

    /****
     * metodo que devuelve todos los datos del usuario por el id
     * @param usuarioId id del usuario
     * @return devuelve los datos del usuario
     */
    public static ClsUser getUsuario (int usuarioId){
        ClsUser usuario = new ClsUser();

        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("Select * from ct_usuarios where usuarioId = "+usuarioId+"");
            while (rs.next()) {
                usuario.Nombre = (rs.getString("nombre"));
                usuario.Ape = (rs.getString("apellido1"));
                usuario.Ape2= (rs.getString("apellido2"));
                usuario.correoElectronico= (rs.getString("email"));
                usuario.bloqueado= Boolean.parseBoolean((rs.getString("bloqueado")));
                usuario.Grupo= Integer.parseInt((rs.getString("departamentoId")));
                usuario.TipoUsuario= Integer.parseInt((rs.getString("tipoUsuarioId")));
                usuario.idImagen = (rs.getString("imagenId"));
                usuario.usuarioId= Integer.parseInt((rs.getString("usuarioId")));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }

    /***
     * Metodo que inserta Usaurio
     * @param nombre nombre del usuario
     * @param apellido1  apellido del usuario
     * @param email  email del usuario
     * @param departamentoId departamento al que pertenece
     * @param tipoUsuarioId tipo de usuario
     * @return devuelve un valor boolen (true- esta insertado correctamente; false - no se ha insertado)
     */
    public static boolean insertarUsuario (String nombre,String apellido1,String  email, int departamentoId,int tipoUsuarioId){
        boolean insertado=false;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String sql;

            sql = "INSERT INTO ct_usuarios(nombre,apellido1, email, bloqueado, departamentoId,tipoUsuarioId, imagenId)" +
                    "VALUES ( '"+nombre+"','"+ apellido1  +"','" + email + "',0," +departamentoId+ "," +tipoUsuarioId+ ",0)";

            PreparedStatement ps=DbConnection.connection.prepareStatement(sql);
            insertado=ps.execute(sql);





        }catch (Exception e) {
            e.printStackTrace();
        }
        return !insertado;
    }

    /***
     * Metodo que bloquea Usaurio
     * @param usuarioId id de usuario
     * @return devuelve un valor boolean
     */
    public static boolean bloqueaUsuario ( int usuarioId){
        boolean insertado=false;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String sql;

            sql = "UPDATE  ct_usuarios SET  bloqueado=1" +
                    "WHERE usuarioId=" +usuarioId+ ",0)";

            PreparedStatement ps=DbConnection.connection.prepareStatement(sql);
            insertado=ps.execute(sql);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return !insertado;
    }

    /****
     * metodo que devuelve todos   usuario por departamemto y tipo usuario y por bloqueado
     * @param departamentoId id del departamento
     * @param tipousuarioId id del tipo de usuario
     * @param bloqueado si esta bloqeuado
     * @return devuelve un array de todos los usuarios encontrados
     */
    public static List<ClsUser> getUsuario (int departamentoId,int tipousuarioId,boolean bloqueado){
        List<ClsUser> array = new ArrayList<>();
        ResultSet rs;
        try {
            DbConnection.statement = DbConnection.connection.createStatement();
            if(tipousuarioId==0){
                  rs = DbConnection.statement.executeQuery("SELECT * FROM  ct_usuarios WHERE bloqueado="+bloqueado+"   AND tipoUsuarioId>"+tipousuarioId+" ");
            }else {
                  rs = DbConnection.statement.executeQuery("SELECT * FROM  ct_usuarios WHERE bloqueado=" + bloqueado + " AND  departamentoId=" + departamentoId + " AND tipoUsuarioId>" + tipousuarioId + " ");
            }
            while (rs.next()) {
                String Nombre = (rs.getString("nombre"));
                String Ape = (rs.getString("apellido1"));
                String Ape2= (rs.getString("apellido2"));
                String correo= (rs.getString("email"));
                String idImagen = (rs.getString("imagenId"));
                int usuarioId= Integer.parseInt((rs.getString("usuarioId")));
                array.add(new ClsUser( usuarioId,  Nombre,  Ape,  Ape2,  correo, tipousuarioId,departamentoId, idImagen, bloqueado));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    /***
     * Metodo que modifica el id de imagen del Usaurio
     * @param usuarioId id del usuario
     * @param idImagenNuevo id de la imagen
     */
    public static void updatearIdImagenUsuario ( int usuarioId,String idImagenNuevo){
        try {
            String sql;

            sql = "UPDATE  ct_usuarios SET  imagenId = '"+idImagenNuevo +
                    "' WHERE usuarioId = '" +usuarioId+ "'";

            PreparedStatement ps=DbConnection.connection.prepareStatement(sql);
            ps.execute(sql);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /***
     * Metodo que modifica el nombre de usuario
     * @param nombreNuevo nombre nuevo
     * @param usuarioId id del usuario
     */
    public static void modificarNombre ( String nombreNuevo,int usuarioId){
        try {
            String sql;

            sql = "UPDATE  ct_usuarios SET  nombre = '"+nombreNuevo +
                    "' WHERE usuarioId = '" +usuarioId+ "'";

            PreparedStatement ps=DbConnection.connection.prepareStatement(sql);
            ps.execute(sql);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }




}



