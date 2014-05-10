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
 * Loading Class:
 *
 * Showing the Loading screen to display some useful information about the current download
 *
 * @author saschb2b
 */

package de.craften.craftenlauncher.GUI.panel;

import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.logic.Facade;
import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.vm.DownloadVM;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
public class Loading extends Basic implements Observer {
    JProgressBar pbar = new JProgressBar();
    JLabel info = new JLabel(), traffic = new JLabel();
    boolean wantToStart, isMinecraftDownloaded;

    public Loading(Dimension d) {
        super(d);
        setBackground("/image/LoadingBackground.png");
        setSlider("/image/LoadingSlider.png");

        addProgressBar();
        addInfo();
    }

    public void init() {
        wantToStart = true;

        if (isMinecraftDownloaded)
            startMc();
    }

    private void addProgressBar() {
        pbar.setBorderPainted(false);
        pbar.setMaximum(1100);
        pbar.setValue(0);
        pbar.setSize(this.getWidth() - sliderWidth - 30, 30);
        pbar.setLocation(15, this.getHeight() / 2 - pbar.getHeight() / 2);
        pbar.setForeground(new Color(73, 159, 53));
        pbar.setBackground(new Color(57, 57, 57));
        pbar.setDoubleBuffered(true);
        add(pbar);

        traffic.setLocation(pbar.getLocation().x + 5, pbar.getLocation().y + 30);
        traffic.setSize(pbar.getSize());
        traffic.setForeground(Color.white);
        add(traffic);
    }

    private void addInfo() {
        info.setSize(pbar.getSize());
        info.setForeground(Color.WHITE);
        info.setLocation(new Point(pbar.getX(), pbar.getY() - info.getHeight() - 5));
        add(info);
    }

    private void startMc() {
        try {
            Facade.getInstance().startMinecraft();
        } catch (CraftenLogicException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof DownloadVM) {
            String context = ((DownloadVM) o).getInfo();
            traffic.setText(((DownloadVM) o).getDownloadedKByte() + " KByte");
            if (context != null && !context.equals("")) {
                System.out.println(context);
                try {
                    info.setText(context);
                } catch (Exception e) {
                    Logger.getInstance().logError("GUIAccess info error..");
                    e.printStackTrace();
                }
            }
            try {
                pbar.setValue(((DownloadVM) o).getProgress());
            } catch (Exception e) {
                Logger.getInstance().logError("GUIAccess Progress error..");
                e.printStackTrace();
            }

            isMinecraftDownloaded = ((DownloadVM) o).isMinecraftDownloaded();
            if (wantToStart && isMinecraftDownloaded) {
                startMc();
            }
        }
    }
}
