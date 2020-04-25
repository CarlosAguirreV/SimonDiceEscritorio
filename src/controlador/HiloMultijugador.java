package controlador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Este se encarga de conectar con el otro jugador, enviar y recibir los datos
 * durante el transcurso de la partida.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class HiloMultijugador implements Runnable {

    private final int PUERTO = 3000;
    private JuegoMultijugador controlador;
    private PrintWriter emisor;
    private BufferedReader receptor;
    private ServerSocket socketServidor;
    private Socket socketCliente;
    private boolean conexionCerradaAposta;

    /**
     * Contructor, requiere el controlador para realizar las acciones.
     *
     * @param controlador El controlador del JuegoMultijugador.
     */
    public HiloMultijugador(JuegoMultijugador controlador) {
        this.controlador = controlador;
        this.conexionCerradaAposta = false;
    }

    @Override
    public void run() {
        try {
            if (this.controlador.isServidor()) {
                // --------------------------------- SERVIDOR ---------------------------------

                // Crear el socket servidor, aceptar una sola peticion y preparar todo para poder enviar y recibir datos.
                this.socketServidor = new ServerSocket(PUERTO);
                Socket socket = this.socketServidor.accept();
                this.crearEmisorReceptor(socket);

                // Enviar el nombre del jugador y obtener el nombre del contrincante.
                emisor.println(this.controlador.getNombreJugador());
                this.controlador.setNombreContrincante(receptor.readLine());

                // Si ha llegado hasta aqui es porque se ha conectado con el otro jugador.
                this.controlador.setConectado(true);

                // Llamar al metodo de envio y recibo de datos.
                this.control();

                // Cerrar todo.
                this.emisor.close();
                this.receptor.close();
                this.socketServidor.close();

            } else {
                // --------------------------------- CLIENTE ---------------------------------

                // Crear el socket cliente y preparar todo para poder enviar y recibir datos.
                this.socketCliente = new Socket(this.controlador.getIp(), PUERTO);
                this.crearEmisorReceptor(this.socketCliente);

                // Enviar el nombre del jugador y obtener el nombre del contrincante.
                this.controlador.setNombreContrincante(receptor.readLine());
                emisor.println(this.controlador.getNombreJugador());

                // Si ha llegado hasta aqui es porque se ha conectado con el otro jugador.
                this.controlador.setConectado(true);

                // Llamar al metodo de envio y recibo de datos.
                this.control();

                // Cerrar todo.
                this.emisor.close();
                this.receptor.close();
                this.socketCliente.close();

            }
        } catch (IOException ex) {
            System.out.println("Error de entrada salida con el socket." + ex.getMessage());
            if (!this.conexionCerradaAposta) {
                this.cerrarConexion();
                this.controlador.seCerroLaConexion();
            }

        } catch (InterruptedException ex) {
            System.out.println("Error al hacer sleep." + ex.getMessage());
        }

        // Cuando termine la partida avisa al padre de que se ha desconectado.
        this.controlador.setConectado(false);

        System.out.println("Hilo multijugador cerrado correctamente.");
    }

    // Metodo auxiliar. Crea el emisor y el receptor de datos del cliente y servidor.
    private void crearEmisorReceptor(Socket socket) throws IOException {
        this.emisor = new PrintWriter(socket.getOutputStream(), true);
        this.receptor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    // Permite enviar datos entre los jugadores.
    // Mientras uno escucha el otro espera a que el contrincante acierte la combinacion y pulse un valor nuevo para poder mandarlo.
    private void control() throws InterruptedException, IOException {

        // Mientras que no sea fin de partida.
        while (!this.controlador.isFinPartida()) {
            if (this.controlador.meToca()) {
                // --------------------------------- TE TOCA ---------------------------------
                // Esperando a que el contrincante pulse algo nuevo.
                byte valorNuevoPulsado = JuegoMultijugador.NADA;

                // Se queda en bucle hasta que en el controlador exista un nuevo valor pulsado.
                while (valorNuevoPulsado == JuegoMultijugador.NADA) {

                    // Recoge el valor pulsado.
                    valorNuevoPulsado = this.controlador.removeNuevoValor();

                    // Si el valor nuevo es mayor que nada (que se ha pulsado algo nuevo o es fin de partida (-1))...
                    if (valorNuevoPulsado > JuegoMultijugador.NADA) {

                        // Se cambia el turno.
                        this.controlador.setMeToca(false);

                        // Manda al contrincante el nuevo valor pulsado o (-1, FIN_PARTIDA).
                        emisor.println(Byte.toString(valorNuevoPulsado));
                        System.out.println("Paso 4 (Te toca) - El valor se manda via red.");

                        // Si el nuevo valor pulsado resulta ser un -1 es porque ha fallado o bien la red o bien la combinacion, equivale a fin de partida.
                        if (valorNuevoPulsado == JuegoMultijugador.FIN_PARTIDA) {
                            this.controlador.finPartidaGanaste(false);
                        }
                    }

                    // Hacer que el hilo descanse un rato antes de volver a comprobar si se ha pulsado un nuevo valor.
                    Thread.sleep(300);
                }

            } else {
                // --------------------------------- NO TE TOCA ---------------------------------
                // Escuchando una respuesta.
                String cadenaRecibida = receptor.readLine();

                if (cadenaRecibida != null) {
                    // Si la cadena recibida no es null entonces se trata de un valor valido y por lo cual puede convertirse a byte.
                    byte valorRecibido = Byte.parseByte(cadenaRecibida);
                    System.out.println("Paso 5 (Espera) - Valor recibido: " + valorRecibido); // BORRAR

                    // Si el valor recibido es un -1 es que es el fin de la partida, en caso contrario aniade el nuevo valor y cambia el turno.
                    if (valorRecibido == JuegoMultijugador.FIN_PARTIDA) {
                        this.controlador.finPartidaGanaste(true);
                    } else {
                        // Aniade el nuevo valor recibido a la serie.
                        this.controlador.addValorNuevo(valorRecibido);

                        // Indicar que te toca.
                        this.controlador.setMeToca(true);
                    }
                } else {
                    // Si la cadena recibida es null es porque se ha cerrado la conexion por parte del emisor.
                    this.cerrarConexion();
                    this.controlador.seCerroLaConexion();
                }
            }
        }
    }

    /**
     * Cierra la conexion de forma segura. Este se ejecutara cuando se pulse el
     * boton de desconectar en la vista, es llamado desde el controlador.
     * Fuerza a que se de el error de entrada salida el cual esta controlado.
     */
    public void cerrarConexion() {
        try {
            this.conexionCerradaAposta = true;

            if (this.emisor != null) {
                this.emisor.close();
            }

            if (this.receptor != null) {
                this.receptor.close();
            }

            if (this.socketServidor != null) {
                this.socketServidor.close();
            }
            System.out.println("Se ha cerrado la conexion.");
            this.controlador.setConectado(false);

        } catch (IOException ex) {
            System.out.println("Error de entrada salida con el socket." + ex.getMessage());
        }
    }

}
