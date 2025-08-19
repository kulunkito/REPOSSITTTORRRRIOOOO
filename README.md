# Escaneador de Red en Java

Este es un escaneador de red simple desarrollado en Java con interfaz gráfica (Swing). Permite a los usuarios escanear un rango de direcciones IP para identificar qué equipos están activos en una red.

---

### Características

* **Interfaz gráfica intuitiva**: Construida con Java Swing para una experiencia de usuario sencilla.
* **Validación de datos en tiempo real**: Los campos de entrada (IP, tiempo de espera, reintentos) se validan visualmente para evitar errores.
* **Escaneo en segundo plano**: El escaneo se ejecuta en un hilo separado (`SwingWorker`) para no congelar la interfaz.
* **Control de escaneo**: Permite iniciar y detener el proceso de escaneo en cualquier momento.
* **Resultados detallados**: Muestra la IP, el nombre del equipo, el estado de conexión y el tiempo de respuesta.
* **Funcionalidades de filtrado y ordenamiento**: Los resultados pueden filtrarse y ordenarse directamente desde la tabla.
* **Guardar resultados**: Permite exportar los resultados del escaneo a un archivo de texto.

---

### Requisitos

* Java Development Kit (JDK) 8 o superior.
* El programa utiliza los comandos `ping` y `nslookup` del sistema operativo, por lo que es necesario que estén disponibles en tu entorno (funciona en la mayoría de los sistemas, como Windows, macOS y Linux).

---

### Cómo usar

1.  **Clonar el repositorio** (si usas Git):
    ```bash
    git clone [https://github.com/tu-usuario/EscaneadorDeRed-Java.git](https://github.com/tu-usuario/EscaneadorDeRed-Java.git)
    ```
2.  **Compilar y ejecutar**: Abre el proyecto en tu IDE (como IntelliJ IDEA, Eclipse o VS Code) y ejecuta la clase `EscanerDeRed.java`.

3.  **Uso de la interfaz**:
    * **IP de inicio/fin**: Ingresa el rango de direcciones IP que deseas escanear.
    * **Tiempo de espera**: Define el tiempo máximo (en milisegundos) que el programa esperará una respuesta de cada IP.
    * **Número de reintentos**: El número de veces que se intentará hacer ping a una IP si no responde al primer intento.
    * **Botones**:
        * `Iniciar escaneo`: Comienza el proceso.
        * `Detener escaneo`: Detiene el escaneo en curso.
        * `Limpiar`: Borra todos los resultados de la tabla.
        * `Guardar resultados`: Guarda los datos en un archivo CSV.
        * `Mostrar solo activos/todos`: Filtra la tabla por el estado de las IPs.

---

### Contribuciones

Si encuentras algún problema o tienes sugerencias, no dudes en abrir un *issue* o enviar un *pull request*.

---

### Licencia

Este proyecto está bajo la licencia [elige una licencia, como MIT o Apache].