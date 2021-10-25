package com.example.controltime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import java.sql.RowId;
import java.util.ArrayList;
import java.util.List;

public class InsertUserActivity extends AppCompatActivity {
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
    String TipoUsuario;
    String Grupo;
    Button btnInsert;
    ConexionNOOO conexion;
    Utils utils;
    TipoUsuario objTipoUsuario;
    Grupos objGrupos;
    GrupoXUsuarios objGXU;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    String Id;
    long RowId;
    ArrayList<String> ArrayId= new ArrayList<String>();

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

        objGXU=new GrupoXUsuarios();
        objGXU.CargarGrupoXUsuario(mDataBase,spnIdGrupo,InsertUserActivity.this);

        spnIdGrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RowId=Integer.valueOf(objGXU.getId())+1;
                Id= String.valueOf(RowId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        objTipoUsuario=new TipoUsuario();
        objTipoUsuario.CargarTipoUsuario(mDataBase,spnTipoUsuario,InsertUserActivity.this);
        spnTipoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoUsuario=parent.getItemAtPosition(position).toString();
                objTipoUsuario=new TipoUsuario(String.valueOf(id),TipoUsuario);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        objGrupos= new Grupos();
        objGrupos. CargarGrupo(mDataBase,spnGrupo,InsertUserActivity.this);
        spnGrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               Grupo= parent.getItemAtPosition(position).toString();
               objGrupos=new Grupos(String.valueOf(id),Grupo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.MostrarMensajes(InsertUserActivity.this,"\n1. Un caracter en minúscula \n2. Un caracter en mayúscula \n3. Un caracter especial \n4. Un número \n5. Sin espacios entre los caracteres de la contraseña,\n6. Mínimo 8 caracteres","La contraseña debe de contener al menos : ");
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMensajePass.setText("");
                txtMensajeEmail.setText("");
                txtMensajeApe.setText("");
                txtMensajeNombre.setText("");
                User user=new User();
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
                //ENTRO EN LA TABLA USER
                    Query query=mDataBase.child("users");
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for(DataSnapshot ds: snapshot.getChildren()){
                                    //comprobamos si el correo existe en la tabla users
                                    Query query1=mDataBase.child("users").child(Email.replace(".", "_").trim());
                                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull   DataSnapshot snapshot2) {
                                            if(snapshot2.exists()){// si el usuario existe no ppodemos continuar
                                                Utils.MostrarMensajes(InsertUserActivity.this, "EL USUARIO EXISTE"  , " USUARIO");
                                            }else{
                                               // SI NO EXISTE PODEMOS GRABAR EL NUEVO
                                                //INSERTO EL USUARIO EN AUTHENTICATION
                                                mAuth.createUserWithEmailAndPassword(Email, Pass)
                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {
                                                                   // GRABAMOS EN USERS
                                                                   User use = new User(Nombre, Ape, Email,objTipoUsuario.id ,objGrupos.id);
                                                                    String email = Email.replace(".", "_");
                                                                    mDataBase.child("users").child(email).setValue(use).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task2) {
                                                                            if (task2.isSuccessful()) {
                                                                                //  grabamos en GrupoXUsuarios
                                                                                GrupoXUsuarios GrupoUsuario=new GrupoXUsuarios(Id,Email,objGrupos.id,objTipoUsuario.id);
                                                                                mDataBase.child("GruposXUsuarios").child(Id).setValue(GrupoUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull  Task<Void> task) {
                                                                                        if(task.isSuccessful()){
                                                                                            Toast.makeText(InsertUserActivity.this,"Usuario grabado correctamente",Toast.LENGTH_LONG).show();
                                                                                            // VOLVEMOS A LA PANTALLA DE LOGIN
                                                                                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                                                                            startActivity(intent);
                                                                                        }
                                                                                    }
                                                                                });


                                                                            /*   Toast.makeText(InsertUserActivity.this,"Usuario grabado correctamente",Toast.LENGTH_LONG).show();
                                                                               // VOLVEMOS A LA PANTALLA DE LOGIN
                                                                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                                                                startActivity(intent);*/
                                                                            } else {

                                                                                Utils.MostrarMensajes(InsertUserActivity.this, "NO SE HA PODIDO GRABAR EL USUARIO ", "GRABA USUARIO");
                                                                            }
                                                                        }
                                                                    });
                                                                }

                                                            }
                                                        });
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                }

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {

                        }
                    });

                }
            }
        });
    }




}