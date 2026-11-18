package co.edu.poli.examen2.aplicacion.servicio;

import java.util.List;

import co.edu.poli.examen2.aplicacion.puerto.entrada.SerializarExamenesUseCase;
import co.edu.poli.examen2.aplicacion.puerto.salida.ArchivoExamenesPort;
import co.edu.poli.examen2.aplicacion.puerto.salida.ExamenRepository;
import co.edu.poli.examen2.dominio.excepcion.ExamenValidacionException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;

/**
 * Servicio de aplicacion que guarda los examenes en un archivo.
 *
 * <p>Toma los examenes del repositorio y se los entrega al puerto
 * {@link ArchivoExamenesPort}; no sabe como se escribe cada formato.</p>
 *
 * @author Juan Rosero
 */
public class SerializarExamenesServicio implements SerializarExamenesUseCase {

    private final ExamenRepository examenRepository;
    private final ArchivoExamenesPort archivoExamenes;

    /**
     * Construye el servicio.
     *
     * @param examenRepository puerto de salida con los examenes en memoria
     * @param archivoExamenes puerto de salida hacia archivos
     */
    public SerializarExamenesServicio(ExamenRepository examenRepository,
                                      ArchivoExamenesPort archivoExamenes) {
        this.examenRepository = examenRepository;
        this.archivoExamenes = archivoExamenes;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ExamenValidacionException si no hay examenes, falta la ruta o el formato
     */
    @Override
    public int serializar(String ruta, FormatoArchivo formato) {
        ValidadorArchivo.validar(ruta, formato);

        List<ExamenSangre> examenes = examenRepository.listar();
        if (examenes.isEmpty()) {
            throw new ExamenValidacionException("No hay examenes registrados para serializar.");
        }

        archivoExamenes.escribir(examenes, ruta, formato);
        return examenes.size();
    }
}
