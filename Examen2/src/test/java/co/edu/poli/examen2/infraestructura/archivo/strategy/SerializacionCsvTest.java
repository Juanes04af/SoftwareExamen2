package co.edu.poli.examen2.infraestructura.archivo.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
 * Pruebas de la estrategia CSV (patron Strategy).
 *
 * @author Juan Rosero
 */
class SerializacionCsvTest {

    @TempDir
    Path carpeta;

    private final SerializacionCsv estrategia = new SerializacionCsv();

    private static ExamenSangre examen(String codigo, String nombre) {
        return new ExamenSangreBuilder().codigo(codigo).nombrePaciente(nombre)
                .costo(120.5).tipoSangre(TipoSangre.B).factorRh(FactorRh.NEGATIVO).build();
    }

    @Test
    @DisplayName("Implementa el formato CSV")
    void formato() {
        assertEquals(FormatoArchivo.CSV, estrategia.getFormato());
    }

    @Test
    @DisplayName("Escribe un archivo de texto legible con encabezado")
    void escribeTextoLegible() throws IOException {
        Path archivo = carpeta.resolve("examenes.csv");
        estrategia.escribir(List.of(examen("EXM-001", "Ana Perez")), archivo);

        List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
        assertEquals(SerializacionCsv.ENCABEZADO, lineas.get(0));
        assertEquals("EXM-001,Ana Perez,120.50,B,NEGATIVO", lineas.get(1));
    }

    @Test
    @DisplayName("Lo que escribe lo puede volver a leer igual, incluso con comas y comillas")
    void idaYVuelta() throws IOException {
        Path archivo = carpeta.resolve("examenes.csv");
        List<ExamenSangre> originales = List.of(
                examen("EXM-001", "Perez, Ana"),
                examen("EXM-002", "Luis \"Lucho\" Gomez"));

        estrategia.escribir(originales, archivo);
        List<ExamenSangre> leidos = estrategia.leer(archivo);

        assertEquals(originales, leidos);
        assertEquals("Perez, Ana", leidos.get(0).getNombrePaciente());
        assertEquals("Luis \"Lucho\" Gomez", leidos.get(1).getNombrePaciente());
    }

    @Test
    @DisplayName("Lee un archivo editado a mano: sin encabezado, con BOM, simbolo + y lineas vacias")
    void leeArchivoEditadoAMano() throws IOException {
        Path archivo = carpeta.resolve("manual.csv");
        Files.writeString(archivo, "﻿EXM-010,Maria Ruiz,80,ab,+\n\nEXM-011,Pedro Diaz,95.5,O,NEGATIVO\n",
                StandardCharsets.UTF_8);

        List<ExamenSangre> leidos = estrategia.leer(archivo);

        assertEquals(2, leidos.size());
        assertEquals("AB+", leidos.get(0).getGrupoSanguineo());
        assertEquals(95.5, leidos.get(1).getCosto(), 0.0001);
    }

    @Test
    @DisplayName("Una fila invalida se rechaza indicando el numero de linea")
    void filaInvalida() throws IOException {
        Path archivo = carpeta.resolve("malo.csv");
        Files.writeString(archivo, SerializacionCsv.ENCABEZADO + "\nEXM-1,Ana Perez,-5,A,POSITIVO\n");

        IOException error = assertThrows(IOException.class, () -> estrategia.leer(archivo));
        assertTrue(error.getMessage().startsWith("Linea 2"));
    }

    @Test
    @DisplayName("Una fila con columnas de mas o de menos se rechaza")
    void columnasIncorrectas() throws IOException {
        Path archivo = carpeta.resolve("malo.csv");
        Files.writeString(archivo, "EXM-1,Ana Perez,100,A\n");

        assertThrows(IOException.class, () -> estrategia.leer(archivo));
    }

    @Test
    @DisplayName("Separa campos y escapa valores con caracteres especiales")
    void separarYEscapar() {
        assertEquals(List.of("a", "b,c", "d\"e"), SerializacionCsv.separar("a,\"b,c\",\"d\"\"e\""));
        assertEquals("simple", SerializacionCsv.escapar("simple"));
        assertEquals("\"x,y\"", SerializacionCsv.escapar("x,y"));
    }
}
