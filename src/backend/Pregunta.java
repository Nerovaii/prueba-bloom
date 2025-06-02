package backend;

import java.util.Arrays;

public class Pregunta {
    private String enunciado;
    private String[] opciones;
    private String respuestaCorrecta;
    private String tipo;       // "multiple" o "vf"
    private String nivelBloom; // "Recordar", "Entender", "Aplicar", "Analizar", "Evaluar", "Crear"

    // Se ordena la pregunta

    public Pregunta(String enunciado, String[] opciones, String respuestaCorrecta, String tipo, String nivelBloom) {
        this.enunciado = enunciado;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
        this.tipo = tipo.toLowerCase();
        this.nivelBloom = nivelBloom;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String[] getOpciones() {
        return opciones;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNivelBloom() {
        return nivelBloom;
    }

    @Override
    public String toString() {
        return "Pregunta{" +
                "enunciado='" + enunciado + '\'' +
                ", opciones=" + Arrays.toString(opciones) +
                ", respuestaCorrecta='" + respuestaCorrecta + '\'' +
                ", tipo='" + tipo + '\'' +
                ", nivelBloom='" + nivelBloom + '\'' +
                '}';
    }
}
