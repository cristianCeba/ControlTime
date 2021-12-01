package com.example.controltime.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.controltime.Clases.ClsUser;
import com.example.controltime.Clases.DbConnection;
import com.example.controltime.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClsFragmentInicioNOO#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClsFragmentInicioNOO extends Fragment {
    ClsUser usuario;
    int id;
    View view;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClsFragmentInicioNOO() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClsFragmentInicio.
     */
    // TODO: Rename and change types and number of parameters
    public static ClsFragmentInicioNOO newInstance(String param1, String param2) {
        ClsFragmentInicioNOO fragment = new ClsFragmentInicioNOO();
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







       id= Integer.parseInt(ClsUser.UsuarioIdApp(getContext()));

        buscarUsuario(id);

        FragmentManager fragmentManager= getActivity().getSupportFragmentManager();

        FragmentTransaction transaction=fragmentManager.beginTransaction();

        if(usuario.TipoUsuario==0){
            ClsFragmentoFichaje fragmentInicial=new ClsFragmentoFichaje();
            transaction.add(R.id.nav_host_fragment, fragmentInicial);
        }else{
            /*ClsFragmentoValidarNOO fragmentInicial=new ClsFragmentoValidarNOO();
            transaction.add(R.id.nav_host_fragment, fragmentInicial);*/
        }


        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_cls_inicio, container, false);
        //return inflater.inflate(R.layout.fragment_cls_inicio, container, false);
return view;
    }


    public void buscarUsuario (int id){
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()){
                    usuario = ClsUser.getUsuario(id);
                    DbConnection.cerrarConexion();
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