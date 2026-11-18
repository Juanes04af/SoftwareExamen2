package co.edu.poli.examen2.dominio.modelo;

/**
 * Formatos disponibles para serializar y deserializar los examenes.
 *
 * <p>Cada formato tiene una estrategia concreta en la infraestructura
 * (patron Strategy).</p>
 *
 * @author Juan Rosero
 */
public enum FormatoArchivo {

    /** Serializacion binaria de Java. */
    BINARIO("Binario (.dat)", "dat"),
    /** Texto separado por comas. */
    CSV("CSV (.csv)", "csv");

    /** Nombre que se muestra en la interfaz. */
    private final String descripcion;

    /** Extension del archivo sin el punto. */
    private final String extension;

    FormatoArchivo(String descripcion, String extension) {
        this.descripcion = descripcion;
        this.extension = extension;
    }

    /**
     * Devuelve la extension del archivo sin el punto.
     *
     * @return extension, por ejemplo "csv"
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Devuelve el nombre visible del formato.
     *
     * @return descripcion del formato
     */
    @Override
    public String toString() {
        return descripcion;
    }
}
