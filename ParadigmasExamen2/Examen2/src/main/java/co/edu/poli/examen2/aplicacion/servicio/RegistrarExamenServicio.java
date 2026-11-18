package co.edu.poli.examen2.aplicacion.servicio;

import co.edu.poli.examen2.aplicacion.puerto.entrada.RegistrarExamenUseCase;
import co.edu.poli.examen2.aplicacion.puerto.salida.ExamenRepository;
import co.edu.poli.examen2.dominio.excepcion.ExamenDuplicadoException;
import co.edu.poli.examen2.dominio.excepcion.ExamenValidacionException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.ExamenSangreBuilder;
import co.edu.poli.examen2.dominio.modelo.FactorRh;
import co.edu.poli.examen2.dominio.modelo.TipoSangre;

/**
 * Servicio de aplicacion que registra examenes de sangre.
 *
 * <p>Convierte los textos de la interfaz, construye el examen con
 * {@link ExamenSangreBuilder} y lo guarda mediante el puerto
 * {@link ExamenRepository}.</p>
 *
 * @author Juan Rosero
 */
public class RegistrarExamenServicio implements RegistrarExamenUseCase {

    private final ExamenRepository examenRepository;

    /**
     * Construye el servicio.
     *
     * @param examenRepository puerto de salida para persistencia
     */
    public RegistrarExamenServicio(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ExamenValidacionException si algun dato es invalido
     * @throws ExamenDuplicadoException si el codigo ya existe
     */
    @Override
    public ExamenSangre registrar(String codigo,
                                  String nombrePaciente,
                                  String costoTexto,
                                  String tipoSangre,
                                  boolean rhPositivo) {

        ExamenSangre examen = new ExamenSangreBuilder()
                .codigo(codigo)
                .nombrePaciente(nombrePaciente)
                .costo(parsearCosto(costoTexto))
                .tipoSangre(TipoSangre.desdeTexto(tipoSangre))
                .factorRh(rhPositivo ? FactorRh.POSITIVO : FactorRh.NEGATIVO)
                .build();

        if (examenRepository.existe(examen.getCodigo())) {
            throw new ExamenDuplicadoException(examen.getCodigo());
        }

        examenRepository.guardar(examen);
        return examen;
    }

    /**
     * Convierte el costo capturado como texto. Acepta punto o coma decimal.
     *
     * @param costoTexto texto del costo
     * @return costo como numero
     * @throws ExamenValidacionException si el texto no es un numero
     */
    static double parsearCosto(String costoTexto) {
        if (costoTexto == null || costoTexto.isBlank()) {
            throw new ExamenValidacionException("El costo es obligatorio.");
        }
        try {
            return Double.parseDouble(costoTexto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new ExamenValidacionException("El costo debe ser numerico: " + costoTexto);
        }
    }
}
