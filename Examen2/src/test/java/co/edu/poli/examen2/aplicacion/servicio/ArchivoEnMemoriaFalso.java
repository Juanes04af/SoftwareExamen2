package co.edu.poli.examen2.aplicacion.servicio;

import java.util.ArrayList;
import java.util.List;

import co.edu.poli.examen2.aplicacion.puerto.salida.ArchivoExamenesPort;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;

/**
 * Adaptador falso del puerto de archivos usado en las pruebas de los servicios.
 * Demuestra que los casos de uso funcionan sin tocar el disco.
 *
 * @author Juan Rosero
 */
class ArchivoEnMemoriaFalso implements ArchivoExamenesPort {

    List<ExamenSangre> contenido = new ArrayList<>();
    String ultimaRuta;
    FormatoArchivo ultimoFormato;

    @Override
    public void escribir(List<ExamenSangre> examenes, String ruta, FormatoArchivo formato) {
        contenido = new ArrayList<>(examenes);
        ultimaRuta = ruta;
        ultimoFormato = formato;
    }

    @Override
    public List<ExamenSangre> leer(String ruta, FormatoArchivo formato) {
        ultimaRuta = ruta;
        ultimoFormato = formato;
        return new ArrayList<>(contenido);
    }
}
