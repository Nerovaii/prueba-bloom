import javax.swing.SwingUtilities;
import frontend.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.pack();
            ventana.setVisible(true);
        });
    }
}
