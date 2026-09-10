package Presentation.View;

import javax.swing.*;
import java.awt.*;

public class CambiarClave {
    private JFrame frame;
    private JTextField claveActual;
    private JTextField nuevaClave;
    private JButton botonCambiarClave;
    private JButton botonCancelar;

    public CambiarClave() {
        frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setContentPane(clavePanel());
        frame.pack();
        frame.setSize(350,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public JPanel clavePanel() {
        JPanel panel = new JPanel(new GridLayout(3,2,6,6));

        JLabel claveActualLabel = new JLabel("Clave Actual:");
        claveActual = new JTextField(10);
        panel.add(claveActualLabel);
        panel.add(claveActual);

        JLabel nuevaClavelabel = new JLabel("Nueva Clave:");
        panel.add(nuevaClavelabel);
        nuevaClave = new JTextField(10);
        panel.add(nuevaClave);

        JPanel panelBotones = new JPanel();
        botonCambiarClave = new JButton("Cambiar Clave");
        panelBotones.add(botonCambiarClave);

        botonCancelar = new JButton("Cancelar");
        panelBotones.add(botonCancelar);

        JPanel raiz = new JPanel(new BorderLayout(8,8));
        raiz.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        raiz.add(panel, BorderLayout.CENTER);
        raiz.add(panelBotones, BorderLayout.SOUTH);
        return raiz;
    }
    public String getClaveActual() {
        return claveActual.getText();
    }
    public String getNuevaClave() {
        return nuevaClave.getText();
    }
    public JButton getCambiarClave() {
        return botonCambiarClave;
    }
    public JButton getCancelar() { return botonCancelar; }
    public void dispose() { frame.dispose(); }
    public void mostrar() {
        frame.setVisible(true);
    }
}
