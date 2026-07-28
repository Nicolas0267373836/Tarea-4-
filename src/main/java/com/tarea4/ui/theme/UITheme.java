package com.tarea4.ui.theme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Enumeration;

/**
 * Estilos compartidos de la interfaz.
 */
public final class UITheme {

    public static final Color PRIMARY = new Color(37, 99, 235);
    public static final Color PRIMARY_DARK = new Color(30, 64, 175);
    public static final Color NAVY = new Color(15, 23, 42);
    public static final Color TEXT = new Color(30, 41, 59);
    public static final Color MUTED = new Color(100, 116, 139);
    public static final Color BACKGROUND = new Color(241, 245, 249);
    public static final Color BORDER = new Color(203, 213, 225);
    public static final Color DANGER = new Color(220, 38, 38);
    public static final Color SUCCESS = new Color(22, 163, 74);
    public static final Color WHITE = Color.WHITE;

    private static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);

    private UITheme() {
    }

    public static void aplicar() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            // El Look & Feel del sistema sigue siendo funcional.
        }

        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, new javax.swing.plaf.FontUIResource(FONT_NORMAL));
            }
        }

        UIManager.put("OptionPane.messageFont", FONT_NORMAL);
        UIManager.put("OptionPane.buttonFont", FONT_BUTTON);
        UIManager.put("Table.font", FONT_NORMAL);
        UIManager.put("Table.rowHeight", 38);
        UIManager.put("TableHeader.font", FONT_BUTTON);
        UIManager.put("TableHeader.background", new Color(226, 232, 240));
        UIManager.put("TableHeader.foreground", TEXT);
        UIManager.put("nimbusSelectionBackground", PRIMARY);
        UIManager.put("nimbusFocus", PRIMARY);
    }

    public static JButton botonPrimario(String texto) {
        return configurarBoton(texto, PRIMARY, WHITE);
    }

    public static JButton botonSecundario(String texto) {
        return configurarBoton(texto, new Color(226, 232, 240), TEXT);
    }

    public static JButton botonPeligro(String texto) {
        return configurarBoton(texto, DANGER, WHITE);
    }

    public static JButton configurarBoton(String texto, Color fondo, Color colorTexto) {
        JButton button = new JButton(texto);
        button.setFont(FONT_BUTTON);
        button.setBackground(fondo);
        button.setForeground(colorTexto);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        int anchoSeguro = Math.max(120, button.getPreferredSize().width + 24);
        button.setPreferredSize(new Dimension(anchoSeguro, 42));
        button.setMinimumSize(new Dimension(anchoSeguro, 42));
        button.setOpaque(true);
        return button;
    }

    public static void prepararCampo(JComponent component) {
        component.setFont(FONT_NORMAL);
        component.setForeground(TEXT);
        component.setBackground(WHITE);
        component.setPreferredSize(new Dimension(320, 40));
        component.setMinimumSize(new Dimension(180, 40));

        Border interior = BorderFactory.createEmptyBorder(6, 10, 6, 10);
        Border exterior = BorderFactory.createLineBorder(BORDER);
        component.setBorder(BorderFactory.createCompoundBorder(exterior, interior));
    }
}
