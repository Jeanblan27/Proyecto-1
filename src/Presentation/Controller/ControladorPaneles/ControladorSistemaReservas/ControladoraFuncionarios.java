package Presentation.Controller.ControladorPaneles.ControladorSistemaReservas;

import Presentation.View.PanelsDeReservas.SistemaDeReservas.PanelFuncionarios;
import Presentation.Model.Funcionario.ListaFuncionarios;
import Presentation.Model.Funcionario.Funcionario;

import javax.swing.*;
import java.util.List;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ControladoraFuncionarios implements PropertyChangeListener{

private ListaFuncionarios modelo;
private PanelFuncionarios vista;
    public ControladoraFuncionarios(ListaFuncionarios modelo, PanelFuncionarios vista) {
        this.vista = vista;
        this.modelo = modelo;

        iniciarListenersFuncionarios();
    }
    public void cargarFuncionarios() {
        vista.limpiarTabla();
        List<Funcionario> funcionario = modelo.getFuncionarios();

        for (Funcionario funcionarios : funcionario) {

            vista.agregarDatosATabla(
                    funcionarios.getId(),
                    funcionarios.getNombre(),
                    funcionarios.getTelefono(),
                    funcionarios.getRol()
            );
        }

    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("agregado")) {
            cargarFuncionarios();
        }
        if (evt.getPropertyName().equals("eliminado")) {
            cargarFuncionarios();
        }
    }
    private void iniciarListenersFuncionarios() {
        cargarFuncionarios();

        //Boton Guardar
         vista.getBotonGuardar().addActionListener(e -> {

            if (vista.validarFormulario() && !(modelo.validarFuncionario(vista.getCampoGuardarID().getText()))) {

                modelo.agregarFuncionario(
                    vista.getCampoGuardarID().getText(),
                    vista.getCampoGuardarNombre().getText(),
                    vista.getCampoGuardarTelefono().getText(),
                    vista.getRolSeleccionado()

                );
            }else{
                JOptionPane.showMessageDialog(null,"El funcionario no es valido");
            }
         });

         //Boton Borrar
         vista.getBotonBorrarFuncionario().addActionListener(e -> {

            if(modelo.validarFuncionario(vista.getCampoGuardarID().getText())){
                modelo.eliminarFuncionario(vista.getCampoGuardarID().getText());
            }else{
                JOptionPane.showMessageDialog(null,"El funcionario con ese ID no esta en el sistema");
            }
        });

         //Boton Limpiar
        vista.getBotonLimpiar().addActionListener(e -> {

            vista.limpiarFormulario();
        });

        //Boton Buscar
        vista.getBotonBuscar().addActionListener(e1 ->{

            Funcionario funcionario = modelo.buscarfuncionario(vista.getCampoBuscarID().getText());
            if(funcionario != null){
                vista.setCampoGuardarID(funcionario.getId());
                vista.setCampoGuardarNombre(funcionario.getNombre());
                vista.setCampoGuardarTelefono(funcionario.getTelefono());
                vista.setRolSeleccionado(funcionario.getRol());
            }else {
                JOptionPane.showMessageDialog(null,"El funcionario con ese ID no existe");
            }
        });
    }
}
