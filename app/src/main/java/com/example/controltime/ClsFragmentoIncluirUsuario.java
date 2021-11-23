package com.example.controltime;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClsFragmentoIncluirUsuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClsFragmentoIncluirUsuario extends Fragment {

    EditText txtNombre;
    EditText txtApe1;
    EditText txtEmail;
    EditText txtPass;
    EditText txtPass2;
    TextView txtMensajePass;
    TextView txtMensajeNombre;
    TextView txtMensajeApe;
    TextView txtMensajeEmail;
    Spinner spnTipoUsuario;
    Spinner spnGrupo;
    Spinner spnIdGrupo;
    ImageButton btnInfo;

    String Nombre;
    String Ape;
    String Email;
    String Pass;
    String Pass2;
    int TipoUsuario;
    int Grupo;
    Button btnInsert;
    String mensaje;
    ClsUtils utils;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    List<ClsTipoUsuario> arrayTipoUsuario=new ArrayList<>();
    List<ClsGrupos> arrayGrupo=new ArrayList<>();
    ClsUser usuario=new ClsUser();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClsFragmentoIncluirUsuario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClsFragmentoIncluirUsuario.
     */
    // TODO: Rename and change types and number of parameters
    public static ClsFragmentoIncluirUsuario newInstance(String param1, String param2) {
        ClsFragmentoIncluirUsuario fragment = new ClsFragmentoIncluirUsuario();
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
        View vista = inflater.inflate(R.layout.fragment_cls_fragmento_incluir_usuario, container, false);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        txtNombre = (EditText) vista.findViewById(R.id.editTextName);
        txtMensajeNombre=(TextView) vista.findViewById(R.id.textMensajeNombre);
        txtApe1 = (EditText) vista.findViewById(R.id.editTextApe);
        txtMensajeApe=(TextView) vista.findViewById(R.id.textMensajeApe);
        spnTipoUsuario=(Spinner)vista.findViewById(R.id.spnTipo);
        spnGrupo=(Spinner)vista.findViewById(R.id.spnGrupo);
        spnIdGrupo=(Spinner)vista.findViewById(R.id.spnIdGrupo);


        txtEmail = (EditText) vista.findViewById(R.id.editTextEmail);
        txtMensajeEmail=(TextView) vista.findViewById(R.id.textMensajeEmail);
        txtPass = (EditText) vista.findViewById(R.id.editTextPass);
        txtMensajePass=(TextView) vista.findViewById(R.id.textMensajePass);
        btnInsert = (Button) vista.findViewById(R.id.btnRegistro);
        txtPass2 = (EditText) vista.findViewById(R.id.editTextPass2);
        btnInfo = vista.findViewById(R.id.btnInfo);



        cargaTipoUsuario();
        ArrayAdapter<ClsTipoUsuario> adapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line,arrayTipoUsuario);
        spnTipoUsuario.setAdapter(adapter);
        spnTipoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoUsuario=(int) parent.getItemIdAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cargaGrupos();
        ArrayAdapter<ClsGrupos> adapterGrupo=new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line,arrayGrupo);
        spnGrupo.setAdapter(adapterGrupo);
        spnGrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Grupo= (int) parent.getItemIdAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClsUtils.MostrarMensajes(getContext(),"\n1. Un caracter en minúscula \n2. Un caracter en mayúscula \n3. Un caracter especial \n4. Un número \n5. Sin espacios entre los caracteres de la contraseña,\n6. Mínimo 8 caracteres","La contraseña debe de contener al menos : ");
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMensajePass.setText("");
                txtMensajeEmail.setText("");
                txtMensajeApe.setText("");
                txtMensajeNombre.setText("");
                ClsUser user=new ClsUser();
                Nombre=txtNombre.getText().toString();
                Ape=txtApe1.getText().toString();
                Email=txtEmail.getText().toString();
                Pass=txtPass.getText().toString();
                Pass2=txtPass2.getText().toString();
                if(Nombre.isEmpty()){
                    txtMensajeNombre.setText("Por favor, introduce el nombre");
                    txtNombre.requestFocus();
                }else if(Ape.isEmpty()){
                    txtMensajeApe.setText("Por favor, introduce el apellido");
                    txtApe1.requestFocus();
                }else if(Email.isEmpty()){
                    txtMensajeEmail.setText("Por favor, introduce el email");
                    txtEmail.requestFocus();
                }else if(!utils.validarEmail(txtEmail.getText().toString())){
                    txtMensajeEmail.setText("El correo no es válido");
                    txtEmail.requestFocus();
                }else if(Pass.isEmpty()){
                    txtMensajePass.setText("Por favor, introduce la contraseña");
                    txtPass.requestFocus();

                }else if (!utils.validarContraseña(Pass)){
                    txtMensajePass.setText("la contraseña no es válida");
                    txtPass2.requestFocus();
                } else if(Pass2.isEmpty()){
                    txtMensajePass.setText("Por favor, introduce otra vez la contraseña");
                    txtPass2.requestFocus();
                }else if (!Pass2.equals(Pass) )
                {
                    txtMensajePass.setText("la contraseña es diferente");
                    txtPass2.requestFocus();
                }else {
                    //comprobamos si existe en ct_usuario
                    buscarUsuario(Email);
                    if(usuario.usuarioId==0) {
                        //INSERTO EL USUARIO EN AUTHENTICATION
                        mAuth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //insertamos en ct_usuarios
                                    insertar();
                                    if(mensaje!=""){
                                        Toast.makeText(getContext(),mensaje ,Toast.LENGTH_LONG).show();
                                    }else {
                                       Toast.makeText(getContext(),"Usuario creado correctamente",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
        return vista;
    }

    public void cargaTipoUsuario (){
        mensaje="";
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()){
                    arrayTipoUsuario=ClsTipoUsuario.getTipoUsuario();
                    DbConnection.cerrarConexion();
                }else{
                    mensaje="Ha ocurrido un error intentelo en unos minutos";
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

    public void cargaGrupos() {
        mensaje = "";
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if (DbConnection.conectarBaseDeDatos()) {
                    arrayGrupo = ClsGrupos.getDepartamento();
                    DbConnection.cerrarConexion();
                } else {
                    mensaje = "Ha ocurrido un error intentelo en unos minutos";
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

    public void buscarUsuario (String correo){
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()){
                    usuario = ClsUser.getUsuario(correo);
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

    public void insertar (){
        mensaje="";
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {

                if(DbConnection.conectarBaseDeDatos()){
                    if(!ClsUser.insertarUsuario(Nombre,Ape,Email,Grupo,TipoUsuario)){
                        mensaje="Ha ocurrido un error al grabar el usuario";
                    }
                    DbConnection.cerrarConexion();

                }else{
                    mensaje="Ha ocurrido un error intentelo en unos minutos";
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