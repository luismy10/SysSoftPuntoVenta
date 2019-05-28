package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.EtiquetaADO;
import model.EtiquetaTB;

public class FxEtiquetasBusquedaController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private RadioButton rbVertical;
    @FXML
    private RadioButton rbHorizontal;
    @FXML
    private VBox vbEtiquetas;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtAncho;
    @FXML
    private TextField txtAlto;
    @FXML
    private ScrollPane spScrolLista;

    private FxEtiquetasController etiquetasController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        addElement();
    }

    public void onLoadEtiquetas() {

    }

    private void addElement() {
        ArrayList<EtiquetaTB> etbs = EtiquetaADO.ListEtiquetas();
        HBox hBox = new HBox();
        hBox.setStyle("-fx-spacing:0.8333333333333334em;-fx-padding:0.8333333333333334em;");
        //
        for (int i = 0; i < etbs.size(); i++) {
            int cod = etbs.get(i).getIdEtiqueta();
            String ruta = etbs.get(i).getRuta();
            VBox vBox = new VBox();
            vBox.setId("" + etbs.get(i).getIdEtiqueta());
            vBox.setAlignment(Pos.CENTER);
            vBox.setStyle("-fx-spacing:0.4166666666666667em;-fx-padding: 0.4166666666666667em;-fx-border-color: #cccccc;-fx-cursor:hand;");
            vBox.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                if (event.getClickCount() == 2) {
                    etiquetasController.loadEtiqueta(cod, ruta);
                    Tools.Dispose(window);
                }
            });

            ImageView imageView = new ImageView(new Image("/view/brochure-96.png"));
            imageView.setFitWidth(170);
            imageView.setFitHeight(150);

            Label nombre = new Label(etbs.get(i).getNombre());
            nombre.getStyleClass().add("labelOpenSansRegular14");

            Label tipo = new Label(etbs.get(i).getNombreTipo());
            tipo.getStyleClass().add("labelRobotoBold14");

            vBox.getChildren().addAll(imageView, nombre, tipo);

            hBox.getChildren().add(vBox);
        }
        //
        vbEtiquetas.getChildren().add(hBox);
    }

    @FXML
    private void onKeyPressedSelect(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionSelect(ActionEvent event) {

    }

    @FXML
    private void onKeyPressedCancelar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        Tools.Dispose(window);
    }

    public void setInitEtiquetasController(FxEtiquetasController etiquetasController) {
        this.etiquetasController = etiquetasController;
    }

}
