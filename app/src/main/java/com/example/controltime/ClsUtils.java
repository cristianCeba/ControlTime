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
import java.util.Calendar;
import java.util.Date;
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



    public static double getDiasSolicitados(String Fechadesde, String FechaHasta) throws ParseException {
        double dias=0.0;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date dataDesde = formato.parse(Fechadesde);
        Date dataHasta = formato.parse(FechaHasta);
        dias=( (dataHasta.getTime() - dataDesde.getTime()) / (1000 * 60 * 60 * 24));

        return dias;
    }

}
