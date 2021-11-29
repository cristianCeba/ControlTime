package com.example.controltime.Adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.controltime.Clases.ClsUsuarioPermiso;
import com.example.controltime.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ClsAdaptadorPermisos extends BaseAdapter {

    private Context miContexto;

    private ArrayList<ClsUsuarioPermiso> miArrayList;

    private StorageReference storage;

    public ClsAdaptadorPermisos(Context miContexto, ArrayList<ClsUsuarioPermiso> miArrayList){

        this.miContexto=miContexto;

        this.miArrayList=miArrayList;
    }

    @Override
    public int getCount() {
        return miArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return miArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(miContexto);

        view = layoutInflater.inflate(R.layout.list_item_permisos, null);

        TextView nombre=(TextView)view.findViewById(R.id.textUsuarioPermiso);

        TextView fechaInicioPermiso =(TextView)view.findViewById(R.id.textMostrarInicioPermiso);

        TextView fechaFinPermiso =(TextView)view.findViewById(R.id.textMostrarFinPermiso);

        TextView permiso =(TextView)view.findViewById(R.id.textMostrarTipoPermiso);

        ImageView imagen = view.findViewById(R.id.imagenUsuario);



        nombre.setText(miArrayList.get(position).getNombre() + "  ");

        String [] fechaIni = miArrayList.get(position).getInicioPermiso().split("-");

        fechaInicioPermiso.setText(fechaIni[2] + "/" + fechaIni[1] + "/" + fechaIni[0]);

        String [] fechaFin = miArrayList.get(position).getFinPermiso().split("-");

        fechaFinPermiso.setText(fechaFin[2] + "/" + fechaFin[1] + "/" + fechaFin[0]);

        permiso.setText("permiso solicitado: " + (miArrayList.get(position).getTipoPemriso()));
        String idImagen = String.valueOf(miArrayList.get(position).getidImage());
        if (miArrayList.get(position).getidImage() != 0){
            String correo = miArrayList.get(position).getCorreo();
            System.out.println("correo --> " + correo);
            System.out.println("idImagen --> " + idImagen);
            storage = FirebaseStorage.getInstance().getReference();

            StorageReference pathReference = storage.child("imagenes").child(correo).child(idImagen);

            pathReference.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    imagen.setImageBitmap(bitMap);
                }
            });
        }

        return view;
    }
}
