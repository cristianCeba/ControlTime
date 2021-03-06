package com.example.controltime.Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {
    static Connection connection;
    static Statement statement;

    public static Statement getStatement() {
        return statement;
    }

    /***
     * Metodo que conecta con la base de datos
     * @return
     */
    public static boolean conectarBaseDeDatos (){

        String url = "jdbc:mysql://qxp181.pacopiesgato.es:3306/qxp181";
        String userName = "qxp181";
        String password = "Camaleon1973";
        connection=null;
        boolean conecta=false;
        try {
            connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Conéctese a la base de datos con éxito !!!");
            // 1, carga el controlador
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("Unidad cargada con éxito !!!");
            conecta=true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return conecta;
    }

    /***
     * Metodo que cierra la conexiom
     */
    public static void cerrarConexion (){
        if(connection!=null){
            try {
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    /*public static void insertarUsuario (){
        try {

            //3.sql declaración
            String sql = "INSERT INTO ct_usuarios (nombre,apellido1,departamentoId,tipoUsuarioId, email,imagenId) VALUES ( 'Cristian', 'Ceballos',1,1,'cristiaaa@gmail.com',125)";
            // 4. Obtenga el ps utilizado para enviar sentencias SQL a la base de datos
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.execute(sql);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }*/


    public static void insertarFichaje (String dia,int idUsuario,String horaEntrada,int id,int estadoFichaje){
        try {
            String sql;
            String fecha = dia.replace(":","-");
            int idFIchaje = DbConnection.getIdFIchaje(dia,idUsuario);
            if (id == 1){
                //3.sql declaración
                sql = "INSERT INTO ct_fichajes (usuarioID,dia,horaEntrada,estadoFichajeId) VALUES ( "+idUsuario+",'"+fecha+"','"+horaEntrada+"'," +estadoFichaje+")";
            } else {
                sql = "INSERT INTO ct_descansos (idFich,horaIni) VALUES ( "+idFIchaje+",'"+horaEntrada+"')";
            }
            // 4. Obtenga el ps utilizado para enviar sentencias SQL a la base de datos
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.execute(sql);


        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void incluirHoranFin (String dia,int idUsuario,String horaFin,int id){
        try {
            String fecha = dia.replace(":","-");
            String sql;

            System.out.println("hora fin --> " + horaFin);
            //3.sql declaración
            if (id ==1){
                 sql= "Update ct_fichajes set horaSalida = '"+horaFin+"' WHERE usuarioId="+idUsuario+" AND  dia='"+fecha+"' ";
            } else {
                 int idDescanso = DbConnection.getIdDescanso(dia,idUsuario);
                 sql = "Update ct_descansos set horaFin = '"+horaFin+"' WHERE descansosId="+idDescanso+"";
            }

            // 4. Obtenga el ps utilizado para enviar sentencias SQL a la base de datos
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.execute(sql);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ClsFichaje buscarHorario (String dia, int idUsuario)  {

        ClsFichaje fichaje = null;
        String fecha = dia.replace(":","-");
        ResultSet rs = null;

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery("Select ct_fichajes.horaEntrada, ct_fichajes.horaSalida,ct_descansos.horaIni,ct_descansos.horaFin,ct_fichajes.estadoFichajeId from ct_fichajes " +
                    "left JOIN ct_descansos ON ct_fichajes.fichajeId=ct_descansos.idFich WHERE usuarioId="+idUsuario+" AND  dia='"+fecha+"'");

            while (rs.next()) {
                fichaje = new ClsFichaje();
                fichaje.setHoraIni(rs.getString("ct_fichajes.horaEntrada"));
                fichaje.setHoraFin(rs.getString("ct_fichajes.horaSalida"));
                fichaje.setHoraIniDescanso(rs.getString("ct_descansos.horaIni"));
                fichaje.setHoraFinDescanso(rs.getString("ct_descansos.horaFin"));
                fichaje.setEstadoFichaje(rs.getInt("ct_fichajes.estadoFichajeId"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



        return fichaje;
    }

    public static int getIdFIchaje (String dia,int idUsuario)  {

        ClsFichaje fichaje = new ClsFichaje();
        String fecha = dia.replace(":","-");
        ResultSet rs = null;
        int id = 0;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery("Select ct_fichajes.fichajeId from ct_fichajes " +
                    "left JOIN ct_descansos ON ct_fichajes.fichajeId=ct_descansos.idFich WHERE usuarioId="+idUsuario+" AND  dia='"+fecha+"'");

            while (rs.next()) {
                id  = rs.getInt("ct_fichajes.fichajeId");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return id;

    }

    public static int getIdDescanso (String dia,int idUsuario)  {

        ClsFichaje fichaje = new ClsFichaje();
        String fecha = dia.replace(":","-");
        ResultSet rs = null;
        int id = 0;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery("Select ct_descansos.descansosId from ct_fichajes " +
                    "left JOIN ct_descansos ON ct_fichajes.fichajeId=ct_descansos.idFich WHERE usuarioId="+idUsuario+" AND  dia='"+fecha+"'");

            while (rs.next()) {
                id  = rs.getInt("ct_descansos.descansosId");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return id;

    }
}
