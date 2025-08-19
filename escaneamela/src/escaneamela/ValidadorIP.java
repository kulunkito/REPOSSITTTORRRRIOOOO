package escaneamela;

/**
 * Clase de utilidad para validar el formato de una dirección IP.
 */
public class ValidadorIP {
    public static boolean esIPValida(String ip) {
        return ip.matches("^((25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\.){3}(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)$");
    }    
}