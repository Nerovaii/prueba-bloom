package frontend;

import backend.GestorDePrueba;
import backend.Pregunta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PanelPregunta extends JPanel {
    private GestorDePrueba gestor;
    private boolean modoRevision;

    private JLabel lblEnunciado;
    private JPanel panelOpciones;
    private JButton btnAtras;
    private JButton btnSiguiente;
    private ButtonGroup grupoOpciones;
    private JLabel lblError;

    public PanelPregunta(GestorDePrueba gestor, boolean modoRevision) {
        this.gestor = gestor;
        this.modoRevision = modoRevision;

        setLayout(new BorderLayout(10, 10));
        lblEnunciado = new JLabel("", SwingConstants.CENTER);
        lblEnunciado.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblEnunciado, BorderLayout.NORTH);

        panelOpciones = new JPanel();
        panelOpciones.setLayout(new GridLayout(0, 1, 5, 5));
        add(panelOpciones, BorderLayout.CENTER);

        lblError = new JLabel(" ", SwingConstants.CENTER);
        lblError.setForeground(Color.RED);
        add(lblError, BorderLayout.EAST);

        JPanel panelSur = new JPanel(new FlowLayout());
        btnAtras = new JButton("Atrás");
        btnSiguiente = new JButton("Siguiente");
        panelSur.add(btnAtras);
        panelSur.add(btnSiguiente);
        add(panelSur, BorderLayout.SOUTH);

        // En modo revisión, siempre partimos desde la primera pregunta
        if (modoRevision) {
            gestor.reiniciar();
        }

        btnAtras.addActionListener((ActionEvent e) -> {
            if (!gestor.estaPrimeraPregunta()) {
                gestor.retroceder();
                lblError.setText(" ");
                cargarVista();
            }
        });

        btnSiguiente.addActionListener((ActionEvent e) -> {
            if (!modoRevision) {
                // En modo normal, validamos que haya opción seleccionada
                String seleccionada = getOpcionSeleccionada();
                if (seleccionada == null) {
                    lblError.setText("Debes seleccionar una opción.");
                    return;
                }
                // Registrar la respuesta
                gestor.registrarRespuesta(gestor.getPreguntaActualIndex(), seleccionada);
            }

            // Si no es última pregunta, avanzar
            if (!gestor.estaUltimaPregunta()) {
                gestor.avanzar();
                lblError.setText(" ");
                cargarVista();
            } else {
                // Estamos en la última pregunta
                if (!modoRevision) {
                    // Modo normal: enviamos y vamos a resumen
                    PanelResumen pr = new PanelResumen(gestor);
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    frame.setContentPane(pr);
                    frame.revalidate();
                    frame.pack();
                } else {
                    // Modo revisión: al presionar "Fin Revisión", volvemos a VentanaPrincipal
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    frame.dispose();
                    VentanaPrincipal nuevaVentana = new VentanaPrincipal();
                    nuevaVentana.setVisible(true);
                }
            }
        });

        // Cargar la primera vista
        cargarVista();
    }

    /** Obtiene la opción seleccionada (texto) o null si ninguna está marcada */
    private String getOpcionSeleccionada() {
        for (Component comp : panelOpciones.getComponents()) {
            if (comp instanceof JRadioButton) {
                JRadioButton radio = (JRadioButton) comp;
                if (radio.isSelected()) {
                    return radio.getText();
                }
            }
        }
        return null;
    }

    /** Carga la vista con la pregunta actual */
    private void cargarVista() {
        Pregunta p = gestor.getPreguntaActual();
        lblEnunciado.setText("<html><body style='width: 400px;'>" + p.getEnunciado() + "</body></html>");

        panelOpciones.removeAll();
        grupoOpciones = new ButtonGroup();

        String[] opciones = p.getOpciones();
        for (String opcion : opciones) {
            JRadioButton radio = new JRadioButton(opcion);
            radio.setFont(new Font("Arial", Font.PLAIN, 14));
            grupoOpciones.add(radio);
            panelOpciones.add(radio);
        }

        // Si ya hay respuesta guardada y no estamos en modo revisión, seleccionarla
        if (!modoRevision && gestor.tieneRespuestaPara(gestor.getPreguntaActualIndex())) {
            String respGuardada = gestor.getRespuestaPara(gestor.getPreguntaActualIndex());
            for (Component comp : panelOpciones.getComponents()) {
                JRadioButton radio = (JRadioButton) comp;
                if (radio.getText().equals(respGuardada)) {
                    radio.setSelected(true);
                    break;
                }
            }
        }

        // Si estamos en modo revisión, colorear opciones y ajustar botones
        if (modoRevision) {
            String correcta = p.getRespuestaCorrecta();
            String elegida = gestor.getRespuestaPara(gestor.getPreguntaActualIndex());
            for (Component comp : panelOpciones.getComponents()) {
                JRadioButton radio = (JRadioButton) comp;
                radio.setEnabled(false);
                if (radio.getText().equals(correcta)) {
                    radio.setForeground(Color.GREEN.darker());
                } else if (elegida != null && radio.getText().equals(elegida) && !elegida.equals(correcta)) {
                    radio.setForeground(Color.RED);
                }
            }
            btnAtras.setEnabled(!gestor.estaPrimeraPregunta());
            btnSiguiente.setText(gestor.estaUltimaPregunta() ? "Fin Revisión" : "Siguiente");
        } else {
            // En modo normal
            btnAtras.setEnabled(!gestor.estaPrimeraPregunta());
            btnSiguiente.setText(gestor.estaUltimaPregunta() ? "Enviar" : "Siguiente");
        }

        panelOpciones.revalidate();
        panelOpciones.repaint();
    }
}
