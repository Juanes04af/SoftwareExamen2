package co.edu.poli.examen2.infraestructura.ui;

import java.io.File;
import java.util.List;

import co.edu.poli.examen2.aplicacion.puerto.entrada.DeserializarExamenesUseCase;
import co.edu.poli.examen2.aplicacion.puerto.entrada.ListarExamenesUseCase;
import co.edu.poli.examen2.aplicacion.puerto.entrada.RegistrarExamenUseCase;
import co.edu.poli.examen2.aplicacion.puerto.entrada.SerializarExamenesUseCase;
import co.edu.poli.examen2.dominio.excepcion.ExamenException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FormatoArchivo;
import co.edu.poli.examen2.dominio.modelo.TipoSangre;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * Patron Adapter (estructural): adaptador de entrada JavaFX.
 *
 * <p>Traduce los eventos de la interfaz (clics y textos) en llamadas a los
 * puertos de entrada. No contiene reglas de negocio ni sabe como se guardan
 * los datos: solo recibe los casos de uso por constructor y muestra
 * el resultado en el area de mensajes.</p>
 *
 * @author Juan Rosero
 */
public class ControlExamen {

    /** Campo del codigo del examen. */
    @FXML
    private TextField txtCodigoExamen;

    /** Campo del nombre del paciente. */
    @FXML
    private TextField txtNombrePaciente;

    /** Campo del costo. */
    @FXML
    private TextField txtCosto;

    /** Lista de grupos sanguineos. */
    @FXML
    private ComboBox<TipoSangre> cmbTipoSangre;

    /** Opcion de factor RH positivo. */
    @FXML
    private RadioButton rbPositivo;

    /** Grupo de opciones del factor RH. */
    @FXML
    private ToggleGroup tgFactorRH;

    /** Lista de formatos de archivo (estrategias disponibles). */
    @FXML
    private ComboBox<FormatoArchivo> cmbFormato;

    /** Area de mensajes y listados. */
    @FXML
    private TextArea txtAreaMensajes;

    private final RegistrarExamenUseCase registrarExamen;
    private final ListarExamenesUseCase listarExamenes;
    private final SerializarExamenesUseCase serializarExamenes;
    private final DeserializarExamenesUseCase deserializarExamenes;

    /**
     * Construye el adaptador con los puertos de entrada que necesita.
     *
     * @param registrarExamen caso de uso de registro
     * @param listarExamenes caso de uso de listado
     * @param serializarExamenes caso de uso de serializacion
     * @param deserializarExamenes caso de uso de deserializacion
     */
    public ControlExamen(RegistrarExamenUseCase registrarExamen,
                         ListarExamenesUseCase listarExamenes,
                         SerializarExamenesUseCase serializarExamenes,
                         DeserializarExamenesUseCase deserializarExamenes) {
        this.registrarExamen = registrarExamen;
        this.listarExamenes = listarExamenes;
        this.serializarExamenes = serializarExamenes;
        this.deserializarExamenes = deserializarExamenes;
    }

    /**
     * Carga las opciones de los ComboBox despues de inyectar los controles.
     */
    @FXML
    public void initialize() {
        cmbTipoSangre.setItems(FXCollections.observableArrayList(TipoSangre.values()));
        cmbFormato.setItems(FXCollections.observableArrayList(FormatoArchivo.values()));
        cmbFormato.getSelectionModel().select(FormatoArchivo.CSV);
    }

    /**
     * Registra un examen con los datos del formulario.
     */
    @FXML
    private void handleGuardar() {
        TipoSangre tipo = cmbTipoSangre.getValue();
        try {
            ExamenSangre examen = registrarExamen.registrar(
                    txtCodigoExamen.getText(),
                    txtNombrePaciente.getText(),
                    txtCosto.getText(),
                    tipo == null ? null : tipo.name(),
                    rbPositivo.isSelected());
            mostrar("Examen registrado: " + examen);
            limpiarFormulario();
        } catch (ExamenException e) {
            mostrar("Error: " + e.getMessage());
        }
    }

    /**
     * Muestra todos los examenes registrados.
     */
    @FXML
    private void handleListar() {
        List<ExamenSangre> examenes = listarExamenes.listar();
        if (examenes.isEmpty()) {
            mostrar("No hay examenes registrados.");
            return;
        }
        StringBuilder texto = new StringBuilder("Examenes registrados (")
                .append(examenes.size()).append("):\n");
        for (ExamenSangre examen : examenes) {
            texto.append(" - ").append(examen).append('\n');
        }
        mostrar(texto.toString());
    }

    /**
     * Limpia los campos del formulario.
     */
    @FXML
    private void handleLimpiar() {
        limpiarFormulario();
        txtAreaMensajes.clear();
    }

    /**
     * Guarda los examenes en un archivo con el formato seleccionado.
     */
    @FXML
    private void handleSerializar() {
        FormatoArchivo formato = cmbFormato.getValue();
        File archivo = selector(formato, "Guardar examenes").showSaveDialog(ventana());
        if (archivo == null) {
            return;
        }
        try {
            int cantidad = serializarExamenes.serializar(archivo.getAbsolutePath(), formato);
            mostrar("Se guardaron " + cantidad + " examenes en " + archivo.getName()
                    + " con la estrategia " + formato + ".");
        } catch (ExamenException e) {
            mostrar("Error: " + e.getMessage());
        }
    }

    /**
     * Carga los examenes desde un archivo con el formato seleccionado.
     */
    @FXML
    private void handleDeserializar() {
        FormatoArchivo formato = cmbFormato.getValue();
        File archivo = selector(formato, "Cargar examenes").showOpenDialog(ventana());
        if (archivo == null) {
            return;
        }
        try {
            int cantidad = deserializarExamenes.deserializar(archivo.getAbsolutePath(), formato);
            mostrar("Se cargaron " + cantidad + " examenes desde " + archivo.getName()
                    + " con la estrategia " + formato + ".");
        } catch (ExamenException e) {
            mostrar("Error: " + e.getMessage());
        }
    }

    /**
     * Crea el selector de archivos filtrado por la extension del formato.
     *
     * @param formato formato seleccionado
     * @param titulo titulo de la ventana
     * @return selector configurado
     */
    private FileChooser selector(FormatoArchivo formato, String titulo) {
        FileChooser selector = new FileChooser();
        selector.setTitle(titulo);
        if (formato != null) {
            selector.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    formato.toString(), "*." + formato.getExtension()));
            selector.setInitialFileName("examenes." + formato.getExtension());
        }
        return selector;
    }

    private Window ventana() {
        return txtAreaMensajes.getScene().getWindow();
    }

    private void mostrar(String mensaje) {
        txtAreaMensajes.setText(mensaje);
    }

    private void limpiarFormulario() {
        txtCodigoExamen.clear();
        txtNombrePaciente.clear();
        txtCosto.clear();
        cmbTipoSangre.getSelectionModel().clearSelection();
        rbPositivo.setSelected(true);
    }
}
