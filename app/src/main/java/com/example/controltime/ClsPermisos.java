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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

    /*CONSTRUCTOR DE LA CLASE*/
    public ClsPermisos( int UsuarioId, double dias,String FechaDesde,String FechaHasta,
                        int TipoPermiso,int Estado,int Id ) {

        this.UsuarioId = UsuarioId;
        this.dias = 0.0;
        this.FechaDesde ="";
        this.FechaHasta = "";
        this.TipoPermiso=TipoPermiso;
        this.Estado=Estado;
        this.RowId=Id;


    }
    public static boolean insertarPermiso (int idUsuario,String fechaIni,String fechaFin,int tipoId){
        boolean insertado=false;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String sql;

            String diaIni = fechaIni.replace("/", "-") ;
            String diaFin = fechaFin.replace("/", "-");


            double dias=ClsUtils.calculaDiasHabiles(formato.parse(diaIni),formato.parse(diaFin));
            fechaIni=new SimpleDateFormat("yyyy:MM:dd").format(new Date());
            fechaFin=new SimpleDateFormat("yyyy:MM:dd").format(new Date());

            fechaIni= fechaIni.replace(":", "-") ;
            fechaFin= fechaFin.replace(":", "-");
            sql = "INSERT INTO ct_permisos(usuarioId, desdeFecha,hastaFecha,diasHabiles,tipoPermiso,estadoPermisoId)" +
                    "VALUES ( "+idUsuario+",'"+ fechaIni  +"','"+  fechaFin +"'," + dias + "," +tipoId+ ",0)";

            PreparedStatement ps=DbConnection.connection.prepareStatement(sql);
            insertado=ps.execute(sql);




        }catch (Exception e) {
            e.printStackTrace();
        }
        return insertado;
    }

    public static boolean validaIntevalosFechas(int idUsuario,String fechaIni,String fechaFin) {
        boolean hayDatos = false;
        try {
            String sql;
            fechaIni=new SimpleDateFormat("yyyy:MM:dd").format(new Date());
            fechaFin=new SimpleDateFormat("yyyy:MM:dd").format(new Date());

            fechaIni= fechaIni.replace(":", "-") ;
            fechaFin= fechaFin.replace(":", "-");

            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery(sql = "SELECT * FROM ct_usuarios " +
                    " WHERE usuarioId ='" + idUsuario + "' AND (desdeFecha BETWEEN '" + fechaIni + "' AND '" + fechaFin + "')" +
                    " OR (hastaFecha BETWEEN '" + fechaIni + "' AND '" + fechaFin + "')");

            while (rs.next()) {
                hayDatos=true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (hayDatos);
    }
    public static ArrayList<ClsPermisos> getPermisos (int idUsuario, String fechaIni,String fechaFin ){
        List<ClsPermisos> objPermisos = new ArrayList<>() ;

        try {
            fechaIni=new SimpleDateFormat("yyyy:MM:dd").format(new Date());
            fechaFin=new SimpleDateFormat("yyyy:MM:dd").format(new Date());

            fechaIni= fechaIni.replace(":", "-") ;
            fechaFin= fechaFin.replace(":", "-");
            DbConnection.statement = DbConnection.connection.createStatement();
            ResultSet rs = DbConnection.statement.executeQuery("SELECT * FROM  ct_permisos " +
                    " WHERE usuarioId ='" + idUsuario + "' AND (desdeFecha BETWEEN '" + fechaIni + "' AND '" + fechaFin + "')" +
                    " OR (hastaFecha BETWEEN '" + fechaIni + "' AND '" + fechaFin + "')");
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
