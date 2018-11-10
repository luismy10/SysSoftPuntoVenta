package controller;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.output.OutputException;

public class FxCodigoBarrasController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ImageView ivCodigo;
    @FXML
    private ComboBox<String> cbCodificacion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        cbCodificacion.getItems().addAll("Code bar", "Code 128");

    }

    @FXML
    private void onActionGenerar(ActionEvent event) {

    }

    @FXML
    private void onActionImprimir(ActionEvent event) {

    }

    @FXML
    private void onActionCodificacion(ActionEvent event) {
        try {
            Barcode b;
            if (cbCodificacion.getSelectionModel().getSelectedIndex() == 0) {
                b = BarcodeFactory.createCodabar("1023567889232");
            } else {
                b = BarcodeFactory.createCode128("5689452310123");
            }
            b.setBarHeight(50);
            b.setDrawingText(true);
            BufferedImage bufferedImage = new BufferedImage((int) ivCodigo.getFitWidth(), 200, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = bufferedImage.createGraphics();            
            b.draw((Graphics2D) graphics,20, 60);
            WritableImage wr = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    pw.setArgb(x, y, bufferedImage.getRGB(x, y));
                }
            }
            ivCodigo.setImage(wr);
        } catch (BarcodeException | OutputException ex) {
            Logger.getLogger(FxCodigoBarrasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
