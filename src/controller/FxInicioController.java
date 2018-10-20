package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FxInicioController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private AnchorPane vbContent;
    @FXML
    private ImageView imState;
    @FXML
    private Text lblEstado;
    @FXML
    private HBox btnInicio;
    @FXML
    private HBox btnOperacion;
    @FXML
    private HBox btnConsultas;
    @FXML
    private HBox btnReportes;
    @FXML
    private HBox btnGraficos;
    @FXML
    private HBox btnConfiguracion;
    @FXML
    private VBox vbSiderBar;
    @FXML
    private Text lblDatos;
    @FXML
    private Text lblPuesto;

    private ScrollPane principal;

    private HBox operaciones;

    private HBox consultas;

    private HBox configuracion;

    private boolean isExpand = true;

    private double width_siderbar;
    @FXML
    private Text lblDatos1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void initInicioController() {
        try {
            lblEstado.setText(Session.CONNECTION_SESSION == true ? "Conectado" : "Desconectado");
            imState.setImage(Session.CONNECTION_SESSION == true ? new Image("/view/connected.png")
                    : new Image("/view/disconnected.png"));

            FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_PRINCIPAL));
            principal = fXMLPrincipal.load();

            FXMLLoader fXMLOperaciones = new FXMLLoader(getClass().getResource(Tools.FX_FILE_OPERACIONES));
            operaciones = fXMLOperaciones.load();
            FxOperacionesController controllerOperaciones = fXMLOperaciones.getController();
            controllerOperaciones.setContent(window, vbContent);

            FXMLLoader fXMLConsultas = new FXMLLoader(getClass().getResource(Tools.FX_FILE_CONSULTAS));
            consultas = fXMLConsultas.load();
            FxConsultasController controllerConsultas = fXMLConsultas.getController();
            controllerConsultas.setContent(window, vbContent);

            FXMLLoader fXMLConfiguracion = new FXMLLoader(getClass().getResource(Tools.FX_FILE_CONFIGURACION));
            configuracion = fXMLConfiguracion.load();
            FxConfiguracionController configuracionController = fXMLConfiguracion.getController();
            configuracionController.setContent(window, vbContent);

            setNode(principal);
            btnInicio.getStyleClass().add("buttonContainerActivate");
            width_siderbar = vbSiderBar.getPrefWidth();

        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public void initWindowSize() {
        window.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            Session.WIDTH_WINDOW = (double) newValue;
            SysSoft.pane.setPrefWidth(Session.WIDTH_WINDOW);
        });

        window.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            Session.HEIGHT_WINDOW = (double) newValue;
            SysSoft.pane.setPrefHeight(Session.HEIGHT_WINDOW);
        });
    }

    public void initUserSession(String value) {
        lblPuesto.setText(value);
        lblDatos.setText(Session.USER_NAME);
    }

    //Set selected node to a content holder
    private void setNode(Node node) {
        vbContent.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        vbContent.getChildren().add(node);

    }

    @FXML
    private void onMouseClickedSiderBar(MouseEvent event) {
        if (isExpand) {
            vbSiderBar.setPrefWidth(0);
            isExpand = false;
        } else {
            vbSiderBar.setPrefWidth(width_siderbar);
            isExpand = true;
        }
    }

    @FXML
    private void onMouseClickedInicio(MouseEvent event) {
        setNode(principal);
        btnOperacion.getStyleClass().remove("buttonContainerActivate");
        btnConsultas.getStyleClass().remove("buttonContainerActivate");
        btnReportes.getStyleClass().remove("buttonContainerActivate");
        btnGraficos.getStyleClass().remove("buttonContainerActivate");
        btnConfiguracion.getStyleClass().remove("buttonContainerActivate");
        btnInicio.getStyleClass().remove("buttonContainerActivate");
        btnInicio.getStyleClass().add("buttonContainerActivate");

    }

    @FXML
    private void onMouseClickedOperaciones(MouseEvent event) {
        setNode(operaciones);
        btnInicio.getStyleClass().remove("buttonContainerActivate");
        btnConsultas.getStyleClass().remove("buttonContainerActivate");
        btnReportes.getStyleClass().remove("buttonContainerActivate");
        btnGraficos.getStyleClass().remove("buttonContainerActivate");
        btnConfiguracion.getStyleClass().remove("buttonContainerActivate");
        btnOperacion.getStyleClass().remove("buttonContainerActivate");
        btnOperacion.getStyleClass().add("buttonContainerActivate");
    }

    @FXML
    private void onMouseClickedConsultas(MouseEvent event) {
        setNode(consultas);
        btnInicio.getStyleClass().remove("buttonContainerActivate");
        btnOperacion.getStyleClass().remove("buttonContainerActivate");
        btnReportes.getStyleClass().remove("buttonContainerActivate");
        btnGraficos.getStyleClass().remove("buttonContainerActivate");
        btnConfiguracion.getStyleClass().remove("buttonContainerActivate");
        btnConsultas.getStyleClass().remove("buttonContainerActivate");
        btnConsultas.getStyleClass().add("buttonContainerActivate");
    }

    @FXML
    private void onMouseClickedReportes(MouseEvent event) {
        btnInicio.getStyleClass().remove("buttonContainerActivate");
        btnOperacion.getStyleClass().remove("buttonContainerActivate");
        btnConsultas.getStyleClass().remove("buttonContainerActivate");
        btnGraficos.getStyleClass().remove("buttonContainerActivate");
        btnConfiguracion.getStyleClass().remove("buttonContainerActivate");
        btnReportes.getStyleClass().remove("buttonContainerActivate");
        btnReportes.getStyleClass().add("buttonContainerActivate");
    }

    @FXML
    private void onMouseClickedGraficos(MouseEvent event) {
        btnInicio.getStyleClass().remove("buttonContainerActivate");
        btnOperacion.getStyleClass().remove("buttonContainerActivate");
        btnConsultas.getStyleClass().remove("buttonContainerActivate");
        btnReportes.getStyleClass().remove("buttonContainerActivate");
        btnConfiguracion.getStyleClass().remove("buttonContainerActivate");
        btnGraficos.getStyleClass().remove("buttonContainerActivate");
        btnGraficos.getStyleClass().add("buttonContainerActivate");
    }

    @FXML
    private void onMouseClickedConfiguracion(MouseEvent event) {
        setNode(configuracion);
        btnInicio.getStyleClass().remove("buttonContainerActivate");
        btnOperacion.getStyleClass().remove("buttonContainerActivate");
        btnConsultas.getStyleClass().remove("buttonContainerActivate");
        btnReportes.getStyleClass().remove("buttonContainerActivate");
        btnGraficos.getStyleClass().remove("buttonContainerActivate");
        btnConfiguracion.getStyleClass().remove("buttonContainerActivate");
        btnConfiguracion.getStyleClass().add("buttonContainerActivate");

    }

    @FXML
    private void onMouseClickedCerrar(MouseEvent event) throws IOException {
        Tools.Dispose(window);
        URL urllogin = getClass().getResource(Tools.FX_FILE_LOGIN);
        FXMLLoader fXMLLoaderLogin = FxWindow.LoaderWindow(urllogin);
        Parent parent = fXMLLoaderLogin.load(urllogin.openStream());
        Scene scene = new Scene(parent);
        Stage primaryStage = new Stage();
        primaryStage.getIcons().add(new Image(Tools.FX_LOGO));
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("Sys Soft");
        primaryStage.centerOnScreen();
        primaryStage.setMaximized(true);
        primaryStage.show();
        primaryStage.requestFocus();

    }

}
