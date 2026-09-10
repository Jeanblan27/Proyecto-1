package Presentation.Model.Recurso;

import Presentation.Model.Categoria.Categoria;
import Presentation.Model.Reserva.ListaReservas;
import Data.RecursoXML;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class ListaRecursos {

    private ArrayList<Recurso> recursos;
    private PropertyChangeSupport soporte;
    private RecursoXML almacenamiento;

    public ListaRecursos(List<Categoria> categorias) {

        recursos = new ArrayList<>();
        almacenamiento = new RecursoXML();
        recursos.addAll(almacenamiento.cargar(categorias));

        soporte = new PropertyChangeSupport(this);

    }

    // =========================
    // AGREGAR RECURSO

    public void agregarRecurso(Recurso recurso) {
        recursos.add(recurso);
        almacenamiento.guardar(recursos);

        soporte.firePropertyChange(
                "agregado",
                null,
                recurso);
    }

    public ArrayList<Recurso> getRecursos() {
        return recursos;
    }

    public Recurso buscarRecurso(int id) {

        for (Recurso recurso : recursos) {

            if (recurso.getId() == id) {
                return recurso;
            }
        }
        return null;
    }

    // =========================
    // MODIFICAR RECURSO

    public boolean modificarRecurso(int id, Categoria categoria, String descripcion) {

        Recurso recurso = buscarRecurso(id);

        if (recurso != null) {

            recurso.setCategoria(categoria);
            recurso.setDescripcion(descripcion);
            almacenamiento.guardar(recursos);

            return true;
        }

        return false;
    }

    // =========================
    // ELIMINAR RECURSO

    public boolean eliminarRecurso(int id) {

        Recurso recurso = buscarRecurso(id);

        if (recurso != null) {

            recursos.remove(recurso);

            almacenamiento.guardar(recursos);

            soporte.firePropertyChange(
                    "eliminado",
                    recurso,
                    null
            );

            return true;
        }

        return false;
    }

    // =========================
    // FILTRAR POR CATEGORÍA

    public ArrayList<Recurso> filtrarPorCategoria(Categoria categoria) {

        ArrayList<Recurso> resultado = new ArrayList<>();

        for (Recurso recurso : recursos) {

            if (recurso.getCategoria().getId() == categoria.getId()) {

                resultado.add(recurso);
            }
        }

        return resultado;
    }

    // =========================
    // OBTENER RECURSOS DISPONIBLES

    public ArrayList<Recurso> obtenerRecursosDisponibles(
            Categoria categoria,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            ListaReservas listaReservas) {

        ArrayList<Recurso> disponibles = new ArrayList<>();

        // Recorrer todos los recursos
        for (Recurso recurso : recursos) {

            // Verificar que pertenezca a la categoría
            if (recurso.getCategoria().getId() == categoria.getId()) {

                // Verificar si está disponible
                boolean reservado =
                        listaReservas.recursoEstaReservado(recurso, fecha, horaInicio, horaFin
                        );

                // Si NO está reservado, está disponible
                if (!reservado) {
                    disponibles.add(recurso);
                }
            }
        }

        return disponibles;
    }
    public ArrayList<Categoria> obtenerCategorias() {

        ArrayList<Categoria> categorias = new ArrayList<>();

        for (Recurso recurso : recursos) {

            Categoria categoria = recurso.getCategoria();

            boolean existe = false;

            for (Categoria c : categorias) {

                if (c.getId() == categoria.getId()) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                categorias.add(categoria);
            }
        }

        return categorias;
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        soporte.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        soporte.removePropertyChangeListener(listener);
    }
}
