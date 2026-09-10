package Presentation.Controller.ControladorPaneles.ControladorSistemaReservasFuncionario;

import Model.Categoria.Categoria;
import Model.Funcionario.Funcionario;
import Model.Recurso.ListaRecursos;
import Model.Categoria.ListaCategorias;
import Model.Recurso.Recurso;
import Model.Reserva.Reserva;
import View.PanelsDeReservas.SistemaReservasFuncionario.PanelReservas;
import Model.Reserva.ListaReservas;
import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ControladorReservas implements PropertyChangeListener {

    private PanelReservas vista;
    private ListaReservas modeloReservas;
    private ListaRecursos modeloRecursos;
    private Funcionario funcionario;
    private ListaCategorias modeloCategorias;

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

        // Cargar las reservas existentes
        // cuando se abre el panel

        cargarCategorias();
        cargarReservas();
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