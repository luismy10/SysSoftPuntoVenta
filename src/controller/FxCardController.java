package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.DirectorioADO;

public class FxCardController implements Initializable {

    @FXML
    private HBox window;
    @FXML
    private Label lblSubTitle;
    @FXML
    private Text lblTitle;

    private long idDirectorio;

    private int idAtributo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void onMouseClickedEdit(MouseEvent event) throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_ASIGNACION);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxAsignacionController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Editar información", window.getScene().getWindow());
        stage.setResizable(false);
        stage.show();
        controller.setViewUpdate(idDirectorio, idAtributo, lblTitle.getText());

    }

    @FXML
    private void onMouseClickedRemove(MouseEvent event) {
        short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Perfil", "¿Esta seguro de eliminar el campo?", true);
        if (confirmation == 1) {
            String result = DirectorioADO.DeleteDirectory(idDirectorio);
            if (result.equalsIgnoreCase("eliminado")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Perfil", "Se elimino correctamente", false);
                
            } else if (result.equalsIgnoreCase("error")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Perfil", "Se produjo un error intente de nuevo", false);
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Perfil", result, false);

            }
        }
    }

    public Label getLblSubTitle() {
        return lblSubTitle;
    }

    public Text getLblTitle() {
        return lblTitle;
    }

    public void setIdAtributo(int idAtributo) {
        this.idAtributo = idAtributo;
    }

    public void setIdDirectorio(long idDirectorio) {
        this.idDirectorio = idDirectorio;
    }

}
