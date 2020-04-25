package vista;

import controlador.ConectorIndividual;
import controlador.ConectorMultijugador;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Vista del menu principal del juego de Simon.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class VistaMenuPrincipal extends JFrame {

    private JPanel pnlGlobal, pnlCentral, pnlMultijugador, pnlRecords, pnlVerRegistros, pnlAcercaDe, pnlAcercaDeCentro, pnlAcercaDeSur;
    private JLabel lblImagenPortada, lblTextoRecordInd, lblRecordIndividual, lblTextoRecordMult, lblRecordMultijugador, lblImagenAcercaDe, lblAcercaDeTitulo1, lblAcercaDeTitulo2, lblMultijugador, lblRegistros;
    private JButton[] coleccionBotonesMenu;
    private final String cadenasBotonesMenu[] = {"Juego individual", "Juego multijugador", "Ver registros", "Acerca de", "Salir"};
    private JButton[] coleccionBotonesDialogo;
    private final String cadenasBotonesDialogo[] = {"Unirse a una partida ", "Crear una partida", "Individual", "Multijugador"};
    
    private final String[] coleccionRutas1 = {"/recursos/imgIndividual.png", "/recursos/imgMultijugador.png", "/recursos/imgRegistros.png", "/recursos/imgAcercaDe1.png", "/recursos/imgSalir.png"};
    private final String[] coleccionRutas2 = {"/recursos/imgUnirse1.png", "/recursos/imgCrear.png", "/recursos/imgIndividual.png", "/recursos/imgMultijugador.png"};
    private static final String[] INFORMACION = {"Carlos Aguirre", "09-05-2019"};

    public VistaMenuPrincipal() {
        // Pasos para la creacion de la ventana.
        this.crearElementos();
        this.crearDistribucion();
        this.colocarElementos();
        this.definirEstilos();
        this.eventos();

        // Propiedades de la ventana.
        this.setTitle("Juego Simon Dice");
        this.setResizable(false);
        this.pack();
        this.setSize(500, 650);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Mostrar los records.
        this.mostrarRecordsAnteriores();
    }

    // Crea todos los elementos de la ventana.
    private void crearElementos() {
        // Elementos del menu.
        this.coleccionBotonesMenu = new JButton[5];
        this.pnlGlobal = new JPanel();
        this.pnlCentral = new JPanel();
        this.lblImagenPortada = new JLabel(new ImageIcon(getClass().getResource("/recursos/imgPortada.png")));
        for (int i = 0; i < this.coleccionBotonesMenu.length; i++) {
            this.coleccionBotonesMenu[i] = new JButton(this.cadenasBotonesMenu[i]);
            this.coleccionBotonesMenu[i].setName(Integer.toString(i));
        }
        this.pnlRecords = new JPanel();
        this.lblTextoRecordInd = new JLabel("Record individual");
        this.lblRecordIndividual = new JLabel("...");
        this.lblTextoRecordMult = new JLabel("Record multijugador");
        this.lblRecordMultijugador = new JLabel("...");

        // Elementos de las ventanas de dialogo.
        this.coleccionBotonesDialogo = new JButton[4];
        this.pnlMultijugador = new JPanel();
        this.lblMultijugador = new JLabel("Escoge una opcion");
        this.pnlVerRegistros = new JPanel();
        this.lblRegistros = new JLabel("¿Que registros deseas ver?");
        this.pnlAcercaDe = new JPanel();
        this.pnlAcercaDeCentro = new JPanel();
        this.pnlAcercaDeSur = new JPanel();
        this.lblImagenAcercaDe = new JLabel(new ImageIcon(getClass().getResource("/recursos/imgAcercaDe2.png")));
        this.lblAcercaDeTitulo1 = new JLabel("Sobre");
        this.lblAcercaDeTitulo2 = new JLabel("Un poco de historia");
        for (int i = 0; i < this.coleccionBotonesDialogo.length; i++) {
            this.coleccionBotonesDialogo[i] = new JButton(this.cadenasBotonesDialogo[i]);
            this.coleccionBotonesDialogo[i].setName(Integer.toString(i));
        }
    }

    // Define las distribuciones que ha de tener cada panel.
    private void crearDistribucion() {
        // Elementos del menu.
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.pnlGlobal.setLayout(new BorderLayout());
        this.pnlCentral.setLayout(new GridLayout(5, 1, 0, 10));
        this.pnlRecords.setLayout(new GridLayout(2, 2));

        // Elementos de las ventanas de dialogo.
        this.pnlMultijugador.setLayout(new GridLayout(3, 1, 0, 10));
        this.pnlVerRegistros.setLayout(new GridLayout(3, 1, 0, 10));
        this.pnlAcercaDe.setLayout(new BorderLayout(0, 10));
        this.pnlAcercaDeCentro.setLayout(new GridLayout(3, 2));
        this.pnlAcercaDeSur.setLayout(new GridLayout(5, 1));
    }

    // Coloca cada elemento en su respectivo panel.
    private void colocarElementos() {
        // Elementos del menu.
        this.getContentPane().add(this.pnlGlobal);
        this.pnlGlobal.add(this.lblImagenPortada, BorderLayout.NORTH);
        this.pnlGlobal.add(this.pnlCentral, BorderLayout.CENTER);
        for (JButton btn : this.coleccionBotonesMenu) {
            this.pnlCentral.add(btn);
        }
        this.pnlGlobal.add(this.pnlRecords, BorderLayout.SOUTH);
        this.pnlRecords.add(this.lblTextoRecordInd);
        this.pnlRecords.add(this.lblTextoRecordMult);
        this.pnlRecords.add(this.lblRecordIndividual);
        this.pnlRecords.add(this.lblRecordMultijugador);

        // Elementos de las ventanas de dialogo.
        this.pnlMultijugador.add(this.lblMultijugador);
        this.pnlMultijugador.add(this.coleccionBotonesDialogo[0]);
        this.pnlMultijugador.add(this.coleccionBotonesDialogo[1]);

        this.pnlVerRegistros.add(this.lblRegistros);
        this.pnlVerRegistros.add(this.coleccionBotonesDialogo[2]);
        this.pnlVerRegistros.add(this.coleccionBotonesDialogo[3]);

        this.pnlAcercaDe.add(this.lblImagenAcercaDe, BorderLayout.NORTH);
        this.pnlAcercaDe.add(this.pnlAcercaDeCentro, BorderLayout.CENTER);
        this.pnlAcercaDeCentro.add(this.lblAcercaDeTitulo1);
        this.pnlAcercaDeCentro.add(new JLabel(""));
        this.pnlAcercaDeCentro.add(new JLabel("Desarrollador"));
        this.pnlAcercaDeCentro.add(new JLabel(INFORMACION[0]));
        this.pnlAcercaDeCentro.add(new JLabel("Fecha"));
        this.pnlAcercaDeCentro.add(new JLabel(INFORMACION[1]));
        this.pnlAcercaDe.add(this.pnlAcercaDeSur, BorderLayout.SOUTH);
        this.pnlAcercaDeSur.add(this.lblAcercaDeTitulo2);
        this.pnlAcercaDeSur.add(new JLabel("El juego de Simon fue creado por Rudolf Heinrich Baer y Howard"));
        this.pnlAcercaDeSur.add(new JLabel("J. Morrison en 1978. Tuvo gran exito en la epoca de los 80."));
        this.pnlAcercaDeSur.add(new JLabel("Su nombre se debe a otro conocido juego, Simon dice del cual he"));
        this.pnlAcercaDeSur.add(new JLabel("cogido el nombre para ponerle el titulo a esta version del juego."));

    }

    // Define los estilos de todos los elementos.
    private void definirEstilos() {
        // Elementos del menu.
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(VistaMenuPrincipal.class.getResource("/recursos/icono.png")));
        this.pnlGlobal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.pnlCentral.setBorder(BorderFactory.createEmptyBorder(30, 30, 15, 30));
        this.pnlGlobal.setBackground(Color.white);
        this.pnlCentral.setBackground(Color.white);
        for (JButton btn : this.coleccionBotonesMenu) {
            this.auxiliarBotonesEstilo(btn);
            btn.setIcon(new javax.swing.ImageIcon(getClass().getResource(this.coleccionRutas1[Byte.parseByte(btn.getName())])));
            btn.setEnabled(false);
        }
        this.pnlRecords.setBackground(Color.white);
        this.auxiliarEtiquetasEstilo(15, this.lblTextoRecordInd, this.lblTextoRecordMult);
        this.auxiliarEtiquetasEstilo(25, this.lblRecordIndividual, this.lblRecordMultijugador);

        // Elementos de las ventanas de dialogo.
        this.lblAcercaDeTitulo1.setFont(new java.awt.Font("Dialog", 1, 15));
        this.lblAcercaDeTitulo2.setFont(new java.awt.Font("Dialog", 1, 15));
        this.lblMultijugador.setFont(new java.awt.Font("Dialog", 1, 20));
        this.lblRegistros.setFont(new java.awt.Font("Dialog", 1, 20));
        for (JButton btn : this.coleccionBotonesDialogo) {
            btn.setIcon(new javax.swing.ImageIcon(getClass().getResource(this.coleccionRutas2[Byte.parseByte(btn.getName())])));
            this.auxiliarBotonesEstilo(btn);
        }
    }

    // Ayuda a definir el estilo de las etiquetas.
    private void auxiliarEtiquetasEstilo(int tamFuente, JLabel... etiquetas) {
        for (JLabel lbl : etiquetas) {
            lbl.setFont(new java.awt.Font("Dialog", 1, tamFuente));
            lbl.setHorizontalAlignment(JLabel.CENTER);
        }
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
        for (JButton btn : this.coleccionBotonesMenu) {
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    switch (Byte.parseByte(btn.getName())) {
                        case 0:
                            // Juego individual.
                            System.out.println("Menu - Juego individual.");
                            new VistaIndividual();
                            VistaMenuPrincipal.this.dispose();
                            break;
                        case 1:
                            // Juego multijugador.
                            System.out.println("Menu - Juego multijugador.");
                            mostrarCuadroDialogo(pnlMultijugador, "Juego multijugador > Opcion", "Volver al menu");
                            break;
                        case 2:
                            // Ver registros.
                            System.out.println("Menu - Ver registros.");
                            mostrarCuadroDialogo(pnlVerRegistros, "Ver registros > Cual", "Volver al menu");
                            break;
                        case 3:
                            // Acerca de.
                            System.out.println("Menu - Acerca de.");
                            mostrarCuadroDialogo(pnlAcercaDe, "Acerca de", "Cerrar");
                            break;
                        case 4:
                            // Salir.
                            System.out.println("Menu - Salir. Hasta otra.");
                            System.exit(0);
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
                    btn.setBackground(Colores.COLECCION_COLORES_MENU[indiceBoton]);
                    // Para hacer buen contraste entre fondo y letra.
                    if (indiceBoton < 2 || indiceBoton == 4) {
                        btn.setForeground(Color.white);
                    }
                    btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/imgSelector.png")));
                }

                @Override
                public void mouseExited(MouseEvent me) {
                    btn.setEnabled(false);
                    btn.setBackground(Color.WHITE);
                    btn.setForeground(Color.BLACK);
                    btn.setIcon(new javax.swing.ImageIcon(getClass().getResource(coleccionRutas1[indiceBoton])));
                }
            });
        }

        // Botones de las ventanas de dialogo.
        for (JButton btn : this.coleccionBotonesDialogo) {
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    switch (Byte.parseByte(btn.getName())) {
                        case 0:
                            // Unirse.
                            System.out.println("\t-> Unirse a una partida.");
                            new VistaUnirse();
                            VistaMenuPrincipal.this.dispose();
                            break;
                        case 1:
                            System.out.println("\t-> Crear partida.");
                            new VistaCrear();
                            VistaMenuPrincipal.this.dispose();
                            break;
                        case 2:
                            System.out.println("\t-> Registro individual.");
                            new VistaRegistros(new ConectorIndividual());
                            VistaMenuPrincipal.this.dispose();
                            break;
                        case 3:
                            System.out.println("\t-> Registro multijugador.");
                            new VistaRegistros(new ConectorMultijugador());
                            VistaMenuPrincipal.this.dispose();
                            break;
                        default:
                            // Para cualquier otra cosa.
                            System.out.println("\t->  No entiendo lo que has pulsado.");
                    }
                }
            });
        }
        // Fin eventos.
    }

    // Muestra una ventana de dialogo personalizada.
    private void mostrarCuadroDialogo(JPanel panel, String titulo, String opcionCerrar) {
        String[] opciones = {opcionCerrar};
        JOptionPane.showOptionDialog(this, panel, titulo, JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, opciones, opciones[0]);
    }

    // Muestra los records de ambos modos de juego.
    private void mostrarRecordsAnteriores() {
        try{
        
        ConectorIndividual datosIndividual = new ConectorIndividual();
        this.lblRecordIndividual.setText(Integer.toString(datosIndividual.getUltimoRegistro().getRecord()));
        datosIndividual.desconectar();

        ConectorMultijugador datosMutlijugador = new ConectorMultijugador();
        this.lblRecordMultijugador.setText(Integer.toString(datosMutlijugador.getPuntosMaximos()));
        datosMutlijugador.desconectar();
        
        this.setVisible(true);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "<html><h1>No se encontró la carpeta lib ni su contenido</h1><p>Es necesario para poder controlar la base de datos de SQLite empleada para guardar los registros.</p><p>No se puede ejecutar la aplicacion asi.</p><p>Busca la carpeta con la libreria en el archivo comprimido y situala junto al archivo JuegoSimon.jar</p><em>-- Un saludo del desarrollador --</em><html>", "Hey, que falta la carpeta lib y su contenido", JOptionPane.OK_OPTION);
            System.exit(0);
        }
    }

}
