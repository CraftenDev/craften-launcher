/**
 * CraftenLauncher is an alternative Launcher for Minecraft developed by Mojang.
 * Copyright (C) 2014  Johannes "redbeard" Busch, Sascha "saschb2b" Becker
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Profile Class:
 * <p>
 * Displays the users information
 *
 * @author saschb2b
 */

package de.craften.craftenlauncher.gui.panel;

import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.gui.MainController;
import de.craften.craftenlauncher.logic.Facade;
import de.craften.craftenlauncher.logic.auth.MinecraftUser;
import de.craften.ui.swingmaterial.MaterialButton;
import de.craften.ui.swingmaterial.MaterialColor;
import de.craften.ui.swingmaterial.MaterialShadow;
import de.craften.ui.swingmaterial.Roboto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class ProfilePanel extends JPanel {
    private static final Logger LOGGER = LogManager.getLogger(ProfilePanel.class);

    public ProfilePanel() {
        setBackground(Color.WHITE);
        setLayout(null);
    }

    public void init() {
        removeAll();
        addProfileInformation();
        repaint();

        if (Facade.getInstance().isQuickPlay()) {
            MainController.getInstance().play();
        }
    }

    private void addProfileInformation() {
        MinecraftUser user = null;
        try {
            user = Facade.getInstance().getUser();
        } catch (CraftenLogicException e) {
            LOGGER.error("Grab username error", e);
        }

        if (user != null) {
            //PlayerName
            JLabel playerName = new JLabel(user.getUsername());
            playerName.setFont(Roboto.MEDIUM.deriveFont(20f));
            playerName.setSize(240, 60);
            playerName.setLocation(68, 2);
            playerName.setVerticalAlignment(JLabel.TOP);
            playerName.setHorizontalAlignment(JLabel.CENTER);
            playerName.setForeground(MaterialColor.LIGHT_BLACK);
            add(playerName);

            JLabel playerMail = new JLabel(user.getEmail());
            playerMail.setFont(Roboto.REGULAR.deriveFont(12f));
            playerMail.setSize(240, 60);
            playerMail.setLocation(68, 26);
            playerMail.setVerticalAlignment(JLabel.TOP);
            playerMail.setHorizontalAlignment(JLabel.CENTER);
            playerMail.setForeground(MaterialColor.MIN_BLACK);
            add(playerMail);
        }

        //TODO this logic should not be here
        try {
            if (Facade.getInstance().getMinecraftArguments().containsKey("version")) {
                Facade.getInstance().setMinecraftVersion(Facade.getInstance().getMinecraftArgument("version"));
            } else {
                Facade.getInstance().setMinecraftVersion(Facade.getInstance().getMinecraftVersions().get(0));
            }
        } catch (CraftenLogicException e) {
            LOGGER.error(e);
        }

        final JButton playButton = new MaterialButton();
        playButton.setText("Play");
        playButton.setBackground(MaterialColor.CYAN_500);
        playButton.setForeground(Color.WHITE);
        playButton.setSize(240 + MaterialShadow.OFFSET_LEFT + MaterialShadow.OFFSET_RIGHT, 36 + MaterialShadow.OFFSET_TOP + MaterialShadow.OFFSET_BOTTOM);
        playButton.setLocation(68 - MaterialShadow.OFFSET_LEFT, 81 - MaterialShadow.OFFSET_TOP);
        playButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainController.getInstance().play();
            }
        });
        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setComponentZOrder(playButton, 0);
            }
        });
        add(playButton);
        try {
            //Auto-Connect IP
            if (Facade.getInstance().getMinecraftArguments().containsKey("server")) {
                String serverAddress = Facade.getInstance().getMinecraftArgument("server");
                if (serverAddress.length() > 21) {
                    serverAddress = serverAddress.substring(0, 20) + "\u2026";
                }
                playButton.setText("Join " + serverAddress);
            }
        } catch (CraftenLogicException e) {
            LOGGER.error("Could not get server argument", e);
        }

        final MaterialButton logoutButton = new MaterialButton();
        logoutButton.setText("Logout");
        logoutButton.setForeground(MaterialColor.CYAN_500);
        logoutButton.setRippleColor(MaterialColor.CYAN_500);
        logoutButton.setBackground(MaterialColor.TRANSPARENT);
        logoutButton.setType(MaterialButton.Type.FLAT);
        logoutButton.setSize(240 + MaterialShadow.OFFSET_LEFT + MaterialShadow.OFFSET_RIGHT, 36 + MaterialShadow.OFFSET_TOP + MaterialShadow.OFFSET_BOTTOM);
        logoutButton.setLocation(68 - MaterialShadow.OFFSET_LEFT, 127 - MaterialShadow.OFFSET_TOP);
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainController.getInstance().logout();
            }
        });
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setComponentZOrder(logoutButton, 0);
            }
        });
        add(logoutButton);

        //Version
        try {
            JLabel versionLabel = new JLabel("Version: " + Facade.getInstance().getMinecraftVersion().getVersion());
            versionLabel.setFont(Roboto.REGULAR.deriveFont(12f));
            versionLabel.setSize(240, 30);
            versionLabel.setLocation(68, logoutButton.getY() + logoutButton.getHeight() - 10);
            versionLabel.setForeground(MaterialColor.MIN_BLACK);
            versionLabel.setHorizontalAlignment(JLabel.LEFT);
            add(versionLabel);
        } catch (CraftenLogicException e) {
            e.printStackTrace();
        }

        //RAM
        try {
            String ram;
            if (Facade.getInstance().getMinecraftArguments().containsKey("xmx")) {
                ram = Facade.getInstance().getMinecraftArgument("xmx").toUpperCase();
            } else {
                ram = "~" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M";
            }
            JLabel ramLabel = new JLabel("RAM: " + ram);
            ramLabel.setFont(Roboto.REGULAR.deriveFont(12f));
            ramLabel.setSize(240, 30);
            ramLabel.setLocation(68, logoutButton.getY() + logoutButton.getHeight() - 10);
            ramLabel.setForeground(MaterialColor.MIN_BLACK);
            ramLabel.setHorizontalAlignment(JLabel.RIGHT);
            add(ramLabel);
        } catch (CraftenLogicException e) {
            e.printStackTrace();
        }
    }
}
