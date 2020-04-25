package modelo;

import java.time.LocalDate;

/**
 * Clase POJO que representa un registro del modo multijugador.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class POJOmultijugador {

    private int id, puntos;
    private String fecha, contrincante;
    private byte ganaste;
    protected static final String[] cadenas = {"Perdiste", "Ganaste", "Empate"};

    /**
     * Constructor vacio.
     */
    public POJOmultijugador() {
        LocalDate fechaLocal = LocalDate.now();
        this.fecha = fechaLocal.getDayOfMonth() + "-" + fechaLocal.getMonthValue() + "-" + fechaLocal.getYear();
    }

    /**
     * Constructor incompleto que excluye el id. Se usa principalmente para
     * posteriormente almacenarlo en la BD. Ya que el ID se auto incrementa.
     *
     * @param puntos Record multijugador.
     * @param contrincante Nombre del contrincante.
     * @param ganaste Define si perdiste(0), ganaste(1) o quedaste empate(2).
     */
    public POJOmultijugador(int puntos, String contrincante, byte ganaste) {
        this();
        this.puntos = puntos;
        this.contrincante = contrincante;
        this.setGanaste(ganaste);
    }

    /**
     * Constructor completo. Usado principalmente para crear un objeto completo.
     *
     * @param id Identificador unico del registro.
     * @param puntos Record multijugador.
     * @param fecha Fecha en que se consiguio.
     * @param contrincante Nombre del contrincante.
     * @param ganaste Define si ganaste o perdiste.
     */
    public POJOmultijugador(int id, int puntos, String fecha, String contrincante, byte ganaste) {
        this.id = id;
        this.puntos = puntos;
        this.fecha = fecha;
        this.contrincante = contrincante;
        this.setGanaste(ganaste);
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
     * Obtiene los puntos de esta partida.
     *
     * @return Los puntos.
     */
    public int getPuntos() {
        return puntos;
    }

    /**
     * Define los puntos ganados en la partida.
     *
     * @param puntos Puntos ganados.
     */
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    /**
     * Obtiene la fecha en que se hizo esta jugada.
     *
     * @return Cadena de texto con la fecha.
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Define la fecha de la jugada, esta ha de ser una cadena de texto.
     *
     * @param fecha Cadena de texto con la fecha.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el nombre del contrincante.
     *
     * @return Nombre del contrincante.
     */
    public String getContrincante() {
        return contrincante;
    }

    /**
     * Define el nombre del contrincante.
     *
     * @param contrincante Nombre del contrincante.
     */
    public void setContrincante(String contrincante) {
        this.contrincante = contrincante;
    }

    /**
     * Define si ganaste o perdiste esta partida.
     *
     * @return perdiste(0), ganaste(1) o quedaste empate(2).
     */
    public byte getGanaste() {
        return ganaste;
    }

    /**
     * Definir si ganaste en esta partida.
     *
     * @param ganaste perdiste(0), ganaste(1) o quedaste empate(2).
     */
    public void setGanaste(byte ganaste) {
        this.ganaste = ganaste > -1 && ganaste < 4 ? ganaste : 0;
    }

    /**
     * Metodo toString sobreescrito. Muestra un resumen rapido del objeto.
     *
     * @return Cadena con un breve resumen de este objeto.
     */
    @Override
    public String toString() {
        return "POJOmultijugador{" + "id=" + id + ", puntos=" + puntos + ", fecha=" + fecha + ", contrincante=" + contrincante + ", ganaste=" + cadenas[ganaste] + '}';
    }
}
