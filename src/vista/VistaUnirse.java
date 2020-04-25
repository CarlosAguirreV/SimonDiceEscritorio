package vista;

import controlador.JuegoMultijugador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * Vista usada para unirse a una partida multijugador en el juego de Simon.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class VistaUnirse extends JFrame {

    private JPanel pnlGlobal, pnlCentral, pnlIp, pnlNombre, pnlBotones;
    private JLabel lblTitulo, lblIp, lblNombre, lblEstado;
    private JTextField txtIpServidor, txtNombreJugador;
    private JButton[] coleccionBotones;
    private final String cadenasBotonesMenu[] = {"Volver ", "Conectar ", "Cancelar "};
    private final String[] coleccionRutas1 = {"/recursos/imgVolver.png", "/recursos/imgConectar.png", "/recursos/imgDesconectar2.png"};
    private final JuegoMultijugador CONTROLADOR;

    /**
     * Constructor vacio. Crea la vista.
     */
    public VistaUnirse() {
        // Pasos para la creacion de la ventana.
        this.crearElementos();
        this.crearDistribucion();
        this.colocarElementos();
        this.definirEstilos();
        this.eventos();

        // Propiedades de la ventana.
        this.setTitle("Juego Simon Dice - Unirse");
        this.setResizable(false);
        this.pack();
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);

        // Crear el controlador JuegoMultijugador.
        this.CONTROLADOR = new JuegoMultijugador(false);
    }

    /**
     * Constructor con los parametros ip y el nombre. Es usada para cuando se
     * termina una partida o se cierra una conexion, esta cargara los datos que
     * tenias al conectarte.
     *
     * @param ip La ip del servidor al que te conectaste.
     * @param tuNombre El nombre que definiste con anterioridad.
     */
    public VistaUnirse(String ip, String tuNombre) {
        this();
        this.txtIpServidor.setText(ip);
        this.txtNombreJugador.setText(tuNombre);
    }

    // Crea todos los elementos de la ventana.
    private void crearElementos() {
        this.pnlGlobal = new JPanel();
        this.pnlCentral = new JPanel();
        this.pnlIp = new JPanel();
        this.pnlNombre = new JPanel();
        this.pnlBotones = new JPanel();
        this.txtIpServidor = new JTextField("192.168.1.");
        this.txtNombreJugador = new JTextField("Anonimo");
        this.coleccionBotones = new JButton[3];
        this.lblIp = new JLabel("Ip del servidor");
        this.lblNombre = new JLabel("Tu nombre");
        this.lblTitulo = new JLabel(" Unirse a una partida");
        this.lblEstado = new JLabel("Escribe la ip del servidor, pon tu nombre y haz clic en Conectar.");
        for (int i = 0; i < this.coleccionBotones.length; i++) {
            this.coleccionBotones[i] = new JButton(this.cadenasBotonesMenu[i]);
            this.coleccionBotones[i].setName(Integer.toString(i));
        }
    }

    // Define las distribuciones que ha de tener cada panel.
    private void crearDistribucion() {
        // Elementos del menu.
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.pnlGlobal.setLayout(new BorderLayout());
        this.pnlCentral.setLayout(new GridLayout(3, 1, 0, 10));
        this.pnlIp.setLayout(new GridLayout(2, 1));
        this.pnlNombre.setLayout(new GridLayout(2, 1));
        this.pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
    }

    // Coloca cada elemento en su respectivo panel.
    private void colocarElementos() {
        this.getContentPane().add(this.pnlGlobal);
        this.pnlGlobal.add(this.lblTitulo, BorderLayout.NORTH);
        this.pnlGlobal.add(this.pnlCentral, BorderLayout.CENTER);
        this.pnlCentral.add(this.pnlIp);
        this.pnlIp.add(this.lblIp);
        this.pnlIp.add(this.txtIpServidor);
        this.pnlCentral.add(this.pnlNombre);
        this.pnlNombre.add(this.lblNombre);
        this.pnlNombre.add(this.txtNombreJugador);
        this.pnlCentral.add(this.lblEstado);
        this.pnlGlobal.add(this.pnlBotones, BorderLayout.SOUTH);
        for (JButton boton : this.coleccionBotones) {
            this.pnlBotones.add(boton);
        }
    }

    // Define los estilos de todos los elementos.
    private void definirEstilos() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(VistaUnirse.class.getResource("/recursos/icono.png")));
        this.pnlGlobal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.pnlCentral.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        this.pnlIp.setOpaque(false);
        this.pnlNombre.setOpaque(false);
        this.pnlGlobal.setBackground(Color.white);
        this.pnlCentral.setBackground(Color.white);
        this.pnlBotones.setBackground(Color.white);
        this.lblTitulo.setFont(new java.awt.Font("Dialog", Font.BOLD, 33));
        this.lblTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/imgUnirse2.png")));
        this.lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        this.lblIp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/imgElemento.png")));
        this.lblNombre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/imgElemento.png")));
        this.lblEstado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/imgInfo.png")));
        this.txtIpServidor.setMargin(new Insets(5, 10, 5, 10));
        this.txtNombreJugador.setMargin(new Insets(5, 10, 5, 10));
        this.lblEstado.setFont(new java.awt.Font("Dialog", 1, 13));
        this.auxiliarTexto(this.lblIp, this.lblNombre, this.txtIpServidor, this.txtNombreJugador);
        for (JButton btn : this.coleccionBotones) {
            this.auxiliarBotonesEstilo(btn);
            btn.setIcon(new javax.swing.ImageIcon(getClass().getResource(this.coleccionRutas1[Byte.parseByte(btn.getName())])));
            btn.setEnabled(false);
        }

        this.coleccionBotones[2].setVisible(false); // Ocultar el boton de cancelar por defecto.
    }

    // Ayuda a definir el estilo de los botones.
    private void auxiliarBotonesEstilo(JButton boton) {
        boton.setFont(new java.awt.Font("Dialog", 1, 26));
        boton.setBackground(Color.WHITE);
        boton.setForeground(Color.BLACK);
        boton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Ayuda a definir los estilos de las etiquetas.
    private void auxiliarTexto(JComponent... elementos) {
        for (JComponent componente : elementos) {
            componente.setFont(new java.awt.Font("Dialog", 1, 26));
            if (componente instanceof JTextField) {
                componente.setForeground(Colores.COLOR_TURQUESA);
            }
        }
    }

    // Controla los eventos de la ventana.
    private void eventos() {
        // Asegurarse de que en el campo de texto nombre no se exceda del numero de caracteres.
        this.txtNombreJugador.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if (txtNombreJugador.getText().length() >= JuegoMultijugador.LIMITE_NOMBRE) {
                    ke.consume();
                }
            }

        });

        // Botones del menu.
        for (JButton btn : this.coleccionBotones) {
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    switch (Byte.parseByte(btn.getName())) {
                        case 0:
                            // Volver al menu principal.
                            new VistaMenuPrincipal();
                            VistaUnirse.this.dispose();
                            break;
                        case 1:
                            // Conectar.
                            // Pasarle los parametros al controlador.
                            CONTROLADOR.setNombreJugador(txtNombreJugador.getText());
                            CONTROLADOR.setVistaUnirse(VistaUnirse.this);

                            // Poner el nombre de jugador que se ha quedado al final.
                            txtNombreJugador.setText(CONTROLADOR.getNombreJugador());

                            if (CONTROLADOR.intentarConectar(txtIpServidor.getText())) {
                                bloquearCampos(true);
                                lblEstado.setText("Intentando conectar...");
                                txtIpServidor.setBackground(Color.white);
                            } else {
                                lblEstado.setText("La direccion ip no es correcta.");
                                txtIpServidor.setBackground(Colores.COLOR_ROJO);
                            }

                            break;
                        case 2:
                            // Cancelar.
                            CONTROLADOR.cerrarConexion();
                            break;
                        default:
                            // Para cualquier otra cosa.
                            System.out.println("No entiendo lo que has pulsado.");
                    }
                }
            });

            // Cambio de color de los botones.
            btn.addMouseListener(new MouseAdapter() {
                byte indiceBoton = Byte.parseByte(btn.getName());

                @Override
                public void mouseEntered(MouseEvent me) {
                    btn.setEnabled(true);
                    // Solo para el boton de conectar.
                    switch (indiceBoton) {
                        case 1:
                            btn.setBackground(Color.GREEN);
                            btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/imgAceptar.png")));
                            break;
                        case 2:
                            btn.setBackground(Color.RED);
                            btn.setForeground(Color.WHITE);
                            break;
                        default:
                            btn.setBackground(Color.ORANGE);
                            break;
                    }

                }

                @Override
                public void mouseExited(MouseEvent me) {
                    btn.setEnabled(false);
                    btn.setBackground(Color.WHITE);
                    btn.setIcon(new javax.swing.ImageIcon(getClass().getResource(coleccionRutas1[indiceBoton])));
                }
            });
        }
        // Fin eventos.
    }

    /**
     * Estado en el cual la vista bloquea todos los campos y solo muestra el
     * boton de cancelar. Este metodo es llamado desde el controlador.
     *
     * @param conectado True si estoy conectado, false en caso contrario.
     */
    public void estoyConectado(boolean conectado) {
        System.out.println("Paso 0 (Unirse) - " + (conectado ? "Estas conectado.\n" : "No has logrado establecer la conexion."));

        if (conectado) {
            new VistaMultijugador(CONTROLADOR);
            this.dispose();
        } else {
            lblEstado.setText("<html>No se pudo conectar.<br>Asegurese de haber escrito bien la ip del servidor.</html>");
            bloquearCampos(false);
        }

    }

    // Bloquea todos los cuadros de texto y muestra u oculta los botones.
    private void bloquearCampos(boolean bloquear) {
        txtNombreJugador.setEnabled(!bloquear);
        txtIpServidor.setEnabled(!bloquear);

        coleccionBotones[0].setVisible(!bloquear);
        coleccionBotones[1].setVisible(!bloquear);
        coleccionBotones[2].setVisible(bloquear);

        VistaUnirse.this.repaint();
    }
}
