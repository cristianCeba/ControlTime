package com.example.controltime;

import android.widget.Switch;

import java.io.Serializable;

public class ClsUsuarioHorario implements Serializable {

    String nombre;
    String fecha;
    String horaInicioJornada;
    String horaFinJornada;
    String horaInicioDescanso;
    String horaFinDescanso;
    String correo;
    int codigo;

    public ClsUsuarioHorario(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getcodigo() {
        return codigo;
    }

    public void setNombre(int codigo) {
        this.codigo = codigo;
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

}
