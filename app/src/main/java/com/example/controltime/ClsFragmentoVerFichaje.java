package com.example.controltime;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClsFragmentoVerFichaje#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClsFragmentoVerFichaje extends Fragment {

    CalendarView calendario;
    TextView mostrarInicio,mostrarFin,mostrarInicioDesc,mostrarFinDesc,mostrarMensaje;
    private DatabaseReference mDataBase;
    String usuarioAplicacion,dia;
    int mes;
    ClsFichaje fichaje;
    boolean diaEncontrado;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClsFragmentoVerFichaje() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClsFragmentoVerFichaje.
     */
    // TODO: Rename and change types and number of parameters
    public static ClsFragmentoVerFichaje newInstance(String param1, String param2) {
        ClsFragmentoVerFichaje fragment = new ClsFragmentoVerFichaje();
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
        View vista = inflater.inflate(R.layout.fragment_cls_fragmento_ver_fichaje, container, false);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        calendario = vista.findViewById(R.id.calendarView);
        mostrarInicio = vista.findViewById(R.id.textMostrarHoraIni);
        mostrarFin = vista.findViewById(R.id.textMostrarHoraFin);
        mostrarInicioDesc = vista.findViewById(R.id.textMostrarHoraIniDesc);
        mostrarFinDesc = vista.findViewById(R.id.textMostrarHoraFinDesc);
        mostrarMensaje = vista.findViewById(R.id.textMostrarError);
        usuarioAplicacion = ClsUser.UsuarioConectadoApp(getContext()).replace(".", "_").trim();


        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                limpiarPantalla();
                diaEncontrado = false;
                mes = month + 1;
                if (mes >= 10) {
                    dia = (year + "-" + mes + "-" + dayOfMonth );
                } else {
                    dia = (year + "-" + mes + "-" + dayOfMonth );
                }
                System.out.println("dia --> " + dia);

                buscarDia();

                if (fichaje.getEstadoFichaje() == 1){
                    if (fichaje.horaIni != null) {
                        diaEncontrado = true;
                        mostrarInicio.setText(fichaje.horaIni);
                    }
                    if (fichaje.horaFin != null) {
                        mostrarFin.setText(fichaje.horaFin);
                    }
                    if (fichaje.horaIniDescanso != null) {
                        mostrarInicioDesc.setText(fichaje.horaIniDescanso);
                    }
                    if (fichaje.horaFinDescanso != null) {
                        mostrarFinDesc.setText(fichaje.horaFinDescanso);
                    }
                }

                if (!diaEncontrado) {
                    mostrarMensaje.setText("No se ha encontrado registro");
                }
            }
        });

        return vista;
    }

    public void limpiarPantalla (){
        mostrarInicio.setText("");
        mostrarFin.setText("");
        mostrarInicioDesc.setText("");
        mostrarFinDesc.setText("");
        mostrarMensaje.setText("");
    }

    public void buscarDia (){
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                DbConnection.conectarBaseDeDatos();
                fichaje = DbConnection.buscarHorario(dia,1);
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