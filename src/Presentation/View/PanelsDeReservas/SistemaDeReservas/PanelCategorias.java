package Presentation.View.PanelsDeReservas.SistemaDeReservas;

import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;

public class PanelCategorias extends JPanel {

    private static final Border BORDER_OK    =                // borde para campos válidos (gris)
            BorderFactory.createLineBorder(new Color(180, 180, 180));
    private static final Border BORDER_ERROR =                // borde para campos inválidos (rojo, 2px)
            BorderFactory.createLineBorder(Color.RED, 2);

    private JButton btnBuscarCategoria;
    private JButton btnGuardarCategoria;
    private JButton btnEliminarCategoria;
    private JButton btnLimpiar;
    private JTextField campoBusquedaDescripcion;
    private JTextField campoDescripcion;
    private JTextField campoId;
    private DefaultTableModel datosDeLaTabla;

    public PanelCategorias() {
        setLayout(new GridLayout(3,1,8,8));
        construirPanelCategorias();
    }
    public void construirPanelCategorias(){
        JPanel panelBuscarCategoria  = new JPanel();
        JPanel panelGuardarCategoria = new JPanel(new GridLayout(3, 2, 8, 8));
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 4, 4));

        btnBuscarCategoria = new JButton("Buscar");
        btnGuardarCategoria = new JButton("Guardar");
        btnEliminarCategoria = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        campoBusquedaDescripcion = new JTextField(10);
        campoDescripcion = new JTextField(10);
        campoId = new JTextField(30);

        panelBuscarCategoria.setLayout(new FlowLayout());
        panelBuscarCategoria.add(new JLabel("Buscar por descripcion:"));
        panelBuscarCategoria.add(campoBusquedaDescripcion);
        panelBuscarCategoria.add(btnBuscarCategoria);

        panelGuardarCategoria.add(new JLabel(" Descripcion:"));
        panelGuardarCategoria.add(campoDescripcion);
        panelGuardarCategoria.add(new JLabel(" ID:"));
        panelGuardarCategoria.add(campoId);
        panelGuardarCategoria.add(btnGuardarCategoria);
        panelBotones.add(btnEliminarCategoria);
        panelBotones.add(btnLimpiar);
        panelGuardarCategoria.add(panelBotones);
        panelGuardarCategoria.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JPanel datosCategoria = new JPanel(new BorderLayout());
        datosCategoria.add(new JLabel("Datos de las Categorias: "), BorderLayout.NORTH);
        String[] columnas = {"ID", "Descripcion" };
        datosDeLaTabla = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(datosDeLaTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        datosCategoria.add(scroll);
        datosCategoria.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setBackground(Color.LIGHT_GRAY);
        add(panelBuscarCategoria);
        add(panelGuardarCategoria);
        add(datosCategoria);
    }
    public JButton getBtnBuscarCategoria() { return btnBuscarCategoria; }
    public JButton getBtnGuardarCategoria() { return btnGuardarCategoria; }
    public JButton getBtnEliminarCategoria() { return btnEliminarCategoria; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JTextField getCampoBusquedaDescripcion() { return campoBusquedaDescripcion; }
    public JTextField getCampoDescripcion() { return campoDescripcion; }
    public JTextField getCampoId() { return campoId; }
    public void setCampoDescripcion(String descripcion ) {
        campoDescripcion.setText(descripcion);
    }
    public void setCampoId(String descripcion) {
        campoId.setText(descripcion);
    }

    public void agregarDatosATabla(int id, String descrpcion) {

        datosDeLaTabla.addRow(new Object[]{
                id,
               descrpcion
        });
    }
    public void limpiarTabla() {
        datosDeLaTabla.setRowCount(0);
    }

    // --------------------- validaciones ---------------------
    private boolean validarDescripcion() {

        return !campoDescripcion.getText().trim().isEmpty();
    }
    private boolean validarId() {

        return !campoId.getText().trim().isEmpty();
    }
    public boolean validarFormulario() {
        boolean ok = validarDescripcion()
                ;


        marcar(campoDescripcion,       validarDescripcion());

        return ok;
    }
    public void limpiarFormulario() {
        campoDescripcion.setText("");
        campoId.setText("");

        marcar(campoDescripcion,   true);

    }

    private void marcar(JTextField f, boolean ok) {

        f.setBorder(ok ? BORDER_OK : BORDER_ERROR);
    }
}
