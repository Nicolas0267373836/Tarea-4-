package com.tarea4.ui;

import com.tarea4.model.Usuario;
import com.tarea4.service.UsuarioService;
import com.tarea4.session.SesionUsuario;
import com.tarea4.ui.components.Dialogs;
import com.tarea4.ui.components.RoundedPanel;
import com.tarea4.ui.theme.UITheme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla principal con listado y operaciones CRUD.
 */
public final class DashboardFrame extends JFrame {

    private final UsuarioService usuarioService;
    private final UsuarioTableModel tableModel = new UsuarioTableModel();
    private final JTable table = new JTable(tableModel);
    private final JLabel sessionLabel = new JLabel();
    private final JLabel countLabel = new JLabel("0 usuarios");

    public DashboardFrame(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;

        setTitle("Sistema de Usuarios - Panel principal");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(940, 600));
        setSize(1120, 700);
        setLocationRelativeTo(null);
        setContentPane(crearContenido());

        cargarUsuarios();
    }

    private JPanel crearContenido() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BACKGROUND);
        root.add(crearHeader(), BorderLayout.NORTH);
        root.add(crearCentro(), BorderLayout.CENTER);
        return root;
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel marca = new JPanel();
        marca.setOpaque(false);
        marca.setLayout(new BoxLayout(marca, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Clientes registrados");
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(UITheme.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        countLabel.setForeground(new java.awt.Color(148, 163, 184));
        countLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 24));
        countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        countLabel.setPreferredSize(new Dimension(190, 20));
        marca.add(title);
        marca.add(Box.createVerticalStrut(4));
        marca.add(countLabel);
        header.add(marca, BorderLayout.WEST);

        JPanel session = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        session.setOpaque(false);
        sessionLabel.setForeground(new java.awt.Color(226, 232, 240));
        sessionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sessionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 18));
        sessionLabel.setPreferredSize(new Dimension(190, 42));
        sessionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        JButton logoutButton = UITheme.botonSecundario("Cerrar sesión");
        logoutButton.addActionListener(event -> cerrarSesion());
        session.add(sessionLabel);
        session.add(logoutButton);
        header.add(session, BorderLayout.EAST);

        actualizarEtiquetaSesion();
        return header;
    }

    private JPanel crearCentro() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UITheme.BACKGROUND);
        wrapper.setBorder(BorderFactory.createEmptyBorder(28, 30, 30, 30));

        RoundedPanel card = new RoundedPanel(22, UITheme.WHITE);
        card.setLayout(new BorderLayout(0, 18));
        card.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        card.add(crearToolbar(), BorderLayout.NORTH);
        card.add(crearTabla(), BorderLayout.CENTER);
        card.add(crearAcciones(), BorderLayout.SOUTH);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        wrapper.add(card, constraints);
        return wrapper;
    }

    private JPanel crearToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(18, 0));
        toolbar.setOpaque(false);

        JLabel instruction = new JLabel("Selecciona una fila para actualizar o eliminar");
        instruction.setForeground(UITheme.MUTED);
        instruction.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        instruction.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 24));
        toolbar.add(instruction, BorderLayout.WEST);

        return toolbar;
    }

    private JScrollPane crearTabla() {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new java.awt.Color(226, 232, 240));
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setForeground(UITheme.TEXT);
        table.setSelectionBackground(new java.awt.Color(219, 234, 254));
        table.setSelectionForeground(UITheme.PRIMARY_DARK);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        for (int column = 0; column < table.getColumnCount(); column++) {
            table.getColumnModel().getColumn(column).setCellRenderer(renderer);
        }
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(125);
        table.getColumnModel().getColumn(1).setPreferredWidth(125);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setPreferredWidth(220);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        scrollPane.getViewport().setBackground(UITheme.WHITE);
        return scrollPane;
    }

    private JPanel crearAcciones() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        JButton newButton = UITheme.botonPrimario("Nuevo usuario");
        JButton updateButton = UITheme.botonSecundario("Actualizar");
        JButton deleteButton = UITheme.botonPeligro("Eliminar");

        newButton.addActionListener(event -> abrirNuevo());
        updateButton.addActionListener(event -> abrirActualizar());
        deleteButton.addActionListener(event -> eliminarSeleccionado());

        actions.add(newButton);
        actions.add(updateButton);
        actions.add(deleteButton);
        return actions;
    }

    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarTodos();
            tableModel.setUsuarios(usuarios);
            countLabel.setText(usuarios.size() + " registrados");
            sincronizarSesion(usuarios);
        } catch (Exception exception) {
            Dialogs.error(this, exception);
        }
    }

    private void sincronizarSesion(List<Usuario> usuarios) {
        SesionUsuario.getInstance().getUsuarioActual().ifPresent(actual -> {
            usuarios.stream()
                    .filter(usuario -> usuario.getId().equals(actual.getId()))
                    .findFirst()
                    .ifPresent(usuario -> SesionUsuario.getInstance().iniciar(usuario));
        });
        actualizarEtiquetaSesion();
    }

    private void actualizarEtiquetaSesion() {
        String text = SesionUsuario.getInstance()
                .getUsuarioActual()
                .map(usuario -> "Sesión: @" + usuario.getUsuario())
                .orElse("Sin sesión");
        sessionLabel.setText(text);
    }

    private void abrirNuevo() {
        new UsuarioFormDialog(
                this,
                usuarioService,
                UsuarioFormDialog.Modo.NUEVO,
                null,
                this::cargarUsuarios
        ).setVisible(true);
    }

    private void abrirActualizar() {
        Usuario seleccionado = obtenerSeleccionado();
        if (seleccionado == null) {
            mostrarSeleccionRequerida("actualizar");
            return;
        }

        new UsuarioFormDialog(
                this,
                usuarioService,
                UsuarioFormDialog.Modo.EDICION,
                seleccionado,
                this::cargarUsuarios
        ).setVisible(true);
    }

    private void eliminarSeleccionado() {
        Usuario seleccionado = obtenerSeleccionado();
        if (seleccionado == null) {
            mostrarSeleccionRequerida("eliminar");
            return;
        }

        int option = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar a "
                        + seleccionado.getNombreCompleto()
                        + " (@" + seleccionado.getUsuario() + ")?\n\n"
                        + "Esta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            usuarioService.eliminar(seleccionado.getId());
            boolean esUsuarioActual = SesionUsuario.getInstance()
                    .getUsuarioActual()
                    .map(actual -> actual.getId().equals(seleccionado.getId()))
                    .orElse(false);

            if (esUsuarioActual) {
                Dialogs.exito(
                        this,
                        "Tu usuario fue eliminado. La sesión se cerrará."
                );
                cerrarSesion();
            } else {
                Dialogs.exito(this, "El usuario fue eliminado correctamente.");
                cargarUsuarios();
            }
        } catch (Exception exception) {
            Dialogs.error(this, exception);
        }
    }

    private Usuario obtenerSeleccionado() {
        int selectedViewRow = table.getSelectedRow();
        if (selectedViewRow < 0) {
            return null;
        }
        int modelRow = table.convertRowIndexToModel(selectedViewRow);
        return tableModel.getUsuario(modelRow);
    }

    private void mostrarSeleccionRequerida(String accion) {
        JOptionPane.showMessageDialog(
                this,
                "Selecciona un usuario de la tabla para " + accion + ".",
                "Selecciona un usuario",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void cerrarSesion() {
        SesionUsuario.getInstance().cerrar();
        dispose();
        new LoginFrame(usuarioService).setVisible(true);
    }

    private static final class UsuarioTableModel extends AbstractTableModel {

        private static final String[] COLUMNAS = {
                "Nombre",
                "Apellido",
                "Teléfono",
                "Correo electrónico",
                "Usuario"
        };

        private List<Usuario> usuarios = new ArrayList<>();

        @Override
        public int getRowCount() {
            return usuarios.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMNAS.length;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNAS[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Usuario usuario = usuarios.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> usuario.getNombre();
                case 1 -> usuario.getApellido();
                case 2 -> usuario.getTelefono();
                case 3 -> usuario.getCorreo();
                case 4 -> usuario.getUsuario();
                default -> "";
            };
        }

        public void setUsuarios(List<Usuario> usuarios) {
            this.usuarios = new ArrayList<>(usuarios);
            fireTableDataChanged();
        }

        public Usuario getUsuario(int rowIndex) {
            return usuarios.get(rowIndex);
        }
    }
}
