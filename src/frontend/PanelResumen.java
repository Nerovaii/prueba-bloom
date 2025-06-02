package frontend;

import backend.GestorDePrueba;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class PanelResumen extends JPanel {
    private GestorDePrueba gestor;

    public PanelResumen(GestorDePrueba gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("Resumen de Resultados", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel para mostrar porcentajes
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new GridLayout(0, 1, 5, 5));

        Map<String, Integer> porNivel = gestor.calcularPorcentajePorNivelBloom();
        Map<String, Integer> porTipo  = gestor.calcularPorcentajePorTipo();

        panelContenido.add(new JLabel("Porcentaje de aciertos por nivel Bloom:", SwingConstants.CENTER));
        for (String nivel : porNivel.keySet()) {
            panelContenido.add(new JLabel(nivel + ": " + porNivel.get(nivel) + "%", SwingConstants.CENTER));
        }

        panelContenido.add(Box.createVerticalStrut(10)); // Espacio

        panelContenido.add(new JLabel("Porcentaje de aciertos por tipo de pregunta:", SwingConstants.CENTER));
        for (String tipo : porTipo.keySet()) {
            panelContenido.add(new JLabel(tipo + ": " + porTipo.get(tipo) + "%", SwingConstants.CENTER));
        }

        add(panelContenido, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnRevisar = new JButton("Revisar preguntas");
        panelBotones.add(btnRevisar);
        add(panelBotones, BorderLayout.SOUTH);

        btnRevisar.addActionListener(e -> {
            // Reiniciar índice para que la revisión comience desde la primera pregunta
            gestor.reiniciar();
            PanelPregunta panelRevision = new PanelPregunta(gestor, true);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(panelRevision);
            frame.revalidate();
            frame.pack();
        });
    }
}
