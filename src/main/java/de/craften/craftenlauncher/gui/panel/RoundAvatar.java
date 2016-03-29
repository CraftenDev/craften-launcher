package de.craften.craftenlauncher.gui.panel;

import de.craften.craftenlauncher.logic.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A round avatar with a (almost) white border.
 */
public class RoundAvatar extends JPanel {
    private BufferedImage skin;

    public RoundAvatar() {
        setBackground(Color.decode("#f44336"));

        try {
            skin = ImageIO.read(getClass().getResource("/images/steve.png"));
        } catch (IOException e) {
            Logger.logWarning("Could not load steve skin");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(getWidth() / 2, getHeight() / 2, getWidth(), getWidth());

        g2.setColor(Color.decode("#f3f6f4"));
        g2.fill(circle);

        circle.setFrameFromCenter(getWidth() / 2, getHeight() / 2, getWidth() - 5, getWidth() - 5);
        g2.setColor(getBackground());
        g2.fill(circle);

        g2.setClip(circle);
        if (skin != null) {
            BufferedImage body = MinecraftSkin.getBodySkin(skin);
            int skinWidth = getWidth() - 20;
            int skinHeight = (int) (1.0 * body.getHeight() / body.getWidth() * skinWidth);
            g2.drawImage(body, (getWidth() - skinWidth) / 2, 18, skinWidth, skinHeight, null);
        }
    }

    public void setSkin(BufferedImage skin) {
        this.skin = skin;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                repaint();
            }
        });
    }

    public BufferedImage getSkin() {
        return skin;
    }
}
