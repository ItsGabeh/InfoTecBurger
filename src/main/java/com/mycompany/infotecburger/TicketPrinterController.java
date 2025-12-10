package com.mycompany.infotecburger;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Esta clase sirve como un controlador para ejecutar operaciones a través de una impresora
 * para imprimir tickets de la compra
 * @author Gabeh
 */
public class TicketPrinterController {
    // Devuelve una lista de string de cada nombre de cada impresora
    public static List<String> getAllPrinters() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        // Con un stream usando programación funcional podemos simplificar el devolver una lista solo con los nombres de cada servicio
        // La operación terminal toList devuelve un list inmutable
        return Arrays.stream(services).map(s -> s.getName()).toList();
    }

    // Busca la impresora asociada con el nombre dado
    private static PrintService searchPrinter(String printerName) {
        // Un printer service es una interfaz que actúa como una factory de DocPrinterJob
        // para agregar trabajos de impresión, describe lo que una impresora puede hacer
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

        for (PrintService service : services) {
            if (service.getName().equals(printerName)) {
                return service;
            }
        }

        return null; // regresa null si no se encuentra la impresora en los servicios disponibles
    }

    public static void print(String printerName, String text) {
        // Buscar el servicio con el nombre de la impresora con el que se creó el controlador
        PrintService service = searchPrinter(printerName);

        if (service != null) {
            // Extraer los bytes del texto a imprimir
            byte[] contentBytes = text.getBytes(StandardCharsets.UTF_8);

            // El flavor especifica el formato de los datos enviados, y se componen de dos partes
            // Representación de Clase: InputStream, URL, byte[] o Printable
            // MIME type: text/plain, image/png ...
            // Debido a que el contenido del ticket ya está generado en un string armado y es un string relativamente corto
            // lo podemos enviar como un array de bytes con TEXT_PLAIN_UTF_8, ya que los datos son puro texto
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.TEXT_PLAIN_UTF_8;

            // Un Doc es una interfaz que representa el documento a imprimir
            // Se crea con el contenido, el formato de los datos y unos atributos
            Doc document = new SimpleDoc(contentBytes, flavor, null);

            // Es la interfaz que representa el trabajo de impresión, toma el objeto Doc y lo envía al PrintService
            DocPrintJob job = service.createPrintJob();
            // Estos atributos definen como debe realizarse el trabajo, por ejemplo copias, tamaño del papel, o el destino para el archivo de salida
            PrintRequestAttributeSet requestAttributes = new HashPrintRequestAttributeSet();

            try {
                // Ejecutar el trabajo de impresión
                job.print(document, requestAttributes);
            } catch (PrintException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
