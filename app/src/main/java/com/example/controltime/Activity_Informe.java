package com.example.controltime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        edtGrupoApp=(TextView) findViewById(R.id.edtGrupoApp);
        spnUsuario=(Spinner) findViewById(R.id.spnUsaurio);
        edtTipoUsuarioApp=(TextView)findViewById(R.id.edtTipoUsuarioApp);
        /*** * MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/
        edtUsuarioApp=(TextView) findViewById(R.id.edtUsuarioApp);
        edtUsuarioApp.setText(ClsUser.UsuarioConectadoApp(getApplication()) );
        Usuario=edtUsuarioApp.getText().toString().replace(".", "_").trim();

        edtTipoUsuarioApp.setText("TIPO: " + ClsUser.TipoUsuarioConectadoApp(getApplication()));
        edtGrupoApp.setText("DEPARTAMENTO: " + ClsUser.GruposuarioConectadoApp(getApplication()));

        ClsGrupos objGrupo= new ClsGrupos();
        objGrupo.GetNombreGrupo(ClsUser.GruposuarioConectadoApp(getApplication()));
        Toast.makeText(getApplicationContext(),objGrupo.Grupo.toString(),Toast.LENGTH_LONG).show();



        /*FIN MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/
        // busca el grupo al que pertenece el usuario y cargar el spinner con los usuarios de ese grupo
        usuario=new ClsUser();
        //usuario.GetGrupoXUsuario(mDataBase,Usuario,edtGrupoApp,Activity_Informe.this,spnUsuario);
        usuario.CargarUsuariosPorGrupo(spnUsuario,Activity_Informe.this,ClsUser.GruposuarioConectadoApp(getApplication()));
        //FIN

        spnUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(Activity_Informe.this,parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}