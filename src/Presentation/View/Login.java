package Presentation.View;


import javax.swing.*;
import java.awt.*;

public class Login {
    private final JFrame frame;

    private JTextField campoClave;
    private JTextField campoId;
    private JButton botonIngresar;
    private JButton botonCancelar;
    private JButton botonCambiarClave;

    public Login() {
        frame = new JFrame("SISTEMA DE RESERVAS");
        frame.setLocationRelativeTo(null);
        frame.setContentPane(LoginPanel());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350,200);
        frame.setVisible(true);
    }
    public JPanel LoginPanel(){
        JPanel panel = new JPanel(new GridLayout(2,2,1,22));

        JLabel lblId = new JLabel("ID");
        JLabel lblClave = new JLabel("Clave");
        campoClave = new JTextField(10);
        campoId = new JTextField(10);

        panel.add(lblId);
        panel.add(campoId);

        panel.add(lblClave);
        panel.add(campoClave);

        JPanel panelBotones = new JPanel();
        botonIngresar = new JButton("Ingresar");
        botonCancelar = new JButton("Cancelar");
        botonCambiarClave = new JButton("Cambiar clave");
        panelBotones.add(botonIngresar);
        panelBotones.add(botonCancelar);
        panelBotones.add(botonCambiarClave);


        JPanel raiz = new JPanel(new BorderLayout(8,8));
        raiz.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        raiz.add(panel, BorderLayout.CENTER);
        raiz.add(panelBotones, BorderLayout.SOUTH);
        return raiz;
    }
    public String getClave() {
        return campoClave.getText();
    }
    public String getId() {
        return campoId.getText();
    }
    public JButton getBotonIngresar() {
        return botonIngresar;
    }
    public JButton getBotonCancelar() {
        return botonCancelar;
    }
    public JButton getBotonCambiarClave() {
        return botonCambiarClave;
    }


    public void mostrar() {
        frame.setVisible(true);
    }
    public void dispose() { frame.dispose(); }
}
