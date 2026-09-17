package Presentation.Controller.ControladorPaneles;

import Presentation.Model.Reserva.ListaReservas;
import Presentation.Model.Reserva.Reserva;
import Logic.GeneradorPDF;
import Presentation.View.PanelsDeReservas.PanelActividades;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

public class ControladorActividades {

    // Mismo rango de horas que Calendarización (6am-10pm, según el
    // mockup del PDF), para que ambas vistas sean consistentes.
    private static final int HORA_INICIO = 6;
    private static final int HORA_FIN = 22;

    private static final String[] NOMBRES_DIA = {
            "Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom"
    };

    private final PanelActividades vista;
    private final ListaReservas modeloReservas;

    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter formatoCorto = DateTimeFormatter.ofPattern("dd/MM");

    public ControladorActividades(PanelActividades vista, ListaReservas modeloReservas) {
        this.vista = vista;
        this.modeloReservas = modeloReservas;
        iniciarListeners();
    }

    private void iniciarListeners() {
        vista.getBotonCargar().addActionListener(e -> cargarMatriz());
        vista.getBotonImprimir().addActionListener(e -> imprimir());
    }

    private void cargarMatriz() {
        MatrizActividades matriz = construirMatriz();
        if (matriz != null) {
            vista.mostrarMatriz(matriz.columnas, matriz.datos);
        }
    }

    private void imprimir() {

        MatrizActividades matriz = construirMatriz();
        if (matriz == null) {
            return; // construirMatriz ya mostró el mensaje de error correspondiente
        }

        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new File("actividades.pdf"));
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
     * Valida la fecha de referencia y arma la matriz semanal (columnas +
     * datos). Se usa tanto para mostrarla en pantalla como para
     * imprimirla.
     *
     * @return la matriz lista para usar, o null si hubo un error de
     *         validación (ya se le mostró el mensaje al usuario).
     */
    private MatrizActividades construirMatriz() {

        String textoFecha = vista.getCampoFechaReferencia().getText();
        if (textoFecha == null || textoFecha.isBlank()) {
            JOptionPane.showMessageDialog(vista, "Debe ingresar una fecha de referencia.");
            return null;
        }

        LocalDate fechaReferencia;
        try {
            fechaReferencia = LocalDate.parse(textoFecha, formatoFecha);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(vista, "Fecha inválida. Use el formato dd/MM/yyyy.");
            return null;
        }

        // DECISIÓN DE DISEÑO: la semana va de lunes a domingo, calculada
        // a partir de la fecha de referencia ingresada.
        LocalDate lunes = fechaReferencia.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        LocalDate[] diasSemana = new LocalDate[7];
        String[] columnas = new String[8];
        columnas[0] = "Hora";
        for (int i = 0; i < 7; i++) {
            diasSemana[i] = lunes.plusDays(i);
            columnas[i + 1] = NOMBRES_DIA[i] + " " + diasSemana[i].format(formatoCorto);
        }

        int totalFilas = HORA_FIN - HORA_INICIO;
        Object[][] datos = new Object[totalFilas][8];

        for (int fila = 0; fila < totalFilas; fila++) {
            LocalTime hora = LocalTime.of(HORA_INICIO + fila, 0);
            datos[fila][0] = hora.toString();

            for (int dia = 0; dia < 7; dia++) {
                ArrayList<Reserva> reservas = modeloReservas.buscarReservasEnHora(diasSemana[dia], hora);

                if (reservas.isEmpty()) {
                    datos[fila][dia + 1] = "";
                } else {
                    StringBuilder texto = new StringBuilder();
                    for (Reserva reserva : reservas) {
                        if (texto.length() > 0) {
                            texto.append("; ");
                        }
                        texto.append(reserva.getActividad())
                                .append(" (")
                                .append(reserva.getFuncionario().getNombre())
                                .append(")");
                    }
                    datos[fila][dia + 1] = texto.toString();
                }
            }
        }

        String titulo = "Actividades - Semana del " + lunes.format(formatoCorto)
                + " al " + lunes.plusDays(6).format(formatoCorto);

        return new MatrizActividades(titulo, columnas, datos);
    }

    /** Pequeño contenedor para no duplicar la construcción de la matriz entre Cargar e Imprimir. */
    private static class MatrizActividades {
        final String titulo;
        final String[] columnas;
        final Object[][] datos;

        MatrizActividades(String titulo, String[] columnas, Object[][] datos) {
            this.titulo = titulo;
            this.columnas = columnas;
            this.datos = datos;
        }
    }
}