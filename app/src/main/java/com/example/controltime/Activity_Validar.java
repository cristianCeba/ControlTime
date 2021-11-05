package com.example.controltime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

public class Activity_Validar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar);


        FragmentManager fragmentManager=this.getSupportFragmentManager();

        FragmentTransaction transaction=fragmentManager.beginTransaction();

        Fragment fragmento =new ClsFragmentoPermisos();

        transaction.add(R.id.LinearLayoutContenedorDeFragments, fragmento);

        transaction.commit();
    }


    public void cambiarFragmento(View view){
        Fragment fragmento;
        System.out.println("Entran en cambiar fragmento");
        if (view == findViewById(R.id.btnMostrarHorarios)) {
            fragmento = new ClsFragmentoHorario();
            System.out.println("Entran en fragmento 1");
        } else if (view == findViewById(R.id.btnMostrarPermisos)){
            fragmento = new ClsFragmentoPermisos();
            System.out.println("Entran en fragmento 2");
        } else {
            fragmento = new ClsFragmentoHorario();
            System.out.println("Entran en else");
        }

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.LinearLayoutContenedorDeFragments, fragmento);

        transaction.addToBackStack(null);

        transaction.commit();
    }
}