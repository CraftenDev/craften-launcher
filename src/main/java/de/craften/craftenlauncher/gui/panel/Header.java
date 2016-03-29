package de.craften.craftenlauncher.gui.panel;

import de.craften.craftenlauncher.logic.Facade;
import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.vm.SkinVM;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import static java.awt.EventQueue.invokeLater;

/**
 * The header panel.
 */
public class Header extends JPanel implements Observer {
    private RoundAvatar avatar;

    public Header() {
        setPreferredSize(new Dimension(375, 188));
        setLayout(null);
        setBounds(0, 0, 375, 188);
        addBackground();
        addAvatar();
        setBackground(Color.WHITE);

        Facade.getInstance().setSkinObserver(this);
    }

    private void addBackground() {
        BufferedImage bg = null;
        try {
            bg = ImageIO.read(getClass().getResource("/images/header.png"));
        } catch (IOException e) {
            Logger.logError("Could not load header background image");
        }
        JPanel background = new ImagePanel(bg);
        background.setSize(new Dimension(375, 134));
        background.setLocation(0, 0);
        background.setBackground(Color.RED);
        add(background);
    }

    private void addAvatar() {
        avatar = new RoundAvatar();
        avatar.setSize(new Dimension(130, 130));
        avatar.setLocation(122, 58);
        add(avatar);
        setComponentZOrder(avatar, 0);
    }

    @Override
    public void update(final Observable o, Object arg) {
        invokeLater(new Runnable() {

            @Override
            public void run() {
                if (o instanceof SkinVM) {
                    if (((SkinVM) o).wasSkinDownloaded()) {
                        avatar.setSkin(((SkinVM) o).getSkin());
                        repaint();
                    }
                }
            }
        });
    }
}
