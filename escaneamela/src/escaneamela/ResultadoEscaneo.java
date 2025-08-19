package escaneamela;

/**
 * Clase de modelo para almacenar los datos de cada IP escaneada.
 */
public class ResultadoEscaneo {
    private String ip;
    private String nombreEquipo;
    private boolean conectado;
    private long tiempoRespuesta; // en ms

    public ResultadoEscaneo(String ip, String nombreEquipo, boolean conectado, long tiempoRespuesta) {
        this.ip = ip;
        this.nombreEquipo = nombreEquipo;
        this.conectado = conectado;
        this.tiempoRespuesta = tiempoRespuesta;
    }

    // Getters para acceder a las propiedades
    public String getIp() { return ip; }
    public String getNombreEquipo() { return nombreEquipo; }
    public boolean isConectado() { return conectado; }
    public long getTiempoRespuesta() { return tiempoRespuesta; }

    /**
     * Retorna el estado como una cadena de texto para exportar a archivos.
     */
    public String getEstado() {
        return conectado ? "Activo" : "Inactivo";
    }
}