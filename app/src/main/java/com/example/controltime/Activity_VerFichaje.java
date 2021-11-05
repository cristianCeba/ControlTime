package com.example.controltime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Activity_VerFichaje extends AppCompatActivity {

    CalendarView calendario;
    TextView mostrarInicio,mostrarFin,mostrarInicioDesc,mostrarFinDesc,mostrarMensaje;
    private DatabaseReference mDataBase;
    String usuarioAplicacion,dia;
    int mes;
    boolean diaEncontrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_fichaje);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        calendario = findViewById(R.id.calendarView);
        mostrarInicio = findViewById(R.id.textMostrarHoraIni);
        mostrarFin = findViewById(R.id.textMostrarHoraFin);
        mostrarInicioDesc = findViewById(R.id.textMostrarHoraIniDesc);
        mostrarFinDesc = findViewById(R.id.textMostrarHoraFinDesc);
        mostrarMensaje = findViewById(R.id.textMostrarError);
        usuarioAplicacion = ClsUser.UsuarioConectadoApp(getApplicationContext()).replace(".", "_").trim();


        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                limpiarPantalla();
                diaEncontrado = false;
                mes = month +1;
                if (mes >= 10){
                    dia = (dayOfMonth + ":" + mes + ":" + year);
                } else {
                    dia = (dayOfMonth + ":" + "0" + mes + ":" + year);
                }

                Query query=mDataBase.child("fichaje").child(usuarioAplicacion);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Buscamos todos los fichajes del usuario y si ya ha fichado hoy, dehsabilitamos el botón de iniciar fichaje
                        for(DataSnapshot ds: snapshot.getChildren()){
                            ClsFichaje fichajeUsuario = ds.getValue(ClsFichaje.class);

                            
                            //User.guardarFichajeUsuario(fichajeUsuario);
                            if (ds.getKey().equals(dia)){
                                diaEncontrado = true;
                                if (!fichajeUsuario.horaIni.equals("0")){
                                    mostrarInicio.setText(fichajeUsuario.horaIni);
                                }
                                if(!fichajeUsuario.horaFin.equals("0")){
                                    mostrarFin.setText(fichajeUsuario.horaFin);
                                }
                                if(!fichajeUsuario.horaIniDescanso.equals("0")){
                                    mostrarInicioDesc.setText(fichajeUsuario.horaIniDescanso);
                                }
                                if(!fichajeUsuario.horaFinDescanso.equals("0")){
                                    mostrarFinDesc.setText(fichajeUsuario.horaFinDescanso);
                                }

                            }
                        }

                        if(!diaEncontrado){
                            mostrarMensaje.setText("No se ha encontrado registro");
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("4");
                    }
                });
            }
        });
    }

    public void limpiarPantalla (){
        mostrarInicio.setText("");
        mostrarFin.setText("");
        mostrarInicioDesc.setText("");
        mostrarFinDesc.setText("");
        mostrarMensaje.setText("");
    }
}