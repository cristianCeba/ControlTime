package com.example.controltime;

import android.content.Context;
import android.os.Environment;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import harmony.java.awt.Color;

public class ClsFicheroPDF {
String Ruta;
String Nombre;

    public ClsFicheroPDF(String ruta, String nombre) {
        Ruta = ruta;
        Nombre = nombre;
    }
    public ClsFicheroPDF() {
        Ruta = "";
        Nombre = "";
    }
    public void generarPDF(Context context, List<ClsPermisos>ArrayPermisos)  {
        Document doc = new Document();
        ClsUtils.MostrarMensajes(context, "Generando pDF.....", "generarPDF");

        try{
            File fichero= crearFichero(this.Nombre,this.Ruta );
            if(fichero!=null){
                //salida
                FileOutputStream ficheroPDF= new FileOutputStream(fichero.getAbsolutePath());
                PdfWriter writer=PdfWriter.getInstance(doc,ficheroPDF);
                // Abrimos el documento.
                doc.open();
                doc.add(new Paragraph("PERMISOS PEDIDOS\n\n"));

                // Insertamos una tabla.
                PdfPTable tabla = new PdfPTable(5);
                for (int i = 0; i < ArrayPermisos.size(); i++) {
                        tabla.addCell(String.valueOf(ArrayPermisos.get(i).UsuarioId));
                        tabla.addCell( ArrayPermisos.get(i).FechaDesde);
                        tabla.addCell( ArrayPermisos.get(i).FechaHasta);
                        tabla.addCell(String.valueOf(ArrayPermisos.get(i).dias));
                        tabla.addCell(String.valueOf(ArrayPermisos.get(i).TipoPermiso));

                }
                doc.add(tabla);
            }
        }catch (DocumentException e){
            ClsUtils.MostrarMensajes(context, e.getMessage(), "DocumentException");
        }catch (IOException e){
            ClsUtils.MostrarMensajes(context, e.getMessage(), "IOException");
        }finally {
            ClsUtils.MostrarMensajes(context, "CREADO ", "generarPDF");
            doc.close();
        }

    }
    public   File crearFichero(String nombreFichero,String Ruta) throws IOException {
        File ruta = getRuta(Ruta);
        File fichero = null;
        if (ruta != null) {
            fichero = new File(ruta, nombreFichero);
        }
        return fichero;
    }
    /**
     * Obtenemos la ruta donde vamos a almacenar el fichero.
     *
     * @return
     */
    public  File getRuta (String Ruta) {
        // El fichero sera almacenado en un directorio dentro del directorio Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Ruta);

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                         return null;
                    }
                }
            }
        }

        return ruta;
    }
}
