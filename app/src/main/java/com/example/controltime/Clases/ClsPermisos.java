package com.example.controltime.Clases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ClsPermisos {

    public  int RowId;
    public int UsuarioId;
    public double dias;
    public String FechaDesde;
    public String FechaHasta;
    public int TipoPermiso;
    public int Estado;

    /*CONSTRUCTOR , INICIALIZAMOS LA CLASE*/
    public ClsPermisos() {
        this.UsuarioId = 0;
        this.dias = 0.0;
        this.FechaDesde ="";
        this.FechaHasta = "";
        this.TipoPermiso=0;
        this.Estado=0;
        this.RowId=0;
    }

    @Override
    public String  toString() {
        return "FechaDesde='" + FechaDesde + '\'' +
                " FechaHasta='" + FechaHasta + '\'' +
                " Total dias=" + dias +
                " TipoPermiso='" + TipoPermiso + '\'' +
                " Estado=" + Estado ;

    }

    /***CONSTRUCTOR DE LA CLASE*/
    public ClsPermisos( int UsuarioId, double dias,String FechaDesde,String FechaHasta,
                        int TipoPermiso,int Estado,int Id ) {

        this.UsuarioId = UsuarioId;
        this.dias = dias;
        this.FechaDesde =FechaDesde;
        this.FechaHasta = FechaHasta;
        this.TipoPermiso=TipoPermiso;
        this.Estado=Estado;
        this.RowId=Id;


    }

    public void setRowId(int rowId) {
        RowId = rowId;
    }

    public void setUsuarioId(int usuarioId) {
        UsuarioId = usuarioId;
    }

    public void setDias(double dias) {
        this.dias = dias;
    }

    public void setFechaDesde(String fechaDesde) {
        FechaDesde = fechaDesde;
    }

    public void setFechaHasta(String fechaHasta) {
        FechaHasta = fechaHasta;
    }

    public void setTipoPermiso(int tipoPermiso) {
        TipoPermiso = tipoPermiso;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public int getRowId() {
        return RowId;
    }

    public int getUsuarioId() {
        return UsuarioId;
    }

    public double getDias() {
        return dias;
    }

    public String getFechaDesde() {
        return FechaDesde;
    }

    public String getFechaHasta() {
        return FechaHasta;
    }

    public int getTipoPermiso() {
        return TipoPermiso;
    }

    public int getEstado() {
        return Estado;
    }

    /***Metodo que inserta ppor usuario , fechas y tipode Permiso*/
    public static boolean insertarPermiso (int idUsuario,String fechaIni,String fechaFin,int tipoId,double dias){
        boolean insertado=false;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String sql;
            String diaIni = ClsUtils.formatearFecha(fechaIni);
            String diaFin = ClsUtils.formatearFecha(fechaFin);
            sql = "INSERT INTO ct_permisos(usuarioId, desdeFecha,hastaFecha,diasHabiles,tipoPermiso,estadoPermisoId)" +
                      "VALUES ( "+idUsuario+",'"+ diaIni  +"','"+  diaFin +"'," + dias + "," +tipoId+ ",0)";

              PreparedStatement ps= DbConnection.connection.prepareStatement(sql);
              insertado=ps.execute(sql);





        }catch (Exception e) {
            e.printStackTrace();
        }
        return !insertado;
    }
    /***Comprueba que no esta pedidas las fechas seleccionadas*/
    public static boolean validaIntevalosFechas(int idUsuario,String fechaIni,String fechaFin) {
        boolean hayDatos = false;
        try {
            String sql;
            String diaIni =ClsUtils.formatearFecha(fechaIni);
            String diaFin = ClsUtils.formatearFecha(fechaFin);

            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery(sql = "SELECT * FROM ct_permisos " +
                    " WHERE usuarioId =" + idUsuario + " AND ((desdeFecha BETWEEN '" + diaIni + "' AND '" + diaFin + "')" +
                    " OR (hastaFecha BETWEEN '" + diaIni + "' AND '" + diaFin + "'))");

            while (rs.next()) {
                hayDatos=true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (hayDatos);
    }
   /***Devuelve todos los permisos que tiene un usuario por fechas*/
    public static ArrayList<ClsPermisos> getPermisos (int idUsuario, String fechaIni,String fechaFin ){
        List<ClsPermisos> objPermisos = new ArrayList<>() ;

        try {
            String diaIni =ClsUtils.formatearFecha(fechaIni);
            String diaFin = ClsUtils.formatearFecha(fechaFin);
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("SELECT * FROM  ct_permisos " +
                    " WHERE usuarioId ='" + idUsuario + "' AND ((desdeFecha BETWEEN '" + diaIni + "' AND '" + diaFin + "')" +
                    " OR (hastaFecha BETWEEN '" + diaIni + "' AND '" + diaFin + "')) ORDER BY desdeFecha");
            while (rs.next()) {
                int permisosId= Integer.parseInt(rs.getString("permisosId"));
                String desdeFecha=rs.getString("desdeFecha");
                String hastaFecha=rs.getString("hastaFecha");
                double diasHabiles= Double.parseDouble(rs.getString("diasHabiles"));
                int tipoPermiso= Integer.parseInt(rs.getString("tipoPermiso"));
                int estadoPermisoId= Integer.parseInt(rs.getString("estadoPermisoId"));

                objPermisos.add(new ClsPermisos( idUsuario, diasHabiles,desdeFecha,hastaFecha,
                        tipoPermiso,estadoPermisoId,permisosId));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ClsPermisos>) objPermisos;
    }
    /***Devuelve todos los permisos que tiene un usuario por fechas, tipo y estado*/
    public static ArrayList<ClsPermisos> getPermisos (int idUsuario, String fechaIni,String fechaFin,int tipoPermisoId, int estadoId ){
        List<ClsPermisos> objPermisos = new ArrayList<>() ;

        try {
            String diaIni =ClsUtils.formatearFecha(fechaIni);
            String diaFin = ClsUtils.formatearFecha(fechaFin);
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("SELECT * FROM  ct_permisos " +
                    " WHERE estadoPermisoId="+estadoId+" AND tipoPermisoId ="+tipoPermisoId+" "  +
                    " AND  usuarioId ='" + idUsuario + "' AND ((desdeFecha BETWEEN '" + diaIni + "' AND '" + diaFin + "')" +
                    " OR (hastaFecha BETWEEN '" + diaIni + "' AND '" + diaFin + "'))");
            while (rs.next()) {
                int permisosId= Integer.parseInt(rs.getString("permisosId"));
                String desdeFecha=rs.getString("desdeFecha");
                String hastaFecha=rs.getString("hastaFecha");
                double diasHabiles= Double.parseDouble(rs.getString("diasHabiles"));
                int tipoPermiso= Integer.parseInt(rs.getString("tipoPermiso"));
                int estadoPermisoId= Integer.parseInt(rs.getString("estadoPermisoId"));

                objPermisos.add(new ClsPermisos( idUsuario, diasHabiles,desdeFecha,hastaFecha,
                        tipoPermiso,estadoPermisoId,permisosId));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ClsPermisos>) objPermisos;
    }
    /***Devuelve todos los permisos que tiene un usuario por fechas,   y estado*/
    public static ArrayList<ClsPermisos> getPermisos (int idUsuario, String fechaIni,String fechaFin,  int estadoId ){
        List<ClsPermisos> objPermisos = new ArrayList<>() ;

        try {
            String diaIni =ClsUtils.formatearFecha(fechaIni);
            String diaFin = ClsUtils.formatearFecha(fechaFin);
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("SELECT * FROM  ct_permisos " +
                    " WHERE estadoPermisoId="+estadoId+"  "  +
                    " AND  usuarioId ='" + idUsuario + "' AND ((desdeFecha BETWEEN '" + diaIni + "' AND '" + diaFin + "')" +
                    " OR (hastaFecha BETWEEN '" + diaIni + "' AND '" + diaFin + "'))");
            while (rs.next()) {
                int permisosId= Integer.parseInt(rs.getString("permisosId"));
                String desdeFecha=rs.getString("desdeFecha");
                String hastaFecha=rs.getString("hastaFecha");
                double diasHabiles= Double.parseDouble(rs.getString("diasHabiles"));
                int tipoPermiso= Integer.parseInt(rs.getString("tipoPermiso"));
                int estadoPermisoId= Integer.parseInt(rs.getString("estadoPermisoId"));

                objPermisos.add(new ClsPermisos( idUsuario, diasHabiles,desdeFecha,hastaFecha,
                        tipoPermiso,estadoPermisoId,permisosId));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ClsPermisos>) objPermisos;
    }

}
