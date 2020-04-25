package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.POJOmultijugador;

/**
 * Esta clase se encarga de controlar la tabla de registros "multijugador" de la
 * BD SQLite.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class ConectorMultijugador extends Conector {

    /**
     * Constructor vacio, llama al constructor superior.
     */
    public ConectorMultijugador() {
        super();
    }

    /**
     * Guarda el registro en la BD a partir de un objeto POJO pasado como
     * parametro.
     *
     * @param objMultijugador POJO multijugador.
     * @return true si se ha guardado, false en caso contrario.
     */
    public boolean guardarRegistro(POJOmultijugador objMultijugador) {
        try {
            PreparedStatement sentenciaInsercion = super.conexion.prepareStatement("INSERT INTO multijugador(puntos, fecha, contrincante, ganaste) VALUES (?,?,?,?)");
            sentenciaInsercion.setInt(1, objMultijugador.getPuntos());
            sentenciaInsercion.setString(2, objMultijugador.getFecha());
            sentenciaInsercion.setString(3, objMultijugador.getContrincante());
            sentenciaInsercion.setInt(4, objMultijugador.getGanaste());
            sentenciaInsercion.execute();
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
    public ArrayList<POJOmultijugador> getRegistros() {
        ResultSet resultado = null;
        ArrayList<POJOmultijugador> coleccionMultijugador = new ArrayList();

        try {
            PreparedStatement consulta = super.conexion.prepareStatement("SELECT * FROM multijugador");
            resultado = consulta.executeQuery();

            while (resultado.next()) {
                coleccionMultijugador.add(new POJOmultijugador(resultado.getInt("id"), resultado.getInt("puntos"), resultado.getString("fecha"), resultado.getString("contrincante"), (byte) resultado.getInt("ganaste")));
            }

        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
        return coleccionMultijugador;
    }

    /**
     * Obtiene el ultimo registro de la BD, el mas actual.
     *
     * @return POJO multijugador.
     */
    public POJOmultijugador getUltimoRegistro() {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = super.conexion.prepareStatement("SELECT * FROM multijugador WHERE id = (SELECT MAX(id) FROM multijugador);");
            resultado = consulta.executeQuery();

            return resultado.next() ? new POJOmultijugador(resultado.getInt("id"), resultado.getInt("puntos"), resultado.getString("fecha"), resultado.getString("contrincante"), (byte) resultado.getInt("ganaste")) : new POJOmultijugador();

        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
        return new POJOmultijugador();
    }

    /**
     * Obtiene los puntos maximos de la partida en modo multijugador. El record.
     * Este metodo es EXCLUSIVO de esta clase.
     *
     * @return Puntos maximos obtenidos, el record
     */
    public int getPuntosMaximos() {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = super.conexion.prepareStatement("SELECT MAX(puntos) AS puntos FROM multijugador;");
            resultado = consulta.executeQuery();

            return resultado.next() ? resultado.getInt("puntos") : 0;

        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
        return 0;
    }

    /**
     * Vacia todos los registros excepto el ultimo (mas actual) si se indica
     * false en el parametro borrara todo.
     *
     * @param borrarTodo A true vacia toda la tabla, a false borra todo
     * exceptuando el ultimo registro, el mas actual.
     * @return true si se ha podido borrar, false en caso contrario.
     */
    public boolean vaciarRegistros(boolean borrarTodo) {
        try {
            PreparedStatement sentenciaBorrado = null;
            PreparedStatement sentenciaReiniciarNumeracion = super.conexion.prepareStatement("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='multijugador'");
            PreparedStatement sentenciaActualizarId = super.conexion.prepareStatement("UPDATE multijugador SET id = 0 WHERE id = (SELECT MAX(id) FROM multijugador)");

            if (borrarTodo) {
                sentenciaBorrado = super.conexion.prepareStatement("DELETE FROM multijugador");
            } else {
                sentenciaBorrado = super.conexion.prepareStatement("DELETE FROM multijugador WHERE id < (SELECT MAX(id) FROM multijugador)");
            }

            sentenciaBorrado.executeUpdate();
            sentenciaActualizarId.executeUpdate();
            sentenciaReiniciarNumeracion.executeUpdate();

            return true;

        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return false;
        }
    }
}
