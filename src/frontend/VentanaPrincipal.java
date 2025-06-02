package frontend;

import backend.GestorDePrueba;
import backend.ParserArchivo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private GestorDePrueba gestor;
    private JLabel lblInfoPrueba;
    private JButton btnCargar;
    private JButton btnIniciar;

    public VentanaPrincipal() {
        super("Administrador de Pruebas - Taxonomía de Bloom");
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);

        // Layout vertical
        setLayout(new BorderLayout());
        lblInfoPrueba = new JLabel("No hay prueba cargada", SwingConstants.CENTER);
        lblInfoPrueba.setFont(new Font("Arial", Font.PLAIN, 16));
        add(lblInfoPrueba, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());
        btnCargar = new JButton("Cargar archivo de preguntas");
        btnIniciar = new JButton("Iniciar prueba");
        btnIniciar.setEnabled(false);
        panelBotones.add(btnCargar);
        panelBotones.add(btnIniciar);
        add(panelBotones, BorderLayout.SOUTH);

        // Acción de btnCargar
        btnCargar.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            int seleccion = fileChooser.showOpenDialog(this);
            if (seleccion == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();
                try {
                    List<backend.Pregunta> lista = ParserArchivo.cargar(archivo.getAbsolutePath());
                    gestor = new GestorDePrueba(lista);
                    lblInfoPrueba.setText(lista.size() + " preguntas cargadas");
                    btnIniciar.setEnabled(true);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error al cargar archivo:\n" + ex.getMessage(),
                            "Error de carga",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción de btnIniciar
        btnIniciar.addActionListener((ActionEvent e) -> {
            PanelPregunta panel = new PanelPregunta(gestor, false);
            setContentPane(panel);
            revalidate();
            pack();
        });
    }
}
