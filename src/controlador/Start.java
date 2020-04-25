package controlador;

import vista.VistaMenuPrincipal;

/**
 * Metodo principal. Por defecto ejecuta el menu principal. IMPORTANTE: Si
 * ejecuta esta aplicacion desde el IDE NetBeans tal vez diga que falta cierta
 * librería, esta se encuentra en la misma carpeta del proyecto y se llama
 * sqlite-jdbc-3.27.2.1.jar no hay mayor complicacion.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class Start {

    public static void main(String[] args) {
        // Crea la vista del menu principal, despues cuando se haga clic en algun elemento, cada elemento creara a su respectivo controlador.
        new VistaMenuPrincipal();
    }

}
