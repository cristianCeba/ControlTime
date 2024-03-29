package com.example.controltime.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.controltime.Clases.ClsGrupos;
import com.example.controltime.Clases.ClsTipoUsuario;
import com.example.controltime.Clases.ClsUser;
import com.example.controltime.Clases.ClsUtils;
import com.example.controltime.Clases.DbConnection;
import com.example.controltime.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Activity_InsertUser extends AppCompatActivity {
    EditText txtNombre;
    EditText txtApe1;
    EditText txtEmail;
    EditText txtPass;
    EditText txtPass2;
    TextView txtMensajePass;
    TextView txtMensajeNombre;
    TextView txtMensajeApe;
    TextView txtMensajeEmail,textGrupo;

    Spinner spnTipoUsuario;
    Spinner spnGrupo;

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

    ArrayList<String> ArrayId= new ArrayList<String>();
    List<ClsTipoUsuario> arrayTipoUsuario=new ArrayList<>();
    List<ClsGrupos> arrayGrupo=new ArrayList<>();
    ClsUser usuario=new ClsUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_user);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        txtNombre = (EditText) findViewById(R.id.editTextName);
        txtMensajeNombre=(TextView) findViewById(R.id.textMensajeNombre);
        txtApe1 = (EditText) findViewById(R.id.editTextApe);
        txtMensajeApe=(TextView) findViewById(R.id.textMensajeApe);
        spnTipoUsuario=(Spinner)findViewById(R.id.spnTipo);
        spnGrupo=(Spinner)findViewById(R.id.spnGrupo);
        textGrupo=findViewById(R.id.textGrupo);


        txtEmail = (EditText) findViewById(R.id.editTextEmail);
        txtMensajeEmail=(TextView) findViewById(R.id.textMensajeEmail);
        txtPass = (EditText) findViewById(R.id.editTextPass);
        txtMensajePass=(TextView) findViewById(R.id.textMensajePass);
        btnInsert = (Button) findViewById(R.id.btnRegistro);
        txtPass2 = (EditText) findViewById(R.id.editTextPass2);
        btnInfo = findViewById(R.id.btnInfo);



        cargaTipoUsuario();
        ArrayAdapter<ClsTipoUsuario> adapter=new ArrayAdapter<>(getApplication(), android.R.layout.simple_dropdown_item_1line,arrayTipoUsuario);
        spnTipoUsuario.setAdapter(adapter);
        spnTipoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoUsuario=(int) parent.getItemIdAtPosition(position);
                if(TipoUsuario==0){
                    spnGrupo.setVisibility(View.INVISIBLE);
                    textGrupo.setVisibility(View.INVISIBLE);

                }else{
                    spnGrupo.setVisibility(View.VISIBLE);
                    textGrupo.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cargaGrupos();
        ArrayAdapter<ClsGrupos> adapterGrupo=new ArrayAdapter<>(getApplication(), android.R.layout.simple_dropdown_item_1line,arrayGrupo);
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
                ClsUtils.MostrarMensajes(Activity_InsertUser.this,"\n1. Un caracter en minúscula \n2. Un caracter en mayúscula \n3. Un caracter especial\n4. Un número\n5. Sin espacios entre los caracteres de la contraseña\n6. Mínimo 8 caracteres","",false,ClsUtils.actividadEnum.INFORMATIVO);

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
                                        ClsUtils.MostrarMensajes(Activity_InsertUser.this,"","",true,ClsUtils.actividadEnum.INSERTAR);
                                     //   Toast.makeText(getApplicationContext(),mensaje ,Toast.LENGTH_LONG).show();
                                    }else {
                                        //Intent intent = new Intent(getApplicationContext(), Activity_Login.class);
                                        //startActivity(intent);
                                        buscarUsuario(Email);
                                        ClsUser.UsuarioPreferencesApp(Email,Pass,usuario.usuarioId,getApplicationContext());
                                        ClsUtils.MostrarMensajes(Activity_InsertUser.this,"","",false,ClsUtils.actividadEnum.INSERTAR);
                                        Intent intent = new Intent(getApplicationContext(), Activity_Navegador.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
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
            mensaje="Ha ocurrido un error intentelo en unos minutos";
        }
    }

    public void cargaGrupos(){
        mensaje="";
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()){
                    arrayGrupo=ClsGrupos.getDepartamento();
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
            mensaje="Ha ocurrido un error intentelo en unos minutos";
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
            mensaje="Ha ocurrido un error intentelo en unos minutos";
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
            mensaje="Ha ocurrido un error intentelo en unos minutos";
        }
    }

}