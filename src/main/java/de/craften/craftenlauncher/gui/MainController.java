package de.craften.craftenlauncher.gui;

import de.craften.craftenlauncher.exception.CraftenLogicException;
import de.craften.craftenlauncher.logic.Facade;
import de.craften.craftenlauncher.logic.Logger;

import javax.swing.*;

/**
 * The controller of the launcher.
 */
public class MainController {
    private static MainController instance;
    private MainWindow mainWindow;

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    public void openMainWindow() {
        mainWindow = new MainWindow();
        mainWindow.setVisible(true);

        if (!Facade.getInstance().isForceLogin()) {
            try {
                if (Facade.getInstance().getUser() != null && Facade.getInstance().getUser().getEmail() != null &&
                        !Facade.getInstance().getUser().getEmail().isEmpty()) {
                    MainController.getInstance().performLogin(Facade.getInstance().getUser().getEmail(), null);
                }
            } catch (CraftenLogicException e) {
                Logger.logInfo("Automatic login failed");
            }
        }
    }

    public void performLogin(String username, char[] password) throws CraftenLogicException {
        if (password != null) {
            Facade.getInstance().setUser(username, password);
        }
        Facade.getInstance().authenticateUser();

        mainWindow.showProfile();
    }

    public void logout() {
        Facade.getInstance().logout();
        mainWindow.reset();
    }

    public void play() {
        mainWindow.showLoadingScreen();
    }

    public void startMinecraft() {
        try {
            Facade.getInstance().startMinecraft();
        } catch (CraftenLogicException e) {
            e.printStackTrace();
        }
    }
}
