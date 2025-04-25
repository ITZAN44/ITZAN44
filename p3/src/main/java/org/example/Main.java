package org.example;

import org.example.imagenes.gui.ServidorWebGUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new ServidorWebGUI().setVisible(true);
        });
    }
}