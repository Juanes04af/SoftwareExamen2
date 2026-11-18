package co.edu.poli.examen2.aplicacion.servicio;

import co.edu.poli.examen2.dominio.excepcion.ExamenValidacionException;
import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;

/**
 * Validaciones comunes de los casos de uso que trabajan con archivos.
 *
 * @author Juan Rosero
 */
final class ValidadorArchivo {

    private ValidadorArchivo() {
    }

    /**
     * Verifica que la ruta y el formato esten presentes.
     *
     * @param ruta ruta del archivo
     * @param formato formato del archivo
     * @throws ExamenValidacionException si falta alguno de los dos
     */
    static void validar(String ruta, FormatoArchivo formato) {
        if (ruta == null || ruta.isBlank()) {
            throw new ExamenValidacionException("Debe indicar la ruta del archivo.");
        }
        if (formato == null) {
            throw new ExamenValidacionException("Debe seleccionar el formato del archivo.");
        }
    }
}
