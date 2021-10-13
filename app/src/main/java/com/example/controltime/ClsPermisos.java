package com.example.controltime;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ClsPermisos {

    public  long RowId;

    public String Usuario;
    public double dias;
    public String FechaDesde;
    public String FechaHasta;
    public long TipoPermiso;
    public long Estado;




    /*CONSTRUCTOR , INICIALIZAMOS LA CLASE*/
    public ClsPermisos() {
        this.Usuario = "";
        this.dias = 0.0;
        this.FechaDesde ="";
        this.FechaHasta = "";
        this.TipoPermiso=0;

        this.RowId=0;
    }


    /*CONSTRUCTOR DE LA CLASE*/
    public ClsPermisos( String Usuario, double dias,String FechaDesde,String FechaHasta,
                        long TipoPermiso,long Estado,long Id ) {

        this.Usuario = Usuario;
        this.dias = dias;
        this.FechaDesde = FechaDesde;
        this.FechaHasta = FechaHasta;
        this.TipoPermiso=TipoPermiso;
        this.Estado=Estado;
        this.RowId=Id;


    }



}
