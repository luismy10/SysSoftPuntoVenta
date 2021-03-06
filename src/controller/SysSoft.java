package controller;

import com.sun.javafx.application.LauncherImpl;
import java.net.URL;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SysSoft extends Application {

    private final int COUNT_LIMIT = 50000;
    private Parent parent;
    private Scene scene;
    private FxLoginController loginController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.getIcons().add(new Image(Tools.FX_LOGO));
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("Sys Soft");
        primaryStage.centerOnScreen();
        primaryStage.setMaximized(true);
        primaryStage.show();
        primaryStage.requestFocus();  
        loginController.initComponents();
    }

    @Override
    public void init() throws Exception {

        URL urllogin = getClass().getResource(Tools.FX_FILE_LOGIN);
        FXMLLoader fXMLLoaderLogin = FxWindow.LoaderWindow(urllogin);
        parent = fXMLLoaderLogin.load(urllogin.openStream());
        loginController = fXMLLoaderLogin.getController();
        Session.PANE = new Pane();
        scene = new Scene(parent);
        for (int i = 0; i < COUNT_LIMIT; i++) {
            double progress = (100 * i) / COUNT_LIMIT;
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
        }

    }

    public static void main(String[] args) {
        LauncherImpl.launchApplication(SysSoft.class, FxPreloader.class, args);
    }

}
