package co.edu.poli.examen2.aplicacion.puerto.entrada;

import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;

/**
 * Puerto de entrada para cargar examenes desde un archivo.
 *
 * @author Juan Rosero
 */
public interface DeserializarExamenesUseCase {

    /**
     * Carga los examenes de un archivo y reemplaza los que estan en memoria.
     *
     * @param ruta ruta del archivo origen
     * @param formato formato en que esta escrito el archivo
     * @return cantidad de examenes cargados
     */
    int deserializar(String ruta, FormatoArchivo formato);
}
