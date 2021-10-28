package com.example.controltime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity_ModificarFichaje extends AppCompatActivity {

    EditText inicioJornada,finJornada,inicioDescanso,finDescanso;
    CalendarView calendario;
    Button btnEnviarJornada;
    Pattern pat;
    Matcher mat;
    Boolean horasNoIndicadas, diaMarcado;
    String usuarioAplicacion,dia;
    int mes;
    private DatabaseReference mDataBase;


    /**
     * Actividad en la que el usuario podrá solicitar a su supervisor la modificación de horas de un día en concreto.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__modificar_fichaje);

        inicioJornada = findViewById(R.id.editTextInicioFichaje);
        finJornada = findViewById(R.id.editTextFinFichaje);
        inicioDescanso = findViewById(R.id.editTextInicioDescanso);
        finDescanso = findViewById(R.id.editTextFinDescanso);
        btnEnviarJornada =findViewById(R.id.btnEnviarJornada);
        calendario = findViewById(R.id.calendarView2);
        pat = Pattern.compile("[0-9]{2}:[0-9]{2}");
        usuarioAplicacion = User.UsuarioConectadoApp(getApplicationContext()).replace(".", "_").trim();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        diaMarcado = false;
        dia = "";
        //Guardamos el día que selecciona el usuario
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaMarcado = true;
                mes = month + 1;
                if (mes >= 10) {
                    dia = (dayOfMonth + ":" + mes + ":" + year);
                } else {
                    dia = (dayOfMonth + ":" + "0" + mes + ":" + year);
                }
            }
        });

        //Se guarda en base de datos la modificación de horas que solicita el usuario.
        btnEnviarJornada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horasNoIndicadas = false;

                if (!validarHora(inicioJornada.getText().toString())){
                    horasNoIndicadas = true;
                }
                if (!validarHora(finJornada.getText().toString())){
                    horasNoIndicadas = true;
                }
                if (!validarHora(inicioDescanso.getText().toString())){
                    horasNoIndicadas = true;
                }
                if (!validarHora(finDescanso.getText().toString())){
                    horasNoIndicadas = true;
                }

                if (horasNoIndicadas){
                    mostrarError("Se debe de indicar hora y minutos por ejemplo: 14:25","Se debe de indicar todas las horas");
                } else {

                    Query query=mDataBase.child("users");
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        /**
                         * Buscamos en base de datos si el usuario ha registrado algún fichaje del día, y dependiendo de lo que el usuario ha registrado
                         * habilitamos y deshabilitamos los botones de fichajes.
                         */
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot ds: snapshot.getChildren()) {

                                User usuario = ds.getValue(User.class);

                                if (ds.getKey().equals(usuarioAplicacion)) {
                                    if (validarHoras()){
                                        incluirHorario(usuario);
                                    }
                                }
                            }


                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("4");
                        }
                    });
                }

            }
        });



    }

    //Validador de hora que indica el usuario. ejemplo -> 14:25
    public boolean validarHora (String hora){
        mat = pat.matcher(hora);
        System.out.println("hora --> " + hora);
        if (mat.matches()) {
            comprobarHora(hora);
            System.out.println("true");
            return true;
        } else {
            System.out.println("false");
            return false;
        }
    }

    // Método que revisa si el usuario ha marcado un día en el calendarío, sino mostramos un error.
    public void incluirHorario (User usuario) {

        if(diaMarcado){
            String grupo = String.valueOf(usuario.Grupo);
            Fichaje fichaje = new Fichaje(inicioJornada.getText().toString(),finJornada.getText().toString(),inicioDescanso.getText().toString(),finDescanso.getText().toString());
            mDataBase.child("FichajesSolicitados").child(grupo).child(dia).child(usuarioAplicacion).setValue(fichaje);
            Toast.makeText(Activity_ModificarFichaje.this, "Horario enviado al supervisor",
                    Toast.LENGTH_SHORT).show();
        } else {
            mostrarError("Indica el día:","Se debe de indicar el día que solicitas la modificación");
        }

    }

    //Método que realiza la validación si el usuario ha introducido correctamente las horas indicadas
    public void comprobarHora(String horaEnviada) {

        String [] horaDividida = horaEnviada.split(":");
        int hora = Integer.parseInt(horaDividida[0]);
        int minutos = Integer.parseInt(horaDividida[1]);
        System.out.println("hora --> " + hora);
        System.out.println("minutos --> " + minutos);

        if (hora > 23 || minutos > 59) {
            mostrarError("Hora mal indicada :" + horaEnviada, "Alguna hora no está indicada correctamente");
        }
    }

    /*
        Método que realizar validación que las horas indicadas por el usuario se ha introducido de forma correcta
        1- La hora de inicio de la jornada tiene que ser la menor de todas las horas
        2- La hora de inicio del descanso debe de ser menor que la final del descanso
        3- La hora de inicio del descanso debe de ser menor que el final del descanso y la final de la jornada
        4- La hora final del descanso debe de ser menor que la hora final de la jornada
        5- La hora final del descanso debe de ser menor que el inicio del descanso
     */
    public boolean validarHoras (){
        boolean horasCorrectas = true;
        String[] horaInicioJornada = inicioJornada.getText().toString().split(":");
        String[] horaFinalJornada = finJornada.getText().toString().split(":");
        String[] horaInicioDescanso = inicioDescanso.getText().toString().split(":");
        String[] horaFinalDescanso = finDescanso.getText().toString().split(":");

        if (Integer.parseInt(horaInicioJornada[0]) > Integer.parseInt(horaInicioDescanso[0]) ||
                Integer.parseInt(horaInicioJornada[0]) > Integer.parseInt(horaFinalDescanso[0])||
                Integer.parseInt(horaInicioJornada[0]) > Integer.parseInt(horaFinalJornada[0])){
            horasCorrectas = false;
            mostrarError("","La hora de inicio de la jornada debe de ser la menor hora");
        } else if (Integer.parseInt(horaInicioDescanso[0]) == Integer.parseInt(horaFinalDescanso[0])){
            if (Integer.parseInt(horaInicioDescanso[1]) > Integer.parseInt(horaFinalDescanso[1])){
                horasCorrectas = false;
                mostrarError("","La hora final del descanso debe de ser la menor que el inicio del descanso");
            }
        }else if (Integer.parseInt(horaInicioDescanso[0]) > Integer.parseInt(horaFinalJornada[0]) ||
                Integer.parseInt(horaInicioDescanso[0]) > Integer.parseInt(horaFinalDescanso[0])){
            horasCorrectas = false;
            mostrarError("","La hora de inicio del descanso debe de ser la menor que el fin del descanso y de la jornada");
        }else if (Integer.parseInt(horaFinalDescanso[0]) > Integer.parseInt(horaFinalJornada[0])){
            horasCorrectas = false;
            mostrarError("","La hora final del descanso debe de ser la menor que el de la jornada");
        } else if (Integer.parseInt(horaInicioDescanso[0]) > Integer.parseInt(horaFinalDescanso[0])){
            horasCorrectas = false;
            mostrarError("","La hora final del descanso debe de ser menor que el inicio del descanso");
        }
        return horasCorrectas;
    }

    /*
        Método que muestra un error al usuario
     */
    public void mostrarError (String secuencia, String titulo){
        final CharSequence[] opciones = {secuencia};
        final AlertDialog.Builder alertInfo = new AlertDialog.Builder(Activity_ModificarFichaje.this);

        alertInfo.setTitle(titulo);

        alertInfo.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertInfo.show();
    }



}