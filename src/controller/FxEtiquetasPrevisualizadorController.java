package controller;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import model.ArticuloTB;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.output.OutputException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FxEtiquetasPrevisualizadorController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private VBox vbEtiquetas;
    @FXML
    private ScrollPane scrollPane;
    
    private Pane panel;

    private double widthEtiquetaMM;

    private int orientacionEtiqueta;

    private double heightEtiquetaMM;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
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
    private void onKeyPressedCancelar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        Tools.Dispose(window);
    }

    public void loadEtiqueta(String ruta, ArticuloTB articuloTB) {
        if (ruta != null) {
            panel = new Pane();
            panel.setStyle("-fx-background-color:white;"); 
            panel.getChildren().clear();
            Group selectionLayer = new Group();
            panel.getChildren().add(selectionLayer);

            JSONObject jSONObject = obtenerObjetoJSON(ruta);
            if (jSONObject.get("cuerpo") != null) {
                JSONObject object = obtenerObjetoJSON(jSONObject.get("cuerpo").toString());
                widthEtiquetaMM = Double.parseDouble(object.get("width").toString());
                heightEtiquetaMM = Double.parseDouble(object.get("height").toString());
                orientacionEtiqueta = Integer.parseInt(object.get("orientation").toString());
                int type = Integer.parseInt(object.get("type").toString());
                if (orientacionEtiqueta == PageFormat.PORTRAIT) {
                    panel.setPrefSize(converMmToPixel(widthEtiquetaMM), converMmToPixel(heightEtiquetaMM));
                } else {
                    panel.setPrefSize(converMmToPixel(heightEtiquetaMM), converMmToPixel(widthEtiquetaMM));
                }
                if (object.get("childs") != null) {
                    JSONArray array = obtenerArrayJSON(object.get("childs").toString());
                    Iterator it = array.iterator();
                    while (it.hasNext()) {
                        JSONObject objectchild = obtenerObjetoJSON(it.next().toString());
                        if (objectchild.get("type") != null) {
                            if (String.valueOf(objectchild.get("type")).equalsIgnoreCase("text")) {
                                String variable = String.valueOf(objectchild.get("variable").toString());
                                String text = "";
                                switch (type) {
                                    case 1:
                                        if (variable.equalsIgnoreCase("clave_articulo")) {
                                            text = articuloTB.getClave();
                                        } else if (variable.equalsIgnoreCase("clave_alterna")) {
                                            text = articuloTB.getClaveAlterna();
                                        } else if (variable.equalsIgnoreCase("descripcion")) {
                                            text = articuloTB.getNombreMarca();
                                        } else if (variable.equalsIgnoreCase("precio")) {
                                            text = Tools.roundingValue(articuloTB.getPrecioVentaGeneral(), 2);
                                        } else if (variable.equalsIgnoreCase("girocomercial_empresa")) {
                                            text = "Giro Comercial";
                                        } else if (variable.equalsIgnoreCase("representante_empresa")) {
                                            text = Session.REPRESENTANTE_EMPRESA;
                                        } else if (variable.equalsIgnoreCase("telefono_empresa")) {
                                            text = Session.TELEFONO_EMPRESA;
                                        } else if (variable.equalsIgnoreCase("celular_empresa")) {
                                            text = Session.CELULAR_EMPRESA;
                                        } else {
                                            text = String.valueOf(objectchild.get("text"));
                                        }
                                        break;
                                    case 2:
                                        if (variable.equalsIgnoreCase("clave_articulo")) {
                                            text = articuloTB.getClave();
                                        } else if (variable.equalsIgnoreCase("clave_alterna")) {
                                            text = articuloTB.getClaveAlterna();
                                        } else if (variable.equalsIgnoreCase("descripcion")) {
                                            text = articuloTB.getNombreMarca();
                                        } else if (variable.equalsIgnoreCase("precio")) {
                                            text = Tools.roundingValue(articuloTB.getPrecioVentaGeneral(), 2);
                                        } else if (variable.equalsIgnoreCase("fecha_registro")) {

                                        } else if (variable.equalsIgnoreCase("fecha_vencimiento")) {

                                        } else if (variable.equalsIgnoreCase("girocomercial_empresa")) {
                                            text = "Giro Comercial";
                                        } else if (variable.equalsIgnoreCase("representante_empresa")) {
                                            text = Session.REPRESENTANTE_EMPRESA;
                                        } else if (variable.equalsIgnoreCase("telefono_empresa")) {
                                            text = Session.TELEFONO_EMPRESA;
                                        } else if (variable.equalsIgnoreCase("celular_empresa")) {
                                            text = Session.CELULAR_EMPRESA;
                                        } else {
                                            text = String.valueOf(objectchild.get("text"));
                                        }
                                        break;
                                    case 3:

                                        break;
                                    default:
                                        break;
                                }
                                panel.getChildren().add(addText(
                                        text,
                                        Double.parseDouble(objectchild.get("x").toString()),
                                        Double.parseDouble(objectchild.get("y").toString()),
                                        Double.parseDouble(objectchild.get("with").toString()),
                                        Double.parseDouble(objectchild.get("height").toString()),
                                        String.valueOf(objectchild.get("fontname")),
                                        Double.parseDouble(objectchild.get("fontsize").toString())
                                ));
                            } else if (String.valueOf(objectchild.get("type")).equalsIgnoreCase("codebar")) {
                                String variable = String.valueOf(objectchild.get("variable").toString());
                                String text = "";
                                switch (type) {
                                    case 1:
                                        if (variable.equalsIgnoreCase("clave_articulo")) {
                                            text = articuloTB.getClave();
                                        } else if (variable.equalsIgnoreCase("clave_alterna")) {
                                            text = articuloTB.getClaveAlterna();
                                        } else if (variable.equalsIgnoreCase("descripcion")) {
                                            text = articuloTB.getNombreMarca();
                                        } else if (variable.equalsIgnoreCase("precio")) {
                                            text = Tools.roundingValue(articuloTB.getPrecioVentaGeneral(), 2);
                                        } else if (variable.equalsIgnoreCase("girocomercial_empresa")) {
                                            text = "Giro Comercial";
                                        } else if (variable.equalsIgnoreCase("representante_empresa")) {
                                            text = Session.REPRESENTANTE_EMPRESA;
                                        } else if (variable.equalsIgnoreCase("telefono_empresa")) {
                                            text = Session.TELEFONO_EMPRESA;
                                        } else if (variable.equalsIgnoreCase("celular_empresa")) {
                                            text = Session.CELULAR_EMPRESA;
                                        } else {
                                            text = String.valueOf(objectchild.get("text"));
                                        }
                                        break;
                                    case 2:
                                        if (variable.equalsIgnoreCase("clave_articulo")) {
                                            text = articuloTB.getClave();
                                        } else if (variable.equalsIgnoreCase("clave_alterna")) {
                                            text = articuloTB.getClaveAlterna();
                                        } else if (variable.equalsIgnoreCase("descripcion")) {
                                            text = articuloTB.getNombreMarca();
                                        } else if (variable.equalsIgnoreCase("precio")) {
                                            text = Tools.roundingValue(articuloTB.getPrecioVentaGeneral(), 2);
                                        } else if (variable.equalsIgnoreCase("fecha_registro")) {

                                        } else if (variable.equalsIgnoreCase("fecha_vencimiento")) {

                                        } else if (variable.equalsIgnoreCase("girocomercial_empresa")) {
                                            text = "Giro Comercial";
                                        } else if (variable.equalsIgnoreCase("representante_empresa")) {
                                            text = Session.REPRESENTANTE_EMPRESA;
                                        } else if (variable.equalsIgnoreCase("telefono_empresa")) {
                                            text = Session.TELEFONO_EMPRESA;
                                        } else if (variable.equalsIgnoreCase("celular_empresa")) {
                                            text = Session.CELULAR_EMPRESA;
                                        } else {
                                            text = String.valueOf(objectchild.get("text"));
                                        }
                                        break;
                                    case 3:

                                        break;
                                    default:
                                        break;
                                }
                                panel.getChildren().add(addBarCode(
                                        text,
                                        Double.parseDouble(objectchild.get("x").toString()),
                                        Double.parseDouble(objectchild.get("y").toString()),
                                        Double.parseDouble(objectchild.get("with").toString()),
                                        Double.parseDouble(objectchild.get("height").toString()),
                                        String.valueOf(objectchild.get("fontname")),
                                        Double.parseDouble(objectchild.get("fontsize").toString())
                                ));
                            }
                        }
                    }
                }
            }
            vbEtiquetas.getChildren().add(panel);
        }
    }

    private JSONObject obtenerObjetoJSON(final String codigoJSON) {
        JSONParser lector = new JSONParser();
        JSONObject objectJSON = null;
        try {
            Object recuperado = lector.parse(codigoJSON);
            objectJSON = (JSONObject) recuperado;
        } catch (ParseException ex) {
            System.out.println("Posicion:" + ex.getPosition());
            System.out.println(ex);
        }
        return objectJSON;
    }

    private JSONArray obtenerArrayJSON(final String codigoJSON) {
        JSONParser lector = new JSONParser();
        JSONArray arrayJSON = null;

        try {
            Object recuperado = lector.parse(codigoJSON);
            arrayJSON = (JSONArray) recuperado;
        } catch (ParseException e) {
            System.out.println("Posicion: " + e.getPosition());
            System.out.println(e);
        }

        return arrayJSON;
    }

    private WritableImage generateBarCode(String value, java.awt.Font font) {
        int heightBuffer = (int) (60);
        WritableImage wr = null;
        try {
            Barcode barCode = BarcodeFactory.createCode128(value);
            barCode.setBarHeight(30);
            barCode.setBarWidth(1);
            barCode.setDrawingText(true);
            barCode.setFont(font);
            BufferedImage bufferedImage = new BufferedImage(barCode.getWidth(), heightBuffer, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = bufferedImage.createGraphics();
            barCode.draw((Graphics2D) graphics, 0, 0);
            wr = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int px = 0; px < bufferedImage.getWidth(); px++) {
                for (int py = 0; py < bufferedImage.getHeight(); py++) {
                    pw.setArgb(px, py, bufferedImage.getRGB(px, py));
                }
            }
        } catch (BarcodeException | OutputException ex) {

        }
        return wr;
    }

    private ImageView addBarCode(String value, double x, double y, double width, double height, String fontFamily, double size) {
        CodBar ivCodigo = new CodBar(value, x, y, new java.awt.Font(fontFamily, java.awt.Font.BOLD, (int) size));
        ivCodigo.setImage(generateBarCode(ivCodigo.getTexto(), ivCodigo.getFont()));
        ivCodigo.setFitWidth(width == -1 ? ivCodigo.getImage().getWidth() : width);
        ivCodigo.setFitHeight(height == -1 ? ivCodigo.getImage().getHeight() : height);
        return ivCodigo;
    }

    private Text addText(String value, double x, double y, double width, double height, String fontFamily, double size) {
        Text text = new Text(value, x, y);
        text.setFontText(fontFamily, FontWeight.BOLD, FontPosture.REGULAR, size);
        text.setPrefSize(width == -1 ? USE_COMPUTED_SIZE : width, height == -1 ? USE_COMPUTED_SIZE : height);
        return text;
    }

    public double converMmToPoint(double mm) {
        return (mm * 2.83465) / 1;
    }

    public double converMmToPixel(double mm) {
        return BigDecimal.valueOf((mm * 3.7795275591) / 1).setScale(0, RoundingMode.HALF_UP).doubleValue();
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
    }

    public PageFormat getPageFormat(PrinterJob pj) {
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();
        paper.setSize(converMmToPoint(widthEtiquetaMM), converMmToPoint(heightEtiquetaMM));
        paper.setImageableArea(0, 0, pf.getWidth(), pf.getHeight());
        pf.setOrientation(orientacionEtiqueta);
        pf.setPaper(paper);
        return pf;
    }
    
     private void eventAcercar() {
        final double zoomFactor = 1.2;
        // do the resizing
        panel.setScaleX(zoomFactor * panel.getScaleX());
        panel.setScaleY(zoomFactor * panel.getScaleY());
        // refresh ScrollPane scroll positions & content bounds
        scrollPane.layout();
    }

    private void eventAlejar() {
        final double zoomFactor = 1 / 1.2;
        // do the resizing
        panel.setScaleX(zoomFactor * panel.getScaleX());
        panel.setScaleY(zoomFactor * panel.getScaleY());
        // refresh ScrollPane scroll positions & content bounds
        scrollPane.layout();
    }

    @FXML
    private void onKeyPressedAcercar(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            eventAcercar();
        }
    }

    @FXML
    private void onActionAcercar(ActionEvent event) {
        eventAcercar();
    }

    @FXML
    private void onKeyPressedAlejar(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            eventAlejar();
        }
    }

    @FXML
    private void onActionAlejar(ActionEvent event) {
        eventAlejar();
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
                g2d.drawImage(bufferedImage, 5, -10, (int) converMmToPoint(widthEtiquetaMM), (int) converMmToPoint(heightEtiquetaMM), null);
                g2d.dispose();
                return (PAGE_EXISTS);
            } else {
                return (NO_SUCH_PAGE);
            }
        }

    }

}
