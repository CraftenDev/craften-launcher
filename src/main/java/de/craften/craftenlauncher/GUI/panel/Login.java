/**
 * CraftenLauncher is an alternative Launcher for Minecraft developed by Mojang.
 * Copyright (C) 2014  Johannes "redbeard" Busch, Sascha "saschb2b" Becker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Login Class:
 *
 * Showing the Loading screen to display some useful information about the current download
 *
 * @author saschb2b
 */

package de.craften.craftenlauncher.GUI.panel;

import de.craften.craftenlauncher.GUI.element.CL_CloseButton;
import de.craften.craftenlauncher.GUI.Manager;
import de.craften.craftenlauncher.GUI.element.CL_LabelError;
import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.logic.Facade;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("serial")
public class Login extends Basic {
    private CL_CloseButton _cbutton;
    private String _defaultUsername = "example@email.com";

    private Font _fontPlain = new Font(Font.SANS_SERIF, Font.PLAIN, 10),
            _fontItalic = new Font(Font.SANS_SERIF, Font.ITALIC, 10),
            _fontHeader = new Font(Font.SANS_SERIF, Font.BOLD, 30);

    private JTextField _Username;
    private JPasswordField _Password;
    private JLabel _login, _withYourMinecraftAccount;
    private CL_LabelError _error;

    private Point _pUsername = new Point(25, 108), _pPassword = new Point(210, 108), _mousePointer = new Point(0, 0);

    private Dimension _dTextField = new Dimension(120, 35);

    private float _alpha = 1f;

    public Login(Dimension d) {
        super(d);
        setBackground("/images/LoginBackground.png");
        setSlider("/images/LoginSlider.png");
        setSliderClickable(true);
    }

    public void init() {
        removeAll();

        addHeader();
        buildUI();
        addHelpLabels();

        if (!Facade.getInstance().isForceLogin()) {
            try {
                if (Facade.getInstance().getUser() != null) {
                    Facade.getInstance().authenticateUser();

                    Manager.getInstance().showProfile();
                }
            } catch (CraftenLogicException e) {
                _error.setErrortext(e.getMessage());
            }
        }
        repaint();
    }

    public void sliderAction() {
        doLogin();
    }

    private void doLogin() {
        String username = null;
        if (!_Username.getText().equals(_defaultUsername)) {
            username = _Username.getText();
        }
        try {
            _error.setText("");
            Facade.getInstance().setUser(username, _Password.getPassword());
            Facade.getInstance().authenticateUser();

            Manager.getInstance().showProfile();
        } catch (CraftenLogicException e) {
            _error.setErrortext(e.getMessage());
        }
    }

    private void addHeader() {
        _login = new JLabel("Login");
        _login.setVerticalTextPosition(SwingConstants.CENTER);
        _login.setFont(_fontHeader);
        _login.setSize(this.getWidth(), 60);
        _login.setLocation(18, 0);
        _login.setForeground(Color.WHITE);
        add(_login);

        _withYourMinecraftAccount = new JLabel("with your Premium Minecraft Account");
        _withYourMinecraftAccount.setVerticalTextPosition(SwingConstants.CENTER);
        _withYourMinecraftAccount.setFont(_fontPlain);
        _withYourMinecraftAccount.setSize(this.getWidth(), 60);
        _withYourMinecraftAccount.setLocation(100, 8);
        _withYourMinecraftAccount.setForeground(Color.WHITE);
        add(_withYourMinecraftAccount);
    }

    private void buildUI() {
        _cbutton = new CL_CloseButton();
        _cbutton.setLocation(this.getWidth() - sliderWidth - _cbutton.getWidth() - 5, 5);
        add(_cbutton);

        _Username = new JTextField();
        _Username.setFont(_fontPlain);
        _Username.setText(_defaultUsername);
        _Username.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (_Username.getText().equals(_defaultUsername)) {
                    _Username.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // TODO Auto-generated method stub
                if (_Username.getText().equals("")) {
                    _Username.setText(_defaultUsername);
                }
            }
        });
        _Username.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doLogin();
                }
            }
        });

        _Username.setBorder(null);
        _Username.setBounds(_pUsername.x, _pUsername.y, _dTextField.width, _dTextField.height);
        add(_Username);

        _error = new CL_LabelError();
        _error.setFont(_fontPlain);
        _error.setForeground(new Color(255, 103, 73));
        _error.setBounds(_Username.getX() - 8, this.getHeight() / 2 - (int) (_dTextField.height * 1.5), this.getWidth(), _dTextField.height);
        add(_error);

        _Password = new JPasswordField();
        _Password.setBorder(null);
        _Password.setBounds(_pPassword.x, _pPassword.y, _dTextField.width, _dTextField.height);
        _Password.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doLogin();
                }
            }
        });
        add(_Password);
    }

    private void addHelpLabels() {
        JLabel _linkUsernameorEmail = new JLabel("Username or E-mail?");
        _linkUsernameorEmail.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent env) {
                try {
                    java.awt.Desktop.getDesktop().browse(new URI("https://help.mojang.com/customer/portal/articles/1233873"));
                } catch (IOException e) {
                } catch (URISyntaxException e) {
                }
            }
        });
        _linkUsernameorEmail.setLocation(_Username.getX() - 8, _Username.getY() + _Username.getHeight() + 2);
        markJLabelLink(_linkUsernameorEmail);
        add(_linkUsernameorEmail);

        JLabel _linkForgotPassword = new JLabel("Forgot password?");
        _linkForgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent env) {
                try {
                    java.awt.Desktop.getDesktop().browse(new URI("https://help.mojang.com/customer/portal/articles/329524-change-or-forgot-password"));
                } catch (IOException e) {
                } catch (URISyntaxException e) {
                }
            }
        });
        _linkForgotPassword.setLocation(_Password.getX() - 8, _Password.getY() + _Password.getHeight() + 2);
        markJLabelLink(_linkForgotPassword);
        add(_linkForgotPassword);


        JLabel _linkRegisterAccount = new JLabel("Register Account");
        _linkRegisterAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent env) {
                try {
                    java.awt.Desktop.getDesktop().browse(new URI("https://account.mojang.com/register?agent=minecraft"));
                } catch (IOException e) {
                } catch (URISyntaxException e) {
                }
            }
        });
        _linkRegisterAccount.setLocation(_linkUsernameorEmail.getX(), this.getHeight() - _linkRegisterAccount.getHeight() - 25);
        markJLabelLink(_linkRegisterAccount);
        add(_linkRegisterAccount);
    }

    private void markJLabelLink(JLabel label) {
        label.setFont(_fontPlain);
        label.setSize(new Dimension(100, 20));
        label.setForeground(Color.WHITE);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
