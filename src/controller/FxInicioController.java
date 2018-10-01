package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
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
import javafx.util.Duration;
import model.DBUtil;
import model.DetalleADO;
import model.EmpresaADO;
import model.EmpresaTB;

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

    private ScrollPane principal;

    private HBox operaciones;

    private HBox consultas;

    private HBox configuracion;

    private boolean stateconnect;

    private boolean isExpand = true;

    private double width_siderbar;

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

            TimerService service = new TimerService();
            service.setPeriod(Duration.seconds(59));
            service.setOnSucceeded((WorkerStateEvent t) -> {
                try {
                    DBUtil.dbConnect();
                    DBUtil.dbDisconnect();
                } catch (SQLException ex) {
                    Logger.getLogger(FxInicioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            service.start();

            ArrayList<EmpresaTB> list = EmpresaADO.GetEmpresa();
            if (!list.isEmpty()) {
                Session.EMPRESA = list.get(0).getRazonSocial().equalsIgnoreCase(list.get(0).getNombre()) ? list.get(0).getNombre() : list.get(0).getRazonSocial();
                Session.TELEFONO = list.get(0).getTelefono();
                Session.CELULAR = list.get(0).getCelular();
                Session.PAGINAWEB = list.get(0).getPaginaWeb();
                Session.EMAIL = list.get(0).getEmail();
                Session.DIRECCION = list.get(0).getDomicilio();
            }

            DetalleADO.GetDetailIdName("3", "0010", "").forEach(e -> {
                Session.IMPUESTO = Double.parseDouble(e.getDescripcion().get());
            });

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

}
