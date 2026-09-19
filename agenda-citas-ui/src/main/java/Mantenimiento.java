import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DAO.CitasDAO;
import Modelos.Citas;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

public class Mantenimiento extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JSpinner spinner;
	private JComboBox<Citas.EstadoCita> comboBox;
	private JButton btnNewButton;
	private VentanaPrincipal ventanaPrincipal;
	private Citas citaEnEdicion;
	private JButton btnNewButton_1;
	private JCheckBox chckbxNewCheckBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mantenimiento frame = new Mantenimiento();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void guardarCita() {
		String nombre = textField.getText().trim();

		if (nombre.isEmpty()) {
			JOptionPane.showMessageDialog(this,
				"El nombre completo es obligatorio.",
				"Datos incompletos", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String descripcion = textField_2.getText().trim();

		int duracion;
		try {
			duracion = Integer.parseInt(textField_1.getText().trim());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this,
				"El tiempo estimado debe ser un numero.",
				"Dato invalido", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (duracion <= 0) {
			JOptionPane.showMessageDialog(this,
				"El tiempo estimado debe ser mayor a cero.",
				"Dato invalido", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		Date fechaSeleccionada = (Date) spinner.getValue();
		LocalDateTime fechaHora = fechaSeleccionada.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();

		if (fechaHora.isBefore(LocalDateTime.now())) {
			JOptionPane.showMessageDialog(this,
				"La fecha y hora debe ser posterior a la fecha y hora actual.",
				"Dato invalido", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Citas.EstadoCita estadoSeleccionado = (Citas.EstadoCita) comboBox.getSelectedItem();

		try {
			CitasDAO dao = new CitasDAO();

			if (citaEnEdicion == null) {
				// ---- MODO CREAR ----
				Citas nuevaCita = new Citas(nombre, fechaHora, descripcion, duracion, Citas.EstadoCita.PENDIENTE, chckbxNewCheckBox.isSelected());
				int idGenerado = dao.crear(nuevaCita);

				if (idGenerado > 0) {
					JOptionPane.showMessageDialog(this, "Cita guardada correctamente.");
				} else {
					JOptionPane.showMessageDialog(this, "No se pudo guardar la cita.",
						"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else {
				// ---- MODO EDITAR ----
				citaEnEdicion.setNombre(nombre);
				citaEnEdicion.setfecha_hora_programada(fechaHora);
				citaEnEdicion.setDescripcion_servicio(descripcion);
				citaEnEdicion.setMinutos_estimados(duracion);
				citaEnEdicion.setEstado(estadoSeleccionado);
				citaEnEdicion.setRequiereConfirmacionLlamada(chckbxNewCheckBox.isSelected());

				int filasAfectadas = dao.actualizarCitas(citaEnEdicion);

				if (filasAfectadas > 0) {
					JOptionPane.showMessageDialog(this, "Cita actualizada correctamente.");
				} else {
					JOptionPane.showMessageDialog(this, "No se pudo actualizar la cita.",
						"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			if (ventanaPrincipal != null) {
				ventanaPrincipal.cargarCitas();
			}
			dispose();

		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this,
				"Error al guardar en la base de datos:\n" + ex.getMessage(),
				"Error de conexion", JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * Create the frame.
	 */
	public Mantenimiento() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 614, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nombre completo");
		lblNewLabel.setBounds(23, 82, 153, 31);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Fecha y Hora");
		lblNewLabel_1.setBounds(23, 123, 89, 31);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Descripcion del servicio");
		lblNewLabel_1_1.setBounds(23, 164, 153, 31);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Tiempo estimado (minutos)");
		lblNewLabel_1_1_1.setBounds(23, 204, 185, 31);
		contentPane.add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Estado");
		lblNewLabel_1_1_1_1.setBounds(23, 245, 131, 31);
		contentPane.add(lblNewLabel_1_1_1_1);
		
		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Completar los siguientes datos");
		lblNewLabel_1_1_1_1_1.setBounds(234, 10, 231, 31);
		contentPane.add(lblNewLabel_1_1_1_1_1);
		
		textField = new JTextField();
		textField.setBounds(217, 88, 248, 25);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(217, 210, 248, 25);
		contentPane.add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(217, 170, 248, 25);
		contentPane.add(textField_2);
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR)); //nos asegura de que el sistema forze a buscar la fecha actual del dispositivo
		spinner.setBounds(217, 129, 248, 23);
		contentPane.add(spinner);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setEnabled(false);
		textField_3.setBounds(217, 51, 248, 25);
		contentPane.add(textField_3);
		
		JLabel lblNewLabel_2 = new JLabel("ID");
		lblNewLabel_2.setBounds(23, 48, 89, 31);
		contentPane.add(lblNewLabel_2);
		
		btnNewButton = new JButton("Guardar");
		btnNewButton.setBounds(491, 300, 84, 20);
		contentPane.add(btnNewButton);
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardarCita();
				
			}
			
		});
		
		comboBox = new JComboBox<Citas.EstadoCita>();
		comboBox.setModel(new DefaultComboBoxModel<Citas.EstadoCita>(Citas.EstadoCita.values()));
		comboBox.setSelectedItem(Citas.EstadoCita.PENDIENTE);
		comboBox.setEnabled(false); // Definimos el estado de la cita en pendiente
		comboBox.setBounds(217, 251, 248, 25);
		contentPane.add(comboBox);
		
		btnNewButton_1 = new JButton("Cancelar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
			dispose();
			}
		});
		btnNewButton_1.setBounds(70, 300, 84, 20);
		contentPane.add(btnNewButton_1);
		
		chckbxNewCheckBox = new JCheckBox("¿Es primera visita?");
		chckbxNewCheckBox.setBounds(217, 286, 202, 49);
		contentPane.add(chckbxNewCheckBox);

	}
	
	public Mantenimiento(VentanaPrincipal ventanaPrincipal) {
		this();
		this.ventanaPrincipal = ventanaPrincipal;
		setTitle("Agendar cita");
	}

	public Mantenimiento(VentanaPrincipal ventanaPrincipal, Citas citaExistente) {
		this(ventanaPrincipal);
		this.citaEnEdicion = citaExistente;

		textField_3.setText(String.valueOf(citaExistente.getId()));
		textField.setText(citaExistente.getNombreCompleto());
		textField_2.setText(citaExistente.getDescripcionServicio());
		textField_1.setText(String.valueOf(citaExistente.getMinEstimados()));

		Date fecha = Date.from(citaExistente.getFechaHora()
				.atZone(ZoneId.systemDefault())
				.toInstant());
		spinner.setValue(fecha);

		comboBox.setSelectedItem(citaExistente.getEstado());
		chckbxNewCheckBox.setSelected(citaExistente.isRequiereConfirmacionLlamada());
		comboBox.setEnabled(true);

		setTitle("Editar cita");
	}
}
