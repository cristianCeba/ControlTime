package com.example.controltime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
    Date hora;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichaje);

        btnIniFichaje = findViewById(R.id.btnInicioFichaje);
        btnFinFichaje = findViewById(R.id.btnFinFichaje);
        textMostHora = findViewById(R.id.textHora);
        textMostDia = findViewById(R.id.textDia);

        String date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        hora = new Date();

        // textView is the TextView view that should display it
        textMostDia.setText(date);
        textMostHora.setText(dateFormat.format(hora));


    }

}