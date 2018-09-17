package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.DBUtil;
import model.DirectorioADO;
import model.DirectorioTB;

public class FxPerfilController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private VBox vbList;
    @FXML
    private Text lblInformation;
    
    private String idPersona;
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
    }

    private void onViewAsignacion() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_ASIGNACION);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxAsignacionController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Asignar información", window.getScene().getWindow());
        stage.setResizable(false);
        stage.show();
        controller.setViewAdd(idPersona);
    }

    @FXML
    private void onKeyPressedToRegister(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onViewAsignacion();
        }
    }

    @FXML
    private void onActionToRegister(ActionEvent event) throws IOException {
        onViewAsignacion();
    }

    void setLoadView(String idPersona, String information) {
        this.idPersona = idPersona;
        lblInformation.setText(information);
        loadViewUpdate(idPersona);
    }

    public void loadViewUpdate(String idPersona) {
        if (DBUtil.StateConnection()) {
            try {
                ArrayList<DirectorioTB> arrayList = DirectorioADO.GetIdDirectorio(idPersona);                
                for (int i = 0; i < arrayList.size(); i++) {
                    FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/view/persona/FxCard.fxml"));
                    HBox node = fXMLLoader.load();
                    FxCardController controller = fXMLLoader.getController();
                    controller.setIdDirectorio(arrayList.get(i).getIdDirectorio());
                    controller.setIdAtributo(arrayList.get(i).getAtributo());
                    controller.getLblSubTitle().setText(arrayList.get(i).getNombre());
                    controller.getLblTitle().setText(arrayList.get(i).getValor());
                    vbList.getChildren().addAll(node);
                }

            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

    @FXML
    private void onKeyPressedToReload(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            vbList.getChildren().clear();
            loadViewUpdate(idPersona);
        }
    }

    @FXML
    private void onActionToReload(ActionEvent event) {
        vbList.getChildren().clear();
        loadViewUpdate(idPersona);
    }

}
