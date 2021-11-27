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

import com.example.controltime.Clases.ClsUsuarioHorario;
import com.example.controltime.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ClsAdaptadorHorarios extends BaseAdapter {

    private Context miContexto;

    private StorageReference storage;

    private ArrayList<ClsUsuarioHorario> miArrayList;

    public ClsAdaptadorHorarios(Context miContexto, ArrayList<ClsUsuarioHorario> miArrayList){

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
        return miArrayList.get(position).getcodigo();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(miContexto);

        view = layoutInflater.inflate(R.layout.list_item_horarios, null);

        TextView nombre=(TextView)view.findViewById(R.id.textUsuario);

        TextView horaInicio =(TextView)view.findViewById(R.id.textMostrarInicioJornada);

        TextView horaFin =(TextView)view.findViewById(R.id.textMostrarFinJornada);

        TextView horaInicioDescanso =(TextView)view.findViewById(R.id.textMostrarInicioDescanso);

        TextView horaFinDescanso =(TextView)view.findViewById(R.id.textFinInicioDescanso);

        TextView fecha =(TextView)view.findViewById(R.id.textFecha);

        ImageView imagen = view.findViewById(R.id.imagenUsuario);


        nombre.setText(miArrayList.get(position).getNombre() + "  ");

        horaInicio.setText((miArrayList.get(position).gethoraInicioJornada()));

        horaFin.setText((miArrayList.get(position).gethoraFinJornada()));

        horaInicioDescanso.setText((miArrayList.get(position).gethoraInicioDescanso()));

        horaFinDescanso.setText((miArrayList.get(position).gethoraFinDescanso()));

        String [] dia = miArrayList.get(position).getFecha().split("-");

        fecha.setText(dia[2] + "/" + dia[1] + "/" + dia[0]);

        if (miArrayList.get(position).getIdImagen() != null){
            String correo = miArrayList.get(position).getCorreo();
            String idImagen = miArrayList.get(position).getIdImagen();
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
