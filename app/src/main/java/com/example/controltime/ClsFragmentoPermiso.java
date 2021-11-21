package com.example.controltime;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import sun.bob.mcalendarview.MCalendarView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClsFragmentoPermiso#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClsFragmentoPermiso extends Fragment {


    EditText edtFechaDesde;
    EditText edtFechaHasta;
    Spinner spnTipoPermiso;
    CheckBox chkMedioDia;
    Button btnPermiso;
    Button btnValidar;

    MCalendarView calendarView;
    double Dias;
    boolean esMedioDia;
    //boolean existeFecha;
    //boolean esta ;
    int colorDia;
    int TipoPermiso;
    // int num;
    //int suma;
   // String Usuario;
    String FechaDesde;
    String FechaHasta;
    String Id;
    // String valor="";
    //long RowId;
    //ArrayList<String> ArrayId= new ArrayList<String>();
    List<ClsPermisos> ArrayPermisos;
    List<ClsTipoPermiso>arrayTiposPer= new ArrayList<>();
    ClsPermisos objPermisos ;
    ClsUser objUser;


    com.example.controltime.ClsTipoPermiso objtipoPermiso;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClsFragmentoPermiso() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClsFragmentoPermiso.
     */
    // TODO: Rename and change types and number of parameters
    public static ClsFragmentoPermiso newInstance(String param1, String param2) {
        ClsFragmentoPermiso fragment = new ClsFragmentoPermiso();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_cls_fragmento_permiso, container, false);





        objUser=new ClsUser();
        objPermisos=new ClsPermisos();
        ArrayPermisos=new ArrayList<>();
        arrayTiposPer=new ArrayList<>();



/*** *  FECHAS */

        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        edtFechaDesde=(EditText) vista.findViewById(R.id.edtFechaDesde);
        edtFechaDesde.setText(date);
        edtFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaSeleccionada(edtFechaDesde);
                edtFechaDesde.setText(edtFechaDesde.getText());
               // Toast.makeText(getContext(),"onClick Fechadesde" + edtFechaDesde.getText() ,Toast.LENGTH_LONG).show();
            }
        });


        edtFechaHasta=(EditText) vista.findViewById(R.id.edtFechaHasta);
        edtFechaHasta.setText(date);
        edtFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaSeleccionada(edtFechaHasta);
                edtFechaHasta.setText( edtFechaHasta.getText());
              //  Toast.makeText(getContext(),"onClick FechaHasta" + edtFechaHasta.getText() ,Toast.LENGTH_LONG).show();
            }
        });
        FechaDesde=edtFechaDesde.getText().toString();
        FechaHasta=edtFechaHasta.getText().toString();


/***FIN FECHAS */

/*** * CALENDARIO */
        calendarView=(MCalendarView) vista.findViewById(R.id.calendar);

/*** * FIN CALENDARIO     */

        /***SELECCION DE MEDIO DIA*/
        chkMedioDia=(CheckBox) vista.findViewById(R.id.chkMedioDia);
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
        spnTipoPermiso=(Spinner) vista.findViewById(R.id.spnTipoPermiso);
        cargaTiposDePermisos();
        ArrayAdapter<ClsTipoPermiso> adapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line,arrayTiposPer);
        spnTipoPermiso.setAdapter(adapter);

        spnTipoPermiso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoPermiso= (int) parent.getItemIdAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
/***FIN Spinner Tipo Permisos        */


        /*** BOTON VER PERMISOS*
         * mostramos los dias que tienen permisos solicitados en el calendario         *         * */
        btnPermiso=(Button) vista.findViewById(R.id.btnPermiso);
        btnPermiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargaPermisos(objUser.usuarioId,edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString());
                  for(int i=0;i<=ArrayPermisos.size()-1;i++){
                    try {
                      switch (ArrayPermisos.get(i).TipoPermiso){
                          case 0://""0":
                              colorDia=Color.rgb(247,218,56);
                              break;
                          case 1://""1":
                              colorDia=Color.rgb(125,129,127);
                              break;
                          case 2://"2":
                              colorDia=Color.rgb(176,39,169);
                              break;
                          case 3://"3":
                              colorDia=Color.rgb(39,89,176);
                              break;
                          case 4://"4":
                              colorDia=Color.rgb(255,87,34);
                              break;
                          case 5://"5":
                              colorDia=Color.rgb(76,175,80);
                              break;
                          case 6://"6":
                              colorDia=Color.rgb(176,114,39);

                              break;
                      }
                      String fechaIni= ArrayPermisos.get(i).FechaDesde.replace("-", "/") ;
                      String fechaFin= ArrayPermisos.get(i).FechaHasta.replace("-", "/") ;
                      SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                      Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));;
                      cal.setTime(formato.parse( fechaIni));
                      int year = cal.get(Calendar.YEAR);
                      int month = cal.get(Calendar.MONTH)+1;
                      int day = cal.get(Calendar.DAY_OF_MONTH);
                      cal.setTime(formato.parse(fechaFin));
                      int yearF = cal.get(Calendar.YEAR);
                      int monthF = cal.get(Calendar.MONTH)+1;
                      int dayF= cal.get(Calendar.DAY_OF_MONTH);
                      for (int y =year;y<= yearF;y++){
                          for (int m =month;m<= monthF;m++){
                              for (int d =day;d<= dayF;d++){
                                  calendarView.markDate(y,m,d).setMarkedStyle(1,colorDia);
                              }
                          }
                      }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                  }

            }
        });
        /** FIN BOTON VER PERMISOS*/

/** *  *BOTON VALIDAR graba el permiso solicitado
 * Grabamos el permiso solicitado    * */
        btnValidar=(Button) vista.findViewById(R.id.btnValidar);
        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// GRABAMOS LOS DATOS DE LA SOLICITUD DE LOS PERMISOS
                //EL ESTADO VA A SER 0=PENDIENTE

                    if (esMedioDia){
                        edtFechaHasta.setText(edtFechaDesde.getText().toString());
                    }

                    buscarUsuario (ClsUser.UsuarioConectadoApp(getContext()));
                    Toast.makeText(getContext(),"onClick Fechas" + edtFechaDesde.getText() +" - "+ edtFechaHasta.getText() ,Toast.LENGTH_LONG).show();
                    insertar();

            }
        });

        /***FIN BOTON VALIDAR*/
        return vista;
    }

    public void cargaTiposDePermisos (){
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
               if(DbConnection.conectarBaseDeDatos()){
                    arrayTiposPer=ClsTipoPermiso.getPermisos();
                    DbConnection.cerrarConexion();
               }
            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void cargaPermisos (int usuarioId,String fechaIni,String fechaFin){
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()){
                    ArrayPermisos=ClsPermisos.getPermisos(usuarioId,fechaIni,fechaFin);
                    DbConnection.cerrarConexion();
                }
            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




    public void buscarUsuario (String correo){
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()){
                    objUser = ClsUser.getUsuario(correo);
                    DbConnection.cerrarConexion();
                }

            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**** * Metodo que muestra el calendario y se le asigna el valor al editText  */
   private  void getFechaSeleccionada(EditText fecha){

        ClsUtils fragment =ClsUtils.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fecha.setText(dayOfMonth + "/" + (month+1) + "/" + year);
               // Toast.makeText(getContext(),"Fecha seleccionada " + fecha.getText(),Toast.LENGTH_LONG).show();
            }
        });
        fragment.show(getChildFragmentManager(),"datePicker");
    }

    public void insertar (){

        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {

                if(DbConnection.conectarBaseDeDatos()){

                    //COMPROBAMOS QUE NO HAYA UN PERMISO EN ESE INTERVALO DE FECHAS
                    if (!ClsPermisos.validaIntevalosFechas(objUser.usuarioId,edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString() )) {
                        if(!ClsPermisos.insertarPermiso(objUser.usuarioId,edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString(),TipoPermiso)){
                            Toast.makeText(getContext(),"No se ha podido insertar el permiso" ,Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getContext(),"Hay un  permiso dentro del rango de fechas seleccionado" ,Toast.LENGTH_LONG).show();
                    }

                    DbConnection.cerrarConexion();
                }else{
                    Toast.makeText(getContext(),"Error en la bbdd." ,Toast.LENGTH_LONG).show();
                }
            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}