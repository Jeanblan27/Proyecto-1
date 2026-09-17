package Presentation.View;

import javax.swing.*;
import java.awt.*;

public class CambiarClave {

    private JFrame frame;

    private JTextField claveActual;
    private JTextField nuevaClave;
    private JTextField confirmarClave;

    private JButton botonCambiarClave;
    private JButton botonCancelar;


    public CambiarClave() {

        frame = new JFrame("Cambiar Clave");

        frame.setContentPane(clavePanel());
        frame.pack();
        frame.setSize(400, 220);
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


    public JPanel clavePanel() {

        JPanel panel = new JPanel(
                new GridLayout(4, 2, 6, 6)
        );


        // CLAVE ACTUAL
        JLabel claveActualLabel =
                new JLabel("Clave Actual:");

        claveActual = new JTextField(10);

        panel.add(claveActualLabel);
        panel.add(claveActual);


        // NUEVA CLAVE
        JLabel nuevaClaveLabel =
                new JLabel("Nueva Clave:");

        nuevaClave = new JTextField(10);

        panel.add(nuevaClaveLabel);
        panel.add(nuevaClave);


        // CONFIRMAR CLAVE
        JLabel confirmarClaveLabel =
                new JLabel("Confirmar Clave:");

        confirmarClave = new JTextField(10);

        panel.add(confirmarClaveLabel);
        panel.add(confirmarClave);


        // BOTONES
        JPanel panelBotones = new JPanel();

        botonCambiarClave =
                new JButton("Cambiar Clave");

        botonCancelar =
                new JButton("Cancelar");

        panelBotones.add(botonCambiarClave);
        panelBotones.add(botonCancelar);


        // PANEL PRINCIPAL
        JPanel raiz =
                new JPanel(new BorderLayout(8, 8));

        raiz.setBorder(
                BorderFactory.createEmptyBorder(
                        8, 8, 8, 8
                )
        );

        raiz.add(
                panel,
                BorderLayout.CENTER
        );

        raiz.add(
                panelBotones,
                BorderLayout.SOUTH
        );


        return raiz;
    }


    public String getClaveActual() {
        return claveActual.getText();
    }


    public String getNuevaClave() {
        return nuevaClave.getText();
    }


    public String getConfirmarClave() {
        return confirmarClave.getText();
    }


    public JButton getCambiarClave() {
        return botonCambiarClave;
    }


    public JButton getCancelar() {
        return botonCancelar;
    }


    public void dispose() {
        frame.dispose();
    }


    public void mostrar() {
        frame.setVisible(true);
    }
}

