package com.example.controltime;

import java.util.Date;

public class ClsPermisos {


    public String Usuario;
    public double dias;
    public String FechaDesde;
    public String FechaHasta;
    public long TipoPermiso;

    /*CONSTRUCTOR , INICIALIZAMOS LA CLASE*/
    public ClsPermisos() {
        this.Usuario = "";
        this.dias = 0.0;
        this.FechaDesde ="";
        this.FechaHasta = "";
        this.TipoPermiso=0;
    }



    /*CONSTRUCTOR DE LA CLASE*/
    public ClsPermisos( String Usuario, double dias,String FechaDesde,String FechaHasta,long TipoPermiso ) {

        this.Usuario = Usuario;
        this.dias = dias;
        this.FechaDesde = FechaDesde;
        this.FechaHasta = FechaHasta;
        this.TipoPermiso=TipoPermiso;


    }


}