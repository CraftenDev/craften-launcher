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
import javax.swing.plaf.ComponentUI;
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
    private transient ElevationEffect elevationEffect;
    private transient RippleEffect rippleEffect;
    private transient BufferedImage previousSkin;
    private transient BufferedImage skin;
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

        setUI(new ComponentUI() {
            @Override
            public boolean contains(JComponent c, int x, int y) {
                int width = getWidth() - MaterialShadow.OFFSET_LEFT - MaterialShadow.OFFSET_RIGHT;
                return new Ellipse2D.Double(MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_TOP, width, width).contains(x, y);
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

        Ellipse2D circle = new Ellipse2D.Double(MaterialShadow.OFFSET_LEFT + 2, MaterialShadow.OFFSET_TOP + 2, width - 4, width - 4);
        g2.setColor(getBackground());
        g2.fill(circle);
        g2.setClip(circle);

        if (previousSkin != null) {
            int skinWidth = width - 20;
            int skinHeight = (int) (1.0 * previousSkin.getHeight() / previousSkin.getWidth() * skinWidth);
            g2.drawImage(previousSkin, (getWidth() - skinWidth) / 2, 18 + MaterialShadow.OFFSET_TOP, skinWidth, skinHeight, null);
            g2.setClip(new Ellipse2D.Double(MaterialShadow.OFFSET_LEFT + (width - avatarDiameter) / 2 - 2, MaterialShadow.OFFSET_TOP + (width - avatarDiameter) / 2 - 2, avatarDiameter + 4, avatarDiameter + 4));
        }

        if (skin != null) {
            int skinWidth = width - 20;
            int skinHeight = (int) (1.0 * skin.getHeight() / skin.getWidth() * skinWidth);
            g2.drawImage(skin, (getWidth() - skinWidth) / 2, 18 + MaterialShadow.OFFSET_TOP, skinWidth, skinHeight, null);
        }

        g2.setClip(circle);
        rippleEffect.paint(g);

        g2.setClip(null);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4));
        g2.draw(new Ellipse2D.Double(MaterialShadow.OFFSET_LEFT + 2, MaterialShadow.OFFSET_TOP + 2, width - 4, width - 4));
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
