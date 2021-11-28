package com.example.controltime.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.controltime.Clases.ClsUser;
import com.example.controltime.Clases.DbConnection;
import com.example.controltime.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClsFragmentInicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClsFragmentInicio extends Fragment {
    ClsUser usuario;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClsFragmentInicio() {
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
    public static ClsFragmentInicio newInstance(String param1, String param2) {
        ClsFragmentInicio fragment = new ClsFragmentInicio();
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
        int id= Integer.parseInt(ClsUser.UsuarioIdApp(getContext()));

        buscarUsuario(id);
        if(usuario.TipoUsuario==0){
            return inflater.inflate(R.layout.fragment_cls_fragmento_validar, container, false);
        }else{
            return inflater.inflate(R.layout.fragment_cls_fragmento_fichaje, container, false);
        }
        //return inflater.inflate(R.layout.fragment_cls_inicio, container, false);

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