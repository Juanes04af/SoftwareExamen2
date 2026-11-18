package co.edu.poli.examen2.dominio.modelo;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

/**
 * Entidad de dominio que representa un examen de sangre.
 *
 * <p>Es inmutable y solo puede crearse mediante {@link ExamenSangreBuilder}
 * (patron Builder), que valida las reglas de negocio antes de construirla.
 * Por eso su constructor es de paquete.</p>
 *
 * @author Juan Rosero
 */
public final class ExamenSangre implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Codigo unico del examen, por ejemplo EXM-001. */
    private final String codigo;

    /** Nombre completo del paciente. */
    private final String nombrePaciente;

    /** Costo del examen, siempre mayor que cero. */
    private final double costo;

    /** Grupo sanguineo del paciente. */
    private final TipoSangre tipoSangre;

    /** Factor RH del paciente. */
    private final FactorRh factorRh;

    /**
     * Construye el examen. Solo lo invoca {@link ExamenSangreBuilder#build()}.
     *
     * @param builder builder con los datos ya validados
     */
    ExamenSangre(ExamenSangreBuilder builder) {
        this.codigo = builder.getCodigo();
        this.nombrePaciente = builder.getNombrePaciente();
        this.costo = builder.getCosto();
        this.tipoSangre = builder.getTipoSangre();
        this.factorRh = builder.getFactorRh();
    }

    /**
     * @return codigo del examen
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @return nombre del paciente
     */
    public String getNombrePaciente() {
        return nombrePaciente;
    }

    /**
     * @return costo del examen
     */
    public double getCosto() {
        return costo;
    }

    /**
     * @return tipo de sangre
     */
    public TipoSangre getTipoSangre() {
        return tipoSangre;
    }

    /**
     * @return factor RH
     */
    public FactorRh getFactorRh() {
        return factorRh;
    }

    /**
     * Devuelve el grupo sanguineo completo, por ejemplo "AB+".
     *
     * @return grupo y factor juntos
     */
    public String getGrupoSanguineo() {
        return tipoSangre.name() + factorRh.getSimbolo();
    }

    /**
     * Dos examenes son iguales si tienen el mismo codigo.
     *
     * @param o objeto a comparar
     * @return {@code true} si los codigos coinciden
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamenSangre)) {
            return false;
        }
        return codigo.equals(((ExamenSangre) o).codigo);
    }

    /**
     * @return hash basado en el codigo
     */
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    /**
     * Representacion legible usada en el listado de la interfaz.
     *
     * @return texto con los datos del examen
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s | %s | %s | $%.2f",
                codigo, nombrePaciente, getGrupoSanguineo(), costo);
    }
}
