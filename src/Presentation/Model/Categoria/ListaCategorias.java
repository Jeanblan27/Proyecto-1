package Presentation.Model.Categoria;

import java.util.List;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import Data.CategoriaXML;

public class ListaCategorias {
    private List<Categoria> categorias;
    private PropertyChangeSupport soporte;
    private int siguienteId;
    private CategoriaXML almacenamiento;

    public ListaCategorias() {
        almacenamiento = new CategoriaXML();

        categorias = almacenamiento.cargar();

        if (categorias.isEmpty()) {

            categorias.add(
                    new Categoria(1, "laptop")
            );

            categorias.add(
                    new Categoria(2, "PC")
            );

            almacenamiento.guardar(categorias);
        }
        soporte = new PropertyChangeSupport(this);
    }
    public List<Categoria> getCategorias() {
        return categorias;
    }
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Categoria buscarCategoria(String descripcion) {

        for (Categoria categoria : categorias) {
            if (categoria.getDescripcion().equals(descripcion)) {
                return categoria;
            }
        }
        return null;
    }
    public boolean validarCategoria(int id){
        for(Categoria categoria : categorias){
            if(categoria.getId()==(id)){
                return true;
            }
        }
        return false;
    }
    public boolean validarDescripcion(String descripcion){
        for(Categoria categoria : categorias){
            if(categoria.getDescripcion().equals(descripcion)){
                return true;
            }
        }
        return false;
    }
    public void agregarCategoria(String descripcion){

        for(Categoria categoria : categorias){
            if(categoria.getId()==siguienteId){
                ++siguienteId;
            }
        }

        Categoria categoria = new Categoria(siguienteId, descripcion);
        categorias.add(categoria);

        almacenamiento.guardar(categorias);
        soporte.firePropertyChange("agregado",null,categoria);
    }
    public void eliminarCategoria(String descripcion){
        categorias.removeIf(
                categoria -> categoria.getDescripcion().equals(descripcion)
        );
        almacenamiento.guardar(categorias);
        soporte.firePropertyChange("eliminado",null,descripcion);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        soporte.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        soporte.removePropertyChangeListener(listener);
    }
}

