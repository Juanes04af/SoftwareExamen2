package co.edu.poli.examen2.aplicacion.puerto.salida;

import java.util.List;

import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;

/**
 * Puerto de salida para escribir y leer examenes en archivos.
 *
 * <p>La aplicacion pide "guardar en archivo" o "leer de archivo"
 * sin conocer como se escribe cada formato.</p>
 *
 * @author Juan Rosero
 */
public interface ArchivoExamenesPort {

    /**
     * Escribe los examenes en un archivo.
     *
     * @param examenes examenes a escribir
     * @param ruta ruta del archivo destino
     * @param formato formato del archivo
     */
    void escribir(List<ExamenSangre> examenes, String ruta, FormatoArchivo formato);

    /**
     * Lee los examenes de un archivo.
     *
     * @param ruta ruta del archivo origen
     * @param formato formato del archivo
     * @return examenes leidos
     */
    List<ExamenSangre> leer(String ruta, FormatoArchivo formato);
}
