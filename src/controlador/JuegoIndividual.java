package controlador;

import java.util.Random;
import modelo.POJOindividual;
import vista.VistaIndividual;
import vista.VistaMenuPrincipal;

/**
 * Clase preparada para jugar al juego de Simon un solo jugador.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class JuegoIndividual extends Juego {

    private Random rand;
    private VistaIndividual vista;

    /**
     * Constructor de la clase.
     *
     * @param vista Vista asociada a este juego.
     */
    public JuegoIndividual(VistaIndividual vista) {
        this.vista = vista;

        this.rand = new Random();

        this.cargarDatos();
        this.reiniciar();
    }

    /**
     * Permite jugar al juego de Simon.
     *
     * @param valorPulsado El valor que se haya pulsado.
     * @return true si la tecla pulsada es correcta, false si no lo es, en cuyo
     * caso se debera reiniciar todo.
     */
    public boolean jugar(byte valorPulsado) {
        if (valorPulsado == serie.get(super.contadorToques)) {
            // Si aciertas.
            this.puntosActuales += PUNTOS_POR_ACIERTO;

            if (super.serie.size() == ++super.contadorToques) {
                this.aniadirValorAleatorio();

                // Mostrar el un mensaje desde la vista.
                this.vista.mostrarMensajeCorrecto("<html><h1>¡¡Has acertado!!</h1><br/>Presta atencion a la combinacion.</html>");
            }

            // Refrescar valores de la vista.
            this.refrescarValoresVista();

            return true;

        } else {
            // Si fallas.
            System.out.println("Fin de la partida.");

            // Mostrar el un mensaje desde la vista.
            boolean respuesta = this.vista.mostrarMensajeConfirmacion("<html><h1>Fin del juego</h1><hr/>Tocaba "
                    + super.COLORES[serie.get(super.contadorToques)]
                    + " pero pulsaste " + super.COLORES[valorPulsado] + "."
                    + (this.isNuevoRecord() ? "<br/>Has batido un nuevo record:<h3>" + this.puntosActuales + " puntos.</h3>" : "")
                    + "<hr/>¿Deseas jugar de nuevo?</html>");

            // Si has batido un nuevo record.
            if (isNuevoRecord()) {
                System.out.println("Nuevo record!!!");
                this.guardarDatos();
                this.recordAnterior = this.puntosActuales;
            }

            if (respuesta) {
                this.reiniciar();
            } else {
                new VistaMenuPrincipal();
                this.vista.dispose();
            }

            return false;
        }
    }

    // Aniade un valor nuevo a la serie.
    // Se asegura de que un valor nunca pueda ser repetido mas de dos veces.
    private void aniadirValorAleatorio() {

        int tamanioSerie = super.serie.size();
        byte valorAleatorio = (byte) rand.nextInt(NUMERO_TECLAS);

        if (tamanioSerie >= 2) {
            // Si la serie tiene mas de dos valores comprueba que no se repitan, si lo hace se genera un valor nuevo, si no se deja el que esta.
            if (valorAleatorio == super.serie.get(tamanioSerie - 1) && valorAleatorio == super.serie.get(tamanioSerie - 2)) {
                byte valorNuevo = this.generarValorNuevoAleatorio(valorAleatorio);
                super.serie.add(valorNuevo);

            } else {
                // Como no se repite no pasa nada.
                super.serie.add(valorAleatorio);
            }

        } else {
            // Como son los primeros dos valores se puede aniadir el valor aleatorio a la serie sin problemas.
            super.serie.add(valorAleatorio);
        }

        // Definir el contador de toques a 0.
        super.contadorToques = 0;

        // Refrescar valores de la vista.
        this.refrescarValoresVista();

        // Crea un hilo para mostrar la serie al jugador.
        HiloMuestraSeries objHilo = new HiloMuestraSeries(this.vista, this);
        Thread hilo = new Thread(objHilo);
        hilo.start();
    }

    // En el caso en que se repitan los dos ultimos valores se llamara a este metodo el cual retorna un valor distinto. Evita mas de dos repeticiones seguidas.
    private byte generarValorNuevoAleatorio(byte valorAleatorioRepetido) {
        byte[] valoresNuevosPosibles = new byte[NUMERO_TECLAS - 1];

        byte indice = 0;
        for (byte i = 0; i < NUMERO_TECLAS; i++) {
            if (i == valorAleatorioRepetido) {
                continue;
            } else {
                valoresNuevosPosibles[indice++] = i;
            }
        }

        return valoresNuevosPosibles[this.rand.nextInt(NUMERO_TECLAS - 1)];
    }

    @Override
    protected void reiniciar() {
        super.puntosActuales = 0;
        super.serie.clear();
        this.aniadirValorAleatorio();

        // Muestra los valores en la vista.
        this.refrescarValoresVista();
    }

    @Override
    protected void guardarDatos() {
        ConectorIndividual controlBD = new ConectorIndividual();
        POJOindividual datos = new POJOindividual(this.puntosActuales);
        controlBD.guardarRegistro(datos);
        controlBD.desconectar();
    }

    @Override
    protected void cargarDatos() {
        ConectorIndividual controlBD = new ConectorIndividual();
        POJOindividual datos = controlBD.getUltimoRegistro();
        this.recordAnterior = datos.getRecord();
        controlBD.desconectar();
    }

    // Refresca los valores del panel superior de la vistaIndividual.
    private void refrescarValoresVista() {
        this.vista.mostrarRecord(this.recordAnterior);
        this.vista.mostrarPuntos(super.getPuntosActuales());
        this.vista.mostrarToquesRestantes(super.getToquesRestantes());
    }

}
