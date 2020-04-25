package vista;

import java.awt.Color;

/**
 * Interfaz que recoge los metodos comunes que han de tener las vistas de los
 * juegos. Excluyendo los basicos.
 *
 * @author Carlos Aguirre Vozmediano
 */
public interface InterfazVista {

    public void mostrarRecord(int record);

    public void mostrarPuntos(int puntos);

    public void mostrarToquesRestantes(int numToques);

    public void bloquearBotones(boolean bloqueado, boolean pintarFondo);

    public void mostrarValorSerie(byte valorActual);

    public void mostrarMensajeCorrecto(String mensaje);

    public void resaltarFondoVerde();
}
