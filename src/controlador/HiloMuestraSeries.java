package controlador;

import java.util.ArrayList;
import vista.InterfazVista;

/**
 * Este hilo solo se encarga de mostrar la serie en una vista.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class HiloMuestraSeries implements Runnable {

    private final int TIEMPO_ESPERA;
    private InterfazVista vista;
    private Juego controlador;
    private ArrayList<Byte> serie;

    /**
     * Muestra la serie obtenida del controlador en la vista especificada.
     *
     * @param vista La vista en la que se mostrara la serie.
     * @param controlador El controlador del cual se obtendra la serie.
     */
    public HiloMuestraSeries(InterfazVista vista, Juego controlador) {
        this.vista = vista;
        this.controlador = controlador;
        this.serie = controlador.getSerie();

        this.TIEMPO_ESPERA = this.serie.size() > 10 ? 400 : 500;

        // Bloquear botones de la vista.
        this.vista.bloquearBotones(true, true);

        // Indicar al controlador que se va a mostrar la serie.
        this.controlador.setMostrandoValores(true);
    }

    /**
     * Muestra una breve animacion.
     *
     * @param vista La vista en la cual se mostrara la animacion.
     */
    public HiloMuestraSeries(InterfazVista vista) {
        this.TIEMPO_ESPERA = 100;
        this.vista = vista;
        this.vista.bloquearBotones(true, true);
        this.serie = null;
    }

    @Override
    public void run() {
        try {
            if (this.serie != null) {
                // Mostrar combinacion.
                for (byte valor : this.serie) {
                    this.vista.mostrarValorSerie(valor);
                    Thread.sleep(TIEMPO_ESPERA);
                    this.vista.mostrarValorSerie((byte) -1);
                    Thread.sleep(TIEMPO_ESPERA);
                }

                // Indicar al controlador que se ha terminado de mostrar la serie.
                this.controlador.setMostrandoValores(false);
                this.vista.bloquearBotones(false, true);

            } else {
                // Mostrar animacion (botones que se encienden uno detras de otro).
                byte[] arrayBotonesOrden = {0, 2, 3, 1};
                for (byte repeticiones = 0; repeticiones < 2; repeticiones++) {
                    for (byte boton = 0; boton < 4; boton++) {
                        this.vista.mostrarValorSerie(arrayBotonesOrden[boton]);
                        Thread.sleep(TIEMPO_ESPERA);
                        this.vista.mostrarValorSerie((byte) -1);
                    }
                }

                // Desbloquea los botones y pinta el fondo de color verde.
                this.vista.bloquearBotones(false, false);
                this.vista.resaltarFondoVerde();
            }
        } catch (InterruptedException ex) {
            System.out.println("ERROR - Al hacer sleep con el HiloMuestraSeries");
        }
    }

}
