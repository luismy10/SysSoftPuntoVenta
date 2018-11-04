/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * FXML Controller class
 *
 * @author Ruberfc
 */
public class FxCodigoBarrasController implements Initializable {

    public static java.awt.Image getBarCode(String value) {
        Barcode128 code = new Barcode128();
        code.setCode(value);
        code.setCodeType(Barcode128.CODE128);
        code.setChecksumText(true);
        code.setTextAlignment(Element.ALIGN_CENTER);
        java.awt.Image image128;
        image128 = code.createAwtImage(Color.BLACK, Color.WHITE);

        return image128;
    }
    
    public static void getBarCodePDF(int opcion,String codigo) throws FileNotFoundException, DocumentException {
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
