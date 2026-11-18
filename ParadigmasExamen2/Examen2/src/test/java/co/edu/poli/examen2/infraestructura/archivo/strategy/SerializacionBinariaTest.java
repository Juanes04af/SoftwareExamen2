package co.edu.poli.examen2.infraestructura.archivo.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.ExamenSangreBuilder;
import co.edu.poli.examen2.dominio.modelo.FactorRh;
import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;
import co.edu.poli.examen2.dominio.modelo.TipoSangre;

/**
 * Pruebas de la estrategia binaria (patron Strategy).
 *
 * @author Juan Rosero
 */
class SerializacionBinariaTest {

    @TempDir
    Path carpeta;

    private final SerializacionBinaria estrategia = new SerializacionBinaria();

    @Test
    @DisplayName("Implementa el formato binario")
    void formato() {
        assertEquals(FormatoArchivo.BINARIO, estrategia.getFormato());
    }

    @Test
    @DisplayName("Lo que escribe lo puede volver a leer con todos sus datos")
    void idaYVuelta() throws IOException {
        Path archivo = carpeta.resolve("examenes.dat");
        ExamenSangre original = new ExamenSangreBuilder().codigo("EXM-001").nombrePaciente("Ana Perez")
                .costo(150).tipoSangre(TipoSangre.O).factorRh(FactorRh.NEGATIVO).build();

        estrategia.escribir(List.of(original), archivo);
        ExamenSangre leido = estrategia.leer(archivo).get(0);

        assertEquals(original, leido);
        assertEquals("O-", leido.getGrupoSanguineo());
        assertEquals(150.0, leido.getCosto(), 0.0001);
    }

    @Test
    @DisplayName("Rechaza un archivo que no es binario de Java")
    void archivoCorrupto() throws IOException {
        Path archivo = carpeta.resolve("texto.dat");
        Files.writeString(archivo, "esto no es un archivo binario");

        assertThrows(IOException.class, () -> estrategia.leer(archivo));
    }

    @Test
    @DisplayName("El filtro de seguridad rechaza objetos que no son del modelo")
    void rechazaClasesNoPermitidas() throws IOException {
        Path archivo = carpeta.resolve("ajeno.dat");
        List<Object> ajeno = new ArrayList<>();
        ajeno.add(new Date());
        try (OutputStream salida = Files.newOutputStream(archivo);
             ObjectOutputStream objetos = new ObjectOutputStream(salida)) {
            objetos.writeObject(ajeno);
        }

        assertThrows(IOException.class, () -> estrategia.leer(archivo));
    }
}
