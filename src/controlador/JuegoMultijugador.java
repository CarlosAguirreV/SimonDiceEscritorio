package controlador;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import modelo.POJOmultijugador;
import vista.VistaCrear;
import vista.VistaMultijugador;
import vista.VistaUnirse;

/**
 * Clase preparada para jugar al juego de Simon con otro jugador. Instrucciones:
 * Servidor - No hay que hacer nada mas que crear el objeto. Cliente - 1 Crear
 * objeto, 2 intentarConectar al cual hay que pasarle la IP del jugador.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class JuegoMultijugador extends Juego {

    public static final byte LIMITE_NOMBRE = 8;
    public static final byte FIN_PARTIDA = -1;
    public static final byte NADA = -2;
    public static final String NOMBRE_POR_DEFECTO = "Anonimo";
    private boolean servidor, meToca;
    private String nombreJugador, nombreContrincante, ip;
    private int numVictorias;
    private int numDerrotas;
    private byte valorNuevoPulsado;
    private boolean finPartida;
    private boolean conectado;
    private VistaUnirse vistaUnirse;
    private VistaCrear vistaCrear;
    private VistaMultijugador vistaMultijugador;
    private HiloMultijugador objHilo;

    /**
     * Contructor de la clase.
     *
     * @param esServidor Identifica si es un servidor o un cliente.
     */
    public JuegoMultijugador(boolean esServidor) {
        this.servidor = esServidor;
        this.setNombreJugador(null);
        this.numVictorias = 0;
        this.numDerrotas = 0;
        this.valorNuevoPulsado = NADA; // Valor vacio.
        this.finPartida = false;
        this.conectado = false;

        if (esServidor) {
            this.meToca = true;
            this.ip = this.getMiIp();
        } else {
            this.meToca = false;
            this.ip = "";
        }

        super.serie = new ArrayList();

        this.cargarDatos();
    }

    /**
     * Asigna la vista que va a controlar este controlador.
     *
     * @param vista La VistaMultijugador que se va a controlar.
     */
    public void setVistaMultijugador(VistaMultijugador vista) {
        this.vistaMultijugador = vista;

        // Pasarle los datos a la vista para que los muestre.
        this.vistaMultijugador.mostrarNombreOponente(this.nombreContrincante);
        this.vistaMultijugador.mostrarMiNombre(this.nombreJugador);
        this.refrescarValoresVista();

        // Si eres el cliente se te bloquean los botones.
        this.vistaMultijugador.bloquearBotones(!this.servidor, true);

        // Si eres servidor mostrar un mensaje pidiendo un valor.
        if (this.servidor) {
            this.vistaMultijugador.resaltarFondoVerde();
            this.vistaMultijugador.mostrarMensajeInferior("Pulsa algo");
        } else {
            this.vistaMultijugador.mostrarMensajeInferior("Espera al otro jugador");
        }
    }

    /**
     * Si eres cliente podras definir la vista unirse.
     *
     * @param vista El objeto VistaUnirse (solo cliente).
     */
    public void setVistaUnirse(VistaUnirse vista) {
        this.vistaUnirse = !this.servidor ? vista : null;
    }

    /**
     * Si eres servidor podras definir la vista crear.
     *
     * @param vista El objeto VistaCrear (solo servidor).
     */
    public void setVistaCrear(VistaCrear vista) {
        this.vistaCrear = this.servidor ? vista : null;
    }

    /**
     * Permite jugar al juego de simon en modo multijugador. Si se acierta toda
     * la serie se pedira un nuevo valor que sera mandado via red. Si se falla
     * se definira el final de la partida. Este metodo es llamado desde los
     * botones de la vista.
     *
     * @param valorPulsado El valor que se ha pulsado.
     */
    public void jugar(byte valorPulsado) {
        if (super.serie.size() == super.contadorToques) {
            // Si el tamanio de la serie coincide con el indice de toques entonces el siguiente valor se mandara por red.
            System.out.println("Paso 1 (Te toca) - Pulsado valor nuevo. Valor: " + valorPulsado);
            this.pulsarValorNuevo(valorPulsado);
        } else {
            if (valorPulsado == serie.get(super.contadorToques)) {
                // Si aciertas.
                this.puntosActuales += PUNTOS_POR_ACIERTO;

                // Si el tamanio de la rerie coincide con el indice de toques pide un nuevo valor.
                if (super.serie.size() == ++super.contadorToques) {
                    // Crea un hilo para mostrar una pequenia animacion.
                    HiloMuestraSeries objHilo = new HiloMuestraSeries(this.vistaMultijugador);
                    Thread hilo = new Thread(objHilo);
                    hilo.start();

                    this.vistaMultijugador.mostrarMensajeInferior("Pulsa un nuevo valor");
                }

                // Refrescar valores de la vista.
                this.refrescarValoresVista();

            } else {
                // Si fallas.
                System.out.println("Paso 1 (Te toca) - Te has equivocado.");

                // Envia por red el valor -1 para informar de que se ha terminado la partida.
                this.pulsarValorNuevo(FIN_PARTIDA);

                // Mostrar el un mensaje desde la vista.
                this.vistaMultijugador.mostrarMensajeCorrecto(
                        "<html><h1>Fin del juego</h1><hr/>Tocaba "
                        + super.COLORES[serie.get(super.contadorToques)]
                        + " pero pulsaste " + super.COLORES[valorPulsado]
                        + "<h3>Tu puntuacion es: "
                        + this.puntosActuales + "</h3>"
                        + (super.isNuevoRecord() ? "<h3>¡Has batido un nuevo record!</h3>" : "")
                        + "</html>");

                this.finalizarPartidaMultijugador();
            }
        }
    }

    // Este metodo es llamado en cuanto se acierte la combinacion principal.
    private void pulsarValorNuevo(byte valorNuevoPulsado) {
        if (this.meToca) {
            this.valorNuevoPulsado = valorNuevoPulsado;

            // Solo se almacena si es distinto de -1 (fin de partida).
            if (valorNuevoPulsado != FIN_PARTIDA) {
                this.serie.add(this.valorNuevoPulsado);
            }

            // Bloquear los botones
            this.vistaMultijugador.bloquearBotones(true, true);

            System.out.println("Paso 2 (Te toca) - Definido el valor nuevo y botones bloqueados.");
        }
    }

    /**
     * Metodo que define que se ha terminado la partida. Si has ganado te
     * muestra un mensaje y te envia al menu principal. Este metodo solo es
     * llamado desde el hilo.
     *
     * @param heGanado Define si has ganado o no.
     */
    protected synchronized void finPartidaGanaste(boolean heGanado) {
        this.finPartida = true;

        System.out.println("\nPaso final - Fin de la partida. " + (heGanado ? "Ganas tu." : "Has perdido."));

        // Lo que ocurre si has ganado al oponente.
        if (heGanado) {
            // Mostrar mensaje de victoria.
            this.vistaMultijugador.mostrarMensajeCorrecto("<html><h1>¡¡Enhorabuena, has ganado!!</h1><hr/>Se acabo la partida. <h3>Tu puntuacion es: "
                    + super.puntosActuales + "</h3>"
                    + (super.isNuevoRecord() ? "<h3>¡Has batido un nuevo record!</h3>" : "")
                    + "</html>");

            this.numVictorias += 1;

            this.finalizarPartidaMultijugador();
        }
    }

    // Esto es lo que sucedera al terminar la partida.
    private void finalizarPartidaMultijugador() {
        // Si se ha batido un nuevo record este se almacena.
        if (super.isNuevoRecord()) {
            System.out.println("Nuevo record!!!");
            this.recordAnterior = this.puntosActuales;
        }

        // Guardar registro en la BD.
        this.guardarDatos();

        // Volver a las pantallas anteriores.
        if (this.servidor) {
            new VistaCrear(this.nombreJugador);
        } else {
            new VistaUnirse(this.ip, this.nombreJugador);
        }

        this.vistaMultijugador.dispose();
    }

    /**
     * Define si es el fin de la partida.
     *
     * @return true Si es el final, false si no lo es.
     */
    protected synchronized boolean isFinPartida() {
        return this.finPartida;
    }

    /**
     * Con este metodo sabras si estas o no conectado con el otro jugador.
     *
     * @return true Si estas conectado, false si no.
     */
    protected boolean isConectado() {
        return this.conectado;
    }

    /**
     * Indica si este jugador esta o no conectado. En caso de estarlo avisara
     * tambien a las respectivas vistas de conexion para que se cierren. Este
     * metodo es llamado desde el HiloMultijugador.
     *
     * @param estoyConectado true esta conectado, false no.
     */
    protected synchronized void setConectado(boolean estoyConectado) {
        this.conectado = estoyConectado;

        // Comunicarle a la vistaUnirse el resultado de la conexion si eres cliente y con la vistaCrear si eres servidor.
        if (!this.servidor && this.vistaUnirse != null) {
            this.vistaUnirse.estoyConectado(this.conectado);
        } else if (this.servidor && this.vistaCrear != null) {
            this.vistaCrear.estoyConectado(this.conectado);
        }

        // Si estoy conectado me desvinculo de las vistas unirse y crear, ya que no las voy a usar mas.
        if (estoyConectado) {
            this.vistaCrear = null;
            this.vistaUnirse = null;
        }
    }

    /**
     * El hilo multijugador recoge el valor nuevo pulsado. Este metodo es
     * llamado con cierta frecuencia desde el HiloMultijugador para comprobar si
     * hay un nuevo valor.
     *
     * @return El valor nuevo pulsado.
     */
    protected synchronized byte removeNuevoValor() {
        if (this.meToca && this.valorNuevoPulsado != NADA) {
            byte valorTemporal = this.valorNuevoPulsado;
            this.valorNuevoPulsado = NADA;

            System.out.println("Paso 3 (Te toca) - Se recoge el valor nuevo y a continuacion se cambia el turno.");

            return valorTemporal;
        } else {
            return NADA;
        }
    }

    /**
     * Recibe un nuevo valor por la red. Este metodo es llamado por
     * HiloMultijugador.
     *
     * @param valorNuevoObtenido El valor que se ha obtenido.
     */
    protected synchronized void addValorNuevo(byte valorNuevoObtenido) {
        if (!this.meToca) {
            System.out.println("Paso 6 (Espera) - Valor aniadido, se crea el hilo para que muestre la serie e indica que me toca.");

            // Aniadir el valor nuevo recibido.
            this.serie.add(valorNuevoObtenido);

            // Mostrar un mensaje antes de crear el hilo y mostrar la combinacion.
            this.vistaMultijugador.mostrarMensajeInferior("¡ATENTO!");
            this.vistaMultijugador.mostrarMensajeCorrecto("<html><h1>¡Atento!</h1>Pulsa en Aceptar cuando estes listo.</html>");
            this.vistaMultijugador.mostrarMensajeInferior("Repite la combinacion.");

            // Crea un hilo para mostrar la serie al jugador.
            System.out.println("Paso 7 (Espera) - Mostrando la serie en la vista. Serie completa: " + this.serie.toString() + "\n");
            HiloMuestraSeries objHilo = new HiloMuestraSeries(this.vistaMultijugador, this);
            Thread hilo = new Thread(objHilo);
            hilo.start();

            // Poner el indice de toques a 0 y refrescar la vista.
            super.contadorToques = 0;
            this.refrescarValoresVista();
        }
    }

    /**
     * Metodo que define si te toca o no.
     *
     * @param meToca True te toca, false no.
     */
    protected synchronized void setMeToca(boolean meToca) {
        this.meToca = meToca;
        this.vistaMultijugador.mostrarMensajeInferior(meToca ? "Te toca" : "Espera al otro jugador");
    }

    // Obtiene la ip propia del servidor.
    private String getMiIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            return "N/A";
        }
    }

    /**
     * Crea la partida. Espera a que otro jugador se una a la partida para
     * iniciarla. Solo para servidor.
     */
    public void crearPartida() {
        if (this.servidor) {
            System.out.println("Esperando al otro jugador...");
            System.out.println("Tu ip es: " + this.getIp());

            this.crearHiloRed();
        }
    }

    /**
     * Permite conectarse al servidor (Unirse a la partida). Solo para clientes.
     *
     * @param ipServidor La ip del servidor.
     * @return true si la IP esta bien.
     */
    public boolean intentarConectar(String ipServidor) {
        if (!this.servidor && this.setIp(ipServidor)) {
            this.crearHiloRed();
            return true;
        }
        return false;
    }

    // Crea un hilo para que se encargue de las funciones de red.
    private void crearHiloRed() {
        this.objHilo = new HiloMultijugador(this);
        Thread hiloMultijugador = new Thread(this.objHilo);
        hiloMultijugador.start();
    }

    @Override
    protected void cargarDatos() {
        ConectorMultijugador controlBD = new ConectorMultijugador();
        this.recordAnterior = controlBD.getPuntosMaximos();
        controlBD.desconectar();
    }

    @Override
    protected void guardarDatos() {
        ConectorMultijugador controlBD = new ConectorMultijugador();
        POJOmultijugador datos = new POJOmultijugador(super.puntosActuales, this.nombreContrincante, this.getResultado());
        controlBD.guardarRegistro(datos);
        controlBD.desconectar();
    }

    @Override
    protected void reiniciar() {
        super.puntosActuales = 0;
        super.serie.clear();
    }

    /**
     * Especifica si es o no tu turno.
     *
     * @return true si te toca, false si no.
     */
    protected boolean meToca() {
        return this.meToca;
    }

    /**
     * Permite poner un nombre al jugador. Si se le pasa una cadena vacia o a
     * null por defecto el nombre es "Anonimo".
     *
     * @param nombre El nombre del jugador.
     */
    public void setNombreJugador(String nombre) {
        nombre = nombre == null || nombre.trim().length() == 0 ? NOMBRE_POR_DEFECTO : nombre;
        this.nombreJugador = nombre.trim().length() > LIMITE_NOMBRE ? nombre.substring(0, LIMITE_NOMBRE) : nombre.trim();
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return Cadena con el nombre del jugador.
     */
    public String getNombreJugador() {
        return this.nombreJugador;
    }

    /**
     * Define el nombre del contrincante.
     *
     * @param nombre Cadena con el nombre del contrincante.
     */
    public synchronized void setNombreContrincante(String nombre) {
        this.nombreContrincante = nombre;
    }

    /**
     * Obtiene el nombre del contrincante.
     *
     * @return Cadena con el nombre del contrincante.
     */
    public String getNombreContrincante() {
        return this.nombreContrincante;
    }

    /**
     * Define la IP del servidor.Solo sirve si eres el cliente, que intenta
     * conectarse con el servidor. Hace una pequenia comprobacion para saber si
     * lo que introduces se parece a una IP.
     *
     * @param ipServidor La ip del servidor.
     * @return true si es pasable, false si no lo es.
     */
    private boolean setIp(String ipServidor) {
        if ((this.servidor || ipServidor == null || ipServidor.split("\\.").length != 4) && !ipServidor.equals("localhost")) {
            return false;
        } else {
            this.ip = ipServidor;
            return true;
        }
    }

    /**
     * Obtiene la ip del servidor. Valido tanto para cliente como servidor. En
     * otras palabras, para el servidor es su IP propia y para el cliente
     * representa la IP del servidor al cual se conectara.
     *
     * @return La ip del servidor.
     */
    public String getIp() {
        return this.ip;
    }

    /**
     * Obtiene el numero de victorias. En otras palabras, las que has ido
     * acumulando al jugar contra tu oponente.
     *
     * @return Numero de victorias.
     */
    public int getNumeroVictorias() {
        return this.numVictorias;
    }

    /**
     * Obtiene el numero de derrotas. En otras palabras, las que has ido
     * acumulando al jugar contra tu oponente.
     *
     * @return Numero de derrotas.
     */
    public int getNumeroDerrotas() {
        return this.numDerrotas;
    }

    /**
     * Obtiene el resultado de la partida.
     *
     * @return perdiste(0), ganaste(1) o quedaste empate(2).
     */
    public byte getResultado() {
        if (this.numVictorias > this.numDerrotas) {
            return 1;
        } else if (this.numDerrotas > this.numVictorias) {
            return 2;
        } else {
            return 0;
        }
    }

    /**
     * Obtiene como respuesta si este jugador es o no un servidor.
     *
     * @return true si soy servidor, false en caso contrario.
     */
    public synchronized boolean isServidor() {
        return servidor;
    }

    /**
     * Cierra la conexion.
     */
    public void cerrarConexion() {
        if (this.objHilo != null) {
            this.objHilo.cerrarConexion();
        }
    }

    // Refresca los valores del panel superior de la vista multijugador.
    private void refrescarValoresVista() {
        if (this.vistaMultijugador != null) {
            this.vistaMultijugador.mostrarRecord(this.recordAnterior);
            this.vistaMultijugador.mostrarPuntos(super.getPuntosActuales());
            this.vistaMultijugador.mostrarToquesRestantes(super.getToquesRestantes());
        }
    }

    /**
     * El hilo informa de que se ha cerrado la conexion.
     */
    public synchronized void seCerroLaConexion() {
        if (this.vistaMultijugador != null) {
            this.vistaMultijugador.mostrarMensajeCorrecto("<html><h1>Se ha cerrado la conexion.</h1>Se volvera a la ventana de conexion.</html>");

            // Volver a las pantallas anteriores.
            if (this.servidor) {
                new VistaCrear(this.nombreJugador);
            } else {
                new VistaUnirse(this.ip, this.nombreJugador);
            }

            this.vistaMultijugador.dispose();
        }
    }
}
