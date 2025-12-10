package com.mycompany.infotecburger;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Esta vista sirve para seleccionar una de las impresoras disponibles para imprimir el ticket.
 * Extiende de JDialog, está hecho para ser una ventana secundaria o modal, lo que es mejor enfoque que usar un Frame.
 * @author Gabeh
 */
public class PrinterView extends JDialog {
    JPanel containerPanel; // Este panel contiene a todos los elementos de la UI

    JLabel printerListLabel;
    JList<String> printerList; //
    JScrollPane printerListScrollPane;
    DefaultListModel<String> printerListModel; // Aquí se van a ir agregando los nombres de las impresoras

    JButton printButton;
    JButton cancelButton; // Opcional, solo para cerrar la ventana en caso de que no se quiera cerrar desde el botón de cerrar

    private String contentToPrint; // El contenido a imprimir

    // Para mostrar este dialog, es necesario especificar un Frame que actúa como el dueño de la ventana y un título
    public PrinterView(Frame owner) {
        super(owner, "Imprimir ticket", true);
        initComponents();
    }

    // Se asume que el contenido que se pasa para imprimir no es nulo o está vacío
    public void setContentToPrint(String contentToPrint) {
        this.contentToPrint = contentToPrint;
    }

    private void initComponents() {
        // INICIALIZACIÓN DE LOS COMPONENTES
        containerPanel = new JPanel();

        printerListLabel = new JLabel("Seleccione una impresora");
        printerListModel = new DefaultListModel<>();
        printerList = new JList<>(printerListModel);
        printerListScrollPane = new JScrollPane(printerList);

        printButton = new JButton("Imprimir");
        cancelButton = new JButton("Cancelar");

        // CONFIGURACIÓN DE LA LISTA
        // Llamar al controlador de las impresoras de tickets para obtener todas las impresoras para poder imprimir
        List<String> printers = TicketPrinterController.getAllPrinters();
        printerListModel.addAll(printers);
        printerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Que solo se pueda seleccionar una impresora de la lista

        // CONFIGURACIÓN DEL PANEL CONTENEDOR
        containerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor =  GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        containerPanel.add(printerListScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.CENTER;
        containerPanel.add(printButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.CENTER;
        containerPanel.add(cancelButton, gbc);

        // CONFIGURACIÓN DE LOS BOTONES
        printButton.addActionListener(e -> {
            // Para mandar a imprimir hay que obtener la impresora seleccionada
            String printerName = printerList.getSelectedValue();
            System.out.println(printerName);

            if (printerName != null &&  !printerName.isBlank()) {
                TicketPrinterController.print(printerName, contentToPrint);
                this.dispose(); // Una vez que se imprimó el contenido, se cierra la ventana automáticamente
            } else {
                System.out.println("No se seleccionó una impresora para imprimir");
            }
        });

        cancelButton.addActionListener(e -> {
            this.dispose(); // Liberar los recursos de esta ventana
        });

        // CONFIGURACIÓN DEL FRAME
        this.add(containerPanel);
        this.setSize(300, 300);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Liberar los recursos cuando se cierre
        this.setLocationRelativeTo(null);
    }

}
