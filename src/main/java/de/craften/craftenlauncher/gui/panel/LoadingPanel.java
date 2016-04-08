package de.craften.craftenlauncher.gui.panel;

import de.craften.craftenlauncher.gui.MainController;
import de.craften.craftenlauncher.logic.vm.DownloadVM;
import de.craften.ui.swingmaterial.MaterialColor;
import de.craften.ui.swingmaterial.MaterialProgressSpinner;
import de.craften.ui.swingmaterial.Roboto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
public class LoadingPanel extends JPanel implements Observer {
    private static final Logger LOGGER = LogManager.getLogger(LoadingPanel.class);
    private MaterialProgressSpinner pbar = new MaterialProgressSpinner();
    private JLabel info = new JLabel(), traffic = new JLabel();
    private boolean wantToStart, isMinecraftDownloaded;

    public LoadingPanel() {
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
                LOGGER.debug(context);
                try {
                    info.setText(context);
                } catch (Exception e) {
                    LOGGER.error("Error while accessing GUI", e);
                }
            }

            isMinecraftDownloaded = ((DownloadVM) o).isMinecraftDownloaded();
            if (wantToStart && isMinecraftDownloaded) {
                MainController.getInstance().startMinecraft();
            }
        }
    }
}
