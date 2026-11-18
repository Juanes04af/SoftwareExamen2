package co.edu.poli.examen2.infraestructura.archivo.strategy;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;

/**
 * Patron Strategy (comportamiento): interfaz comun de las formas
 * de escribir y leer examenes en un archivo.
 *
 * <p><b>Problema que resuelve:</b> sin esta interfaz, el codigo que guarda
 * archivos tendria un {@code if/else} por cada formato y agregar uno nuevo
 * obligaria a modificarlo.</p>
 *
 * <p><b>Solucion:</b> cada formato es una clase que implementa esta interfaz.
 * El contexto ({@code ArchivoExamenesAdapter}) usa la estrategia elegida
 * sin saber cual es.</p>
 *
 * @author Juan Rosero
 */
public interface EstrategiaSerializacion {

    /**
     * Indica que formato implementa esta estrategia.
     *
     * @return formato del archivo
     */
    FormatoArchivo getFormato();

    /**
     * Escribe los examenes en el archivo.
     *
     * @param examenes examenes a escribir
     * @param archivo archivo destino
     * @throws IOException si ocurre un error de escritura
     */
    void escribir(List<ExamenSangre> examenes, Path archivo) throws IOException;

    /**
     * Lee los examenes del archivo.
     *
     * @param archivo archivo origen
     * @return examenes leidos
     * @throws IOException si ocurre un error de lectura o el contenido es invalido
     */
    List<ExamenSangre> leer(Path archivo) throws IOException;
}
