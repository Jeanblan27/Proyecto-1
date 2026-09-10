package Data;

import Presentation.Model.Categoria.Categoria;
import Presentation.Model.Recurso.Recurso;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecursoXML {

    private static final String ARCHIVO = "datos/recursos.xml";

    public void guardar(List<Recurso> recursos) {

        try {
            File archivo = new File(ARCHIVO);

            File carpeta = archivo.getParentFile();

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder =
                    factory.newDocumentBuilder();

            Document documento =
                    builder.newDocument();

            // <recursos>
            Element raiz =
                    documento.createElement("recursos");

            documento.appendChild(raiz);

            for (Recurso recurso : recursos) {

                // <recurso>
                Element elementoRecurso =
                        documento.createElement("recurso");

                // <id>
                Element id =
                        documento.createElement("id");

                id.setTextContent(
                        String.valueOf(recurso.getId())
                );

                elementoRecurso.appendChild(id);

                // <categoriaId>
                Element categoriaId =
                        documento.createElement("categoriaId");

                categoriaId.setTextContent(
                        String.valueOf(
                                recurso.getCategoria().getId()
                        )
                );

                elementoRecurso.appendChild(categoriaId);

                // <descripcion>
                Element descripcion =
                        documento.createElement("descripcion");

                descripcion.setTextContent(
                        recurso.getDescripcion()
                );

                elementoRecurso.appendChild(descripcion);

                raiz.appendChild(elementoRecurso);
            }

            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();

            Transformer transformer =
                    transformerFactory.newTransformer();

            transformer.setOutputProperty(
                    OutputKeys.INDENT,
                    "yes"
            );

            DOMSource source =
                    new DOMSource(documento);

            StreamResult resultado =
                    new StreamResult(archivo);

            transformer.transform(source, resultado);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public List<Recurso> cargar(
            List<Categoria> categorias) {

        List<Recurso> recursos =
                new ArrayList<>();

        File archivo =
                new File(ARCHIVO);

        if (!archivo.exists()) {
            return recursos;
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
                    documento.getElementsByTagName("recurso");

            for (int i = 0; i < lista.getLength(); i++) {

                Element elemento =
                        (Element) lista.item(i);

                int id =
                        Integer.parseInt(
                                elemento
                                        .getElementsByTagName("id")
                                        .item(0)
                                        .getTextContent()
                        );

                int categoriaId =
                        Integer.parseInt(
                                elemento
                                        .getElementsByTagName("categoriaId")
                                        .item(0)
                                        .getTextContent()
                        );

                String descripcion =
                        elemento
                                .getElementsByTagName("descripcion")
                                .item(0)
                                .getTextContent();

                Categoria categoria = null;

                for (Categoria c : categorias) {

                    if (c.getId() == categoriaId) {
                        categoria = c;
                        break;
                    }
                }

                if (categoria != null) {

                    Recurso recurso =
                            new Recurso(
                                    id,
                                    categoria,
                                    descripcion
                            );

                    recursos.add(recurso);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return recursos;
    }
}