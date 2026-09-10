package Presentation.Controller.ControladorPaneles.ControladorSistemaReservas;
import Presentation.Model.Categoria.Categoria;
import Presentation.Model.Categoria.ListaCategorias;
import Presentation.Model.Recurso.ListaRecursos;
import Presentation.Model.Recurso.Recurso;
import Presentation.Model.Reserva.Reserva;
import Presentation.View.PanelsDeReservas.SistemaDeReservas.PanelRecursos;

import java.util.List;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ControladorRecursos implements PropertyChangeListener{
    private PanelRecursos vista;
    private ListaRecursos modelo;
    private ListaCategorias modeloCategoria ;

    public ControladorRecursos(PanelRecursos vista, ListaRecursos modelo, ListaCategorias modeloCategoria) {
        this.vista = vista;
        this.modelo = modelo;
        this.modeloCategoria = modeloCategoria;
        cargarCategorias();
        cargarRecurso();
        iniciarListenersRecursos();

        modeloCategoria.addPropertyChangeListener(this);
    }
    private void iniciarListenersRecursos(){

        vista.getBotonGuardarRecurso().addActionListener(e -> {
            Categoria categoria = vista.getCategoriaSeleccionada();
            Recurso recurso = new Recurso(Integer.parseInt(vista.getCampoGuardarID().getText()),
                    categoria,vista.getCampoGuardarDescripcion().getText());
            modelo.agregarRecurso(recurso);
        });

    }

    private void cargarCategorias() {
        vista.cargarCategorias(
                modeloCategoria.getCategorias()
        );
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("agregado")) {
            cargarRecurso();
            cargarCategorias();
        }
        if (evt.getPropertyName().equals("eliminado")) {
            cargarRecurso();
            cargarCategorias();
        }
    }

    public void cargarRecurso() {
        vista.limpiarTabla();
        List<Recurso> recurso = modelo.getRecursos();

        for (Recurso recurso1 : recurso) {

            vista.agregarDatosATabla(
                    String.valueOf((recurso1.getId())),
                    recurso1.getCategoria(),
                    recurso1.getDescripcion()

            );
        }

    }

}
