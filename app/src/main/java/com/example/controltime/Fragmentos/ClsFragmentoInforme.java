package com.example.controltime.Fragmentos;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.controltime.Clases.ClsFichaje;
import com.example.controltime.Clases.ClsFicheroPDF;
import com.example.controltime.Clases.ClsPermisos;
import com.example.controltime.Clases.ClsUser;
import com.example.controltime.Clases.ClsUtils;
import com.example.controltime.Clases.DbConnection;
import com.example.controltime.R;
import com.google.firebase.database.DatabaseReference;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClsFragmentoInforme#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClsFragmentoInforme extends Fragment {
    String Usuario;
    TextView edtUsuarioApp;
    TextView edtGrupoApp;
    TextView edtTipoUsuarioApp;
    DatabaseReference mDataBase;


    Spinner spnUsuarios;
    String Grupo;
    String mensaje;
    EditText edtFechaDesde;
    EditText edtFechaHasta;
    String UsurioSeleccionado;
    Button GenerarPDFPermisos;
    Button GenerarPDFFichaje;
    ClsUser objUsuario;
    ClsFicheroPDF objPDF;
    ClsPermisos objPermisos;
    List<ClsPermisos> ArrayPermisos;

    List<ClsFichaje> ArrayFichajes;
    List<ClsUser>arrayUsuarios;
    int idUsuario;
    int idSeleccion;
    private final static String CARPETA_PDF_PERMISOS = "PDF_Permisos";
    private final static String CARPETA_PDF_fichajes = "PDF_Fichajes";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClsFragmentoInforme() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClsFragmentoInforme.
     */
    // TODO: Rename and change types and number of parameters
    public static ClsFragmentoInforme newInstance(String param1, String param2) {
        ClsFragmentoInforme fragment = new ClsFragmentoInforme();
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

        View vista = inflater.inflate(R.layout.fragment_cls_fragmento_informe, container, false);

        idUsuario= Integer.parseInt(ClsUser.UsuarioIdApp(getContext()));

        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

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





        spnUsuarios=(Spinner) vista.findViewById(R.id.spnUsuarios);
        GenerarPDFPermisos=(Button) vista.findViewById(R.id.btnGenerarPer);
        GenerarPDFFichaje=(Button) vista.findViewById(R.id.btnGenerarFich);
        objPDF=new ClsFicheroPDF();

        objUsuario=new ClsUser();
        objPermisos=new ClsPermisos();
        ArrayPermisos=new ArrayList<>();
        ArrayFichajes=new ArrayList<>();
        arrayUsuarios=new ArrayList<>();
       // List<ClsUser> Arrayusuario=new ArrayList<>();

        buscarUsuario(idUsuario);
        cargaUsuarios(objUsuario.Grupo,objUsuario.TipoUsuario);
        ArrayAdapter<ClsUser> adapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line,arrayUsuarios);
        spnUsuarios.setAdapter(adapter);
        spnUsuarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UsurioSeleccionado=parent.getItemAtPosition(position).toString();
                String[] splitUsuario=UsurioSeleccionado.split("-");
                  idSeleccion= Integer.parseInt(splitUsuario[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //FIN

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE )!=
                        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    1000);
        }
        /***BOTONES GENERAR PDF*/
        GenerarPDFPermisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargaPermisos(idSeleccion,edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString());
                String Nombre= idSeleccion+ "Permisos_" + edtFechaDesde.getText().toString().replace("/","") +"_" + edtFechaHasta.getText().toString().replace("/","") + ".pdf";
                objPDF=new ClsFicheroPDF(Nombre);
                if(ArrayPermisos.size()==0 ){
                    ClsUtils.MostrarMensajes(getContext(), "El empleado " +  UsurioSeleccionado + " no tiene ningún datos para generar el fichero", "", true, ClsUtils.actividadEnum.ERROR);
                }else {
                    ArrayFichajes.clear();
                    if (!objPDF.generarPDF(getContext(), ArrayPermisos, ArrayFichajes)) {
                        ClsUtils.MostrarMensajes(getContext(), "Ha habido un error al generar e pdf, intentelo mas tarde", "", true, ClsUtils.actividadEnum.ERROR);
                    } else {
                        //este mensaje lo meustro dentro del metodo para que me de la ruta
                        //ClsUtils.MostrarMensajes(getContext(), "", "", false, ClsUtils.actividadEnum.PDF);
                    }
                }
            }
        });


        GenerarPDFFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               cargaFichajes(idSeleccion,edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString()  );
                String Nombre= idSeleccion+ "Fichajes_" + edtFechaDesde.getText().toString().replace("/","") +"_" + edtFechaHasta.getText().toString().replace("/","") + ".pdf";
                objPDF=new ClsFicheroPDF(Nombre);
                if(ArrayFichajes.size()==0){
                    ClsUtils.MostrarMensajes(getContext(), "El empleado " +  UsurioSeleccionado + " no tiene ningún datos para generar el fichero", "", true, ClsUtils.actividadEnum.ERROR);
                }else {
                    ArrayPermisos.clear();
                    if (!objPDF.generarPDF(getContext(), ArrayPermisos, ArrayFichajes)) {
                        ClsUtils.MostrarMensajes(getContext(), "Ha habido un error al generar e pdf, intentelo mas tarde", "", true, ClsUtils.actividadEnum.ERROR);
                    } else {
                        //este mensaje lo meustro dentro del metodo para que me de la ruta
                        //ClsUtils.MostrarMensajes(getContext(), "", "", false, ClsUtils.actividadEnum.PDF);
                    }
                }
            }
        });
        /***FIN GENERAR PDF*******/
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
    public void buscarUsuario (int id){
        mensaje="";
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()){
                    objUsuario = ClsUser.getUsuario(id);
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
    public void cargaUsuarios (int departamentoId,int tipoUsuaioId){
        mensaje="";
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()){
                    arrayUsuarios=ClsUser.getUsuario(departamentoId,tipoUsuaioId,false);
                    DbConnection.cerrarConexion();
                }else{
                    mensaje="Ha ocurrido un error intentelo en unos minutos";
                    //Toast.makeText(getContext(),"Ha ocurrido un error intentelo en unos minutos",Toast.LENGTH_SHORT).show();
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



    public void cargaFichajes (int usuarioId,String fechaIni,String fechaFin){
        mensaje="";
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()){
                    ArrayFichajes=ClsFichaje.getFichajes (usuarioId,fechaIni,fechaFin);
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
}