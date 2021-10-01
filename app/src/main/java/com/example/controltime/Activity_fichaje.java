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
    TextView textMostHora, textMostDia, textMostrarInicioHora, textMostrarFinHora, textMostrarInicioDescanso, textMostrarFinDescanso;
    private DatabaseReference mDataBase;
    Date hora;
    String dia;
    String usuarioAplicacion;

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
        textMostHora = findViewById(R.id.textHora);
        textMostDia = findViewById(R.id.textDia);
        textMostrarInicioHora = findViewById(R.id.textMostrarInicioFichaje);
        textMostrarFinHora = findViewById(R.id.textMostrarFinFichaje);
        textMostrarInicioDescanso = findViewById(R.id.textMostrarInicioDescanso);
        textMostrarFinDescanso = findViewById(R.id.textMostrarFinDescanso);
        usuarioAplicacion = User.UsuarioConectadoApp(getApplicationContext()).replace(".", "_").trim();
        //Fecha que usamos para guardar en la base de datos ejemplo --> 22:09:2021
        dia = new SimpleDateFormat("dd:MM:yyyy").format(new Date());

        Query query=mDataBase.child("fichaje").child(usuarioAplicacion);
        System.out.println("0");
        System.out.println("Usuario --> " +
                usuarioAplicacion);
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
                    User.guardarFichajeUsuario(fichajeUsuario);
                    if (ds.getKey().equals(dia)){
                        btnIniFichaje.setEnabled(false);
                        if (!fichajeUsuario.horaIni.equals("0")){
                            System.out.println("fichajeUsuario.horaIni --> " + fichajeUsuario.horaIni);
                            textMostrarInicioHora.setText(fichajeUsuario.horaIni.toString());
                        }
                        if(!fichajeUsuario.horaFin.equals("0")){
                            textMostrarFinHora.setText(fichajeUsuario.horaFin);
                            btnFinFichaje.setEnabled(false);
                        }
                        if(!fichajeUsuario.horaIniDescanso.equals("0")){
                            textMostrarInicioDescanso.setText(fichajeUsuario.horaIniDescanso);
                            btnIniDescanso.setEnabled(false);
                        }
                        if(!fichajeUsuario.horaFinDescanso.equals("0")){
                            textMostrarFinDescanso.setText(fichajeUsuario.horaFinDescanso);
                            btnFinDescanso.setEnabled(false);
                        }
                    } else {
                        //Solo dejamos habiitado el botón de iniciar fichaje
                        btnIniDescanso.setEnabled(false);
                        btnFinDescanso.setEnabled(false);
                        btnFinFichaje.setEnabled(false);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("4");
            }
        });

        //Mostramos la hora por pantalla
        textMostDia.setText(dia);

        //Botón inicio de fichaje, deshabilitamos el botón cuando se pulsa y se muestra la hora de inicio de fichaje.
        btnIniFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fecha que usamos para guardar en la base de datos ejemplo --> 22:09:2021
                Fichaje fichaje = new Fichaje (textMostHora.getText().toString(),"0","0","0");
                mDataBase.child("fichaje").child(usuarioAplicacion).child(dia).setValue(fichaje);
                btnIniFichaje.setEnabled(false);
                textMostrarInicioHora.setText(fichaje.horaIni.toString());
                btnIniDescanso.setEnabled(true);
                btnFinDescanso.setEnabled(true);
            }
        });

        //Botón fin de fichaje, ponemos el botón deshabilitado y mostramos la hora del fin de fichaje.
        btnFinFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fichaje fichaje = User.DevolverFichajeUsuario();
                fichaje.horaFin = textMostHora.getText().toString();
                mDataBase.child("fichaje").child(usuarioAplicacion).child(dia).setValue(fichaje);
                textMostrarFinHora.setText(fichaje.horaFin.toString());
                btnFinFichaje.setEnabled(false);
            }
        });

        btnIniDescanso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fichaje fichaje = User.DevolverFichajeUsuario();
                fichaje.horaIniDescanso = textMostHora.getText().toString();
                mDataBase.child("fichaje").child(usuarioAplicacion).child(dia).setValue(fichaje);
                textMostrarInicioDescanso.setText(fichaje.horaIniDescanso.toString());
                btnIniDescanso.setEnabled(false);
            }
        });

        btnFinDescanso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fichaje fichaje = User.DevolverFichajeUsuario();
                fichaje.horaFinDescanso = textMostHora.getText().toString();
                mDataBase.child("fichaje").child(usuarioAplicacion).child(dia).setValue(fichaje);
                textMostrarFinDescanso.setText(fichaje.horaFinDescanso.toString());
                btnFinDescanso.setEnabled(false);
                btnFinFichaje.setEnabled(true);
            }
        });

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        //Ponemos un timer para mostar cada segundo la hora y mostrarla.
        Timer timer = new Timer();
        try {
            Date hora = new Date();
            textMostHora.setText(dateFormat.format(hora));
        }catch (Exception e){
            System.out.println("error");
        }

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    hora = new Date();
                    String h = dateFormat.format(hora);
                    textMostHora.setText("");
                    textMostHora.setText(h);
                }catch (Exception e){
                    System.out.println("error");
                    this.run();
                }
            }
        }, 0, 1000);

    }

}


