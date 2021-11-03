package com.example.controltime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_Informe extends AppCompatActivity {
    String Usuario;
    TextView edtUsuarioApp;
    private DatabaseReference mDataBase;
    User usuario;
    Spinner spnUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);
        mDataBase = FirebaseDatabase.getInstance().getReference();


        usuario=new User();

        /*** * MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/
        edtUsuarioApp=(TextView) findViewById(R.id.edtUsuarioApp);
        edtUsuarioApp.setText(User.UsuarioConectadoApp(getApplication()) );
        //
        Usuario=edtUsuarioApp.getText().toString().replace(".", "_").trim();
        edtUsuarioApp.setText(User.UsuarioConectadoApp(getApplication())+ "           GRUPO: " + usuario.getGrupo() );
        /*FIN MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/
        usuario.GrupoUsuario(mDataBase,Usuario);

        spnUsuario=(Spinner) findViewById(R.id.spnUsaurio);
        usuario.CargarUsuariosPorGrupo(mDataBase,spnUsuario,Activity_Informe.this,Usuario);
        spnUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Activity_Informe.this,parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}