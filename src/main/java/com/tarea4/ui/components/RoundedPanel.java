package com.tarea4.ui.components;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Panel visual con esquinas redondeadas.
 */
public final class RoundedPanel extends JPanel {

    private final int radio;
    private final Color colorFondo;

    public RoundedPanel(int radio, Color colorFondo) {
        this.radio = radio;
        this.colorFondo = colorFondo;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        graphics2D.setColor(colorFondo);
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
        graphics2D.dispose();
        super.paintComponent(graphics);
    }
}

