package de.craften.craftenlauncher.gui.panel;

import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.gui.MainController;
import de.craften.craftenlauncher.logic.Facade;
import de.craften.craftenlauncher.logic.manager.TranslationManager;
import de.craften.ui.swingmaterial.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class ProfileWithoutLoginPanel extends JPanel {
    private static final Logger LOGGER = LogManager.getLogger(ProfileWithoutLoginPanel.class);

    public ProfileWithoutLoginPanel() {
        setBackground(Color.WHITE);
        setLayout(null);
    }

    public void init() {
        removeAll();
        addProfileInformation();
        repaint();
    }

    private void addProfileInformation() {
        final JButton playButton = new MaterialButton();
        final MaterialTextField playerNameField = new MaterialTextField();
        playerNameField.setLabel(TranslationManager.getString("usernameLabel"));
        playerNameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if(!playerNameField.getText().isEmpty()) {
                        MainController.getInstance().playWithoutLogin(playerNameField.getText());
                    }
                } else {
                    playButton.setEnabled(!playerNameField.getText().isEmpty());
                }
            }
        });
        playerNameField.setBounds(0, 0, 240, 72);
        playerNameField.setLocation(68, 2);
        add(playerNameField);

        //TODO this logic should not be here
        try {
            if (Facade.getInstance().getMinecraftArguments().containsKey("version")) {
                Facade.getInstance().setMinecraftVersion(Facade.getInstance().getMinecraftArgument("version"));
            } else {
                //TODO this is a pretty hacky way to get the latest stable version; it should be possible to check if a version is a release using the json data
                for (String v : Facade.getInstance().getMinecraftVersions()) {
                    if (v.matches("\\d+\\.\\d+(\\.\\d+)?")) {
                        Facade.getInstance().setMinecraftVersion(v);
                        break;
                    }
                }
            }
        } catch (CraftenLogicException e) {
            LOGGER.error(e);
        }

        playButton.setText(TranslationManager.getString("playBtn"));
        playButton.setBackground(MaterialColor.CYAN_500);
        playButton.setForeground(Color.WHITE);
        playButton.setSize(240 + MaterialShadow.OFFSET_LEFT + MaterialShadow.OFFSET_RIGHT, 36 + MaterialShadow.OFFSET_TOP + MaterialShadow.OFFSET_BOTTOM);
        playButton.setLocation(68 - MaterialShadow.OFFSET_LEFT, 88 - MaterialShadow.OFFSET_TOP);
        playButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainController.getInstance().playWithoutLogin(playerNameField.getText());
            }
        });
        playButton.setEnabled(!playerNameField.getText().isEmpty());
        add(playButton);
        try {
            //Auto-Connect IP
            if (Facade.getInstance().getMinecraftArguments().containsKey("server")) {
                String serverAddress = Facade.getInstance().getMinecraftArgument("server");
                if (serverAddress.length() > 21) {
                    serverAddress = serverAddress.substring(0, 20) + "\u2026";
                }
                playButton.setText(TranslationManager.getString("joinServerBtn", serverAddress));
            }
        } catch (CraftenLogicException e) {
            LOGGER.error("Could not get server argument", e);
        }

        final MaterialButton backButton = new MaterialButton();
        backButton.setText(TranslationManager.getString("backBtn"));
        backButton.setForeground(MaterialColor.CYAN_500);
        backButton.setRippleColor(MaterialColor.CYAN_500);
        backButton.setBackground(MaterialColor.TRANSPARENT);
        backButton.setType(MaterialButton.Type.FLAT);
        backButton.setSize(240 + MaterialShadow.OFFSET_LEFT + MaterialShadow.OFFSET_RIGHT, 36 + MaterialShadow.OFFSET_TOP + MaterialShadow.OFFSET_BOTTOM);
        backButton.setLocation(68 - MaterialShadow.OFFSET_LEFT, 132 - MaterialShadow.OFFSET_TOP);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainController.getInstance().logout();
            }
        });
        add(backButton);

        //Version
        try {

            final MaterialComboBox<String> versions = new MaterialComboBox<>();
            for (String version : Facade.getInstance().getMinecraftVersions()) {
                versions.addItem(version);
            }
            versions.setSelectedItem(Facade.getInstance().getMinecraftVersion().getVersion());
            versions.setSize(125, 42);
            versions.setLocation(68, backButton.getY() + backButton.getHeight() - 3);
            versions.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        Facade.getInstance().setMinecraftVersion(String.valueOf(versions.getSelectedItem()));
                    } catch (CraftenLogicException e) {
                        LOGGER.error("Could not set version", e);
                    }
                }
            });
            versions.setVisible(false);
            add(versions);

            final JLabel versionLabel = new JLabel(TranslationManager.getString("versionLabel", Facade.getInstance().getMinecraftVersion().getVersion()));
            versionLabel.setFont(Roboto.REGULAR.deriveFont(12f));
            versionLabel.setSize(125, 30);
            versionLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            versionLabel.setLocation(68, backButton.getY() + backButton.getHeight() + 5);
            versionLabel.setForeground(MaterialColor.MIN_BLACK);
            versionLabel.setHorizontalAlignment(JLabel.LEFT);
            versionLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    versionLabel.setVisible(false);
                    versions.setVisible(true);
                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {
                    versionLabel.setForeground(MaterialColor.CYAN_500);
                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {
                    versionLabel.setForeground(MaterialColor.MIN_BLACK);
                }
            });
            add(versionLabel);
        } catch (CraftenLogicException e) {
            LOGGER.error("Could not get versions", e);
        }

        //RAM
        try {
            String ram;
            if (Facade.getInstance().getMinecraftArguments().containsKey("xmx")) {
                ram = Facade.getInstance().getMinecraftArgument("xmx").toUpperCase();
            } else {
                ram = "~" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M";
            }
            JLabel ramLabel = new JLabel(TranslationManager.getString("ramLabel", ram));
            ramLabel.setFont(Roboto.REGULAR.deriveFont(12f));
            ramLabel.setSize(240, 30);
            ramLabel.setLocation(68, backButton.getY() + backButton.getHeight() + 5);
            ramLabel.setForeground(MaterialColor.MIN_BLACK);
            ramLabel.setHorizontalAlignment(JLabel.RIGHT);
            add(ramLabel);
        } catch (CraftenLogicException e) {
            LOGGER.error("Could not get RAM", e);
        }
    }
}
