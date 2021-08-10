package com.example.controltime;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Patterns;

import androidx.appcompat.app.AlertDialog;

import java.util.regex.Pattern;

public class  Utils {

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
}
