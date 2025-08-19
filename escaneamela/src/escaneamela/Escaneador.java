package escaneamela;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que extiende SwingWorker para realizar el escaneo de IP en un hilo separado.
 * Gestiona el progreso y la cancelación de la tarea.
 */
public class Escaneador extends SwingWorker<List<ResultadoEscaneo>, ResultadoEscaneo> {

    private final long ipInicioLong;
    private final long ipFinLong;
    private final int espera;
    private final int reintentos;
    private final JProgressBar barraProgreso;
    private final TablaResultados modeloTabla;
    private final List<ResultadoEscaneo> resultados;

    public Escaneador(String ipInicio, String ipFin, int espera, int reintentos, JProgressBar barraProgreso, TablaResultados modeloTabla, List<ResultadoEscaneo> resultados) {
        this.ipInicioLong = ipToLong(ipInicio);
        this.ipFinLong = ipToLong(ipFin);
        this.espera = espera;
        this.reintentos = reintentos;
        this.barraProgreso = barraProgreso;
        this.modeloTabla = modeloTabla;
        this.resultados = resultados;
    }

    @Override
    protected List<ResultadoEscaneo> doInBackground() throws Exception {
        barraProgreso.setMaximum((int)(ipFinLong - ipInicioLong + 1));
        
        long ipActualLong = ipInicioLong;
        while(ipActualLong <= ipFinLong){
            if (isCancelled()) {
                // Si la tarea se cancela, se detiene el bucle
                return null;
            }

            String ip = longToIp(ipActualLong);
            ResultadoEscaneo r = Escaneo.escanearIP(ip, espera, reintentos);
            
            // Publica el resultado para que la interfaz lo procese
            publish(r);

            ipActualLong++;
            int progress = (int)(ipActualLong - ipInicioLong);
            setProgress(progress);
        }
        return resultados;
    }

    /**
     * Se ejecuta en el hilo de la GUI para procesar los resultados intermedios.
     */
    @Override
    protected void process(List<ResultadoEscaneo> chunks) {
        // SOLUCIÓN: Verifica si la lista de "chunks" no es nula antes de procesarla
        if (chunks != null) {
            for (ResultadoEscaneo resultado : chunks) {
                // SOLUCIÓN 2 (OPCIONAL): Verifica que el elemento no sea nulo también
                if (resultado != null) {
                     resultados.add(resultado);
                }
            }
            modeloTabla.setDatos(resultados);
        }
    }

    /**
     * Se ejecuta cuando la tarea ha finalizado (correctamente o por cancelación).
     */
    @Override
    protected void done() {
        try {
            get(); // Espera a que termine la tarea para obtener el resultado
            barraProgreso.setString("Escaneo finalizado");
        } catch (java.util.concurrent.CancellationException e) {
            // El escaneo fue cancelado, el mensaje ya se mostró en el frame principal
        } catch (Exception e) {
            barraProgreso.setString("Error durante el escaneo");
            e.printStackTrace();
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
}