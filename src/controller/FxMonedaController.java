package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FxMonedaController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private TableView<?> tvList;
    @FXML
    private TableColumn<?, ?> tcMoneda;
    @FXML
    private TableColumn<?, ?> tcTipoCambio;
    @FXML
    private TableColumn<?, ?> tcAbreviatura;
    @FXML
    private TableColumn<?, ?> tcPredeterminado;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void InitializationTransparentBackground() {
        Session.pane.setStyle("-fx-background-color: black");
        Session.pane.setTranslateX(0);
        Session.pane.setTranslateY(0);
        Session.pane.setPrefWidth(Session.WIDTH_WINDOW);
        Session.pane.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.pane.setOpacity(0.7f);
        content.getChildren().add(Session.pane);
    }

    private void openWindowMoney() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_MONEDAPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxMonedaProcesoController controller = fXMLLoader.getController();
        controller.setInitMoneyController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Registre su moneda", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();
        
    }

    @FXML
    private void onKeyPressedAdd(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowMoney();
        }
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        openWindowMoney();
    }

    @FXML
    private void onKeyPressedEdit(KeyEvent event) {

    }

    @FXML
    private void onActionEdit(ActionEvent event) {

    }

    @FXML
    private void onKeyPressedRemove(KeyEvent event) {

    }

    @FXML
    private void onActionRemove(ActionEvent event) {

    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
