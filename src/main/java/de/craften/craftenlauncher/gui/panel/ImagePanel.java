package de.craften.craftenlauncher.gui.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A panel that displays an image.
 */
public class ImagePanel extends JPanel {
    private transient BufferedImage image;

    public ImagePanel(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }
}
