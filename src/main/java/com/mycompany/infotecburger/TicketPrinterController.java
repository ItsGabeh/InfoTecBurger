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
import java.awt.*;
import java.awt.print.Printable;
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

    // Este método es específico para aquellas APIś que no soportan datos crudos
    // El único ejemplo conocido hasta ahora es Microsoft Print to PDF
    // Debe pasarse la lista de objetos
    public static void printWithGraphics(String printerName, List<MainView.MenuItem> items, String total) {
        try {
            // Darle el formato de una clase que extiende de printable
            DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;

            Printable ticket = (graphics, pageFormat, pageIndex) -> {
                if (pageIndex > 0) return Printable.NO_SUCH_PAGE; // El documento no tiene más páginas y se detiene el ciclo de impresión

                // El ticket se dibuja aquí con el onjeto de Graphics 2d
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY()); // El objeto se puede trasladar hasta los limites de la pagina

                int x = 20;
                int y = 20; // Dibujar un poco más abajo del límite

                g2d.drawString("======== Ticket de orden =======", x, y);
                y += 20;

                for (MainView.MenuItem menuItem : items) {
                    String tabs;
                    int itemNameLength = menuItem.getName().length();
                    if (itemNameLength <= 17) {
                        tabs = "  \t\t";
                    } else if (itemNameLength >= 21) {
                        tabs = "\t";
                    } else {
                        tabs = "\t\t";
                    }

                    g2d.drawString(" - " + menuItem.getName() + tabs + menuItem.getPrice(), x, y);
                    y += 20;
                }

                g2d.drawString("Total a pagar: " +  total, x, y);
                y += 20;

                g2d.drawString("===============================", x, y);
                return Printable.PAGE_EXISTS;
            };

            Doc doc = new SimpleDoc(ticket, flavor, null);

            // Buscar el servicio con el nombre de la impresora con el que se creó el controlador
            PrintService service = searchPrinter(printerName);
            if (service != null) {
                DocPrintJob job = service.createPrintJob();
                PrintRequestAttributeSet requestAttributeSet = new HashPrintRequestAttributeSet();
                job.print(doc, requestAttributeSet);
            }
        } catch (PrintException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Este método sirve para aquellas impresoras que acepten escribir datos crudos
    // Pueden ser impresoras o en linux impresoras virtuales de pdf
    // Microsoft Print to PDF no parece ser compatible con BYTE_ARRAY
    // Para hacer print a esa herramienta de pdf, se debe usar printGraphics
    public static void print(String printerName, List<MainView.MenuItem> items, String total) {
        // Crear el string a imprimir con los items y el total
        StringBuilder builder = new StringBuilder();
        builder.append("======== Ticket de orden =======\n");

        // Recorrer cada elemento de la orden y agregarlo al string
        for (MainView.MenuItem menuItem : items) {
            // Obtener el item para no llamar más veces a getElementAt
            // Extra: para hacer que el ticket se vea mejor con los precios separados para legibilidad
            String tabs;
            int itemNameLength = menuItem.getName().length();
            if (itemNameLength <= 17) {
                tabs = "  \t\t";
            } else if (itemNameLength >= 21) {
                tabs = "\t";
            } else {
                tabs = "\t\t";
            }

            // Para evitar string concatenation en el builder se recomienda hacer varias llamadas a append en cadena
            builder.append(" - ").append(menuItem.getName()).append(tabs).append(menuItem.getPrice()).append("\n");
        }
        builder.append("\nTotal a pagar: ").append(total).append("\n================================\n");

        // Buscar el servicio con el nombre de la impresora con el que se creó el controlador
        PrintService service = searchPrinter(printerName);

        if (service != null) {
            // Extraer los bytes del texto a imprimir
            byte[] contentBytes = builder.toString().getBytes(StandardCharsets.UTF_8);

            // El flavor especifica el formato de los datos enviados, y se componen de dos partes
            // Representación de Clase: InputStream, URL, byte[] o Printable
            // MIME type: text/plain, image/png ...
            // Debido a que el contenido del ticket ya está generado en un string armado y es un string relativamente corto
            // lo podemos enviar como un array de bytes con TEXT_PLAIN_UTF_8, ya que los datos son puro texto
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

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
