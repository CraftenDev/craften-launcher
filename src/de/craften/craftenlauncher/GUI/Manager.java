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
 * Manager Class:
 *
 * Manages the displayed panels
 *
 * @author saschb2b
 */

package de.craften.craftenlauncher.GUI;

import de.craften.craftenlauncher.GUI.panel.Loading;
import de.craften.craftenlauncher.GUI.panel.Login;
import de.craften.craftenlauncher.GUI.panel.Profile;
import de.craften.craftenlauncher.logic.Facade;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingUtilities.invokeLater;

@SuppressWarnings("serial")
public class Manager extends JFrame {
    private static final long serialVersionUID = 1L;
    private static Manager instance = null;
    private int _width = 450;
    private int _height = 250;
    private Login login;
    private Profile profile;
    private Loading loading;

    public static synchronized Manager getInstance() {
        if (instance == null)
            instance = new Manager();
        return instance;
    }

    private Manager() {
        try {
            setIconImage(new ImageIcon(getClass().getResource("/image/icon.png")).getImage());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        setSize(_width, _height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        centreWindow(this);
        setUndecorated(true);
        setVisible(true);
        setLayout(null);

        addLayers();
    }

    public void reset() {
        remove(profile);
        remove(login);
        remove(loading);

        addLayers();

        repaint();

        init();
    }

    private void addLayers(){
        login = new Login(new Dimension(_width, _height));
        add(login);
        login.setVisible(true);

        profile = new Profile(new Dimension(_width, _height));
        add(profile);

        loading = new Loading(new Dimension(_width, _height));
        add(loading);

        Facade.getInstance().setMinecraftDownloadObserver(loading);
    }

    public void init() {
        login.init();
    }

    public void showProfile() {
        login.setVisible(false);
        profile.setVisible(true);
        profile.init();
        try {
            setIconImage(new ImageIcon(getClass().getResource("/image/icon2.png")).getImage());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void showLoadingScreen() {
        profile.setVisible(false);
        loading.init();
        loading.setVisible(true);

    }

    private void centreWindow(Manager frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - _width) / 2);
        int y = (int) ((dimension.getHeight() - _height) / 2);
        frame.setLocation(x, y);
    }
}
