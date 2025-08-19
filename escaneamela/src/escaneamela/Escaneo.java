package escaneamela;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Clase estática que contiene la lógica para escanear una sola IP.
 * Utiliza comandos del sistema para realizar el ping y la búsqueda de nombre.
 */
public class Escaneo {

    /**
     * Escanea una dirección IP para determinar si está activa.
     */
    public static ResultadoEscaneo escanearIP(String ip, int espera, int reintentos) {
        long inicioTiempo = System.currentTimeMillis();
        boolean activo = false;

        for (int i = 0; i < reintentos; i++) {
            try {
                // Comando de ping adaptable a diferentes sistemas operativos
                ProcessBuilder pb;
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    pb = new ProcessBuilder("ping", "-n", "1", "-w", String.valueOf(espera), ip);
                } else {
                    pb = new ProcessBuilder("ping", "-c", "1", "-W", String.valueOf(espera / 1000), ip);
                }
                
                Process p = pb.start();
                int code = p.waitFor();

                if (code == 0) {
                    activo = true;
                    break; // No seguir probando si ya respondió
                }
            } catch (Exception e) {
                // Si ocurre una excepción, se asume que la IP no responde
                activo = false;
            }
        }

        long tiempoRespuesta = System.currentTimeMillis() - inicioTiempo;
        String nombre = activo ? obtenerNombreHost(ip) : "No responde";

        // No generamos puerto con nextInt(0), lo dejamos fijo en -1 si no hay.
        return new ResultadoEscaneo(ip, nombre, activo, tiempoRespuesta);
    }

    /**
     * Intenta obtener el nombre de host de una dirección IP.
     */
    public static String obtenerNombreHost(String ip) {
        try {
            ProcessBuilder pb = new ProcessBuilder("nslookup", ip);
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("name =")) {
                    return line.split("name =")[1].trim();
                }
            }
        } catch (Exception e) {
            // Si hay un error, el nombre de host no se pudo resolver
            return "Desconocido";
        }
        return "Desconocido";
    }
}