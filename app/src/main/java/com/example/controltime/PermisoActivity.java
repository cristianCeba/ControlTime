package com.example.controltime;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import sun.bob.mcalendarview.MCalendarView;

public class PermisoActivity extends AppCompatActivity {
    TextView edtUsuarioApp;
    EditText edtFechaDesde;
    EditText edtFechaHasta;
    Spinner spnTipoPermiso;
    CheckBox chkMedioDia;

    Button btnPermiso;
    ImageButton btnConsulta;
    ImageButton btnValidar;
    int colorDia;
    MCalendarView calendarView;
    private DatabaseReference mDataBase;
    TextView textTipoPer;
    String TipoPermiso;
    boolean esMedioDia;

    String Usuario;
    String FechaDesde;
    String FechaHasta;
    String Id;
    long RowId;
    ArrayList<String> ArrayId= new ArrayList<String>();
    int num;
    int suma;
    boolean existeFecha;
    boolean esta ;

    ClsPermisos objPermisos ;
    double Dias;
    String valor="";
    com.example.controltime.ClsTipoPermiso objtipoPermiso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permiso);
        getSupportActionBar().hide();// quito la barra de arriba
        textTipoPer=(TextView) findViewById(R.id.textTipoPer);
        mDataBase = FirebaseDatabase.getInstance().getReference();

        java.util.Date fecha = new Date();

/*** * MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/
        edtUsuarioApp=(TextView) findViewById(R.id.edtUsuarioApp);
        edtUsuarioApp.setText(User.UsuarioConectadoApp(getApplication()));
        Usuario=edtUsuarioApp.getText().toString().replace(".", "_").trim();
        /*FIN MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/
       //LLenarLista(Usuario,calendarView);
/***  * Comprobamos el ultimo id metido para el usuario registrado*/
        RowId=UltimoId() ;
        Id=String.valueOf(RowId);
        /*FIN Comprobamos el ultimo id metido para el usuario registrado*/

/*** *  FECHAS */

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

       int valor=0;//ComprobarFechas(edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString());
/***FIN FECHAS */

/*** * CALENDARIO */
     calendarView=(MCalendarView) findViewById(R.id.calendar);

/*** * FIN CALENDARIO     */

 /***SELECCION DE MEDIO DIA*/
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
 /***FIN SELECCION DE MEDIO DIA*/

/***Spinner Tipo Permisos*/
        spnTipoPermiso=(Spinner) findViewById(R.id.spnTipoPermiso);
        objtipoPermiso=new ClsTipoPermiso();
       objtipoPermiso.CargarTipoPermisos(mDataBase,spnTipoPermiso,PermisoActivity.this);
        spnTipoPermiso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoPermiso=parent.getItemAtPosition(position).toString();
                objtipoPermiso=new  ClsTipoPermiso(String.valueOf(id),parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
/***FIN Spinner Tipo Permisos        */


 /*** BOTON SOLICITAR PERMISOS*
         * Ocultamos los controles de la consulta         *         * */
        btnPermiso=(Button) findViewById(R.id.btnPermiso);
        btnPermiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  OcultarMostrarControles(true);
                LLenarLista(Usuario,calendarView);


            }
        });
 /** FIN BOTON SOLICITAR PERMISOS*/

 /**  * BOTON CONSULTAR PERMISOS
        /*  * Ocultamos los controles de solicitar permisos   *   *
          btnConsulta=(ImageButton)findViewById(R.id.btnConsulta);
        btnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   OcultarMostrarControles(false);
                LLenarLista(Usuario);
            }
        });*/
/**FIN  BOTON CONSULTAR PERMISOS*/



/** *  *BOTON VALIDAR graba el permiso solicitado
         * Grabamos el permiso solicitado    * */
        btnValidar=(ImageButton) findViewById(R.id.btnValidar);
        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// GRABAMOS LOS DATOS DE LA SOLICITUD DE LOS PERMISOS

                //EL ESTADO VA A SER 0=PENDIENTE
                try {
                    if (esMedioDia){
                        edtFechaHasta.setText(edtFechaDesde.getText().toString());
                     }else{
                        Dias=1+Utils.getDiasSolicitados(edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString());
                    }
                        Toast.makeText(PermisoActivity.this," ID:" + Id,Toast.LENGTH_LONG).show();
                        //ClsPermisos per = new ClsPermisos(edtUsuarioApp.getText().toString()   ,Dias,edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString(),TipoPermiso,0,RowId );
                        ClsPermisos per = new ClsPermisos(edtUsuarioApp.getText().toString()   ,Dias,edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString(),objtipoPermiso.id,0,RowId );
                        mDataBase = FirebaseDatabase.getInstance().getReference().child("Permisos").child (Usuario).child(Id);
                        mDataBase.setValue (per).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task2) {
                                if (task2.isSuccessful()) {
                                    Toast.makeText(PermisoActivity.this,"Permiso grabado correctamente , dias:" + Dias,Toast.LENGTH_LONG).show();
                                    LLenarLista(Usuario,calendarView);
                                    /***  * Comprobamos el ultimo id metido para el usuario registrado*/
                                    RowId=UltimoId() ;
                                    Id=String.valueOf(RowId);
                                    /*FIN Comprobamos el ultimo id metido para el usuario registrado*/
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

 /***FIN BOTON VALIDAR*/
    }

/**** * Metodo que muestra el calendario y se le asigna el valor al editText  */
    private  void getFechaSeleccionada(EditText fecha){

        Utils fragment =Utils.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fecha.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        });
        fragment.show(getSupportFragmentManager(),"datePicker");
    }
 /***  * Metodo para Mostrar/Ocultar conrtoles


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

    }*/

    public void LLenarLista( String Usuario,MCalendarView calendario)   {

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
                         switch (objPer.TipoPermiso){
                             case "0":
                                 colorDia=Color.rgb(247,218,56);
                                 break;
                             case "1":
                                 colorDia=Color.rgb(125,129,127);
                                 break;
                             case "2":
                                 colorDia=Color.rgb(176,39,169);
                                 break;
                             case "3":
                                 colorDia=Color.rgb(39,89,176);
                                 break;
                             case "4":
                                 colorDia=Color.rgb(255,87,34);
                                 break;
                             case "5":
                                 colorDia=Color.rgb(76,175,80);
                                 break;
                             case "6":
                                 colorDia=Color.rgb(176,114,39);

                                 break;
                         }




                        Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));;
                        cal.setTime(formato.parse( objPer.FechaDesde));
                         int year = cal.get(Calendar.YEAR);
                         int month = cal.get(Calendar.MONTH)+1;
                         int day = cal.get(Calendar.DAY_OF_MONTH);
                         cal.setTime(formato.parse( objPer.FechaHasta));
                         int yearF = cal.get(Calendar.YEAR);
                         int monthF = cal.get(Calendar.MONTH)+1;
                         int dayF= cal.get(Calendar.DAY_OF_MONTH);
                         for (int y =year;y<= yearF;y++){
                             for (int m =month;m<= monthF;m++){
                                 for (int d =day;d<= dayF;d++){
                           // Toast.makeText(PermisoActivity.this,"" + d + "/"+ m+"/"+y , Toast.LENGTH_SHORT).show();

                                    calendario.markDate(y,m,d).setMarkedStyle(1,colorDia);

                                 }
                             }
                         }
                     } catch (ParseException e) {
                         e.printStackTrace();
                     }


                    }
                }
            }
            @Override
            public void onCancelled(@NonNull   DatabaseError error) {

            }
        });

    }

    /***METODO QUE MIRA SI LAS FECHAS INTRODUCIDAS ESTAN YA PEDIDAS
     *
     * @param FechaIni Fecha comienzo
     * @param FechaFin fecja fin
     * @return
     */
    public int ComprobarFechas(String FechaIni,String FechaFin){

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
                              cant++;
                          }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    suma=cant;
                }
            }
            @Override
            public void onCancelled(@NonNull   DatabaseError error) {

            }
        });
        return suma;
    }

  /** * Metdo que devuelve el   id + 1 para insertar
     * @return     ID
     */
   public Long UltimoId(){
        mDataBase = FirebaseDatabase.getInstance().getReference();
       Query query =mDataBase.child("Permisos").child(Usuario);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull  DataSnapshot snapshot) {
               if(snapshot.exists()){
                   for(DataSnapshot ds:snapshot.getChildren()){
                      ArrayId.add(ds.getKey());
                   }
                   RowId=Integer.valueOf(ArrayId.get(ArrayId.size()-1))+1 ;
                   Id=String.valueOf(RowId);
               }

           }
         @Override
           public void onCancelled(@NonNull   DatabaseError error) {

           }
       });
        return RowId;
    }

}


