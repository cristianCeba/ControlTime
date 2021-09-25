package com.example.controltime;

public class Fichaje {

    public String horaIni;
    public String horaFin;
    public String horaIniDescanso;
    public String horaFinDescanso;

    public Fichaje (String horaInicio, String horaFinal, String horaInicioDescanso, String horaFinalDescanso){
        this.horaIni = horaInicio;
        this.horaFin = horaFinal;
        this.horaIniDescanso  = horaInicioDescanso;
        this.horaFinDescanso = horaFinalDescanso;
    }

    public Fichaje (String horaInicio){
        this.horaIni = horaInicio;
    }

    public Fichaje (){

    }
}
