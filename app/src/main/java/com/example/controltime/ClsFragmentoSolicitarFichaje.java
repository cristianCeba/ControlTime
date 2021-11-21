package com.example.controltime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClsFragmentoSolicitarFichaje#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClsFragmentoSolicitarFichaje extends Fragment {

    EditText inicioJornada,finJornada,inicioDescanso,finDescanso;
    CalendarView calendario;
    Button btnEnviarJornada;
    Pattern pat;
    Matcher mat;
    Boolean horasNoIndicadas, diaMarcado;
    ClsFichaje fichaje = new ClsFichaje();
    String usuarioAplicacion,dia;
    int mes;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClsFragmentoSolicitarFichaje() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClsFragmentoSolicitarFichaje.
     */
    // TODO: Rename and change types and number of parameters
    public static ClsFragmentoSolicitarFichaje newInstance(String param1, String param2) {
        ClsFragmentoSolicitarFichaje fragment = new ClsFragmentoSolicitarFichaje();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_cls_fragmento_solicitar_fichaje, container, false);
        inicioJornada = vista.findViewById(R.id.editTextInicioFichaje);
        finJornada = vista.findViewById(R.id.editTextFinFichaje);
        inicioDescanso = vista.findViewById(R.id.editTextInicioDescanso);
        finDescanso = vista.findViewById(R.id.editTextFinDescanso);
        btnEnviarJornada = vista.findViewById(R.id.btnEnviarJornada);
        calendario = vista.findViewById(R.id.calendarView2);
        pat = Pattern.compile("[0-9]{2}:[0-9]{2}");
        usuarioAplicacion = ClsUser.UsuarioConectadoApp(getContext()).replace(".", "_").trim();

        diaMarcado = false;
        dia = "";
        //Guardamos el día que selecciona el usuario
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaMarcado = true;
                mes = month + 1;
                if (mes >= 10) {
                    dia = (year + "-" + mes + "-" + dayOfMonth);
                } else {
                    dia = (year + "-" + mes + "-" + dayOfMonth);
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
                    if (validarHoras()) {
                        fichaje.setHoraIni(inicioJornada.getText().toString());
                        fichaje.setHoraFin(finJornada.getText().toString());
                        fichaje.setHoraIniDescanso(inicioDescanso.getText().toString());
                        fichaje.setHoraFinDescanso(finJornada.getText().toString());
                        incluirHorario(fichaje);
                    }
                }
            }
        });
        return vista;
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
    public void incluirHorario (ClsFichaje fichaje) {

        if(diaMarcado){
            insertar(1);
            updatear(1);
            insertar(2);
            updatear(2);
            inicioJornada.setText("");
            finJornada.setText("");
            inicioDescanso.setText("");
            finDescanso.setText("");
            Toast.makeText(getContext(),"El fichaje ha sido enviado al supervisor",Toast.LENGTH_SHORT).show();
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
        final AlertDialog.Builder alertInfo = new AlertDialog.Builder(getContext());

        alertInfo.setTitle(titulo);

        alertInfo.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertInfo.show();
    }

    public void insertar (int id){
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int estadoFichaje = 0;
                DbConnection.conectarBaseDeDatos();
                if (id == 1){
                    DbConnection.insertarFichaje(dia,1,fichaje.getHoraIni().toString(),id,estadoFichaje);

                }else {
                    DbConnection.insertarFichaje(dia,1,fichaje.getHoraIniDescanso().toString(),id,estadoFichaje);
                }
                DbConnection.cerrarConexion();
            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updatear (int id){
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                DbConnection.conectarBaseDeDatos();
                if (id == 1){
                    DbConnection.incluirHoranFin(dia,1,fichaje.getHoraFin().toString(),id);

                }else {
                    DbConnection.incluirHoranFin(dia,1,fichaje.getHoraFinDescanso().toString(),id);
                }
                DbConnection.cerrarConexion();
            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}