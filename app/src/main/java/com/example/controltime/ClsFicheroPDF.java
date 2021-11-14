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
    public void generarPDF(Context context) throws IOException, DocumentException {
        Document doc = new Document();
        ClsUtils.MostrarMensajes(context, "ENTRA", "generarPDF");
       // String NombreFich= this.Nombre;

        File fichero= crearFichero(this.Nombre,this.Ruta );
        ClsUtils.MostrarMensajes(context, fichero.getAbsolutePath(), "generarPDF");
        //salida
        FileOutputStream ficheroPDF= new FileOutputStream(fichero.getAbsolutePath());
        PdfWriter writer=PdfWriter.getInstance(doc,ficheroPDF);

        // Incluimos el pie de pagina y una cabecera
        HeaderFooter cabecera = new HeaderFooter(new Phrase("FECHA:     USUARIO: " ), false);
        HeaderFooter pie = new HeaderFooter(new Phrase("FIN"), false);

        doc.setHeader(cabecera);
        doc.setFooter(pie);

        // Abrimos el documento.
        doc.open();

        // Añadimos un titulo con la fuente por defecto.
        doc.add(new Paragraph("LOS PERMISOSSSS"));

        Font font = FontFactory.getFont(FontFactory.HELVETICA, 28,
                Font.BOLD, Color.RED);
        doc.add(new Paragraph("PERMISOS PEDIDOS", font));

        // Insertamos una imagen que se encuentra en los recursos de la
        // aplicacion.
  /*
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.fondo4);//VER UN LOGO
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Image imagen = Image.getInstance(stream.toByteArray());
        doc.add(imagen);
*/
        // Insertamos una tabla.
        PdfPTable tabla = new PdfPTable(5);
        for (int i = 0; i < 15; i++) {
            tabla.addCell("dato " + i);
        }
        doc.add(tabla);

        // Agregar marca de agua
 /*
        font = FontFactory.getFont(FontFactory.HELVETICA, 42, Font.BOLD, Color.GRAY);
        ColumnText.showTextAligned(writer.getDirectContentUnder(),
                Element.ALIGN_CENTER, new Paragraph(
                        "androfast.com", font), 297.5f, 421,
                writer.getPageNumber() % 2 == 1 ? 45 : -45);
*/
        ClsUtils.MostrarMensajes(context, "CERRAMOS", "generarPDF");
        doc.close();
    }
    public static File crearFichero(String nombreFichero,String Ruta) throws IOException {
        File ruta = getRuta(Ruta);
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }
    /**
     * Obtenemos la ruta donde vamos a almacenar el fichero.
     *
     * @return
     */
    public static File getRuta (String Ruta) {

        // El fichero sera almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    Ruta);

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                         return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }
}
