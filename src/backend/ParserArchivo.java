package backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserArchivo {

    /**
     * Carga preguntas desde un archivo de texto separado por ';'.
     * Formato esperado por línea:
     *    enunciado;op1;op2;...;opN;respuestaCorrecta;tipo;nivelBloom
     *
     * @param rutaArchivo Ruta al archivo de preguntas
     * @return Lista de objetos Pregunta
     * @throws IOException Si ocurre error de lectura o formato inválido
     */
    public static List<Pregunta> cargar(String rutaArchivo) throws IOException {
        List<Pregunta> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numeroLinea = 0;

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                if (linea.trim().isEmpty()) continue; // Ignorar líneas en blanco

                // Separar por ';' (el -1 mantiene campos vacíos si los hubiera)
                String[] partes = linea.split(";", -1);

                // Validar mínimo de campos: enunciado + al menos 2 opciones + respuesta + tipo + nivelBloom = 5
                if (partes.length < 5) {
                    throw new IOException("Línea " + numeroLinea + ": formato inválido. Se requieren al menos 5 campos separados por ';'.");
                }

                // 1) Enunciado
                String enunciado = partes[0].trim();
                if (enunciado.isEmpty()) {
                    throw new IOException("Línea " + numeroLinea + ": el enunciado no puede estar vacío.");
                }

                // 2) Opciones: van desde índice 1 hasta (length - 4)
                int cantidadOpciones = partes.length - 4;
                if (cantidadOpciones < 2) {
                    throw new IOException("Línea " + numeroLinea + ": se requieren al menos 2 opciones.");
                }

                String[] opciones = new String[cantidadOpciones];
                for (int i = 0; i < cantidadOpciones; i++) {
                    opciones[i] = partes[1 + i].trim();
                    if (opciones[i].isEmpty()) {
                        throw new IOException("Línea " + numeroLinea + ": la opción en índice " + (i + 1) + " está vacía.");
                    }
                }

                // 3) Respuesta correcta
                String respuestaCorrecta = partes[1 + cantidadOpciones].trim();
                if (respuestaCorrecta.isEmpty()) {
                    throw new IOException("Línea " + numeroLinea + ": la respuesta correcta no puede estar vacía.");
                }
                // Validar que la respuesta correcta exista dentro de las opciones
                boolean existeEnOpciones = false;
                for (String op : opciones) {
                    if (op.equals(respuestaCorrecta)) {
                        existeEnOpciones = true;
                        break;
                    }
                }
                if (!existeEnOpciones) {
                    throw new IOException("Línea " + numeroLinea + ": la respuesta correcta '" + respuestaCorrecta +
                            "' no coincide con ninguna de las opciones.");
                }

                // 4) Tipo ("multiple" o "vf")
                String tipo = partes[2 + cantidadOpciones].trim().toLowerCase();
                if (!(tipo.equals("multiple") || tipo.equals("vf"))) {
                    throw new IOException("Línea " + numeroLinea + ": tipo inválido ('" + tipo +
                            "'). Debe ser 'multiple' o 'vf'.");
                }
                // Si es vf, asegurar que sólo existen dos opciones: "Verdadero" y "Falso" (opcional)
                if (tipo.equals("vf")) {
                    if (cantidadOpciones != 2) {
                        throw new IOException("Línea " + numeroLinea + ": para preguntas de tipo 'vf' deben haber exactamente 2 opciones.");
                    }
                }

                // 5) Nivel Bloom
                String nivelBloom = partes[3 + cantidadOpciones].trim();
                if (nivelBloom.isEmpty()) {
                    throw new IOException("Línea " + numeroLinea + ": el nivel Bloom no puede estar vacío.");
                }
                // (Opcional: podrías validar que nivelBloom sea uno de los seis niveles esperados)

                // 6) Construir y agregar la Pregunta
                Pregunta pregunta = new Pregunta(enunciado, opciones, respuestaCorrecta, tipo, nivelBloom);
                lista.add(pregunta);
            }
        }

        return lista;
    }

}
