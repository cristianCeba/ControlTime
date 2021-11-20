package com.example.controltime;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Activity_Navegador extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    ClsUser usuario;
    ImageView imagenUsuario;
    StorageReference storage;
    String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__navegador);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Toast.makeText(Activity_Navegador.this, "ID_USUARIO: " + ClsUser.UsuarioIdApp(Activity_Navegador.this).toString(), Toast.LENGTH_SHORT).show();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_fichaje, R.id.nav_cerrarSesion,R.id.nav_configuracion,R.id.nav_solicitarFichaje2,R.id.nav_verFichaje,R.id.nav_permisos,R.id.nav_informe)
                .setDrawerLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        correo = ClsUser.UsuarioConectadoApp(getApplicationContext()).replace("_",".");
        buscarUsuario(correo);


        TextView tNombreUsuario = navigationView.getHeaderView(0).findViewById(R.id.textNombre);
        tNombreUsuario.setText(usuario.Nombre + " " + usuario.Ape);
        TextView tCorreoUsuario =  navigationView.getHeaderView(0).findViewById(R.id.textCorreo);
        tCorreoUsuario.setText(correo);
        imagenUsuario = navigationView.getHeaderView(0).findViewById(R.id.imageFondo);
        storage = FirebaseStorage.getInstance().getReference();
        recuperarImagen();

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
                DbConnection.conectarBaseDeDatos();
                usuario = ClsUser.getUsuario(correo);
                DbConnection.cerrarConexion();
            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void recuperarImagen () {
        String idImagen = usuario.idImagen;
        System.out.println("IdImagen --> " + idImagen);
        if (idImagen != null){

            StorageReference pathReference = storage.child("imagenes").child(correo).child(idImagen);

            pathReference.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    imagenUsuario.setImageBitmap(bitMap);
                }
            });

        }

    }
}