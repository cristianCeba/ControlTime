package com.example.controltime.Fragmentos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.controltime.Adaptadores.ClsAdaptadorHorarios;
import com.example.controltime.Clases.ClsUser;
import com.example.controltime.Clases.ClsUsuarioHorario;
import com.example.controltime.Clases.ClsUtils;
import com.example.controltime.Clases.DbConnection;
import com.example.controltime.R;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ClsFragmentoHorario extends Fragment {



    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    ArrayList<ClsUsuarioHorario> usuarios = new ArrayList<>();
    ClsUser u = new ClsUser();
    private ClsAdaptadorHorarios adaptador;
    ListView listaDeUsuarios;
    ClsUser usuario;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ClsFragmentoHorario() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ClsFragmentoHorario newInstance(int columnCount) {
        ClsFragmentoHorario fragment = new ClsFragmentoHorario();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buscarUsuario();
        buscarUsuarios();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragmento_horario, container, false);
        //grupoUsuarios = u.ListaUsuariosPorGrupoYTipo(getContext(),ClsUser.GruposuarioConectadoApp(getContext()),ClsUser.TipoUsuarioConectadoApp(getContext()));
        listaDeUsuarios = view.findViewById(R.id.listusuariohorario);
        RellenarHorarios();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void RellenarHorarios (){

        adaptador = new ClsAdaptadorHorarios(getActivity(),usuarios);
        listaDeUsuarios.setAdapter(adaptador);

                /*
                    Cada vez que se pulsa un fichaje preguntamos si quiere validarlo y si acepta se valida el fichaje.
                 */
        listaDeUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater  layoutInflater=LayoutInflater.from(getContext());
                view=layoutInflater.inflate(R.layout.informativo,null);


               AlertDialog.Builder opciones = new AlertDialog.Builder(view.getContext());
                opciones.setMessage("¿Quieres validar el fichaje? una vez validado el fichaje no podrá ser modificado")
                        .setTitle("Advertencia")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("Borramos usuario");
                                validarHorario(usuarios.get(position).idFichaje);
                                BorrarUsuariosHorario();
                                buscarUsuarios();
                                RellenarHorarios();
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                AlertDialog tit=opciones.create();
                tit.setView(view);
                tit.show();


            }
        });

    }

    public void buscarUsuario (){

        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()) {
                    usuario = ClsUser.getUsuario(ClsUser.UsuarioConectadoApp(getContext()));
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

    public void BorrarUsuariosHorario (){
        for(int i = 0; usuarios.size() > i ; i++){
            usuarios.remove(i);
        }
    }

    public void buscarUsuarios (){

        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()) {
                    if (usuario.TipoUsuario == 1){
                        usuarios = ClsUsuarioHorario.buscarHorarioPorUsuario(usuario.usuarioId,usuario.Grupo);
                    } else {
                        usuarios = ClsUsuarioHorario.buscarTodosLosUsuarios(usuario.usuarioId,usuario.Grupo);
                    }

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

    public void validarHorario (int idFichaje){

        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()) {
                    ClsUsuarioHorario.validarHorario(idFichaje);
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