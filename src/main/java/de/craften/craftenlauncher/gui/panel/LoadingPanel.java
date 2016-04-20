package de.craften.craftenlauncher.gui.panel;

import de.craften.craftenlauncher.gui.MainController;
import de.craften.craftenlauncher.logic.manager.TranslationManager;
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
        setLayout(null);
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
        pbar.setSize(50, 50);
        pbar.setForeground(MaterialColor.CYAN_500);
        pbar.setLocation((376 - 50) / 2, 60);
        pbar.setBounds((376 - 50) / 2, 60, 50, 50);
        add(pbar);
    }

    private void addInfo() {
        info.setForeground(Color.BLACK);
        info.setFont(Roboto.REGULAR.deriveFont(12f));
        info.setHorizontalAlignment(JLabel.CENTER);
        info.setSize(240, 15);
        info.setLocation(68, 130);
        add(info);

        traffic.setForeground(Color.BLACK);
        traffic.setFont(Roboto.REGULAR.deriveFont(12f));
        traffic.setHorizontalAlignment(JLabel.CENTER);
        traffic.setSize(240, 15);
        traffic.setLocation(68, 145);
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
                    info.setText(TranslationManager.getString("downloadingFile", context));
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
