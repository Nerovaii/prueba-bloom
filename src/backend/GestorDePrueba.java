package backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorDePrueba {
    private List<Pregunta> preguntas;
    private int preguntaActual; // Índice de la pregunta actual en la lista
    private Map<Integer, String> respuestasUsuario; // Key = índice de pregunta; Value = respuesta elegida

    public GestorDePrueba(List<Pregunta> preguntas) {
        if (preguntas == null || preguntas.isEmpty()) {
            throw new IllegalArgumentException("La lista de preguntas no puede ser nula o vacía.");
        }
        this.preguntas = preguntas;
        this.preguntaActual = 0;
        this.respuestasUsuario = new HashMap<>();
    }

    /** Devuelve la pregunta que corresponde al índice actual */
    public Pregunta getPreguntaActual() {
        return preguntas.get(preguntaActual);
    }

    /** Devuelve el índice de la pregunta actual */
    public int getPreguntaActualIndex() {
        return preguntaActual;
    }

    /** Devuelve true si actualmente estamos en la primera pregunta */
    public boolean estaPrimeraPregunta() {
        return preguntaActual == 0;
    }

    /** Devuelve true si actualmente estamos en la última pregunta */
    public boolean estaUltimaPregunta() {
        return preguntaActual == preguntas.size() - 1;
    }

    /** Avanza al siguiente ítem, si no estamos en la última pregunta */
    public void avanzar() {
        if (preguntaActual < preguntas.size() - 1) {
            preguntaActual++;
        }
    }

    /** Retrocede al ítem anterior, si no estamos en la primera pregunta */
    public void retroceder() {
        if (preguntaActual > 0) {
            preguntaActual--;
        }
    }

    /** Registra la respuesta del usuario para una pregunta dada */
    public void registrarRespuesta(int indicePregunta, String respuestaElegida) {
        if (indicePregunta < 0 || indicePregunta >= preguntas.size()) {
            throw new IndexOutOfBoundsException("Índice de pregunta inválido: " + indicePregunta);
        }
        respuestasUsuario.put(indicePregunta, respuestaElegida);
    }

    /** Retorna true si el usuario ya respondió la pregunta de índice dado */
    public boolean tieneRespuestaPara(int indicePregunta) {
        return respuestasUsuario.containsKey(indicePregunta);
    }

    /** Devuelve la respuesta que el usuario dio para la pregunta (o null si no existe) */
    public String getRespuestaPara(int indicePregunta) {
        return respuestasUsuario.get(indicePregunta);
    }

    /** Devuelve el número total de preguntas */
    public int totalPreguntas() {
        return preguntas.size();
    }

    /** Resetea el índice de pregunta para que apunte a la primera */
    public void reiniciar() {
        this.preguntaActual = 0;
    }

    /**
     * Calcula el porcentaje de respuestas correctas por nivel Bloom.
     * @return Mapa donde la clave es el nivel Bloom y el valor es el porcentaje (0–100)
     */
    public Map<String, Integer> calcularPorcentajePorNivelBloom() {
        Map<String, Integer> aciertosPorNivel = new HashMap<>();
        Map<String, Integer> totalPorNivel   = new HashMap<>();

        // 1) Contabilizar totales por nivel
        for (Pregunta p : preguntas) {
            String nivel = p.getNivelBloom();
            totalPorNivel.put(nivel, totalPorNivel.getOrDefault(nivel, 0) + 1);
            aciertosPorNivel.putIfAbsent(nivel, 0);
        }

        // 2) Contar aciertos
        for (int i = 0; i < preguntas.size(); i++) {
            Pregunta p = preguntas.get(i);
            String nivel = p.getNivelBloom();

            if (respuestasUsuario.containsKey(i)) {
                String respUser = respuestasUsuario.get(i);
                if (respUser.equals(p.getRespuestaCorrecta())) {
                    aciertosPorNivel.put(nivel, aciertosPorNivel.get(nivel) + 1);
                }
            }
        }

        // 3) Convertir a porcentaje
        Map<String, Integer> porcentajePorNivel = new HashMap<>();
        for (String nivel : totalPorNivel.keySet()) {
            int tot  = totalPorNivel.get(nivel);
            int aci  = aciertosPorNivel.getOrDefault(nivel, 0);
            int porc = (int) Math.round((aci * 100.0) / tot);
            porcentajePorNivel.put(nivel, porc);
        }

        return porcentajePorNivel;
    }

    /**
     * Calcula el porcentaje de respuestas correctas por tipo de pregunta ("multiple" o "vf").
     * @return Mapa donde la clave es el tipo y el valor es el porcentaje (0–100)
     */
    public Map<String, Integer> calcularPorcentajePorTipo() {
        Map<String, Integer> aciertosPorTipo = new HashMap<>();
        Map<String, Integer> totalPorTipo   = new HashMap<>();

        // 1) Contabilizar totales por tipo
        for (Pregunta p : preguntas) {
            String tipo = p.getTipo();
            totalPorTipo.put(tipo, totalPorTipo.getOrDefault(tipo, 0) + 1);
            aciertosPorTipo.putIfAbsent(tipo, 0);
        }

        // 2) Contar aciertos
        for (int i = 0; i < preguntas.size(); i++) {
            Pregunta p = preguntas.get(i);
            String tipo = p.getTipo();

            if (respuestasUsuario.containsKey(i)) {
                String respUser = respuestasUsuario.get(i);
                if (respUser.equals(p.getRespuestaCorrecta())) {
                    aciertosPorTipo.put(tipo, aciertosPorTipo.get(tipo) + 1);
                }
            }
        }

        // 3) Convertir a porcentaje
        Map<String, Integer> porcentajePorTipo = new HashMap<>();
        for (String tipo : totalPorTipo.keySet()) {
            int tot  = totalPorTipo.get(tipo);
            int aci  = aciertosPorTipo.getOrDefault(tipo, 0);
            int porc = (int) Math.round((aci * 100.0) / tot);
            porcentajePorTipo.put(tipo, porc);
        }

        return porcentajePorTipo;
    }

}
