package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FxPerfilController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private VBox vbList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/view/persona/FxCard.fxml"));
            HBox node = fXMLLoader.load();
            FxCardController controller = fXMLLoader.getController();
            controller.getLblSubTitle().setText("CELULAR");
            controller.getLblTitle().setText("+51 966750883");

            FXMLLoader fXMLLoader1 = new FXMLLoader(getClass().getResource("/view/persona/FxCard.fxml"));
            HBox node1 = fXMLLoader1.load();
            FxCardController controller1 = fXMLLoader1.getController();
            controller1.getLblSubTitle().setText("TELÉFONO");
            controller1.getLblTitle().setText("+(064) 7891234");

            Node node2 = FXMLLoader.load(getClass().getResource("/view/persona/FxCard.fxml"));

            vbList.getChildren().addAll(node, node1, node2);

        } catch (IOException ex) {
            Logger.getLogger(FxPerfilController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onMouseClickedClose(MouseEvent event) {
        Tools.Dispose(window);
    }

}
