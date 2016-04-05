package de.craften.craftenlauncher.gui.panel;

import de.craften.craftenlauncher.gui.MainController;
import de.craften.craftenlauncher.logic.Facade;
import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.vm.SkinVM;
import de.craften.ui.swingmaterial.MaterialIconButton;
import de.craften.ui.swingmaterial.MaterialShadow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private MaterialIconButton logoutButton;

    public Header() {
        setPreferredSize(new Dimension(375, 188));
        setLayout(null);
        setBounds(0, 0, 375, 188);
        addBackground();
        addAvatar();
        addLogoutButton();
        setBackground(Color.WHITE);
        setComponentZOrder(avatar, 0);
        setComponentZOrder(logoutButton, 1);

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
        add(background);
    }

    private void addAvatar() {
        avatar = new RoundAvatar();
        avatar.setSize(new Dimension(130 + MaterialShadow.OFFSET_TOP + MaterialShadow.OFFSET_BOTTOM, 130 + MaterialShadow.OFFSET_TOP + MaterialShadow.OFFSET_BOTTOM));
        avatar.setLocation((getWidth() - avatar.getWidth()) / 2, 58 - MaterialShadow.OFFSET_TOP);
        add(avatar);
    }

    private void addLogoutButton() {
        logoutButton = new MaterialIconButton();
        logoutButton.setSize(new Dimension(48, 48));
        logoutButton.setLocation(8, 8);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setToolTipText("Logout");
        logoutButton.setEnabled(false);
        try {
            logoutButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/images/logout.png"))));
        } catch (IOException e) {
            Logger.logError("Could not load logout icon");
        }
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainController.getInstance().logout();
            }
        });
        add(logoutButton);
    }

    public void setLogoutEnabled(boolean logoutEnabled) {
        logoutButton.setEnabled(logoutEnabled);
    }

    @Override
    public void update(final Observable o, Object arg) {
        invokeLater(new Runnable() {

            @Override
            public void run() {
                if (o instanceof SkinVM) {
                    if (((SkinVM) o).wasSkinDownloaded()) {
                        avatar.setSkin(((SkinVM) o).getSkin());
                    }
                }
            }
        });
    }

    public void resetSkin() {
        avatar.resetSkin();
    }
}
