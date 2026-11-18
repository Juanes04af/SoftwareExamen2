package co.edu.poli.examen2.infraestructura.archivo.strategy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.edu.poli.examen2.dominio.excepcion.ExamenValidacionException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.ExamenSangreBuilder;
import co.edu.poli.examen2.dominio.modelo.FactorRh;
import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;
import co.edu.poli.examen2.dominio.modelo.TipoSangre;

/**
 * Estrategia concreta: texto separado por comas (archivo .csv).
 *
 * <p>El archivo es legible y editable con el Bloc de notas. Cada fila se
 * reconstruye con {@link ExamenSangreBuilder}, de modo que una fila con datos
 * invalidos se rechaza indicando el numero de linea.</p>
 *
 * <pre>
 * codigo,nombrePaciente,costo,tipoSangre,factorRh
 * EXM-001,Ana Perez,150.00,A,POSITIVO
 * </pre>
 *
 * @author Juan Rosero
 */
public class SerializacionCsv implements EstrategiaSerializacion {

    /** Encabezado del archivo. */
    public static final String ENCABEZADO = "codigo,nombrePaciente,costo,tipoSangre,factorRh";

    private static final int COLUMNAS = 5;
    private static final char SEPARADOR = ',';
    private static final char COMILLA = '"';

    /**
     * {@inheritDoc}
     */
    @Override
    public FormatoArchivo getFormato() {
        return FormatoArchivo.CSV;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void escribir(List<ExamenSangre> examenes, Path archivo) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            escritor.write(ENCABEZADO);
            escritor.newLine();
            for (ExamenSangre examen : examenes) {
                escritor.write(String.join(String.valueOf(SEPARADOR),
                        escapar(examen.getCodigo()),
                        escapar(examen.getNombrePaciente()),
                        String.format(Locale.ROOT, "%.2f", examen.getCosto()),
                        examen.getTipoSangre().name(),
                        examen.getFactorRh().name()));
                escritor.newLine();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExamenSangre> leer(Path archivo) throws IOException {
        List<ExamenSangre> examenes = new ArrayList<>();
        try (BufferedReader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            int numero = 0;
            while ((linea = lector.readLine()) != null) {
                numero++;
                linea = quitarBom(linea);
                if (linea.isBlank() || (numero == 1 && linea.trim().equalsIgnoreCase(ENCABEZADO))) {
                    continue;
                }
                examenes.add(convertirFila(linea, numero));
            }
        }
        return examenes;
    }

    /**
     * Convierte una fila del archivo en un examen.
     *
     * @param linea texto de la fila
     * @param numero numero de linea, usado en el mensaje de error
     * @return examen construido
     * @throws IOException si la fila no tiene el formato esperado
     */
    private static ExamenSangre convertirFila(String linea, int numero) throws IOException {
        List<String> campos = separar(linea);
        if (campos.size() != COLUMNAS) {
            throw new IOException("Linea " + numero + ": se esperaban " + COLUMNAS
                    + " columnas y se encontraron " + campos.size() + ".");
        }
        try {
            return new ExamenSangreBuilder()
                    .codigo(campos.get(0))
                    .nombrePaciente(campos.get(1))
                    .costo(Double.parseDouble(campos.get(2).trim()))
                    .tipoSangre(TipoSangre.desdeTexto(campos.get(3)))
                    .factorRh(FactorRh.desdeTexto(campos.get(4)))
                    .build();
        } catch (NumberFormatException | ExamenValidacionException e) {
            throw new IOException("Linea " + numero + ": " + e.getMessage(), e);
        }
    }

    /**
     * Separa una fila en campos respetando los valores entre comillas.
     *
     * @param linea fila del archivo
     * @return lista de campos
     */
    static List<String> separar(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean entreComillas = false;
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (entreComillas) {
                if (c == COMILLA && i + 1 < linea.length() && linea.charAt(i + 1) == COMILLA) {
                    actual.append(COMILLA);
                    i++;
                } else if (c == COMILLA) {
                    entreComillas = false;
                } else {
                    actual.append(c);
                }
            } else if (c == COMILLA) {
                entreComillas = true;
            } else if (c == SEPARADOR) {
                campos.add(actual.toString());
                actual.setLength(0);
            } else {
                actual.append(c);
            }
        }
        campos.add(actual.toString());
        return campos;
    }

    /**
     * Encierra el valor entre comillas si contiene comas, comillas o saltos de linea.
     *
     * @param valor texto a escribir
     * @return texto seguro para CSV
     */
    static String escapar(String valor) {
        boolean especial = valor.indexOf(SEPARADOR) >= 0
                || valor.indexOf(COMILLA) >= 0
                || valor.indexOf('\n') >= 0;
        if (!especial) {
            return valor;
        }
        return COMILLA + valor.replace("\"", "\"\"") + COMILLA;
    }

    /**
     * Elimina la marca BOM que agregan algunos editores al inicio del archivo.
     *
     * @param linea linea leida
     * @return linea sin BOM
     */
    private static String quitarBom(String linea) {
        return linea.startsWith("﻿") ? linea.substring(1) : linea;
    }
}
