package Presentation.Model.Reserva;

import Presentation.Model.Funcionario.Funcionario;
import Presentation.Model.Recurso.Recurso;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class ListaReservas {

    private ArrayList<Reserva> reservas;
    private int siguienteId;
    private PropertyChangeSupport soporte;

    public ListaReservas() {
        reservas = new ArrayList<>();
        siguienteId = 1;
        soporte = new PropertyChangeSupport(this);
    }
    // =========================
    // AGREGAR RESERVA
    public void agregarReserva(Reserva reserva) {

        reservas.add(reserva);
    }

    public ArrayList<Reserva> getReservas() {

        return reservas;
    }
    //
    public int generarId() {
        return siguienteId++;
    }

    public Reserva buscarReserva(int id) {

        for (Reserva reserva : reservas) {

            if (reserva.getId() == id) {
                return reserva;
            }
        }

        return null;
    }

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

            return true;
        }

        return false;
    }

    public boolean cancelarReserva(int id) {

        Reserva reserva = buscarReserva(id);

        if (reserva != null) {

            reserva.setEstado(Estado.CANCELADA);
            return true;
        }

        return false;
    }

    public boolean recursoDisponible(
            Recurso recurso,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin) {

        for (Reserva reserva : reservas) {

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
                            && horaFin.isAfter(reserva.getHoraInicio());

            if (hayCruce) {
                return false;
            }
        }

        return true;
    }
    public boolean recursoEstaReservado(
            Recurso recurso,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin) {

        for (Reserva reserva : reservas) {

            // Si es otra fecha, no hay conflicto
            if (!reserva.getFecha().equals(fecha)) {
                continue;
            }

            // Revisar los recursos de la reserva
            for (Recurso r : reserva.getRecursos()) {

                // ¿Es el mismo recurso?
                if (r.getId() == recurso.getId()) {

                    // ¿Se cruzan los horarios?
                    if (horaInicio.isBefore(reserva.getHoraFin())
                            && horaFin.isAfter(reserva.getHoraInicio())) {

                        return true;
                    }
                }
            }
        }

        return false;
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        soporte.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        soporte.removePropertyChangeListener(listener);
    }
}