package escaneamela;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EscanerDeRed extends JFrame {
	private JTextField ipInicioField;
    private JTextField ipFinField;
    private JTextField tiempoEsperaField;
    private JTextArea resultadoArea;

    public EscanerDeRed() {
        setTitle("Escáner de Red");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior: entradas
        JPanel inputPanel = new JPanel(new GridLayout(6, 1, 10, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ipInicioField = new JTextField();
        ipFinField = new JTextField();
        tiempoEsperaField = new JTextField();

        inputPanel.add(new JLabel("IP de inicio:"));
        inputPanel.add(ipInicioField);
        inputPanel.add(new JLabel("IP de fin:"));
        inputPanel.add(ipFinField);
        inputPanel.add(new JLabel("Tiempo de espera (ms):"));
        inputPanel.add(tiempoEsperaField);

        add(inputPanel, BorderLayout.NORTH);

        // Panel central: resultados
        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        add(new JScrollPane(resultadoArea), BorderLayout.CENTER);

        // Panel inferior: botones
        JPanel buttonPanel = new JPanel();

        JButton escanearButton = new JButton("Iniciar escaneo");
        JButton limpiarButton = new JButton("Limpiar pantalla");

        buttonPanel.add(escanearButton);
        buttonPanel.add(limpiarButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Acciones
        escanearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iniciarEscaneo();
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resultadoArea.setText("");
            }
        });

        setVisible(true);
    }

    private void iniciarEscaneo() {
        String ipInicio = ipInicioField.getText().trim();
        String ipFin = ipFinField.getText().trim();
        String espera = tiempoEsperaField.getText().trim();

        if (ipInicio.isEmpty() || ipFin.isEmpty() || espera.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.");
            return;
        }

        resultadoArea.append("Escaneando desde " + ipInicio + " hasta " + ipFin + " con " + espera + "ms de espera...\n");
        // Acá podrías agregar la lógica real de escaneo
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EscanerDeRed());
    }
}