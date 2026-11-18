package co.edu.poli.examen2.dominio.excepcion;

/**
 * Excepcion lanzada cuando no es posible leer o escribir
 * el archivo de examenes.
 *
 * @author Juan Rosero
 */
public class ArchivoException extends ExamenException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepcion con mensaje.
     *
     * @param mensaje descripcion del error
     */
    public ArchivoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Construye la excepcion con mensaje y causa.
     *
     * @param mensaje descripcion del error
     * @param causa causa original
     */
    public ArchivoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
