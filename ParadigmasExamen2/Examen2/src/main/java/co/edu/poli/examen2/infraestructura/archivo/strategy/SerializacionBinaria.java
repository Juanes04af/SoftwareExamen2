package co.edu.poli.examen2.infraestructura.archivo.strategy;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;

/**
 * Estrategia concreta: serializacion binaria de Java (archivo .dat).
 *
 * <p>Usa {@link ObjectOutputStream} y {@link ObjectInputStream}. Al leer aplica
 * un {@link ObjectInputFilter} que solo permite las clases del modelo, para
 * evitar la deserializacion de objetos arbitrarios.</p>
 *
 * @author Juan Rosero
 */
public class SerializacionBinaria implements EstrategiaSerializacion {

    /**
     * Clases permitidas al deserializar; cualquier otra se rechaza.
     * {@code java.lang.Object} se permite solo porque ArrayList guarda sus
     * elementos internamente en un arreglo {@code Object[]}.
     */
    private static final ObjectInputFilter FILTRO = ObjectInputFilter.Config.createFilter(
            "maxdepth=10;maxarray=100000;co.edu.poli.examen2.dominio.modelo.*;"
            + "java.util.ArrayList;java.lang.Enum;java.lang.Object;!*");

    /**
     * {@inheritDoc}
     */
    @Override
    public FormatoArchivo getFormato() {
        return FormatoArchivo.BINARIO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void escribir(List<ExamenSangre> examenes, Path archivo) throws IOException {
        try (OutputStream salida = Files.newOutputStream(archivo);
             ObjectOutputStream objetos = new ObjectOutputStream(salida)) {
            objetos.writeObject(new ArrayList<>(examenes));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExamenSangre> leer(Path archivo) throws IOException {
        try (InputStream entrada = Files.newInputStream(archivo);
             ObjectInputStream objetos = new ObjectInputStream(entrada)) {
            objetos.setObjectInputFilter(FILTRO);
            Object leido = objetos.readObject();
            return convertir(leido);
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IOException("El archivo no contiene examenes validos.", e);
        }
    }

    /**
     * Verifica que el objeto leido sea una lista de examenes.
     *
     * @param leido objeto deserializado
     * @return lista de examenes
     * @throws IOException si el contenido no es una lista de examenes
     */
    private static List<ExamenSangre> convertir(Object leido) throws IOException {
        if (!(leido instanceof List)) {
            throw new IOException("El archivo no contiene una lista de examenes.");
        }
        List<ExamenSangre> examenes = new ArrayList<>();
        for (Object elemento : (List<?>) leido) {
            if (!(elemento instanceof ExamenSangre)) {
                throw new IOException("El archivo contiene datos que no son examenes.");
            }
            examenes.add((ExamenSangre) elemento);
        }
        return examenes;
    }
}
