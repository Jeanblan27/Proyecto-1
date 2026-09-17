package Data;

import Presentation.Model.Funcionario.Funcionario;
import Presentation.Model.Funcionario.Rol;

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
import java.util.ArrayList;
import java.util.List;

public class FuncionarioXML {

    private final File archivo;

    public FuncionarioXML(String ruta) {
        archivo = new File(ruta);
    }

    public void guardar(List<Funcionario> funcionarios) {

        try {

            File carpeta = archivo.getParentFile();

            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder =
                    factory.newDocumentBuilder();

            Document documento =
                    builder.newDocument();

            Element raiz =
                    documento.createElement("funcionarios");

            documento.appendChild(raiz);

            for (Funcionario funcionario : funcionarios) {

                Element elementoFuncionario =
                        documento.createElement("funcionario");

                agregarElemento(
                        documento,
                        elementoFuncionario,
                        "id",
                        funcionario.getId()
                );

                agregarElemento(
                        documento,
                        elementoFuncionario,
                        "nombre",
                        funcionario.getNombre()
                );

                agregarElemento(
                        documento,
                        elementoFuncionario,
                        "clave",
                        funcionario.getClave()
                );

                agregarElemento(
                        documento,
                        elementoFuncionario,
                        "telefono",
                        funcionario.getTelefono()
                );

                agregarElemento(
                        documento,
                        elementoFuncionario,
                        "rol",
                        funcionario.getRol().name()
                );

                raiz.appendChild(elementoFuncionario);
            }

            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();

            Transformer transformer =
                    transformerFactory.newTransformer();

            transformer.setOutputProperty(
                    OutputKeys.INDENT,
                    "yes"
            );

            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount",
                    "4"
            );

            DOMSource source =
                    new DOMSource(documento);

            StreamResult result =
                    new StreamResult(archivo);

            transformer.transform(source, result);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al guardar funcionarios: "
                            + e.getMessage(),
                    e
            );
        }
    }

    public List<Funcionario> cargar() {

        List<Funcionario> funcionarios =
                new ArrayList<>();

        if (!archivo.exists()) {
            return funcionarios;
        }

        try {

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder =
                    factory.newDocumentBuilder();

            Document documento =
                    builder.parse(archivo);

            documento.getDocumentElement().normalize();

            NodeList lista =
                    documento.getElementsByTagName("funcionario");

            for (int i = 0; i < lista.getLength(); i++) {

                Node nodo = lista.item(i);

                if (nodo.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                Element elemento =
                        (Element) nodo;

                String id =
                        obtenerTexto(elemento, "id");

                String nombre =
                        obtenerTexto(elemento, "nombre");

                String clave =
                        obtenerTexto(elemento, "clave");

                String telefono =
                        obtenerTexto(elemento, "telefono");

                String textoRol =
                        obtenerTexto(elemento, "rol");

                Rol rol =
                        Rol.valueOf(textoRol);

                Funcionario funcionario =
                        new Funcionario(
                                id,
                                nombre,
                                clave,
                                telefono,
                                rol
                        );

                funcionarios.add(funcionario);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al cargar funcionarios: "
                            + e.getMessage(),
                    e
            );
        }

        return funcionarios;
    }

    private void agregarElemento(
            Document documento,
            Element padre,
            String nombre,
            String valor) {

        Element elemento =
                documento.createElement(nombre);

        elemento.setTextContent(
                valor != null ? valor : ""
        );

        padre.appendChild(elemento);
    }

    private String obtenerTexto(
            Element padre,
            String nombre) {

        NodeList lista =
                padre.getElementsByTagName(nombre);

        if (lista.getLength() == 0) {
            return "";
        }

        return lista
                .item(0)
                .getTextContent()
                .trim();
    }
}

