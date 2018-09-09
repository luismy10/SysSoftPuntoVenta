package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.DBUtil;

public class FxInicioController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ScrollPane vbContent;

    private VBox principal;

    private HBox operaciones;

    private HBox configuracion;

    @FXML
    private ImageView imState;
    @FXML
    private Text lblEstado;

    private boolean stateconnect;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Load all fxmls in a cache
        try {
            stateconnect = DBUtil.StateConnection();
            lblEstado.setText(stateconnect == true ? "Conectado" : "Desconectado");

            imState.setImage(stateconnect == true ? new Image("/view/connected.png")
                    : new Image("/view/disconnected.png"));

            FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_PRINCIPAL));
            principal = fXMLPrincipal.load();
            FXMLLoader fXMLOperaciones = new FXMLLoader(getClass().getResource(Tools.FX_FILE_OPERACIONES));
            operaciones = fXMLOperaciones.load();
            FXMLLoader fXMLConfiguracion = new FXMLLoader(getClass().getResource(Tools.FX_FILE_CONFIGURACION));
            configuracion = fXMLConfiguracion.load();
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

    @FXML
    private void onMouseClickedConfiguracion(MouseEvent event) {
        setNode(configuracion);
    }

}
