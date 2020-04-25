package controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.POJOindividual;

/**
 * Esta clase se encarga de controlar la tabla de registros "individual" de la
 * BD SQLite.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class ConectorIndividual extends Conector {

    /**
     * Constructor vacio, llama al constructor superior.
     */
    public ConectorIndividual() {
        super();
    }

    /**
     * Guarda el registro en la BD a partir de un objeto POJO pasado como
     * parametro.
     *
     * @param objIndividual POJO individual.
     * @return true si se ha guardado, false en caso contrario.
     */
    public boolean guardarRegistro(POJOindividual objIndividual) {
        try {
            PreparedStatement sentenciaInsercion = super.conexion.prepareStatement("INSERT INTO individual(record, fecha) VALUES (?,?)");
            sentenciaInsercion.setInt(1, objIndividual.getRecord());
            sentenciaInsercion.setString(2, objIndividual.getFecha());
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
    public ArrayList<POJOindividual> getRegistros() {
        ResultSet resultado = null;
        ArrayList<POJOindividual> coleccionIndividual = new ArrayList();

        try {
            PreparedStatement consulta = super.conexion.prepareStatement("SELECT * FROM individual");
            resultado = consulta.executeQuery();

            while (resultado.next()) {
                coleccionIndividual.add(new POJOindividual(resultado.getInt("id"), resultado.getInt("record"), resultado.getString("fecha")));
            }

        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
        return coleccionIndividual;
    }

    /**
     * Obtiene el ultimo registro de la BD, el mas actual.
     *
     * @return POJOindividual
     */
    public POJOindividual getUltimoRegistro() {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = super.conexion.prepareStatement("SELECT * FROM individual WHERE id = (SELECT MAX(id) FROM individual);");
            resultado = consulta.executeQuery();

            return resultado.next() ? new POJOindividual(resultado.getInt("id"), resultado.getInt("record"), resultado.getString("fecha")) : new POJOindividual();

        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
        return new POJOindividual();
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
            PreparedStatement sentenciaReiniciarNumeracion = super.conexion.prepareStatement("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='individual'");
            PreparedStatement sentenciaActualizarId = super.conexion.prepareStatement("UPDATE individual SET id = 0 WHERE id = (SELECT MAX(id) FROM individual)");

            if (borrarTodo) {
                sentenciaBorrado = super.conexion.prepareStatement("DELETE FROM individual");
            } else {
                sentenciaBorrado = super.conexion.prepareStatement("DELETE FROM individual WHERE id < (SELECT MAX(id) FROM individual)");
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
