package co.edu.poli.examen2.infraestructura.archivo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import co.edu.poli.examen2.aplicacion.puerto.salida.ArchivoExamenesPort;
import co.edu.poli.examen2.dominio.excepcion.ArchivoException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;
import co.edu.poli.examen2.infraestructura.archivo.strategy.EstrategiaSerializacion;
import co.edu.poli.examen2.infraestructura.archivo.strategy.SerializacionBinaria;
import co.edu.poli.examen2.infraestructura.archivo.strategy.SerializacionCsv;

/**
 * Patron Adapter (estructural): adaptador de salida hacia el sistema de archivos.
 * Tambien es el <b>Contexto</b> del patron Strategy.
 *
 * <ul>
 *   <li><b>Como Adapter:</b> implementa el puerto {@link ArchivoExamenesPort} y se
 *       encarga de la "conexion" con el disco: convertir la ruta, verificar que el
 *       archivo exista, crear carpetas y traducir los errores de E/S a
 *       {@link ArchivoException}.</li>
 *   <li><b>Como Contexto de Strategy:</b> no sabe escribir ningun formato; elige
 *       la {@link EstrategiaSerializacion} que corresponde al formato pedido y
 *       le delega la lectura o escritura.</li>
 * </ul>
 *
 * @author Juan Rosero
 */
public class ArchivoExamenesAdapter implements ArchivoExamenesPort {

    /** Estrategias disponibles, una por formato. */
    private final Map<FormatoArchivo, EstrategiaSerializacion> estrategias =
            new EnumMap<>(FormatoArchivo.class);

    /**
     * Construye el adaptador con las estrategias binaria y CSV.
     */
    public ArchivoExamenesAdapter() {
        registrar(new SerializacionBinaria());
        registrar(new SerializacionCsv());
    }

    /**
     * Registra una estrategia. Agregar un formato nuevo (por ejemplo JSON) solo
     * requiere crear su clase y registrarla, sin modificar el resto del codigo.
     *
     * @param estrategia estrategia a registrar
     */
    public final void registrar(EstrategiaSerializacion estrategia) {
        estrategias.put(estrategia.getFormato(), estrategia);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void escribir(List<ExamenSangre> examenes, String ruta, FormatoArchivo formato) {
        Path archivo = Paths.get(ruta);
        try {
            Path carpeta = archivo.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            estrategia(formato).escribir(examenes, archivo);
        } catch (IOException e) {
            throw new ArchivoException("No fue posible escribir el archivo " + ruta, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExamenSangre> leer(String ruta, FormatoArchivo formato) {
        Path archivo = Paths.get(ruta);
        if (!Files.isRegularFile(archivo)) {
            throw new ArchivoException("El archivo no existe: " + ruta);
        }
        try {
            return estrategia(formato).leer(archivo);
        } catch (IOException e) {
            throw new ArchivoException("No fue posible leer el archivo " + ruta
                    + ". Verifique que el formato seleccionado sea " + formato + ".", e);
        }
    }

    /**
     * Busca la estrategia del formato pedido.
     *
     * @param formato formato del archivo
     * @return estrategia correspondiente
     * @throws ArchivoException si el formato no tiene estrategia registrada
     */
    private EstrategiaSerializacion estrategia(FormatoArchivo formato) {
        EstrategiaSerializacion estrategia = estrategias.get(formato);
        if (estrategia == null) {
            throw new ArchivoException("Formato no soportado: " + formato);
        }
        return estrategia;
    }
}
