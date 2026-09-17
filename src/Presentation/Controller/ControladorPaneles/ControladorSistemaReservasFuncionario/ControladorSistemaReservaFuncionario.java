package Presentation.Controller.ControladorPaneles.ControladorSistemaReservasFuncionario;

import Presentation.Controller.ControladorPaneles.ControladorActividades;
import Presentation.Controller.ControladorPaneles.ControladorCalendarizacion;
import Presentation.Controller.ControladorPaneles.ControladorEstadisticas;
import Presentation.Model.Funcionario.Funcionario;
import Presentation.Model.Categoria.ListaCategorias;
import Presentation.Model.Recurso.ListaRecursos;
import Presentation.Model.Reserva.ListaReservas;
import Presentation.View.PanelsDeReservas.SistemaReservasFuncionario.SistemaReservaFuncionario;

public class ControladorSistemaReservaFuncionario {

    private SistemaReservaFuncionario vista;
    private ListaReservas modelo;
    private ListaCategorias modeloCategoria;
    private ListaRecursos modeloRecursos;
    private Funcionario funcionario;


    private ControladorCalendarizacion controladorCalendarizacion;
    private ControladorEstadisticas controladorEstadisticas;
    private ControladorActividades controladorActividades;
    private ControladorReservas controladorReservas;

    public ControladorSistemaReservaFuncionario(
            SistemaReservaFuncionario vista,
            ListaReservas modelo,
            ListaCategorias modeloCategoria,
            ListaRecursos modeloRecursos,
            Funcionario funcionario) {

        this.vista = vista;
        this.modelo = modelo;
        this.modeloCategoria = modeloCategoria;
        this.modeloRecursos = modeloRecursos;
        this.funcionario = funcionario;

        iniciarListenerPaneles();
        iniciarControladorReservas();
        iniciarControladorCalendarizacion();
    }


    private void iniciarControladorCalendarizacion() {
        controladorCalendarizacion = new ControladorCalendarizacion(vista.getPanelCalendarizacion(), modeloCategoria,modeloRecursos, modelo);
    }
    private void iniciarControladorEstadisticas() {

    }
    private void iniciarControladorActividades() {

    }
    private void iniciarControladorReservas() {
        controladorReservas = new ControladorReservas(vista.getPanelReservas(),modelo,modeloRecursos,modeloCategoria,funcionario );
    }
    private void iniciarListenerPaneles() {

        vista.getBotonReserva().addActionListener(e -> {

            vista.getCardLayout().show(vista.getPaneles(), "RESERVAS");
        });
        vista.getBotonCalendarizacion().addActionListener(e -> {

            vista.getCardLayout().show(
                    vista.getPaneles(),
                    "CALENDARIZACION"
            );
        });
        vista.getBotonActividades().addActionListener(e -> {
            vista.getCardLayout().show(
                    vista.getPaneles(),
                    "ACTIVIDADES"
            );
        });

        vista.getBotonEstadisticas().addActionListener(e -> {
            vista.getCardLayout().show(
                    vista.getPaneles(),
                    "ESTADISTICAS"
            );
        });
    }
}
