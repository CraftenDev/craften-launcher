package de.craften.craftenlauncher.gui.panel;

import de.craften.craftenlauncher.exception.CraftenAuthenticationException;
import de.craften.craftenlauncher.exception.CraftenException;
import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.gui.MainController;
import de.craften.craftenlauncher.logic.Facade;
import de.craften.craftenlauncher.logic.manager.TranslationManager;
import de.craften.ui.swingmaterial.*;
import de.craften.ui.swingmaterial.toast.TextToast;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel {
    private static final Logger LOGGER = LogManager.getLogger(LoginPanel.class);
    private MaterialTextField usernameField;
    private MaterialPasswordField passwordField;
    private MaterialButton loginButton;

    public LoginPanel() {
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
            LOGGER.error(e);
        }
    }

    private void doLogin() {
        try {
            MainController.getInstance().performLogin(usernameField.getText(), passwordField.getPassword());
        } catch (CraftenAuthenticationException e) {
            switch (e.getReason()) {
                case DID_NOT_BUY_MINECRAFT:
                    MainController.getInstance().displayToast(new TextToast(TranslationManager.getString("loginFailedBuyMinecraft")));
                    break;
                case USER_CREDENTIALS_ARE_WRONG:
                    MainController.getInstance().displayToast(new TextToast(TranslationManager.getString("loginFailedCredentials")));
                    break;
                default:
                    MainController.getInstance().displayToast(new TextToast(TranslationManager.getString("loginFailed")));
                    break;
            }
            LOGGER.error("Auth failure", e);
        } catch (CraftenException e) {
            LOGGER.error("Unexpected error while authenticating", e);
        }
    }

    private void buildUI() {
        usernameField = new MaterialTextField();
        usernameField.setLabel(TranslationManager.getString("emailLabel"));
        usernameField.setHint(TranslationManager.getString("emailHint"));
        usernameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doLogin();
                } else {
                    loginButton.setEnabled(!usernameField.getText().isEmpty() && passwordField.getPassword().length > 0);
                }
            }
        });
        usernameField.setBounds(0, 0, 240, 72);
        usernameField.setLocation(68, -10);
        add(usernameField);

        passwordField = new MaterialPasswordField();
        passwordField.setLabel(TranslationManager.getString("passwordLabel"));
        passwordField.enableInputMethods(true);
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doLogin();
                } else {
                    loginButton.setEnabled(!usernameField.getText().isEmpty() && passwordField.getPassword().length > 0);
                }
            }
        });
        passwordField.setBounds(0, 0, 240, 72);
        passwordField.setLocation(68, 57);
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
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setText(TranslationManager.getString("loginBtn"));
        loginButton.setBounds(0, 0, 240 + MaterialShadow.OFFSET_LEFT + MaterialShadow.OFFSET_RIGHT,
                36 + MaterialShadow.OFFSET_TOP + MaterialShadow.OFFSET_BOTTOM);
        loginButton.setLocation(68 - MaterialShadow.OFFSET_LEFT, 147 - MaterialShadow.OFFSET_TOP);
        loginButton.setEnabled(!usernameField.getText().isEmpty() && passwordField.getPassword().length > 0);
        add(loginButton);
    }

    private void addHelpLabels() {
        final JLabel usernameOrEmailLink = new JLabel(TranslationManager.getString("usernameOrEmail"));
        usernameOrEmailLink.setFont(Roboto.REGULAR.deriveFont(12f));
        usernameOrEmailLink.setForeground(MaterialColor.MIN_BLACK);
        usernameOrEmailLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        usernameOrEmailLink.setHorizontalAlignment(JLabel.CENTER);
        usernameOrEmailLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent env) {
                openLink("https://help.mojang.com/customer/portal/articles/1233873");
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                usernameOrEmailLink.setForeground(MaterialColor.CYAN_500);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                usernameOrEmailLink.setForeground(MaterialColor.MIN_BLACK);
            }
        });
        usernameOrEmailLink.setLocation(68, loginButton.getY() + loginButton.getHeight() - 10);
        usernameOrEmailLink.setSize(240, 15);
        add(usernameOrEmailLink);
        setComponentZOrder(usernameOrEmailLink, 0);

        final JLabel forgotPasswordLink = new JLabel(TranslationManager.getString("forgotPassword"));
        forgotPasswordLink.setFont(Roboto.REGULAR.deriveFont(12f));
        forgotPasswordLink.setForeground(MaterialColor.MIN_BLACK);
        forgotPasswordLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPasswordLink.setHorizontalAlignment(JLabel.CENTER);
        forgotPasswordLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent env) {
                openLink("https://help.mojang.com/customer/portal/articles/329524-change-or-forgot-password");
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                forgotPasswordLink.setForeground(MaterialColor.CYAN_500);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                forgotPasswordLink.setForeground(MaterialColor.MIN_BLACK);
            }
        });
        forgotPasswordLink.setLocation(68, loginButton.getY() + loginButton.getHeight() + 5);
        forgotPasswordLink.setSize(240, 15);
        add(forgotPasswordLink);
        setComponentZOrder(forgotPasswordLink, 0);
    }

    private static void openLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            LOGGER.warn("Could not open link", e);
        }
    }
}
