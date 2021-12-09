package com.example.controltime.Clases;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClsFichaje {

    public String horaIni;
    public String horaFin;
    public String horaIniDescanso;
    public String horaFinDescanso;
    public int fichajeId;
    public int usuarioId;
    public String dia;
    public int estadoFichaje;

    /***
     * Constructor de la clase
     * @param horaIni Hora inicio del fichaje
     * @param horaFin Hora fin del fichaje
     * @param fichajeId id del fichaje
     * @param usuarioId id del usuario
     * @param dia dia del fichaje
     * @param estadoFichaje estado del fichaje
     */
    public ClsFichaje(String horaIni, String horaFin, int fichajeId, int usuarioId, String dia, int estadoFichaje) {
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.fichajeId = fichajeId;
        this.usuarioId = usuarioId;
        this.dia = dia;
        this.estadoFichaje = estadoFichaje;
    }

    public ClsFichaje(String horaIni, String horaFin, int fichajeId,
                      int usuarioId, String dia, int estadoFichaje,
                      String descansoIni, String descansoFin) {
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.fichajeId = fichajeId;
        this.usuarioId = usuarioId;
        this.dia = dia;
        this.estadoFichaje = estadoFichaje;
        this.horaIniDescanso=descansoIni;
        this.horaFinDescanso=descansoFin;
    }

    /***
     * Constructor inicializado
      */
    public ClsFichaje(){
        this.horaIni = "";
        this.horaFin = "";
        this.horaIniDescanso  = "";
        this.horaFinDescanso = "";
    }

    /***
     * Propiedades de  la clase
     * @return
     */
    public String getHoraIni() {return horaIni;}
    public void setHoraIni(String horaIni) {this.horaIni = horaIni;}
    public String getHoraFin() {return horaFin;}
    public void setHoraFin(String horaFin) {this.horaFin = horaFin;}
    public String getHoraIniDescanso() {return horaIniDescanso;}
    public void setHoraIniDescanso(String horaIniDescanso) {this.horaIniDescanso = horaIniDescanso;}
    public String getHoraFinDescanso() {return horaFinDescanso;}
    public void setHoraFinDescanso(String horaFinDescanso) {this.horaFinDescanso = horaFinDescanso;}
    public int getEstadoFichaje() {return estadoFichaje;}
    public void setEstadoFichaje(int estadoFichaje) {this.estadoFichaje = estadoFichaje;}

    /***
     * metodo que devuelve todos los datos del fichaje por usuario y rango de fechas
     * @param usuarioId id del usuario
     * @param fechaIni fecha inicial
     * @param fechaFin fecha fin
     * @return
     */
    public static ArrayList<ClsFichaje> getFichajes (int usuarioId,String fechaIni,String fechaFin){
        List<ClsFichaje> array = new ArrayList<>() ;
        try {
            String diaIni = ClsUtils.formatearFecha(fechaIni);
            String diaFin = ClsUtils.formatearFecha(fechaFin);
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("SELECT * FROM  ct_fichajes as A " +
                    " INNER JOIN  ct_descansos as B on B.idFich =A.fichajeId "+
                    " WHERE A.usuarioId ='" + usuarioId + "' AND  A.dia BETWEEN '" + diaIni + "' AND '" + diaFin + "'") ;
            while (rs.next()) {
                int fichajeId= Integer.parseInt(rs.getString("A.fichajeId"));
                String horaEntrada=rs.getString("A.horaEntrada");
                String horaSalida=rs.getString("A.horaSalida");
                String dia=rs.getString("A.dia");
                String horaDescansoIni=rs.getString("B.horaIni");
                String horaDescansoFIn=rs.getString("B.horaFin");
                int estadoFichajeId= Integer.parseInt(rs.getString("estadoFichajeId"));
                array.add(new ClsFichaje(   horaEntrada,horaSalida,fichajeId,usuarioId,dia,estadoFichajeId,horaDescansoIni,horaDescansoFIn));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ClsFichaje>) array;
    }


}
