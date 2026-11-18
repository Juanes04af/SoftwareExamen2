package co.edu.poli.examen2.dominio.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.edu.poli.examen2.dominio.excepcion.ExamenValidacionException;

/**
 * Pruebas del patron Builder y de las reglas de la entidad ExamenSangre.
 *
 * @author Juan Rosero
 */
class ExamenSangreBuilderTest {

    private ExamenSangreBuilder valido() {
        return new ExamenSangreBuilder()
                .codigo("EXM-001")
                .nombrePaciente("Ana Perez")
                .costo(150.0)
                .tipoSangre(TipoSangre.AB)
                .factorRh(FactorRh.NEGATIVO);
    }

    @Test
    @DisplayName("Construye un examen valido con todos sus datos")
    void construyeExamenValido() {
        ExamenSangre examen = valido().build();

        assertEquals("EXM-001", examen.getCodigo());
        assertEquals("Ana Perez", examen.getNombrePaciente());
        assertEquals(150.0, examen.getCosto(), 0.0001);
        assertEquals(TipoSangre.AB, examen.getTipoSangre());
        assertEquals(FactorRh.NEGATIVO, examen.getFactorRh());
        assertEquals("AB-", examen.getGrupoSanguineo());
    }

    @Test
    @DisplayName("Elimina los espacios al inicio y al final del codigo y el nombre")
    void eliminaEspacios() {
        ExamenSangre examen = valido().codigo("  EXM-002 ").nombrePaciente("  Luis Gomez  ").build();

        assertEquals("EXM-002", examen.getCodigo());
        assertEquals("Luis Gomez", examen.getNombrePaciente());
    }

    @Test
    @DisplayName("Rechaza un codigo vacio o nulo")
    void rechazaCodigoVacio() {
        assertThrows(ExamenValidacionException.class, () -> valido().codigo("   ").build());
        assertThrows(ExamenValidacionException.class, () -> valido().codigo(null).build());
    }

    @Test
    @DisplayName("Rechaza un nombre con menos de tres caracteres")
    void rechazaNombreCorto() {
        assertThrows(ExamenValidacionException.class, () -> valido().nombrePaciente("Al").build());
        assertThrows(ExamenValidacionException.class, () -> valido().nombrePaciente(null).build());
    }

    @Test
    @DisplayName("Rechaza un costo cero, negativo o que no sea un numero")
    void rechazaCostoInvalido() {
        assertThrows(ExamenValidacionException.class, () -> valido().costo(0).build());
        assertThrows(ExamenValidacionException.class, () -> valido().costo(-50).build());
        assertThrows(ExamenValidacionException.class, () -> valido().costo(Double.NaN).build());
        assertThrows(ExamenValidacionException.class,
                () -> valido().costo(Double.POSITIVE_INFINITY).build());
    }

    @Test
    @DisplayName("Rechaza un examen sin tipo de sangre o sin factor RH")
    void rechazaTipoOFactorNulo() {
        assertThrows(ExamenValidacionException.class, () -> valido().tipoSangre(null).build());
        assertThrows(ExamenValidacionException.class, () -> valido().factorRh(null).build());
    }

    @Test
    @DisplayName("Dos examenes son iguales si tienen el mismo codigo")
    void igualdadPorCodigo() {
        ExamenSangre a = valido().build();
        ExamenSangre b = valido().nombrePaciente("Otro Paciente").costo(99).build();
        ExamenSangre c = valido().codigo("EXM-999").build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    @DisplayName("toString muestra codigo, paciente, grupo y costo")
    void representacionTexto() {
        assertEquals("EXM-001 | Ana Perez | AB- | $150.00", valido().build().toString());
    }
}
