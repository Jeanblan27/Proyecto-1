package Presentation.Controller.ControladorPaneles.ControladorSistemaReservasFuncionario;


import Controller.ControladorPaneles.ControladorActividades;
import Controller.ControladorPaneles.ControladorCalendarizacion;
import Controller.ControladorPaneles.ControladorEstadisticas;
import Model.Funcionario.Funcionario;
import Model.Categoria.ListaCategorias;
import Model.Recurso.ListaRecursos;
import Model.Reserva.ListaReservas;
import View.PanelsDeReservas.SistemaReservasFuncionario.SistemaReservaFuncionario;

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
        controladorCalendarizacion = new ControladorCalendarizacion(vista.getPanelCalendarizacion());
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
