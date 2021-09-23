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
    Button btnIniFichaje,btnFinFichaje;
    //textMostHora --> Muestra por pantalla la hora actualizada
    //textMostDia --> Muestra por pantalla el dia y el mes actualizado
    TextView textMostHora, textMostDia;
    private DatabaseReference mDataBase;
    Date hora;
    String dia;
    String usuarioAplicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichaje);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        btnIniFichaje = findViewById(R.id.btnInicioFichaje);
        btnFinFichaje = findViewById(R.id.btnFinFichaje);
        textMostHora = findViewById(R.id.textHora);
        textMostDia = findViewById(R.id.textDia);
        usuarioAplicacion = User.UsuarioConectadoApp(getApplicationContext()).replace(".", "_").trim();
        //Fecha que usamos para guardar en la base de datos ejemplo --> 22:09:2021
        dia = new SimpleDateFormat("dd:MM:yyyy").format(new Date());


        //Recuperamos los datos del usuario del día.
        Query query=mDataBase.child("fichaje").child(usuarioAplicacion);
        System.out.println("Usuario --> " +
                usuarioAplicacion);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                     for(DataSnapshot ds: snapshot.getChildren()){
                                                         Fichaje fichajeUsuario = ds.getValue(Fichaje.class);
                                                         System.out.println("ds.getKey() --> " + ds.getKey());
                                                         System.out.println("dia --> " + dia);
                                                         if (ds.getKey().equals(dia)){
                                                             btnIniFichaje.setEnabled(false);
                                                         }
                                                     }



                                                 }

                                                 @Override
                                                 public void onCancelled(@NonNull DatabaseError error) {

                                                 }
                                             });

        // textView is the TextView view that should display it

        textMostDia.setText(dia);

        btnIniFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fecha que usamos para guardar en la base de datos ejemplo --> 22:09:2021
                Fichaje fichaje = new Fichaje (textMostHora.getText().toString());
                mDataBase.child("fichaje").child(usuarioAplicacion).child(dia).setValue(fichaje);
                btnIniFichaje.setEnabled(false);
            }
        });



        //HiloHora hilo = new HiloHora(textMostHora);
        //hilo.start();

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

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
                System.out.println("Empieza el timer --> " + textMostHora.getText().toString());
            }
        }, 0, 1000);

    }




}