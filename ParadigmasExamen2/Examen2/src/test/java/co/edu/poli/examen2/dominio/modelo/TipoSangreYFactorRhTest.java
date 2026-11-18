package co.edu.poli.examen2.dominio.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.edu.poli.examen2.dominio.excepcion.ExamenValidacionException;

/**
 * Pruebas de la conversion de textos a TipoSangre y FactorRh.
 *
 * @author Juan Rosero
 */
class TipoSangreYFactorRhTest {

    @Test
    @DisplayName("Convierte textos a tipo de sangre sin importar mayusculas ni espacios")
    void tipoDesdeTexto() {
        assertEquals(TipoSangre.AB, TipoSangre.desdeTexto(" ab "));
        assertEquals(TipoSangre.O, TipoSangre.desdeTexto("O"));
    }

    @Test
    @DisplayName("Rechaza tipos de sangre vacios o inexistentes")
    void tipoInvalido() {
        assertThrows(ExamenValidacionException.class, () -> TipoSangre.desdeTexto(""));
        assertThrows(ExamenValidacionException.class, () -> TipoSangre.desdeTexto(null));
        assertThrows(ExamenValidacionException.class, () -> TipoSangre.desdeTexto("Z"));
    }

    @Test
    @DisplayName("Convierte el factor RH por nombre o por simbolo")
    void factorDesdeTexto() {
        assertEquals(FactorRh.POSITIVO, FactorRh.desdeTexto("positivo"));
        assertEquals(FactorRh.NEGATIVO, FactorRh.desdeTexto("-"));
        assertEquals(FactorRh.POSITIVO, FactorRh.desdeTexto("+"));
    }

    @Test
    @DisplayName("Rechaza factores RH vacios o inexistentes")
    void factorInvalido() {
        assertThrows(ExamenValidacionException.class, () -> FactorRh.desdeTexto(" "));
        assertThrows(ExamenValidacionException.class, () -> FactorRh.desdeTexto(null));
        assertThrows(ExamenValidacionException.class, () -> FactorRh.desdeTexto("X"));
    }

    @Test
    @DisplayName("Cada formato de archivo tiene su extension")
    void extensionesDeFormato() {
        assertEquals("dat", FormatoArchivo.BINARIO.getExtension());
        assertEquals("csv", FormatoArchivo.CSV.getExtension());
        assertEquals("CSV (.csv)", FormatoArchivo.CSV.toString());
    }
}
