package escaneamela;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de tabla personalizado para mostrar los resultados del escaneo.
 * Extiende AbstractTableModel para funcionar con JTable.
 */
public class TablaResultados extends AbstractTableModel {

    private List<ResultadoEscaneo> datos;
    private final String[] columnas = {"IP", "Nombre equipo", "Activo", "Tiempo (ms)"};

    public TablaResultados(List<ResultadoEscaneo> datos) {
        if (datos == null) {
            this.datos = new ArrayList<>();
        } else {
            this.datos = datos;
        }
    }

    /**
     * Actualiza los datos del modelo y notifica a la tabla para que se redibuje.
     */
    public void setDatos(List<ResultadoEscaneo> nuevosDatos) {
        if (nuevosDatos == null) {
            this.datos = new ArrayList<>();
        } else {
            this.datos = nuevosDatos;
        }
        fireTableDataChanged();
    }

    /**
     * Retorna la lista de resultados actual.
     */
    public List<ResultadoEscaneo> getDatos() {
        return datos;
    }

    @Override
    public int getRowCount() {
        return datos.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }
    
    /**
     * Retorna la clase de cada columna para que la tabla pueda ordenarla correctamente.
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: // IP
            case 1: // Nombre equipo
                return String.class;
            case 2: // Activo
                return Boolean.class;
            case 3: // Tiempo (ms)
                return Long.class;
            default:
                return Object.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ResultadoEscaneo r = datos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return r.getIp();
            case 1:
                return r.getNombreEquipo();
            case 2:
                // Devuelve el booleano para el ordenamiento
                return r.isConectado();
            case 3:
                return r.getTiempoRespuesta();
            default:
                return null;
        }
    }
}