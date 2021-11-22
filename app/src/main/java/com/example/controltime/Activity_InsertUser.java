package com.example.controltime;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
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
    TextView txtMensajeEmail;
    TextView txtIdGrupo;
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
    ClsTipoUsuario objTipoUsuario;
    ClsGrupos objGrupos;
    ClsGrupoXUsuarios objGXU;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    String Id;
    long RowId;
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
        spnIdGrupo=(Spinner)findViewById(R.id.spnIdGrupo);


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
                ClsUtils.MostrarMensajes(Activity_InsertUser.this,"\n1. Un caracter en minúscula \n2. Un caracter en mayúscula \n3. Un caracter especial \n4. Un número \n5. Sin espacios entre los caracteres de la contraseña,\n6. Mínimo 8 caracteres","La contraseña debe de contener al menos : ");
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
                                        Toast.makeText(getApplicationContext(),mensaje ,Toast.LENGTH_LONG).show();
                                    }else {
                                        //Intent intent = new Intent(getApplicationContext(), Activity_Login.class);
                                        //startActivity(intent);
                                        buscarUsuario(Email);
                                        ClsUser.UsuarioPreferencesApp(Email,Pass,usuario.usuarioId,getApplicationContext());

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
            e.printStackTrace();
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
                   if(!ClsUser.insertarUsuario(Nombre,Ape,Ape,Email,Grupo,TipoUsuario)){
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