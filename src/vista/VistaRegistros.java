package vista;

import controlador.Conector;
import controlador.ConectorIndividual;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import modelo.POJOindividual;
import modelo.POJOmultijugador;

/**
 * Vista del menu principal del juego de Simon.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class VistaRegistros extends JFrame {

    private JPanel pnlGlobal, pnlNorte, pnlBotones;
    private JScrollPane pnlScrollCentral;
    private JLabel lblTitulo;
    private JButton[] coleccionBotones;
    private final String cadenasBotonesMenu[] = {"Vaciar ", "Eliminar", "Volver"};
    private final String[] coleccionRutas1 = {"/recursos/imgVaciar.png", "/recursos/imgEliminar.png", "/recursos/imgVolver.png"};
    private JTable tablaRegistros;
    private DefaultTableModel modeloTabla;
    private final Conector CONECTOR;
    private String[] cadenasResultados = {"Perdiste", "Ganaste", "Empate"};

    /**
     * Constructor de la clase. Necesita el conector con la BD.
     *
     * @param controlador Necesita el controlador de la BD.
     */
    public VistaRegistros(Conector controlador) {
        this.CONECTOR = controlador;

        // Pasos para la creacion de la ventana.
        this.crearElementos();
        this.crearDistribucion();
        this.colocarElementos();
        this.definirEstilos();
        this.eventos();

        // Propiedades de la ventana.
        this.setTitle("Juego Simon Dice - Ver registros");
        this.setResizable(false);
        this.pack();
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    // Crea todos los elementos de la ventana.
    private void crearElementos() {
        this.pnlGlobal = new JPanel();
        this.pnlNorte = new JPanel();
        this.pnlBotones = new JPanel();
        this.coleccionBotones = new JButton[3];
        this.lblTitulo = new JLabel(" Ver registros " + (this.CONECTOR instanceof ConectorIndividual ? "individual" : "multijugador"));
        for (int i = 0; i < this.coleccionBotones.length; i++) {
            this.coleccionBotones[i] = new JButton(this.cadenasBotonesMenu[i]);
            this.coleccionBotones[i].setName(Integer.toString(i));
        }

        this.tablaRegistros = new JTable();
        this.pnlScrollCentral = new JScrollPane(this.tablaRegistros);

        this.rellenarModeloTabla();
    }

    // Rellena el modelo de la tabla, el cual sera mostrado por la tabla.
    private void rellenarModeloTabla() {
        this.modeloTabla = new DefaultTableModel();
        this.tablaRegistros.setModel(modeloTabla);

        if (this.CONECTOR instanceof ConectorIndividual) {
            // Si son los registros individuales.
            this.modeloTabla.addColumn("Id");
            this.modeloTabla.addColumn("Record");
            this.modeloTabla.addColumn("Fecha");

            ArrayList<POJOindividual> registros = this.CONECTOR.getRegistros();

            for (POJOindividual registro : registros) {
                String[] valores = {
                    Integer.toString(registro.getId()),
                    Integer.toString(registro.getRecord()),
                    registro.getFecha()};

                this.modeloTabla.addRow(valores);
            }

        } else {
            // Si son los registros multijugador.
            this.modeloTabla.addColumn("Id");
            this.modeloTabla.addColumn("Puntos");
            this.modeloTabla.addColumn("Fecha");
            this.modeloTabla.addColumn("Contrincante");
            this.modeloTabla.addColumn("Resultado");

            ArrayList<POJOmultijugador> registros = this.CONECTOR.getRegistros();

            for (POJOmultijugador registro : registros) {
                String[] valores = {
                    Integer.toString(registro.getId()),
                    Integer.toString(registro.getPuntos()),
                    registro.getFecha(),
                    registro.getContrincante(),
                    this.cadenasResultados[registro.getGanaste()]};

                this.modeloTabla.addRow(valores);
            }

        }
        this.repaint();
    }

    // Define las distribuciones que ha de tener cada panel.
    private void crearDistribucion() {
        // Elementos del menu.
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.pnlGlobal.setLayout(new BorderLayout());
        this.pnlNorte.setLayout(new GridLayout(2, 1, 0, 20));
        this.pnlBotones.setLayout(new GridLayout(1, 3, 5, 0));
    }

    // Coloca cada elemento en su respectivo panel.
    private void colocarElementos() {
        this.getContentPane().add(this.pnlGlobal);
        this.pnlGlobal.add(this.pnlNorte, BorderLayout.NORTH);
        this.pnlGlobal.add(this.pnlScrollCentral, BorderLayout.CENTER);
        this.pnlNorte.add(this.lblTitulo);
        this.pnlNorte.add(this.pnlBotones);
        for (JButton boton : this.coleccionBotones) {
            this.pnlBotones.add(boton);
        }
    }

    // Define los estilos de todos los elementos.
    private void definirEstilos() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(VistaRegistros.class.getResource("/recursos/icono.png")));
        this.pnlGlobal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.pnlScrollCentral.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        this.pnlGlobal.setBackground(Color.white);
        this.pnlNorte.setOpaque(false);
        this.pnlBotones.setOpaque(false);
        this.pnlScrollCentral.setOpaque(false);
        this.lblTitulo.setFont(new java.awt.Font("Dialog", Font.BOLD, 33));
        this.lblTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/imgRegistros.png")));
        this.lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        for (JButton btn : this.coleccionBotones) {
            this.auxiliarBotonesEstilo(btn);
            btn.setIcon(new javax.swing.ImageIcon(getClass().getResource(this.coleccionRutas1[Byte.parseByte(btn.getName())])));
            btn.setEnabled(false);
        }
        this.tablaRegistros.setEnabled(false);
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

    // Controla los eventos de la ventana.
    private void eventos() {
        // Botones del menu.
        for (JButton btn : this.coleccionBotones) {
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    switch (Byte.parseByte(btn.getName())) {
                        case 0:
                            // Vaciar registros.
                            System.out.println("Vaciar registros.");
                            if (mostrarMensajeConfirmacion("<html><h1>¿Estas seguro?</h1>Se borraran todos los registros excepto el mas reciente.</html>")) {
                                CONECTOR.vaciarRegistros(false);
                                rellenarModeloTabla();
                            }
                            break;
                        case 1:
                            // Eliminar registros.
                            System.out.println("Eliminar todo.");
                            if (mostrarMensajeConfirmacion("<html><h1>¿Estas seguro?</h1>Los datos se obtienen de los registros.<br/>Una vez sean borrados no se podran recuperar.</html>")) {
                                CONECTOR.vaciarRegistros(true);
                                rellenarModeloTabla();
                            }
                            break;
                        case 2:
                            // Volver.
                            System.out.println("Volver al menu.");
                            CONECTOR.desconectar();
                            new VistaMenuPrincipal();
                            VistaRegistros.this.dispose();
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
                        case 0:
                            btn.setBackground(Colores.COLOR_AZUL);
                            btn.setForeground(Color.WHITE);
                            break;
                        case 1:
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

    // Muestra un mensaje de confirmacion y retorna la respuesta.
    private boolean mostrarMensajeConfirmacion(String mensaje) {
        int valor = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.INFORMATION_MESSAGE);
        return valor == JOptionPane.YES_OPTION;
    }
}
