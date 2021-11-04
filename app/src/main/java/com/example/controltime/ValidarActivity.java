package com.example.controltime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ValidarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar);


        FragmentManager fragmentManager=this.getSupportFragmentManager();

        FragmentTransaction transaction=fragmentManager.beginTransaction();

        Fragment fragmento =new FragmentoPermisos();

        transaction.add(R.id.LinearLayoutContenedorDeFragments, fragmento);

        transaction.commit();
    }


    public void cambiarFragmento(View view){
        Fragment fragmento;
        System.out.println("Entran en cambiar fragmento");
        if (view == findViewById(R.id.btnMostrarHorarios)) {
            fragmento = new FragmentoHorario();
            System.out.println("Entran en fragmento 1");
        } else if (view == findViewById(R.id.btnMostrarPermisos)){
            fragmento = new FragmentoPermisos();
            System.out.println("Entran en fragmento 2");
        } else {
            fragmento = new FragmentoHorario();
            System.out.println("Entran en else");
        }

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.LinearLayoutContenedorDeFragments, fragmento);

        transaction.addToBackStack(null);

        transaction.commit();
    }
}