package co.edu.poli.examen2.aplicacion.puerto.entrada;

import java.util.List;

import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

/**
 * Puerto de entrada para consultar los examenes registrados.
 *
 * @author Juan Rosero
 */
public interface ListarExamenesUseCase {

    /**
     * Obtiene todos los examenes en el orden en que fueron registrados.
     *
     * @return lista de examenes, vacia si no hay ninguno
     */
    List<ExamenSangre> listar();
}
