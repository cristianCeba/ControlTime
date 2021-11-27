package com.example.controltime.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controltime.Clases.ClsFichaje;
import com.example.controltime.Clases.ClsUser;
import com.example.controltime.Clases.DbConnection;
import com.example.controltime.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClsFragmentoFichaje#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClsFragmentoFichaje extends Fragment {

    //btnIniFichaje --> Botón que inicia el fichaje.
    //btnFinFichaje --> Botón que finaliza el fichaje.
    Button btnIniFichaje,btnFinFichaje,btnIniDescanso,btnFinDescanso;
   // ImageButton btnIniFichaje;
    //textMostHora --> Muestra por pantalla la hora actualizada
    //textMostDia --> Muestra por pantalla el dia y el mes actualizado
    TextView textMostMensaje, textMostDia, textMostrarInicioHora, textMostrarFinHora, textMostrarInicioDescanso, textMostrarFinDescanso;
    Date hora;
    Calendar cal;
    String dia,usuarioAplicacion, diaMostrar;
    Boolean fichajeEncontrado;
    ClsFichaje fichaje;
    private int idUsuario;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClsFragmentoFichaje() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClsFragmentoFichaje.
     */
    // TODO: Rename and change types and number of parameters
    public static ClsFragmentoFichaje newInstance(String param1, String param2) {
        ClsFragmentoFichaje fragment = new ClsFragmentoFichaje();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fecha que usamos para guardar en la base de datos ejemplo --> 22:09:2021
        //dia = new SimpleDateFormat("dd:MM:yyyy").format(new Date());
        dia = new SimpleDateFormat("yyyy:MM:dd").format(new Date());
        diaMostrar = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        idUsuario = Integer.parseInt(ClsUser.UsuarioIdApp(getContext()));
        fichajeEncontrado = false;

        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                DbConnection.conectarBaseDeDatos();
                fichaje = DbConnection.buscarHorario(dia,idUsuario);
                DbConnection.cerrarConexion();
            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_cls_fragmento_fichaje, container, false);

        btnIniFichaje = vista.findViewById(R.id.btnInicioFichaje);
        btnFinFichaje = vista.findViewById(R.id.btnFinFichaje);
        btnIniDescanso = vista.findViewById(R.id.btnInicioDescanso);
        btnFinDescanso = vista.findViewById(R.id.btnFinDescanso);
        textMostMensaje = vista.findViewById(R.id.textMostrar);
        textMostDia = vista.findViewById(R.id.textDia);
        textMostrarInicioHora = vista.findViewById(R.id.textMostrarInicioFichaje);
        textMostrarFinHora = vista.findViewById(R.id.textMostrarFinFichaje);
        textMostrarInicioDescanso = vista.findViewById(R.id.textMostrarInicioDescanso);
        textMostrarFinDescanso = vista.findViewById(R.id.textMostrarFinDescanso);
        usuarioAplicacion = ClsUser.UsuarioConectadoApp(getContext()).replace(".", "_").trim();

        btnIniFichaje.setVisibility(View.VISIBLE);

        if (fichaje.getHoraIni() != null){
            habilitarBotones(fichaje);
        } else {
            btnIniDescanso.setVisibility(View.INVISIBLE);
            btnFinDescanso.setVisibility(View.INVISIBLE);
            btnFinFichaje.setVisibility(View.INVISIBLE);
        }
        //Mostramos la hora por pantalla
        textMostDia.setText(diaMostrar);



        //Botón inicio de fichaje, deshabilitamos el botón y habilitamos el botón de inicio de fichaje
        btnIniFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMostMensaje.setText("TRABAJANDO");
                //Fecha que usamos para guardar en la base de datos ejemplo --> 22:09:2021
                fichaje.setHoraIni(sacarHora());
                insertar(1);
                btnIniFichaje.setVisibility(View.INVISIBLE);
                textMostrarInicioHora.setText(fichaje.horaIni.toString());

                btnIniDescanso.setVisibility(View.VISIBLE);
                btnFinFichaje.setVisibility(View.VISIBLE);
            }
        });

        //Botón fin de fichaje, ponemos el botón deshabilitado.
        btnFinFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMostMensaje.setText("JORNADA FINALIZADA");
                fichaje.horaFin = sacarHora();
                updatear(1);
                textMostrarFinHora.setText(fichaje.horaFin.toString());
                btnFinFichaje.setVisibility(View.INVISIBLE);
                btnIniDescanso.setVisibility(View.INVISIBLE);
            }
        });
        //Botón inicio de descanso, ponemos el botón deshabilitado y habilitamos el botón de fin de descanso.
        btnIniDescanso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMostMensaje.setText("DESCANSANDO");
                fichaje.horaIniDescanso = sacarHora();
                insertar(2);
                textMostrarInicioDescanso.setText(fichaje.horaIniDescanso.toString());
                btnIniDescanso.setVisibility(View.INVISIBLE);
                btnFinDescanso.setVisibility(View.VISIBLE);
                btnFinFichaje.setVisibility(View.INVISIBLE);
            }
        });
        //Botón fin de descanso, ponemos el botón deshabilitado y habilitamos el botón de fin de fichaje.
        btnFinDescanso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMostMensaje.setText("TRABAJANDO");
                fichaje.horaFinDescanso = sacarHora();
                textMostrarFinDescanso.setText(fichaje.horaFinDescanso.toString());
                updatear(2);
                btnFinDescanso.setVisibility(View.INVISIBLE);
                btnFinFichaje.setVisibility(View.VISIBLE);
            }
        });

        return vista;
    }

    /**
     * Método que devuelve la hora actual.
     * @return String con la hora actual.
     */
    public String sacarHora (){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        cal = Calendar.getInstance();
        cal.setTime(new Date());


        //Le cambiamos la hora y minutos
        cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)+ 1);

        hora = cal.getTime();

        return dateFormat.format(hora).toString();
    }

    public void habilitarBotones (ClsFichaje fichajeUsuario) {

        if (fichajeUsuario.horaIni != null && fichajeUsuario.horaFinDescanso == null && fichajeUsuario.horaIniDescanso == null && fichajeUsuario.horaFin == null) {

            btnIniFichaje.setVisibility(View.INVISIBLE);
            btnFinFichaje.setVisibility(View.VISIBLE);
            btnIniDescanso.setVisibility(View.VISIBLE);
            btnFinDescanso.setVisibility(View.INVISIBLE);
            textMostMensaje.setText("TRABAJANDO");
            textMostrarInicioHora.setText(fichajeUsuario.horaIni.toString());
            /**
             * Revisamos si ha registrado el inicio del fichaje y el inicio del descanso
             */
        } else if (fichajeUsuario.horaIni != null && fichajeUsuario.horaIniDescanso != null && fichajeUsuario.horaFinDescanso == null) {
            btnIniFichaje.setVisibility(View.INVISIBLE);
            btnFinFichaje.setVisibility(View.INVISIBLE);
            btnIniDescanso.setVisibility(View.INVISIBLE);
            btnFinDescanso.setVisibility(View.VISIBLE);
            textMostMensaje.setText("DESCANSANDO");
            textMostrarInicioHora.setText(fichajeUsuario.horaIni.toString());
            textMostrarInicioDescanso.setText(fichajeUsuario.horaIniDescanso);
            /**
             * Revisamos si ha registrado el inicio del fichaje el inicio del descanso y el fin del descanso
             */
        } else if (fichajeUsuario.horaIni != null && fichajeUsuario.horaIniDescanso != null && fichajeUsuario.horaFinDescanso != null && fichajeUsuario.horaFin == null) {
            btnIniFichaje.setVisibility(View.INVISIBLE);
            btnFinFichaje.setVisibility(View.VISIBLE);
            btnIniDescanso.setVisibility(View.INVISIBLE);
            btnFinDescanso.setVisibility(View.INVISIBLE);
            textMostMensaje.setText("TRABAJANDO");
            textMostrarInicioHora.setText(fichajeUsuario.horaIni.toString());
            textMostrarInicioDescanso.setText(fichajeUsuario.horaIniDescanso);
            textMostrarFinDescanso.setText(fichajeUsuario.horaFinDescanso);
            /**
             * Solo quedaría finalizar el fichaje
             */
        } else if (fichajeUsuario.horaIni != null &&  fichajeUsuario.horaFin != null && fichajeUsuario.horaIni == null && fichajeUsuario.horaIniDescanso == null){
            btnIniFichaje.setVisibility(View.INVISIBLE);
            btnFinFichaje.setVisibility(View.INVISIBLE);
            btnIniDescanso.setVisibility(View.INVISIBLE);
            btnFinDescanso.setVisibility(View.INVISIBLE);
            textMostMensaje.setText("TRABAJANDO");
            textMostrarInicioHora.setText(fichajeUsuario.horaIni.toString());
            textMostrarFinHora.setText(fichajeUsuario.horaFin);
        }else{
                btnIniFichaje.setVisibility(View.INVISIBLE);
                btnFinFichaje.setVisibility(View.INVISIBLE);
                btnIniDescanso.setVisibility(View.INVISIBLE);
                btnFinDescanso.setVisibility(View.INVISIBLE);
                textMostMensaje.setText("JORNADA FINALIZADA");
                textMostrarInicioHora.setText(fichajeUsuario.horaIni.toString());
                textMostrarFinHora.setText(fichajeUsuario.horaFin);
                textMostrarInicioDescanso.setText(fichajeUsuario.horaIniDescanso);
                textMostrarFinDescanso.setText(fichajeUsuario.horaFinDescanso);
            }

    }

    public void insertar (int id){
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {

                if (DbConnection.conectarBaseDeDatos()) {
                    int estadoFichaje = 1;
                    if (id == 1) {
                        DbConnection.insertarFichaje(dia, idUsuario, fichaje.getHoraIni().toString(), id, estadoFichaje);

                    } else {
                        DbConnection.insertarFichaje(dia, idUsuario, fichaje.getHoraIniDescanso().toString(), id, estadoFichaje);
                    }
                    DbConnection.cerrarConexion();

                } else{
                    Toast.makeText(getContext(),"Ha ocurrido un error intentelo en unos minutos",Toast.LENGTH_SHORT).show();
                }
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
                if (DbConnection.conectarBaseDeDatos()) {
                if (id == 1){
                    DbConnection.incluirHoranFin(dia,idUsuario,fichaje.getHoraFin().toString(),id);

                }else {
                    DbConnection.incluirHoranFin(dia,idUsuario,fichaje.getHoraFinDescanso().toString(),id);
                }
                DbConnection.cerrarConexion();
                } else{
                    Toast.makeText(getContext(),"Ha ocurrido un error intentelo en unos minutos",Toast.LENGTH_SHORT).show();
                }
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