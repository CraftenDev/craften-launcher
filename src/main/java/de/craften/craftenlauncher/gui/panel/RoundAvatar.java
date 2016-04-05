package de.craften.craftenlauncher.gui.panel;

import de.craften.craftenlauncher.logic.Logger;
import de.craften.ui.swingmaterial.ElevationEffect;
import de.craften.ui.swingmaterial.MaterialShadow;
import de.craften.ui.swingmaterial.RippleEffect;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A round avatar with a (almost) white border.
 */
public class RoundAvatar extends JPanel {
    private ElevationEffect elevationEffect;
    private RippleEffect rippleEffect;
    private BufferedImage skin;

    public RoundAvatar() {
        elevationEffect = ElevationEffect.applyCirularTo(this, 1);
        rippleEffect = RippleEffect.applyTo(this);
        setForeground(Color.WHITE);
        setBackground(Color.decode("#f44336"));
        setOpaque(false);

        try {
            skin = ImageIO.read(getClass().getResource("/images/steve.png"));
        } catch (IOException e) {
            Logger.logWarning("Could not load steve skin");
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                elevationEffect.setLevel(2);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                elevationEffect.setLevel(1);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        elevationEffect.paint(g2);

        int width = getWidth() - MaterialShadow.OFFSET_LEFT - MaterialShadow.OFFSET_RIGHT;
        Ellipse2D outer = new Ellipse2D.Double(MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_TOP, width, width);
        g2.setColor(Color.decode("#f3f6f4"));
        g2.fill(outer);

        Ellipse2D circle = new Ellipse2D.Double(MaterialShadow.OFFSET_LEFT + 5, MaterialShadow.OFFSET_TOP + 5, width - 10, width - 10);
        g2.setColor(getBackground());
        g2.fill(circle);

        g2.setClip(circle);
        if (skin != null) {
            BufferedImage body = MinecraftSkin.getBodySkin(skin);
            int skinWidth = width - 20;
            int skinHeight = (int) (1.0 * body.getHeight() / body.getWidth() * skinWidth);
            g2.drawImage(body, (getWidth() - skinWidth) / 2, 18 + MaterialShadow.OFFSET_TOP, skinWidth, skinHeight, null);
        }

        g2.setClip(outer);
        rippleEffect.paint(g);
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
