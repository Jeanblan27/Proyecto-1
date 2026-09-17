package Presentation.Model.Reserva;

import Presentation.Model.Funcionario.Funcionario;
import Presentation.Model.Funcionario.ListaFuncionarios;
import Presentation.Model.Recurso.Recurso;
import Presentation.Model.Recurso.ListaRecursos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import Data.ReservaXML;

public class ListaReservas {

    private ArrayList<Reserva> reservas;
    private int siguienteId;
    private ReservaXML almacenamiento;
    private PropertyChangeSupport soporte;

    // =========================
    // CONSTRUCTOR
    // =========================

    public ListaReservas(
            ListaFuncionarios listaFuncionarios,
            ListaRecursos listaRecursos) {

        soporte = new PropertyChangeSupport(this);

        almacenamiento = new ReservaXML();

        reservas = new ArrayList<>(almacenamiento.cargar(listaFuncionarios, listaRecursos));

        // El siguiente ID empieza en 1
        siguienteId = 1;

        // Buscar el siguiente ID disponible
        for (Reserva reserva : reservas) {

            if (reserva.getId() >= siguienteId) {

                siguienteId =
                        reserva.getId() + 1;
            }
        }
    }

    // =========================
    // AGREGAR RESERVA
    // =========================

    public void agregarReserva(Reserva reserva) {

        reservas.add(reserva);

        // Guardar en XML
        almacenamiento.guardar(reservas);

        soporte.firePropertyChange(
                "agregado",
                null,
                reserva
        );
    }

    // =========================
    // OBTENER RESERVAS
    // =========================

    public ArrayList<Reserva> getReservas() {

        return reservas;
    }

    // =========================
    // GENERAR ID
    // =========================

    public int generarId() {

        return siguienteId++;
    }

    // =========================
    // BUSCAR RESERVA
    // =========================

    public Reserva buscarReserva(int id) {

        for (Reserva reserva : reservas) {

            if (reserva.getId() == id) {

                return reserva;
            }
        }

        return null;
    }

    // =========================
    // MODIFICAR RESERVA
    // =========================

    public boolean modificarReserva(
            int id,
            Funcionario funcionario,
            String actividad,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            ArrayList<Recurso> recursos) {

        Reserva reserva = buscarReserva(id);

        if (reserva != null) {

            reserva.setFuncionario(funcionario);
            reserva.setActividad(actividad);
            reserva.setFecha(fecha);
            reserva.setHoraInicio(horaInicio);
            reserva.setHoraFin(horaFin);
            reserva.setRecursos(recursos);

            // Guardar cambios en XML
            almacenamiento.guardar(reservas);

            soporte.firePropertyChange(
                    "modificado",
                    null,
                    reserva
            );

            return true;
        }

        return false;
    }

    // =========================
    // CANCELAR RESERVA
    // =========================

    public boolean cancelarReserva(int id) {

        Reserva reserva = buscarReserva(id);

        if (reserva != null) {

            reserva.setEstado(Estado.CANCELADA);

            // Guardar cancelación en XML
            almacenamiento.guardar(reservas);

            soporte.firePropertyChange(
                    "cancelado",
                    null,
                    reserva
            );

            return true;
        }

        return false;
    }

    // =========================
    // VERIFICAR DISPONIBILIDAD
    // =========================

    public boolean recursoDisponible(
            Recurso recurso,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin) {

        for (Reserva reserva : reservas) {

            // Las canceladas no bloquean recursos
            if (reserva.getEstado() == Estado.CANCELADA) {
                continue;
            }

            if (!reserva.getFecha().equals(fecha)) {
                continue;
            }

            if (!reserva.getRecursos().contains(recurso)) {
                continue;
            }

            boolean hayCruce =
                    horaInicio.isBefore(reserva.getHoraFin())
                            && horaFin.isAfter(
                            reserva.getHoraInicio()
                    );

            if (hayCruce) {

                return false;
            }
        }

        return true;
    }

    // =========================
    // RECURSO ESTA RESERVADO
    // =========================

    public boolean recursoEstaReservado(
            Recurso recurso,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin) {

        for (Reserva reserva : reservas) {

            // Las canceladas no cuentan
            if (reserva.getEstado() == Estado.CANCELADA) {
                continue;
            }

            // Si es otra fecha, no hay conflicto
            if (!reserva.getFecha().equals(fecha)) {
                continue;
            }

            // Revisar los recursos de la reserva
            for (Recurso r : reserva.getRecursos()) {

                // ¿Es el mismo recurso?
                if (r.getId() == recurso.getId()) {

                    // ¿Se cruzan los horarios?
                    if (horaInicio.isBefore(
                            reserva.getHoraFin())
                            && horaFin.isAfter(
                            reserva.getHoraInicio())) {

                        return true;
                    }
                }
            }
        }

        return false;
    }

    // =========================
    // BUSCAR RESERVAS EN HORA
    // =========================

    public ArrayList<Reserva> buscarReservasEnHora(
            LocalDate fecha,
            LocalTime hora) {

        ArrayList<Reserva> reservasEncontradas =
                new ArrayList<>();

        for (Reserva reserva : reservas) {

            // Ignorar reservas canceladas
            if (reserva.getEstado() == Estado.CANCELADA) {
                continue;
            }

            // Comprobar la fecha
            if (!reserva.getFecha().equals(fecha)) {
                continue;
            }

            // Comprobar si la hora está dentro
            // de la reserva
            if (!hora.isBefore(reserva.getHoraInicio())
                    && hora.isBefore(reserva.getHoraFin())) {

                reservasEncontradas.add(reserva);
            }
        }

        return reservasEncontradas;
    }

    // =========================
    // BUSCAR RESERVA DE RECURSO
    // EN UNA HORA
    // =========================

    public Reserva buscarReservaEnHora(
            Recurso recurso,
            LocalDate fecha,
            LocalTime hora) {

        for (Reserva reserva : reservas) {

            // Ignorar reservas canceladas
            if (reserva.getEstado() == Estado.CANCELADA) {
                continue;
            }

            // Comprobar la fecha
            if (!reserva.getFecha().equals(fecha)) {
                continue;
            }

            // Comprobar si el recurso pertenece
            // a la reserva
            if (!reserva.getRecursos().contains(recurso)) {
                continue;
            }

            // Comprobar si la hora está dentro
            // de la reserva
            if (!hora.isBefore(reserva.getHoraInicio())
                    && hora.isBefore(reserva.getHoraFin())) {

                return reserva;
            }
        }

        return null;
    }

    // =========================
    // PROPERTY CHANGE
    // =========================

    public void addPropertyChangeListener(
            PropertyChangeListener listener) {

        soporte.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(
            PropertyChangeListener listener) {

        soporte.removePropertyChangeListener(listener);
    }
}