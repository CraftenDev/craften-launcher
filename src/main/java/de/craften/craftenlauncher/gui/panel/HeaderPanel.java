package de.craften.craftenlauncher.gui.panel;

import de.craften.craftenlauncher.gui.MainController;
import de.craften.craftenlauncher.logic.Facade;
import de.craften.craftenlauncher.logic.vm.SkinVM;
import de.craften.ui.swingmaterial.MaterialIconButton;
import de.craften.ui.swingmaterial.MaterialShadow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class HeaderPanel extends JPanel implements Observer {
    private static final Logger LOGGER = LogManager.getLogger(HeaderPanel.class);
    private RoundAvatarPanel avatar;
    private MaterialIconButton logoutButton;

    public HeaderPanel() {
        setPreferredSize(new Dimension(376, 188));
        setLayout(null);
        setBounds(0, 0, 376, 188);
        addBackground();
        addAvatar();
        //addLogoutButton();
        setBackground(Color.WHITE);
        setComponentZOrder(avatar, 0);
        //setComponentZOrder(logoutButton, 1);

        Facade.getInstance().setSkinObserver(this);
    }

    private void addBackground() {
        BufferedImage bg;
        try {
            bg = ImageIO.read(getClass().getResource("/images/header.png"));
        } catch (IOException e) {
            LOGGER.error("Could not load header background image", e);
            return;
        }
        JPanel background = new ImagePanel(bg);
        background.setSize(new Dimension(376, 134));
        background.setLocation(0, 0);
        add(background);
    }

    private void addAvatar() {
        avatar = new RoundAvatarPanel();
        avatar.setSize(new Dimension(130 + MaterialShadow.OFFSET_TOP + MaterialShadow.OFFSET_BOTTOM, 130 + MaterialShadow.OFFSET_TOP + MaterialShadow.OFFSET_BOTTOM));
        avatar.setLocation((getWidth() - avatar.getWidth()) / 2, 58 - MaterialShadow.OFFSET_TOP);
        add(avatar);
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
