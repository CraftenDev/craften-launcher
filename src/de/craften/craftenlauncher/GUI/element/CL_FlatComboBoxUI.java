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
 * CL_FlatComboBoxUI Class:
 * CL_FlatComboBoxRenderer Class:
 *
 * Own ComboBox Renderer to display a flat customizeable ComboBox
 *
 * @author saschb2b
 */

package de.craften.craftenlauncher.GUI.element;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

@SuppressWarnings("serial")
public class CL_FlatComboBoxUI extends BasicComboBoxUI {

    public static CL_FlatComboBoxUI createUI(JComponent c) {
        return new CL_FlatComboBoxUI();
    }

    @Override
    protected ListCellRenderer createRenderer(){
        return new CL_FlatComboBoxRenderer();
    }

    @Override
    protected JButton createArrowButton() {
        return new BasicArrowButton(
                BasicArrowButton.SOUTH,
                Color.lightGray, Color.lightGray,
                new Color(57,57,57), Color.lightGray);
    }
}

@SuppressWarnings("serial")
class CL_FlatComboBoxRenderer extends DefaultListCellRenderer{
    public CL_FlatComboBoxRenderer() {
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus)
    {
        if (list.getSelectionBackground() instanceof ColorUIResource)
        {
            list.setSelectionBackground(Color.gray);
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
