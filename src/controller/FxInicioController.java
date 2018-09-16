package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    @FXML
    private ImageView imState;
    @FXML
    private Text lblEstado;
    @FXML
    private Button btnInicio;
    @FXML
    private Button btnOperacion;
    @FXML
    private Button btnConsultas;
    @FXML
    private Button btnReportes;
    @FXML
    private Button btnGraficos;
    @FXML
    private Button btnConfiguracion;

    private VBox principal;

    private HBox operaciones;

    private HBox configuracion;

    private boolean stateconnect;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    private void onActionInicio(ActionEvent event) {
        setNode(principal);
        btnOperacion.getStyleClass().remove("buttonPanelActive");
        btnConsultas.getStyleClass().remove("buttonPanelActive");
        btnReportes.getStyleClass().remove("buttonPanelActive");
        btnGraficos.getStyleClass().remove("buttonPanelActive");
        btnConfiguracion.getStyleClass().remove("buttonPanelActive");
        btnInicio.getStyleClass().add("buttonPanelActive");
    }

    @FXML
    private void onActionOperation(ActionEvent event) {
        setNode(operaciones);
        btnInicio.getStyleClass().remove("buttonPanelActive");
        btnConsultas.getStyleClass().remove("buttonPanelActive");
        btnReportes.getStyleClass().remove("buttonPanelActive");
        btnGraficos.getStyleClass().remove("buttonPanelActive");
        btnConfiguracion.getStyleClass().remove("buttonPanelActive");
        btnOperacion.getStyleClass().add("buttonPanelActive");
    }

    @FXML
    private void onActionConsultas(ActionEvent event) {
        btnInicio.getStyleClass().remove("buttonPanelActive");
        btnOperacion.getStyleClass().remove("buttonPanelActive");
        btnReportes.getStyleClass().remove("buttonPanelActive");
        btnGraficos.getStyleClass().remove("buttonPanelActive");
        btnConfiguracion.getStyleClass().remove("buttonPanelActive");
        btnConsultas.getStyleClass().add("buttonPanelActive");
    }

    @FXML
    private void onActionReportes(ActionEvent event) {
        btnInicio.getStyleClass().remove("buttonPanelActive");
        btnOperacion.getStyleClass().remove("buttonPanelActive");
        btnConsultas.getStyleClass().remove("buttonPanelActive");
        btnGraficos.getStyleClass().remove("buttonPanelActive");
        btnConfiguracion.getStyleClass().remove("buttonPanelActive");
        btnReportes.getStyleClass().add("buttonPanelActive");
    }

    @FXML
    private void onActionGraficos(ActionEvent event) {
        btnInicio.getStyleClass().remove("buttonPanelActive");
        btnOperacion.getStyleClass().remove("buttonPanelActive");
        btnConsultas.getStyleClass().remove("buttonPanelActive");
        btnReportes.getStyleClass().remove("buttonPanelActive");
        btnConfiguracion.getStyleClass().remove("buttonPanelActive");
        btnGraficos.getStyleClass().add("buttonPanelActive");
    }

    @FXML
    private void onActionConfiguracion(ActionEvent event) {
        setNode(configuracion);
        btnInicio.getStyleClass().remove("buttonPanelActive");
        btnOperacion.getStyleClass().remove("buttonPanelActive");
        btnConsultas.getStyleClass().remove("buttonPanelActive");
        btnReportes.getStyleClass().remove("buttonPanelActive");
        btnGraficos.getStyleClass().remove("buttonPanelActive");
        btnConfiguracion.getStyleClass().add("buttonPanelActive");
    }

}
