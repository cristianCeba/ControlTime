package com.example.controltime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Activity_Informe extends AppCompatActivity {
    String Usuario;
    TextView edtUsuarioApp;
    TextView edtGrupoApp;
    TextView edtTipoUsuarioApp;
    DatabaseReference mDataBase;
    ClsUser usuario;
    Spinner spnUsuario;
    String Grupo;
    EditText edtFechaDesde;
    EditText edtFechaHasta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        edtFechaDesde=(EditText)findViewById(R.id.edtFechaDesde);
        edtFechaHasta=(EditText)findViewById(R.id.edtFechaHasta);
        spnUsuario=(Spinner) findViewById(R.id.spnUsaurio);

        /*** * MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/
        edtUsuarioApp=(TextView) findViewById(R.id.edtUsuarioApp);
        edtUsuarioApp.setText(ClsUser.UsuarioConectadoApp(getApplication()) );
        Usuario=edtUsuarioApp.getText().toString().replace(".", "_").trim();
        ClsGrupos objGrupo= new ClsGrupos();
        ClsTipoUsuario objTipo = new ClsTipoUsuario();
        edtTipoUsuarioApp=(TextView)findViewById(R.id.edtTipoUsuarioApp);
        edtGrupoApp=(TextView) findViewById(R.id.edtGrupoApp);
        objGrupo.GetNombreGrupoXId(mDataBase, edtGrupoApp,ClsUser.GruposuarioConectadoApp(getApplication()));
        objTipo.GetTipoXId(mDataBase,edtTipoUsuarioApp,ClsUser.TipoUsuarioConectadoApp(getApplication()));
        /**FIN MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/


        usuario=new ClsUser();
        List<ClsUser> Arrayusuario=new ArrayList<>();
        if(ClsUser.TipoUsuarioConectadoApp(getApplication()).equals("0") || ClsUser.TipoUsuarioConectadoApp(getApplication()).equals("3")){
            // administrador o director ,carga todos los usuarios
            Arrayusuario=   usuario.ListaUsuarios(Activity_Informe.this );
            ArrayAdapter<ClsUser> arrayAdapter= new ArrayAdapter<>(Activity_Informe.this, android.R.layout.simple_dropdown_item_1line,Arrayusuario);
            spnUsuario.setAdapter(arrayAdapter);
        }else{
            // jefe , carga solo los de su grupo
            if(ClsUser.TipoUsuarioConectadoApp(getApplication()).equals("2")){
                //carga por el grupo al que pertenece , con los usuarios de tipo 1
                Arrayusuario=   usuario.ListaUsuariosPorGrupoYTipo(Activity_Informe.this,ClsUser.GruposuarioConectadoApp(getApplication()),"1");
                ArrayAdapter<ClsUser> arrayAdapter= new ArrayAdapter<>(Activity_Informe.this, android.R.layout.simple_dropdown_item_1line,Arrayusuario);
                spnUsuario.setAdapter(arrayAdapter);
            }
        }
        spnUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //FIN




    }
}