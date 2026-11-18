package co.edu.poli.examen2.infraestructura.web;

import java.io.IOException;

import co.edu.poli.examen2.aplicacion.puerto.entrada.DeserializarExamenesUseCase;
import co.edu.poli.examen2.aplicacion.puerto.entrada.ListarExamenesUseCase;
import co.edu.poli.examen2.aplicacion.puerto.entrada.RegistrarExamenUseCase;
import co.edu.poli.examen2.aplicacion.puerto.entrada.SerializarExamenesUseCase;
import co.edu.poli.examen2.aplicacion.puerto.salida.ArchivoExamenesPort;
import co.edu.poli.examen2.aplicacion.puerto.salida.ExamenRepository;
import co.edu.poli.examen2.aplicacion.servicio.DeserializarExamenesServicio;
import co.edu.poli.examen2.aplicacion.servicio.ListarExamenesServicio;
import co.edu.poli.examen2.aplicacion.servicio.RegistrarExamenServicio;
import co.edu.poli.examen2.aplicacion.servicio.SerializarExamenesServicio;
import co.edu.poli.examen2.infraestructura.archivo.ArchivoExamenesAdapter;
import co.edu.poli.examen2.infraestructura.persistencia.RepositorioMemoriaAdapter;
import co.edu.poli.examen2.infraestructura.ui.ControlExamen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de entrada principal. Realiza el ensamblado (wiring) de la
 * arquitectura hexagonal: adaptadores de salida, casos de uso y
 * adaptador de entrada.
 *
 * @author Juan Rosero
 */
public class App extends Application {

    /** Ruta de la vista principal dentro del classpath. */
    private static final String VISTA = "/co/edu/poli/examen2/vista/examen.fxml";

    /**
     * Construye las dependencias y carga la interfaz JavaFX.
     *
     * @param stage escenario principal
     * @throws IOException si no se puede cargar el FXML
     */
    @Override
    public void start(Stage stage) throws IOException {

        /* ADAPTADORES DE SALIDA: memoria y archivos */
        ExamenRepository repositorio = new RepositorioMemoriaAdapter();
        ArchivoExamenesPort archivos = new ArchivoExamenesAdapter();

        /* CASOS DE USO */
        RegistrarExamenUseCase registrar = new RegistrarExamenServicio(repositorio);
        ListarExamenesUseCase listar = new ListarExamenesServicio(repositorio);
        SerializarExamenesUseCase serializar = new SerializarExamenesServicio(repositorio, archivos);
        DeserializarExamenesUseCase deserializar = new DeserializarExamenesServicio(repositorio, archivos);

        /* INYECCION de los puertos en el adaptador de entrada */
        FXMLLoader loader = new FXMLLoader(App.class.getResource(VISTA));
        loader.setControllerFactory(tipo -> {
            if (tipo == ControlExamen.class) {
                return new ControlExamen(registrar, listar, serializar, deserializar);
            }
            throw new IllegalStateException("Controlador no esperado: " + tipo.getName());
        });

        Parent root = loader.load();

        stage.setTitle("Examenes de Sangre - Arquitectura Hexagonal");
        stage.setScene(new Scene(root, 640, 640));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Inicia la aplicacion JavaFX.
     *
     * @param args argumentos de linea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
