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
 * Manager Class:
 * <p>
 * Manages the displayed panels
 *
 * @author saschb2b
 */

package de.craften.craftenlauncher.gui;

import de.craften.craftenlauncher.gui.panel.Header;
import de.craften.craftenlauncher.gui.panel.Loading;
import de.craften.craftenlauncher.gui.panel.Login;
import de.craften.craftenlauncher.gui.panel.Profile;
import de.craften.craftenlauncher.logic.Facade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("serial")
public class Manager extends JFrame {
    private static final long serialVersionUID = 1L;
    private static Manager instance = null;
    private Header header;
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
            setIconImage(new ImageIcon(getClass().getResource("/images/icon.png")).getImage());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        setSize(375, 445);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        centerWindow(this);
        //setUndecorated(true);
        //setShape(new RoundRectangle2D.Double(0, 0, _width, _height, 3, 3));
        setVisible(true);
        setLayout(new BorderLayout(0, 0));
        setTitle("Craften Launcher");
        setResizable(false);

        addHeader();
        addLayers();

        final AtomicReference<Point> mousePointer = new AtomicReference<>();
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point point = mousePointer.get();
                Manager.getInstance().setLocation(e.getXOnScreen() - point.x, e.getYOnScreen() - point.y);
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePointer.set(e.getPoint());
            }
        });
    }

    public void reset() {
        remove(header);
        remove(profile);
        remove(login);
        remove(loading);

        addHeader();
        addLayers();

        repaint();
        init();
    }

    private void addHeader() {
        header = new Header();
        add(header, BorderLayout.PAGE_START);
    }

    private void addLayers() {
        login = new Login();
        add(login, BorderLayout.CENTER);
        login.setVisible(true);

        profile = new Profile();
        loading = new Loading();

        Facade.getInstance().setMinecraftDownloadObserver(loading);
    }

    public void init() {
        login.init();
    }

    public void showProfile() {
        remove(login);
        remove(loading);
        add(profile, BorderLayout.CENTER);
        profile.init();

        try {
            setIconImage(new ImageIcon(getClass().getResource("/images/icon2.png")).getImage());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void showLoadingScreen() {
        remove(login);
        remove(profile);
        loading.setVisible(true);
        loading.init();
        add(loading, BorderLayout.CENTER);
    }

    private void centerWindow(Manager frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((screenSize.getWidth() - getWidth()) / 2);
        int y = (int) ((screenSize.getHeight() - getHeight()) / 2);
        frame.setLocation(x, y);
    }
}
