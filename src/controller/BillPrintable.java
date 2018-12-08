/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.TableView;
import model.ArticuloTB;

/**
 *
 * @author Aleza
 */
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
        int result = NO_SUCH_PAGE;
        if (pageIndex == 0) {

            Graphics2D g2d = (Graphics2D) graphics;

            double width = pageFormat.getImageableWidth();

            g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());

            ////////// code by alqama//////////////
            FontMetrics metrics = g2d.getFontMetrics(new Font("Arial", Font.PLAIN, 9));

            try {
                /*Draw Header*/
                int y = 20;
                int yShift = 12;
                int headerRectHeight = 15;

                ///////////////// Product names Get ///////////
                String pn1a = "SHAMPOO SEDAL DE 60G";
                String pn2a = "PAPEL SUAVE";
                String pn3a = "TOALLA DE PAPEL MATRIZ";
                String pn4a = "DETERGENTE ACE DE 500 GM";
                String pn5a = "DETERGENTE ARIEL DE 500 GM";
                String pn6a = "DETERGENTE OPAL DE 500 GM";
                ///////////////// Product names Get ///////////

                ///////////////// Product price Get ///////////
                double pp1a = Double.valueOf("100.00");
                double pp2a = Double.valueOf("100.00");
                double pp3a = Double.valueOf("100.00");
                double pp4a = Double.valueOf("100.00");
                double pp5a = Double.valueOf("200.00");
                double pp6a = Double.valueOf("140.00");
                double sum = pp1a + pp2a + pp3a + pp4a;
                ///////////////// Product price Get ///////////
                Date date = new Date();
                SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat hora = new SimpleDateFormat("hh:mm:ss aa");

                g2d.setFont(new Font("Arial", Font.PLAIN, 9));
                g2d.setPaint(Color.BLACK);
                g2d.drawString(Session.NOMBREEMPRESA, centerText(width, metrics, Session.NOMBREEMPRESA), y);
                y += yShift;
                g2d.drawString(Session.DIRECCION, centerText(width, metrics, Session.DIRECCION), y);
                y += yShift;
                g2d.drawString("TEL: " + Session.TELEFONO + " CEL:" + Session.CELULAR, centerText(width, metrics, "TEL: " + Session.TELEFONO + " CEL:" + Session.CELULAR), y);
                y += yShift;
                g2d.drawString("RUC " + Session.RUC, centerText(width, metrics, "RUC " + Session.RUC), y);
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
                            (tvList.getItems().get(i).getCantidad() + "." + tvList.getItems().get(i).getNombreMarca().get()).length() >= 25
                            ? (tvList.getItems().get(i).getCantidad() + "." + tvList.getItems().get(i).getNombreMarca().get()).substring(0, 25)
                            : (tvList.getItems().get(i).getCantidad() + "." + tvList.getItems().get(i).getNombreMarca().get()),
                            0, y);
                    g2d.drawString(getText(tvList.getItems().get(i).getImporte().get()), rightText(width, metrics, getText(tvList.getItems().get(i).getImporte().get())), y);
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
                y += yShift;
                g2d.drawString("OP. GRAVADA", 0, y);
                g2d.drawString(getText(gravada), rightText(width, metrics, getText(gravada)), y);
                y += yShift;
                g2d.drawString("I.G.V", 0, y);
                g2d.drawString(getText(igv), rightText(width, metrics, getText(igv)), y);
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
                y += yShift;
                g2d.drawString("\n", 0, y);
                y += yShift;
                g2d.drawString("¡Que tengas un buen dia!", centerText(width, metrics, "¡Que tengas un buen dia!"), y);
                y += yShift;
                g2d.drawString("\n", 0, y);
                y += yShift;
                g2d.drawString("Fecha de Venta: " + fecha.format(date), centerText(width, metrics, "Fecha de Venta: " + fecha.format(date)), y);
                y += yShift;
                g2d.drawString("Hora: " + hora.format(date), centerText(width, metrics, "Hora: " + hora.format(date)), y);
                y += yShift;
                g2d.drawString("\n", 0, y);
            } catch (NumberFormatException r) {
            }

            result = PAGE_EXISTS;
        }
        return result;
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
}
