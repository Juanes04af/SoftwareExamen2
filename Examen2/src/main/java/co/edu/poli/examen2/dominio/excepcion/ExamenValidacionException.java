package co.edu.poli.examen2.dominio.excepcion;

/**
 * Excepcion lanzada cuando un dato del examen no cumple
 * las reglas de validacion.
 *
 * @author Juan Rosero
 */
public class ExamenValidacionException extends ExamenException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepcion.
     *
     * @param mensaje regla que no fue cumplida
     */
    public ExamenValidacionException(String mensaje) {
        super(mensaje);
    }
}
