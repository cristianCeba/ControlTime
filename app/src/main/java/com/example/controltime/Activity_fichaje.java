package com.example.controltime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.Timer;
import java.util.TimerTask;

public class Activity_fichaje extends AppCompatActivity {

    //btnIniFichaje --> Botón que inicia el fichaje.
    //btnFinFichaje --> Botón que finaliza el fichaje.
    Button btnIniFichaje,btnFinFichaje,btnIniDescanso,btnFinDescanso;
    //textMostHora --> Muestra por pantalla la hora actualizada
    //textMostDia --> Muestra por pantalla el dia y el mes actualizado
    TextView textMostMensaje, textMostDia, textMostrarInicioHora, textMostrarFinHora, textMostrarInicioDescanso, textMostrarFinDescanso;
    private DatabaseReference mDataBase;
    Date hora;
    Calendar cal;
    String dia,usuarioAplicacion, diaMostrar;
    Boolean fichajeEncontrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichaje);

        Fichaje fichajeUsuario;
        mDataBase = FirebaseDatabase.getInstance().getReference();
        btnIniFichaje = findViewById(R.id.btnInicioFichaje);
        btnFinFichaje = findViewById(R.id.btnFinFichaje);
        btnIniDescanso = findViewById(R.id.btnInicioDescanso);
        btnFinDescanso = findViewById(R.id.btnFinDescanso);
        textMostMensaje = findViewById(R.id.textMostrar);
        textMostDia = findViewById(R.id.textDia);
        textMostrarInicioHora = findViewById(R.id.textMostrarInicioFichaje);
        textMostrarFinHora = findViewById(R.id.textMostrarFinFichaje);
        textMostrarInicioDescanso = findViewById(R.id.textMostrarInicioDescanso);
        textMostrarFinDescanso = findViewById(R.id.textMostrarFinDescanso);
        usuarioAplicacion = User.UsuarioConectadoApp(getApplicationContext()).replace(".", "_").trim();
        //Fecha que usamos para guardar en la base de datos ejemplo --> 22:09:2021
        dia = new SimpleDateFormat("dd:MM:yyyy").format(new Date());
        diaMostrar = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        fichajeEncontrado = false;

        Query query=mDataBase.child("fichaje").child(usuarioAplicacion);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Buscamos todos los fichajes del usuario y si ya ha fichado hoy, dehsabilitamos el botón de iniciar fichaje
                btnIniFichaje.setEnabled(false);
                btnIniFichaje.setEnabled(true);
                for(DataSnapshot ds: snapshot.getChildren()){
                    Fichaje fichajeUsuario = ds.getValue(Fichaje.class);
                    System.out.println("ds.getKey() --> " + ds.getKey());
                    System.out.println("dia --> " + dia);
                    if (ds.getKey().equals(dia) && !fichajeEncontrado){
                        User.guardarFichajeUsuario(fichajeUsuario);
                        fichajeEncontrado = true;
                        btnIniFichaje.setEnabled(false);

                        if(!fichajeUsuario.horaIni.equals("0") && fichajeUsuario.horaFinDescanso.equals("0") && fichajeUsuario.horaIniDescanso.equals("0")){
                            btnIniFichaje.setEnabled(false);
                            btnFinFichaje.setEnabled(false);
                            btnIniDescanso.setEnabled(true);
                            btnFinDescanso.setEnabled(false);
                            textMostMensaje.setText("TRABAJANDO");
                            textMostrarInicioHora.setText(fichajeUsuario.horaIni.toString());
                        } else if (!fichajeUsuario.horaIni.equals("0") && !fichajeUsuario.horaIniDescanso.equals("0") && fichajeUsuario.horaFinDescanso.equals("0")){
                            btnIniFichaje.setEnabled(false);
                            btnFinFichaje.setEnabled(false);
                            btnIniDescanso.setEnabled(false);
                            btnFinDescanso.setEnabled(true);
                            textMostMensaje.setText("DESCANSANDO");
                            textMostrarInicioHora.setText(fichajeUsuario.horaIni.toString());
                            textMostrarInicioDescanso.setText(fichajeUsuario.horaIniDescanso);
                        } else if (!fichajeUsuario.horaIni.equals("0") && !fichajeUsuario.horaIniDescanso.equals("0") && !fichajeUsuario.horaFinDescanso.equals("0") && fichajeUsuario.horaFin.equals("0")) {
                            btnIniFichaje.setEnabled(false);
                            btnFinFichaje.setEnabled(true);
                            btnIniDescanso.setEnabled(false);
                            btnFinDescanso.setEnabled(false);
                            textMostMensaje.setText("TRABAJANDO");
                            textMostrarInicioHora.setText(fichajeUsuario.horaIni.toString());
                            textMostrarInicioDescanso.setText(fichajeUsuario.horaIniDescanso);
                            textMostrarFinDescanso.setText(fichajeUsuario.horaFinDescanso);
                        } else {
                            btnIniFichaje.setEnabled(false);
                            btnFinFichaje.setEnabled(false);
                            btnIniDescanso.setEnabled(false);
                            btnFinDescanso.setEnabled(false);
                            textMostMensaje.setText("JORNADA FINALIZADA");
                            textMostrarInicioHora.setText(fichajeUsuario.horaIni.toString());
                            textMostrarFinHora.setText(fichajeUsuario.horaFin);
                            textMostrarInicioDescanso.setText(fichajeUsuario.horaIniDescanso);
                            textMostrarFinDescanso.setText(fichajeUsuario.horaFinDescanso);
                        }
                    }

                }

                if (!fichajeEncontrado){
                    btnIniDescanso.setEnabled(false);
                    btnFinDescanso.setEnabled(false);
                    btnFinFichaje.setEnabled(false);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("4");
            }
        });

        //Mostramos la hora por pantalla
        textMostDia.setText(diaMostrar);

        //Botón inicio de fichaje, deshabilitamos el botón cuando se pulsa y se muestra la hora de inicio de fichaje.
        btnIniFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMostMensaje.setText("TRABAJANDO");
                //Fecha que usamos para guardar en la base de datos ejemplo --> 22:09:2021
                Fichaje fichaje = new Fichaje (sacarHora(),"0","0","0");
                mDataBase.child("fichaje").child(usuarioAplicacion).child(dia).setValue(fichaje);
                btnIniFichaje.setEnabled(false);
                textMostrarInicioHora.setText(fichaje.horaIni.toString());
                btnIniDescanso.setEnabled(true);
                User.guardarFichajeUsuario(fichaje);
            }
        });

        //Botón fin de fichaje, ponemos el botón deshabilitado y mostramos la hora del fin de fichaje.
        btnFinFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMostMensaje.setText("JORNADA FINALIZADA");
                Fichaje fichaje = User.DevolverFichajeUsuario();
                fichaje.horaFin = sacarHora();
                mDataBase.child("fichaje").child(usuarioAplicacion).child(dia).setValue(fichaje);
                textMostrarFinHora.setText(fichaje.horaFin.toString());
                btnFinFichaje.setEnabled(false);
            }
        });

        btnIniDescanso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMostMensaje.setText("DESCANSANDO");
                Fichaje fichaje = User.DevolverFichajeUsuario();
                fichaje.horaIniDescanso = sacarHora();
                mDataBase.child("fichaje").child(usuarioAplicacion).child(dia).setValue(fichaje);
                textMostrarInicioDescanso.setText(fichaje.horaIniDescanso.toString());
                btnIniDescanso.setEnabled(false);
                btnFinDescanso.setEnabled(true);
            }
        });

        btnFinDescanso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMostMensaje.setText("TRABAJANDO");
                Fichaje fichaje = User.DevolverFichajeUsuario();
                fichaje.horaFinDescanso = sacarHora();
                mDataBase.child("fichaje").child(usuarioAplicacion).child(dia).setValue(fichaje);
                textMostrarFinDescanso.setText(fichaje.horaFinDescanso.toString());
                btnFinDescanso.setEnabled(false);
                btnFinFichaje.setEnabled(true);
            }
        });


    }


    public String sacarHora (){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        cal = Calendar.getInstance();
        cal.setTime(new Date());

        //Desplegamos la fecha
        System.out.println("Fecha actual: " + hora);

        //Le cambiamos la hora y minutos
        cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)+ 2);

        hora = cal.getTime();

        return dateFormat.format(hora).toString();
    }

}


