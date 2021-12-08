package com.example.controltime.Actividades;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.controltime.Clases.ClsUser;
import com.example.controltime.Clases.DbConnection;
import com.example.controltime.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.zip.Inflater;

public class Activity_Navegador extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    ClsUser usuario;
    String correo;
    static ImageView imagenUsuario;
    static TextView nombreUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__navegador);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        correo = ClsUser.UsuarioConectadoApp(getApplicationContext()).replace("_",".");
        buscarUsuario(correo);

            mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_todos_permiso,
                    R.id.nav_horario, R.id.nav_fichaje,R.id.nav_configuracion,R.id.nav_CerrarSesion,R.id.nav_solicitarFichaje2,R.id.nav_verFichaje,R.id.nav_permisos,R.id.nav_informe)
                    .setDrawerLayout(drawer)
                    .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.mobile_navigation);

        if (usuario.TipoUsuario == 0){
            navGraph.setStartDestination(R.id.nav_informe);
        }

        navController.setGraph(navGraph);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        Menu M_Sie_Menu =navigationView.getMenu();
        MenuItem permisos= M_Sie_Menu.findItem(R.id.nav_permisos);
        MenuItem  fichaje= M_Sie_Menu.findItem(R.id.nav_fichaje);

        MenuItem verFichaje= M_Sie_Menu.findItem(R.id.nav_verFichaje);
        MenuItem solicitarFichaje= M_Sie_Menu.findItem(R.id.nav_solicitarFichaje2);
        MenuItem empleado= M_Sie_Menu.findItem(R.id.nav_incluirUsuario);
        MenuItem validar= M_Sie_Menu.findItem(R.id.nav_validar);
        MenuItem informe= M_Sie_Menu.findItem(R.id.nav_informe);
        MenuItem conf= M_Sie_Menu.findItem(R.id.nav_configuracion);
        MenuItem horario=M_Sie_Menu.findItem(R.id.nav_horario);
        MenuItem todosPermisos=M_Sie_Menu.findItem(R.id.nav_todos_permiso);
      //  MenuItem inicio=M_Sie_Menu.findItem(R.id.nav_inicio);

       // Toast.makeText(Activity_Navegador.this, "ID_USUARIO: " + usuario.usuarioId , Toast.LENGTH_SHORT).show();
        TextView tNombreUsuario = navigationView.getHeaderView(0).findViewById(R.id.textNombre);
        tNombreUsuario.setText(usuario.Nombre + " " + usuario.Ape);
        TextView tCorreoUsuario =  navigationView.getHeaderView(0).findViewById(R.id.textCorreo);
        tCorreoUsuario.setText(correo);
        imagenUsuario = navigationView.getHeaderView(0).findViewById(R.id.imageFondo);
        nombreUsuario = navigationView.getHeaderView(0).findViewById(R.id.textNombre);
        recuperarImagen(usuario);

        /**
         * Modificamos los items según el tipo de usuario ha accedido a la aplicación.
         */
        if (usuario.TipoUsuario == 0){
            permisos.setVisible(false);
            fichaje.setVisible(false);
            verFichaje.setVisible(false);
            solicitarFichaje.setVisible(false);
            horario.setVisible(true);
            todosPermisos.setVisible(true);

        } else if (usuario.TipoUsuario == 1){
            horario.setVisible(true);
            todosPermisos.setVisible(true);
            empleado.setVisible(false);
        } else {
            validar.setVisible(false);
            todosPermisos.setVisible(false);
            horario.setVisible(false);
            informe.setVisible(false);
            empleado.setVisible(false);

        }

    }

    public void cerrarSesion (MenuItem item){
        ClsUser.CerrarSesion(getApplicationContext());
        Intent intent = new Intent(this, Activity_Login.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__navegador, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void buscarUsuario (String correo){
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
               if(DbConnection.conectarBaseDeDatos()){
                   usuario = ClsUser.getUsuario(correo);
                   DbConnection.cerrarConexion();
               }

            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public static void recuperarImagen (ClsUser usuario) {

        String idImagen = usuario.idImagen;
        StorageReference storage;
        storage = FirebaseStorage.getInstance().getReference();
        System.out.println("IdImagen --> " + idImagen);
        if (idImagen != null){

            StorageReference pathReference = storage.child("imagenes").child(usuario.correoElectronico).child(idImagen);

            pathReference.getBytes(1024*1024*5).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    imagenUsuario.setImageBitmap(bitMap);
                }
            });

        }

    }

    public static void cambiarNombre (ClsUser usuario) {
        nombreUsuario.setText(usuario.Nombre + " " + usuario.Ape);
    }
    @Override
    public void onBackPressed()
    {
    }
}