package Logic;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Generador de reportes PDF en forma de tabla, usando Apache PDFBox
 * (decisión de equipo, ver conversación sobre licencias: PDFBox usa
 * licencia Apache 2.0, a diferencia de iText que es AGPL).
 *
 * Reemplaza la versión anterior escrita a mano con el formato PDF
 * directo. La firma del método público (generarTabla) se mantiene
 * igual a propósito, así que los controladores que ya la usaban no
 * necesitan cambiar cómo la llaman.
 */
public class GeneradorPDF {

    // Tamaño carta en orientación horizontal (en puntos; 1 punto = 1/72 pulgada)
    private static final float ANCHO_PAGINA = PDRectangle.LETTER.getHeight(); // 792
    private static final float ALTO_PAGINA = PDRectangle.LETTER.getWidth();   // 612
    private static final float MARGEN = 36f;
    private static final float ALTO_FILA = 18f;
    private static final float ESPACIO_TITULO = 26f;
    private static final int TAMANO_FUENTE_TITULO = 14;
    private static final int TAMANO_FUENTE_CELDA = 8;
    private static final float ANCHO_COLUMNA_HORA = 55f;

    /**
     * Genera un PDF con una tabla (encabezados + filas de datos) y lo
     * guarda en el archivo indicado. Si la tabla no cabe en una página,
     * continúa automáticamente en páginas siguientes repitiendo el
     * encabezado.
     *
     * @param archivo  archivo de salida (se sobrescribe si ya existe)
     * @param titulo   título que aparece arriba de la tabla en cada página
     * @param columnas nombres de columnas (la primera se asume "Hora" y
     *                 se le da un ancho fijo más angosto; el resto se
     *                 reparte el espacio restante en partes iguales)
     * @param datos    filas de datos; cada Object se convierte con
     *                 toString() antes de dibujarse
     */
    public static void generarTabla(File archivo, String titulo, String[] columnas, Object[][] datos)
            throws IOException {

        float[] anchosColumna = calcularAnchosColumna(columnas.length);
        PDFont fuente = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        float altoDisponibleFilas = ALTO_PAGINA - (2 * MARGEN) - ESPACIO_TITULO - ALTO_FILA;
        int filasPorPagina = Math.max(1, (int) Math.floor(altoDisponibleFilas / ALTO_FILA));

        int totalFilas = datos.length;
        int totalPaginas = Math.max(1, (int) Math.ceil(totalFilas / (double) filasPorPagina));

        try (PDDocument documento = new PDDocument()) {

            if (totalFilas == 0) {
                dibujarPagina(documento, titulo, columnas, new Object[0][], anchosColumna, fuente);
            } else {
                int inicio = 0;
                int numeroPagina = 1;

                while (inicio < totalFilas) {
                    int fin = Math.min(inicio + filasPorPagina, totalFilas);
                    Object[][] bloque = Arrays.copyOfRange(datos, inicio, fin);

                    String tituloPagina = titulo;
                    if (totalPaginas > 1) {
                        tituloPagina = titulo + " (pagina " + numeroPagina + " de " + totalPaginas + ")";
                    }

                    dibujarPagina(documento, tituloPagina, columnas, bloque, anchosColumna, fuente);

                    inicio = fin;
                    numeroPagina++;
                }
            }

            documento.save(archivo);
        }
    }

    private static void dibujarPagina(
            PDDocument documento, String titulo, String[] columnas,
            Object[][] filas, float[] anchosColumna, PDFont fuente) throws IOException {

        PDPage pagina = new PDPage(new PDRectangle(ANCHO_PAGINA, ALTO_PAGINA));
        documento.addPage(pagina);

        try (PDPageContentStream cs = new PDPageContentStream(documento, pagina)) {

            float xInicio = MARGEN;
            float yTope = ALTO_PAGINA - MARGEN;

            // ----- Título -----
            cs.beginText();
            cs.setFont(fuente, TAMANO_FUENTE_TITULO);
            cs.newLineAtOffset(xInicio, yTope - TAMANO_FUENTE_TITULO);
            cs.showText(titulo);
            cs.endText();

            float yTabla = yTope - ESPACIO_TITULO;
            int totalFilasConEncabezado = filas.length + 1;
            float altoTabla = totalFilasConEncabezado * ALTO_FILA;
            float anchoTabla = sumar(anchosColumna);

            cs.setLineWidth(0.5f);

            // ----- Líneas horizontales -----
            for (int f = 0; f <= totalFilasConEncabezado; f++) {
                float y = yTabla - (f * ALTO_FILA);
                cs.moveTo(xInicio, y);
                cs.lineTo(xInicio + anchoTabla, y);
                cs.stroke();
            }

            // ----- Líneas verticales -----
            float xLinea = xInicio;
            cs.moveTo(xLinea, yTabla);
            cs.lineTo(xLinea, yTabla - altoTabla);
            cs.stroke();
            for (float ancho : anchosColumna) {
                xLinea += ancho;
                cs.moveTo(xLinea, yTabla);
                cs.lineTo(xLinea, yTabla - altoTabla);
                cs.stroke();
            }

            // ----- Texto del encabezado -----
            float xCelda = xInicio;
            float yTextoEncabezado = yTabla - ALTO_FILA + 5;
            for (int c = 0; c < columnas.length; c++) {
                String texto = truncarTexto(
                        columnas[c] == null ? "" : columnas[c], anchosColumna[c] - 6, fuente, TAMANO_FUENTE_CELDA);
                dibujarTexto(cs, fuente, texto, xCelda + 3, yTextoEncabezado);
                xCelda += anchosColumna[c];
            }

            // ----- Texto de las filas -----
            for (int f = 0; f < filas.length; f++) {
                xCelda = xInicio;
                float yTextoFila = yTabla - ((f + 2) * ALTO_FILA) + 5;
                for (int c = 0; c < columnas.length; c++) {
                    Object valor = (filas[f] != null && c < filas[f].length) ? filas[f][c] : null;
                    String texto = truncarTexto(
                            valor == null ? "" : valor.toString(), anchosColumna[c] - 6, fuente, TAMANO_FUENTE_CELDA);
                    dibujarTexto(cs, fuente, texto, xCelda + 3, yTextoFila);
                    xCelda += anchosColumna[c];
                }
            }
        }
    }

    private static void dibujarTexto(PDPageContentStream cs, PDFont fuente, String texto, float x, float y)
            throws IOException {
        if (texto.isEmpty()) {
            return;
        }
        cs.beginText();
        cs.setFont(fuente, TAMANO_FUENTE_CELDA);
        cs.newLineAtOffset(x, y);
        cs.showText(texto);
        cs.endText();
    }

    private static float[] calcularAnchosColumna(int numColumnas) {

        float[] anchos = new float[numColumnas];
        float anchoTotal = ANCHO_PAGINA - (2 * MARGEN);

        if (numColumnas == 1) {
            anchos[0] = anchoTotal;
            return anchos;
        }

        anchos[0] = ANCHO_COLUMNA_HORA;
        float restante = anchoTotal - ANCHO_COLUMNA_HORA;
        float anchoResto = restante / (numColumnas - 1);
        for (int i = 1; i < numColumnas; i++) {
            anchos[i] = anchoResto;
        }
        return anchos;
    }

    private static float sumar(float[] valores) {
        float total = 0;
        for (float v : valores) {
            total += v;
        }
        return total;
    }

    /**
     * Trunca el texto para que no se salga de la celda, midiendo el
     * ancho real del texto con la fuente (PDFBox permite medir esto de
     * forma exacta, a diferencia de la versión artesanal anterior que
     * solo aproximaba el ancho promedio de un caracter).
     */
    private static String truncarTexto(String texto, float anchoDisponible, PDFont fuente, int tamanoFuente)
            throws IOException {

        if (texto.isEmpty() || calcularAnchoTexto(texto, fuente, tamanoFuente) <= anchoDisponible) {
            return texto;
        }

        String resultado = texto;
        while (resultado.length() > 0
                && calcularAnchoTexto(resultado + "...", fuente, tamanoFuente) > anchoDisponible) {
            resultado = resultado.substring(0, resultado.length() - 1);
        }

        return resultado.isEmpty() ? "" : resultado + "...";
    }

    private static float calcularAnchoTexto(String texto, PDFont fuente, int tamanoFuente) throws IOException {
        return fuente.getStringWidth(texto) / 1000f * tamanoFuente;
    }
}
