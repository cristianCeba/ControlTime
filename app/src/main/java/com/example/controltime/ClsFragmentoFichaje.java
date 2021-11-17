package com.example.controltime;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    //textMostHora --> Muestra por pantalla la hora actualizada
    //textMostDia --> Muestra por pantalla el dia y el mes actualizado
    TextView textMostMensaje, textMostDia, textMostrarInicioHora, textMostrarFinHora, textMostrarInicioDescanso, textMostrarFinDescanso;
    private DatabaseReference mDataBase;
    Date hora;
    Calendar cal;
    String dia,usuarioAplicacion, diaMostrar;
    Boolean fichajeEncontrado;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_cls_fragmento_fichaje, container, false);
        ClsFichaje fichajeUsuario;
        mDataBase = FirebaseDatabase.getInstance().getReference();
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
        //Fecha que usamos para guardar en la base de datos ejemplo --> 22:09:2021
        dia = new SimpleDateFormat("dd:MM:yyyy").format(new Date());
        diaMostrar = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        fichajeEncontrado = false;

        Query query=mDataBase.child("fichaje").child(usuarioAplicacion);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            /**
             * Buscamos en base de datos si el usuario ha registrado algún fichaje del día, y dependiendo de lo que el usuario ha registrado
             * habilitamos y deshabilitamos los botones de fichajes.
             */
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                btnIniFichaje.setEnabled(false);
                btnIniFichaje.setEnabled(true);
                for(DataSnapshot ds: snapshot.getChildren()){
                    ClsFichaje fichajeUsuario = ds.getValue(ClsFichaje.class);
                    System.out.println("ds.getKey() --> " + ds.getKey());
                    System.out.println("dia --> " + dia);
                    /**
                     * Revisamos todos los fichajes del usuario para ver si ya ha fichado en el día.
                     */
                    if (ds.getKey().equals(dia) && !fichajeEncontrado){
                        ClsUser.guardarFichajeUsuario(fichajeUsuario);
                        fichajeEncontrado = true;
                        btnIniFichaje.setEnabled(false);
                        /**
                         * Revisamos si solo ha registrado la hora de inicio de fichaje.
                         */
                        if(!fichajeUsuario.horaIni.equals("0") && fichajeUsuario.horaFinDescanso.equals("0") && fichajeUsuario.horaIniDescanso.equals("0")){
                            btnIniFichaje.setEnabled(false);
                            btnFinFichaje.setEnabled(false);
                            btnIniDescanso.setEnabled(true);
                            btnFinDescanso.setEnabled(false);
                            textMostMensaje.setText("TRABAJANDO");
                            textMostrarInicioHora.setText(fichajeUsuario.horaIni.toString());
                            /**
                             * Revisamos si ha registrado el inicio del fichaje y el inicio del descanso
                             */
                        } else if (!fichajeUsuario.horaIni.equals("0") && !fichajeUsuario.horaIniDescanso.equals("0") && fichajeUsuario.horaFinDescanso.equals("0")){
                            btnIniFichaje.setEnabled(false);
                            btnFinFichaje.setEnabled(false);
                            btnIniDescanso.setEnabled(false);
                            btnFinDescanso.setEnabled(true);
                            textMostMensaje.setText("DESCANSANDO");
                            textMostrarInicioHora.setText(fichajeUsuario.horaIni.toString());
                            textMostrarInicioDescanso.setText(fichajeUsuario.horaIniDescanso);
                            /**
                             * Revisamos si ha registrado el inicio del fichaje el inicio del descanso y el fin del descanso
                             */
                        } else if (!fichajeUsuario.horaIni.equals("0") && !fichajeUsuario.horaIniDescanso.equals("0") && !fichajeUsuario.horaFinDescanso.equals("0") && fichajeUsuario.horaFin.equals("0")) {
                            btnIniFichaje.setEnabled(false);
                            btnFinFichaje.setEnabled(true);
                            btnIniDescanso.setEnabled(false);
                            btnFinDescanso.setEnabled(false);
                            textMostMensaje.setText("TRABAJANDO");
                            textMostrarInicioHora.setText(fichajeUsuario.horaIni.toString());
                            textMostrarInicioDescanso.setText(fichajeUsuario.horaIniDescanso);
                            textMostrarFinDescanso.setText(fichajeUsuario.horaFinDescanso);
                            /**
                             * Solo quedaría finalizar el fichaje
                             */
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
                /**
                 * Si no encontramos fichajes del día del usuario dejamos solo habilitado el botón de iniciar fichaje.
                 */
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

        //Botón inicio de fichaje, deshabilitamos el botón y habilitamos el botón de inicio de fichaje
        btnIniFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMostMensaje.setText("TRABAJANDO");
                //Fecha que usamos para guardar en la base de datos ejemplo --> 22:09:2021
                ClsFichaje fichaje = new ClsFichaje(sacarHora(),"0","0","0");
                mDataBase.child("fichaje").child(usuarioAplicacion).child(dia).setValue(fichaje);
                btnIniFichaje.setEnabled(false);
                textMostrarInicioHora.setText(fichaje.horaIni.toString());
                btnIniDescanso.setEnabled(true);
                ClsUser.guardarFichajeUsuario(fichaje);
            }
        });

        //Botón fin de fichaje, ponemos el botón deshabilitado.
        btnFinFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMostMensaje.setText("JORNADA FINALIZADA");
                ClsFichaje fichaje = ClsUser.DevolverFichajeUsuario();
                fichaje.horaFin = sacarHora();
                mDataBase.child("fichaje").child(usuarioAplicacion).child(dia).setValue(fichaje);
                textMostrarFinHora.setText(fichaje.horaFin.toString());
                btnFinFichaje.setEnabled(false);
            }
        });
        //Botón inicio de descanso, ponemos el botón deshabilitado y habilitamos el botón de fin de descanso.
        btnIniDescanso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMostMensaje.setText("DESCANSANDO");
                ClsFichaje fichaje = ClsUser.DevolverFichajeUsuario();
                fichaje.horaIniDescanso = sacarHora();
                mDataBase.child("fichaje").child(usuarioAplicacion).child(dia).setValue(fichaje);
                textMostrarInicioDescanso.setText(fichaje.horaIniDescanso.toString());
                btnIniDescanso.setEnabled(false);
                btnFinDescanso.setEnabled(true);
            }
        });
        //Botón fin de descanso, ponemos el botón deshabilitado y habilitamos el botón de fin de fichaje.
        btnFinDescanso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMostMensaje.setText("TRABAJANDO");
                ClsFichaje fichaje = ClsUser.DevolverFichajeUsuario();
                fichaje.horaFinDescanso = sacarHora();
                mDataBase.child("fichaje").child(usuarioAplicacion).child(dia).setValue(fichaje);
                textMostrarFinDescanso.setText(fichaje.horaFinDescanso.toString());
                btnFinDescanso.setEnabled(false);
                btnFinFichaje.setEnabled(true);
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
        cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)+ 2);

        hora = cal.getTime();

        return dateFormat.format(hora).toString();
    }
}