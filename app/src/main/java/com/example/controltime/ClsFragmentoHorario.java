package com.example.controltime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    ArrayList<ClsUser> grupoUsuarios = new ArrayList<>();
    ClsUser u = new ClsUser();
    private ClsAdaptadorHorarios adaptador;
    String usuarioAplicacion,idGrupo,nombre;
    ListView listaDeUsuarios;
    private DatabaseReference mDataBase;

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

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragmento_horario, container, false);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        usuarioAplicacion = ClsUser.UsuarioConectadoApp(getActivity()).replace(".", "_").trim();
        idGrupo = ClsUser.GruposuarioConectadoApp(getContext());
        grupoUsuarios = u.ListaUsuariosPorGrupoYTipo(getContext(),ClsUser.GruposuarioConectadoApp(getContext()),ClsUser.TipoUsuarioConectadoApp(getContext()));
        listaDeUsuarios = view.findViewById(R.id.listusuariohorario);
        RellenarHorarios();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void RellenarHorarios (){
        Query query = mDataBase.child("FichajesSolicitados").child(idGrupo);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            /**
             * Buscamos en base de datos los fichajes pendientes de aprobar por el administrador del grupo.
             */
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String fecha;
                for (DataSnapshot ds : snapshot.getChildren()) {

                    fecha = ds.getKey().toString();
                    System.out.println("Fichajes solicitados 1");
                    for (DataSnapshot ds1 : ds.getChildren()){
                        String correo = ds1.getKey().toString();
                        System.out.println(correo);
                        ClsFichaje fichaje = ds1.getValue(ClsFichaje.class);
                        System.out.println("Fichajes solicitados 2");
                        if (ClsUser.TipoUsuarioConectadoApp(getContext()).equals("2") && !fichaje.tipoUsuario.equals("2")){
                            crearUsuario(fichaje,correo,fecha);
                        } else if (ClsUser.TipoUsuarioConectadoApp(getContext()).equals("0")){
                            crearUsuario(fichaje,correo,fecha);
                        }

                    }

                }
                System.out.println("Empieza coger imagen");
                cogerIdImagen();
                System.out.println("Termina coger imagen");
                adaptador = new ClsAdaptadorHorarios(getActivity(),usuarios);
                listaDeUsuarios.setAdapter(adaptador);

                /*
                    Cada vez que se pulsa un fichaje preguntamos si quiere validarlo y si acepta se valida el fichaje.
                 */
                listaDeUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AlertDialog.Builder opciones = new AlertDialog.Builder(view.getContext());
                        opciones.setMessage("¿Quieres validar el fichaje? una vez validado el fichaje no podrá ser modificado")
                                .setTitle("Advertencia")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ClsFichaje fichaje = new ClsFichaje();
                                        fichaje.horaIni = usuarios.get(position).horaInicioJornada;
                                        fichaje.horaFin = usuarios.get(position).horaFinJornada;
                                        fichaje.horaIniDescanso = usuarios.get(position).horaInicioDescanso;
                                        fichaje.horaFinDescanso = usuarios.get(position).horaFinDescanso;
                                        validarUsuario(fichaje, usuarios.get(position).getCorreo(),usuarios.get(position).getFecha().replace("/",":"));
                                        usuarios.remove(position);
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("4");
            }
        });



    }

    /*
        Validamos el fichaje del usuario marcado.
        el fichaje lo agregamos en la tabla de fichajes y lo borramos de la tabla FichajesSolicitados
        Borramos todos los usuarios en el array list de usuarios para rellenarlo de nuevo quitando el fichaje que hemos validado ya.
     */
    public void validarUsuario (ClsFichaje fichaje, String correo, String dia){

        mDataBase.child("FichajesSolicitados").child(idGrupo).child(dia.trim()).child(correo).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error");
            }
        });
        mDataBase.child("fichaje").child(correo).child(dia).setValue(fichaje);
        for (int i = 0; usuarios.size() > i ; i++){
            usuarios.remove(i);
        }
        RellenarHorarios();
    }

    public void crearUsuario (ClsFichaje fichaje, String correo, String fecha){
        ClsUsuarioHorario usuario = new ClsUsuarioHorario();
        usuario.setNombre(nombre);
        usuario.sethoraInicioJornada(fichaje.horaIni);
        usuario.sethoraFinJornada(fichaje.horaFin);
        usuario.sethoraInicioDescanso(fichaje.horaIniDescanso);
        usuario.sethoraFinDescanso(fichaje.horaFinDescanso);
        usuario.setCorreo(correo);
        usuario.setFecha("  " + fecha.replace(":","/"));
        usuario.setNombre(fichaje.nombre);

        usuarios.add(usuario);
    }

    // Recorremos todos los usuarios guardados y si tienen imagen se lo agregamos
    public void cogerIdImagen (){

        for (int i = 0; grupoUsuarios.size() > i ; i++){
            for (int j = 0; usuarios.size() > j ; j++){
                if (grupoUsuarios.get(i).correoElectronico.equals(usuarios.get(j).correo.replace("_","."))){
                    usuarios.get(j).idImagen = grupoUsuarios.get(i).idImagen;
                }
            }
        }
    }

}