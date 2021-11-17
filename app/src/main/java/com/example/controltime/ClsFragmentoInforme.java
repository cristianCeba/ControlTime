package com.example.controltime;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    EditText edtFechaDesde;
    EditText edtFechaHasta;
    String UsurioSeleccionado;
    Button GenerarPDFPermisos;
    Button GenerarPDFFichaje;
    ClsUser objUsuario;
    ClsFicheroPDF objPDF;
    ClsPermisos objPermisos;
    List<ClsPermisos> ArrayPermisos;
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
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_cls_fragmento_informe, container, false);
        mDataBase = FirebaseDatabase.getInstance().getReference();


        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        edtFechaHasta=(EditText) vista.findViewById(R.id.edtFechaHasta);
        edtFechaHasta.setText(date);
        edtFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaSeleccionada(edtFechaHasta);
                edtFechaHasta.setText( edtFechaHasta.getText());
            }
        });

        edtFechaDesde=(EditText) vista.findViewById(R.id.edtFechaDesde);
        edtFechaDesde.setText(date);
        edtFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaSeleccionada(edtFechaDesde);
                edtFechaDesde.setText(edtFechaDesde.getText());
            }
        });



        spnUsuarios=(Spinner) vista.findViewById(R.id.spnUsuarios);
        GenerarPDFPermisos=(Button) vista.findViewById(R.id.btnGenerarPer);
        GenerarPDFFichaje=(Button) vista.findViewById(R.id.btnGenerarFich);
        objPDF=new ClsFicheroPDF();

        objUsuario=new ClsUser();
        objPermisos=new ClsPermisos();
        ArrayPermisos=new ArrayList<>();
        List<ClsUser> Arrayusuario=new ArrayList<>();

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


        // cargar el spinner por usuario tipo y grupos**********************
        if(ClsUser.TipoUsuarioConectadoApp(getContext()).equals("0") || ClsUser.TipoUsuarioConectadoApp(getContext()).equals("3")){
            // administrador o director ,carga todos los usuarios
            objUsuario.ListaUsuarios(getContext() ,spnUsuarios);
        }else{
            // jefe , carga solo los de su grupo
            if(ClsUser.TipoUsuarioConectadoApp(getContext()).equals("2")){
                //carga por el grupo al que pertenece , con los usuarios de tipo 1
                objUsuario.ListaUsuariosPorGrupoYTipo(getContext(),ClsUser.GruposuarioConectadoApp(getContext()),"1",spnUsuarios);
            }
        }
        spnUsuarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UsurioSeleccionado=parent.getItemAtPosition(position).toString();
                //  ClsUtils.MostrarMensajes(Activity_Informe.this  , edtFechaDesde.getText().toString() + " _ " + edtFechaHasta.getText().toString(), "PERMISOS");
                ArrayPermisos= objPermisos.ListaPermisosPorUsuario(getContext(),UsurioSeleccionado.replace(".","_"));
                //ArrayPermisos=objPermisos.ListaPermisosPorUsuarioYFechas(Activity_Informe.this,UsurioSeleccionado.replace(".","_"),edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString());
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
                //  ArrayPermisos= objPermisos.ListaPermisosPorUsuario(Activity_Informe.this,UsurioSeleccionado.replace(".","_"));
                //ArrayPermisos=objPermisos.ListaPermisosPorUsuarioYFechas(Activity_Informe.this,UsurioSeleccionado.replace(".","_"),edtFechaDesde.getText().toString(),edtFechaHasta.getText().toString());

                //if(ArrayPermisos.size()>0){
                String Nombre= UsurioSeleccionado + edtFechaDesde.getText().toString().replace("/","") +"_" + edtFechaHasta.getText().toString().replace("/","") + ".pdf";
                objPDF=new ClsFicheroPDF(CARPETA_PDF_PERMISOS,Nombre);
                objPDF.generarPDF(getContext(),ArrayPermisos);
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
}