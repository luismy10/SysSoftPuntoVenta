package controller;

import com.itextpdf.text.pdf.Barcode128;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class FxEtiquetasController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private Pane panel;
    @FXML
    private TextField txtTexto;
    @FXML
    private ComboBox<String> cbFuente;
    @FXML
    private Spinner<Double> spFontSize;
    @FXML
    private CheckBox cbBold;
    @FXML
    private CheckBox cbItalic;
    @FXML
    private ScrollPane scrollPane;

    private Text textReferent;

    private CodBar imageViewReferent;

    private SelectionModel selectionModel;

    private double x;

    private double y;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        cbFuente.getItems().addAll(Arrays.asList(fontNames));

        spFontSize.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 100, 12));
        spFontSize.setOnMousePressed((event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (textReferent != null) {
                    textReferent.setFont(Font.font(textReferent.getFont().getFamily(), textReferent.getFontWight(), textReferent.getFontPosture(), spFontSize.getValue()));
                }
            }
        });
        spFontSize.getEditor().setOnAction((event) -> {
            if (textReferent != null) {
                textReferent.setFont(Font.font(textReferent.getFont().getFamily(), textReferent.getFontWight(), textReferent.getFontPosture(), Double.parseDouble(spFontSize.getEditor().getText())));
            }
        });
        spFontSize.getEditor().setOnKeyTyped((event) -> {
            char c = event.getCharacter().charAt(0);
            if ((c < '0' || c > '9') && (c != '\b') && (c != '.')) {
                event.consume();
            }
            if (c == '.' && spFontSize.getEditor().getText().contains(".")) {
                event.consume();
            }
        });

        Group selectionLayer = new Group();
        panel.getChildren().add(selectionLayer);

        selectionModel = new SelectionModel(selectionLayer);

        window.setOnMousePressed(mouseEvent -> selectionModel.clear());
        window.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    }

    private WritableImage generateBarCode(String value) {
        int widthBuffer = (int) (141);
        int heightBuffer = (int) (50);
        Barcode128 barcode128 = new Barcode128();
        barcode128.setCode(value);
        java.awt.Image image = barcode128.createAwtImage(java.awt.Color.BLACK, java.awt.Color.WHITE);
        BufferedImage bufferedImage = new BufferedImage(widthBuffer, heightBuffer, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(java.awt.Color.WHITE);
        graphics.fillRect(0, 0, widthBuffer, heightBuffer);
        graphics.drawImage(image, 0, 0, widthBuffer, 30, null);
        java.awt.Font font = new java.awt.Font("Lucida Sans Typewriter", java.awt.Font.BOLD, 16);
        graphics.setFont(font);
        graphics.setColor(java.awt.Color.BLACK);
        graphics.drawString(value, (widthBuffer - graphics.getFontMetrics(font).stringWidth(value)) / 2, 48);
        graphics.dispose();

        WritableImage wr = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
        PixelWriter pw = wr.getPixelWriter();
        for (int px = 0; px < bufferedImage.getWidth(); px++) {
            for (int py = 0; py < bufferedImage.getHeight(); py++) {
                pw.setArgb(px, py, bufferedImage.getRGB(px, py));
            }
        }

        return wr;
    }

    private ImageView addBarCode() {
        CodBar ivCodigo = new CodBar("12345678", 0, 0);
        ivCodigo.setImage(generateBarCode(ivCodigo.getTexto()));
        ivCodigo.setOnMousePressed(mouseEvent -> {
            imageViewReferent = ivCodigo;
            txtTexto.setText(imageViewReferent.getTexto());
            selectionModel.clear();
            selectionModel.add(ivCodigo);

            x = ivCodigo.getTranslateX() - mouseEvent.getSceneX();
            y = ivCodigo.getTranslateY() - mouseEvent.getSceneY();

            // consume event, so that scene won't get it (which clears selection)
            mouseEvent.consume();
        });
        ivCodigo.setOnMouseDragged(mouseEvent -> {
            ivCodigo.setTranslateX(mouseEvent.getSceneX() + x);
            ivCodigo.setTranslateY(mouseEvent.getSceneY() + y);
        });
        ivCodigo.setOnMouseReleased(mouseEvent -> {
            double px = ivCodigo.getTranslateX();
            double py = ivCodigo.getTranslateY();
            ivCodigo.relocate(ivCodigo.getLayoutX() + px, ivCodigo.getLayoutY() + py);
            ivCodigo.setTranslateX(0);
            ivCodigo.setTranslateY(0);
        });
        return ivCodigo;
    }

    private Label addText(String value) {
        Text text = new Text(value, 0, 0);
        text.setOnMousePressed(mouseEvent -> {
            textReferent = text;
            cbFuente.getSelectionModel().select(textReferent.getFont().getFamily());
            spFontSize.getValueFactory().setValue(textReferent.getFont().getSize());
            txtTexto.setText(textReferent.getText());
            cbBold.setSelected(textReferent.isBold());
            cbItalic.setSelected(textReferent.isItalic());

            selectionModel.clear();
            selectionModel.add(text);

            x = text.getTranslateX() - mouseEvent.getSceneX();
            y = text.getTranslateY() - mouseEvent.getSceneY();

            mouseEvent.consume();
        });
        text.setOnMouseDragged(mouseEvent -> {
            text.setTranslateX(mouseEvent.getSceneX() + x);
            text.setTranslateY(mouseEvent.getSceneY() + y);
        });
        text.setOnMouseReleased(mouseEvent -> {
            double px = text.getTranslateX();
            double py = text.getTranslateY();
            text.relocate(text.getLayoutX() + px, text.getLayoutY() + py);
            text.setTranslateX(0);
            text.setTranslateY(0);
        });
        return text;
    }

    @FXML
    private void onKeyPressedSearch(KeyEvent event) {
    }

    @FXML
    private void onActionSearch(ActionEvent event) {
    }

    @FXML
    private void onKeyPressNuevo(KeyEvent event) {
    }

    @FXML
    private void onActionNuevo(ActionEvent event) {
    }

    @FXML
    private void onKeyPressEditar(KeyEvent event) {
    }

    @FXML
    private void onActionEditar(ActionEvent event) {
    }

    @FXML
    private void onKeyPressedSave(KeyEvent event) {
    }

    @FXML
    private void onActionSave(ActionEvent event) {
    }

    @FXML
    private void onKeyPressedPrint(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            eventImprimir();
        }
    }

    @FXML
    private void onActionPrint(ActionEvent event) {
        eventImprimir();
    }

    @FXML
    private void onKeyPressedEliminar(KeyEvent event) {
    }

    @FXML
    private void onActionEliminar(ActionEvent event) {
    }

    @FXML
    private void onKeyPressedTexto(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            panel.getChildren().add(addText("Texto de muestra"));
        }
    }

    @FXML
    private void onActionTexto(ActionEvent event) {
        panel.getChildren().add(addText("Texto de muestra"));
    }

    @FXML
    private void onKeyPressedCodBar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            panel.getChildren().add(addBarCode());
        }
    }

    @FXML
    private void onActionCodBar(ActionEvent event) {
        panel.getChildren().add(addBarCode());
    }

    @FXML
    private void onKeyPressedQuitar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            panel.getChildren().remove(selectionModel.getNodeSelection());
            selectionModel.clear();
        }
    }

    @FXML
    private void onActionQuitar(ActionEvent event) {
        panel.getChildren().remove(selectionModel.getNodeSelection());
        selectionModel.clear();
    }

    @FXML
    private void onActionEditTexto(ActionEvent event) {
        if (textReferent != null && selectionModel.getNodeSelection() == textReferent) {
            if (!txtTexto.getText().trim().isEmpty()) {
                textReferent.setText(txtTexto.getText().trim());
            } else {
                textReferent.setText("Texto de referencia.");
            }

        } else if (imageViewReferent != null && selectionModel.getNodeSelection() == imageViewReferent) {
            if (!txtTexto.getText().trim().isEmpty()) {
                imageViewReferent.setTexto(txtTexto.getText().trim());
                imageViewReferent.setImage(generateBarCode(imageViewReferent.getTexto()));
            } else {
                imageViewReferent.setTexto(txtTexto.getText().trim());
                imageViewReferent.setImage(generateBarCode(imageViewReferent.getTexto()));
            }
        }

    }

    @FXML
    private void onActionFuente(ActionEvent event) {
        if (cbFuente.getSelectionModel().getSelectedIndex() >= 0) {
            if (textReferent != null && selectionModel.getNodeSelection() == imageViewReferent) {
                textReferent.setFont(Font.font(cbFuente.getSelectionModel().getSelectedItem(), textReferent.getFontWight(), textReferent.getFontPosture(), textReferent.getFont().getSize()));
            }
        }
    }

    @FXML
    private void onActionBold(ActionEvent event) {
        if (textReferent != null && selectionModel.getNodeSelection() == imageViewReferent) {
            if (textReferent.isBold()) {
                textReferent.setFont(
                        Font.font(textReferent.getFont().getFamily(),
                                FontWeight.NORMAL,
                                textReferent.getFontPosture(),
                                spFontSize.getValue()));
                textReferent.setBold(false);
                textReferent.setFontWight(FontWeight.NORMAL);
            } else {
                textReferent.setFont(
                        Font.font(textReferent.getFont().getFamily(),
                                FontWeight.BOLD,
                                textReferent.getFontPosture(),
                                spFontSize.getValue()));
                textReferent.setBold(true);
                textReferent.setFontWight(FontWeight.BOLD);
            }
        }
    }

    @FXML
    private void onActionItalic(ActionEvent event) {
        if (textReferent != null && selectionModel.getNodeSelection() == imageViewReferent) {
            if (textReferent.isItalic()) {
                textReferent.setFont(
                        Font.font(textReferent.getFont().getFamily(),
                                textReferent.getFontWight(),
                                FontPosture.REGULAR,
                                spFontSize.getValue()));
                textReferent.setItalic(false);
                textReferent.setFontPosture(FontPosture.REGULAR);
            } else {
                textReferent.setFont(
                        Font.font(textReferent.getFont().getFamily(),
                                textReferent.getFontWight(),
                                FontPosture.ITALIC,
                                spFontSize.getValue()));
                textReferent.setItalic(true);
                textReferent.setFontPosture(FontPosture.ITALIC);
            }
        }
    }

    private void eventImprimir() {
        WritableImage image = createScaledView(panel, 4);
        BillPrintableEtiquetas billPrintable = new BillPrintableEtiquetas(SwingFXUtils.fromFXImage(image, null));

        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setCopies(1);
        Book book = new Book();
        book.append(billPrintable, getPageFormat(pj));
        pj.setPageable(book);
        try {
            if (pj.printDialog()) {
                pj.print();
            }
        } catch (PrinterException ex) {
        }

//        File file = new File("C:/Users/Aleza/Desktop/temp2.png");
//        WritableImage image = createScaledView(panel, 2);
//        RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
//        ImageIO.write(renderedImage, "png", file);
    }

    public PageFormat getPageFormat(PrinterJob pj) {
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();
        paper.setSize(getPointToMM(50), getPointToMM(25));
        paper.setImageableArea(0, 0, pf.getWidth(), pf.getHeight());   //define boarder size    after that print area width is about 180 points
        pf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
        pf.setPaper(paper);
        return pf;
    }

    public double getPointToMM(double mm) {
        return (mm * 2.83465) / 1;
    }

    private static WritableImage createScaledView(Node node, int scale) {
        final Bounds bounds = node.getLayoutBounds();

        final WritableImage image = new WritableImage(
                (int) Math.round(bounds.getWidth() * scale),
                (int) Math.round(bounds.getHeight() * scale));

        final SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(javafx.scene.transform.Transform.scale(scale, scale));

        return node.snapshot(spa, image);
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

    class BillPrintableEtiquetas implements Printable {

        private final BufferedImage bufferedImage;

        public BillPrintableEtiquetas(BufferedImage bufferedImage) {
            this.bufferedImage = bufferedImage;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex == 0) {
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
                g2d.drawImage(bufferedImage, 5, 0, 141, 70, null);
                g2d.dispose();
                return (PAGE_EXISTS);
            } else {
                return (NO_SUCH_PAGE);
            }

        }

    }

}
