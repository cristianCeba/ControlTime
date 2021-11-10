package com.example.controltime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ClsAdaptadorPermisos extends BaseAdapter {

    private Context miContexto;

    private ArrayList<ClsUsuarioPermiso> miArrayList;

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
        return miArrayList.get(position).getCodigo();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(miContexto);

        view = layoutInflater.inflate(R.layout.list_item_permisos, null);

        TextView nombre=(TextView)view.findViewById(R.id.textUsuarioPermiso);

        TextView fechaInicioPermiso =(TextView)view.findViewById(R.id.textMostrarInicioPermiso);

        TextView fechaFinPermiso =(TextView)view.findViewById(R.id.textMostrarFinPermiso);

        TextView permiso =(TextView)view.findViewById(R.id.textMostrarTipoPermiso);



        nombre.setText(miArrayList.get(position).getNombre());

        fechaInicioPermiso.setText((miArrayList.get(position).getInicioPermiso()));

        fechaFinPermiso.setText((miArrayList.get(position).getFinPermiso()));

        permiso.setText((miArrayList.get(position).getTipoPemriso()));

        return view;
    }
}
