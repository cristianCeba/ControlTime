package com.example.controltime.Clases;

import android.content.Context;
import android.os.Environment;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ClsFicheroPDF {

String Nombre;
String mensaje;
String nomEstado;
    String nomPer;
    /***
     * Constructor de la clase
     * @param nombre nombre del fichero
     */
    public ClsFicheroPDF(  String nombre) {
        Nombre = nombre;
    }
    /***
     * Constructor inicializado
     */
    public ClsFicheroPDF() {
        Nombre = "";
    }
    /***
     * Metodo que genera un pdf y lo guarda el la ruta de las descargas
     * @param context  Context de donde viene
     * @param ArrayPermisos array de los permisos
     * @param ArrayFichajes array de los fichajes
     * @return devuelve un valor boolean (true - generado correctamente ; false - no se ha generado)
     */
    public boolean generarPDF(Context context, List<ClsPermisos>ArrayPermisos, List<ClsFichaje>ArrayFichajes)  {
        Document doc = new Document();
        boolean estaGenerado=false;
        File f =context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File fichero = null;
        try{

                //File fichero= crearFichero(this.Nombre,this.Ruta );
                fichero = new File(f.getAbsolutePath() + "/" + this.Nombre);
                if (fichero != null) {
                    //salida
                    FileOutputStream ficheroPDF = new FileOutputStream(fichero.getAbsolutePath());
                    PdfWriter writer = PdfWriter.getInstance(doc, ficheroPDF);
                    // Abrimos el documento.
                    doc.open();

                    if (ArrayPermisos.size() > 0) {
                        doc.add(new Paragraph("PERMISOS PEDIDOS\n\n"));

                        // Insertamos una tabla.
                        PdfPTable tabla = new PdfPTable(6);
                        tabla.addCell("USUARIO ID");
                        tabla.addCell("FECHA DESDE");
                        tabla.addCell("FECHA HASTA");
                        tabla.addCell("TOTAL DIAS");
                        tabla.addCell("PERMISO");
                        tabla.addCell("ESTADO");
                        for (int i = 0; i < ArrayPermisos.size(); i++) {
                            tabla.addCell(String.valueOf(ArrayPermisos.get(i).UsuarioId));
                            tabla.addCell(ArrayPermisos.get(i).FechaDesde);
                            tabla.addCell(ArrayPermisos.get(i).FechaHasta);
                            tabla.addCell(String.valueOf(ArrayPermisos.get(i).dias));
                            int idPer = ArrayPermisos.get(i).TipoPermiso;

                            String nomPer = getNombrePermiso(idPer);
                            int idEstado =ArrayPermisos.get(i).Estado;
                            String nomEstado=getNombreEstado(idEstado);
                            tabla.addCell(nomPer);
                            tabla.addCell(nomEstado);

                        }
                        estaGenerado = true;
                        doc.add(tabla);
                    }
                    if (ArrayFichajes.size() > 0) {
                        doc.add(new Paragraph("FICHAJES SOLICITADOS\n\n"));

                        // Insertamos una tabla.
                        PdfPTable tabla = new PdfPTable(7);
                        tabla.addCell("USUARIO ID");
                        tabla.addCell("DIA");
                        tabla.addCell("HORA INI");
                        tabla.addCell("HORA FIN");
                        tabla.addCell("INI DESCANSO");
                        tabla.addCell("FIN DESCANSO");
                        tabla.addCell("ESTADO");
                        for (int i = 0; i < ArrayFichajes.size(); i++) {
                            tabla.addCell(String.valueOf(ArrayFichajes.get(i).usuarioId));
                            tabla.addCell(ArrayFichajes.get(i).dia);
                            tabla.addCell(ArrayFichajes.get(i).horaIni);
                            tabla.addCell(ArrayFichajes.get(i).horaFin);
                            tabla.addCell(ArrayFichajes.get(i).horaIniDescanso);
                            tabla.addCell(ArrayFichajes.get(i).horaFinDescanso);
                            int idEstado =ArrayFichajes.get(i).estadoFichaje;
                            String nomEstado=getNombreEstado(idEstado);
                            tabla.addCell(nomEstado);
                        }
                        estaGenerado = true;
                        doc.add(tabla);
                    }
                }

        }catch (DocumentException e){
            estaGenerado=false;
           // ClsUtils.MostrarMensajes(context, e.getMessage(), "DocumentException");
        }catch (IOException e){
            estaGenerado=false;
         //   ClsUtils.MostrarMensajes(context, e.getMessage(), "IOException");
        }finally {
            if(estaGenerado==true) {
                ClsUtils.MostrarMensajes(context, fichero.getAbsolutePath(), "", false, ClsUtils.actividadEnum.PDF);
            }
            doc.close();
        }
return  estaGenerado;
    }


    public String getNombrePermiso (int idPer){
        mensaje=""  ;
        nomPer="";
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()){
                      nomPer = ClsTipoPermiso.NombrePermiso(idPer);

                    DbConnection.cerrarConexion();
                }else{
                    mensaje="Ha ocurrido un error intentelo en unos minutos";
                }
            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            mensaje="Ha ocurrido un error intentelo en unos minutos";
        }
        return nomPer;
    }

    public String getNombreEstado (int idEstado){
        mensaje=""  ;
        nomEstado="";
        Thread h1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(DbConnection.conectarBaseDeDatos()){
                    nomEstado = ClsEstadosFichajes.NombrePermiso(idEstado);

                    DbConnection.cerrarConexion();
                }else{
                    mensaje="Ha ocurrido un error intentelo en unos minutos";
                }
            }
        });
        h1.start();
        try {

            h1.join();

        } catch (InterruptedException e) {
            mensaje="Ha ocurrido un error intentelo en unos minutos";
        }
        return nomEstado;
    }

}
