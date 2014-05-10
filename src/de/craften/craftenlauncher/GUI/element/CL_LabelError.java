package de.craften.craftenlauncher.GUI.element;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CL_LabelError extends JLabel implements ActionListener {
    private Timer _timer;

    public CL_LabelError() {
        _timer = new Timer(3000, this);
    }

    public void setErrortext(String text){
        super.setText(text);
        setVisible(true);
        _timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        _timer.stop();
    }
}
