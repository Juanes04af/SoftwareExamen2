package co.edu.poli.examen2.dominio.modelo;

import co.edu.poli.examen2.dominio.excepcion.ExamenValidacionException;

/**
 * Grupos sanguineos del sistema ABO.
 *
 * <p>Reemplaza el texto libre que usaba la vista original
 * ("A", "B", "AB", "O") por un tipo que el compilador puede verificar.</p>
 *
 * @author Juan Rosero
 */
public enum TipoSangre {

    /** Grupo A. */
    A,
    /** Grupo B. */
    B,
    /** Grupo AB. */
    AB,
    /** Grupo O. */
    O;

    /**
     * Convierte un texto en un tipo de sangre.
     *
     * @param texto texto a convertir, sin importar mayusculas o espacios
     * @return tipo de sangre correspondiente
     * @throws ExamenValidacionException si el texto es vacio o no es un grupo valido
     */
    public static TipoSangre desdeTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new ExamenValidacionException("Debe seleccionar el tipo de sangre.");
        }
        try {
            return TipoSangre.valueOf(texto.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ExamenValidacionException("Tipo de sangre invalido: " + texto);
        }
    }
}
