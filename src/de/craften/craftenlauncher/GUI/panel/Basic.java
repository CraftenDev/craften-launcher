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
 * Basic Class:
 *
 * Template class for every Panel with an optional clickable slider
 *
 * @author saschb2b
 */
package de.craften.craftenlauncher.GUI.panel;

import de.craften.craftenlauncher.GUI.Manager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class Basic extends JPanel {
    BufferedImage background, slider;
    Point mousePointer;
    boolean isSliderClickable;

    final int sliderWidth = 70;

    public Basic(Dimension d) {
        setVisible(false);
        setPreferredSize(d);
        setLayout(null);
        setBounds(0, 0, d.width, d.height);

        addMoveFunction();
    }

    public void setBackground(String path) {
        try {
            background = ImageIO.read(getClass().getResource(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSlider(String path) {
        try {
            slider = ImageIO.read(getClass().getResource(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSliderClickable(boolean isSliderClickable) {
        this.isSliderClickable = isSliderClickable;
    }

    public void sliderAction(){ }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        g.drawImage(slider, 0, 0, null);
    }

    public void addMoveFunction() {
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Manager.getInstance().setLocation(e.getXOnScreen() - mousePointer.x, e.getYOnScreen() - mousePointer.y);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (isSliderClickable && getX() >= 0 && e.getX() >= getWidth() - sliderWidth && e.getX() <= getWidth())
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                else
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isSliderClickable && getX() >= 0 && e.getX() >= getWidth() - sliderWidth && e.getX() <= getWidth()) {
                    sliderAction();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mousePointer = e.getPoint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }
}
