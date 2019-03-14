package controller;

import br.com.adilson.util.PrinterMatrix;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Window;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import model.ArticuloTB;

public class BillPrintable implements Printable {

    private double subTotal;
    private double descuento;
    private double gravada;
    private double igv;
    private double total;

    private double efectivo;
    private double vuelto;

    private String tickt;

    private TableView<ArticuloTB> tvList;

    public BillPrintable() {

    }

    public BillPrintable(double subTotal, double descuento, double gravada, double igv, double total, double efectivo, double vuelto, String tickt, TableView<ArticuloTB> tvList) {
        this.subTotal = subTotal;
        this.descuento = descuento;
        this.gravada = gravada;
        this.igv = igv;
        this.total = total;
        this.efectivo = efectivo;
        this.vuelto = vuelto;
        this.tickt = tickt;
        this.tvList = tvList;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

        if (pageIndex == 0) {
            Graphics2D g2d = (Graphics2D) graphics;
            double width = pageFormat.getImageableWidth();

            g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
            ////////// code by alqama//////////////
            FontMetrics metrics = g2d.getFontMetrics(new Font("Arial", Font.PLAIN, 9));

            int y = 20;
            int yShift = 12;
            ///////////////// Product price Get ///////////
            Date date = new Date();
            SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat hora = new SimpleDateFormat("hh:mm:ss aa");

            g2d.setFont(new Font("Arial", Font.PLAIN, 9));
            g2d.setPaint(Color.BLACK);
            g2d.drawString(Session.RAZONSOCIAL_EMPRESA, centerText(width, metrics, Session.RAZONSOCIAL_EMPRESA), y);
//                y += yShift;
//                g2d.drawString(Session.DIRECCION, centerText(width, metrics, Session.DIRECCION), y);
//                y += yShift;
//                g2d.drawString("TEL: " + Session.TELEFONO + " CEL:" + Session.CELULAR, centerText(width, metrics, "TEL: " + Session.TELEFONO + " CEL:" + Session.CELULAR), y);
//                y += yShift;
//                g2d.drawString("RUC " + Session.RUC, centerText(width, metrics, "RUC " + Session.RUC), y);
            y += yShift;
            g2d.drawString("N° Ticket: " + tickt, centerText(width, metrics, "N° Ticket: " + tickt), y);
            y += yShift;
            g2d.drawString("\n", 0, y);
            y += yShift;
            g2d.drawString("CANT ARTICULO", 0, y);
            g2d.drawString("IMPORTE", (int) (width - metrics.stringWidth("IMPORTE")), y);

            for (int i = 0; i < tvList.getItems().size(); i++) {
                y += yShift;
                g2d.drawString(
                        (tvList.getItems().get(i).getCantidad() + " " + tvList.getItems().get(i).getNombreMarca()).length() >= 25
                        ? (tvList.getItems().get(i).getCantidad() + " " + tvList.getItems().get(i).getNombreMarca()).substring(0, 25)
                        : (tvList.getItems().get(i).getCantidad() + " " + tvList.getItems().get(i).getNombreMarca()),
                        0, y);
                g2d.drawString(getText(tvList.getItems().get(i).getTotalImporte()), rightText(width, metrics, getText(tvList.getItems().get(i).getTotalImporte())), y);
            }

            y += yShift;
            g2d.drawString("\n", 0, y);
            y += yShift;
            g2d.drawString("TOTAL DESCUENTO", 0, y);
            g2d.drawString(getText(descuento), rightText(width, metrics, getText(descuento)), y);
            y += yShift;
            g2d.drawString("SUBTOTAL", 0, y);
            g2d.drawString(getText(subTotal), rightText(width, metrics, getText(subTotal)), y);
            y += yShift;
            g2d.drawString("\n", 0, y);
//                y += yShift;
//                g2d.drawString("OP. GRAVADA", 0, y);
//                g2d.drawString(getText(gravada), rightText(width, metrics, getText(gravada)), y);
//                y += yShift;
//                g2d.drawString("I.G.V", 0, y);
//                g2d.drawString(getText(igv), rightText(width, metrics, getText(igv)), y);
            y += yShift;
            g2d.drawString("IMPORTE TOTAL", 0, y);
            g2d.drawString(getText(total), rightText(width, metrics, getText(total)), y);
            y += yShift;
            g2d.drawString("\n", 0, y);
            y += yShift;
            g2d.drawString("EFECTIVO", 0, y);
            g2d.drawString(getText(efectivo), rightText(width, metrics, getText(efectivo)), y);
            y += yShift;
            g2d.drawString("VUELTO", 0, y);
            g2d.drawString(getText(vuelto), rightText(width, metrics, getText(vuelto)), y);
//                y += yShift;
//                g2d.drawString("Son: CIENTO CUARENTA Y NUEVE SOLES", 0, y);
//                y += yShift;
//                g2d.drawString("\n", 0, y);
//                y += yShift;
//                g2d.drawString("¡Que tengas un buen dia!", centerText(width, metrics, "¡Que tengas un buen dia!"), y);
            y += yShift;
            g2d.drawString("\n", 0, y);
            y += yShift;
            g2d.drawString("Fecha: " + fecha.format(date), 0, y);
            g2d.drawString("Hora: " + hora.format(date), rightText(width, metrics, "Hora: " + hora.format(date)), y);

            g2d.dispose();
            return (PAGE_EXISTS);
        } else {
            return (NO_SUCH_PAGE);
        }

    }

    public int centerText(double width, FontMetrics metrics, String text) {
        return (int) ((width - metrics.stringWidth(text)) / 2);
    }

    public int rightText(double width, FontMetrics metrics, String text) {
        return (int) (width - metrics.stringWidth(text));
    }

    private String getText(double value) {
        return Tools.roundingValue(value, 2);
    }

    public void modelTicket(Window window, int sheetWidth, int rows, int lines, ArrayList<HBox> object, String messageClassTitle, String messageClassContent) {
        int column = sheetWidth;
        try {
            PrinterMatrix p = new PrinterMatrix();

            p.setOutSize(rows, column);
            short linesbefore = 0;
            short linescurrent = 0;
            short linesafter = 0;
            int linescount = 0;
            int rowscount = 0;
            for (int i = 0; i < object.size(); i++) {
                HBox hBox = object.get(i);
                rowscount += 1;
                linescount = rowscount + linesafter;
                linesafter = 0;
                if (hBox.getChildren().size() > 1) {
                    int columnI = 0;
                    int columnF = 0;
                    int columnA = 0;
                    int countColumns = 0;
                    rowscount = linescount;
                    for (int v = 0; v < hBox.getChildren().size(); v++) {
                        TextFieldTicket field = (TextFieldTicket) hBox.getChildren().get(v);
                        columnI = columnA;
                        columnF = columnI + field.getColumnWidth();
                        columnA = columnF;
                        if (null != field.getAlignment()) {
                            switch (field.getAlignment()) {
                                case CENTER_LEFT:
                                    p.printTextWrap(linescount + linesafter, field.getLines(), columnI, columnF, field.getText());
                                    linesbefore = (short) field.getLines();
                                    linescurrent = (short) (linesbefore == 1 ? linesbefore : linescurrent);
                                    countColumns++;
                                    linesafter = hBox.getChildren().size() == countColumns ? linescurrent : 0;
                                    break;
                                case CENTER:
                                    p.printTextWrap(linescount + linesafter, field.getLines(), ((columnI + columnF) - field.getText().length()) / 2, columnF, field.getText());
                                    linesbefore = (short) field.getLines();
                                    linescurrent = (short) (linesbefore == 1 ? linesbefore : linescurrent);
                                    countColumns++;
                                    linesafter = hBox.getChildren().size() == countColumns ? linescurrent : 0;
                                    break;
                                case CENTER_RIGHT:
                                    p.printTextWrap(linescount + linesafter, field.getLines(), columnF - field.getText().length(), columnF, field.getText());
                                    linesbefore = (short) field.getLines();
                                    linescurrent = (short) (linesbefore == 1 ? linesbefore : linescurrent);
                                    countColumns++;
                                    linesafter = hBox.getChildren().size() == countColumns ? linescurrent : 0;
                                    break;
                                default:
                                    p.printTextWrap(linescount + linesafter, field.getLines(), columnI, columnF, field.getText());
                                    linesbefore = (short) field.getLines();
                                    linescurrent = (short) (linesbefore == 1 ? linesbefore : linescurrent);
                                    countColumns++;
                                    linesafter = hBox.getChildren().size() == countColumns ? linescurrent : 0;
                                    break;
                            }
                        }

                    }
                } else {
                    rowscount = linescount;
                    TextFieldTicket field = (TextFieldTicket) hBox.getChildren().get(0);
                    if (null != field.getAlignment()) {
                        switch (field.getAlignment()) {
                            case CENTER_LEFT:
                                p.printTextWrap(linescount + linesafter, field.getLines(), 0, field.getColumnWidth(), field.getText());
                                linesafter = (short) field.getLines();
                                break;
                            case CENTER:
                                p.printTextWrap(linescount + linesafter, field.getLines(), (field.getColumnWidth() - field.getText().length()) / 2, field.getColumnWidth(), field.getText());
                                linesafter = (short) field.getLines();
                                break;
                            case CENTER_RIGHT:
                                p.printTextWrap(linescount + linesafter, field.getLines(), field.getColumnWidth() - field.getText().length(), field.getColumnWidth(), field.getText());
                                linesafter = (short) field.getLines();
                                break;
                            default:
                                p.printTextWrap(linescount + linesafter, field.getLines(), 0, field.getColumnWidth(), field.getText());
                                linesafter = (short) field.getLines();
                                break;
                        }
                    }
                }
            }
            int salida = object.size() + lines;
            salida++;
            p.printTextWrap(salida, 0, 0, column, "\n");
            salida++;
            p.printTextWrap(salida, 0, 0, column, "\n");
            salida++;
            p.printTextWrap(salida, 0, 0, column, "\n");
            salida++;
            p.printTextWrap(salida, 0, 0, column, "\n");
            salida++;
            p.printTextWrap(salida, 0, 0, column, "\n");
            p.toFile("c:\\temp\\impresion.txt");
//            printDoc("c:\\temp\\impresion.txt");
        } catch (Exception e) {
            Tools.AlertMessage(window, Alert.AlertType.ERROR, messageClassTitle, messageClassContent, false);
        }
    }

    private void printDoc(String ruta) {
        File file = new File(ruta);
        FileInputStream inputStream = null;
        try {
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException ex) {
            }
            if (inputStream == null) {
                return;
            }
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            PrintService service = findPrintService(Session.NOMBRE_IMPRESORA, printService);
            DocPrintJob job = service.createPrintJob();

            byte[] bytes = readFileToByteArray(file);
            byte[] cutP = new byte[]{0x1d, 'V', 1};
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(bytes);
            outputStream.write(cutP);
            byte c[] = outputStream.toByteArray();
            Doc doc = new SimpleDoc(c, flavor, null);
            job.print(doc, null);
        } catch (IOException | PrintException e) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private PrintService findPrintService(String printerName, PrintService[] services) {
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }
        return null;
    }

    private static byte[] readFileToByteArray(File file) {
        byte[] bArray = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bArray);
            fis.close();

        } catch (IOException ioExp) {
        }
        return bArray;
    }

}
