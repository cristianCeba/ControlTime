package com.example.controltime;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterListViewNOOOO extends BaseAdapter {


    public ArrayList<HashMap> list;
    private LayoutInflater inflater;
    private Activity activity;

    AdapterListViewNOOOO(Activity activity, ArrayList<HashMap> list) {
        super();
        this.activity = activity;
        this.list = list;


    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.activity_lista, null);

        HashMap map = list.get(position);

        TextView colId = (TextView) convertView.findViewById(R.id.colID);
        TextView colFechaDesde = (TextView) convertView.findViewById(R.id.colFechaDesde);
        TextView colFechaHasta = (TextView) convertView.findViewById(R.id.colFechaHasta);
        TextView colTipo = (TextView) convertView.findViewById(R.id.colTipo);
        TextView colEstado = (TextView) convertView.findViewById(R.id.colEstado);
        TextView colDias=(TextView) convertView.findViewById(R.id.colDias) ;
        //Rellenamos los valores de cada columna de la fila
        colId.setText((String)map.get("ID"));
        colFechaDesde.setText((String)map.get("Fecha_Desde"));
        colFechaHasta.setText((String)map.get("Fecha_Hasta"));
        colDias.setText((String)map.get("Dias"));
        colTipo.setText((String)map.get("Tipo_Permiso"));
        colEstado.setText((String)map.get("Estado"));

        return convertView;
    }

}