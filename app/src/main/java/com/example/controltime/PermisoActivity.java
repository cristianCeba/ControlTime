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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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
    String Id;
    long RowId;

    ArrayList<String> ArrayId= new ArrayList<String>();
    double Dias;
    String valor="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permiso);
        getSupportActionBar().hide();// quito la barra de arriba
        textTipoPer=(TextView) findViewById(R.id.textTipoPer);


        /*MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/
        edtUsuarioApp=(TextView) findViewById(R.id.edtUsuarioApp);
        edtUsuarioApp.setText(User.UsuarioConectadoApp(getApplication()));
        Usuario=edtUsuarioApp.getText().toString().replace(".", "_").trim();
        /*FIN MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/

        /* FECHAS */
        edtFechaDesde=(EditText) findViewById(R.id.edtFechaDesde);
        edtFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaSeleccionada(edtFechaDesde);
                edtFechaDesde.setText(edtFechaDesde.getText());
            }
        });


        edtFechaHasta=(EditText) findViewById(R.id.edtFechaHasta);
        edtFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaSeleccionada(edtFechaHasta);
                edtFechaHasta.setText( edtFechaHasta.getText());
            }
        });
        FechaDesde=edtFechaDesde.getText().toString();
        FechaHasta=edtFechaHasta.getText().toString();
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


        /*BOTON SOLICITAR PERMISOS*
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

        /* BOTON CONSULTAR PERMISOS*/
        /*
         * Ocultamos los controles de solicitar permisos
         *
         * */
        btnConsulta=(ImageButton)findViewById(R.id.btnConsulta);
        btnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OcultarMostrarControles(false);
            }
        });
        /*FIN  BOTON CONSULTAR PERMISOS*/



        /**
         *  *BOTON VALIDAR
         * Grabamos el permiso solicitado
         * */
        btnValidar=(ImageButton) findViewById(R.id.btnValidar);
        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// GRABAMOS LOS DATOS DE LA SOLICITUD DE LOS PERMISOS

                //EL ESTADO VA A SER 0=PENDIENTE
                try {

                    Id=UltimoId();
                    if (esMedioDia){
                        edtFechaHasta.setText(edtFechaDesde.getText().toString());
                     }else{
                        Dias=Utils.getDiasSolicitados(edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString());
                    }
                        ClsPermisos per = new ClsPermisos(Usuario + " " ,Dias,edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString(),TipoPermiso,0,9 );
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

        }else{
            chkMedioDia.setVisibility(View.INVISIBLE);
            textTipoPer.setVisibility(View.INVISIBLE);
            spnTipoPermiso.setVisibility(View.INVISIBLE);
            btnValidar.setVisibility(View.INVISIBLE);
        }

    }

private String UltimoId(){
    int num;
  //  ArrayId.add("6");
  //  Toast.makeText(PermisoActivity.this,Usuario,Toast.LENGTH_LONG).show();

   Query query =mDataBase.child("Permisos").child(Usuario);
   query.addListenerForSingleValueEvent(new ValueEventListener() {
       @Override
       public void onDataChange(@NonNull  DataSnapshot snapshot) {
           for(DataSnapshot ds:snapshot.getChildren()){
              ArrayId.add(ds.getKey());
           }
       }

       @Override
       public void onCancelled(@NonNull   DatabaseError error) {

       }
   });
    num=Integer.valueOf(Collections.max(ArrayId))+1 ;
    return String.valueOf(num);
}

}


