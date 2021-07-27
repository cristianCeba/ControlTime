package com.example.controltime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    String servidor;
    String usuario;
    String pass;
    String bbdd;
    int puerto;
    String driver="com.mysql.jdbc.Driver";
    String cadenaConexion;
    Connection conexion;
    EditText Txtmensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Txtmensaje=(EditText) findViewById(R.id.txtMensaje);
    }
    public void conecta(View view) {
        Txtmensaje.setText("CONECTANDO.....");

        String mensaje="";
        String MensajeError="";

        try {
            //  ClsAccesoDatos con = new ClsAccesoDatos("db4free.net","bbddprueba","bbddprueba","bbddprueba",3306);

            mensaje="1 ENTRA EN TRY....";
            if (AbreConexion( MensajeError)){

                mensaje = mensaje   + "  " + MensajeError;
            }else{

                mensaje = mensaje   + "  " + MensajeError;
            }
        } catch (ClassNotFoundException e) {

            mensaje=mensaje + e.getMessage();
        }finally {
            Txtmensaje.setText( mensaje);
        }


    }


    public boolean AbreConexion(String mensajeError) throws ClassNotFoundException {


        boolean estadoConexion=false;
        mensajeError="2 . HA ENTRADO";
        try {

            cadenaConexion="jdbc:mysql://" + servidor + ":" + puerto + "/";

            //   Class.forName(driver).newInstance();
            Class.forName("com.mysql.jdbc.Driver").newInstance ();
            //conexion     = DriverManager.getConnection("jdbc:mysql://" + servidor + ":" + puerto + "/" + bbdd, usuario,pass);


            conexion= DriverManager.getConnection(cadenaConexion + bbdd ,usuario,pass);
            if(!conexion.isClosed()){
                estadoConexion=true;
            }
        } catch (IllegalAccessException e) {
            mensajeError= e.getMessage();
        } catch (InstantiationException e) {
            mensajeError= e.getMessage();
        } catch (SQLException e) {
            mensajeError="Error en la cadena de conexión " +  e.getMessage();


        }
        return estadoConexion;
    }
}