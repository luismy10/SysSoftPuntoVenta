package controller;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SysSoft extends Application {

    public static Pane pane;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = getClass().getResource(Tools.FX_FILE_INICIO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        FxInicioController controller = fXMLLoader.getController();
        Scene scene = new Scene(parent);
        primaryStage.getIcons().add(new Image(Tools.FX_LOGO));
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("Sys Soft");
        primaryStage.centerOnScreen();
        primaryStage.setMaximized(true);
        primaryStage.show();
        primaryStage.requestFocus();
        controller.initWindowSize();
        pane = new Pane();
        Session.WIDTH_WINDOW = scene.getWidth();
        Session.HEIGHT_WINDOW=scene.getHeight();
        //controller.fillEmployeeTable();
    }

    public static void main(String[] args) {
        launch(args);

    }

}
