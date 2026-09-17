package Presentation.View.PanelsDeReservas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelActividades extends JPanel {

    private JTextField campoFechaReferencia;

    private JButton botonCargar;
    private JButton botonImprimir;

    private JTable tablaActividades;
    private DefaultTableModel modeloTabla;

    public PanelActividades() {

        setLayout(new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );

        crearPanelSuperior();
        crearTabla();
    }

    private void crearPanelSuperior() {

        JPanel panelFiltros = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        JLabel labelFecha = new JLabel(
                "Fecha de referencia:"
        );

        campoFechaReferencia = new JTextField(10);

        campoFechaReferencia.setToolTipText(
                "Formato: dd/MM/yyyy"
        );

        botonCargar = new JButton(
                "Cargar"
        );

        botonImprimir = new JButton(
                "Imprimir"
        );

        panelFiltros.add(labelFecha);
        panelFiltros.add(campoFechaReferencia);
        panelFiltros.add(botonCargar);
        panelFiltros.add(botonImprimir);

        add(
                panelFiltros,
                BorderLayout.NORTH
        );
    }

    private void crearTabla() {

        modeloTabla = new DefaultTableModel();

        tablaActividades = new JTable(
                modeloTabla
        );

        tablaActividades.setRowHeight(30);

        tablaActividades.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        tablaActividades
                .getTableHeader()
                .setReorderingAllowed(false);

        JScrollPane scrollPane =
                new JScrollPane(tablaActividades);

        add(
                scrollPane,
                BorderLayout.CENTER
        );
    }

    /**
     * Muestra la matriz semanal de actividades.
     */
    public void mostrarMatriz(
            String[] columnas,
            Object[][] datos) {

        modeloTabla.setDataVector(
                datos,
                columnas
        );

        tablaActividades.setModel(
                modeloTabla
        );

        tablaActividades.setRowHeight(30);

        tablaActividades
                .getTableHeader()
                .setReorderingAllowed(false);
    }

    public JTextField getCampoFechaReferencia() {
        return campoFechaReferencia;
    }

    public JButton getBotonCargar() {
        return botonCargar;
    }

    public JButton getBotonImprimir() {
        return botonImprimir;
    }

    public JTable getTablaActividades() {
        return tablaActividades;
    }
}

