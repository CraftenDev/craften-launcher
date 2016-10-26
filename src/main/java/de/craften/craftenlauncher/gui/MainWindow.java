package de.craften.craftenlauncher.gui;

import de.craften.craftenlauncher.gui.panel.*;
import de.craften.craftenlauncher.logic.Facade;
import de.craften.ui.swingmaterial.toast.ToastBar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainWindow extends JDialog {
    private static final Logger LOGGER = LogManager.getLogger(MainWindow.class);
    private static final long serialVersionUID = 1L;
    final ToastBar toastBar;
    private HeaderPanel header;
    private final JPanel body;
    private CardLayout bodyLayout;
    private LoginPanel login;
    private ProfilePanel profile;
    private ProfileWithoutLoginPanel profileWithoutLogin;
    private LoadingPanel loading;
    private static final int height = 453;

    public MainWindow() {
        super((Dialog) null); //show this window in the taskbar, see http://stackoverflow.com/a/25533860
        try {
            setIconImage(new ImageIcon(getClass().getResource("/images/icon.png")).getImage());
        } catch (NullPointerException e) {
            LOGGER.error("Could not load icon", e);
        }

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setSize(new Dimension(376, height));
        setLocationRelativeTo(null);
        setTitle("Craften Launcher");
        setResizable(false);

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());

        header = new HeaderPanel();
        content.add(header, BorderLayout.NORTH);

        JPanel bodyContainer = new JPanel();
        bodyContainer.setLayout(null);
        bodyContainer.setPreferredSize(new Dimension(376, height - 188));

        bodyLayout = new CardLayout(0, 0);
        body = new JPanel();
        body.setLayout(bodyLayout);
        body.setSize(376, height - 188);
        bodyContainer.add(body);

        toastBar = new ToastBar();
        toastBar.setSize(376, 48);
        toastBar.setLocation(0, height - 188 - 48);
        bodyContainer.add(toastBar);
        bodyContainer.setComponentZOrder(toastBar, 0);

        addLayers();
        content.add(bodyContainer, BorderLayout.CENTER);
        add(content);

        pack();
        centerWindow(this);
        setVisible(true);
    }

    public void reset() {
        header.resetSkin();
        body.removeAll();
        addLayers();
        bodyLayout.show(body, "login");
    }

    private void addLayers() {
        login = new LoginPanel();
        body.add(login, "login");

        profile = new ProfilePanel();
        body.add(profile, "profile");

        profileWithoutLogin = new ProfileWithoutLoginPanel();
        body.add(profileWithoutLogin, "profileWithoutLogin");

        loading = new LoadingPanel();
        body.add(loading, "loading");
        Facade.getInstance().setMinecraftDownloadObserver(loading);

        bodyLayout.show(body, "login");
    }

    public void showProfile() {
        profile.init();
        bodyLayout.show(body, "profile");
    }

    public void showProfileWithoutLogin() {
        profileWithoutLogin.init();
        bodyLayout.show(body, "profileWithoutLogin");
    }

    public void showLoadingScreen() {
        loading.init();
        bodyLayout.show(body, "loading");
    }

    private void centerWindow(MainWindow frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((screenSize.getWidth() - getWidth()) / 2);
        int y = (int) ((screenSize.getHeight() - getHeight()) / 2);
        frame.setLocation(x, y);
    }
}
