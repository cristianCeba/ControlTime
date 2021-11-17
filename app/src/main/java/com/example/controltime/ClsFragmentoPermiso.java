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

    TextView edtUsuarioApp;
    TextView edtGrupoApp;
    TextView edtTipoUsuarioApp;

    EditText edtFechaDesde;
    EditText edtFechaHasta;
    Spinner spnTipoPermiso;
    CheckBox chkMedioDia;

    Button btnPermiso;
    ImageButton btnConsulta;
    Button btnValidar;
    int colorDia;
    MCalendarView calendarView;
    // CalendarView calendarView2;
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
    List<ClsPermisos> ArrayPermisos;
    int num;
    int suma;
    boolean existeFecha;
    boolean esta ;

    ClsPermisos objPermisos ;
    ClsUser objUser;
    double Dias;
    String valor="";
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
        textTipoPer=(TextView) vista.findViewById(R.id.textTipoPer);
        mDataBase = FirebaseDatabase.getInstance().getReference();

        Date fecha = new Date();
        /*** * MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/
        edtUsuarioApp=(TextView) vista.findViewById(R.id.edtUsuarioApp);
        edtUsuarioApp.setText(ClsUser.UsuarioConectadoApp(getContext()) );
        Usuario=edtUsuarioApp.getText().toString().replace(".", "_").trim();
        ClsGrupos objGrupo= new ClsGrupos();
        ClsTipoUsuario objTipo = new ClsTipoUsuario();
        edtTipoUsuarioApp=(TextView)vista.findViewById(R.id.edtTipoUsuarioApp);
        edtGrupoApp=(TextView) vista.findViewById(R.id.edtGrupoApp);
        objGrupo.GetNombreGrupoXId(mDataBase, edtGrupoApp,ClsUser.GruposuarioConectadoApp(getContext()));
        objTipo.GetTipoXId(mDataBase,edtTipoUsuarioApp,ClsUser.TipoUsuarioConectadoApp(getContext()));
        /**FIN MOSTRAMOS EL USUARIO QUE ESTA CONECTADO*/
        // LLenarLista(Usuario,calendarView);
/***  * Comprobamos el ultimo id metido para el usuario registrado*/
        RowId=UltimoId() ;
        Id=String.valueOf(RowId);
        /*FIN Comprobamos el ultimo id metido para el usuario registrado*/

        objUser=new ClsUser();
        String[] nombreCompleto;
        nombreCompleto=  objUser.GetNombreYApellido(mDataBase,Usuario);

        objPermisos=new ClsPermisos();
        ArrayPermisos=new ArrayList<>();
        ArrayPermisos= objPermisos.ListaPermisosPorUsuario(getContext(),Usuario);
        // ClsUtils.MostrarMensajes(Activity_Permiso.this, "cantidad: " + ArrayPermisos.size(), "TOTAL PERMISOS POR USUARIO ");

/*** *  FECHAS */

        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        edtFechaDesde=(EditText) vista.findViewById(R.id.edtFechaDesde);
        edtFechaDesde.setText(date);
        edtFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaSeleccionada(edtFechaDesde);
                edtFechaDesde.setText(edtFechaDesde.getText());

            }
        });


        edtFechaHasta=(EditText) vista.findViewById(R.id.edtFechaHasta);
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
        calendarView=(MCalendarView) vista.findViewById(R.id.calendar);
        //calendarView2=(CalendarView)findViewById(R.id.calendar2);
        //long diaSeleccionado=calendarView2.getDate();
        //Toast.makeText(PermisoActivity.this,""+  calendarView.getMarkedDates(),Toast.LENGTH_LONG).show();
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
        objtipoPermiso=new ClsTipoPermiso();
        objtipoPermiso.CargarTipoPermisos(mDataBase,spnTipoPermiso,getContext());
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
         * mostramos los dias que tienen permisos solicitados en el calendario         *         * */
        btnPermiso=(Button) vista.findViewById(R.id.btnPermiso);
        btnPermiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LLenarLista(Usuario,calendarView);

                //ClsPermisos objPermisos=new ClsPermisos();
                //   ArrayPermisos=new ArrayList<>();
                // ArrayPermisos= objPermisos.ListaPermisosPorUsuario(Activity_Permiso.this,Usuario);
                //ClsUtils.MostrarMensajes(Activity_Permiso.this, "cantidad: " + ArrayPermisos.size(), "TOTAL PERMISOS POR USUARIO ");
                //ArrayAdapter<ClsPermisos> arrayAdapter= new ArrayAdapter<>(Activity_Permiso.this, android.R.layout.simple_dropdown_item_1line,ArrayPermisos);
                //spnTipoPermiso.setAdapter(arrayAdapter);
            }
        });
        /** FIN BOTON SOLICITAR PERMISOS*/

/** *  *BOTON VALIDAR graba el permiso solicitado
 * Grabamos el permiso solicitado    * */
        btnValidar=(Button) vista.findViewById(R.id.btnValidar);
        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// GRABAMOS LOS DATOS DE LA SOLICITUD DE LOS PERMISOS
                //EL ESTADO VA A SER 0=PENDIENTE
                try {
                    if (esMedioDia){
                        edtFechaHasta.setText(edtFechaDesde.getText().toString());
                    }else{
                        Dias=1+ClsUtils.getDiasSolicitados(edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString());
                    }
                    Toast.makeText(getContext()," ID:" + Id,Toast.LENGTH_LONG).show();
                    //ClsPermisos per = new ClsPermisos(edtUsuarioApp.getText().toString()   ,Dias,edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString(),objtipoPermiso.Tipo,0,RowId );
                    ClsPermisos per = new ClsPermisos(nombreCompleto[0],Dias,edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString(),objtipoPermiso.Tipo,0,RowId );
                    per.TipoUsuario = ClsUser.TipoUsuarioConectadoApp(getContext());
                    per.GrupoUsuario = ClsUser.GruposuarioConectadoApp(getContext());

                    mDataBase = FirebaseDatabase.getInstance().getReference().child("Permisos").child (Usuario).child(Id);
                    mDataBase.setValue(per).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {
                                Toast.makeText(getContext(),"Permiso grabado correctamente , dias:" + Dias,Toast.LENGTH_LONG).show();

                                /***  * Comprobamos el ultimo id metido para el usuario registrado*/
                                RowId=UltimoId() ;
                                Id=String.valueOf(RowId);
                                /***FIN Comprobamos el ultimo id metido para el usuario registrado*/
                            } else {
                                ClsUtils.MostrarMensajes(getContext(), "NO SE HA PODIDO GRABAR EL PERMISO ", "GRABA USUARIO");
                            }
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        /***FIN BOTON VALIDAR*/
        return vista;
    }
    /**** * Metodo que muestra el calendario y se le asigna el valor al editText  */
    private  void getFechaSeleccionada(EditText fecha){

        ClsUtils fragment =ClsUtils.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fecha.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        });
        fragment.show(getFragmentManager(),"datePicker");
    }


    //, CalendarView calendario2
    public void LLenarLista(String Usuario, MCalendarView calendario)   {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        mDataBase = FirebaseDatabase.getInstance().getReference();
        Query query =mDataBase.child("Permisos").child(Usuario);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int cant=0;
                    for(DataSnapshot ds:snapshot.getChildren()){
                        ClsPermisos objPer = ds.getValue(ClsPermisos.class);


                        try {
                            switch (objPer.TipoPermiso){
                                case "Vacaciones"://""0":
                                    colorDia= Color.rgb(247,218,56);
                                    break;
                                case "Baja Enf."://""1":
                                    colorDia=Color.rgb(125,129,127);
                                    break;
                                case "Fallecimiento F."://"2":
                                    colorDia=Color.rgb(176,39,169);
                                    break;
                                case "Hospitalización F."://"3":
                                    colorDia=Color.rgb(39,89,176);
                                    break;
                                case "Boda"://"4":
                                    colorDia=Color.rgb(255,87,34);
                                    break;
                                case "Boda F."://"5":
                                    colorDia=Color.rgb(76,175,80);
                                    break;
                                case "Baja"://"6":
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
            public void onCancelled(@NonNull DatabaseError error) {

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