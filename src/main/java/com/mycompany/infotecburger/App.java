package com.mycompany.infotecburger;

import javax.swing.SwingUtilities;

public class App {
    static void main() {
        // Los programas que tienen una interfaz de swing, deben iniciar las interfaces con invokeLater
        // Se asegura que el código relacionado con la GUI se ejecuta en el Event Dispatch Thread
        // Debido a que swing no es "Thread Safe", esto debe ocurrir en el EDT para evitar problemas de concurrencia o errores impredecibles
        // Requiere la instancia de una clase anónima Runnable que debe implementar el método 'run'
        // El código se puede simplificar a una lambda function, ya que run es una 'Interfaz Funcional'
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}
