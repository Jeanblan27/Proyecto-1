package Presentation.View.PanelsDeReservas;

import Presentation.Model.Categoria.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelCalendarizacion extends JPanel {

    private JComboBox<Categoria> comboCategoria;
    private JTextField campoFecha;

    private JButton botonCargar;
    private JButton botonImprimir;

    private JTable tablaCalendarizacion;
    private DefaultTableModel modeloTabla;

    public PanelCalendarizacion() {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        crearPanelSuperior();
        crearTabla();
    }

    private void crearPanelSuperior() {

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel labelCategoria = new JLabel("Categoría:");

        comboCategoria = new JComboBox<>();

        JLabel labelFecha = new JLabel("Fecha:");

        campoFecha = new JTextField(10);
        campoFecha.setToolTipText("Formato: dd/MM/yyyy");

        botonCargar = new JButton("Cargar");
        botonImprimir = new JButton("Imprimir");

        panelFiltros.add(labelCategoria);
        panelFiltros.add(comboCategoria);

        panelFiltros.add(labelFecha);
        panelFiltros.add(campoFecha);

        panelFiltros.add(botonCargar);
        panelFiltros.add(botonImprimir);

        add(panelFiltros, BorderLayout.NORTH);
    }

    private void crearTabla() {

        modeloTabla = new DefaultTableModel();

        tablaCalendarizacion = new JTable(modeloTabla);

        tablaCalendarizacion.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaCalendarizacion.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tablaCalendarizacion);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Carga las categorías recibidas desde el controlador.
     */
    public void cargarCategorias(List<Categoria> categorias) {

        comboCategoria.removeAllItems();

        for (Categoria categoria : categorias) {
            comboCategoria.addItem(categoria);
        }
    }

    /**
     * Muestra la matriz de calendarización en la JTable.
     */
    public void mostrarMatriz(String[] columnas, Object[][] datos) {

        modeloTabla.setDataVector(datos, columnas);

        tablaCalendarizacion.setModel(modeloTabla);

        tablaCalendarizacion.setRowHeight(25);
        tablaCalendarizacion.getTableHeader().setReorderingAllowed(false);
    }

    public JComboBox<Categoria> getComboCategoria() {
        return comboCategoria;
    }

    public JTextField getCampoFecha() {
        return campoFecha;
    }

    public JButton getBotonCargar() {
        return botonCargar;
    }

    public JButton getBotonImprimir() {
        return botonImprimir;
    }

    public JTable getTablaCalendarizacion() {
        return tablaCalendarizacion;
    }
}
