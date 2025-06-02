# PruebaBloom

Aplicación Java Swing para administrar pruebas basadas en la Taxonomía de Bloom. Permite cargar un archivo de preguntas, responderlas en modo normal y luego revisar resultados detallados (por nivel Bloom y por tipo de pregunta).

---

## Descripción del proyecto

- **Backend** (`backend/`):  
  - **Pregunta.java**: modelo que representa cada pregunta.  
  - **ParserArchivo.java**: lee un archivo de texto (`.txt`) y convierte cada línea en un objeto `Pregunta`.  
  - **GestorDePrueba.java**: controla la navegación entre preguntas, el registro de respuestas y el cálculo de porcentajes (por nivel Bloom y por tipo).

- **Frontend** (`frontend/`):  
  - **VentanaPrincipal.java**: ventana inicial para seleccionar el archivo de preguntas e iniciar la prueba.  
  - **PanelPregunta.java**: panel que muestra cada pregunta, opciones, botones “Atrás/Siguiente” o “Enviar/Fin Revisión”.  
  - **PanelResumen.java**: panel que muestra los porcentajes de aciertos y permite entrar en modo revisión (resaltando aciertos y errores).

- **Main.java**: punto de entrada que lanza `VentanaPrincipal` sobre el hilo de Swing.

---

## Cómo compilar

1. **Requisitos**  
   - Java JDK 11 o superior (asegúrate de que `javac` y `java` estén en el PATH).

2. **Abrir terminal** en la carpeta raíz del proyecto (donde están los subdirectorios `backend/`, `frontend/` y `Main.java`).

3. **Compilar todo**:
   ```bash
   javac backend/*.java frontend/*.java Main.java
   java Main
