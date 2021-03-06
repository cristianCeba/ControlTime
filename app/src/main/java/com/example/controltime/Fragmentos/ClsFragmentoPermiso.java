package com.example.controltime.Fragmentos;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.controltime.Clases.ClsPermisos;
import com.example.controltime.Clases.ClsTipoPermiso;
import com.example.controltime.Clases.ClsUser;
import com.example.controltime.Clases.ClsUtils;
import com.example.controltime.Clases.DbConnection;
import com.example.controltime.R;
import com.google.firebase.database.collection.LLRBNode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.vo.DateData;

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
    String mensaje;
    // String valor="";
    //long RowId;
    //ArrayList<String> ArrayId= new ArrayList<String>();
    List<ClsPermisos> ArrayPermisos;
    List<ClsTipoPermiso>arrayTiposPer= new ArrayList<>();
    ClsPermisos objPermisos ;
    ClsUser objUser;


    ClsTipoPermiso objtipoPermiso;

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
        mensaje="";
        buscarUsuario (ClsUser.UsuarioConectadoApp(getContext()));


/*** *  FECHAS */

        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        edtFechaDesde=(EditText) vista.findViewById(R.id.edtFechaDesde);
        edtFechaDesde.setText(date);
        edtFechaDesde.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    edtFechaDesde.setInputType(InputType.TYPE_NULL);
                    getFechaSeleccionada(edtFechaDesde);
                    edtFechaDesde.clearFocus();
                }
            }
        });



        edtFechaHasta=(EditText) vista.findViewById(R.id.edtFechaHasta);
        edtFechaHasta.setText(date);
        edtFechaHasta.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    edtFechaHasta.setInputType(InputType.TYPE_NULL);
                    getFechaSeleccionada(edtFechaHasta);
                    edtFechaHasta.clearFocus();
                }
            }
        });
        FechaDesde=edtFechaDesde.getText().toString();
        FechaHasta=edtFechaHasta.getText().toString();







/***FIN FECHAS */

/*** * CALENDARIO */
        calendarView=(MCalendarView) vista.findViewById(R.id.calendar);


        Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));;
        cal.setTime( new Date());
        int year = cal.get(Calendar.YEAR);
        LlenaPermisos( objUser.usuarioId,"01/01/"+year ,edtFechaHasta.getText().toString() );
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
       /* btnPermiso=(Button) vista.findViewById(R.id.btnPermiso);
        btnPermiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              LlenaPermisos( objUser.usuarioId,edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString() );

            }
        });*/



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
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    if (sdf.parse(edtFechaDesde.getText().toString()).compareTo(sdf.parse(edtFechaHasta.getText().toString())) > 0) {
//fecha hasta menos que desde
                        ClsUtils.MostrarMensajes( getContext(),  "La fecha hasta no puede ser menor que la fecha desde","",false,ClsUtils.actividadEnum.INFORMATIVO );

                    }  else{
                        insertar();
                        if(  mensaje!=""){
                            ClsUtils.MostrarMensajes(getContext(),mensaje,"",true,ClsUtils.actividadEnum.PERMISO);
                        }else
                        {
                            ClsUtils.MostrarMensajes(getContext(),mensaje,"",false,ClsUtils.actividadEnum.PERMISO);
                            Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));;
                            cal.setTime( new Date());
                            int year = cal.get(Calendar.YEAR);
                            LlenaPermisos( objUser.usuarioId,"01/01/"+year ,edtFechaHasta.getText().toString() );
                        }
                    }
                    //
                    //System.out.println(sdf.parse(edtFechaDesde.getText().toString()).before(sdf.parse(edtFechaHasta.getText().toString())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }



            }
        });

        /***FIN BOTON VALIDAR*/
        return vista;
    }


    private void LlenaPermisos(int usuarioId,String desde , String hasta){
        cargaPermisos(usuarioId,desde,hasta);
        boolean repetido = false;
        calendarView.getMarkedDates().removeAdd();
        for(int i=0;i<=ArrayPermisos.size()-1;i++){
            try {
                // colorDia=-1;

                String fechaIni= ArrayPermisos.get(i).FechaDesde.replace("-", "/") ;
                String fechaFin= ArrayPermisos.get(i).FechaHasta.replace("-", "/") ;

                fechaIni=ClsUtils.formatearFecha(fechaIni,true);
                fechaFin=ClsUtils.formatearFecha(fechaFin,true);
                fechaIni=fechaIni.replace("-", "/");
                fechaFin=fechaFin.replace("-", "/");

                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));;
                cal.setTime( formato.parse(fechaIni));
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH)+1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                cal.setTime(formato.parse(fechaFin));
                int yearF = cal.get(Calendar.YEAR);
                int monthF = cal.get(Calendar.MONTH)+1;
                int dayF= cal.get(Calendar.DAY_OF_MONTH);
                repetido = false;
                for (int y =year;y<= yearF;y++){
                    for (int m =month;m<= monthF;m++){
                        for (int d =day;d<= dayF;d++){
                            switch (ArrayPermisos.get(i).TipoPermiso){
                                case 0: //Vacacions

                                    if (ArrayPermisos.get(i).Estado==0){
                                        System.out.println("ponemos color gris el dia --> " + day);
                                      //  Toast.makeText(getContext(),"VACACIONES- COLOR GRIS  - ESTADO 0 Dia: " + d,Toast.LENGTH_LONG ).show();
                                        colorDia = Color.rgb(205,201,199);
                                    }else{
                                        System.out.println("ponemos color de vacaciones el dia --> " + day);
                                      //  Toast.makeText(getContext(),"VACACIONES-  COLOR AMARILLO ,ESTADO 1 Dia: " + d,Toast.LENGTH_LONG ).show();
                                        colorDia=Color.rgb(247,218,56);
                                    }
                                    calendarView.markDate(y,m,d).setMarkedStyle(4,colorDia);
                                    if(!repetido){
                                        calendarView.markDate(y,m,d).unMarkDate(y,m,d);
                                        repetido = true;
                                    }
                                    //calendarView.markDate(y,m,d).setMarkedStyle(2,colorDia);
                                    break;
                                case 1://Baja
                                    if (ArrayPermisos.get(i).Estado==0){
                                        System.out.println("ponemos color gris el dia --> " + day);
                                     //   Toast.makeText(getContext(),"BAJA- COLOR GRIS  - ESTADO 0 Dia: " + d,Toast.LENGTH_LONG ).show();
                                        colorDia = Color.rgb(205,201,199);
                                    }else {
                                        System.out.println("ponemos color de baja el dia --> " + day);
                                    //    Toast.makeText(getContext(),"BAJA - COLOR MORADO  - ESTADO 1 Dia: " + d,Toast.LENGTH_LONG ).show();
                                        colorDia = Color.rgb(176, 39, 169);
                                    }
                                    calendarView.markDate(y,m,d).setMarkedStyle(1,colorDia);
                                    if(!repetido){
                                        calendarView.markDate(y,m,d).unMarkDate(y,m,d);
                                        repetido = true;
                                    }
                                    break;
                                case 2://Hospitalizacion
                                    if (ArrayPermisos.get(i).Estado==0){
                                        System.out.println("ponemos color gris el dia --> " + day);
                                        colorDia = Color.rgb(205,201,199);
                                    }else {
                                        System.out.println("ponemos color de Hospitalizacion el dia --> " + day);
                                        colorDia = Color.rgb(39, 89, 176);
                                    }
                                    calendarView.markDate(y,m,d).setMarkedStyle(1,colorDia);
                                    if(!repetido){
                                        calendarView.markDate(y,m,d).unMarkDate(y,m,d);
                                        repetido = true;
                                    }
                                    break;
                                case 3://Otros
                                    if (ArrayPermisos.get(i).Estado==0){
                                        colorDia = Color.rgb(205,201,199);
                                    }else {
                                        colorDia = Color.rgb(255, 87, 34);
                                    }
                                    calendarView.markDate(y,m,d).setMarkedStyle(1,colorDia);
                                    if(!repetido){
                                        calendarView.markDate(y,m,d).unMarkDate(y,m,d);
                                        repetido = true;
                                    }
                                    break;
                            }

                        }
                    }
                }

            } catch (ParseException e) {
                ClsUtils.MostrarMensajes(getContext(),e.getMessage(),"",true,ClsUtils.actividadEnum.ERROR);
            }

        }
    }

    public void cargaTiposDePermisos (){
        mensaje="";
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
               if(DbConnection.conectarBaseDeDatos()){
                    arrayTiposPer=ClsTipoPermiso.getPermisos();
                    DbConnection.cerrarConexion();
               }else{
                   mensaje="Ha ocurrido un error intentelo en unos minutos";
               }
            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            mensaje="Ha ocurrido un error intentelo en unos minutos";
        }
    }

    public void cargaPermisos (int usuarioId,String fechaIni,String fechaFin){
        mensaje="";
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()){
                    ArrayPermisos=ClsPermisos.getPermisos(usuarioId,fechaIni,fechaFin);
                    DbConnection.cerrarConexion();
                }else{
                    mensaje="Ha ocurrido un error intentelo en unos minutos";

                }
            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            mensaje="Ha ocurrido un error intentelo en unos minutos";
        }
    }




    public void buscarUsuario (String correo){
        mensaje="";
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()){
                    objUser = ClsUser.getUsuario(correo);
                    DbConnection.cerrarConexion();
                }else{
                    mensaje="Ha ocurrido un error intentelo en unos minutos";
                }

            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            mensaje="Ha ocurrido un error intentelo en unos minutos";
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
        mensaje="";
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {

                if(DbConnection.conectarBaseDeDatos()){

                    try {
                        // comprobamos el total de dias
                        double dias= ClsUtils.calculaDiasHabiles( edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString());
                        if(dias>0){
                            //COMPROBAMOS QUE NO HAYA UN PERMISO EN ESE INTERVALO DE FECHAS
                            if (!ClsPermisos.validaIntevalosFechas(objUser.usuarioId,edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString() )) {
                                if(!ClsPermisos.insertarPermiso(objUser.usuarioId,edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString(),TipoPermiso,dias)){
                                    mensaje="No se ha podido insertar el permiso";
                                }
                            }else{

                                mensaje="Hay un  permiso dentro del rango de fechas seleccionado";
                            }

                        }else
                            {
                            mensaje="El/los dias seleccionados son festivos";
                        }

                        DbConnection.cerrarConexion();
                    } catch (ParseException e) {
                        mensaje="Ha ocurrido un error intentelo en unos minutos";

                    }

                }else{
                    mensaje="Ha ocurrido un error intentelo en unos minutos";
                }
            }
        });
        h1.start();
        try {
            h1.join();
        } catch (InterruptedException e) {
            mensaje="Ha ocurrido un error intentelo en unos minutos";
        }
    }
}