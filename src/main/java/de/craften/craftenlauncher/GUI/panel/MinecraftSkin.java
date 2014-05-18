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
 * MinecraftSkin Class:
 *
 * Displays a Jpanel with a minecraft skin inside
 *
 * @author saschb2b
 * @author leMaik
 */

package de.craften.craftenlauncher.GUI.panel;

import de.craften.craftenlauncher.logic.Facade;
import de.craften.craftenlauncher.logic.vm.SkinVM;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import static java.awt.EventQueue.invokeLater;

@SuppressWarnings("serial")
public class MinecraftSkin extends JPanel implements Observer {
    private static final Rectangle
            SKIN_HEAD = new Rectangle(8, 8, 8, 8),
            SKIN_HELMET = new Rectangle(40, 8, 8, 8),
            SKIN_BODY = new Rectangle(20, 20, 8, 12),
            SKIN_RIGHT_LEG = new Rectangle(4, 20, 4, 12),
            SKIN_LEFT_LEG = new Rectangle(20, 52, 4, 12),
            SKIN_RIGHT_ARM = new Rectangle(44, 20, 4, 12),
            SKIN_LEFT_ARM = new Rectangle(36, 52, 4, 12),
            SKIN_BODY_OVERLAY = new Rectangle(20, 36, 8, 12),
            SKIN_RIGHT_LEG_OVERLAY = new Rectangle(4, 36, 4, 12),
            SKIN_LEFT_LEG_OVERLAY = new Rectangle(4, 52, 4, 12),
            SKIN_RIGHT_ARM_OVERLAY = new Rectangle(44, 36, 4, 12),
            SKIN_LEFT_ARM_OVERLAY = new Rectangle(52, 52, 4, 12);

    private BufferedImage skin;

    public MinecraftSkin(Dimension d) {
        setPreferredSize(d);
        setLayout(null);
        setBounds(0, 0, getPreferredSize().width, getPreferredSize().height);
        setOpaque(false);

        try {
            skin = ImageIO.read(getClass().getResource("/images/steve.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Facade.getInstance().setSkinObserver(this);
    }

    private BufferedImage getSkinPart(Rectangle rect) {
        return skin.getSubimage(rect.x, rect.y, rect.width, rect.height);
    }

    private BufferedImage getFlippedSkinPart(Rectangle rect) {
        BufferedImage src = getSkinPart(rect);
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-src.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(src, null);
    }

    private Image getBodySkin(BufferedImage skin) {
        BufferedImage body = new BufferedImage(20, 41, BufferedImage.TYPE_INT_ARGB);
        Graphics g = body.createGraphics();
        g.drawImage(getSkinPart(SKIN_HEAD), 4, 1, null);
        g.drawImage(getSkinPart(SKIN_BODY), 4, 9, null);
        g.drawImage(getSkinPart(SKIN_RIGHT_ARM), 0, 9, null);
        g.drawImage(getSkinPart(SKIN_RIGHT_LEG), 4, 21, null);

        if (skin.getHeight() == 64) { //1.8 skin
            g.drawImage(getSkinPart(SKIN_LEFT_ARM), 12, 9, null);
            g.drawImage(getSkinPart(SKIN_LEFT_LEG), 8, 21, null);

            //overlays
            g.drawImage(getSkinPart(SKIN_BODY_OVERLAY), 4, 9, null);
            g.drawImage(getSkinPart(SKIN_LEFT_ARM_OVERLAY), 12, 9, null);
            g.drawImage(getSkinPart(SKIN_RIGHT_ARM_OVERLAY), 0, 9, null);
            g.drawImage(getSkinPart(SKIN_LEFT_LEG_OVERLAY), 8, 21, null);
            g.drawImage(getSkinPart(SKIN_RIGHT_LEG_OVERLAY), 4, 21, null);
        } else {
            //draw flipped arm and leg
            g.drawImage(getFlippedSkinPart(SKIN_RIGHT_ARM), 12, 9, null);
            g.drawImage(getFlippedSkinPart(SKIN_RIGHT_LEG), 8, 21, null);
        }

        //helmet last (so that it isn't under the body overlay)
        g.drawImage(getSkinPart(SKIN_HELMET), 3, 0, 10, 10, null);

        return body;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(getBodySkin(skin), 0, 0, g.getClipBounds().height / 2, g.getClipBounds().height, null);
    }

    @Override
    public void update(final Observable o, Object arg) {
        invokeLater(new Runnable() {

            @Override
            public void run() {
                if (o instanceof SkinVM) {
                    if (((SkinVM) o).wasSkinDownloaded()) {
                        skin = ((SkinVM) o).getSkin();
                        repaint();
                    }
                }
            }
        });
    }
}
