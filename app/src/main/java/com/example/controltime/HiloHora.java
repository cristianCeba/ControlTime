package com.example.controltime;

import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class HiloHora extends Thread{

    TextView mostrarhora;
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public HiloHora (TextView hora){
        mostrarhora = hora;
    }
    public void run(){
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                Date hora = new Date();
                mostrarhora.setText(dateFormat.format(hora));
                }catch (Exception e){
                    System.out.println("error");
                }
            }
        },0,1000);
    }
}
