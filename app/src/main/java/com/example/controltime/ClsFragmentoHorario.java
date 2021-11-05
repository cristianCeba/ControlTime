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
    private ListView listView;
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

        listaDeUsuarios = view.findViewById(R.id.listusuariohorario);

        Query query = mDataBase.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            /**
             * Buscamos en base de datos si el usuario ha registrado algún fichaje del día, y dependiendo de lo que el usuario ha registrado
             * habilitamos y deshabilitamos los botones de fichajes.
             */
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ClsUser user = ds.getValue(ClsUser.class);
                    if (ds.getKey().equals(usuarioAplicacion)) {
                        idGrupo = user.getGrupo();
                        nombre = user.Nombre + " " + user.Ape;
                        RellenarHorarios();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("4 --");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void RellenarHorarios (){
        Query query = mDataBase.child("FichajesSolicitados").child(idGrupo);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            /**
             * Buscamos en base de datos los fichajes pendientes de aprobar por el administrador del grupo.
             */
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String fecha;
                for (DataSnapshot ds : snapshot.getChildren()) {

                    fecha = ds.getKey().toString();

                    for (DataSnapshot ds1 : ds.getChildren()){

                        String correo = ds1.getKey().toString();
                        System.out.println(correo);
                        ClsFichaje fichaje = ds1.getValue(ClsFichaje.class);
                        cogerIdGrupo(ds1.getKey().toString());
                        ClsUsuarioHorario usuario = new ClsUsuarioHorario();
                        System.out.println("nombre --> " + nombre);
                        usuario.setNombre(nombre);
                        usuario.sethoraInicioJornada(fichaje.horaIni);
                        System.out.println("horaIni --> " + fichaje.horaIni);
                        usuario.sethoraFinJornada(fichaje.horaFin);
                        usuario.sethoraInicioDescanso(fichaje.horaIniDescanso);
                        usuario.sethoraFinDescanso(fichaje.horaFinDescanso);
                        usuario.setCorreo(correo);
                        usuario.setFecha("  " + fecha.replace(":","/"));

                        usuarios.add(usuario);
                    }

                }

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

    public void cogerIdGrupo (String usuario){
        System.out.println("1.1");
        Query query = mDataBase.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            /**
             * Buscamos al usuario para sacar el grupo en el que está en la aplicación.
             */
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    ClsUser user = ds.getValue(ClsUser.class);

                    if (ds.getKey().equals(usuario)) {

                        idGrupo = user.getGrupo();
                        nombre = user.Nombre + " " + user.Ape;
                    }
                }

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

}