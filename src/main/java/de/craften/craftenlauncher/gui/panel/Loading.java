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
 * Loading Class:
 * <p>
 * Showing the Loading screen to display some useful information about the current download
 *
 * @author saschb2b
 */

package de.craften.craftenlauncher.gui.panel;

import de.craften.craftenlauncher.gui.MainController;
import de.craften.craftenlauncher.logic.Logger;
import de.craften.craftenlauncher.logic.vm.DownloadVM;
import de.craften.ui.swingmaterial.MaterialColor;
import de.craften.ui.swingmaterial.MaterialProgressSpinner;
import de.craften.ui.swingmaterial.Roboto;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
public class Loading extends JPanel implements Observer {
    private MaterialProgressSpinner pbar = new MaterialProgressSpinner();
    private JLabel info = new JLabel(), traffic = new JLabel();
    private boolean wantToStart, isMinecraftDownloaded;

    public Loading() {
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        addProgressBar();
        addInfo();
        add(Box.createVerticalGlue());
        add(Box.createVerticalGlue());
    }

    public void init() {
        wantToStart = true;

        if (isMinecraftDownloaded) {
            MainController.getInstance().startMinecraft();
        }
    }

    private void addProgressBar() {
        pbar.setPreferredSize(new Dimension(50, 50));
        pbar.setForeground(MaterialColor.CYAN_500);
        JPanel pbarWrapper = new JPanel();
        pbarWrapper.setPreferredSize(new Dimension(50, 50));
        pbarWrapper.setBackground(Color.WHITE);
        pbarWrapper.add(pbar);
        pbarWrapper.setAlignmentX(CENTER_ALIGNMENT);
        add(pbarWrapper);
    }

    private void addInfo() {
        info.setForeground(Color.BLACK);
        info.setFont(Roboto.REGULAR.deriveFont(12f));
        info.setAlignmentX(CENTER_ALIGNMENT);
        add(info);

        traffic.setForeground(Color.BLACK);
        traffic.setFont(Roboto.REGULAR.deriveFont(12f));
        traffic.setAlignmentX(CENTER_ALIGNMENT);
        add(traffic);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof DownloadVM) {
            String context = ((DownloadVM) o).getInfo();
            traffic.setText(((DownloadVM) o).getDownloadedKByte() + " KB");
            if (context != null && !context.equals("")) {
                System.out.println(context);
                try {
                    info.setText(context);
                } catch (Exception e) {
                    Logger.logError("GUIAccess info error..");
                    e.printStackTrace();
                }
            }

            isMinecraftDownloaded = ((DownloadVM) o).isMinecraftDownloaded();
            if (wantToStart && isMinecraftDownloaded) {
                MainController.getInstance().startMinecraft();
            }
        }
    }
}
