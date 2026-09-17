package Presentation.View.PanelsDeReservas.SistemaDeReservas;
import Presentation.Model.Categoria.Categoria;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.Border;
import java.awt.*;


public class PanelRecursos extends JPanel {
    private static final Border BORDER_OK    =                // borde para campos válidos (gris)
            BorderFactory.createLineBorder(new Color(180, 180, 180));
    private static final Border BORDER_ERROR =                // borde para campos inválidos (rojo, 2px)
            BorderFactory.createLineBorder(Color.RED, 2);

    private JButton botonBuscar;
    private JButton botonGuardarRecurso;
    private JButton botonBorrarRecurso;
    private JButton botonLimpiar;

    private JTextField campoBuscarDescripcion;
    private JTextField campoGuardarID;
    private JTextField campoGuardarDescripcion;

    private DefaultTableModel datosDeLaTabla;
    private JComboBox<Categoria> comboCategoria;

    public PanelRecursos() {
        setLayout(new GridLayout(3,1,8,8));
        construirPanel();
    }
    public void construirPanel() {
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        JPanel panelGuardar = new JPanel(new GridLayout(4,2,8,8));
        JPanel panelTabla = new JPanel(new BorderLayout());
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 4, 4));

        botonBuscar = new JButton("Buscar");
        botonGuardarRecurso = new JButton("Guardar");
        botonBorrarRecurso = new JButton("Borrar");
        botonLimpiar = new JButton("Limpiar");

        campoBuscarDescripcion = new JTextField(10);
        campoGuardarID = new JTextField(10);
        campoGuardarDescripcion = new JTextField(10);
        comboCategoria = new JComboBox<>();

        //Panel buscar
        panelBuscar.add(new JLabel("Categoria"));
        panelBuscar.add(comboCategoria);
        panelBuscar.add(campoBuscarDescripcion);
        panelBuscar.add(botonBuscar);

        //Panel guardar-borrar
        panelGuardar.add(new JLabel("  ID"));
        panelGuardar.add(campoGuardarID);
        panelGuardar.add(new JLabel("  Categoria"));
        panelGuardar.add(comboCategoria);
        panelGuardar.add(new JLabel("  Descripcion"));
        panelGuardar.add(campoGuardarDescripcion);
        panelGuardar.add(botonGuardarRecurso);
        panelBotones.add(botonBorrarRecurso);
        panelBotones.add(botonLimpiar);
        panelGuardar.add(panelBotones);
        panelGuardar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        panelTabla.add(new JLabel("Datos de Recursos: "), BorderLayout.NORTH);
        String[] columnas = {"ID", "Categoria", "Descripcion" };
        datosDeLaTabla = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(datosDeLaTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        panelTabla.add(scroll);

        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setBackground(Color.LIGHT_GRAY);
        add(panelBuscar);
        add(panelGuardar);
        add(panelTabla);

    }
    public void cargarCategorias(List<Categoria> categorias) {

        comboCategoria.removeAllItems();

        for (Categoria categoria : categorias) {
            comboCategoria.addItem(categoria);
        }
    }
    public Categoria getCategoriaSeleccionada() {
        return (Categoria) comboCategoria.getSelectedItem();
    }
    public JButton getBotonBuscar() { return botonBuscar; }
    public JButton getBotonGuardarRecurso() { return botonGuardarRecurso; }
    public JButton getBotonLimpiar() { return botonLimpiar; }
    public JTextField getCampoBuscarDescripcion() { return campoBuscarDescripcion; }
    public JTextField getCampoGuardarID() { return campoGuardarID; }
    public JTextField getCampoGuardarDescripcion() { return campoGuardarDescripcion; }
    public JComboBox<Categoria> getComboCategoria() { return comboCategoria; }
    public void limpiarTabla() {
        datosDeLaTabla.setRowCount(0);
    }
    public void agregarDatosATabla(String id,Categoria categoria, String descripcion) {

        datosDeLaTabla.addRow(new Object[]{
                id,
                categoria,
                descripcion
        });
    }

}
