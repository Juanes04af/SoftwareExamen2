package co.edu.poli.examen2.aplicacion.puerto.salida;

import java.util.List;

import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

/**
 * Puerto de salida para almacenar y recuperar examenes.
 *
 * <p>La aplicacion conoce esta interfaz, pero no sabe si los datos
 * viven en memoria, en un archivo o en una base de datos.</p>
 *
 * @author Juan Rosero
 */
public interface ExamenRepository {

    /**
     * Guarda un examen.
     *
     * @param examen examen a guardar
     */
    void guardar(ExamenSangre examen);

    /**
     * Indica si ya existe un examen con el codigo dado.
     *
     * @param codigo codigo a buscar
     * @return {@code true} si existe
     */
    boolean existe(String codigo);

    /**
     * Obtiene todos los examenes.
     *
     * @return lista de examenes
     */
    List<ExamenSangre> listar();

    /**
     * Reemplaza todos los examenes por los recibidos.
     *
     * @param examenes nuevos examenes
     */
    void reemplazarTodos(List<ExamenSangre> examenes);
}
