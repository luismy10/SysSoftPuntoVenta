package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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
import model.MenuADO;
import model.MenuTB;

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
    private VBox vbSiderBar;
    @FXML
    private Text lblDatos;
    @FXML
    private Text lblPuesto;
    @FXML
    private VBox vbMenus;

    private ScrollPane principal;

    private HBox operaciones;

    private HBox consultas;

    private HBox configuracion;

    private boolean isExpand = true;

    private double width_siderbar;

    private HBox btnInicio;

    private HBox btnOperaciones;

    private HBox btnConsultas;

    private HBox btnReportes;

    private HBox btnGraficos;

    private HBox btnConfiguracion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void initInicioController(int rol) {
        try {
            lblEstado.setText(Session.CONNECTION_SESSION == true ? "Conectado" : "Desconectado");
            imState.setImage(Session.CONNECTION_SESSION == true ? new Image("/view/connected.png")
                    : new Image("/view/disconnected.png"));

            vbMenus.getChildren().clear();
            ObservableList<MenuTB> menuTBs = MenuADO.GetMenus(rol);

            if (menuTBs.get(0).getIdMenu() != 0) {
                FXMLLoader fXMLSeleccionado = new FXMLLoader(getClass().getResource("/view/inicio/FxMenu.fxml"));
                btnInicio = fXMLSeleccionado.load();
                FxMenuController menuController = fXMLSeleccionado.getController();
                menuController.getIvImagen().setImage(new Image("/view/home.png")); 
                menuController.getLblNombre().setText(
                        menuTBs.get(0).getNombre().substring(0, 1).toUpperCase() + menuTBs.get(0).getNombre().substring(1).toLowerCase()
                );
                btnInicio.setVisible(menuTBs.get(0).isEstado());
                btnInicio.setOnMouseClicked(this::onMouseClickedInicio);
                btnInicio.getStyleClass().add("buttonContainerActivate");
                vbMenus.getChildren().add(btnInicio);

            }
            if (menuTBs.get(1).getIdMenu() != 0) {
                FXMLLoader fXMLSeleccionado = new FXMLLoader(getClass().getResource("/view/inicio/FxMenu.fxml"));
                btnOperaciones = fXMLSeleccionado.load();
                FxMenuController menuController = fXMLSeleccionado.getController();
                menuController.getIvImagen().setImage(new Image("/view/bag.png"));
                menuController.getLblNombre().setText(
                        menuTBs.get(1).getNombre().substring(0, 1).toUpperCase() + menuTBs.get(1).getNombre().substring(1).toLowerCase()
                );
                btnOperaciones.setVisible(menuTBs.get(1).isEstado());
                btnOperaciones.setOnMouseClicked(this::onMouseClickedOperaciones);
                vbMenus.getChildren().add(btnOperaciones);
            }
            if (menuTBs.get(2).getIdMenu() != 0) {
                FXMLLoader fXMLSeleccionado = new FXMLLoader(getClass().getResource("/view/inicio/FxMenu.fxml"));
                btnConsultas = fXMLSeleccionado.load();
                FxMenuController menuController = fXMLSeleccionado.getController();
                menuController.getIvImagen().setImage(new Image("/view/consultas.png"));
                menuController.getLblNombre().setText(
                        menuTBs.get(2).getNombre().substring(0, 1).toUpperCase() + menuTBs.get(2).getNombre().substring(1).toLowerCase()
                );
                btnConsultas.setVisible(menuTBs.get(2).isEstado());
                btnConsultas.setOnMouseClicked(this::onMouseClickedConsultas);
                vbMenus.getChildren().add(btnConsultas);
            }
            if (menuTBs.get(3).getIdMenu() != 0) {
                FXMLLoader fXMLSeleccionado = new FXMLLoader(getClass().getResource("/view/inicio/FxMenu.fxml"));
                btnReportes = fXMLSeleccionado.load();
                FxMenuController menuController = fXMLSeleccionado.getController();
                menuController.getIvImagen().setImage(new Image("/view/reports.png"));
                menuController.getLblNombre().setText(
                        menuTBs.get(3).getNombre().substring(0, 1).toUpperCase() + menuTBs.get(3).getNombre().substring(1).toLowerCase()
                );
                btnReportes.setVisible(menuTBs.get(3).isEstado());
                btnReportes.setOnMouseClicked(this::onMouseClickedReportes);
                vbMenus.getChildren().add(btnReportes);
            }
            if (menuTBs.get(4).getIdMenu() != 0) {
                FXMLLoader fXMLSeleccionado = new FXMLLoader(getClass().getResource("/view/inicio/FxMenu.fxml"));
                btnGraficos = fXMLSeleccionado.load();
                FxMenuController menuController = fXMLSeleccionado.getController();
                menuController.getIvImagen().setImage(new Image("/view/charts.png"));
                menuController.getLblNombre().setText(
                        menuTBs.get(4).getNombre().substring(0, 1).toUpperCase() + menuTBs.get(4).getNombre().substring(1).toLowerCase()
                );
                btnGraficos.setVisible(menuTBs.get(4).isEstado());
                btnGraficos.setOnMouseClicked(this::onMouseClickedGraficos);
                vbMenus.getChildren().add(btnGraficos);
            }
            if (menuTBs.get(5).getIdMenu() != 0) {
                FXMLLoader fXMLSeleccionado = new FXMLLoader(getClass().getResource("/view/inicio/FxMenu.fxml"));
                btnConfiguracion = fXMLSeleccionado.load();
                FxMenuController menuController = fXMLSeleccionado.getController();
                menuController.getIvImagen().setImage(new Image("/view/configuration.png"));
                menuController.getLblNombre().setText(
                        menuTBs.get(5).getNombre().substring(0, 1).toUpperCase() + menuTBs.get(5).getNombre().substring(1).toLowerCase()
                );
                btnConfiguracion.setVisible(menuTBs.get(5).isEstado());
                btnConfiguracion.setOnMouseClicked(this::onMouseClickedConfiguracion);
                vbMenus.getChildren().add(btnConfiguracion);
            }

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

    private void onMouseClickedInicio(MouseEvent event) {
        setNode(principal);
        btnOperaciones.getStyleClass().remove("buttonContainerActivate");
        btnConsultas.getStyleClass().remove("buttonContainerActivate");
        btnReportes.getStyleClass().remove("buttonContainerActivate");
        btnGraficos.getStyleClass().remove("buttonContainerActivate");
        btnConfiguracion.getStyleClass().remove("buttonContainerActivate");
        btnInicio.getStyleClass().remove("buttonContainerActivate");
        btnInicio.getStyleClass().add("buttonContainerActivate");

    }

    private void onMouseClickedOperaciones(MouseEvent event) {
        setNode(operaciones);
        btnInicio.getStyleClass().remove("buttonContainerActivate");
        btnConsultas.getStyleClass().remove("buttonContainerActivate");
        btnReportes.getStyleClass().remove("buttonContainerActivate");
        btnGraficos.getStyleClass().remove("buttonContainerActivate");
        btnConfiguracion.getStyleClass().remove("buttonContainerActivate");
        btnOperaciones.getStyleClass().remove("buttonContainerActivate");
        btnOperaciones.getStyleClass().add("buttonContainerActivate");
    }

    private void onMouseClickedConsultas(MouseEvent event) {
        setNode(consultas);
        btnInicio.getStyleClass().remove("buttonContainerActivate");
        btnOperaciones.getStyleClass().remove("buttonContainerActivate");
        btnReportes.getStyleClass().remove("buttonContainerActivate");
        btnGraficos.getStyleClass().remove("buttonContainerActivate");
        btnConfiguracion.getStyleClass().remove("buttonContainerActivate");
        btnConsultas.getStyleClass().remove("buttonContainerActivate");
        btnConsultas.getStyleClass().add("buttonContainerActivate");
    }

    private void onMouseClickedReportes(MouseEvent event) {
        btnInicio.getStyleClass().remove("buttonContainerActivate");
        btnOperaciones.getStyleClass().remove("buttonContainerActivate");
        btnConsultas.getStyleClass().remove("buttonContainerActivate");
        btnGraficos.getStyleClass().remove("buttonContainerActivate");
        btnConfiguracion.getStyleClass().remove("buttonContainerActivate");
        btnReportes.getStyleClass().remove("buttonContainerActivate");
        btnReportes.getStyleClass().add("buttonContainerActivate");
    }

    private void onMouseClickedGraficos(MouseEvent event) {
        btnInicio.getStyleClass().remove("buttonContainerActivate");
        btnOperaciones.getStyleClass().remove("buttonContainerActivate");
        btnConsultas.getStyleClass().remove("buttonContainerActivate");
        btnReportes.getStyleClass().remove("buttonContainerActivate");
        btnConfiguracion.getStyleClass().remove("buttonContainerActivate");
        btnGraficos.getStyleClass().remove("buttonContainerActivate");
        btnGraficos.getStyleClass().add("buttonContainerActivate");
    }

    private void onMouseClickedConfiguracion(MouseEvent event) {
        setNode(configuracion);
        btnInicio.getStyleClass().remove("buttonContainerActivate");
        btnOperaciones.getStyleClass().remove("buttonContainerActivate");
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
