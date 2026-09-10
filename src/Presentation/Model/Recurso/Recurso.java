package Presentation.Model.Recurso;

import Presentation.Model.Categoria.Categoria;

public class Recurso {

    private int id;
    private Categoria categoria;
    private String descripcion;

    public Recurso(int id, Categoria categoria, String descripcion) {
        this.id = id;
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    // GET
    public int getId() {
        return id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // SET
    public void setId(int id) {
        this.id = id;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}