package vista;

import controlador.JuegoIndividual;
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
 * Vista del juego de Simon.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class VistaIndividual extends JFrame implements InterfazVista {

    private JPanel pnlGlobal, pnlCentral, pnlNorte, pnlNorteIzquieda;
    private JLabel lblRecord, lblPuntos, lblToquesRestantes;
    private JButton btnSalir;
    private JButton[] coleccionBotones; // 0-Rojo, 1-Azul, 2-Amarillo, 3-Verde.
    private String[] coleccionRutasImgOn = {"/recursos/imgBtnVerde", "/recursos/imgBtnRojo", "/recursos/imgBtnAmarillo", "/recursos/imgBtnAzul"};
    private final JuegoIndividual CONTROLADOR;

    /**
     * Constructor de la vista individual, forma la vista.
     */
    public VistaIndividual() {
        // Pasos para la creacion de la ventana.
        this.crearElementos();
        this.crearDistribucion();
        this.colocarElementos();
        this.definirEstilos();
        this.eventos();

        // Propiedades de la ventana.
        this.setTitle("Juego Simon Dice - Individual");
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);

        // Crear el controlador de JuegoIndividual.
        this.CONTROLADOR = new JuegoIndividual(this);
    }

    // Crea todos los elementos de la ventana.
    private void crearElementos() {
        this.pnlGlobal = new JPanel();
        this.pnlNorte = new JPanel();
        this.pnlNorteIzquieda = new JPanel();
        this.pnlCentral = new JPanel();
        this.lblRecord = new JLabel("...");
        this.lblPuntos = new JLabel("...");
        this.lblToquesRestantes = new JLabel("...");
        this.btnSalir = new JButton("Salir");

        this.coleccionBotones = new JButton[4];
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
        this.pnlNorte.setLayout(new BorderLayout());
        this.pnlCentral.setLayout(new GridLayout(2, 2, 0, 0));
        this.pnlNorteIzquieda.setLayout(new GridLayout(3, 1));
    }

    // Coloca cada elemento en su respectivo panel.
    private void colocarElementos() {
        this.getContentPane().add(this.pnlGlobal);
        this.pnlGlobal.add(this.pnlNorte, BorderLayout.NORTH);
        this.pnlGlobal.add(this.pnlCentral, BorderLayout.CENTER);
        this.pnlNorte.add(this.pnlNorteIzquieda, BorderLayout.CENTER);
        this.pnlNorte.add(this.btnSalir, BorderLayout.EAST);
        this.pnlNorteIzquieda.add(this.lblRecord);
        this.pnlNorteIzquieda.add(this.lblPuntos);
        this.pnlNorteIzquieda.add(this.lblToquesRestantes);

        for (JButton btn : this.coleccionBotones) {
            this.pnlCentral.add(btn);
        }
    }

    // Define los estilos de todos los elementos.
    private void definirEstilos() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(VistaIndividual.class.getResource("/recursos/icono.png")));
        this.pnlCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.pnlNorte.setBorder(BorderFactory.createLineBorder(Color.black, 20));
        this.pnlGlobal.setBackground(Color.white);
        this.pnlNorte.setBackground(Color.black);
        this.pnlNorteIzquieda.setBackground(Color.black);
        this.pnlCentral.setBackground(Color.white);

        this.auxiliarEstiloEtiqueta(this.lblRecord);
        this.auxiliarEstiloEtiqueta(this.lblPuntos);
        this.auxiliarEstiloEtiqueta(this.lblToquesRestantes);

        this.btnSalir.setBackground(Color.WHITE);
        this.btnSalir.setForeground(Color.BLACK);
        this.btnSalir.setFont(new java.awt.Font("Dialog", 1, 20));
        this.btnSalir.setFocusPainted(false);
        this.btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/imgSalir2.png")));
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
    private void auxiliarEstiloEtiqueta(JLabel etiqueta) {
        etiqueta.setFont(new java.awt.Font("Dialog", 1, 15));
        etiqueta.setForeground(Color.white);
    }

    // Controla los eventos de la ventana.
    private void eventos() {
        // Boton de salir.
        this.btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (mostrarMensajeConfirmacion("<html><h1>¿Volver al menu?</h1>Perderas tu progreso.</html>")) {
                    new VistaMenuPrincipal();
                    VistaIndividual.this.dispose();
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
        this.lblToquesRestantes.setText("Toques restantes: " + Integer.toString(numToques));
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
     * Bloquea o desbloquea todos los botones. *Lo llama el hilo.
     *
     * @param bloqueado True es que estan bloqueados, false que no lo estan.
     */
    public void bloquearBotones(boolean bloqueado, boolean pintarFondo) {
        for (JButton btn : this.coleccionBotones) {
            btn.setEnabled(!bloqueado);
        }
        if (pintarFondo) {
            this.pnlCentral.setBackground(bloqueado ? Colores.COLOR_AZUL_OSCURO : Color.white);
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
        if (valorActual < this.coleccionBotones.length) {

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
     * Muestra un mensaje de confirmacion y retorna la respuesta.
     *
     * @param mensaje El mensaje que quieres que se muestre.
     * @return true Si se pulso en Si, false si se pulso en No.
     */
    public boolean mostrarMensajeConfirmacion(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
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
     * Pinta el fondo de color verde para indicar que el jugador ha de pulsar un
     * nuevo valor.
     */
    public void resaltarFondoVerde() {
        this.pnlCentral.setBackground(Colores.COLOR_VERDE);
    }
}
