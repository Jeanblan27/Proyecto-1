package Presentation.Controller.ControladorPaneles;

import Logic.GeneradorPDF;
import Presentation.Model.Reserva.Estado;
import Presentation.Model.Reserva.ListaReservas;
import Presentation.Model.Reserva.Reserva;
import Presentation.Model.Recurso.Recurso;
import Presentation.View.PanelsDeReservas.PanelEstadisticas;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ControladorEstadisticas {

    private final PanelEstadisticas vista;
    private final ListaReservas modeloReservas;

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ControladorEstadisticas(
            PanelEstadisticas vista,
            ListaReservas modeloReservas) {

        this.vista = vista;
        this.modeloReservas = modeloReservas;

        iniciarListeners();
    }


    // LISTENERS
    private void iniciarListeners() {
        vista.getBotonCargar().addActionListener(e -> cargarEstadisticas());
        vista.getBotonImprimir().addActionListener(e -> imprimirPDF());
    }


    // CARGAR ESTADÍSTICAS

    private void cargarEstadisticas() {

        LocalDate fechaInicio = obtenerFechaInicio();

        if (fechaInicio == null) {
            return;
        }

        LocalDate fechaFin = obtenerFechaFin();

        if (fechaFin == null) {
            return;
        }

        if (fechaFin.isBefore(fechaInicio)) {
            JOptionPane.showMessageDialog(vista, "La fecha final no puede ser anterior a la fecha inicial.", "Fechas inválidas", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cargarEstadisticaRecursos(fechaInicio, fechaFin);
        cargarEstadisticaActividades(fechaInicio, fechaFin);
    }


    // LEER FECHAS
    private LocalDate obtenerFechaInicio() {

        String texto = vista.getCampoFechaInicio().getText().trim();

        if (texto.isEmpty()) {

            JOptionPane.showMessageDialog(vista, "Debe ingresar la fecha inicial.", "Error", JOptionPane.ERROR_MESSAGE);

            return null;
        }

        try {

            return LocalDate.parse(texto, formatoFecha);

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(vista, "La fecha inicial no es válida.\n" + "Utilice el formato dd/MM/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private LocalDate obtenerFechaFin() {

        String texto = vista.getCampoFechaFin().getText().trim();

        if (texto.isEmpty()) {

            JOptionPane.showMessageDialog(vista, "Debe ingresar la fecha final.", "Error", JOptionPane.ERROR_MESSAGE);

            return null;
        }

        try {

            return LocalDate.parse(texto, formatoFecha);

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(vista, "La fecha final no es válida.\n" + "Utilice el formato dd/MM/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);

            return null;
        }
    }


    // OBTENER RESERVAS DEL PERÍODO
    private List<Reserva> obtenerReservasPeriodo(
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        List<Reserva> resultado = new ArrayList<>();

        for (Reserva reserva :
                modeloReservas.getReservas()) {

            // No contar reservas canceladas
            if (reserva.getEstado() == Estado.CANCELADA) {
                continue;
            }

            LocalDate fecha =
                    reserva.getFecha();

            if (fecha == null) {
                continue;
            }

            boolean dentroDelPeriodo = !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);

            if (dentroDelPeriodo) {
                resultado.add(reserva);
            }
        }

        return resultado;
    }


    // ESTADÍSTICA DE RECURSOS
    private void cargarEstadisticaRecursos(
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        List<Reserva> reservas = obtenerReservasPeriodo(fechaInicio, fechaFin);


        Map<String, Integer> cantidades = new LinkedHashMap<>();

        for (Reserva reserva : reservas) {

            if (reserva.getRecursos() == null) {
                continue;
            }

            for (Recurso recurso : reserva.getRecursos()) {

                if (recurso == null ||
                        recurso.getCategoria() == null) {
                    continue;
                }

                String categoria = recurso.getCategoria().getDescripcion();
                cantidades.put(categoria, cantidades.getOrDefault(categoria, 0) + 1);

            }
        }

        Object[][] datos = new Object[cantidades.size()][2];

        int fila = 0;

        for (Map.Entry<String, Integer> entrada : cantidades.entrySet()) {
            datos[fila][0] = entrada.getKey();
            datos[fila][1] = entrada.getValue();
            fila++;
        }

        vista.mostrarRecursos(datos);

        vista.mostrarGraficoRecursos(crearGraficoBarras(cantidades, "Recursos utilizados por categoría"));
    }


    // ESTADÍSTICA DE ACTIVIDADES
    private void cargarEstadisticaActividades(
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        List<Reserva> reservas = obtenerReservasPeriodo(fechaInicio, fechaFin);

        Map<String, Integer> cantidades = new LinkedHashMap<>();


        LocalDate semanaActual = fechaInicio.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        while (!semanaActual.isAfter(fechaFin)) {

            LocalDate domingo =
                    semanaActual.plusDays(6);

            String nombreSemana =
                    semanaActual.format(formatoFecha)
                            + " - "
                            + domingo.format(formatoFecha);

            cantidades.put(nombreSemana, 0);

            semanaActual = semanaActual.plusWeeks(1);

        }


        for (Reserva reserva : reservas) {

            LocalDate fecha = reserva.getFecha();

            LocalDate lunes = fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

            LocalDate domingo = lunes.plusDays(6);

            String nombreSemana = lunes.format(formatoFecha) + " - " + domingo.format(formatoFecha);

            if (cantidades.containsKey(nombreSemana)) {
                cantidades.put(nombreSemana, cantidades.get(nombreSemana) + 1);
            }
        }

        Object[][] datos = new Object[cantidades.size()][2];

        int fila = 0;

        for (Map.Entry<String, Integer> entrada : cantidades.entrySet()) {
            datos[fila][0] = entrada.getKey();
            datos[fila][1] = entrada.getValue();
            fila++;
        }

        vista.mostrarActividades(datos);

        vista.mostrarGraficoActividades(crearGraficoBarras(cantidades, "Actividades por semana"));
    }


    // GRÁFICO DE BARRAS
    private JPanel crearGraficoBarras(
            Map<String, Integer> datos,
            String titulo) {

        return new GraficoBarras(datos, titulo);
    }


    // GENERACIÓN DEL PDF
    private void imprimirPDF() {

        LocalDate fechaInicio = obtenerFechaInicio();

        if (fechaInicio == null) {
            return;
        }

        LocalDate fechaFin = obtenerFechaFin();

        if (fechaFin == null) {
            return;
        }

        if (fechaFin.isBefore(fechaInicio)) {
            JOptionPane.showMessageDialog(vista, "La fecha final no puede ser anterior " + "a la fecha inicial.", "Fechas inválidas", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Reserva> reservas = obtenerReservasPeriodo(fechaInicio, fechaFin);

        Map<String, Integer> recursos = calcularRecursos(reservas);

        Map<String, Integer> actividades = calcularActividades(reservas, fechaInicio, fechaFin);

        JFileChooser selector = new JFileChooser();

        selector.setSelectedFile(new File("estadisticas.pdf"));

        int opcion = selector.showSaveDialog(vista);

        if (opcion != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File archivo = selector.getSelectedFile();

        try {

            Object[][] datos = construirDatosPDF(recursos, actividades);

            String[] columnas = {"Tipo", "Nombre", "Cantidad"};

            GeneradorPDF.generarTabla(archivo, "Estadísticas del " + fechaInicio.format(formatoFecha) + " al " + fechaFin.format(formatoFecha), columnas, datos);

            JOptionPane.showMessageDialog(vista, "Reporte PDF generado correctamente.");

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(vista, "No se pudo generar el PDF:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // CÁLCULO PARA PDF
    private Map<String, Integer> calcularRecursos(
            List<Reserva> reservas) {

        Map<String, Integer> cantidades = new LinkedHashMap<>();

        for (Reserva reserva : reservas) {

            if (reserva.getRecursos() == null) {
                continue;
            }

            for (Recurso recurso :
                    reserva.getRecursos()) {

                if (recurso == null ||
                        recurso.getCategoria() == null) {
                    continue;
                }

                String categoria = recurso.getCategoria().getDescripcion();

                cantidades.put(categoria, cantidades.getOrDefault(categoria, 0) + 1);
            }
        }

        return cantidades;
    }

    private Map<String, Integer> calcularActividades(
            List<Reserva> reservas,
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        Map<String, Integer> cantidades = new LinkedHashMap<>();

        LocalDate semana = fechaInicio.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        while (!semana.isAfter(fechaFin)) {

            LocalDate domingo = semana.plusDays(6);
            String nombre = semana.format(formatoFecha) + " - " + domingo.format(formatoFecha);
            cantidades.put(nombre, 0);
            semana = semana.plusWeeks(1);

        }

        for (Reserva reserva : reservas) {

            LocalDate lunes = reserva.getFecha().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

            LocalDate domingo = lunes.plusDays(6);

            String nombre = lunes.format(formatoFecha) + " - " + domingo.format(formatoFecha);

            if (cantidades.containsKey(nombre)) {
                cantidades.put(nombre, cantidades.get(nombre) + 1);
            }
        }

        return cantidades;
    }

    private Object[][] construirDatosPDF(
            Map<String, Integer> recursos,
            Map<String, Integer> actividades) {

        int total = recursos.size() + actividades.size();

        Object[][] datos = new Object[total][3];

        int fila = 0;

        for (Map.Entry<String, Integer> entrada :
                recursos.entrySet()) {

            datos[fila][0] = "Recurso";
            datos[fila][1] = entrada.getKey();
            datos[fila][2] = entrada.getValue();

            fila++;
        }

        for (Map.Entry<String, Integer> entrada :
                actividades.entrySet()) {

            datos[fila][0] = "Actividad";
            datos[fila][1] = entrada.getKey();
            datos[fila][2] = entrada.getValue();

            fila++;
        }

        return datos;
    }


    // CLASE PARA DIBUJAR GRÁFICO DE BARRAS
    private static class GraficoBarras extends JPanel {

        private final Map<String, Integer> datos;
        private final String titulo;

        public GraficoBarras(
                Map<String, Integer> datos,
                String titulo) {

            this.datos = datos;
            this.titulo = titulo;

            setPreferredSize(new Dimension(500, 350));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(
                Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            try {
                dibujarGrafico(g2);
            } finally {
                g2.dispose();
            }
        }

        private void dibujarGrafico(
                Graphics2D g) {

            int ancho = getWidth();

            int alto = getHeight();

            int margenIzquierdo = 60;
            int margenDerecho = 30;
            int margenSuperior = 50;
            int margenInferior = 80;


            // TÍTULO

            g.setFont(new Font("Arial", Font.BOLD, 16));

            g.drawString(titulo, margenIzquierdo, 25);

            if (datos.isEmpty()) {
                g.setFont(new Font("Arial", Font.PLAIN, 14));
                g.drawString("No hay datos para mostrar.", margenIzquierdo, alto / 2);
                return;
            }

            // ÁREA DEL GRÁFICO

            int x = margenIzquierdo;

            int y = margenSuperior;

            ancho = ancho - margenIzquierdo - margenDerecho;

            int altoGrafico = alto - margenSuperior - margenInferior;

            // Eje Y
            g.drawLine(x, y, x, y + altoGrafico);

            // Eje X
            g.drawLine(x, y + altoGrafico, x + ancho, y + altoGrafico);

            int maximo = 0;

            for (Integer valor : datos.values()) {

                if (valor > maximo) {
                    maximo = valor;
                }
            }

            if (maximo == 0) {
                maximo = 1;
            }

            int cantidadBarras = datos.size();

            int espacio = ancho / cantidadBarras;

            int anchoBarra = Math.max(20, espacio / 2);

            int indice = 0;

            for (Map.Entry<String, Integer> entrada : datos.entrySet()) {

                int valor = entrada.getValue();

                int alturaBarra = (int) (((double) valor / maximo) * altoGrafico);

                int xBarra = x + indice * espacio + (espacio - anchoBarra) / 2;

                int yBarra = y + altoGrafico - alturaBarra;

                // Barra
                g.fillRect(xBarra, yBarra, anchoBarra, alturaBarra);

                // Valor
                g.setFont(new Font("Arial", Font.BOLD, 12));

                String valorTexto = String.valueOf(valor);

                g.drawString(valorTexto, xBarra + anchoBarra / 2 - 4, yBarra - 5);

                // Etiqueta
                g.setFont(new Font("Arial", Font.PLAIN, 10));

                String etiqueta = entrada.getKey();

                if (etiqueta.length() > 12) {
                    etiqueta = etiqueta.substring(0, 12) + "...";
                }

                g.drawString(etiqueta, xBarra, y + altoGrafico + 20);

                indice++;
            }
        }
    }
}

