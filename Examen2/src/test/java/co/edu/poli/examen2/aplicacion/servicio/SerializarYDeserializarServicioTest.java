package co.edu.poli.examen2.aplicacion.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.edu.poli.examen2.dominio.excepcion.ExamenDuplicadoException;
import co.edu.poli.examen2.dominio.excepcion.ExamenValidacionException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.ExamenSangreBuilder;
import co.edu.poli.examen2.dominio.modelo.FactorRh;
import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;
import co.edu.poli.examen2.dominio.modelo.TipoSangre;
import co.edu.poli.examen2.infraestructura.persistencia.RepositorioMemoriaAdapter;

/**
 * Pruebas de los casos de uso de serializar y deserializar
 * usando un adaptador falso del puerto de archivos.
 *
 * @author Juan Rosero
 */
class SerializarYDeserializarServicioTest {

    private RepositorioMemoriaAdapter repositorio;
    private ArchivoEnMemoriaFalso archivo;
    private SerializarExamenesServicio serializar;
    private DeserializarExamenesServicio deserializar;

    @BeforeEach
    void preparar() {
        repositorio = new RepositorioMemoriaAdapter();
        archivo = new ArchivoEnMemoriaFalso();
        serializar = new SerializarExamenesServicio(repositorio, archivo);
        deserializar = new DeserializarExamenesServicio(repositorio, archivo);
    }

    private static ExamenSangre examen(String codigo) {
        return new ExamenSangreBuilder().codigo(codigo).nombrePaciente("Paciente " + codigo)
                .costo(100).tipoSangre(TipoSangre.O).factorRh(FactorRh.POSITIVO).build();
    }

    @Test
    @DisplayName("Serializar entrega todos los examenes al puerto con la ruta y el formato")
    void serializaTodos() {
        repositorio.guardar(examen("E1"));
        repositorio.guardar(examen("E2"));

        assertEquals(2, serializar.serializar("examenes.csv", FormatoArchivo.CSV));
        assertEquals(2, archivo.contenido.size());
        assertEquals("examenes.csv", archivo.ultimaRuta);
        assertEquals(FormatoArchivo.CSV, archivo.ultimoFormato);
    }

    @Test
    @DisplayName("No permite serializar si no hay examenes")
    void serializarSinExamenes() {
        assertThrows(ExamenValidacionException.class,
                () -> serializar.serializar("examenes.dat", FormatoArchivo.BINARIO));
    }

    @Test
    @DisplayName("Exige la ruta y el formato del archivo")
    void exigeRutaYFormato() {
        repositorio.guardar(examen("E1"));

        assertThrows(ExamenValidacionException.class, () -> serializar.serializar(" ", FormatoArchivo.CSV));
        assertThrows(ExamenValidacionException.class, () -> serializar.serializar("a.csv", null));
        assertThrows(ExamenValidacionException.class, () -> deserializar.deserializar(null, FormatoArchivo.CSV));
    }

    @Test
    @DisplayName("Deserializar reemplaza los examenes que estaban en memoria")
    void deserializaReemplazando() {
        repositorio.guardar(examen("VIEJO"));
        archivo.contenido = List.of(examen("N1"), examen("N2"));

        assertEquals(2, deserializar.deserializar("examenes.dat", FormatoArchivo.BINARIO));
        assertEquals(List.of(examen("N1"), examen("N2")), repositorio.listar());
    }

    @Test
    @DisplayName("Rechaza un archivo con codigos repetidos y conserva los datos actuales")
    void archivoConDuplicados() {
        repositorio.guardar(examen("ACTUAL"));
        archivo.contenido = List.of(examen("D1"), examen("D1"));

        assertThrows(ExamenDuplicadoException.class,
                () -> deserializar.deserializar("examenes.csv", FormatoArchivo.CSV));
        assertEquals(List.of(examen("ACTUAL")), repositorio.listar());
    }
}
