package escaneamela;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase de utilidad para analizar y manipular los resultados del escaneo.
 */
public class AnalizadorResultados {

    /**
     * Cuenta la cantidad de equipos activos en la lista de resultados.
     */
    public static long contarActivos(List<ResultadoEscaneo> lista) {
        return lista.stream().filter(ResultadoEscaneo::isConectado).count();
    }

    /**
     * Filtra la lista de resultados para mostrar solo los equipos activos o inactivos.
     */
    public static List<ResultadoEscaneo> filtrarPorEstado(List<ResultadoEscaneo> lista, boolean activos) {
        return lista.stream()
                .filter(r -> r.isConectado() == activos)
                .collect(Collectors.toList());
    }

    /**
     * Ordena la lista de resultados por tiempo de respuesta.
     */
    public static List<ResultadoEscaneo> ordenarPorTiempo(List<ResultadoEscaneo> lista, boolean asc) {
        return lista.stream()
                .sorted((a, b) -> asc ?
                        Long.compare(a.getTiempoRespuesta(), b.getTiempoRespuesta()) :
                        Long.compare(b.getTiempoRespuesta(), a.getTiempoRespuesta()))
                .collect(Collectors.toList());
    }
}