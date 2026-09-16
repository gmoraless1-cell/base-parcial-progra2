package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import Modelos.Citas;


public class CitasDAO {
	private static final String URL = "jdbc:mysql://localhost:3306/prog2_db";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "umg2026";
    
    public int crear(Citas citas) throws SQLException {
    	//omitimos el campo "ID: en el INSERT porque Workbenck lo va a general solo (AUTO_INCREMET)
    	String sqlInsertar = "INSERT INTO citas (nombre_completo, fecha_hora_programada, descripcion_servicio, duracion_estimada_minutos, estado) VALUES (?, ?, ?, ?, ?)";
    	
    	//El parametro Statement.RETURN_GENERATED_KEYS le pide a Workbenk que nos devuelva el ID asignado
    	try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
    		PreparedStatement stmtInsertar = conexion.prepareStatement(sqlInsertar, Statement.RETURN_GENERATED_KEYS)) {
    			
    		
		//Mapeamos los datos del objeto Cita a la consulta SQL
    	stmtInsertar.setString(1, citas.getNombreCompleto());
    	
    	//Validamos si la fecha viene nula para evitar que truene el sistema
    	if (citas.getFechaHora() != null) {
    		stmtInsertar.setTimestamp(2, Timestamp.valueOf(citas.getFechaHora()));
    	} else {
    		stmtInsertar.setNull(2, java.sql.Types.TIMESTAMP);
    	}
    	
    	//convertimos la fecha a Timestamp para Workbench
    	//stmtInsertar.setTimestamp(2, Timestamp.valueOf(citas.getFechaHora()));
    	
    	stmtInsertar.setString(3, citas.getDescripcionServicio());
    	stmtInsertar.setInt(4, citas.getMinEstimados());
    	stmtInsertar.setInt(5, citas.getEstado().ordinal()); // 'Pendiente', 'confirmado', 'cancelado'
    	
    	// Ejecutamos la insercion
    	int filasAfectadas = stmtInsertar.executeUpdate();
    	
    	//Si la insercion fue exitosa, recuperamos el ID generado automaticamente
    	if (filasAfectadas > 0) {
    		try (ResultSet rsKeys = stmtInsertar.getGeneratedKeys()) {
    			if (rsKeys.next()) {
    				return rsKeys.getInt(1); //retorna el ID autogenerado
    			}
    		}
    	}
    	
    	return 0;
    	
    	}   		
    }
 	 public int actualizarCitas(Citas citas) throws SQLException {
	        String sql = "UPDATE citas SET nombre_completo = ?, fecha_hora_programada = ?, descipcion_servicio = ?, duracion_estimada_minutos = ?, estado = ? WHERE id = ?";

	        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
	             PreparedStatement statement = conexion.prepareStatement(sql)) {
	        	
	        	//Validamos si la fecha viene nula para evitar que truene el sistema
	        	if (citas.getFechaHora() != null) {
	        		statement.setTimestamp(2, Timestamp.valueOf(citas.getFechaHora()));
	        	} else {
	        		statement.setNull(2, java.sql.Types.TIMESTAMP);
	        	}
	        	
	            statement.setString(1, citas.getNombreCompleto());
	            statement.setString(3, citas.getDescripcionServicio());
	            statement.setInt(4, citas.getMinEstimados());
	            statement.setInt(5, citas.getEstado().ordinal());
	            statement.setInt(6, citas.getId());

	            int filasAfectadas = statement.executeUpdate();
	            return filasAfectadas;  // Devuelve el número de registros actualizados
	        }
	    }
	      

}
