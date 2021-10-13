package com.example.controltime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PermisoActivity extends AppCompatActivity {
    TextView edtUsuarioApp;
    EditText edtFechaDesde;
    EditText edtFechaHasta;
    Spinner spnTipoPermiso;
    CheckBox chkMedioDia;
    ImageButton btnPermiso;
    ImageButton btnConsulta;
    ImageButton btnValidar;
    ImageButton btnValidarLista;

    private DatabaseReference mDataBase;
    private ListView listDatos;
    private ArrayList<String> datos;
    private ArrayList<HashMap> datosPer;

    TextView textTipoPer;
    long TipoPermiso;
    boolean esMedioDia;

    String Usuario;
    String FechaDesde;
    String FechaHasta;
    String Id;
    long RowId;
    int num;
    int suma;
    boolean existeFecha;
    boolean esta ;
    ArrayList<String> ArrayId= new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ClsPermisos objPermisos ;
    double Dias;
    String valor="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permiso);
        getSupportActionBar().hide();// quito la barra de arriba
        textTipoPer=(TextView) findViewById(R.id.textTipoPer);
        mDataBase = FirebaseDatabase.getInstance().getReference();

        java.util.Date fecha = new Date();

        /*MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/
        edtUsuarioApp=(TextView) findViewById(R.id.edtUsuarioApp);
        edtUsuarioApp.setText(User.UsuarioConectadoApp(getApplication()));
        Usuario=edtUsuarioApp.getText().toString().replace(".", "_").trim();
        /*FIN MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/

        /*Comprobamos el ultimo id metido para el usuario registrado*/
        RowId=UltimoId() ;
        Id=String.valueOf(RowId);
        /*FIN Comprobamos el ultimo id metido para el usuario registrado*/

        /* FECHAS */

        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        edtFechaDesde=(EditText) findViewById(R.id.edtFechaDesde);
        edtFechaDesde.setText(date);
        edtFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaSeleccionada(edtFechaDesde);
                edtFechaDesde.setText(edtFechaDesde.getText());

            }
        });


        edtFechaHasta=(EditText) findViewById(R.id.edtFechaHasta);
        edtFechaHasta.setText(date);
        edtFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaSeleccionada(edtFechaHasta);
                edtFechaHasta.setText( edtFechaHasta.getText());


            }
        });
        FechaDesde=edtFechaDesde.getText().toString();
        FechaHasta=edtFechaHasta.getText().toString();



       int valor=ComprobarFechas(edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString());

        /* FIN FECHAS */



        /*SELECCION DE MEDIO DIA*/
        chkMedioDia=(CheckBox) findViewById(R.id.chkMedioDia);
        chkMedioDia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    esMedioDia=true;
                    Dias=0.5;
                    edtFechaHasta.setText(edtFechaDesde.getText().toString());
                }else{
                    esMedioDia=false;
                    Dias=0.0;
                }
            }
        });
        /*FIN SELECCION DE MEDIO DIA*/

        /*Spinner Tipo Permisos*/
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


        /**
         * BOTON SOLICITAR PERMISOS*
         * Ocultamos los controles de la consulta
         *
         * */
        btnPermiso=(ImageButton) findViewById(R.id.btnPermiso);
        btnPermiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OcultarMostrarControles(true);

            }
        });
        /* FIN BOTON SOLICITAR PERMISOS*/

        /**
         * BOTON CONSULTAR PERMISOS
        /*
         * Ocultamos los controles de solicitar permisos
         *
         * */
        listDatos = (ListView) findViewById(R.id.listaDatos);

        btnConsulta=(ImageButton)findViewById(R.id.btnConsulta);
        btnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OcultarMostrarControles(false);
                LLenarLista(listDatos,Usuario);

               /* datosPer= new ArrayList<HashMap>();
                HashMap dato=new HashMap();
                dato.put("ID",1);
                dato.put("FECHA_DESDE","02/10/2021");
                datosPer.add(dato);
               AdapterListView adapter = new AdapterListView(PermisoActivity.this, datosPer);
               listDatos.setAdapter(adapter);*/

            }
        });
        /*FIN  BOTON CONSULTAR PERMISOS*/

/**
* BOTON VALIDAR LISTA
* */
        btnValidarLista =(ImageButton) findViewById(R.id.btnValidarLista);
        btnValidarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

/* FIN BOTON VALIDAR LISTA*/
        /**
         *  *BOTON VALIDAR graba el permiso solicitado
         * Grabamos el permiso solicitado
         * */
        btnValidar=(ImageButton) findViewById(R.id.btnValidar);
        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// GRABAMOS LOS DATOS DE LA SOLICITUD DE LOS PERMISOS

                //EL ESTADO VA A SER 0=PENDIENTE
                try {
                    if (esMedioDia){
                        edtFechaHasta.setText(edtFechaDesde.getText().toString());
                     }else{
                        Dias=Utils.getDiasSolicitados(edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString());
                    }
                        Toast.makeText(PermisoActivity.this," ID:" + Id,Toast.LENGTH_LONG).show();
                        ClsPermisos per = new ClsPermisos(Usuario   ,Dias,edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString(),TipoPermiso,0,RowId );
                        mDataBase = FirebaseDatabase.getInstance().getReference().child("Permisos").child (Usuario).child(Id);
                        mDataBase.setValue (per).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task2) {
                                if (task2.isSuccessful()) {
                                    Toast.makeText(PermisoActivity.this,"Permiso grabado correctamente , dias:" + Dias,Toast.LENGTH_LONG).show();
                                } else {
                                    Utils.MostrarMensajes(PermisoActivity.this, "NO SE HA PODIDO GRABAR EL PERMISO ", "GRABA USUARIO");
                                }
                            }
                        });
               } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        /*FIN BOTON VALIDAR*/
    }

    /****
     * Metodo que muestra el calendario y se le asigna el valor al editText
     */
    private  void getFechaSeleccionada(EditText fecha){

        Utils fragment =Utils.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fecha.setText(dayOfMonth + "/" + (month+1) + "/" + year);
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
            listDatos.setVisibility(View.INVISIBLE);
            btnValidarLista.setVisibility(View.INVISIBLE);
        }else{
            chkMedioDia.setVisibility(View.INVISIBLE);
            textTipoPer.setVisibility(View.INVISIBLE);
            spnTipoPermiso.setVisibility(View.INVISIBLE);
            btnValidar.setVisibility(View.INVISIBLE);
            listDatos.setVisibility(View.VISIBLE);
            btnValidarLista.setVisibility(View.VISIBLE);
        }

    }

    public void LLenarLista(ListView lista, String Usuario){

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        mDataBase = FirebaseDatabase.getInstance().getReference();
        Query query =mDataBase.child("Permisos").child(Usuario);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int cant=0;
                 for(DataSnapshot ds:snapshot.getChildren()){
                        ClsPermisos objPer = ds.getValue(ClsPermisos.class);

                       datos = new ArrayList<String>();
                        datos.add(String.valueOf(objPer.RowId));
                        datos.add(objPer.FechaDesde);
                        datos.add(objPer.FechaHasta);
                        datos.add(String.valueOf(objPer.dias));
                        adapter = new ArrayAdapter<String>(PermisoActivity.this,R.layout.support_simple_spinner_dropdown_item,datos);
                        listDatos.setAdapter(adapter);

/*
                     datosPer= new ArrayList<HashMap>();
                     HashMap dato=new HashMap();
                     dato.put("ID",objPer.RowId);
                     dato.put("Fecha_Desde",objPer.FechaDesde);
                     datosPer.add(dato);
                     AdapterListView adapte = new AdapterListView(PermisoActivity.this, datosPer);
                     listDatos.setAdapter(adapte);*/
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull   DatabaseError error) {

            }
        });

    }

    public int ComprobarFechas(String FechaIni,String FechaFin){
      //  existeFecha=false;

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        mDataBase = FirebaseDatabase.getInstance().getReference();
        Query query =mDataBase.child("Permisos").child(Usuario);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int cant=0;
                    for(DataSnapshot ds:snapshot.getChildren()){
                        ClsPermisos objPer = ds.getValue(ClsPermisos.class);
                        try {
                            Date dataDesde = formato.parse(objPer.FechaDesde );
                            Date dataHasta = formato.parse(objPer.FechaHasta);

                            Date Desde = formato.parse(FechaIni );
                            Date  Hasta = formato.parse(FechaFin);

                          if(  (dataDesde.compareTo(Desde) >= 0 && dataDesde.compareTo(Hasta) <= 0) ||(dataHasta.compareTo(Desde) >= 0 && dataHasta.compareTo(Hasta) <= 0)){
                              //existeFecha=true;
                              cant++;
                             // Toast.makeText(PermisoActivity.this,existeFecha +  " -- Fecha desde esta dentro de rango: " + objPer.RowId,Toast.LENGTH_LONG).show();
                          }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    suma=cant;
                //    Toast.makeText(PermisoActivity.this,"SUMA: "+  suma,Toast.LENGTH_LONG).show();
                }
            /*    if(suma>0){
                    existeFecha=true;
                }else{
                    existeFecha=false;
                }*/
                //esta=existeFecha;
            }
            @Override
            public void onCancelled(@NonNull   DatabaseError error) {

            }
        });
        return suma;
    }

    /**
     * Metdo que devuelve el   id para insertar
     * @return     ID
     */
   public Long UltimoId(){
        mDataBase = FirebaseDatabase.getInstance().getReference();
       Query query =mDataBase.child("Permisos").child(Usuario);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull  DataSnapshot snapshot) {
               if(snapshot.exists()){
              //     Toast.makeText(PermisoActivity.this,"Existe",Toast.LENGTH_LONG).show();
                   for(DataSnapshot ds:snapshot.getChildren()){
                      ArrayId.add(ds.getKey());
                   }
                   RowId=Integer.valueOf(ArrayId.get(ArrayId.size()-1))+1 ;
                   Id=String.valueOf(RowId);
                //   Toast.makeText(PermisoActivity.this,"ID: " + RowId,Toast.LENGTH_LONG).show();
               }

           }
         @Override
           public void onCancelled(@NonNull   DatabaseError error) {

           }
       });
        return RowId;
    }

}


