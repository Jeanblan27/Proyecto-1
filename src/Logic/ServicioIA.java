package Logic;

import Presentation.Model.Categoria.Categoria;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseTextConfig;
import com.openai.models.responses.StructuredResponseCreateParams;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ServicioIA {

    private final OpenAIClient cliente;

    public ServicioIA() {
        cliente = OpenAIOkHttpClient.fromEnv();
    }

    public DatosReservaIA extraerReserva(
            String frase,
            List<Categoria> categoriasDisponibles) {

        String categorias = categoriasDisponibles.stream()
                .map(Categoria::getDescripcion)
                .collect(Collectors.joining(", "));

        String fechaActual = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String instrucciones = """
                Eres un asistente para un sistema de reservas de recursos.

                Fecha actual: %s

                Categorías disponibles:
                %s

                Analiza la frase del usuario y extrae la información
                necesaria para crear una reserva.

                Debes devolver:

                - actividad
                - fecha
                - horaInicio
                - horaFin
                - categorias

                Reglas:

                1. La fecha debe tener el formato dd/MM/yyyy.
                2. La hora debe tener el formato HH:mm.
                3. Convierte "10 de la mañana" a "10:00".
                4. Convierte "2 de la tarde" a "14:00".
                5. Si dice "de 10 a 12", utiliza 10:00 y 12:00.
                6. Si utiliza "mañana", "hoy" u otra fecha relativa,
                   calcula la fecha utilizando la fecha actual proporcionada.
                7. Las categorías deben coincidir con las categorías
                   disponibles.
                8. No inventes categorías.
                9. Si el usuario escribe una categoría con diferente
                   mayúscula/minúscula, utiliza el nombre disponible.
                10. Extrae solamente la información que corresponda
                    a la reserva.

                Frase del usuario:

                %s
                """.formatted(
                fechaActual,
                categorias,
                frase
        );

        StructuredResponseCreateParams<DatosReservaIA> parametros =
                ResponseCreateParams.builder()
                        .input(instrucciones)
                        .text(
                                ResponseTextConfig.builder()
                                        .format(DatosReservaIA.class)
                                        .build()
                        )
                        .model(ChatModel.GPT_5)
                        .build();

        return cliente.responses()
                .create(parametros)
                .output()
                .stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "La IA no devolvió una respuesta válida."
                        )
                );
    }
}