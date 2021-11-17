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
    ArrayList<ClsUsuarioPermiso> listaUsuariosPermisos = new ArrayList<>();
    ArrayList<ClsPermisos> usuariosPermisos = new ArrayList<>();
    private ClsAdaptadorPermisos adaptador;
    private DatabaseReference mDataBase;
    ListView listaDeUsuariosPermisos;
    String correoUsuarioPidePermiso;

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
        listaDeUsuariosPermisos = vista.findViewById(R.id.listusuariopermiso);
        mDataBase = FirebaseDatabase.getInstance().getReference();

        RellenarPermisos();

        // Inflate the layout for this fragment

        return vista;
    }

    public void RellenarPermisos (){
        System.out.println("Empieza rellenar permisos");
        Query query = mDataBase.child("Permisos");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            /**
             * Buscamos en base de datos si el usuario ha registrado algún fichaje del día, y dependiendo de lo que el usuario ha registrado
             * habilitamos y deshabilitamos los botones de fichajes.
             */
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    System.out.println("ds.getKey() --> " + ds.getKey());
                    correoUsuarioPidePermiso = ds.getKey().toString();
                    for (DataSnapshot ds1 : ds.getChildren()){

                        ClsPermisos permisoUsuario = ds1.getValue(ClsPermisos.class);

                        if (permisoUsuario.Estado == 0){
                            permisoUsuario.correo = correoUsuarioPidePermiso;
                            if (ClsUser.TipoUsuarioConectadoApp(getContext()).equals("2") && permisoUsuario.TipoUsuario.equals("1")){
                                crearPermiso(permisoUsuario);
                            } else if (ClsUser.TipoUsuarioConectadoApp(getContext()).equals("0")){
                                crearPermiso(permisoUsuario);
                            }
                        }
                    }
                }

                        adaptador = new ClsAdaptadorPermisos(getActivity(), listaUsuariosPermisos);
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
                                                int aprobar = 1;
                                                String rowId = String.valueOf(usuariosPermisos.get(position).RowId);
                                                String correo = listaUsuariosPermisos.get(position).correo;
                                                usuariosPermisos.get(position).correo = null;
                                                usuariosPermisos.get(position).Estado = aprobar;
                                                mDataBase.child("Permisos").child(correo).child(rowId).setValue(usuariosPermisos.get(position));
                                                limpiarArrays();
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

                System.out.println("Termina Rellenar permisos");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



    }

    public void crearPermiso (ClsPermisos permisoUsuario){
        ClsUsuarioPermiso usuarioPermiso = new ClsUsuarioPermiso();
        usuarioPermiso.setInicioPermiso(permisoUsuario.FechaDesde);
        usuarioPermiso.setFinPermiso(permisoUsuario.FechaHasta);
        usuarioPermiso.setTipoPemriso(" solicita permiso de " + permisoUsuario.TipoPermiso);
        usuarioPermiso.setNombre(permisoUsuario.Usuario);
        usuarioPermiso.setCorreo(permisoUsuario.correo);

        usuariosPermisos.add(permisoUsuario);
        listaUsuariosPermisos.add(usuarioPermiso);
    }

    public void limpiarArrays(){
        for (int i = 0; usuariosPermisos.size() > i ; i++){
            usuariosPermisos.remove(i);
            listaUsuariosPermisos.remove(i);
        }
    }
}