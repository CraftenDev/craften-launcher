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
 * Profile Class:
 *
 * Displays the users information
 *
 * @author saschb2b
 */

package de.craften.craftenlauncher.GUI.panel;

import de.craften.craftenlauncher.GUI.element.CL_CloseButton;
import de.craften.craftenlauncher.GUI.element.CL_FlatComboBoxUI;
import de.craften.craftenlauncher.GUI.Manager;
import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.logic.Facade;
import de.craften.craftenlauncher.logic.Logger;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Profile extends Basic {
    private JLabel _playerName, _version, _serverIP, _ram, _logout;
    private JComboBox _cbVersions;
    private MinecraftSkin _skin;
    private CL_CloseButton _cbutton;

    private Font _fontPlain = new Font(Font.SANS_SERIF, Font.PLAIN, 16),
            _fontPlainSmall = new Font(Font.SANS_SERIF, Font.PLAIN, 10),
            _fontHeader = new Font(Font.SANS_SERIF, Font.BOLD, 30);

    private Point _pAvatar, _mousePointer = new Point(0, 0);
    private Dimension _dAvatar = new Dimension(100, 100);
    //private BufferedImage avatar;

    public Profile(Dimension d) {
        super(d);

        setBackground("/image/PlayPanel.png");
        setSlider("/image/PlaySlider.png");
        setSliderClickable(true);
    }

    public void init() {
        removeAll();

        addCloseButton();
        addAvatar();
        addProfileInformation();
    }

    public void sliderAction() {
        Manager.getInstance().showLoadingScreen();
    }

    public void addCloseButton() {
        _cbutton = new CL_CloseButton();
        _cbutton.setLocation(this.getWidth() - sliderWidth - _cbutton.getWidth() - 5, 5);
        add(_cbutton);
    }

    public void addAvatar() {
        _pAvatar = new Point(18, this.getHeight() / 4 + 5);
    }

    private void addProfileInformation() {
        int spacerow = 10;

        String username = "";
        try {
            username = Facade.getInstance().getUser().getUsername();
        } catch (CraftenLogicException e) {
            Logger.getInstance().logError("Grab username error");
            e.printStackTrace();
        }

        //PlayerName
        _playerName = new JLabel("Hello, " + username + "!");
        _playerName.setVerticalTextPosition(SwingConstants.CENTER);
        _playerName.setFont(_fontHeader);
        _playerName.setSize(this.getWidth(), 60);
        _playerName.setLocation(18, 0);
        _playerName.setForeground(Color.WHITE);
        add(_playerName);

        _skin = new MinecraftSkin(new Dimension(250,250));
        _skin.setLocation(_pAvatar);
        add(_skin);

        _logout = new JLabel("Logout");
        _logout.setFont(_fontPlainSmall);
        _logout.setSize(new Dimension(40, 20));
        _logout.setLocation(_playerName.getX(), _playerName.getY() + _playerName.getHeight() - 20);
        _logout.setForeground(Color.WHITE);
        _logout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent env) {
                Facade.getInstance().logout();
                Manager.getInstance().reset();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        _logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(_logout);

        //Minecraft Version
        _cbVersions = new JComboBox();
        try {
            _cbVersions.removeAll();
            for (String v : Facade.getInstance().getMinecraftVersions()) {
                _cbVersions.addItem(v);
            }
        } catch (CraftenLogicException e) {
            e.printStackTrace();
        }

        try {
            if (Facade.getInstance().getMinecraftArguments().containsKey("version"))
                _cbVersions.setSelectedItem(Facade.getInstance().getMinecraftVersion().getVersion());
            else
                Facade.getInstance().setMinecraftVersion(_cbVersions.getSelectedItem().toString());
        } catch (CraftenLogicException e) {
            e.printStackTrace();
        }
        _cbVersions.setSize(100, 20);
        _cbVersions.setLocation(this.getWidth() / 2 - _cbVersions.getWidth() / 2, this.getHeight() / 3);
        _cbVersions.setUI(CL_FlatComboBoxUI.createUI(_cbVersions));
        _cbVersions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    _cbVersions.setVisible(false);
                    _version.setVisible(true);
                    _version.setText("Version: " + _cbVersions.getSelectedItem().toString());
                    System.out.println(_cbVersions.getSelectedItem().toString());
                    Facade.getInstance().setMinecraftVersion(_cbVersions.getSelectedItem().toString());
                } catch (CraftenLogicException e1) {
                    e1.printStackTrace();
                }
            }
        });
        add(_cbVersions);
        _cbVersions.setVisible(false);

        _version = new JLabel("Version: " + _cbVersions.getSelectedItem().toString());
        _version.setFont(_fontPlain);
        _version.setSize(this.getWidth() / 2, 15);
        _version.setLocation(_cbVersions.getLocation());
        _version.setForeground(Color.WHITE);
        _version.addMouseListener(new MouseAdapter() {
            Font original;

            @Override
            public void mouseClicked(MouseEvent e) {
                _version.setVisible(false);
                _cbVersions.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        add(_version);

        //Auto-Connect IP
        try {
            if (Facade.getInstance().getMinecraftArguments().containsKey("server"))
                _serverIP = new JLabel("Will join to: " + Facade.getInstance().getMinecraftArgument("server"));
            else
                _serverIP = new JLabel("");
        } catch (CraftenLogicException e) {
            e.printStackTrace();
        }
        _serverIP.setFont(_fontPlain);
        _serverIP.setSize(this.getWidth(), 15);
        _serverIP.setLocation(_cbVersions.getX(), _cbVersions.getY() + _cbVersions.getHeight() + spacerow);
        _serverIP.setForeground(Color.WHITE);
        add(_serverIP);

        //RAM
        try {
            if (Facade.getInstance().getMinecraftArguments().containsKey("xmx"))
                _ram = new JLabel("RAM: " + Facade.getInstance().getMinecraftArgument("xmx").toUpperCase());
            else
                _ram = new JLabel("RAM: ~" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M");
        } catch (CraftenLogicException e) {
            e.printStackTrace();
        }
        _ram.setFont(_fontPlain);
        _ram.setSize(this.getWidth(), 30);
        _ram.setLocation(_serverIP.getX(), _serverIP.getY() + _serverIP.getHeight() + spacerow);
        _ram.setForeground(Color.WHITE);
        add(_ram);
    }
}
