package Presentation.View.PanelsDeReservas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelEstadisticas extends JPanel {


    // COMPONENTES GENERALES
    private JTextField campoFechaInicio;
    private JTextField campoFechaFin;

    private JButton botonCargar;
    private JButton botonImprimir;


    // ESTADÍSTICA DE RECURSOS
    private JTable tablaRecursos;
    private DefaultTableModel modeloTablaRecursos;

    private JPanel panelGraficoRecursos;


    // ESTADÍSTICA DE ACTIVIDADES
    private JTable tablaActividades;
    private DefaultTableModel modeloTablaActividades;

    private JPanel panelGraficoActividades;


    // CONSTRUCTOR
    public PanelEstadisticas() {

        setLayout(new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        crearPanelFiltros();
        crearContenido();
    }


    // PANEL DE FILTROS
    private void crearPanelFiltros() {

        JPanel panelFiltros = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        JLabel labelFechaInicio =
                new JLabel("Fecha inicio:");

        campoFechaInicio =
                new JTextField(10);

        campoFechaInicio.setToolTipText(
                "Formato: dd/MM/yyyy"
        );

        JLabel labelFechaFin =
                new JLabel("Fecha fin:");

        campoFechaFin =
                new JTextField(10);

        campoFechaFin.setToolTipText(
                "Formato: dd/MM/yyyy"
        );

        botonCargar =
                new JButton("Cargar");

        botonImprimir =
                new JButton("Imprimir PDF");

        panelFiltros.add(labelFechaInicio);
        panelFiltros.add(campoFechaInicio);

        panelFiltros.add(labelFechaFin);
        panelFiltros.add(campoFechaFin);

        panelFiltros.add(botonCargar);
        panelFiltros.add(botonImprimir);

        add(
                panelFiltros,
                BorderLayout.NORTH
        );
    }

    // CONTENIDO
    private void crearContenido() {

        JTabbedPane pestañas =
                new JTabbedPane();


        // PESTAÑA RECURSOS

        JPanel panelRecursos =
                new JPanel(new BorderLayout(10, 10));

        modeloTablaRecursos =
                new DefaultTableModel(
                        new Object[][]{},
                        new String[]{
                                "Categoría",
                                "Cantidad"
                        }
                ) {
                    @Override
                    public boolean isCellEditable(
                            int fila,
                            int columna) {

                        return false;
                    }
                };

        tablaRecursos = new JTable(modeloTablaRecursos);

        tablaRecursos.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollRecursos = new JScrollPane(tablaRecursos);

        panelGraficoRecursos = new JPanel();

        panelGraficoRecursos.setLayout(new BorderLayout());

        panelRecursos.add(scrollRecursos, BorderLayout.WEST);

        panelRecursos.add(panelGraficoRecursos, BorderLayout.CENTER);

        pestañas.addTab("Recursos por categoría", panelRecursos);


        // PESTAÑA ACTIVIDADES

        JPanel panelActividades = new JPanel(new BorderLayout(10, 10));

        modeloTablaActividades = new DefaultTableModel(new Object[][]{}, new String[]{"Semana", "Cantidad de actividades"}) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaActividades = new JTable(modeloTablaActividades);

        tablaActividades.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollActividades = new JScrollPane(tablaActividades);

        panelGraficoActividades = new JPanel();

        panelGraficoActividades.setLayout(new BorderLayout());

        panelActividades.add(scrollActividades, BorderLayout.WEST);

        panelActividades.add(panelGraficoActividades, BorderLayout.CENTER);

        pestañas.addTab("Actividades por semana", panelActividades);

        add(pestañas, BorderLayout.CENTER);
    }


    // GETTERS

    public JTextField getCampoFechaInicio() {
        return campoFechaInicio;
    }

    public JTextField getCampoFechaFin() {
        return campoFechaFin;
    }

    public JButton getBotonCargar() {
        return botonCargar;
    }

    public JButton getBotonImprimir() {
        return botonImprimir;
    }

    public JTable getTablaRecursos() {
        return tablaRecursos;
    }

    public JTable getTablaActividades() {
        return tablaActividades;
    }

    public JPanel getPanelGraficoRecursos() {
        return panelGraficoRecursos;
    }

    public JPanel getPanelGraficoActividades() {
        return panelGraficoActividades;
    }


    // MOSTRAR DATOS
    public void mostrarRecursos(
            Object[][] datos) {

        modeloTablaRecursos.setRowCount(0);

        for (Object[] fila : datos) {

            modeloTablaRecursos.addRow(fila);
        }
    }

    public void mostrarActividades(
            Object[][] datos) {

        modeloTablaActividades.setRowCount(0);

        for (Object[] fila : datos) {

            modeloTablaActividades.addRow(fila);
        }
    }


    // MOSTRAR GRÁFICOS
    public void mostrarGraficoRecursos(
            JPanel grafico) {

        panelGraficoRecursos.removeAll();

        panelGraficoRecursos.add(grafico, BorderLayout.CENTER);

        panelGraficoRecursos.revalidate();
        panelGraficoRecursos.repaint();
    }

    public void mostrarGraficoActividades(
            JPanel grafico) {

        panelGraficoActividades.removeAll();

        panelGraficoActividades.add(grafico, BorderLayout.CENTER);

        panelGraficoActividades.revalidate();
        panelGraficoActividades.repaint();
    }
}

