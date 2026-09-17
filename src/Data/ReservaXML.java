package Data;

import Presentation.Model.Funcionario.Funcionario;
import Presentation.Model.Funcionario.ListaFuncionarios;
import Presentation.Model.Recurso.ListaRecursos;
import Presentation.Model.Recurso.Recurso;
import Presentation.Model.Reserva.Estado;
import Presentation.Model.Reserva.Reserva;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaXML {

    private final File archivo;

    public ReservaXML() {
        archivo = new File("Datos/reservas.xml");
    }


    // =====================================================
    // GUARDAR
    // =====================================================

    public void guardar(List<Reserva> reservas) {

        try {

            File carpeta = archivo.getParentFile();

            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }


            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document documento = builder.newDocument();

            // <reservas>
            Element raiz = documento.createElement("reservas");

            documento.appendChild(raiz);


            for (Reserva reserva : reservas) {

                // <reserva>
                Element elementoReserva = documento.createElement("reserva");


                // <id>
                agregarElemento(documento, elementoReserva, "id", String.valueOf(reserva.getId())
                );


                // <funcionario>
                Element elementoFuncionario = documento.createElement("funcionario");

                agregarElemento(documento, elementoFuncionario, "id", reserva.getFuncionario().getId());

                elementoReserva.appendChild(elementoFuncionario);


                // <actividad>
                agregarElemento(documento, elementoReserva, "actividad", reserva.getActividad());


                // <fecha>
                agregarElemento(documento, elementoReserva, "fecha", reserva.getFecha().toString());


                // <horaInicio>
                agregarElemento(documento, elementoReserva, "horaInicio", reserva.getHoraInicio().toString());

                // <horaFin>
                agregarElemento(documento, elementoReserva, "horaFin", reserva.getHoraFin().toString());

                // <estado>
                agregarElemento(documento, elementoReserva, "estado", reserva.getEstado().name());

                // <recursos>
                Element elementoRecursos = documento.createElement("recursos");


                for (Recurso recurso : reserva.getRecursos()) {

                    // <recurso>
                    Element elementoRecurso = documento.createElement("recurso");

                    agregarElemento(documento, elementoRecurso, "id", String.valueOf(recurso.getId()));


                    elementoRecursos.appendChild(elementoRecurso);
                }


                elementoReserva.appendChild(elementoRecursos);


                raiz.appendChild(elementoReserva);
            }


            // =================================================
            // FORMATEAR XML
            // =================================================

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");


            DOMSource source = new DOMSource(documento);

            StreamResult result = new StreamResult(archivo);


            transformer.transform(source, result);


        } catch (Exception e) {

            throw new RuntimeException("Error al guardar reservas: " + e.getMessage(), e);
        }
    }


    // =====================================================
    // CARGAR
    // =====================================================

    public List<Reserva> cargar(
            ListaFuncionarios listaFuncionarios, ListaRecursos listaRecursos) {

        List<Reserva> reservas = new ArrayList<>();


        // Si todavía no existe el XML
        if (!archivo.exists()) {
            return reservas;
        }


        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document documento = builder.parse(archivo);

            documento.getDocumentElement().normalize();


            NodeList lista = documento.getElementsByTagName("reserva");


            for (int i = 0; i < lista.getLength(); i++) {

                Node nodo = lista.item(i);


                if (nodo.getNodeType() != Node.ELEMENT_NODE) {

                    continue;
                }


                Element elementoReserva = (Element) nodo;


                // =================================================
                // ID
                // =================================================

                int id = Integer.parseInt(obtenerTexto(elementoReserva, "id"));


                // =================================================
                // FUNCIONARIO
                // =================================================

                Element elementoFuncionario = (Element) elementoReserva.getElementsByTagName("funcionario").item(0);


                String funcionarioId = obtenerTexto(elementoFuncionario, "id");


                Funcionario funcionario = listaFuncionarios.buscarfuncionario(funcionarioId);


                // Si el funcionario ya no existe,
                // no podemos crear correctamente la reserva
                if (funcionario == null) {

                    System.out.println("Funcionario no encontrado: " + funcionarioId);

                    continue;
                }


                // =================================================
                // ACTIVIDAD
                // =================================================

                String actividad = obtenerTexto(elementoReserva, "actividad");


                // =================================================
                // FECHA
                // =================================================

                LocalDate fecha = LocalDate.parse(obtenerTexto(elementoReserva, "fecha"));


                // =================================================
                // HORA INICIO
                // =================================================

                LocalTime horaInicio = LocalTime.parse(obtenerTexto(elementoReserva, "horaInicio"));


                // =================================================
                // HORA FIN
                // =================================================

                LocalTime horaFin = LocalTime.parse(obtenerTexto(elementoReserva, "horaFin"));


                // =================================================
                // ESTADO
                // =================================================

                String textoEstado = obtenerTexto(elementoReserva, "estado");


                Estado estado = Estado.valueOf(textoEstado);


                // =================================================
                // RECURSOS
                // =================================================

                ArrayList<Recurso> recursos = new ArrayList<>();


                Element elementoRecursos = (Element) elementoReserva.getElementsByTagName("recursos").item(0);


                if (elementoRecursos != null) {

                    NodeList listaRecurso = elementoRecursos.getElementsByTagName("recurso");

                    for (int j = 0; j < listaRecurso.getLength(); j++) {

                        Element elementoRecurso = (Element) listaRecurso.item(j);


                        int recursoId = Integer.parseInt(obtenerTexto(elementoRecurso, "id"));
                        Recurso recurso = listaRecursos.buscarRecurso(recursoId);

                        if (recurso != null) {

                            recursos.add(recurso);
                        } else {

                            System.out.println("Recurso no encontrado: " + recursoId);
                        }
                    }
                }


                // =================================================
                // CREAR RESERVA
                // =================================================

                Reserva reserva = new Reserva(funcionario, actividad, fecha, horaInicio, horaFin, recursos
                        );


                // Restaurar ID
                reserva.setId(id);


                // Restaurar estado
                reserva.setEstado(estado);


                reservas.add(reserva);
            }


        } catch (Exception e) {

            throw new RuntimeException("Error al cargar reservas: " + e.getMessage(), e);
        }


        return reservas;
    }


    // =====================================================
    // MÉTODOS AUXILIARES
    // =====================================================

    private void agregarElemento(Document documento, Element padre, String nombre, String valor) {

        Element elemento = documento.createElement(nombre);

        elemento.setTextContent(valor != null ? valor : "");

        padre.appendChild(elemento);
    }


    private String obtenerTexto(Element padre, String nombre) {

        NodeList lista = padre.getElementsByTagName(nombre);


        if (lista.getLength() == 0) {
            return "";
        }


        return lista.item(0).getTextContent().trim();
    }
}
