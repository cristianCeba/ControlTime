package com.example.controltime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Activity_Informe extends AppCompatActivity {
    String Usuario;
    TextView edtUsuarioApp;
    TextView edtGrupoApp;
    TextView edtTipoUsuarioApp;
    DatabaseReference mDataBase;


    Spinner spnUsuarios;
    String Grupo;
    EditText edtFechaDesde;
    EditText edtFechaHasta;
    String UsurioSeleccionado;
    Button GenerarPDFPermisos;
    Button GenerarPDFFichaje;
    ClsUser objUsuario;
    ClsFicheroPDF objPDF;
    ClsPermisos objPermisos;
    List<ClsPermisos>ArrayPermisos;
    private final static String CARPETA_PDF_PERMISOS = "PDF_Permisos";
    private final static String CARPETA_PDF_fichajes = "PDF_Fichajes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);
        mDataBase = FirebaseDatabase.getInstance().getReference();


        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        edtFechaHasta=(EditText) findViewById(R.id.edtFechaHasta);
        edtFechaHasta.setText(date);
        edtFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaSeleccionada(edtFechaHasta);
                edtFechaHasta.setText( edtFechaHasta.getText());
            }
        });

        edtFechaDesde=(EditText) findViewById(R.id.edtFechaDesde);
        edtFechaDesde.setText(date);
        edtFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaSeleccionada(edtFechaDesde);
                edtFechaDesde.setText(edtFechaDesde.getText());
            }
        });



        spnUsuarios=(Spinner) findViewById(R.id.spnUsuarios);
        GenerarPDFPermisos=(Button) findViewById(R.id.btnGenerarPer);
        GenerarPDFFichaje=(Button) findViewById(R.id.btnGenerarFich);
        objPDF=new ClsFicheroPDF();

        objUsuario=new ClsUser();
        objPermisos=new ClsPermisos();
        ArrayPermisos=new ArrayList<>();
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


        // cargar el spinner por usuario tipo y grupos**********************
        if(ClsUser.TipoUsuarioConectadoApp(getApplication()).equals("0") || ClsUser.TipoUsuarioConectadoApp(getApplication()).equals("3")){
            // administrador o director ,carga todos los usuarios
            objUsuario.ListaUsuarios(Activity_Informe.this ,spnUsuarios);
        }else{
            // jefe , carga solo los de su grupo
            if(ClsUser.TipoUsuarioConectadoApp(getApplication()).equals("2")){
                //carga por el grupo al que pertenece , con los usuarios de tipo 1
                objUsuario.ListaUsuariosPorGrupoYTipo(Activity_Informe.this,ClsUser.GruposuarioConectadoApp(getApplication()),"1",spnUsuarios);
            }
        }
        spnUsuarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UsurioSeleccionado=parent.getItemAtPosition(position).toString();
              //  ClsUtils.MostrarMensajes(Activity_Informe.this  , edtFechaDesde.getText().toString() + " _ " + edtFechaHasta.getText().toString(), "PERMISOS");
                ArrayPermisos= objPermisos.ListaPermisosPorUsuario(Activity_Informe.this,UsurioSeleccionado.replace(".","_"));
               //ArrayPermisos=objPermisos.ListaPermisosPorUsuarioYFechas(Activity_Informe.this,UsurioSeleccionado.replace(".","_"),edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
         //FIN

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE )!=
                        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    1000);
        }
        /***BOTONES GENERAR PDF*/
        GenerarPDFPermisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  ArrayPermisos= objPermisos.ListaPermisosPorUsuario(Activity_Informe.this,UsurioSeleccionado.replace(".","_"));
                //ArrayPermisos=objPermisos.ListaPermisosPorUsuarioYFechas(Activity_Informe.this,UsurioSeleccionado.replace(".","_"),edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString());

               //if(ArrayPermisos.size()>0){
                    String Nombre= UsurioSeleccionado + edtFechaDesde.getText().toString().replace("/","") +"_" + edtFechaHasta.getText().toString().replace("/","") + ".pdf";
                    objPDF=new ClsFicheroPDF(CARPETA_PDF_PERMISOS,Nombre);
                    objPDF.generarPDF(Activity_Informe.this,ArrayPermisos);
               // }else{
                 //   ClsUtils.MostrarMensajes(Activity_Informe.this  , "No hay datos para las fechas y el usuario seleccionados" + edtFechaDesde.getText().toString() + " _ " + edtFechaHasta.getText().toString(), "PERMISOS  USUARIO ");
              //  }

            }
        });
        GenerarPDFFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String Nombre= UsurioSeleccionado + edtFechaDesde.getText().toString().replace("/","") +"_" + edtFechaHasta.getText().toString().replace("/","") + ".pdf";
                  //  objPDF=new ClsFicheroPDF(CARPETA_PDF_fichajes,Nombre);
                  //  objPDF.generarPDF(Activity_Informe.this,ArrayPermisos);


            }
        });
        /***FIN GENERAR PDF*******/
    }

    /**** * Metodo que muestra el calendario y se le asigna el valor al editText  */
    private  void getFechaSeleccionada(EditText fecha){

        ClsUtils fragment =ClsUtils.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fecha.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        });
        fragment.show(getSupportFragmentManager(),"datePicker");
    }
}