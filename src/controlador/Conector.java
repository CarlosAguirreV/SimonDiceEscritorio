package controlador;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase abstracta que contiene los metodos comunes para los dos conectores
 * (multijugador e individual).
 *
 * @author Carlos Aguirre Vozmediano
 */
public abstract class Conector {

    private String rutaBD = "registros.db";
    protected Connection conexion;

    /**
     * Constructor vacio.
     */
    public Conector() {
        this.conectar();
    }

    /**
     * Conecta con la BD. En caso de no existir, la crea y ademas crea las
     * tablas.
     *
     * @return true si se ha conectado, false en caso contrario.
     */
    private boolean conectar() {
        // Si no existe el archivo dejara una senial para crear las tablas despues.
        boolean existeArchivo = new File(rutaBD).exists();

        // Trata de conectarse a la BD.
        try {
            this.conexion = DriverManager.getConnection("jdbc:sqlite:" + rutaBD);
            if (!existeArchivo) {
                this.crearTablas();
            }
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return false;
        }
    }

    // Crea las tablas de la BD.
    private boolean crearTablas() {
        try {
            PreparedStatement sentenciaCreacion1 = this.conexion.prepareStatement("CREATE TABLE individual(id integer PRIMARY KEY autoincrement, record integer, fecha text)");
            PreparedStatement sentenciaCreacion2 = this.conexion.prepareStatement("CREATE TABLE multijugador(id integer PRIMARY KEY autoincrement, puntos integer, fecha text, contrincante varchar(15), ganaste number(1))");
            sentenciaCreacion1.execute();
            sentenciaCreacion2.execute();
            return true;

        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Desconecta de la BD.
     *
     * @return true si se ha desconectado, false en caso contrario.
     */
    public boolean desconectar() {
        try {
            this.conexion.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los registros.
     *
     * @return ArrayList con todos los registros.
     */
    public abstract ArrayList getRegistros();

    /**
     * Vacia todos los registros excepto el ultimo (mas actual) si se indica
     * false en el parametro borrara todo.
     *
     * @param borrarTodo A true vacia toda la tabla, a false borra todo
     * exceptuando el ultimo registro, el mas actual.
     * @return true si se ha podido borrar, false en caso contrario.
     */
    public abstract boolean vaciarRegistros(boolean borrarTodo);
}
