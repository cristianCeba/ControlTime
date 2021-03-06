package com.example.controltime.Clases;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.controltime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;


public class ClsUtils extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static boolean estaAceptado;

    /***
     * enum para ver de que actividad viene
     */
    public enum actividadEnum {
        LOGIN("Login", 0),
        INSERTAR("Insertar", 1),
        FICHAR("Fichar",2),
        PERMISO("permiso",3),
        INFORMATIVO("Informacion",4),
        ERROR("Error",5),
        PDF("pdf",6),
        VALIDAR("validar",7);

        private String stringValue;
        private int intValue;

        private actividadEnum(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }

    /***
     * metodo que valida el email
     * @param email email a validar
     * @return
     */
    public static boolean validarEmail (String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    /***
     * Metodo que valida la contraseña
     * @param password contraseña a validar
     * @return
     */
    public static boolean validarContraseña (String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        return password.matches(pattern);
    }
    /***
     * metodo que muestra mensaje , por actividad y tipo de mensaje a mostrar
     * @param context
     * @param mensaje
     * @param titulo
     * @param esError
     * @param actividad
     */
    public static void MostrarMensajes(Context context, String mensaje, String titulo, boolean esError,actividadEnum actividad){
        View view = null;
        LayoutInflater  layoutInflater=LayoutInflater.from(context);
        switch (actividad){
            case PDF:
                if(esError==true){
                    view=layoutInflater.inflate(R.layout.activity_error,null);
                }else {
                    view = layoutInflater.inflate(R.layout.pdf, null);
                }
                break;
            case ERROR:
                view=layoutInflater.inflate(R.layout.activity_error,null);
                break;
            case INFORMATIVO:
                view=layoutInflater.inflate(R.layout.informativo,null);
                break;
            case LOGIN:
                if(esError==true){
                    view=layoutInflater.inflate(R.layout.imagen_usuario_no_valido,null);

                }else{
                    view=layoutInflater.inflate(R.layout.imagen_validar_usuario,null);

                }
                break;
            case INSERTAR://inserta usuario
                if(esError==true){
                    view=layoutInflater.inflate(R.layout.imagen_usuario_error_registro_,null);

                }else{
                    view=layoutInflater.inflate(R.layout.imagen_usuario_registrado_ok,null);
                }
                break;
            case FICHAR:
                if(esError==true){
                    view=layoutInflater.inflate(R.layout.activity_error,null);

                }else{
                // aqui no se muestra mensaje
                }
                break;
            case PERMISO:
                if(esError==true){
                    view=layoutInflater.inflate(R.layout.activity_error,null);
                }else{
                    view=layoutInflater.inflate(R.layout.permiso_grabado_,null);

                }
                break;

        }

        // final CharSequence[] opciones = {mensaje   };
        AlertDialog.Builder alertInfo = new AlertDialog.Builder(context );

        AlertDialog tit=alertInfo.create();
          //tit.setTitle(mensaje);
          tit.setMessage(mensaje);
        tit.setView(view);
        tit.show();
    }


    /* TRABAJAR CON FECHAS DATEPicker*/

    private DatePickerDialog.OnDateSetListener listener;

    public  static ClsUtils newInstance(DatePickerDialog.OnDateSetListener listener){
        ClsUtils fragment = new ClsUtils();
        fragment.setListener(listener);
        return fragment;
    }

    private void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener=listener;
    }


    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        final Calendar calendario= Calendar.getInstance();
        int year=calendario.get(Calendar.YEAR);
        int month=calendario.get(Calendar.MONTH);
        int day=calendario.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),listener,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }


    /*FIN  TRABAJAR CON FECHAS DATEPicker*/


    /***
     * Metodo que calcula los dias habiles entre dos fechas
     * @param Fechadesde fecha inicial
     * @param FechaHasta fecha final
     * @return devuelve los dias
     * @throws ParseException
     */
    public static double calculaDiasHabiles(String Fechadesde, String FechaHasta) throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        double diffDays = 0.0;
        boolean diaHabil = false;
        List<Date>listaFechasNoLaborables=new ArrayList<>();




        Date dataDesde = formato.parse(Fechadesde);
        Date dataHasta = formato.parse(FechaHasta);

        Calendar fechaInicial=Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        Calendar fechaFinal=Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        fechaInicial.setTime(dataDesde);
        listaFechasNoLaborables=listaFechasNoLaborables();
        fechaFinal.setTime(dataHasta);
        while(fechaInicial.before(fechaFinal) || fechaInicial.equals(fechaFinal)){
            if (!listaFechasNoLaborables.isEmpty()) {
                for (Date date : listaFechasNoLaborables) {
                    Date fechaNoLaborablecalendar = fechaInicial.getTime();
                    //si el dia de la semana de la fecha minima es diferente de sabado o domingo
                    if (fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && !fechaNoLaborablecalendar.equals(date)) {
                        //se aumentan los dias de diferencia entre min y max
                        diaHabil = true;
                    } else {
                        diaHabil = false;
                        break;
                    }
                }
            } else {
                if (fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                    //se aumentan los dias de diferencia entre min y max
                    diffDays++;
                }
            }
            if (diaHabil == true) {
                diffDays++;
            }
            //se suma 1 dia para hacer la validacion del siguiente dia.
            fechaInicial.add(Calendar.DATE, 1);
        }
        return diffDays;

    }

    /***
     * Metodo que carga los festivos del año
     * @return devuelve una lista con los dias
     * @throws ParseException
     */
    public static List<Date> listaFechasNoLaborables() throws ParseException {
        List<Date> arrayFechas=new ArrayList<>();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        arrayFechas.add( formato.parse("2021/01/01"));
        arrayFechas.add( formato.parse("2021/01/06"));
        arrayFechas.add( formato.parse("2021/03/19"));
        arrayFechas.add( formato.parse("2021/05/01"));
        arrayFechas.add( formato.parse("2021/05/02"));
        arrayFechas.add( formato.parse("2021/05/15"));
        arrayFechas.add( formato.parse("2021/08/15"));
        arrayFechas.add( formato.parse("2021/10/12"));
        arrayFechas.add( formato.parse("2021/11/01"));
        arrayFechas.add( formato.parse("2021/12/06"));
        arrayFechas.add( formato.parse("2021/12/08"));
        arrayFechas.add( formato.parse("2021/12/25"));
        return (ArrayList<Date>)arrayFechas;



    }

    /***
     * metodo que da formato a la fecha
     * @param fecha fecha para dar el formato
     * @return devuelve un string con la fecha formateada
     * @throws ParseException
     */
    public static String formatearFecha(String fecha) throws ParseException {
        final String OLD_FORMAT = "dd/MM/yyyy";
        final String NEW_FORMAT = "yyyy/MM/dd";
        String nuevaFecha="";
        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = sdf.parse(fecha);
        sdf.applyPattern(NEW_FORMAT);
        nuevaFecha = sdf.format(d);
        nuevaFecha=nuevaFecha.replace("/", "-");
        return nuevaFecha;

    }
    public static String formatearFecha(String fecha,boolean mesPrimero) throws ParseException {

        String formatoMes = "dd/MM/yyyy";
        String formatoAnio = "yyyy/MM/dd";
        if (mesPrimero){
            String nuevaFecha="";
            SimpleDateFormat sdf = new SimpleDateFormat(formatoAnio);
            Date d = sdf.parse(fecha);
            sdf.applyPattern(formatoMes);
            nuevaFecha = sdf.format(d);
            nuevaFecha=nuevaFecha.replace("/", "-");
            return nuevaFecha;
        }else{
            String nuevaFecha="";
            SimpleDateFormat sdf = new SimpleDateFormat(formatoMes);
            Date d = sdf.parse(fecha);
            sdf.applyPattern(formatoAnio);
            nuevaFecha = sdf.format(d);
            nuevaFecha=nuevaFecha.replace("/", "-");
            return nuevaFecha;
        }




    }

}
