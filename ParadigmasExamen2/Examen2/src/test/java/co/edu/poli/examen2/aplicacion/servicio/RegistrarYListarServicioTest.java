package co.edu.poli.examen2.aplicacion.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.edu.poli.examen2.dominio.excepcion.ExamenDuplicadoException;
import co.edu.poli.examen2.dominio.excepcion.ExamenValidacionException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FactorRh;
import co.edu.poli.examen2.infraestructura.persistencia.RepositorioMemoriaAdapter;

/**
 * Pruebas de los casos de uso de registro y listado.
 *
 * @author Juan Rosero
 */
class RegistrarYListarServicioTest {

    private RegistrarExamenServicio registrar;
    private ListarExamenesServicio listar;

    @BeforeEach
    void preparar() {
        RepositorioMemoriaAdapter repositorio = new RepositorioMemoriaAdapter();
        registrar = new RegistrarExamenServicio(repositorio);
        listar = new ListarExamenesServicio(repositorio);
    }

    @Test
    @DisplayName("Registra un examen y aparece en el listado")
    void registraYLista() {
        ExamenSangre examen = registrar.registrar("EXM-001", "Ana Perez", "150", "A", true);

        assertEquals(FactorRh.POSITIVO, examen.getFactorRh());
        assertEquals(List.of(examen), listar.listar());
    }

    @Test
    @DisplayName("Acepta coma como separador decimal en el costo")
    void costoConComa() {
        assertEquals(99.5, registrar.registrar("EXM-002", "Luis Gomez", "99,5", "O", false).getCosto(), 0.0001);
    }

    @Test
    @DisplayName("Rechaza un costo vacio o que no es numerico")
    void costoInvalido() {
        assertThrows(ExamenValidacionException.class,
                () -> registrar.registrar("EXM-003", "Ana Perez", "abc", "A", true));
        assertThrows(ExamenValidacionException.class,
                () -> registrar.registrar("EXM-003", "Ana Perez", " ", "A", true));
    }

    @Test
    @DisplayName("Rechaza un examen sin tipo de sangre")
    void sinTipoDeSangre() {
        assertThrows(ExamenValidacionException.class,
                () -> registrar.registrar("EXM-004", "Ana Perez", "100", null, true));
    }

    @Test
    @DisplayName("Rechaza un codigo repetido y no lo guarda dos veces")
    void codigoDuplicado() {
        registrar.registrar("EXM-005", "Ana Perez", "100", "B", true);

        assertThrows(ExamenDuplicadoException.class,
                () -> registrar.registrar(" EXM-005 ", "Otro Paciente", "80", "O", false));
        assertEquals(1, listar.listar().size());
    }

    @Test
    @DisplayName("El listado inicia vacio")
    void listadoVacio() {
        assertTrue(listar.listar().isEmpty());
    }
}
