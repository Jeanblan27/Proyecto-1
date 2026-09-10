package Presentation.Model.Funcionario;

import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class ListaFuncionarios {
    private List<Funcionario> funcionarios;
    private PropertyChangeSupport soporte;


    public ListaFuncionarios() {
        funcionarios = new ArrayList<>();
        funcionarios.add(new Funcionario("123","german","123","88888888", Rol.ADMIN));
        funcionarios.add(new Funcionario("009","juan","54321","22222222",Rol.FUNCIONARIO));

        soporte = new PropertyChangeSupport(this);
    }
    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public boolean existeFuncionario(String contrasenna, String id){
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getClave().equalsIgnoreCase(contrasenna) && funcionario.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
    public boolean validarFuncionario(String id){
        for(Funcionario funcionario : funcionarios){
            if(funcionario.getId().equals(id)){
              return true;
            }
        }
        return false;
    }
    public Funcionario buscarfuncionario(String id){
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getId().equals(id)) {
                return funcionario;
            }
        }
        return null;
    }
    public boolean cambiarClaveFuncionario (String claveActual,String claveNueva) {
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getClave().equalsIgnoreCase(claveActual)) {
                funcionario.setClave(claveNueva);
                return true;
            }
        }
        return false;
    }
    public void agregarFuncionario(String id,String nombre,String telefono,Rol rol) {
        Funcionario funcionario = new Funcionario(id,nombre,id,telefono,rol);
        funcionarios.add(funcionario);
        soporte.firePropertyChange("agregado",null,funcionario);
    }
    public void eliminarFuncionario(String id){
        funcionarios.removeIf(
                funcionario -> funcionario.getId().equals(id)
        );
        soporte.firePropertyChange("eliminado",null,id);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        soporte.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        soporte.removePropertyChangeListener(listener);
    }
}
