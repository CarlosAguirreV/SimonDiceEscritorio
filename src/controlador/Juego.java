package controlador;

import java.util.ArrayList;

/**
 * Clase abstracta del juego de Simon. Incorpora los elementos comunes del juego
 * individual y multijugador.
 *
 * @author Carlos Aguirre Vozmediano
 */
public abstract class Juego {

    protected final static byte NUMERO_TECLAS = 4;
    protected final static int PUNTOS_POR_ACIERTO = 10;
    protected final static String[] COLORES = {"Verde", "Rojo", "Amarillo", "Azul"};
    protected int puntosActuales, recordAnterior, contadorToques;
    protected ArrayList<Byte> serie;
    protected boolean mostrandoValores;

    /**
     * Constructor, define los atributos con un estado correcto.
     */
    public Juego() {
        this.puntosActuales = 0;
        this.recordAnterior = 0;
        this.contadorToques = 0;
        this.serie = new ArrayList();
        this.mostrandoValores = false;
    }

    /**
     * Obtiene los puntos actuales.
     *
     * @return Puntuacion actual.
     */
    public int getPuntosActuales() {
        return this.puntosActuales;
    }

    /**
     * Informa de si se ha superado o no el record anterior.
     *
     * @return true si se ha superado el record, false si no.
     */
    public boolean isNuevoRecord() {
        return this.puntosActuales > this.recordAnterior;
    }

    /**
     * Obtiene los toques que faltan hasta completar la serie.
     *
     * @return Valor entero que representa el numero de toques que faltan.
     */
    public int getToquesRestantes() {
        return this.serie.size() - this.contadorToques;
    }

    /**
     * Obtiene la combinacion de teclas seleccionadas.
     *
     * @return ArrayList de Byte.
     */
    public ArrayList<Byte> getSerie() {
        return this.serie;
    }

    /**
     * Indica si se le esta mostrando la serie al jugador.
     *
     * @return True si se esta mostrando la serie actualmente, False en caso
     * contrario.
     */
    public boolean isMostrandoValores() {
        return this.mostrandoValores;
    }

    /**
     * Indica si se estan mostrando la serie al jugador actualmemte.
     *
     * @param valor True si se esta mostrando la serie actualmente, False en
     * caso contrario.
     */
    public void setMostrandoValores(boolean valor) {
        this.mostrandoValores = valor;
    }

    /**
     * Carga el record anterior de la BD.
     */
    protected abstract void cargarDatos();

    /**
     * Guarda todos los datos en la BD.
     */
    protected abstract void guardarDatos();

    /**
     * Reinicia todos los valores para permitir hechar otra partida.
     */
    protected abstract void reiniciar();

    /**
     * Metodo toString sobreescrito. Obtiene un breve resumen del juego.
     *
     * @return Cadena con el resumen del juego.
     */
    @Override
    public String toString() {
        return "Estado partida:\n - Puntos actuales: " + this.puntosActuales
                + "\n - Record anterior: " + this.recordAnterior
                + "\n - Combinacion actual: " + this.serie.toString()
                + "\n - Toques restantes: " + this.getToquesRestantes();
    }
}
