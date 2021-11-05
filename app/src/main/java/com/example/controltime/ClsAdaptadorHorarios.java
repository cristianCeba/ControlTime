package com.example.controltime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ClsAdaptadorHorarios extends BaseAdapter {

    private Context miContexto;

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


        nombre.setText(miArrayList.get(position).getNombre());

        horaInicio.setText((miArrayList.get(position).gethoraInicioJornada()));

        horaFin.setText((miArrayList.get(position).gethoraFinJornada()));

        horaInicioDescanso.setText((miArrayList.get(position).gethoraInicioDescanso()));

        horaFinDescanso.setText((miArrayList.get(position).gethoraFinDescanso()));

        fecha.setText((miArrayList.get(position).getFecha()));

        return view;
    }
}
