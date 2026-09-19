import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.table.DefaultTableModel;

import DAO.CitasDAO;
import Modelos.Citas;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel modeloTabla;
	private JButton btnNewButton_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal() {
		setTitle("Agenda de citas");
		getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 10));
		getContentPane().setBackground(new Color(124, 252, 0));
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Sistema para agendar citas");
		lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
		lblNewLabel.setBounds(257, 10, 304, 39);
		getContentPane().add(lblNewLabel);

		JButton btnNewButton = new JButton("Agendar cita");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mantenimiento pantalla = new Mantenimiento(VentanaPrincipal.this);
				pantalla.setVisible(true);
			}
		});
		btnNewButton.setBounds(48, 59, 92, 31);
		getContentPane().add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Editar");
		btnNewButton_1.setEnabled(false); // inicia inactivo hasta que se seleccione un registro.
		btnNewButton_1.setBounds(342, 59, 98, 31);
		getContentPane().add(btnNewButton_1);
		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editarCitaSeleccionada();
			}
		});

		btnNewButton_2 = new JButton("Eliminar");
		btnNewButton_2.setEnabled(false); // inicia inactivo hasta que se seleccione un registro.
		btnNewButton_2.setBounds(659, 59, 92, 31);
		getContentPane().add(btnNewButton_2);
		
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eliminarCitaSeleccionada();
			}
		});

		JPanel panel = new JPanel();
		panel.setBounds(10, 119, 801, 320);
		getContentPane().add(panel);
		panel.setLayout(null); 
		
		modeloTabla = new DefaultTableModel(
				new Object[][] {},
				new String[] {
					"Id", "Nombre Completo", "Fecha y Hora", "Descripcion del servicio", "Tiempo aproximado (en minutos)", "Estado de la cita"
				}
			) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};

			table = new JTable(modeloTabla);
			
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						// se habilita "Eliminar y Editar" solo si hay una fila realmente seleccionada
						btnNewButton_2.setEnabled(table.getSelectedRow() != -1);
						btnNewButton_1.setEnabled(table.getSelectedRow() != -1);
					}
				}
			});

			JScrollPane scrollPane = new JScrollPane(table); 
			scrollPane.setBounds(10, 10, 781, 314);
			panel.add(scrollPane);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 835, 486);
		
		//ajuste para el tamaño de las columnas
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 
		table.getColumnModel().getColumn(0).setPreferredWidth(40);   // Id
		table.getColumnModel().getColumn(1).setPreferredWidth(160);  // Nombre Completo
		table.getColumnModel().getColumn(2).setPreferredWidth(120);  // Fecha y Hora
		table.getColumnModel().getColumn(3).setPreferredWidth(230);  // Descripción
		table.getColumnModel().getColumn(4).setPreferredWidth(130);  // Tiempo aproximado
		table.getColumnModel().getColumn(5).setPreferredWidth(120);  // Estado de la cita

		// Cargamos los datos existentes al abrir la ventana
		cargarCitas();
	}

	// Carga las citas desde la base de datos y las muestra en la tabla.
	public void cargarCitas() {
		modeloTabla.setRowCount(0); // limpia la tabla antes de recargar

		try {
			CitasDAO dao = new CitasDAO();
			List<Citas> citas = dao.listarTodos();

			DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

			for (Citas c : citas) {
				modeloTabla.addRow(new Object[]{
					c.getId(),
					c.getNombreCompleto(),
					c.getFechaHora() != null ? c.getFechaHora().format(formato) : "",
					c.getDescripcionServicio(),
					c.getMinEstimados(),
					c.getEstado()
					//c.isRequiereConfirmacionLlamada() ? "Si" : "No"
				});
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
				"Error al cargar las citas desde la base de datos:\n" + e.getMessage(),
				"Error de conexión", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private void eliminarCitaSeleccionada() {
		int filaSeleccionada = table.getSelectedRow();

		if (filaSeleccionada == -1) {
			return;
		}

		int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
		String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

		int confirmacion = JOptionPane.showConfirmDialog(this,
			"¿Estas seguro de eliminar la cita de \"" + nombre + "\" de forma permanente?",
			"Confirmar eliminacion",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE);

		if (confirmacion != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			CitasDAO dao = new CitasDAO();
			int filasAfectadas = dao.borrarCita(id);

			if (filasAfectadas > 0) {
				JOptionPane.showMessageDialog(this, "Cita eliminada correctamente.");
				cargarCitas(); // refresca la tabla
				btnNewButton_2.setEnabled(false); // ya no hay fila seleccionada
				
			}
			else {
				JOptionPane.showMessageDialog(this,
					"No se encontro la cita para eliminar.",
					"Aviso", JOptionPane.WARNING_MESSAGE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
				"Error al eliminar la cita:\n" + e.getMessage(),
				"Error de conexion", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void editarCitaSeleccionada() {
		int filaSeleccionada = table.getSelectedRow();

		if (filaSeleccionada == -1) {
			return;
		}

		int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);

		try {
			CitasDAO dao = new CitasDAO();
			Optional<Citas> citaOpt = dao.buscarPorid(id);

			if (citaOpt.isPresent()) {
				Mantenimiento pantalla = new Mantenimiento(this, citaOpt.get());
				pantalla.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this,
					"No se encontro la cita seleccionada.",
					"Aviso", JOptionPane.WARNING_MESSAGE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
				"Error al buscar la cita:\n" + e.getMessage(),
				"Error de conexion", JOptionPane.ERROR_MESSAGE);
		}
	}
}