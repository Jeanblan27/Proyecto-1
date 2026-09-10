package Presentation.View.PanelsDeReservas.SistemaReservasFuncionario;


import View.PanelsDeReservas.PanelActividades;
import View.PanelsDeReservas.PanelCalendarizacion;
import View.PanelsDeReservas.PanelEstadisticas;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class SistemaReservaFuncionario extends JFrame {

    private static final Border BORDER_OK    =                // borde para campos válidos (gris)
            BorderFactory.createLineBorder(new Color(180, 180, 180));
    private static final Border BORDER_ERROR =                // borde para campos inválidos (rojo, 2px)
            BorderFactory.createLineBorder(Color.RED, 2);

    private JPanel panelDeBotones;
    private JPanel paneles;

    private JButton botonCalendarizacion;
    private JButton botonActividades;
    private JButton botonEstadisticas;
    private JButton botonReserva;

    private PanelCalendarizacion panelCalendarizacion;
    private PanelActividades panelActividades;
    private PanelEstadisticas panelEstadisticas;
    private PanelReservas panelReservas;

    private CardLayout cardLayout ;

    public SistemaReservaFuncionario() {
        setTitle("Reserva");
        setSize(650, 650);
        setContentPane(PanelReserva());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public JPanel PanelReserva() {
        //Panel principal
        panelDeBotones = new JPanel(new GridLayout(1,5,8,8));
        //Agrega los botones al panel

        botonReserva = new JButton("Reserva");
        panelDeBotones.add(botonReserva);
        botonCalendarizacion = new JButton("Calendarizacion");
        panelDeBotones.add(botonCalendarizacion);
        botonActividades = new JButton("Actividades");
        panelDeBotones.add(botonActividades);
        botonEstadisticas = new JButton("Estadisticas");
        panelDeBotones.add(botonEstadisticas);


        //Se agrega cardLayout para tener varios paneles
        cardLayout = new CardLayout();
        paneles = new JPanel(cardLayout );

        panelReservas = new PanelReservas();
        paneles.add(panelReservas,"RESERVAS");
        //Cuarto panel, Calendarizacion
        panelCalendarizacion = new PanelCalendarizacion();
        paneles.add(panelCalendarizacion, "CALENDARIZACION");

        //Quinto panel, Actividades
        panelActividades = new PanelActividades();
        paneles.add(panelActividades, "ACTIVIDADES");

        //Sexto panel, Estadisticas
        panelEstadisticas = new PanelEstadisticas();
        paneles.add(panelEstadisticas, "ESTADISTICAS");

        //Panel principal
        JPanel raiz = new JPanel(new BorderLayout(8,8));
        raiz.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        raiz.add(panelDeBotones,BorderLayout.NORTH);
        raiz.add(paneles);
        return raiz;
    }


    public CardLayout getCardLayout() {
        return cardLayout;
    }
    public JPanel getPaneles() {
        return paneles;
    }
    public void mostrar() {
        setVisible(true);
    }

    public JButton getBotonCalendarizacion() { return botonCalendarizacion; }
    public JButton getBotonActividades() { return botonActividades; }
    public JButton getBotonEstadisticas() { return botonEstadisticas; }
    public JButton getBotonReserva() { return botonReserva; }

    public PanelCalendarizacion getPanelCalendarizacion() { return panelCalendarizacion; }
    public PanelActividades getPanelActividades() { return panelActividades; }
    public PanelEstadisticas getPanelEstadisticas() { return panelEstadisticas; }
    public PanelReservas getPanelReservas() { return panelReservas; }

}