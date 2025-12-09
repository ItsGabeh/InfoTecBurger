package com.mycompany.infotecburger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Objects;

/**
 * Esta clase es el JFrame para el login, no se usó un Form para crear la interfaz.
 * @author Gabeh
 */
public class LoginView extends JFrame {
    // Aquí se declaran los componentes Swing que se van a ocupar en esta interfaz
    JPanel containerPanel; // Este panel sirve para contener a todos los demás paneles
    JPanel formPanel; // Este panel contiene todos los componentes que tienen el formulario de login
    JPanel logoPanel; // Este panel va a ser un panel que muestra la imagen del logo de la aplicación

    // Componentes del Form
    JTextField usernameField;
    JPasswordField passwordField;
    JLabel usernameLabel;
    JLabel passwordLabel;
    JButton loginButton; // El botón que llama al login
    JButton registerButton; //El botón que se usa para registrar
    JLabel errorLabel; // Este label se va a usar como un label que va a mostrar un error si el registro o el login falla

    // Componentes del Panel del logo
    JLabel imageLabel; // este label se usa para hacer un display de la imagen en el panel

    // Controlador de usuario usado para el login o el registro
    UserController userController;

    // Este es el constructor de la clase del Frame
    public LoginView() {
        super("InfoTec Burger: Inicio de sesión"); // Llamar al constructor de la clase JFrame
        initComponents();
    }

    // Siguiendo la convención de NetBeans, initComponentes se encarga de inicializar todos los componentes del frame
    // Este se llama en el constructor del Frame
    private void initComponents() {
        // Inicializar los componentes
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        usernameLabel = new JLabel("Usuario:");
        passwordLabel = new JLabel("Contraseña:");
        loginButton = new JButton("Login");
        registerButton = new JButton("Registrar");
        errorLabel = new JLabel("");
        containerPanel = new JPanel();
        formPanel = new JPanel();
        logoPanel = new JPanel();
        imageLabel = new JLabel();

        // Crear una instancia del controlador
        userController =  new UserController();

        // Configuración del layout
        // Al estar programa la interfaz con código puro
        // Se debe optar por un layout que ayude a posicionar automáticamente los componentes

        // CONFIGURACIÓN DEL PANEL DEL FORM
        // GridBagLayout es un gestor de diseño flexible que permite colocar los componentes en una cuadrícula dinámica
        // el layout será una columna y cada componente se ubicará en una fila
        // los botones estarán en la última fila y cada uno ocupará una columna
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Es una clase que se usa para especificar las restricciones de diseño
        gbc.insets = new Insets(5, 5, 5, 5); // Agregar este "margin" a cada componente, como en css

        gbc.gridx = 0; // gridx define en que "columna" se coloca el componente
        gbc.gridy = 0; // gridy define en que "fila" se coloca el componente
        gbc.gridwidth = 2; // cuantas columnas ocupa el objeto
        gbc.gridheight = 1; // cuantas filas ocupa el objeto
        gbc.anchor = GridBagConstraints.WEST; // Donde empieza el objeto
        gbc.fill = GridBagConstraints.HORIZONTAL; // El objeto se extiende horizontalmente para ocupar el espacio disponible
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.CENTER;
        formPanel.add(registerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.CENTER;
        formPanel.add(errorLabel, gbc);

        // CONFIGURACIÓN DEL PANEL DEL LOGO
        // Añadir el label de la imagen en el panel del logo
        // Aquí se añade un BorderLayout porque más adelante si la imagen no carga se quiere mostrar un label centrado en el panel
        // El borderLayout organiza los componentes en cinco regiones NORTH, SOUTH, EAST, WEST, y CENTER
        // Si solo tenemos un componente en el centro esté se expande a usar todo el espacio desde el centro
        logoPanel.setLayout(new BorderLayout());
        logoPanel.add(imageLabel,  BorderLayout.CENTER);


        // Para mostrar una imagen que cubra completamente en un JPanel la solución recomendada es usar una clase custom que extienda de JPanel
        // y que cargue una BufferedImage
        // Para tener todo en esta misma clase, la solución va a ser usar un JLabel que puede renderizar un ImageIcon,
        // pero debido a que debemos redimensionar la imagen, debemos añadir un listener al panel para cuando cambie de tamaño
        // si no se hace así el tamaño del panel será cero, ya que el Frame no ha sido empaquetado o puesto en visible
        // Es por eso que el código se ejecutará en el momento en que el panel sufra un escalamiento
        // Aquí no se puede reemplazar con lambda porque ComponentListener no es una interfaz funcional
        logoPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                ImageIcon image = null;
                try {
                    // Puede que la imagen sea muy corta o muy larga para cubrir el espacio que tiene disponible el panel
                    // En este caso se debe hacer un resize
                    // Leer la imagen desde el folder resources, cuanto se hace un build resources se combina con el folder que contiene el package
                    // Aquí puede reemplazar la imagen con su propia imagen
                    ImageIcon originalImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/InfoTec_Burger_Logo_By_GeminiIA.png")));

                    // Extraer el tamaño del panel de la imagen
                    int panelWidth = logoPanel.getWidth();
                    int  panelHeight = logoPanel.getHeight();
                    // Crear una nueva imagen escalada con el tamaño dado para el panel
                    Image newImage = originalImage.getImage().getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                    image = new ImageIcon(newImage);
                } catch (NullPointerException exception) {
                    System.out.println("No se pudo cargar la imagen");
                }

                // Verificar si se pudo cargar la imagen
                if (image != null) {
                    imageLabel.setIcon(image);
                } else {
                    // Si no se carga la imagen, hacemos que el panel por lo menos tenga un fondo para diferenciar el lugar donde va la imagen
                    logoPanel.setBackground(Color.white);
                    imageLabel.setText("Aquí se puede poner un logo :)");
                    imageLabel.setHorizontalAlignment(JLabel.CENTER);
                    imageLabel.setVerticalAlignment(JLabel.CENTER);
                }
            }
        });

        // CONFIGURACIÓN DE LOS BOTONES
        // Aquí se puede evitar el uso de una clase anónima porque ActionListener es una interfaz funcional
        // Para escuchar acciones en la interacción del componente, debe registrarse un listener
        // Cuanto se interactúa con la GUI se llama al método actionPerformed
        loginButton.addActionListener((ActionEvent e) -> {
            // Obtener el texto del usuario y del password
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword()); // getPassword devuelve char[], pero se puede construir un String con el constructor de String

            // Llamar al controlador de usuario con el método de obtener usuario y contraseña
            // La clase usuario está dentro de UserController y se debe declarar el tipo primero con la clase padre UserController
            UserController.Usuario usuario = userController.obtenerPorUsuarioYPassword(username, password);
            if (usuario == null) {
                // System.out.println("Usuario no encontrado");
                errorLabel.setText("Usuario no encontrado");
                errorLabel.setForeground(Color.RED);
            } else {
                // System.out.println("Usuario encontrado: " + usuario.getUsername());
                MainView mainView = new MainView(); // Hacer una instancia de la nueva vista principal
                mainView.setVisible(true);

                this.dispose(); // Libera los recursos para esta ventana sin terminar el programa
                errorLabel.setText("");
            }
        });

        registerButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Si el usuario ya existe no se puede crear el mismo
            UserController.Usuario usuario = userController.obtenerPorUsuario(username);
            // Si no se encontró el usuario se pend crear
            if (usuario == null) {
                errorLabel.setText("");
                userController.crearUsuario(username, password);
            } else {
                // System.out.println("Ese usuario ya está registrado");
                errorLabel.setText("El usuario ya existe");
                errorLabel.setForeground(Color.RED);
            }
        });

        // CONFIGURACIÓN DEL PANEL CONTENEDOR
        // El panel contenedor va a tener un panel a la derecha y el otro a la izquierda
        containerPanel.setLayout(new GridLayout(1, 2)); // Una fila y una columna
        // Añadir los paneles al panel contenedor
        containerPanel.add(formPanel);
        containerPanel.add(logoPanel);

        // CONFIGURACIÓN DEL FRAME
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // Terminar el programa cuando se cierre la ventana
        this.setSize(640, 380); // Darle un tamaño al frame
        this.setResizable(false); // Hacer que no se pueda redimensionar
        this.setLocationRelativeTo(null); // Pone la posición de la ventana relativa a un componente, si es null está en el centro de la pantalla

        // Añadir el panel que contiene a todos los paneles
        this.add(containerPanel);
    }
}
