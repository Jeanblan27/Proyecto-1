package Presentation.Controller.ControladorPaneles.ControladorSistemaReservasFuncionario;

import Presentation.Model.Categoria.Categoria;
import Presentation.Model.Funcionario.Funcionario;
import Presentation.Model.Recurso.ListaRecursos;
import Presentation.Model.Categoria.ListaCategorias;
import Presentation.Model.Recurso.Recurso;
import Presentation.Model.Reserva.Reserva;
import Presentation.View.PanelsDeReservas.SistemaReservasFuncionario.PanelReservas;
import Presentation.Model.Reserva.ListaReservas;
import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import Presentation.Model.Reserva.Estado;
import Logic.DatosReservaIA;
import Logic.ServicioIA;

public class ControladorReservas implements PropertyChangeListener {

    private PanelReservas vista;
    private ListaReservas modeloReservas;
    private ListaRecursos modeloRecursos;
    private Funcionario funcionario;
    private ListaCategorias modeloCategorias;
    private ServicioIA servicioIA;

    private DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DateTimeFormatter formatoHora =
            DateTimeFormatter.ofPattern("HH:mm");


    public ControladorReservas(
            PanelReservas vista,
            ListaReservas modeloReservas,
            ListaRecursos modeloRecursos,
            ListaCategorias modeloCategorias,
            Funcionario funcionario) {

        this.vista = vista;
        this.modeloReservas = modeloReservas;
        this.modeloRecursos = modeloRecursos;
        this.modeloCategorias = modeloCategorias;
        this.funcionario = funcionario;

        iniciar();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("agregado")) {
            cargarCategorias();
        }
    }

    private void iniciar() {

        // Botón Reservar
        vista.getBotonReserva().addActionListener(e ->
                realizarReserva()
        );

        // Botón Limpiar
        vista.getBotonLimpiar().addActionListener(e ->
                vista.limpiarFormulario()
        );
        vista.getBotonCancelarReserva().addActionListener(e ->
                cancelarReserva()
        );
        vista.getBotonExtraerIA().addActionListener(e ->
                extraerConIA()
        );
        // Cargar las reservas existentes
        // cuando se abre el panel

        cargarCategorias();
        cargarReservas();
    }

    private void extraerConIA() {

        String frase = vista.getCampoFrase()
                .getText()
                .trim();

        if (frase.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Escriba una frase para extraer la información."
            );
            return;
        }

        try {

            if (servicioIA == null) {
                servicioIA = new ServicioIA();
            }

            DatosReservaIA datos =
                    servicioIA.extraerReserva(
                            frase,
                            modeloCategorias.getCategorias()
                    );

            vista.getCampoActividad()
                    .setText(datos.getActividad());

            vista.getCampoFecha()
                    .setText(datos.getFecha());

            vista.getCampoHoraInicio()
                    .setText(datos.getHoraInicio());

            vista.getCampoHoraFin()
                    .setText(datos.getHoraFin());

            ArrayList<Categoria> categoriasEncontradas =
                    new ArrayList<>();

            for (String nombreCategoria : datos.getCategorias()) {

                for (Categoria categoria :
                        modeloCategorias.getCategorias()) {

                    if (categoria.getDescripcion()
                            .equalsIgnoreCase(
                                    nombreCategoria.trim())) {

                        categoriasEncontradas.add(categoria);
                        break;
                    }
                }
            }

            vista.seleccionarCategorias(
                    categoriasEncontradas
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo utilizar la IA:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    private void realizarReserva() {

        String actividad =
                vista.getCampoActividad().getText().trim();

        String textoFecha =
                vista.getCampoFecha().getText().trim();

        String textoHoraInicio =
                vista.getCampoHoraInicio().getText().trim();

        String textoHoraFin =
                vista.getCampoHoraFin().getText().trim();

        List<Categoria> categorias =
                vista.getCategoriasSeleccionadas();


        // =========================
        // VALIDACIONES
        // =========================

        if (actividad.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Debe ingresar una actividad."
            );
            return;
        }

        if (textoFecha.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Debe ingresar una fecha."
            );
            return;
        }

        if (textoHoraInicio.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Debe ingresar la hora de inicio."
            );
            return;
        }

        if (textoHoraFin.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Debe ingresar la hora de fin."
            );
            return;
        }

        if (categorias.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Debe seleccionar al menos una categoría."
            );
            return;
        }


        // =========================
        // CONVERTIR FECHA
        // =========================

        LocalDate fecha;

        try {

            fecha = LocalDate.parse(
                    textoFecha,
                    formatoFecha
            );

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    vista,
                    "La fecha debe tener el formato dd/MM/yyyy."
            );

            return;
        }


        // =========================
        // CONVERTIR HORAS
        // =========================

        LocalTime horaInicio;
        LocalTime horaFin;

        try {

            horaInicio = LocalTime.parse(
                    textoHoraInicio,
                    formatoHora
            );

            horaFin = LocalTime.parse(
                    textoHoraFin,
                    formatoHora
            );

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Las horas deben tener el formato HH:mm."
            );

            return;
        }


        // =========================
        // VALIDAR HORARIO
        // =========================

        if (!horaInicio.isBefore(horaFin)) {

            JOptionPane.showMessageDialog(
                    vista,
                    "La hora de inicio debe ser menor que la hora de fin."
            );

            return;
        }


        // =========================
        // BUSCAR RECURSOS
        // =========================

        ArrayList<Recurso> recursosSeleccionados =
                new ArrayList<>();


        for (Categoria categoria : categorias) {

            ArrayList<Recurso> disponibles =
                    modeloRecursos.obtenerRecursosDisponibles(
                            categoria,
                            fecha,
                            horaInicio,
                            horaFin,
                            modeloReservas
                    );


            // Si no existe ningún recurso disponible
            if (disponibles.isEmpty()) {

                JOptionPane.showMessageDialog(
                        vista,
                        "No hay recursos disponibles para la categoría: "
                                + categoria.getDescripcion()
                );

                return;
            }


            /*
             * Actualmente se selecciona automáticamente
             * el primer recurso disponible de la categoría.
             */
            recursosSeleccionados.add(
                    disponibles.get(0)
            );
        }

// =========================
// CREAR RESERVA
// =========================

        Reserva reserva = new Reserva(
                funcionario,
                actividad,
                fecha,
                horaInicio,
                horaFin,
                recursosSeleccionados
        );


// =========================
// GENERAR ID
// =========================

        int id = modeloReservas.generarId();

        reserva.setId(id);


// =========================
// AGREGAR AL MODELO
// =========================

        modeloReservas.agregarReserva(reserva);


        // =========================
        // ACTUALIZAR TABLA
        // =========================

        cargarReservas();


        // =========================
        // LIMPIAR FORMULARIO
        // =========================

        vista.limpiarFormulario();


        JOptionPane.showMessageDialog(
                vista,
                "Reserva realizada correctamente."
        );
    }
    private void cancelarReserva() {

        int fila = vista.getTablaReservas().getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Debe seleccionar una reserva para cancelar."
            );
            return;
        }

        String textoId =
                vista.getTablaReservas().getValueAt(fila, 0).toString();

        int id;

        try {
            id = Integer.parseInt(textoId);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo obtener el ID de la reserva."
            );
            return;
        }

        Reserva reserva = modeloReservas.buscarReserva(id);

        if (reserva == null) {
            JOptionPane.showMessageDialog(
                    vista,
                    "La reserva no existe."
            );
            return;
        }

        if (reserva.getEstado() == Presentation.Model.Reserva.Estado.CANCELADA) {
            JOptionPane.showMessageDialog(
                    vista,
                    "La reserva ya está cancelada."
            );
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de que desea cancelar esta reserva?",
                "Cancelar reserva",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean cancelada = modeloReservas.cancelarReserva(id);

        if (cancelada) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Reserva cancelada correctamente."
            );

            cargarReservas();
        } else {
            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo cancelar la reserva."
            );
        }
    }
    private void cargarCategorias() {

        vista.cargarCategorias(
                modeloCategorias.getCategorias()
        );
    }
    // =====================================================
    // CARGAR RESERVAS EN LA TABLA
    // =====================================================

    private void cargarReservas() {

        // Eliminar las filas actuales
        vista.limpiarTabla();


        // Recorrer todas las reservas
        for (Reserva reserva : modeloReservas.getReservas()) {


            /*
             * Como este controlador corresponde al sistema
             * del funcionario, solamente mostramos sus reservas.
             */
            if (reserva.getFuncionario().getId()
                    != funcionario.getId()) {

                continue;
            }


            // =========================
            // RECURSOS
            // =========================

            StringBuilder recursos =
                    new StringBuilder();

            for (Recurso recurso : reserva.getRecursos()) {

                if (recursos.length() > 0) {
                    recursos.append(", ");
                }

                recursos.append(
                        recurso.getDescripcion()
                );
            }


            // =========================
            // HORARIO
            // =========================

            String horario =
                    reserva.getHoraInicio()
                            + " - "
                            + reserva.getHoraFin();


            // =========================
            // AGREGAR A LA TABLA
            // =========================

            vista.agregarReservaATabla(

                    String.valueOf(
                            reserva.getId()
                    ),

                    reserva.getActividad(),

                    reserva.getFecha().format(
                            formatoFecha
                    ),

                    horario,

                    recursos.toString(),

                    "ACTIVA"
            );
        }
    }
}