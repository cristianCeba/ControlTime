package com.example.controltime;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClsFragmentoPermisos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClsFragmentoPermisos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<ClsUsuarioHorario> usuarios = new ArrayList<>();
    private ListView listView;
    private ClsAdaptadorHorarios adaptador;
    ListView listaDeUsuarios;
    String [] nombres;
    String [] horaInicioJornada;
    String [] horaFinJornada;
    String [] horaInicioDescanso;
    String [] horaFinDescanso;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClsFragmentoPermisos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoPermisos.
     */
    // TODO: Rename and change types and number of parameters
    public static ClsFragmentoPermisos newInstance(String param1, String param2) {
        ClsFragmentoPermisos fragment = new ClsFragmentoPermisos();
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
        // Inflate the layout for this fragment

        View vista = inflater.inflate(R.layout.fragment_blank, container, false);
        System.out.println("1");
        System.out.println("getActivity() --< " + vista);
        listaDeUsuarios = vista.findViewById(R.id.listusuariohorario);

        System.out.println("2");
        ClsUsuarioHorario persona5 = new ClsUsuarioHorario();

        persona5.setNombre("Ramona");
        persona5.sethoraInicioJornada("121");
        persona5.sethoraFinJornada("12");
        persona5.sethoraInicioDescanso("1");
        persona5.sethoraFinDescanso("2");
        System.out.println("3");
        ClsUsuarioHorario persona6 = new ClsUsuarioHorario();

        persona6.setNombre("Josefa");
        persona6.sethoraInicioJornada("121");
        persona6.sethoraFinJornada("12");
        persona6.sethoraInicioDescanso("1");
        persona6.sethoraFinDescanso("2");

        usuarios.add(persona5);
        usuarios.add(persona6);

        adaptador = new ClsAdaptadorHorarios(getActivity(),usuarios);
        System.out.println("4");
        listaDeUsuarios.setAdapter(adaptador);
        System.out.println("5");
        listaDeUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        // Inflate the layout for this fragment

        return vista;
    }
}