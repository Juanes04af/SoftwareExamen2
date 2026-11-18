package co.edu.poli.examen2.dominio.excepcion;

/**
 * Excepcion lanzada cuando se intenta registrar un examen
 * con un codigo que ya existe.
 *
 * @author Juan Rosero
 */
public class ExamenDuplicadoException extends ExamenException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepcion.
     *
     * @param codigo codigo repetido
     */
    public ExamenDuplicadoException(String codigo) {
        super("Ya existe un examen con el codigo " + codigo + ".");
    }
}
