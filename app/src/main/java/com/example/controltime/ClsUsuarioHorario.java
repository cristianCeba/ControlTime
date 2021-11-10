package com.example.controltime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

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
