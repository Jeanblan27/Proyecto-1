
package Presentation.View.PanelsDeReservas.SistemaDeReservas;

import Presentation.View.PanelsDeReservas.PanelActividades;
import Presentation.View.PanelsDeReservas.PanelCalendarizacion;
import Presentation.View.PanelsDeReservas.PanelEstadisticas;
import Presentation.View.PanelsDeReservas.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class SistemaDeReservas {

    private static final Border BORDER_OK    =                // borde para campos válidos (gris)
            BorderFactory.createLineBorder(new Color(180, 180, 180));
    private static final Border BORDER_ERROR =                // borde para campos inválidos (rojo, 2px)
            BorderFactory.createLineBorder(Color.RED, 2);


    private final JFrame frame;
    private JPanel panelDeBotones;
    private JPanel paneles;

    private JButton botonFuncionarios;
    private JButton botonCategorias;
    private JButton botonRecursos;
    private JButton botonCalendarizacion;
    private JButton botonActividades;
    private JButton botonEstadisticas;

    private PanelFuncionarios panelFuncionario;
    private PanelCategorias panelCategoria;
    private PanelRecursos panelRecursos;
    private PanelCalendarizacion panelCalendarizacion;
    private PanelActividades panelActividades;
    private PanelEstadisticas panelEstadisticas;


    private CardLayout cardLayout ;

    public SistemaDeReservas() {
        frame = new JFrame("Reserva");
        frame.setSize(650, 650);
        frame.setContentPane(PanelReserva());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public JPanel PanelReserva() {
        //Panel principal
        panelDeBotones = new JPanel(new GridLayout(1,5,8,8));

        //Agrega los botones al panel
        botonFuncionarios = new JButton("Funcionarios");
        panelDeBotones.add(botonFuncionarios);
        botonCategorias = new JButton("Categorias");
        panelDeBotones.add(botonCategorias);
        botonRecursos = new JButton("Recursos");
        panelDeBotones.add(botonRecursos);
        botonCalendarizacion = new JButton("Calendarizacion");
        panelDeBotones.add(botonCalendarizacion);
        botonActividades = new JButton("Actividades");
        panelDeBotones.add(botonActividades);
        botonEstadisticas = new JButton("Estadisticas");
        panelDeBotones.add(botonEstadisticas);

        //Se agrega cardLayout para tener varios paneles
        cardLayout = new CardLayout();
        paneles = new JPanel(cardLayout );

        //Primer panel, Funcionarios
        panelFuncionario = new PanelFuncionarios();
        paneles.add(panelFuncionario, "FUNCIONARIOS");

        //Segundo panel, Categorias
        panelCategoria = new PanelCategorias();
        paneles.add(panelCategoria, "CATEGORIAS");

        //Tercer panel, Recursos
        panelRecursos = new PanelRecursos();
        paneles.add(panelRecursos, "RECURSOS");

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
        frame.setVisible(true);
    }
    public JButton getBotonFuncionarios() {
        return botonFuncionarios;
    }
    public JButton getBotonCategorias() {
        return botonCategorias;
    }
    public JButton getBotonRecursos() { return botonRecursos; }
    public JButton getBotonCalendarizacion() { return botonCalendarizacion; }
    public JButton getBotonActividades() { return botonActividades; }
    public JButton getBotonEstadisticas() { return botonEstadisticas; }
    public PanelFuncionarios getPanelFuncionarios() { return panelFuncionario; }
    public PanelCategorias getPanelCategoria() { return panelCategoria; }
    public PanelRecursos getPanelRecursos() { return panelRecursos; }
    public PanelCalendarizacion getPanelCalendarizacion() { return panelCalendarizacion; }
    public PanelActividades getPanelActividades() { return panelActividades; }
    public PanelEstadisticas getPanelEstadisticas() { return panelEstadisticas; }

}