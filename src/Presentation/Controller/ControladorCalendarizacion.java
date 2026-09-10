package Controller.ControladorPaneles;

import Presentation.Model.Categoria.Categoria;
import Presentation.Model.Categoria.ListaCategorias;
import Presentation.Model.Recurso.Recurso;
import Presentation.Model.Recurso.ListaRecursos;
import Presentation.Model.Reserva.ListaReservas;
import Presentation.Model.Reserva.Reserva;
import Reportes.GeneradorPDF;
import Presentation.View.PanelsDeReservas.PanelCalendarizacion;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ControladorCalendarizacion {

    // DECISIÓN DE DISEÑO: el PDF no especifica el rango de horas de la
    // matriz. Se usa 6:00 a.m. a 10:00 p.m. porque así aparece en el
    // mockup de ejemplo del enunciado ("Calendarización de Recursos").
    private static final int HORA_INICIO = 6;
    private static final int HORA_FIN = 22; // exclusivo, última fila es 21:00

    private final PanelCalendarizacion vista;
    private final ListaCategorias modeloCategoria;
    private final ListaRecursos modeloRecursos;
    private final ListaReservas modeloReservas;

    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ControladorCalendarizacion(
            PanelCalendarizacion vista,
            ListaCategorias modeloCategoria,
            ListaRecursos modeloRecursos,
            ListaReservas modeloReservas) {

        this.vista = vista;
        this.modeloCategoria = modeloCategoria;
        this.modeloRecursos = modeloRecursos;
        this.modeloReservas = modeloReservas;

        vista.cargarCategorias(modeloCategoria.getCategorias());
        iniciarListeners();
    }

    private void iniciarListeners() {
        vista.getBotonCargar().addActionListener(e -> cargarMatriz());
        vista.getBotonImprimir().addActionListener(e -> imprimir());
    }

    private void cargarMatriz() {
        MatrizCalendarizacion matriz = construirMatriz();
        if (matriz != null) {
            vista.mostrarMatriz(matriz.columnas, matriz.datos);
        }
    }

    private void imprimir() {

        MatrizCalendarizacion matriz = construirMatriz();
        if (matriz == null) {
            return; // construirMatriz ya mostró el mensaje de error correspondiente
        }

        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new File("calendarizacion.pdf"));
        int opcion = selector.showSaveDialog(vista);

        if (opcion != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            GeneradorPDF.generarTabla(
                    selector.getSelectedFile(), matriz.titulo, matriz.columnas, matriz.datos);
            JOptionPane.showMessageDialog(vista, "Reporte generado correctamente.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    vista, "No se pudo generar el PDF: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida los filtros seleccionados y arma la matriz (columnas + datos).
     * Se usa tanto para mostrarla en pantalla como para imprimirla, así
     * no se duplica la lógica de construcción en dos lugares.
     *
     * @return la matriz lista para usar, o null si hubo un error de
     *         validación (en cuyo caso ya se le mostró el mensaje al usuario).
     */
    private MatrizCalendarizacion construirMatriz() {

        Categoria categoria = (Categoria) vista.getComboCategoria().getSelectedItem();
        if (categoria == null) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar una categoría.");
            return null;
        }

        String textoFecha = vista.getCampoFecha().getText();
        if (textoFecha == null || textoFecha.isBlank()) {
            JOptionPane.showMessageDialog(vista, "Debe ingresar una fecha.");
            return null;
        }

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(textoFecha, formatoFecha);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(vista, "Fecha inválida. Use el formato dd/MM/yyyy.");
            return null;
        }

        List<Recurso> recursos = modeloRecursos.filtrarPorCategoria(categoria);
        if (recursos.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Esa categoría no tiene recursos registrados.");
            return null;
        }

        String[] columnas = new String[recursos.size() + 1];
        columnas[0] = "Hora";
        for (int i = 0; i < recursos.size(); i++) {
            columnas[i + 1] = recursos.get(i).getDescripcion();
        }

        int totalFilas = HORA_FIN - HORA_INICIO;
        Object[][] datos = new Object[totalFilas][recursos.size() + 1];

        for (int fila = 0; fila < totalFilas; fila++) {
            LocalTime hora = LocalTime.of(HORA_INICIO + fila, 0);
            datos[fila][0] = hora.toString();

            for (int col = 0; col < recursos.size(); col++) {
                Recurso recurso = recursos.get(col);
                Reserva reserva = modeloReservas.buscarReservaEnHora(recurso, fecha, hora);

                datos[fila][col + 1] = reserva == null
                        ? ""
                        : reserva.getActividad() + " - " + reserva.getFuncionario().getNombre();
            }
        }

        String titulo = "Calendarizacion de Recursos - " + categoria.getDescripcion() + " - " + textoFecha;

        return new MatrizCalendarizacion(titulo, columnas, datos);
    }

    /** Pequeño contenedor para no duplicar la construcción de la matriz entre Cargar e Imprimir. */
    private static class MatrizCalendarizacion {
        final String titulo;
        final String[] columnas;
        final Object[][] datos;

        MatrizCalendarizacion(String titulo, String[] columnas, Object[][] datos) {
            this.titulo = titulo;
            this.columnas = columnas;
            this.datos = datos;
        }
    }
}
