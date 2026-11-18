package co.edu.poli.examen2.aplicacion.servicio;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.edu.poli.examen2.aplicacion.puerto.entrada.DeserializarExamenesUseCase;
import co.edu.poli.examen2.aplicacion.puerto.salida.ArchivoExamenesPort;
import co.edu.poli.examen2.aplicacion.puerto.salida.ExamenRepository;
import co.edu.poli.examen2.dominio.excepcion.ExamenDuplicadoException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;

/**
 * Servicio de aplicacion que carga examenes desde un archivo.
 *
 * <p>Lee el archivo mediante {@link ArchivoExamenesPort}, verifica que no
 * haya codigos repetidos y reemplaza los examenes que estaban en memoria.</p>
 *
 * @author Juan Rosero
 */
public class DeserializarExamenesServicio implements DeserializarExamenesUseCase {

    private final ExamenRepository examenRepository;
    private final ArchivoExamenesPort archivoExamenes;

    /**
     * Construye el servicio.
     *
     * @param examenRepository puerto de salida con los examenes en memoria
     * @param archivoExamenes puerto de salida hacia archivos
     */
    public DeserializarExamenesServicio(ExamenRepository examenRepository,
                                        ArchivoExamenesPort archivoExamenes) {
        this.examenRepository = examenRepository;
        this.archivoExamenes = archivoExamenes;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ExamenDuplicadoException si el archivo trae codigos repetidos
     */
    @Override
    public int deserializar(String ruta, FormatoArchivo formato) {
        ValidadorArchivo.validar(ruta, formato);

        List<ExamenSangre> examenes = archivoExamenes.leer(ruta, formato);

        Set<String> codigos = new HashSet<>();
        for (ExamenSangre examen : examenes) {
            if (!codigos.add(examen.getCodigo())) {
                throw new ExamenDuplicadoException(examen.getCodigo());
            }
        }

        examenRepository.reemplazarTodos(examenes);
        return examenes.size();
    }
}
