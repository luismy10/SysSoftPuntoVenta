package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FxInicioController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ScrollPane vbContent;

    private VBox principal;

    private HBox operaciones;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Load all fxmls in a cache
        try {
            FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_PRINCIPAL));
            principal = fXMLPrincipal.load();
            FXMLLoader fXMLOperaciones = new FXMLLoader(getClass().getResource(Tools.FX_FILE_OPERACIONES));
            operaciones = fXMLOperaciones.load();

            setNode(principal);
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    //Set selected node to a content holder
    private void setNode(Node node) {
        vbContent.setContent(node);
    }

    @FXML
    private void onMouseClickedOperation(MouseEvent event) {
        setNode(operaciones);
    }

    @FXML
    private void onMouseClickedInicio(MouseEvent event) {
        setNode(principal);
    }

}
