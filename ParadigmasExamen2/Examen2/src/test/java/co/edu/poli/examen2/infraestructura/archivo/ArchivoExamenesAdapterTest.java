package co.edu.poli.examen2.infraestructura.archivo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import co.edu.poli.examen2.dominio.excepcion.ArchivoException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.ExamenSangreBuilder;
import co.edu.poli.examen2.dominio.modelo.FactorRh;
import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;
import co.edu.poli.examen2.dominio.modelo.TipoSangre;
import co.edu.poli.examen2.infraestructura.archivo.strategy.EstrategiaSerializacion;

/**
 * Pruebas del adaptador de archivos, que actua como contexto del patron Strategy.
 *
 * @author Juan Rosero
 */
class ArchivoExamenesAdapterTest {

    @TempDir
    Path carpeta;

    private final ArchivoExamenesAdapter adaptador = new ArchivoExamenesAdapter();

    private final List<ExamenSangre> examenes = List.of(new ExamenSangreBuilder()
            .codigo("EXM-001").nombrePaciente("Ana Perez").costo(150)
            .tipoSangre(TipoSangre.A).factorRh(FactorRh.POSITIVO).build());

    @Test
    @DisplayName("Con formato CSV delega en la estrategia CSV: el archivo es texto")
    void usaEstrategiaCsv() throws IOException {
        Path archivo = carpeta.resolve("examenes.csv");
        adaptador.escribir(examenes, archivo.toString(), FormatoArchivo.CSV);

        assertTrue(Files.readString(archivo).startsWith("codigo,nombrePaciente"));
        assertEquals(examenes, adaptador.leer(archivo.toString(), FormatoArchivo.CSV));
    }

    @Test
    @DisplayName("Con formato binario delega en la estrategia binaria: el archivo inicia con 0xACED")
    void usaEstrategiaBinaria() throws IOException {
        Path archivo = carpeta.resolve("examenes.dat");
        adaptador.escribir(examenes, archivo.toString(), FormatoArchivo.BINARIO);

        byte[] bytes = Files.readAllBytes(archivo);
        assertEquals((byte) 0xAC, bytes[0]);
        assertEquals((byte) 0xED, bytes[1]);
        assertEquals(examenes, adaptador.leer(archivo.toString(), FormatoArchivo.BINARIO));
    }

    @Test
    @DisplayName("Crea las carpetas que no existen al escribir")
    void creaCarpetas() {
        Path archivo = carpeta.resolve("nueva/sub/examenes.csv");
        adaptador.escribir(examenes, archivo.toString(), FormatoArchivo.CSV);

        assertTrue(Files.exists(archivo));
    }

    @Test
    @DisplayName("Leer un archivo que no existe lanza ArchivoException")
    void archivoInexistente() {
        String ruta = carpeta.resolve("no-existe.csv").toString();

        assertThrows(ArchivoException.class, () -> adaptador.leer(ruta, FormatoArchivo.CSV));
    }

    @Test
    @DisplayName("Leer con el formato equivocado lanza ArchivoException")
    void formatoEquivocado() {
        Path archivo = carpeta.resolve("examenes.csv");
        adaptador.escribir(examenes, archivo.toString(), FormatoArchivo.CSV);

        assertThrows(ArchivoException.class,
                () -> adaptador.leer(archivo.toString(), FormatoArchivo.BINARIO));
    }

    @Test
    @DisplayName("Permite registrar una estrategia nueva sin modificar el adaptador")
    void registraNuevaEstrategia() {
        EstrategiaSerializacion falsa = new EstrategiaSerializacion() {
            @Override
            public FormatoArchivo getFormato() {
                return FormatoArchivo.CSV;
            }

            @Override
            public void escribir(List<ExamenSangre> lista, Path archivo) throws IOException {
                Files.writeString(archivo, "estrategia nueva");
            }

            @Override
            public List<ExamenSangre> leer(Path archivo) {
                return List.of();
            }
        };
        adaptador.registrar(falsa);
        Path archivo = carpeta.resolve("otro.csv");

        adaptador.escribir(examenes, archivo.toString(), FormatoArchivo.CSV);

        assertTrue(Files.exists(archivo));
        assertEquals(List.of(), adaptador.leer(archivo.toString(), FormatoArchivo.CSV));
    }
}
