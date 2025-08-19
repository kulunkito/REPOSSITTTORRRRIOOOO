package escaneamela;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EscanerDeRed extends JFrame {
    private JTextField ipInicioField;
    private JTextField ipFinField;
    private JTextField tiempoEsperaField;
    private JTextField reintentosField;
    private JProgressBar barraProgreso;
    private JTable tabla;
    private TablaResultados modeloTabla;
    private JLabel lblActivos;
    private JButton escanearButton;
    private JButton detenerButton;

    private List<ResultadoEscaneo> resultados;
    private Escaneador escaneador;

    public EscanerDeRed() {
        setTitle("Escáner de Red");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        resultados = new ArrayList<>();
        
        // --- Configuración de la estética (colores pasteles) ---
        Color colorFondo = new Color(240, 248, 255); // Azul pálido
        Color colorPanel = new Color(224, 255, 255); // Celeste
        Color colorBorde = new Color(100, 149, 237); // Azul aciano
        
        getContentPane().setBackground(colorFondo);

        // Panel de entrada para la configuración
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(colorPanel);
        
        // Campos de texto y validación visual
        ipInicioField = new JTextField();
        ipFinField = new JTextField();
        tiempoEsperaField = new JTextField("1000"); 
        reintentosField = new JTextField("1"); 
        
        ipInicioField.setBorder(BorderFactory.createLineBorder(colorBorde));
        ipFinField.setBorder(BorderFactory.createLineBorder(colorBorde));
        tiempoEsperaField.setBorder(BorderFactory.createLineBorder(colorBorde));
        reintentosField.setBorder(BorderFactory.createLineBorder(colorBorde));

        // Listener para la validación visual en tiempo real de todos los campos
        DocumentListener documentListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarCampos(); }
            public void removeUpdate(DocumentEvent e) { validarCampos(); }
            public void changedUpdate(DocumentEvent e) { validarCampos(); }
        };

        ipInicioField.getDocument().addDocumentListener(documentListener);
        ipFinField.getDocument().addDocumentListener(documentListener);
        tiempoEsperaField.getDocument().addDocumentListener(documentListener);
        reintentosField.getDocument().addDocumentListener(documentListener);

        inputPanel.add(new JLabel("IP de inicio:"));
        inputPanel.add(ipInicioField);
        inputPanel.add(new JLabel("IP de fin:"));
        inputPanel.add(ipFinField);
        inputPanel.add(new JLabel("Tiempo de espera (ms):"));
        inputPanel.add(tiempoEsperaField);
        inputPanel.add(new JLabel("Número de reintentos:"));
        inputPanel.add(reintentosField);

        add(inputPanel, BorderLayout.NORTH);

        // Tabla de resultados
        modeloTabla = new TablaResultados(resultados);
        tabla = new JTable(modeloTabla);
        tabla.setAutoCreateRowSorter(true); // Habilita el ordenamiento de la tabla
        tabla.setBackground(new Color(255, 250, 240)); 
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel para la barra de progreso y el contador
        JPanel progresoPanel = new JPanel(new BorderLayout());
        progresoPanel.setBackground(colorPanel);
        barraProgreso = new JProgressBar();
        barraProgreso.setStringPainted(true);
        lblActivos = new JLabel("Equipos activos: 0");
        progresoPanel.add(barraProgreso, BorderLayout.CENTER);
        progresoPanel.add(lblActivos, BorderLayout.SOUTH);

        // Panel para los botones de acción
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(colorPanel);
        escanearButton = new JButton("Iniciar escaneo");
        detenerButton = new JButton("Detener escaneo");
        JButton limpiarButton = new JButton("Limpiar");
        JButton guardarButton = new JButton("Guardar resultados");
        JButton filtrarActivosButton = new JButton("Mostrar solo activos");
        JButton mostrarTodosButton = new JButton("Mostrar todos");
        
        detenerButton.setEnabled(false); // El botón de detener está deshabilitado al inicio

        buttonPanel.add(escanearButton);
        buttonPanel.add(detenerButton);
        buttonPanel.add(limpiarButton);
        buttonPanel.add(guardarButton);
        buttonPanel.add(filtrarActivosButton);
        buttonPanel.add(mostrarTodosButton);

        JPanel southPanel = new JPanel(new GridLayout(2, 1));
        southPanel.add(progresoPanel);
        southPanel.add(buttonPanel);
        southPanel.setBackground(colorPanel);

        add(southPanel, BorderLayout.SOUTH);

        // Manejo de eventos de los botones
        escanearButton.addActionListener(e -> iniciarEscaneo());
        detenerButton.addActionListener(e -> detenerEscaneo());
        limpiarButton.addActionListener(e -> limpiarResultados());
        guardarButton.addActionListener(e -> guardarResultados());
        filtrarActivosButton.addActionListener(e -> {
            modeloTabla.setDatos(AnalizadorResultados.filtrarPorEstado(resultados, true));
            actualizarContador();
        });
        mostrarTodosButton.addActionListener(e -> {
            modeloTabla.setDatos(resultados);
            actualizarContador();
        });

        setVisible(true);
    }

    /**
     * Valida visualmente todos los campos de entrada y cambia su color si no son válidos.
     */
    private void validarCampos() {
        boolean ipInicioValida = ValidadorIP.esIPValida(ipInicioField.getText());
        boolean ipFinValida = ValidadorIP.esIPValida(ipFinField.getText());

        ipInicioField.setBackground(ipInicioValida ? Color.WHITE : new Color(255, 200, 200));
        ipFinField.setBackground(ipFinValida ? Color.WHITE : new Color(255, 200, 200));
        
        boolean tiempoEsperaValido = false;
        try {
            int tiempo = Integer.parseInt(tiempoEsperaField.getText());
            if (tiempo > 0) {
                tiempoEsperaValido = true;
            }
        } catch (NumberFormatException ex) {}
        tiempoEsperaField.setBackground(tiempoEsperaValido ? Color.WHITE : new Color(255, 200, 200));

        boolean reintentosValidos = false;
        try {
            int reintentos = Integer.parseInt(reintentosField.getText());
            if (reintentos > 0) {
                reintentosValidos = true;
            }
        } catch (NumberFormatException ex) {}
        reintentosField.setBackground(reintentosValidos ? Color.WHITE : new Color(255, 200, 200));
    }

    /**
     * Inicia el proceso de escaneo en un hilo de trabajo.
     * Realiza las validaciones finales antes de comenzar.
     */
    private void iniciarEscaneo() {
        String ipInicio = ipInicioField.getText().trim();
        String ipFin = ipFinField.getText().trim();
        int espera, reintentos;

        // Validaciones numéricas y de formato de IP
        try {
            espera = Integer.parseInt(tiempoEsperaField.getText().trim());
            reintentos = Integer.parseInt(reintentosField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El tiempo de espera y los reintentos deben ser números.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (espera <= 0 || reintentos <= 0) {
            JOptionPane.showMessageDialog(this, "El tiempo de espera y el número de reintentos deben ser mayores a cero.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ValidadorIP.esIPValida(ipInicio) || !ValidadorIP.esIPValida(ipFin)) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce direcciones IP válidas.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        long ipInicioLong = ipToLong(ipInicio);
        long ipFinLong = ipToLong(ipFin);

        if (ipInicioLong > ipFinLong) {
            JOptionPane.showMessageDialog(this, "La IP de inicio debe ser menor o igual a la IP de fin.", "Error de rango", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Prepara la UI para el escaneo
        escanearButton.setEnabled(false);
        detenerButton.setEnabled(true);
        resultados.clear();
        modeloTabla.setDatos(resultados);
        actualizarContador();

        // Crea e inicia el SwingWorker
        escaneador = new Escaneador(ipInicio, ipFin, espera, reintentos, barraProgreso, modeloTabla, resultados);
        
        escaneador.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                barraProgreso.setValue((Integer) evt.getNewValue());
            }
        });
        
        escaneador.execute();
    }

    /**
     * Detiene el escaneo en curso si existe.
     */
    private void detenerEscaneo() {
        if (escaneador != null && !escaneador.isDone()) {
            escaneador.cancel(true);
            barraProgreso.setString("Escaneo detenido");
            escanearButton.setEnabled(true);
            detenerButton.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Escaneo detenido por el usuario.", "Escaneo detenido", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Convierte una dirección IP de formato String a un número long.
     */
    private long ipToLong(String ipAddress) {
        String[] ipAddressInArray = ipAddress.split("\\.");
        long result = 0;
        for (int i = 0; i < ipAddressInArray.length; i++) {
            int power = 3 - i;
            int ip = Integer.parseInt(ipAddressInArray[i]);
            result += ip * Math.pow(256, power);
        }
        return result;
    }

    /**
     * Convierte un número long a una dirección IP de formato String.
     */
    private String longToIp(long ipLong) {
        return ((ipLong >> 24) & 0xFF) + "." +
               ((ipLong >> 16) & 0xFF) + "." +
               ((ipLong >> 8) & 0xFF) + "." +
               (ipLong & 0xFF);
    }

    /**
     * Limpia la tabla y los resultados.
     */
    private void limpiarResultados() {
        resultados.clear();
        modeloTabla.setDatos(resultados);
        actualizarContador();
        barraProgreso.setValue(0);
        barraProgreso.setString("");
    }

    /**
     * Guarda los resultados del escaneo en un archivo CSV.
     */
    private void guardarResultados() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fw = new FileWriter(fileChooser.getSelectedFile())) {
                fw.write("IP,Nombre equipo,Estado,Tiempo de respuesta\n");
                for (ResultadoEscaneo r : resultados) {
                    fw.write(r.getIp() + "," + r.getNombreEquipo() + "," + r.getEstado() + "," + r.getTiempoRespuesta() + "ms\n");
                }
                JOptionPane.showMessageDialog(this, "Resultados guardados correctamente en: " + fileChooser.getSelectedFile().getAbsolutePath(), "Guardado exitoso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo. Asegúrate de tener permisos de escritura.", "Error de Guardado", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Actualiza la etiqueta que muestra la cantidad de equipos activos.
     */
    private void actualizarContador() {
        long activos = AnalizadorResultados.contarActivos(modeloTabla.getDatos());
        lblActivos.setText("Equipos activos: " + activos);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EscanerDeRed::new);
    }
}