/**
 * Modulo de la aplicacion de examenes de sangre con arquitectura hexagonal.
 *
 * @author Juan Rosero
 */
module co.edu.poli.examen2 {

    requires javafx.controls;
    requires javafx.fxml;

    /* JavaFX necesita acceso reflexivo al adaptador de entrada. */
    opens co.edu.poli.examen2.infraestructura.ui to javafx.fxml;

    exports co.edu.poli.examen2.infraestructura.web;
}
