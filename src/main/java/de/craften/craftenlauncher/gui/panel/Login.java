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
 * Login Class:
 * <p>
 * Showing the Loading screen to display some useful information about the current download
 *
 * @author saschb2b
 */

package de.craften.craftenlauncher.gui.panel;

import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.gui.MainController;
import de.craften.craftenlauncher.logic.Facade;
import de.craften.ui.swingmaterial.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("serial")
public class Login extends JPanel {
    private static final Logger LOGGER = LogManager.getLogger(Login.class);
    private MaterialTextField usernameField;
    private MaterialPasswordField passwordField;
    private MaterialButton loginButton;
    private JLabel errorLabel;

    public Login() {
        setBackground(Color.WHITE);
        setLayout(null);

        buildUI();
        addHelpLabels();

        try {
            if (Facade.getInstance().getUser() != null && Facade.getInstance().getUser().getEmail() != null) {
                usernameField.setText(Facade.getInstance().getUser().getEmail());
                passwordField.grabFocus();
            }
        } catch (CraftenLogicException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void doLogin() {
        try {
            errorLabel.setText("");
            MainController.getInstance().performLogin(usernameField.getText(), passwordField.getPassword());
        } catch (CraftenLogicException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void buildUI() {
        usernameField = new MaterialTextField();
        usernameField.setLabel("E-mail");
        usernameField.setHint("user@example.com");
        usernameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doLogin();
                }
            }
        });
        usernameField.setBounds(0, 0, 238, 72);
        usernameField.setLocation(69, -10);
        add(usernameField);

        errorLabel = new JLabel();
        errorLabel.setForeground(MaterialColor.RED_500);
        //TODO set location
        add(errorLabel);

        passwordField = new MaterialPasswordField();
        passwordField.setLabel("Password");
        passwordField.enableInputMethods(true);
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doLogin();
                }
            }
        });
        passwordField.setBounds(0, 0, 238, 72);
        passwordField.setLocation(69, 57);
        add(passwordField);

        loginButton = new MaterialButton();
        loginButton.setBackground(MaterialColor.CYAN_500);
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doLogin();
            }
        });
        loginButton.setText("Login");
        loginButton.setBounds(0, 0, 238 + MaterialShadow.OFFSET_LEFT + MaterialShadow.OFFSET_RIGHT,
                36 + MaterialShadow.OFFSET_TOP + MaterialShadow.OFFSET_BOTTOM);
        loginButton.setLocation(69 - MaterialShadow.OFFSET_LEFT, 147 - MaterialShadow.OFFSET_TOP);
        add(loginButton);
    }

    private void addHelpLabels() {
        JLabel usernameOrEmailLink = new JLabel("Username or E-mail?");
        usernameOrEmailLink.setFont(Roboto.REGULAR.deriveFont(11f));
        usernameOrEmailLink.setForeground(MaterialColor.MIN_BLACK);
        usernameOrEmailLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        usernameOrEmailLink.setHorizontalAlignment(JLabel.RIGHT);
        usernameOrEmailLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent env) {
                openLink("https://help.mojang.com/customer/portal/articles/1233873");
            }
        });
        usernameOrEmailLink.setLocation(usernameField.getX() - 8, usernameField.getY() + usernameField.getHeight() + 2);
        usernameOrEmailLink.setLocation(69, loginButton.getY() + loginButton.getHeight() - 5);
        usernameOrEmailLink.setSize(238, 30);
        add(usernameOrEmailLink);
        setComponentZOrder(usernameOrEmailLink, 0);

        JLabel forgotPasswordLink = new JLabel("Forgot password?");
        forgotPasswordLink.setFont(Roboto.REGULAR.deriveFont(11f));
        forgotPasswordLink.setForeground(MaterialColor.MIN_BLACK);
        forgotPasswordLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPasswordLink.setHorizontalAlignment(JLabel.RIGHT);
        forgotPasswordLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent env) {
                openLink("https://help.mojang.com/customer/portal/articles/329524-change-or-forgot-password");
            }
        });
        forgotPasswordLink.setLocation(69, loginButton.getY() + loginButton.getHeight() - 20);
        forgotPasswordLink.setSize(238, 30);
        add(forgotPasswordLink);
        setComponentZOrder(forgotPasswordLink, 0);

        JLabel registerAccountLink = new JLabel("Register account");
        registerAccountLink.setFont(Roboto.REGULAR.deriveFont(11f));
        registerAccountLink.setForeground(MaterialColor.MIN_BLACK);
        registerAccountLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerAccountLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent env) {
                openLink("https://account.mojang.com/register?agent=minecraft");
            }
        });
        registerAccountLink.setLocation(69, loginButton.getY() + loginButton.getHeight() - 20);
        registerAccountLink.setSize(238, 30);
        add(registerAccountLink);
        setComponentZOrder(registerAccountLink, 0);
    }

    private static void openLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            LOGGER.warn("Could not open link", e);
        }
    }
}
