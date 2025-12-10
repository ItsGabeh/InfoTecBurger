package com.mycompany.infotecburger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase es la vista principal del punto de venta
 * Por ahora es un prototipo con una funcionalidad limitada
 * @author Gabeh
 */
public class MainView extends JFrame {
    // Declaración de los componentes de esta vista
    JPanel menuPanel; // Este panel va a contener todos los botones de las opciones disponibles en el menú, junto con los botones
    JPanel orderMenuPanel; // Este panel va a contener la lista de los artículos elegidos
    JPanel containerPanel;

    // Botones del menú
    // 3 botones para hamburguesas, dos para papas y dos para refrescos
    JButton bigBurgerButton;
    JButton smallBurgerButton;
    JButton cheeseBurgerButton;
    JButton bigFriesButton;
    JButton smallFriesButton;
    JButton bigColaButton;
    JButton smallColaButton;
    JButton deleteOrderButton; // Solo para poder borrar los items, no hay una función específica para quitar ciertos elementos por separado
    JButton saveOrderButton;

    // Declaración de los nombres de los items para mantener coherencia
    // Práctica tomada del libro Effective Java de Joshua Bloch
    // Cuando se tienen multiples constantes del mismo tipo es mejor usar una enumeración
    // Las enumeraciones soportan la declaración de un constructor y atributos
    enum MenuItem {
        BIG_BURGER("Hamburguesa grande", 35.0),
        SMALL_BURGER("Hamburguesa pequeña", 30.0),
        CHEESE_BURGER("Hamburguesa de queso", 40.50),
        BIG_FRIES("Papas fritas grandes", 25.0),
        SMALL_FRIES("Papas fritas pequeñas", 20.0),
        BIG_COLA("Refresco grande", 20.0),
        SMALL_COLA("Refresco pequeño", 15.50);

        // Los atributos deben ser inmutables
        private final String name;
        private final double price;

        // El constructor debe ser privado para cumplir con el patrón singleton
        private MenuItem(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }

        @Override
        public String toString() { return name; }
    }

    // Componentes del menu de la orden
    // La lista donde aparecerán los items
    DefaultListModel<MenuItem> menuModel; // TODO
    JList<MenuItem> orderList;
    JScrollPane orderListScrollPane; // EL scroll pane que se va a usar para el JList
    JLabel totalPriceLabel;
    JTextField totalPriceField; // Aquí se va a ir poniendo el precio total

    public MainView() {
        super("InfoTec Burger");
        initComponents();
    }

    private void initComponents() {
        // Inicialización de los componentes
        menuPanel = new JPanel();
        orderMenuPanel = new JPanel();
        containerPanel = new JPanel();
        bigBurgerButton = new JButton(MenuItem.BIG_BURGER.getName()); // En vez de poner el string por separado se puede acceder al nombre concreto ya establecido
        smallBurgerButton = new JButton(MenuItem.SMALL_BURGER.getName());
        cheeseBurgerButton = new JButton(MenuItem.CHEESE_BURGER.getName());
        bigFriesButton = new JButton(MenuItem.BIG_FRIES.getName());
        smallFriesButton = new JButton(MenuItem.SMALL_FRIES.getName());
        bigColaButton = new JButton(MenuItem.BIG_COLA.getName());
        smallColaButton = new JButton(MenuItem.SMALL_COLA.getName());
        deleteOrderButton = new JButton("Borrar orden");
        saveOrderButton = new JButton("Guardar orden");
        menuModel = new DefaultListModel<>(); // Este modelo de la lista para la lista de la orden nos va a permitir añadir objetos
        orderList = new JList<>(menuModel);
        orderListScrollPane = new JScrollPane(orderList); // Añadir la lista a este scroll pane
        totalPriceLabel = new JLabel("Total:");
        totalPriceField = new JTextField(); // Este texto field solo se usa para desplegar el texto, no se debe editar

        // Constraints del layout para los paneles y una separación entre componentes de 5 pixeles
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // CONFIGURACIÓN DEL PANEL DEL MENÚ
        menuPanel.setLayout(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menuPanel.add(bigBurgerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menuPanel.add(smallBurgerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menuPanel.add(cheeseBurgerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menuPanel.add(bigFriesButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menuPanel.add(smallFriesButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menuPanel.add(bigColaButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menuPanel.add(smallColaButton, gbc);

        // CONFIGURACIÓN DEL PANEL DE LA ORDEN
        totalPriceField.setEditable(false);
        totalPriceField.setText("0.0");

        orderMenuPanel.setLayout(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        orderMenuPanel.add(orderListScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        orderMenuPanel.add(totalPriceLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        orderMenuPanel.add(totalPriceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.CENTER;
        orderMenuPanel.add(deleteOrderButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.CENTER;
        orderMenuPanel.add(saveOrderButton, gbc);

        // CONFIGURACIÓN DE LOS BOTONES
        bigBurgerButton.addActionListener(e -> {
            menuModel.addElement(MenuItem.BIG_BURGER);
            double currentPrice = Double.parseDouble(totalPriceField.getText());
            totalPriceField.setText(String.valueOf(currentPrice + MenuItem.BIG_BURGER.getPrice()));
        });

        smallBurgerButton.addActionListener(e -> {
            menuModel.addElement(MenuItem.SMALL_BURGER);
            double currentPrice = Double.parseDouble(totalPriceField.getText());
            totalPriceField.setText(String.valueOf(currentPrice + MenuItem.SMALL_BURGER.getPrice()));
        });

        cheeseBurgerButton.addActionListener(e -> {
           menuModel.addElement(MenuItem.CHEESE_BURGER);
           double currentPrice = Double.parseDouble(totalPriceField.getText());
           totalPriceField.setText(String.valueOf(currentPrice + MenuItem.CHEESE_BURGER.getPrice()));
        });

        bigFriesButton.addActionListener(e -> {
           menuModel.addElement(MenuItem.BIG_FRIES);
           double currentPrice = Double.parseDouble(totalPriceField.getText());
           totalPriceField.setText(String.valueOf(currentPrice + MenuItem.BIG_FRIES.getPrice()));
        });

        smallFriesButton.addActionListener(e -> {
            menuModel.addElement(MenuItem.SMALL_FRIES);
            double currentPrice = Double.parseDouble(totalPriceField.getText());
            totalPriceField.setText(String.valueOf(currentPrice + MenuItem.SMALL_FRIES.getPrice()));
        });

        bigColaButton.addActionListener(e -> {
            menuModel.addElement(MenuItem.BIG_COLA);
            double currentPrice = Double.parseDouble(totalPriceField.getText());
            totalPriceField.setText(String.valueOf(currentPrice + MenuItem.BIG_COLA.getPrice()));
        });

        smallColaButton.addActionListener(e -> {
            menuModel.addElement(MenuItem.SMALL_COLA);
            double currentPrice = Double.parseDouble(totalPriceField.getText());
            totalPriceField.setText(String.valueOf(currentPrice + MenuItem.SMALL_COLA.getPrice()));
        });

        deleteOrderButton.addActionListener(e -> {
            menuModel.removeAllElements();
            totalPriceField.setText("0.0");
        });

        // En un option pane pone un cuadro de texto para confirmar
        // TODO: implementar la GUI del ticket
        saveOrderButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres guardar la orden?", "Confirmar orden", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                // Imprimir un ticket con los datos de la orden
                StringBuilder builder = new StringBuilder();
                builder.append("======== Ticket de orden =======\n");

                // Recorrer cada elemento de la orden y agregarlo al string
                int orderSize = menuModel.getSize();
                for (int i = 0; i < orderSize; i++) {
                    // Obtener el item para no llamar más veces a getElementAt
                    MenuItem menuItem = menuModel.getElementAt(i);

                    // Extra: para hacer que el ticket se vea mejor con los precios separados para legibilidad
                    String tabs;
                    int itemNameLength = menuItem.getName().length();
                    if (itemNameLength <= 17) {
                        tabs = "  \t\t";
                    } else if (itemNameLength >= 21) {
                        tabs = "\t";
                    } else  {
                        tabs = "\t\t";
                    }

                    // Para evitar string concatenation en el builder se recomienda hacer varias llamadas a append en cadena
                    builder.append(" - ").append(menuItem.getName()).append(tabs).append(menuItem.getPrice()).append("\n");
                }
                builder.append("\nTotal a pagar: ").append(totalPriceField.getText()).append("\n================================\n");

                // Abrir la ventana para seleccionar la impresora
                PrinterView printerView = new PrinterView(this); // La instancia de este frame es el padre de este JDialog
                printerView.setContentToPrint(builder.toString()); // Se le pasa el string armado con el builder que es lo que se quiere imprimir
                printerView.setVisible(true);
            }
        });

        // CONFIGURACIÓN DEL PANEL CONTENEDOR
        containerPanel.setLayout(new GridLayout(1,2));
        containerPanel.add(menuPanel);
        containerPanel.add(orderMenuPanel);

        this.add(containerPanel);

        // CONFIGURACIÓN DEL FRAME
        this.setSize(640, 480);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
}
