package com.example.controltime;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClsPermisos {

    public  long RowId;
    public String Usuario;
    public double dias;
    public String FechaDesde;
    public String FechaHasta;
    public String TipoPermiso;
    public String TipoUsuario;
    public String GrupoUsuario;
    public String correo;
    public long Estado;

    /*CONSTRUCTOR , INICIALIZAMOS LA CLASE*/
    public ClsPermisos() {
        this.Usuario = "";
        this.dias = 0.0;
        this.FechaDesde ="";
        this.FechaHasta = "";
        this.TipoPermiso="";

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

    /*CONSTRUCTOR DE LA CLASE*/
    public ClsPermisos( String Usuario, double dias,String FechaDesde,String FechaHasta,
                        String TipoPermiso,long Estado,long Id ) {

        this.Usuario = Usuario;
        this.dias = dias;
        this.FechaDesde = FechaDesde;
        this.FechaHasta = FechaHasta;
        this.TipoPermiso=TipoPermiso;
        this.Estado=Estado;
        this.RowId=Id;


    }



    public ArrayList<ClsPermisos> ListaPermisosPorUsuario(Context context,String UsuarioApp){
        List<ClsPermisos> ArrayPermisos= new ArrayList<>();
        DatabaseReference mDataBase;
        mDataBase=FirebaseDatabase.getInstance().getReference();
        Query query = mDataBase.child("Permisos").child(UsuarioApp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds:snapshot.getChildren()){
                        ClsPermisos objPer = ds.getValue(ClsPermisos.class);
                        ArrayPermisos.add(new ClsPermisos(objPer.Usuario,objPer.dias, objPer.FechaDesde, objPer.FechaHasta,objPer.TipoPermiso, objPer.Estado, objPer.RowId ));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
        return (ArrayList<ClsPermisos>) ArrayPermisos;
    }
/* public int ComprobarFechas(String FechaIni,String FechaFin){

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        mDataBase = FirebaseDatabase.getInstance().getReference();
        Query query =mDataBase.child("Permisos").child(Usuario);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int cant=0;
                    for(DataSnapshot ds:snapshot.getChildren()){
                        ClsPermisos objPer = ds.getValue(ClsPermisos.class);
                        try {
                            Date dataDesde = formato.parse(objPer.FechaDesde );
                            Date dataHasta = formato.parse(objPer.FechaHasta);

                            Date Desde = formato.parse(FechaIni );
                            Date  Hasta = formato.parse(FechaFin);

                            if(  (dataDesde.compareTo(Desde) >= 0 && dataDesde.compareTo(Hasta) <= 0) ||(dataHasta.compareTo(Desde) >= 0 && dataHasta.compareTo(Hasta) <= 0)){
                                cant++;
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    suma=cant;
                }
            }
            @Override
            public void onCancelled(@NonNull   DatabaseError error) {

            }
        });
        return suma;
    }*/
    public ArrayList<ClsPermisos> ListaPermisosPorUsuarioYFechas(Context context,String UsuarioApp,String FechaIni,String FechaFin){
        List<ClsPermisos> ArrayPermisos= new ArrayList<>();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        DatabaseReference mDataBase;
        mDataBase=FirebaseDatabase.getInstance().getReference();
        Query query = mDataBase.child("Permisos").child(UsuarioApp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds:snapshot.getChildren()){
                        ClsPermisos objPer = ds.getValue(ClsPermisos.class);
                        try {
                            Date dataDesde = formato.parse(objPer.FechaDesde );
                            Date dataHasta = formato.parse(objPer.FechaHasta);

                            Date Desde = formato.parse(FechaIni );
                            Date  Hasta = formato.parse(FechaFin);

                            if(  (dataDesde.compareTo(Desde) >= 0 && dataDesde.compareTo(Hasta) <= 0) ||(dataHasta.compareTo(Desde) >= 0 && dataHasta.compareTo(Hasta) <= 0)){
                                ArrayPermisos.add(new ClsPermisos(objPer.Usuario,objPer.dias, objPer.FechaDesde, objPer.FechaHasta,objPer.TipoPermiso, objPer.Estado, objPer.RowId ));
                            }

                        }  catch (ParseException e) {
                            e.printStackTrace();
                        }



                    }
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
        return (ArrayList<ClsPermisos>) ArrayPermisos;
    }

   /* public ArrayList<ClsPermisos> ListaPermisosPorUsuario(Context context, Spinner spnPermisos, String UsuarioApp){
        List<ClsPermisos> ArrayPermisos= new ArrayList<>();
        DatabaseReference mDataBase;
        mDataBase=FirebaseDatabase.getInstance().getReference();
        Query query = mDataBase.child("Permisos").child(UsuarioApp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds:snapshot.getChildren()){
                        ClsPermisos objPer = ds.getValue(ClsPermisos.class);
                        ArrayPermisos.add(new ClsPermisos(objPer.Usuario,objPer.dias, objPer.FechaDesde, objPer.FechaHasta,objPer.TipoPermiso, objPer.Estado, objPer.RowId ));
                    }
                    ArrayAdapter<ClsPermisos> adapter=new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,ArrayPermisos);
                    spnPermisos.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
        return (ArrayList<ClsPermisos>) ArrayPermisos;
    }*/

}
