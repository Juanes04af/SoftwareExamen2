package co.edu.poli.examen2.dominio.modelo;

import co.edu.poli.examen2.dominio.excepcion.ExamenValidacionException;

/**
 * Factor RH de la sangre.
 *
 * @author Juan Rosero
 */
public enum FactorRh {

    /** Factor RH positivo. */
    POSITIVO("+"),
    /** Factor RH negativo. */
    NEGATIVO("-");

    /** Simbolo usado para mostrar el factor junto al grupo (por ejemplo AB+). */
    private final String simbolo;

    FactorRh(String simbolo) {
        this.simbolo = simbolo;
    }

    /**
     * Devuelve el simbolo del factor.
     *
     * @return "+" o "-"
     */
    public String getSimbolo() {
        return simbolo;
    }

    /**
     * Convierte un texto en un factor RH. Acepta el nombre
     * (POSITIVO, NEGATIVO) o el simbolo (+, -).
     *
     * @param texto texto a convertir
     * @return factor correspondiente
     * @throws ExamenValidacionException si el texto no corresponde a ningun factor
     */
    public static FactorRh desdeTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new ExamenValidacionException("Debe seleccionar el factor RH.");
        }
        String valor = texto.trim().toUpperCase();
        for (FactorRh factor : values()) {
            if (factor.name().equals(valor) || factor.simbolo.equals(valor)) {
                return factor;
            }
        }
        throw new ExamenValidacionException("Factor RH invalido: " + texto);
    }
}
