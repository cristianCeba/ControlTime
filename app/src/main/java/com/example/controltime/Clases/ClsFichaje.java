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
    //public String nombre;
    //public String tipoUsuario;


    public ClsFichaje(String horaIni, String horaFin, int fichajeId, int usuarioId, String dia, int estadoFichaje) {
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.fichajeId = fichajeId;
        this.usuarioId = usuarioId;
        this.dia = dia;
        this.estadoFichaje = estadoFichaje;
    }

    public ClsFichaje(String horaInicio, String horaFinal, String horaInicioDescanso, String horaFinalDescanso){
        this.horaIni = horaInicio;
        this.horaFin = horaFinal;
        this.horaIniDescanso  = horaInicioDescanso;
        this.horaFinDescanso = horaFinalDescanso;
    }

    public ClsFichaje(String horaInicio){
        this.horaIni = horaInicio;
    }

    public ClsFichaje(){

    }

    public String getHoraIni() {
        return horaIni;
    }

    public void setHoraIni(String horaIni) {
        this.horaIni = horaIni;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getHoraIniDescanso() {
        return horaIniDescanso;
    }

    public void setHoraIniDescanso(String horaIniDescanso) {
        this.horaIniDescanso = horaIniDescanso;
    }

    public String getHoraFinDescanso() {
        return horaFinDescanso;
    }

    public void setHoraFinDescanso(String horaFinDescanso) {
        this.horaFinDescanso = horaFinDescanso;
    }

   /* public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }*/

    public int getEstadoFichaje() {
        return estadoFichaje;
    }

    public void setEstadoFichaje(int estadoFichaje) {
        this.estadoFichaje = estadoFichaje;
    }

    /***metodo que devuelve todos los datos del fichaje por usuario y rango de fechas*/
    public static ArrayList<ClsFichaje> getFichajes (int usuarioId,String fechaIni,String fechaFin){
        List<ClsFichaje> array = new ArrayList<>() ;

        try {
            String diaIni = ClsUtils.formatearFecha(fechaIni);
            String diaFin = ClsUtils.formatearFecha(fechaFin);
            DbConnection.statement = DbConnection.connection.createStatement();



            ResultSet rs = DbConnection.statement.executeQuery("SELECT * FROM  ct_fichajes " +
                    " WHERE usuarioId ='" + usuarioId + "' AND  dia BETWEEN '" + diaIni + "' AND '" + diaFin + "'") ;

            while (rs.next()) {
                int fichajeId= Integer.parseInt(rs.getString("fichajeId"));
                String horaEntrada=rs.getString("horaEntrada");
                String horaSalida=rs.getString("horaSalida");
                String dia=rs.getString("dia");
                int estadoFichajeId= Integer.parseInt(rs.getString("estadoFichajeId"));
                array.add(new ClsFichaje(   horaEntrada,horaSalida,fichajeId,usuarioId,dia,estadoFichajeId));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ClsFichaje>) array;
    }






/*

    public void insertarFichaje(String dia, String horaEntrada, Context context){
            String URL = "http://192.168.1.135/trabajo/insertarFichaje.php";
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.toString());
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    String fecha = "'"+ dia.replace(":","-") +"'";
                    String hora = "'"+ horaEntrada +"'";

                    parametros.put("dia",fecha);
                    parametros.put("horaEntrada",hora);
                    return parametros;
                }
            };

            RequestQueue re = Volley.newRequestQueue(context);
            re.add(request);
    }

    public static synchronized void insertarFinFichaje (String dia, String horaSalida, Context context){
        String URL = "http://192.168.1.135/trabajo/horaFinFichaje.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();

                String fecha = "'"+ dia.replace(":","-") +"'";
                String hora = "'"+ horaSalida +"'";

                parametros.put("dia",fecha);
                parametros.put("horaSalida",hora);
                parametros.put("usuarioId","0");
                return parametros;
            }
        };

        RequestQueue re = Volley.newRequestQueue(context);
        re.add(request);
        re.stop();
    }*/

}
