package co.edu.poli.examen2.aplicacion.servicio;

import java.util.List;

import co.edu.poli.examen2.aplicacion.puerto.entrada.ListarExamenesUseCase;
import co.edu.poli.examen2.aplicacion.puerto.salida.ExamenRepository;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

/**
 * Servicio de aplicacion que lista los examenes registrados.
 *
 * @author Juan Rosero
 */
public class ListarExamenesServicio implements ListarExamenesUseCase {

    private final ExamenRepository examenRepository;

    /**
     * Construye el servicio.
     *
     * @param examenRepository puerto de salida para persistencia
     */
    public ListarExamenesServicio(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExamenSangre> listar() {
        return examenRepository.listar();
    }
}
