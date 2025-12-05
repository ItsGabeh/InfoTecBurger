package com.mycompany.infotecburger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Esta clase es el controlador CRUD para usuarios en la base de datos.
 * Debido a que este programa es un ejemplo de conexion de base de datos,
 * el controlador no provee hashing de contraseñas para guardar de manera segura
 * la contraseña en la BD.
 * @author Gabeh
 */
public class UserController {
    // Aquí se declaran constantes con los datos de la base de datos para conectarse a ella
    // Si quiere probar el código reemplace estas constantes con su conexión
    private static final String DB_URL = "jdbc:mysql://host:puerto/db";
    private static final String USER = "usuario";
    private static final String PASS = "contraseña";

    // Un bloque estático se ejecuta una sola vez cuando la clase es cargada por el ClassLoader
    // y se usa principalmente para cargar recursos requeridos por la clase
    // Necesitamos cargar dinámicamente la clase del driver en Runtime
    // esto ya no es necesario para las versiones 4.0+ del driver
    // los drivers modernos se cargan a sí mismos, pero se puede poner por si se necesita retrocompatibilidad
    // se puede comentar el bloque estático y funcionará igual
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Esta clase es una clase dentro de otra clase o "inner class"
    // Se usan para agrupar funcionalidad específica de una clase
    // En este caso representa los datos de un usuario en la BD
    public static class Usuario {
        private int id;
        private String username, password;
        public Usuario(int id, String username, String password) {
            this.id = id;
            this.username = username;
            this.password = password;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // Este método auxiliar se usa para conectarse a la base de datos
    // getConnection tira una excepción si el acceso a la base de datos es nulo
    // para evitar usar un try catch aquí se puede agregar la palabra throws seguido
    // del tipo de exception que tira para manejarla en otra función que llame a este método
    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    };

    public void crearUsuario(String username, String password) {
        String sql = "INSERT INTO usuarios (usuario, pass) VALUES (?, ?)";
        // Este try catch se llama try-with-resources
        // Se usa para declarar uno o más recursos, asegurando que se cierren al final de la ejecución del bloque try
        // Para que un recurso sea cerrado debe asegurarse que implemente la interfaz AutoClosable, que incluye a todos los objetos que implementan Closable
        try(Connection conn = conectar()) {
            // Un PreparedStatement es una sentencia SQL pre-compilada, se compila una vez y se usa multiples veces eficientemente
            // usa placeholders "?" para agregar dinámicamente parámetros a la sentencia, incluso ayuda a evitar ataques SQL injection
            // manejando el input de manera segura a través de métodos Setter que ignoran caracteres de escape
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.execute();
            System.out.println("Usuario creado");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Usuario obtenerPorUsuario(String usuario) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ?";
        try (Connection conn = conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);

            // Un ResultSet es un objeto que representa el resultado de ejecutar una query sql a una tabla
            // Mantiene un "cursor" que apunta a la columna actual, y se puede mover este cursor a través de métodos como next, previous, first o last
            // Se puede acceder a los datos de la columna a través de metodos Getters que acceden a la columna por nombre o index
            ResultSet rs = stmt.executeQuery();

            // Si el result set no está vacío el método next no devolverá false
            if (rs.next()) {
                return new Usuario(rs.getInt("Id"), rs.getString("usuario"), rs.getString("pass"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Si la conexión falla o no se encuentra ningún usuario se devuelve null;
        return null;
    }

    public Usuario obtenerPorUsuarioYPassword(String usuario, String password) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? and  pass = ?";
        try (Connection conn = conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(rs.getInt("Id"), rs.getString("usuario"), rs.getString("pass"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
