package Data;

import Model.Categoria.Categoria;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategoriaXML {

    private static final String ARCHIVO = "datos/categorias.xml";

    public void guardar(List<Categoria> categorias) {

        try {
            File archivo = new File(ARCHIVO);

            // Crear la carpeta datos si no existe
            File carpeta = archivo.getParentFile();

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder =
                    factory.newDocumentBuilder();

            Document documento = builder.newDocument();

            // <categorias>
            Element raiz =
                    documento.createElement("categorias");

            documento.appendChild(raiz);

            // Crear cada categoría
            for (Categoria categoria : categorias) {

                // <categoria>
                Element elementoCategoria =
                        documento.createElement("categoria");

                // <id>
                Element id =
                        documento.createElement("id");

                id.setTextContent(
                        String.valueOf(categoria.getId())
                );

                elementoCategoria.appendChild(id);

                // <descripcion>
                Element descripcion =
                        documento.createElement("descripcion");

                descripcion.setTextContent(
                        categoria.getDescripcion()
                );

                elementoCategoria.appendChild(descripcion);

                raiz.appendChild(elementoCategoria);
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

    public List<Categoria> cargar() {

        List<Categoria> categorias =
                new ArrayList<>();

        File archivo = new File(ARCHIVO);

        // Si todavía no existe el XML
        if (!archivo.exists()) {
            return categorias;
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
                    documento.getElementsByTagName("categoria");

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

                String descripcion =
                        elemento
                                .getElementsByTagName("descripcion")
                                .item(0)
                                .getTextContent();

                Categoria categoria =
                        new Categoria(
                                id,
                                descripcion
                        );

                categorias.add(categoria);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return categorias;
    }
}
