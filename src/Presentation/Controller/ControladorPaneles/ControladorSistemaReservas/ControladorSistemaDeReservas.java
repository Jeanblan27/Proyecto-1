package Presentation.Controller.ControladorPaneles.ControladorSistemaReservas;

import Presentation.Controller.ControladorPaneles.*;
import Presentation.Model.Funcionario.ListaFuncionarios;
import Presentation.Model.Categoria.ListaCategorias;
import Presentation.Model.Recurso.ListaRecursos;
import Presentation.View.PanelsDeReservas.SistemaDeReservas.SistemaDeReservas;

public class ControladorSistemaDeReservas {

    private SistemaDeReservas vista;
    private ListaFuncionarios modelo;
    private ListaCategorias modeloCategoria;
    private ListaRecursos modeloRecursos;

    private ControladoraFuncionarios controladorFuncionarios;
    private ControladoraCategorias controladoraCategorias;
    private ControladorRecursos controladorRecursos;
    private ControladorCalendarizacion controladorCalendarizacion;
    private ControladorEstadisticas controladorEstadisticas;
    private ControladorActividades controladorActividades;

    public ControladorSistemaDeReservas(
            SistemaDeReservas vista,
            ListaFuncionarios modelo,
            ListaCategorias modeloCategoria,
            ListaRecursos modeloRecursos) {

        this.vista = vista;
        this.modelo = modelo;
        this.modeloCategoria = modeloCategoria;
        this.modeloRecursos = modeloRecursos;

        iniciarListenerPaneles();
        iniciarControladorFuncionarios();
        iniciarControladorCategorias();
        iniciarControladorRecursos();
    }

    private void iniciarControladorFuncionarios() {

        controladorFuncionarios = new ControladoraFuncionarios(modelo,
                vista.getPanelFuncionarios()
        );

        modelo.addPropertyChangeListener(controladorFuncionarios);
    }
    private void iniciarControladorCategorias() {

        controladoraCategorias = new ControladoraCategorias(vista.getPanelCategoria(), modeloCategoria);
        modeloCategoria.addPropertyChangeListener(controladoraCategorias);
    }
    private void iniciarControladorRecursos() {

        controladorRecursos = new ControladorRecursos(vista.getPanelRecursos(),modeloRecursos,modeloCategoria);
        modeloRecursos.addPropertyChangeListener(controladorRecursos);
    }
    private void iniciarControladorCalendarizacion() {

    }

    private void iniciarListenerPaneles() {

        vista.getBotonFuncionarios().addActionListener(e -> {

            vista.getCardLayout().show(
                    vista.getPaneles(),
                    "FUNCIONARIOS"
            );
        });

        vista.getBotonCategorias().addActionListener(e -> {

            vista.getCardLayout().show(
                    vista.getPaneles(),
                    "CATEGORIAS"
            );
        });

        vista.getBotonRecursos().addActionListener(e -> {

            vista.getCardLayout().show(
                    vista.getPaneles(),
                    "RECURSOS"
            );
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
