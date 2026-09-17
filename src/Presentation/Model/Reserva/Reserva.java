package Presentation.Model.Reserva;

import Presentation.Model.Funcionario.Funcionario;
import Presentation.Model.Recurso.Recurso;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Reserva {

    private int id;
    private Funcionario funcionario;
    private String actividad;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private ArrayList<Recurso> recursos;
    private Estado estado;

    public Reserva(
            Funcionario funcionario,
            String actividad,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            ArrayList<Recurso> recursos) {

        this.funcionario = funcionario;
        this.actividad = actividad;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.recursos = recursos;
        this.estado = Estado.ACTIVA;
    }

    public int getId() {
        return id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public String getActividad() {
        return actividad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public ArrayList<Recurso> getRecursos() {
        return recursos;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public void setRecursos(ArrayList<Recurso> recursos) {
        this.recursos = recursos;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
