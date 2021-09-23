package com.example.controltime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;

public class PermisoActivity extends AppCompatActivity {
    TextView edtUsuarioApp;
    EditText edtFechaDesde;
    EditText edtFechaHasta;
    Spinner spnTipoPermiso;
    CheckBox chkMedioDia;
    ImageButton btnPermiso;
    ImageButton btnConsulta;
    ImageButton btnValidar;
    private DatabaseReference mDataBase;
    TextView textTipoPer;
    long TipoPermiso;
    boolean esMedioDia;

    String Usuario;
    String FechaDesde;
    String FechaHasta;
    double Dias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permiso);
        getSupportActionBar().hide();// quito la barra de arriba
        textTipoPer=(TextView) findViewById(R.id.textTipoPer);



        /*MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/
        edtUsuarioApp=(TextView) findViewById(R.id.edtUsuarioApp);
        Usuario=edtUsuarioApp.getText().toString();
        edtUsuarioApp.setText(User.UsuarioConectadoApp(getApplication()));
        /*FIN MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/

        /* FECHAS */
        edtFechaDesde=(EditText) findViewById(R.id.edtFechaDesde);
        FechaDesde=edtFechaDesde.getText().toString();
        edtFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaSeleccionada(edtFechaDesde);
            }
        });


        edtFechaHasta=(EditText) findViewById(R.id.edtFechaHasta);
        FechaHasta=edtFechaHasta.getText().toString();
        edtFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaSeleccionada(edtFechaHasta);
            }
        });
        /* FIN FECHAS */

        /*SELECCION DE MEDIO DIA*/
        chkMedioDia=(CheckBox) findViewById(R.id.chkMedioDia);
        chkMedioDia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    esMedioDia=true;
                    Dias=0.5;
                }else{
                    esMedioDia=false;
                    Dias=0.0;
                }
            }
        });
        /*FIN SELECCION DE MEDIO DIA*/

       /*Spinner Tipo Permisos        */
        spnTipoPermiso=(Spinner) findViewById(R.id.spnTipoPermiso);
        spnTipoPermiso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoPermiso=id;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*FIN Spinner Tipo Permisos        */

        /* BOTON SOLICITAR PERMISOS*/
        btnPermiso=(ImageButton) findViewById(R.id.btnPermiso);
        btnPermiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OcultarMostrarControles(true);
            }
        });
        /* FIN BOTON SOLICITAR PERMISOS*/

        /* BOTON CONSULTAR PERMISOS*/
        btnConsulta=(ImageButton)findViewById(R.id.btnConsulta);
        btnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OcultarMostrarControles(false);
            }
        });
        /*FIN  BOTON CONSULTAR PERMISOS*/


        /*BOTON VALIDAR*/
        btnValidar=(ImageButton) findViewById(R.id.btnValidar);
        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // GRABAMOS LOS DATOS DE LA SOLICITUS DE LOS PERMISOS

                String usuario = Usuario.replace(".", "_");
               // Toast.makeText(PermisoActivity.this,usuario,Toast.LENGTH_LONG).show();
                try {
                    Utils.MostrarMensajes(PermisoActivity.this, usuario + " DIAS : " + Utils.SumaDias(FechaDesde,FechaHasta) , " USUARIO");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ClsPermisos per = new ClsPermisos(usuario + " " ,0,FechaDesde,FechaHasta,TipoPermiso );

                mDataBase = FirebaseDatabase.getInstance().getReference().child("Permisos").child (usuario);
                mDataBase.setValue (per).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task2) {
                        if (task2.isSuccessful()) {
                            Toast.makeText(PermisoActivity.this,"Permiso grabado correctamente",Toast.LENGTH_LONG).show();

                        } else {

                            Utils.MostrarMensajes(PermisoActivity.this, "NO SE HA PODIDO GRABAR EL PERMISO ", "GRABA USUARIO");
                        }
                    }
                });


            }
        });

        /*FIN BOTON VALIDAR*/
    }

    /****
     * Metodo que muestra el calendario
     * @param edtFecha muestra la fecha seleccionada en el editText
     */
    private void getFechaSeleccionada(final EditText edtFecha){
        Utils fragment =Utils.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final String valor=dayOfMonth + "/" + (month+1) + "/" + year;
                edtFechaDesde.setText(valor);
            }
        });
        fragment.show(getSupportFragmentManager(),"datePicker");
    }

    /***
     * Metodo para Mostrar/Ocultar conrtoles
     * @param EsVisible 0 =VISIBLE
     *                     4=INVISIBLE
     */
    private void OcultarMostrarControles( boolean EsVisible){
        if(EsVisible){
            chkMedioDia.setVisibility(View.VISIBLE);
            textTipoPer.setVisibility(View.VISIBLE);
            spnTipoPermiso.setVisibility(View.VISIBLE);
            btnValidar.setVisibility(View.VISIBLE);

        }else{
            chkMedioDia.setVisibility(View.INVISIBLE);
            textTipoPer.setVisibility(View.INVISIBLE);
            spnTipoPermiso.setVisibility(View.INVISIBLE);
            btnValidar.setVisibility(View.INVISIBLE);
        }

    }
}