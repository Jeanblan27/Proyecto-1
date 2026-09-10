package Presentation.View.PanelsDeReservas.SistemaReservasFuncionario;

import Model.Categoria.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelReservas extends JPanel {

    private JTextField campoFrase;
    private JTextField campoActividad;
    private JTextField campoFecha;
    private JTextField campoHoraInicio;
    private JTextField campoHoraFin;

    private JButton botonExtraerIA;
    private JButton botonReserva;
    private JButton botonCancelarReserva;
    private JButton botonLimpiar;
    private JButton botonImprimir;

    private JList<Categoria> listaCategorias;
    private DefaultListModel<Categoria> modeloCategorias;

    private JTable tablaReservas;
    private DefaultTableModel modeloTablaReservas;

    public PanelReservas() {

        setLayout(new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );

        construirPanel();
    }

    public void construirPanel() {

        // ==========================================
        // NUEVA RESERVA
        // ==========================================

        JPanel panelNuevaReserva = new JPanel(
                new BorderLayout(10, 10)
        );

        panelNuevaReserva.setBorder(
                BorderFactory.createTitledBorder(
                        "Nueva reserva"
                )
        );


        // ==========================================
        // FORMULARIO
        // ==========================================

        JPanel panelFormulario = new JPanel(
                new GridBagLayout()
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;


        campoFrase = new JTextField();
        campoActividad = new JTextField();
        campoFecha = new JTextField();
        campoHoraInicio = new JTextField();
        campoHoraFin = new JTextField();

        botonExtraerIA = new JButton("Extraer IA");


        // ==========================================
        // FRASE
        // ==========================================

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel("Frase"),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panelFormulario.add(
                campoFrase,
                gbc
        );

        gbc.gridx = 2;
        gbc.weightx = 0;

        panelFormulario.add(
                botonExtraerIA,
                gbc
        );


        // ==========================================
        // ACTIVIDAD
        // ==========================================

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;

        panelFormulario.add(
                new JLabel("Actividad"),
                gbc
        );

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;

        panelFormulario.add(
                campoActividad,
                gbc
        );

        gbc.gridwidth = 1;


        // ==========================================
        // FECHA
        // ==========================================

        gbc.gridx = 0;
        gbc.gridy = 2;

        panelFormulario.add(
                new JLabel("Fecha (dd/MM/yyyy)"),
                gbc
        );

        gbc.gridx = 1;
        gbc.gridwidth = 2;

        panelFormulario.add(
                campoFecha,
                gbc
        );

        gbc.gridwidth = 1;


        // ==========================================
        // HORA INICIO
        // ==========================================

        gbc.gridx = 0;
        gbc.gridy = 3;

        panelFormulario.add(
                new JLabel("Hora inicio (HH:mm)"),
                gbc
        );

        gbc.gridx = 1;
        gbc.gridwidth = 2;

        panelFormulario.add(
                campoHoraInicio,
                gbc
        );

        gbc.gridwidth = 1;


        // ==========================================
        // HORA FIN
        // ==========================================

        gbc.gridx = 0;
        gbc.gridy = 4;

        panelFormulario.add(
                new JLabel("Hora fin (HH:mm)"),
                gbc
        );

        gbc.gridx = 1;
        gbc.gridwidth = 2;

        panelFormulario.add(
                campoHoraFin,
                gbc
        );

        gbc.gridwidth = 1;


        // ==========================================
        // CATEGORÍAS
        // ==========================================

        modeloCategorias = new DefaultListModel<>();

        listaCategorias = new JList<>(
                modeloCategorias
        );

        listaCategorias.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );

        listaCategorias.setVisibleRowCount(5);

        JScrollPane scrollCategorias =
                new JScrollPane(listaCategorias);

        JPanel panelCategorias = new JPanel(
                new BorderLayout()
        );

        panelCategorias.setBorder(
                BorderFactory.createTitledBorder(
                        "Categorías requeridas (selección múltiple)"
                )
        );

        panelCategorias.add(
                scrollCategorias,
                BorderLayout.CENTER
        );


        // ==========================================
        // CENTRO
        // ==========================================

        JPanel centroNuevaReserva = new JPanel(
                new BorderLayout(10, 10)
        );

        centroNuevaReserva.add(
                panelFormulario,
                BorderLayout.CENTER
        );

        centroNuevaReserva.add(
                panelCategorias,
                BorderLayout.SOUTH
        );

        panelNuevaReserva.add(
                centroNuevaReserva,
                BorderLayout.CENTER
        );


        // ==========================================
        // BOTONES
        // ==========================================

        botonReserva = new JButton("Reservar");
        botonCancelarReserva = new JButton("Cancelar");
        botonLimpiar = new JButton("Limpiar");

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER)
        );

        panelBotones.add(botonReserva);
        panelBotones.add(botonCancelarReserva);
        panelBotones.add(botonLimpiar);

        panelNuevaReserva.add(
                panelBotones,
                BorderLayout.SOUTH
        );


        // ==========================================
        // MIS RESERVAS
        // ==========================================

        JPanel panelMisReservas = new JPanel(
                new BorderLayout(10, 10)
        );

        panelMisReservas.setBorder(
                BorderFactory.createTitledBorder(
                        "Mis reservas"
                )
        );

        String[] columnas = {
                "ID",
                "Actividad",
                "Fecha",
                "Horario",
                "Recursos",
                "Estado"
        };

        modeloTablaReservas = new DefaultTableModel(
                columnas,
                0
        );

        tablaReservas = new JTable(
                modeloTablaReservas
        );

        panelMisReservas.add(
                new JScrollPane(tablaReservas),
                BorderLayout.CENTER
        );


        // ==========================================
        // IMPRIMIR
        // ==========================================

        botonImprimir = new JButton("Imprimir");

        JPanel panelImprimir = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        panelImprimir.add(botonImprimir);

        panelMisReservas.add(
                panelImprimir,
                BorderLayout.SOUTH
        );


        // ==========================================
        // PANEL PRINCIPAL
        // ==========================================

        add(
                panelNuevaReserva,
                BorderLayout.NORTH
        );

        add(
                panelMisReservas,
                BorderLayout.CENTER
        );
    }


    // ==========================================
    // CATEGORÍAS
    // ==========================================

    public void cargarCategorias(List<Categoria> categorias) {

        modeloCategorias.clear();

        for (Categoria categoria : categorias) {
            modeloCategorias.addElement(categoria);
        }
    }

    public List<Categoria> getCategoriasSeleccionadas() {

        return listaCategorias.getSelectedValuesList();
    }


    // ==========================================
    // TABLA
    // ==========================================

    public void agregarReservaATabla(
            String id,
            String actividad,
            String fecha,
            String horario,
            String recursos,
            String estado) {

        modeloTablaReservas.addRow(
                new Object[]{
                        id,
                        actividad,
                        fecha,
                        horario,
                        recursos,
                        estado
                }
        );
    }

    public void limpiarTabla() {

        modeloTablaReservas.setRowCount(0);
    }


    // ==========================================
    // LIMPIAR FORMULARIO
    // ==========================================

    public void limpiarFormulario() {

        campoFrase.setText("");
        campoActividad.setText("");
        campoFecha.setText("");
        campoHoraInicio.setText("");
        campoHoraFin.setText("");

        listaCategorias.clearSelection();
    }


    // ==========================================
    // GETTERS
    // ==========================================

    public JTextField getCampoFrase() {
        return campoFrase;
    }

    public JTextField getCampoActividad() {
        return campoActividad;
    }

    public JTextField getCampoFecha() {
        return campoFecha;
    }

    public JTextField getCampoHoraInicio() {
        return campoHoraInicio;
    }

    public JTextField getCampoHoraFin() {
        return campoHoraFin;
    }

    public JButton getBotonExtraerIA() {
        return botonExtraerIA;
    }

    public JButton getBotonReserva() {
        return botonReserva;
    }

    public JButton getBotonCancelarReserva() {
        return botonCancelarReserva;
    }

    public JButton getBotonLimpiar() {
        return botonLimpiar;
    }

    public JButton getBotonImprimir() {
        return botonImprimir;
    }

    public JTable getTablaReservas() {
        return tablaReservas;
    }
}