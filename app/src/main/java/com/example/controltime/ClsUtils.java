package com.example.controltime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.DatePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;
public class ClsUtils extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    public static boolean validarEmail (String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static boolean validarContraseña (String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        return password.matches(pattern);
    }

    public static void MostrarMensajes(Context context, String mensaje, String titulo){
        final CharSequence[] opciones = {mensaje   };
        final AlertDialog.Builder alertInfo = new AlertDialog.Builder(context );
        alertInfo.setTitle(titulo);
        alertInfo.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertInfo.show();
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



 /*   public static double getDiasSolicitados(String Fechadesde, String FechaHasta) throws ParseException {
        double dias=0.0;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date dataDesde = formato.parse(Fechadesde);
        Date dataHasta = formato.parse(FechaHasta);
        dias=( (dataHasta.getTime() - dataDesde.getTime()) / (1000 * 60 * 60 * 24));

        return dias;
    }*/

    public static double calculaDiasHabiles(String Fechadesde, String FechaHasta) throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        double diffDays = 0.0;
        boolean diaHabil = false;
        List<Date>listaFechasNoLaborables=new ArrayList<>();


    //    String diaIni =ClsUtils.formatearFecha(Fechadesde,true  );
      //  String diaFin = ClsUtils.formatearFecha(FechaHasta,true);


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
