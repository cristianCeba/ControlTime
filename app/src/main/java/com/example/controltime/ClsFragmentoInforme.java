package com.example.controltime;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;




import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;




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
        arrayUsuarios=new ArrayList<>();
        List<ClsUser> Arrayusuario=new ArrayList<>();




        buscarUsuario(idUsuario);
        cargaUsuarios(objUsuario.TipoUsuario,objUsuario.Grupo);
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
                String Nombre= idSeleccion+ "_" + edtFechaDesde.getText().toString().replace("/","") +"_" + edtFechaHasta.getText().toString().replace("/","") + ".pdf";
                objPDF=new ClsFicheroPDF(CARPETA_PDF_PERMISOS,Nombre);
              if(!objPDF.generarPDF(getContext(),ArrayPermisos)){
                  ClsUtils.MostrarMensajes(getContext(), "Ha habido un error al generar e pdf, intentelo mas tarde", "", true, ClsUtils.actividadEnum.ERROR);
              }else {
                  //este mensaje lo meustro dentro del metodo para que me de la ruta
                  //ClsUtils.MostrarMensajes(getContext(), "", "", false, ClsUtils.actividadEnum.PDF);
              }
            }
        });


        GenerarPDFFichaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Nombre= UsurioSeleccionado + edtFechaDesde.getText().toString().replace("/","") +"_" + edtFechaHasta.getText().toString().replace("/","") + ".pdf";
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
}