package co.edu.poli.examen2.aplicacion.puerto.entrada;

import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;

/**
 * Puerto de entrada para guardar los examenes en un archivo.
 *
 * @author Juan Rosero
 */
public interface SerializarExamenesUseCase {

    /**
     * Guarda todos los examenes registrados en un archivo.
     *
     * @param ruta ruta del archivo destino
     * @param formato formato en que se escribira el archivo
     * @return cantidad de examenes guardados
     */
    int serializar(String ruta, FormatoArchivo formato);
}
