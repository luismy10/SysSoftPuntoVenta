package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode39;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class FxCodigoBarrasController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ImageView ivCodigo;
    @FXML
    private ComboBox<?> cbCodificacion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public static javafx.scene.image.Image getBarCode(String value) {
        Barcode128 code = new Barcode128();
        code.setCode(value);
        code.setCodeType(Barcode128.CODE128);
        code.setChecksumText(true);
        code.setTextAlignment(Element.ALIGN_CENTER);
        BufferedImage buffered = (BufferedImage) code.createAwtImage(Color.BLACK, Color.WHITE);
        javafx.scene.image.Image image = SwingFXUtils.toFXImage(buffered, null);
        return image;
    }

    public static void getBarCodePDF(int opcion, String codigo) throws FileNotFoundException, DocumentException {
        Document doc = new Document();
        PdfWriter pdf = PdfWriter.getInstance(doc, new FileOutputStream("prueba.pdf"));
        doc.open();
        if (opcion == 1) {
            Barcode128 code128 = new Barcode128();
            code128.setCode(codigo);
            Image img128 = code128.createImageWithBarcode(pdf.getDirectContent(), BaseColor.BLACK, BaseColor.BLACK);
            doc.add(img128);
            doc.add(new Paragraph(" "));
        } else if (opcion == 2) {
            Barcode39 code39 = new Barcode39();
            code39.setCode(codigo);
            Image img39 = code39.createImageWithBarcode(pdf.getDirectContent(), BaseColor.BLACK, BaseColor.BLACK);
            doc.add(img39);
        }
        doc.close();
    }

    @FXML
    private void onActionGenerar(ActionEvent event) {
        ivCodigo.setImage(getBarCode("234324235435435"));
    }

    @FXML
    private void onActionImprimir(ActionEvent event) {

    }

}
