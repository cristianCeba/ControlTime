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
}
