package vista;

import controlador.JuegoMultijugador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Vista del juego de Simon multijugador.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class VistaMultijugador extends JFrame implements InterfazVista {

    private JPanel pnlGlobal, pnlCentral, pnlNorte, pnlSur;
    private JLabel lblRecord, lblNombreOponente, lblQuedan, lblPuntos, lblMiNombre, lblMensajeAbajo;
    private JButton btnRojo, btnAzul, btnAmarillo, btnVerde, btnDesconectar;
    private JButton[] coleccionBotones = {btnRojo, btnAzul, btnAmarillo, btnVerde};
    private String[] coleccionRutasImgOn = {"/recursos/imgBtnVerde", "/recursos/imgBtnRojo", "/recursos/imgBtnAmarillo", "/recursos/imgBtnAzul"};
    private final JuegoMultijugador CONTROLADOR;

    /**
     * Constructor de la clase multijugador.
     *
     * @param controlador Necesita el controlador.
     */
    public VistaMultijugador(JuegoMultijugador controlador) {
        // Pasos para la creacion de la ventana.
        this.crearElementos();
        this.crearDistribucion();
        this.colocarElementos();
        this.definirEstilos();
        this.eventos();

        // Propiedades de la ventana.
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);

        // Crear el controlador y pasarle esta vista.
        this.CONTROLADOR = controlador;
        this.CONTROLADOR.setVistaMultijugador(this);
        this.setTitle("Juego Simon Dice - Multijugador " + (CONTROLADOR.isServidor() ? "(Servidor)" : "(Cliente)"));
    }

    // Crea todos los elementos de la ventana.
    private void crearElementos() {
        this.pnlGlobal = new JPanel();
        this.pnlNorte = new JPanel();
        this.pnlCentral = new JPanel();
        this.pnlSur = new JPanel();
        this.lblRecord = new JLabel("...");
        this.lblNombreOponente = new JLabel("...");
        this.lblQuedan = new JLabel("...");
        this.lblPuntos = new JLabel("...");
        this.lblMiNombre = new JLabel("...");
        this.lblMensajeAbajo = new JLabel("...");
        this.btnDesconectar = new JButton("Desconectar");

        for (int i = 0; i < this.coleccionBotones.length; i++) {
            this.coleccionBotones[i] = new JButton();
            this.coleccionBotones[i].setName(Integer.toString(i));
            this.coleccionBotones[i].setIcon(new javax.swing.ImageIcon(getClass().getResource(this.coleccionRutasImgOn[i] + "Off.png")));
            this.coleccionBotones[i].setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(this.coleccionRutasImgOn[i] + "On.png")));
            this.coleccionBotones[i].setContentAreaFilled(false);
        }
    }

    // Define las distribuciones que ha de tener cada panel.
    private void crearDistribucion() {
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.pnlGlobal.setLayout(new BorderLayout());
        this.pnlNorte.setLayout(new GridLayout(3, 2));
        this.pnlCentral.setLayout(new GridLayout(2, 2, 0, 0));
        this.pnlSur.setLayout(new GridLayout(1, 2, 0, 0));
    }

    // Coloca cada elemento en su respectivo panel.
    private void colocarElementos() {
        this.getContentPane().add(this.pnlGlobal);
        this.pnlGlobal.add(this.pnlNorte, BorderLayout.NORTH);
        this.pnlGlobal.add(this.pnlCentral, BorderLayout.CENTER);
        this.pnlGlobal.add(this.pnlSur, BorderLayout.SOUTH);

        this.pnlNorte.add(this.lblMiNombre);
        this.pnlNorte.add(this.lblNombreOponente);
        this.pnlNorte.add(this.lblRecord);
        this.pnlNorte.add(this.lblPuntos);
        this.pnlNorte.add(this.lblQuedan);
        this.pnlNorte.add(new JLabel()); // Relleno

        for (JButton btn : this.coleccionBotones) {
            this.pnlCentral.add(btn);
        }

        this.pnlSur.add(this.lblMensajeAbajo);
        this.pnlSur.add(this.btnDesconectar);
    }

    // Define los estilos de todos los elementos.
    private void definirEstilos() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(VistaMultijugador.class.getResource("/recursos/icono.png")));
        this.pnlGlobal.setBackground(Color.white);
        this.pnlNorte.setBorder(BorderFactory.createLineBorder(Color.black, 20));
        this.pnlNorte.setBackground(Color.black);
        this.pnlCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.pnlCentral.setBackground(Color.white);
        this.pnlSur.setBackground(Color.black);
        this.pnlSur.setBorder(BorderFactory.createLineBorder(Color.black, 20));

        this.auxiliarEstiloEtiqueta(Color.white, this.lblRecord, this.lblNombreOponente, this.lblQuedan, this.lblPuntos, this.lblMiNombre);
        this.auxiliarEstiloEtiqueta(Color.yellow, this.lblMensajeAbajo);

        this.btnDesconectar.setBackground(Color.WHITE);
        this.btnDesconectar.setForeground(Color.BLACK);
        this.btnDesconectar.setFont(new java.awt.Font("Dialog", 1, 15));
        this.btnDesconectar.setFocusPainted(false);
        this.btnDesconectar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.btnDesconectar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/imgDesconectar.png")));
        for (JButton btn : this.coleccionBotones) {
            btn.setFont(new java.awt.Font("Dialog", 1, 26));
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setBorder(null);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            // Para cuando esten deshabilitados.
            btn.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource(coleccionRutasImgOn[Byte.parseByte(btn.getName())] + "Off.png")));
        }
    }

    // Auxiliar de definirEstilos(). Pone el texto en blanco y le da un tamanio.
    private void auxiliarEstiloEtiqueta(Color color, JLabel... etiquetas) {
        for (JLabel lbl : etiquetas) {
            lbl.setFont(new java.awt.Font("Dialog", 1, 15));
            lbl.setForeground(color);
        }
    }

    // Muestra un mensaje de confirmacion y retorna la respuesta.
    private boolean mostrarMensajeConfirmacion(String mensaje) {
        int valor = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.INFORMATION_MESSAGE);
        return valor == JOptionPane.YES_OPTION;
    }

    // Controla los eventos de la ventana.
    private void eventos() {
        // Boton de salir.
        this.btnDesconectar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (mostrarMensajeConfirmacion("<html><h1>¿Desconectar?</h1>Esto cerrara la conexion y volveras al menu principal.<br/>¿Estas seguro?</html>")) {
                    CONTROLADOR.cerrarConexion();
                    new VistaMenuPrincipal();
                    VistaMultijugador.this.dispose();
                }
            }
        });

        // Botones de color.
        for (JButton btn : this.coleccionBotones) {
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    CONTROLADOR.jugar((byte) Byte.parseByte(btn.getName()));
                }
            });

            btn.addMouseListener(new MouseAdapter() {
                byte indiceBoton = Byte.parseByte(btn.getName());

                @Override
                public void mouseEntered(MouseEvent me) {
                    btn.setIcon(new javax.swing.ImageIcon(getClass().getResource(coleccionRutasImgOn[indiceBoton] + "Sel.png")));
                }

                @Override
                public void mouseExited(MouseEvent me) {
                    btn.setIcon(new javax.swing.ImageIcon(getClass().getResource(coleccionRutasImgOn[indiceBoton] + "Off.png")));
                }
            });
        }
    }

    // A partir de aqui son todo metodos usados por el controlador y el hilo que muestra las combinaciones.
    /**
     * Muestra el valor del record. *Lo llama el controlador.
     *
     * @param record El record anterior.
     */
    public void mostrarRecord(int record) {
        this.lblRecord.setText("Record: " + Integer.toString(record));
    }

    /**
     * Muestra los toques restantes. *Lo llama el controlador.
     *
     * @param numToques Toques que faltan para terminar la combinacion.
     */
    public void mostrarToquesRestantes(int numToques) {
        this.lblQuedan.setText("Quedan: " + Integer.toString(numToques));
    }

    /**
     * Muestra la puntuacion actual. *Lo llama el controlador.
     *
     * @param puntos Los puntos ganados hasta el momento.
     */
    public void mostrarPuntos(int puntos) {
        this.lblPuntos.setText("Puntos: " + Integer.toString(puntos));
    }

    /**
     * Muestra el nombre del oponente. Solo esta disponible para esta vista.
     *
     * @param nombreOponente El nombre del oponente.
     */
    public void mostrarNombreOponente(String nombreOponente) {
        this.lblNombreOponente.setText("Oponente: " + nombreOponente);
    }

    /**
     * Muestra tu nombre. Solo esta disponible para esta vista.
     *
     * @param miNombre Tu nombre.
     */
    public void mostrarMiNombre(String miNombre) {
        this.lblMiNombre.setText("Nombre: " + miNombre);
    }

    /**
     * Bloquea o desbloquea todos los botones.
     *
     * @param bloqueado True es que estan bloqueados, false que no lo estan.
     */
    public void bloquearBotones(boolean bloqueado, boolean pintarFondo) {
        for (JButton btn : this.coleccionBotones) {
            btn.setEnabled(!bloqueado);
            if (pintarFondo) {
                this.pnlCentral.setBackground(bloqueado ? Colores.COLOR_AZUL_OSCURO : Color.white);
            }
        }
    }

    /**
     * Deja marcado el valor actual hasta que se pase otro valor. *Lo llama el
     * hilo. IMPORTANTE: Solo se mostrara si los botones estan bloqueados
     * (deshabilitados).
     *
     * @see bloquearBotones
     * @param valorActual Valores de 0 al 3 (0 Verde, 1 Rojo, 2 Amarillo, 3
     * Azul) El valor -1 indica todos apagados.
     */
    public synchronized void mostrarValorSerie(byte valorActual) {
        if (valorActual < this.coleccionBotones.length || valorActual == JuegoMultijugador.FIN_PARTIDA) {
            // Si encuentra el boton con el valor pasado por parametro lo enciende, si no lo apaga.
            for (JButton btn : this.coleccionBotones) {
                byte indiceBoton = Byte.parseByte(btn.getName());
                if (indiceBoton == valorActual) {
                    btn.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource(coleccionRutasImgOn[indiceBoton] + "On.png")));
                } else {
                    btn.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource(coleccionRutasImgOn[indiceBoton] + "Off.png")));
                }
            }
        }
    }

    /**
     * Muestra un mensaje que aparecera al acertar la serie.
     *
     * @param mensaje El mensaje que desees mostrar.
     */
    public synchronized void mostrarMensajeCorrecto(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Confirmar", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Especifica un mensaje para la etiqueta de abajo.
     *
     * @param mensaje El mensaje que quieras.
     */
    public void mostrarMensajeInferior(String mensaje) {
        this.lblMensajeAbajo.setText(mensaje);
    }

    /**
     * Pinta el fondo de color verde para indicar que el jugador ha de pulsar un
     * nuevo valor.
     */
    public void resaltarFondoVerde() {
        this.pnlCentral.setBackground(Colores.COLOR_VERDE);
    }
}
