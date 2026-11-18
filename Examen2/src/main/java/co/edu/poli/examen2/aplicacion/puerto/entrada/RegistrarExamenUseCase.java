package co.edu.poli.examen2.aplicacion.puerto.entrada;

import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

/**
 * Puerto de entrada para registrar un examen de sangre.
 *
 * @author Juan Rosero
 */
public interface RegistrarExamenUseCase {

    /**
     * Registra un nuevo examen con los datos capturados en la interfaz.
     *
     * @param codigo codigo del examen
     * @param nombrePaciente nombre del paciente
     * @param costoTexto costo capturado como texto
     * @param tipoSangre grupo sanguineo como texto (A, B, AB, O)
     * @param rhPositivo {@code true} si el factor RH es positivo
     * @return examen registrado
     */
    ExamenSangre registrar(String codigo,
                           String nombrePaciente,
                           String costoTexto,
                           String tipoSangre,
                           boolean rhPositivo);
}
