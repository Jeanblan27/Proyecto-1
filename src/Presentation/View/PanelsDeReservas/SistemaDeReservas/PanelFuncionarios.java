package Presentation.View.PanelsDeReservas.SistemaDeReservas;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import Model.Funcionario.Rol;

public class PanelFuncionarios extends JPanel {

    private static final Border BORDER_OK    =                // borde para campos válidos (gris)
            BorderFactory.createLineBorder(new Color(180, 180, 180));
    private static final Border BORDER_ERROR =                // borde para campos inválidos (rojo, 2px)
            BorderFactory.createLineBorder(Color.RED, 2);

    private JTextField campoBuscarID;
    private JTextField campoGuardarID;
    private JTextField campoGuardarNombre;
    private JTextField campoGuardarTelefono;

    private JComboBox<Rol> comboRol;

    private JButton botonBuscar;
    private JButton botonGuardarFuncionario;
    private JButton botonBorrarFuncionario;
    private JButton botonLimpiar;

    private DefaultTableModel datosDeLaTabla;

    public PanelFuncionarios() {
        setLayout(new GridLayout(3,1,8,8));
        construirPanel();
    }

    private void construirPanel() {

        JPanel buscarFuncionario = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        JPanel datosDeFuncionarios = new JPanel(new BorderLayout());

        //Panel de buscar
        buscarFuncionario.add(new JLabel("Buscar por ID"));
        campoBuscarID = new JTextField(10);
        buscarFuncionario.add(campoBuscarID);
        botonBuscar = new JButton("Buscar");
        buscarFuncionario.add(botonBuscar);


        JPanel datoDeBusqueda = new JPanel(new GridLayout(4, 2, 4, 4));
        datoDeBusqueda.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        JLabel guardarID = new JLabel("ID");
        JLabel guardarNombre = new JLabel("Nombre");
        JLabel vacio = new JLabel("");
        JLabel vacio2 = new JLabel("");
        JLabel telefono = new JLabel("Telefono");
        campoGuardarTelefono = new JTextField(10);
        campoGuardarID = new JTextField(10);
        campoGuardarNombre = new JTextField(10);

        comboRol = new JComboBox<>(Rol.values());

        campoGuardarNombre.addFocusListener(
                new Validador(() -> {
                    marcar(campoGuardarNombre, validarNombre());
                })
        );
        campoGuardarID.addFocusListener(
                new Validador(() -> {
                    marcar(campoGuardarID, validarNombre());
                })
        );


        JPanel panelBotones = new JPanel(new GridLayout(1,1));
        JPanel panelBotones2 = new JPanel(new GridLayout(1,2));
        botonGuardarFuncionario = new JButton("Guardar");
        botonBorrarFuncionario = new JButton("Borrar");
        botonLimpiar = new JButton("Limpiar");


        datoDeBusqueda.add(guardarID);
        datoDeBusqueda.add(campoGuardarID);
        datoDeBusqueda.add(comboRol);
        datoDeBusqueda.add(guardarNombre);
        datoDeBusqueda.add(campoGuardarNombre);
        datoDeBusqueda.add(vacio);
        datoDeBusqueda.add(telefono);
        datoDeBusqueda.add(campoGuardarTelefono);
        datoDeBusqueda.add(vacio2);


        panelBotones.add(botonGuardarFuncionario);
        panelBotones2.add(botonBorrarFuncionario);
        panelBotones2.add(botonLimpiar);
        datoDeBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        datoDeBusqueda.add(panelBotones);
        datoDeBusqueda.add(panelBotones2);


        //Panel de los datos
        datosDeFuncionarios.add(new JLabel("Datos de Funcionarios: "), BorderLayout.NORTH);
        String[] columnas = {"ID", "Nombre", "Telefono","Rol" };
        datosDeLaTabla = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(datosDeLaTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        datosDeFuncionarios.add(scroll, BorderLayout.CENTER);
        datosDeFuncionarios.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setBackground(Color.LIGHT_GRAY);
        add(buscarFuncionario);
        add(datoDeBusqueda);
        add(datosDeFuncionarios);

    }
    public void agregarDatosATabla(String id, String nombre,String telefono,Rol rol) {

        datosDeLaTabla.addRow(new Object[]{
                id,
                nombre,
                telefono,
                rol,
        });
    }
    public void limpiarTabla() {
        datosDeLaTabla.setRowCount(0);
    }

    public JButton getBotonBuscar() {
        return botonBuscar;
    }
    public JTextField getCampoBuscarID() {
        return campoBuscarID;
    }
    public JButton getBotonGuardar() {
        return botonGuardarFuncionario;
    }
    public JButton getBotonBorrarFuncionario() {
        return botonBorrarFuncionario;
    }
    public JButton getBotonLimpiar() {
        return botonLimpiar;
    }
    public JTextField getCampoGuardarID() {
        return campoGuardarID;
    }
    public void setCampoGuardarID(String id) { campoGuardarID.setText(id); }
    public JTextField getCampoGuardarNombre() {
        return campoGuardarNombre;
    }
    public void setCampoGuardarNombre(String nombre) { campoGuardarNombre.setText(nombre); }
    public JTextField getCampoGuardarTelefono() { return campoGuardarTelefono; }
    public void setCampoGuardarTelefono(String telefono) { campoGuardarTelefono.setText(telefono); }
    public Rol getRolSeleccionado() { return (Rol) comboRol.getSelectedItem(); }
    public void setRolSeleccionado(Rol rolSeleccionado) { comboRol.setSelectedItem(rolSeleccionado); }

    // --------------------- validaciones ---------------------
    private boolean validarId() {
        return !campoGuardarID.getText().trim().isEmpty();
    }

    private boolean validarNombre() {
        String nombre = campoGuardarNombre.getText().trim();
        return !nombre.isEmpty() && nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }
    private boolean validarTelefono() {
        String t = campoGuardarTelefono.getText().trim();
        return t.matches("[0-9\\- ]{6,15}");
    }
    public boolean validarFormulario() {
        boolean ok = validarId()
                && validarNombre()
                && validarTelefono();

        marcar(campoGuardarID,       validarId());
        marcar(campoGuardarNombre,   validarNombre());
        marcar(campoGuardarTelefono,   validarTelefono());
        return ok;
    }
    public void limpiarFormulario() {
        campoGuardarID.setText("");
        campoGuardarNombre.setText("");
        campoGuardarTelefono.setText("");
        marcar(campoGuardarID,   true);
        marcar(campoGuardarNombre,   true);
        marcar(campoGuardarTelefono,   true);
    }

    private void marcar(JTextField f, boolean ok) {
        f.setBorder(ok ? BORDER_OK : BORDER_ERROR);
    }

    /** Adapter de FocusListener que ejecuta una acción al perder el foco. */
    private static class Validador extends FocusAdapter {
        private final Runnable check;
        Validador(Runnable check) { this.check = check; }
        @Override
        public void focusLost(FocusEvent e) { check.run(); }
    }
}