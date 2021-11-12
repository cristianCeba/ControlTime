package com.example.controltime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lowagie.text.DocumentException;

import java.io.IOException;
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
    String UsurioSeleccionado;
    Button GenerarPDFPermisos;
    Button GenerarPDFFichaje;
    ClsFicheroPDF objPDF;
    private final static String CARPETA_PDF_PERMISOS = "PDF_Permisos";
    private final static String CARPETA_PDF_fichajes = "PDF_Fichajes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        edtFechaDesde=(EditText)findViewById(R.id.edtFechaDesde);
        edtFechaHasta=(EditText)findViewById(R.id.edtFechaHasta);
        spnUsuario=(Spinner) findViewById(R.id.spnUsaurio);
        GenerarPDFPermisos=(Button) findViewById(R.id.btnGenerarPer);
        GenerarPDFFichaje=(Button) findViewById(R.id.btnGenerarFich);
        objPDF=new ClsFicheroPDF();

        usuario=new ClsUser();
        List<ClsUser> Arrayusuario=new ArrayList<>();

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


        Arrayusuario=   usuario.ListaUsuarios(Activity_Informe.this );
        ArrayAdapter<ClsUser> arrayAdapter= new ArrayAdapter<>(Activity_Informe.this, android.R.layout.simple_dropdown_item_1line,Arrayusuario);
        spnUsuario.setAdapter(arrayAdapter);


        // cargar el spinner por usuario tipo y grupos**********************

        if(ClsUser.TipoUsuarioConectadoApp(getApplication()).equals("0") || ClsUser.TipoUsuarioConectadoApp(getApplication()).equals("3")){
            // administrador o director ,carga todos los usuarios
            Arrayusuario=   usuario.ListaUsuarios(Activity_Informe.this );
              arrayAdapter= new ArrayAdapter<>(Activity_Informe.this, android.R.layout.simple_dropdown_item_1line,Arrayusuario);
            spnUsuario.setAdapter(arrayAdapter);
        }else{
            // jefe , carga solo los de su grupo
            if(ClsUser.TipoUsuarioConectadoApp(getApplication()).equals("2")){
                //carga por el grupo al que pertenece , con los usuarios de tipo 1
                Arrayusuario=   usuario.ListaUsuariosPorGrupoYTipo(Activity_Informe.this,ClsUser.GruposuarioConectadoApp(getApplication()),"1");
                 arrayAdapter= new ArrayAdapter<>(Activity_Informe.this, android.R.layout.simple_dropdown_item_1line,Arrayusuario);
                spnUsuario.setAdapter(arrayAdapter);
            }
        }
        spnUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UsurioSeleccionado=parent.getItemAtPosition(position).toString();
                ClsUtils.MostrarMensajes(Activity_Informe.this  , "USUARIO : " + UsurioSeleccionado, "TOTAL PERMISOS POR USUARIO ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //FIN

        /***BOTONES GENERAR PDF*/
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},1000);
        }else{

        }
        GenerarPDFPermisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String Nombre=UsurioSeleccionado +"_"+ edtFechaDesde.getText() +"_" + edtFechaHasta.getText();
                    objPDF=new ClsFicheroPDF(CARPETA_PDF_fichajes,Nombre);
                    objPDF.generarPDF();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
        GenerarPDFFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String Nombre=UsurioSeleccionado +"_"+ edtFechaDesde.getText() +"_" + edtFechaHasta.getText();
                    objPDF=new ClsFicheroPDF(CARPETA_PDF_fichajes,Nombre);
                    objPDF.generarPDF();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
        /***FIN GENERAR PDF*******/


    }
}