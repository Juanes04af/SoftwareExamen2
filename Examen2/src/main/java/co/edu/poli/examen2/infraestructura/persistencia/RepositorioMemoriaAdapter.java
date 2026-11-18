package co.edu.poli.examen2.infraestructura.persistencia;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.edu.poli.examen2.aplicacion.puerto.salida.ExamenRepository;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

/**
 * Patron Adapter (estructural): adaptador de salida que guarda
 * los examenes en memoria.
 *
 * <ul>
 *   <li><b>Target:</b> {@link ExamenRepository}, la interfaz que espera la aplicacion
 *       (guardar, existe, listar, reemplazarTodos).</li>
 *   <li><b>Adaptee:</b> {@link LinkedHashMap} de Java, que habla otro idioma
 *       (put, containsKey, values, clear).</li>
 *   <li><b>Adapter:</b> esta clase, que traduce una interfaz a la otra.</li>
 * </ul>
 *
 * <p>Se usa un mapa con orden de insercion para buscar por codigo
 * rapidamente y listar en el orden en que se registraron.</p>
 *
 * @author Juan Rosero
 */
public class RepositorioMemoriaAdapter implements ExamenRepository {

    /** Estructura adaptada: codigo del examen hacia el examen. */
    private final Map<String, ExamenSangre> examenes = new LinkedHashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void guardar(ExamenSangre examen) {
        examenes.put(examen.getCodigo(), examen);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existe(String codigo) {
        return codigo != null && examenes.containsKey(codigo.trim());
    }

    /**
     * {@inheritDoc}
     *
     * @return copia de la lista para que nadie modifique el almacenamiento interno
     */
    @Override
    public List<ExamenSangre> listar() {
        return new ArrayList<>(examenes.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reemplazarTodos(List<ExamenSangre> nuevos) {
        examenes.clear();
        for (ExamenSangre examen : nuevos) {
            guardar(examen);
        }
    }
}
