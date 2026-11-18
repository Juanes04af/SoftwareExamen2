package co.edu.poli.examen2.infraestructura.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.ExamenSangreBuilder;
import co.edu.poli.examen2.dominio.modelo.FactorRh;
import co.edu.poli.examen2.dominio.modelo.TipoSangre;

/**
 * Pruebas del adaptador de salida en memoria (patron Adapter).
 *
 * @author Juan Rosero
 */
class RepositorioMemoriaAdapterTest {

    private final RepositorioMemoriaAdapter repositorio = new RepositorioMemoriaAdapter();

    private static ExamenSangre examen(String codigo) {
        return new ExamenSangreBuilder().codigo(codigo).nombrePaciente("Paciente")
                .costo(50).tipoSangre(TipoSangre.A).factorRh(FactorRh.NEGATIVO).build();
    }

    @Test
    @DisplayName("Guarda examenes y los lista en orden de registro")
    void guardaEnOrden() {
        repositorio.guardar(examen("B"));
        repositorio.guardar(examen("A"));

        assertEquals(List.of(examen("B"), examen("A")), repositorio.listar());
    }

    @Test
    @DisplayName("Indica si existe un codigo, ignorando espacios")
    void existe() {
        repositorio.guardar(examen("EXM-1"));

        assertTrue(repositorio.existe(" EXM-1 "));
        assertFalse(repositorio.existe("EXM-2"));
        assertFalse(repositorio.existe(null));
    }

    @Test
    @DisplayName("El listado es una copia: modificarlo no altera el repositorio")
    void listadoEsCopia() {
        repositorio.guardar(examen("X"));
        repositorio.listar().clear();

        assertEquals(1, repositorio.listar().size());
    }

    @Test
    @DisplayName("Reemplazar todos borra los anteriores")
    void reemplazaTodos() {
        repositorio.guardar(examen("VIEJO"));
        repositorio.reemplazarTodos(List.of(examen("N1")));

        assertEquals(List.of(examen("N1")), repositorio.listar());
    }
}
