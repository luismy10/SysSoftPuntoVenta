package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
        controller.setViewUpdate(idDirectorio,idAtributo,lblTitle.getText());
        
    }

}
