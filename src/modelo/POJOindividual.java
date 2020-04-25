package modelo;

import java.time.LocalDate;

/**
 * Clase POJO que representa un registro del modo individual.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class POJOindividual {

    private int id, record;
    private String fecha;

    /**
     * Constructor vacio. Pone la fecha automaticamente.
     */
    public POJOindividual() {
        LocalDate fechaLocal = LocalDate.now();
        this.fecha = fechaLocal.getDayOfMonth() + "-" + fechaLocal.getMonthValue() + "-" + fechaLocal.getYear();
    }

    /**
     * Constructor incompleto. Se usa principalmente para posteriormente
     * almacenarlo en la BD. Pone la fecha automaticamente.
     *
     * @param record Nuevo record.
     */
    public POJOindividual(int record) {
        this();
        this.record = record;
    }

    /**
     * Constructor completo. Usado principalmente para crear un objeto completo.
     *
     * @param id Identificador unico del registro.
     * @param record Nuevo record.
     * @param fecha Fecha en la cual se supero el record.
     */
    public POJOindividual(int id, int record, String fecha) {
        this.id = id;
        this.record = record;
        this.fecha = fecha;
    }

    /**
     * Obtiene el identificador unico del registro.
     *
     * @return Identificador.
     */
    public int getId() {
        return id;
    }

    /**
     * Permite establecer el id del registro.
     *
     * @param id Tipo entero que representa el id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el record de este registro.
     *
     * @return El record.
     */
    public int getRecord() {
        return record;
    }

    /**
     * Define el nuevo record de este registro.
     *
     * @param record Nuevo record.
     */
    public void setRecord(int record) {
        this.record = record;
    }

    /**
     * Obtiene la fecha en que se batio el record.
     *
     * @return La fecha representada en una cadena.
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Define la fecha en que se logro el nuevo record.
     *
     * @param fecha Fecha representada en una cadena.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Metodo toString sobreescrito. Muestra un resumen rapido del objeto.
     *
     * @return Cadena con un breve resumen de este objeto.
     */
    @Override
    public String toString() {
        return "POJOindividual{" + "id=" + id + ", record=" + record + ", fecha=" + fecha + '}';
    }
}
