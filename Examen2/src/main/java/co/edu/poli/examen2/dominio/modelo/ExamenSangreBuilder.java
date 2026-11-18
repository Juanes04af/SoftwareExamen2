package co.edu.poli.examen2.dominio.modelo;

import co.edu.poli.examen2.dominio.excepcion.ExamenValidacionException;

/**
 * Patron Builder (creacional) para construir un {@link ExamenSangre}.
 *
 * <p><b>Problema que resuelve:</b> el examen tiene cinco datos, dos de ellos
 * de tipo texto. Con un constructor normal es facil intercambiar el codigo
 * con el nombre sin que el compilador lo note, y nada impide crear un examen
 * con datos invalidos.</p>
 *
 * <p><b>Solucion:</b> cada dato se asigna con un metodo con nombre y
 * {@link #build()} valida todas las reglas antes de crear el objeto.
 * Si una regla falla, el examen no se crea.</p>
 *
 * <pre>
 * ExamenSangre examen = new ExamenSangreBuilder()
 *         .codigo("EXM-001")
 *         .nombrePaciente("Ana Perez")
 *         .costo(150.0)
 *         .tipoSangre(TipoSangre.A)
 *         .factorRh(FactorRh.POSITIVO)
 *         .build();
 * </pre>
 *
 * @author Juan Rosero
 */
public class ExamenSangreBuilder {

    /** Longitud minima del nombre del paciente. */
    public static final int LONGITUD_MINIMA_NOMBRE = 3;

    private String codigo;
    private String nombrePaciente;
    private double costo;
    private TipoSangre tipoSangre;
    private FactorRh factorRh;

    /**
     * Asigna el codigo del examen.
     *
     * @param codigo codigo unico, por ejemplo EXM-001
     * @return este builder
     */
    public ExamenSangreBuilder codigo(String codigo) {
        this.codigo = codigo == null ? null : codigo.trim();
        return this;
    }

    /**
     * Asigna el nombre del paciente.
     *
     * @param nombrePaciente nombre completo
     * @return este builder
     */
    public ExamenSangreBuilder nombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente == null ? null : nombrePaciente.trim();
        return this;
    }

    /**
     * Asigna el costo del examen.
     *
     * @param costo valor mayor que cero
     * @return este builder
     */
    public ExamenSangreBuilder costo(double costo) {
        this.costo = costo;
        return this;
    }

    /**
     * Asigna el grupo sanguineo.
     *
     * @param tipoSangre grupo ABO
     * @return este builder
     */
    public ExamenSangreBuilder tipoSangre(TipoSangre tipoSangre) {
        this.tipoSangre = tipoSangre;
        return this;
    }

    /**
     * Asigna el factor RH.
     *
     * @param factorRh factor positivo o negativo
     * @return este builder
     */
    public ExamenSangreBuilder factorRh(FactorRh factorRh) {
        this.factorRh = factorRh;
        return this;
    }

    /**
     * Valida las reglas de negocio y construye el examen.
     *
     * @return examen valido e inmutable
     * @throws ExamenValidacionException si algun dato no cumple las reglas
     */
    public ExamenSangre build() {
        if (codigo == null || codigo.isEmpty()) {
            throw new ExamenValidacionException("El codigo del examen es obligatorio.");
        }
        if (nombrePaciente == null || nombrePaciente.length() < LONGITUD_MINIMA_NOMBRE) {
            throw new ExamenValidacionException(
                    "El nombre del paciente debe tener al menos "
                    + LONGITUD_MINIMA_NOMBRE + " caracteres.");
        }
        if (Double.isNaN(costo) || Double.isInfinite(costo) || costo <= 0) {
            throw new ExamenValidacionException("El costo debe ser un numero mayor que cero.");
        }
        if (tipoSangre == null) {
            throw new ExamenValidacionException("Debe seleccionar el tipo de sangre.");
        }
        if (factorRh == null) {
            throw new ExamenValidacionException("Debe seleccionar el factor RH.");
        }
        return new ExamenSangre(this);
    }

    String getCodigo() {
        return codigo;
    }

    String getNombrePaciente() {
        return nombrePaciente;
    }

    double getCosto() {
        return costo;
    }

    TipoSangre getTipoSangre() {
        return tipoSangre;
    }

    FactorRh getFactorRh() {
        return factorRh;
    }
}
