package Presentation.Controller.ControladorPaneles.ControladorSistemaReservas;
import Presentation.Model.Categoria.Categoria;
import Presentation.View.PanelsDeReservas.SistemaDeReservas.PanelCategorias;
import Presentation.Model.Categoria.ListaCategorias;


import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;


public class ControladoraCategorias implements PropertyChangeListener{
    private PanelCategorias vista;
    private ListaCategorias modelo;

    public ControladoraCategorias(PanelCategorias vista, ListaCategorias modelo) {
        this.vista = vista;
        this.modelo = modelo;
        iniciarListenersCategorias();
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("agregado")) {
            cargarCategorias();
        }
        if (evt.getPropertyName().equals("eliminado")) {
            cargarCategorias();
        }
    }
    public void iniciarListenersCategorias(){
        cargarCategorias();

        //Boton Buscar
        vista.getBtnBuscarCategoria().addActionListener(e -> {

            Categoria categoria = modelo.buscarCategoria(vista.getCampoBusquedaDescripcion().getText());
            if(categoria != null){
                vista.setCampoDescripcion(categoria.getDescripcion());
                vista.setCampoId(String.valueOf(categoria.getId()));

            }else {
                JOptionPane.showMessageDialog(null,"No esta esa Categoria con esa Descripcion");
            }
        });

        //Boton Guardar
        vista.getBtnGuardarCategoria().addActionListener(e -> {

            if (vista.validarFormulario() && !(modelo.validarDescripcion(vista.getCampoDescripcion().getText()))) {

                modelo.agregarCategoria(
                        vista.getCampoDescripcion().getText()

                );
                String auto = "EL ID ES AUTOGENERADO";
                vista.setCampoId(String.valueOf(auto));
            }else{
                JOptionPane.showMessageDialog(null,"La categoria no es valido");
            }
        });

        //Boton Eliminar
        vista.getBtnEliminarCategoria().addActionListener(e -> {
            if(modelo.validarDescripcion(vista.getCampoDescripcion().getText())){
                modelo.eliminarCategoria(vista.getCampoDescripcion().getText());
            }else{
                JOptionPane.showMessageDialog(null,"La categoria con esa descripcion no esta en el sistema");
            }
        });

        //Boton Limpiar
        vista.getBtnLimpiar().addActionListener(e -> {
            vista.limpiarFormulario();
        });
    }

    public void cargarCategorias() {
        vista.limpiarTabla();
        List<Categoria> categorias = modelo.getCategorias();

        for (Categoria categoria : categorias) {

            vista.agregarDatosATabla(
                    categoria.getId(),
                    categoria.getDescripcion()

            );
        }

    }
}
