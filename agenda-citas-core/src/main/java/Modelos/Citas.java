package Modelos;

import java.time.LocalDateTime;


public class Citas {
	// Definimos enum con los 3 valores solicitados, identicos a los de la BD
		public enum EstadoCita {
			
			PENDIENTE,
			CONFIRMADO,
			CANCELADO;

		}
		
		private int id;
		private String nombreCompleto;
		private LocalDateTime fechaHora;
		private String descripcionServicio;
		private int minEstimados;
		private boolean requiereConfirmacionLlamada;
		
		private EstadoCita estado = EstadoCita.PENDIENTE;
		
		public Citas(int id, String nombreCompleto, LocalDateTime fechaHora, String descripcionServicio, int minEstimados, EstadoCita estado, boolean requiereConfirmacionLlamada) {
		        this.id = id;
		        this.nombreCompleto = nombreCompleto;
		        this.fechaHora = fechaHora;
		        this.descripcionServicio = descripcionServicio;
		        this.minEstimados = minEstimados;
		        this.estado = estado;
		        this.requiereConfirmacionLlamada = requiereConfirmacionLlamada;
		}
		
	    // Constructor de conveniencia para una cita que todavia no existe en la
	    // BD (id = 0, MySQL le asigna el id real al insertarlo).
	  public Citas(String nombreCompleto, LocalDateTime fechaHora, String descripcionServicio, int minEstimados, EstadoCita estado, boolean requiereConfirmacionLlamada) {
	        this(0, nombreCompleto, fechaHora, descripcionServicio, minEstimados, estado, requiereConfirmacionLlamada);
	        
	  	}
	  public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getNombreCompleto() {
			return nombreCompleto;
		}
		public void setNombre(String nombreCompleto) {
			this.nombreCompleto = nombreCompleto;
		}
		public LocalDateTime getFechaHora() {
			return fechaHora;
		}
		public void setfecha_hora_programada(LocalDateTime fechaHora) {
			this.fechaHora = fechaHora;
		}
		public String getDescripcionServicio() {
			return descripcionServicio;
		}
		public void setDescripcion_servicio(String descripcionServicio) {
			this.descripcionServicio = descripcionServicio;
		}
		public int getMinEstimados() {
			return minEstimados;
		}
		public void setMinutos_estimados(int minEstimados) {
			this.minEstimados = minEstimados;
		}
		public void getRequiereConfirmacionLlamada(boolean requiereConfirmacionLlamada) {
			this.requiereConfirmacionLlamada = requiereConfirmacionLlamada;
		}
		
		@Override
		public String toString() {
		    // %-3d   -> ID (3 espacios)
		    // %-50s  -> Nombre (50 espacios)
		    // %-10s  -> Fecha y hora (10 espacios)
		    // %-30s -> Descripcion (30 espacios)
			// %d min -> Duracion en minutos.
		    // %s     -> Estado de la cita.
			// %s     -> Confirmacion
		    return String.format("[%3d] %-50s | %-10s | %-30s | %d min | Estado: %s", 
		                         id, nombreCompleto, fechaHora, descripcionServicio, minEstimados, estado, requiereConfirmacionLlamada);
		}

		public EstadoCita getEstado() {
			return estado;
		}

		public void setEstado(EstadoCita estado) {
			this.estado = estado;
		}
		
		public boolean isRequiereConfirmacionLlamada() {
			return requiereConfirmacionLlamada;
		}

		public void setRequiereConfirmacionLlamada(boolean requiereConfirmacionLlamada) {
			this.requiereConfirmacionLlamada = requiereConfirmacionLlamada;
		}


}
