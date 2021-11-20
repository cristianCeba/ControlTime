package com.example.controltime;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ClsFichaje {

    public String horaIni;
    public String horaFin;
    public String horaIniDescanso;
    public String horaFinDescanso;
    public String nombre;
    public String tipoUsuario;


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

    public String getNombre() {
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
    }

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
    }

}
