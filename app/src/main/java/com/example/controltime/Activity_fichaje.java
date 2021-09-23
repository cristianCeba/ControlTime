package com.example.controltime;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichaje);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        btnIniFichaje = findViewById(R.id.btnInicioFichaje);
        btnFinFichaje = findViewById(R.id.btnFinFichaje);
        textMostHora = findViewById(R.id.textHora);
        textMostDia = findViewById(R.id.textDia);
        //Fecha que usamos para guardar en la base de datos ejemplo --> 22:09:2021
        String date = new SimpleDateFormat("dd:MM:yyyy").format(new Date());



        // textView is the TextView view that should display it
        textMostDia.setText(date);

        btnIniFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fecha que usamos para guardar en la base de datos ejemplo --> 22:09:2021
                String date = new SimpleDateFormat("dd:MM:yyyy").format(new Date());
                Fichaje fichaje = new Fichaje (textMostHora.getText().toString());
                mDataBase.child("fichaje").child("1").child(date).setValue(fichaje);
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
                    System.out.println("1");
                    hora = new Date();
                    System.out.println("2");
                    String h = dateFormat.format(hora);
                    System.out.println("3");
                    textMostHora.setText("");
                    textMostHora.setText(h);
                    System.out.println("4");
                }catch (Exception e){
                    System.out.println("error");
                    this.run();
                }
                System.out.println("Empieza el timer --> " + textMostHora.getText().toString());
            }
        }, 0, 1000);

    }




}