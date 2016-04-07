package de.craften.craftenlauncher.gui.panel;

import de.craften.craftenlauncher.gui.util.MinecraftSkin;
import de.craften.ui.swingmaterial.ElevationEffect;
import de.craften.ui.swingmaterial.MaterialShadow;
import de.craften.ui.swingmaterial.RippleEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * A round avatar with a (almost) white border.
 */
public class RoundAvatarPanel extends JPanel {
    private static final Logger LOGGER = LogManager.getLogger(RoundAvatarPanel.class);
    private static final BufferedImage STEVE;
    private ElevationEffect elevationEffect;
    private RippleEffect rippleEffect;
    private BufferedImage previousSkin;
    private BufferedImage skin;
    private int avatarDiameter = -1;

    static {
        BufferedImage steve = null;
        try {
            steve = ImageIO.read(RoundAvatarPanel.class.getResource("/images/steve.png"));
        } catch (IOException e) {
            LOGGER.error("Could not load Steve skin", e);
        }
        STEVE = steve;
    }

    public RoundAvatarPanel() {
        elevationEffect = ElevationEffect.applyCirularTo(this, 1);
        rippleEffect = RippleEffect.applyTo(this);
        skin = MinecraftSkin.getBodySkin(STEVE);
        setForeground(Color.WHITE);
        setBackground(Color.decode("#f44336"));
        setOpaque(false);

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

    public int getAvatarDiameter() {
        return avatarDiameter;
    }

    public void setAvatarDiameter(int avatarDiameter) {
        this.avatarDiameter = avatarDiameter;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth() - MaterialShadow.OFFSET_LEFT - MaterialShadow.OFFSET_RIGHT;
        if (avatarDiameter < 0) {
            avatarDiameter = width - 10;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        elevationEffect.paint(g2);

        Ellipse2D outer = new Ellipse2D.Double(MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_TOP, width, width);
        g2.setColor(Color.decode("#f3f6f4"));
        g2.fill(outer);

        if (previousSkin != null) {
            Ellipse2D circle = new Ellipse2D.Double(MaterialShadow.OFFSET_LEFT + 5, MaterialShadow.OFFSET_TOP + 5, width - 10, width - 10);
            g2.setColor(getBackground());
            g2.fill(circle);
            g2.setClip(circle);

            BufferedImage body = previousSkin;
            int skinWidth = width - 20;
            int skinHeight = (int) (1.0 * body.getHeight() / body.getWidth() * skinWidth);
            g2.drawImage(body, (getWidth() - skinWidth) / 2, 18 + MaterialShadow.OFFSET_TOP, skinWidth, skinHeight, null);
        }

        if (skin != null) {
            Ellipse2D circle = new Ellipse2D.Double(MaterialShadow.OFFSET_LEFT + (width - avatarDiameter) / 2, MaterialShadow.OFFSET_TOP + (width - avatarDiameter) / 2, avatarDiameter, avatarDiameter);
            g2.setColor(getBackground());
            g2.fill(circle);
            g2.setClip(circle);

            int skinWidth = width - 20;
            int skinHeight = (int) (1.0 * skin.getHeight() / skin.getWidth() * skinWidth);
            g2.drawImage(skin, (getWidth() - skinWidth) / 2, 18 + MaterialShadow.OFFSET_TOP, skinWidth, skinHeight, null);
        }

        g2.setClip(outer);
        rippleEffect.paint(g);
    }

    public void setSkin(BufferedImage skin) {
        previousSkin = this.skin;
        this.skin = MinecraftSkin.getBodySkin(skin);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (previousSkin != null) {
                    TimingSource timer = new SwingTimerTimingSource();
                    timer.init();
                    Animator skinAnimator = new Animator.Builder(timer)
                            .addTarget(PropertySetter.getTarget(RoundAvatarPanel.this, "avatarDiameter", 0, getWidth() - MaterialShadow.OFFSET_LEFT - MaterialShadow.OFFSET_RIGHT - 10))
                            .addTarget(new TimingTargetAdapter() {
                                @Override
                                public void end(Animator source) {
                                    previousSkin = null;
                                    repaint();
                                }
                            })
                            .setInterpolator(new SplineInterpolator(0.4, 0, 0.2, 1))
                            .setDuration(250, TimeUnit.MILLISECONDS)
                            .setDisposeTimingSource(true)
                            .build();
                    skinAnimator.start();
                } else {
                    repaint();
                }
            }
        });
    }

    public BufferedImage getSkin() {
        return skin;
    }

    public void resetSkin() {
        setSkin(STEVE);
    }
}
