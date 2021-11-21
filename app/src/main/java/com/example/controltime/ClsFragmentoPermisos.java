package com.example.controltime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    ArrayList<ClsUsuarioPermiso> usuarios = new ArrayList<>();
    private ClsAdaptadorPermisos adaptador;
    ListView listaDeUsuariosPermisos;
    ClsUser usuario;

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
        buscarUsuario();
        buscarUsuarios();
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

        listaDeUsuariosPermisos = vista.findViewById(R.id.listusuariopermiso);
        RellenarPermisos();

        // Inflate the layout for this fragment

        return vista;
    }

    public void RellenarPermisos (){

        adaptador = new ClsAdaptadorPermisos(getContext(),usuarios);
        listaDeUsuariosPermisos.setAdapter(adaptador);

                /*
                    Cada vez que se pulsa un fichaje preguntamos si quiere validarlo y si acepta se valida el fichaje.
                 */
        listaDeUsuariosPermisos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder opciones = new AlertDialog.Builder(view.getContext());
                opciones.setMessage("¿Quieres validar el fichaje? una vez validado el fichaje no podrá ser modificado")
                        .setTitle("Advertencia")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("Borramos usuario");
                                validarHorario(usuarios.get(position).idPermiso);
                                BorrarUsuarios();
                                buscarUsuarios();
                                RellenarPermisos();
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                opciones.show();
            }
        });

    }

    public void BorrarUsuarios(){
        for (int i = 0; usuarios.size() > i ; i++){
            usuarios.remove(i);
        }
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

    public void buscarUsuarios (){

        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()) {
                    if (usuario.TipoUsuario == 1){
                        usuarios = ClsUsuarioPermiso.BuscarPermisoPorUsuario(usuario.usuarioId,usuario.Grupo);
                    } else {
                        System.out.println("Buscamos todos los permisos");
                        usuarios = ClsUsuarioPermiso.BuscarTodosLosUsuarios(usuario.usuarioId,usuario.Grupo);
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

    public void validarHorario (String idFichaje){

        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()) {
                    ClsUsuarioPermiso.validarPermiso(idFichaje);
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