package co.edu.poli.examen2.dominio.excepcion;

/**
 * Excepcion base del dominio de examenes de sangre.
 *
 * @author Juan Rosero
 */
public class ExamenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye una excepcion con mensaje.
     *
     * @param mensaje descripcion del error
     */
    public ExamenException(String mensaje) {
        super(mensaje);
    }

    /**
     * Construye una excepcion con mensaje y causa.
     *
     * @param mensaje descripcion del error
     * @param causa causa original
     */
    public ExamenException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
