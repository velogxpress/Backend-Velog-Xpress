package com.velogexpress.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.velogexpress.entity.*;
import com.velogexpress.repository.*;
import com.velogexpress.service.PdfService;
import com.velogexpress.service.R2Service;
import com.velogexpress.tools.DateTime;
import com.velogexpress.tools.DecimalFormat;
import com.velogexpress.tools.Variables;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.properties.AreaBreakType;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;


@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private static final DateTimeFormatter FRENCH_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", Locale.FRENCH);

    @Value("${file.upload-dir}") private String uploadDir;
    private final R2Service r2Service;


    // ==================== QR CODE GENERATOR ===============================
    public static ImageData generateQrCode(String text)
            throws WriterException, IOException {

        QRCodeWriter qrWriter = new QRCodeWriter();
        BitMatrix matrix = qrWriter.encode(text, BarcodeFormat.QR_CODE, 100, 100);
        java.io.ByteArrayOutputStream pngOutput = new java.io.ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", pngOutput);

        return ImageDataFactory.create(pngOutput.toByteArray());
    }

    // ==================== BAR CODE GENERATOR ===============================
    public static ImageData generateBarCode(String text)
            throws WriterException, IOException {

        Code128Writer qrWriter = new Code128Writer();
        BitMatrix matrix = qrWriter.encode(text, BarcodeFormat.CODE_128, 100, 30);
        java.io.ByteArrayOutputStream pngOutput = new java.io.ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", pngOutput);
        return ImageDataFactory.create(pngOutput.toByteArray());
    }

    private Cell noBorder(String text, boolean bold) {
        Paragraph p = new Paragraph(text);
        if (bold) p.setBold();
        return new Cell().add(p).setBorder(Border.NO_BORDER);
    }

    private boolean isElectronicCategory(OrderDetails detail) {
        if (detail == null || detail.getCategory() == null || detail.getCategory().getPart() == null) {
            return false;
        }
        String part = detail.getCategory().getPart().trim();
        return part.equalsIgnoreCase("Electronique") || part.equalsIgnoreCase("Électronique");
    }

    private String formatPoundsOrQuantity(OrderDetails detail) {
        if (detail == null || detail.getPounds() == null) {
            return "N/A";
        }
        double value = detail.getPounds();
        if (isElectronicCategory(detail)) {
            String quantity = value == Math.rint(value)
                    ? String.format("%.0f", value)
                    : String.format("%.2f", value);
            return quantity + " UNITÉ";
        }
        return String.format("%.2f LBS", value);
    }

    private Cell noBorderCell(String text, boolean bold, int fontSize) {
        Paragraph p = new Paragraph(text);
        if (bold) p.setBold();
        p.setFontSize(fontSize);

        return new Cell()
                .add(p)
                .setBorder(Border.NO_BORDER)
                .setPadding(3);
    }

    private Cell noBorders(String text, boolean bold) {
        Paragraph p = new Paragraph(text)
                .setMargin(2)
                .setPadding(0)
                .setMultipliedLeading(0.9f); // réduit espas ligne
        if (bold) p.setBold();
        return new Cell()
                .add(p)
                .setBorder(Border.NO_BORDER)
                .setPadding(0)
                .setMargin(2);
    }


    private Cell noBorderValueCell(String text, int fontSize) {
        return new Cell()
                .add(new Paragraph(text))
                .setBorder(Border.NO_BORDER)
                .setFontSize(fontSize)
                .setTextAlignment(TextAlignment.LEFT)
                .setPaddingLeft(0)   // 🔥 clé
                .setPaddingRight(0)
                .setPaddingTop(2)
                .setPaddingBottom(2);
    }

    private Cell cell(String text, float size, PdfFont font) {
        return new Cell()
                .add(new Paragraph(text))
                .setFontSize(size)
                .setFont(font)
                .setPadding(2)
                .setBorderTop(new SolidBorder(1f))
                .setBorderBottom(new SolidBorder(1f))
                .setBorderLeft(new SolidBorder(1f))
                .setBorderRight(new SolidBorder(1f));
    }

    public Cell valueCell(String text, PdfFont font) {
        Paragraph p = new Paragraph(text)
                .setFont(font)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.LEFT);

        return new Cell()
                .add(p)
                .setBorder(Border.NO_BORDER);
    }

    private String valueOrNA(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "N/A";
        }
        return value;
    }

    private String numberOrNA(Number value, String suffix) {
        if (value == null) {
            return "N/A";
        }
        return value + (suffix != null ? suffix : "");
    }

    private String numberOrZero(Number value) {
        if (value == null) {
            return "0";
        }
        return value.toString();
    }

    private double calculateChange(Facture facture) {
        double received = facture.getEffectif() == null ? 0D : facture.getEffectif();
        double total = facture.getAmount() == null ? 0D : facture.getAmount();
        double discount = facture.getDiscount() == null ? 0D : facture.getDiscount();
        return Math.max(received - (total - discount), 0D);
    }

    private String safe(Supplier<Object> supplier) {
        try {
            Object value = supplier.get();

            if (value == null) {
                return "N/A";
            }

            String str = String.valueOf(value).trim();

            if (str.isEmpty() || str.equalsIgnoreCase("null")) {
                return "N/A";
            }

            return str;

        } catch (NullPointerException e) {
            return "N/A";
        }
    }

    public static String cleanVelogCode(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        String prefix = "VELOG XPRESS-";

        if (value.startsWith(prefix)) {
            return value.substring(prefix.length());
        }

        return value;
    }
@Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private MainaddressRepository mainaddressRepository;
    @Autowired
    private TauxRepository tauxRepository;
    @Autowired
    private FactureRepository factureRepository;
    @Autowired
    private FactureDetailsRepository factureDetailsRepository;
    @Autowired
    private AmnistyRepository amnistyRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public byte[] clientFactureDownload(String usercode,String order) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // 1 pouce = 25.4 mm, 1 pouce = 72 points
            float mmToPt = 72f / 25.4f;
            float width = 215.9f * mmToPt;  // 215.9 mm ≈ 8.5 pouces
            float height = 279.4f * mmToPt; // 279.4 mm ≈ 11 pouces

            PageSize pageSize = new PageSize(width, height);

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, pageSize);
            document.setMargins(20, 10, 20, 10);

            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            PdfFont fontPlain = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);

            ClassPathResource resource = new ClassPathResource(Variables.logoPath);
            ImageData imageData = ImageDataFactory.create(resource.getPath());
            Image image = new Image(imageData).scaleToFit(100, 50).setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(image);
            Mainaddress address = new Mainaddress();
            Mainaddress addr = null;
            Optional<Mainaddress> mainaddress = Optional.of(address);
            mainaddress=mainaddressRepository.findById(1L);
            if (mainaddress.isPresent()) {
                 addr = mainaddress.get();
            }
            assert addr != null;
            document.add(new Paragraph(addr.getAddressline()+", "+addr.getCity()+", "+addr.getState()+" "+addr.getZipcode()).setFont(fontBold).setFontSize(10).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(addr.getPhone()+" | info@velogxpress.com").setFont(fontBold).setFontSize(10).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("___________________________________________________________________").setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("FACTURE").setFont(fontBold).setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("___________________________________________________________________").setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(""));
            List<OrderDetails> orderDetails =orderDetailsRepository.getOrderDetailFactureByClient(usercode,order);
            double pwa = 0;
            double tot = 0;
            double to=tauxRepository.findByDevise("Dollars US").getSale();
            for (OrderDetails orderDetail : orderDetails) {
                pwa += orderDetail.getPounds();
                tot += orderDetail.getSubtotal();
            }
            String cond="N/A";
            if(orderDetails.get(0).getCondition()!=null){
                cond=orderDetails.get(0).getCondition();
            }
            Table tableInfo = new Table(UnitValue.createPercentArray(new float[]{2,1,7})).useAllAvailableWidth();
            tableInfo.addCell(noBorderCell("Date commande",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(orderDetails.get(0).getShip().getDate(),9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("#. Commande",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(orderDetails.get(0).getShip().getShiporder(),9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("Client",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(orderDetails.get(0).getRec_name(),9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("Telephone",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(orderDetails.get(0).getRec_phone(),9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("Total poids(lbs)",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(pwa+" lbs",9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("Montant",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(DecimalFormat.round2(tot)+" $US",9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("Frais Assurance",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(orderDetails.get(0).getCitypoundfee().getInsurance().getAmount()+" $US",9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("Montant total $US",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell((DecimalFormat.round2(tot+orderDetails.get(0).getCitypoundfee().getInsurance().getAmount()))+" $US",9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("Taux du jour",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(to+" HTG pour 1 $US",9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("Montant total en GDES",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(((DecimalFormat.round2(tot+orderDetails.get(0).getCitypoundfee().getInsurance().getAmount())*to))+" $HT",9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("Status",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(cond,9)).setTextAlignment(TextAlignment.LEFT);

            document.add(tableInfo);
            document.add(new Paragraph(""));

            Image qr = new Image(generateBarCode(orderDetails.get(0).getShip().getShiporder())).setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(qr).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);

            document.add(new Paragraph(""));
            document.add(new Paragraph(""));

            Table table = new Table(UnitValue.createPercentArray(new float[]{1,1,2,1,1,1,1,1})).useAllAvailableWidth();
            table.addHeaderCell(new Cell().add(new Paragraph("DATE").setFont(fontBold).setFontSize(8)));
            table.addHeaderCell(new Cell().add(new Paragraph("CODE UPC").setFont(fontBold).setFontSize(8)));
            table.addHeaderCell(new Cell().add(new Paragraph("TRACKING NUMBER").setFont(fontBold).setFontSize(8)));
            table.addHeaderCell(new Cell().add(new Paragraph("CATEGORIE").setFont(fontBold).setFontSize(8)));
            table.addHeaderCell(new Cell().add(new Paragraph("POIDS").setFont(fontBold).setFontSize(8)));
            table.addHeaderCell(new Cell().add(new Paragraph("PRIX").setFont(fontBold).setFontSize(8)));
            table.addHeaderCell(new Cell().add(new Paragraph("DOUANE/HAZMAT").setFont(fontBold).setFontSize(8)));
            table.addHeaderCell(new Cell().add(new Paragraph("SOUS-TOTAL").setFont(fontBold).setFontSize(8)));

            if(!orderDetails.isEmpty()){
                orderDetails.forEach(detail -> {
                    String track;
                    String frais;
                    String douane;
                    String total;
                    if(detail.getTracking()==null){
                        track="N/A";
                    }else{
                        track=detail.getTracking();
                    }
                    if(detail.getCategory().getPart()=="Normal"){
                        if(detail.getCitypoundfee().getPounds().getAmount()==null){
                            frais="N/A";
                        }else{
                            frais=detail.getCitypoundfee().getPounds().getAmount()+" $US";
                        }
                    }else{
                        if(detail.getPrice()==null){
                            frais="N/A";
                        }else{
                            frais=detail.getPrice()+" $US";
                        }
                    }

                    if(detail.getDouane()==null){
                        douane="N/A";
                    }else{
                        douane=detail.getDouane()+" $US";
                    }
                    if(detail.getSubtotal()==null){
                        total="N/A";
                    }else{
                        total=DecimalFormat.round2(detail.getSubtotal())+" $US";
                    }


                    table.addCell(new Paragraph(DateTime.FORMATDATETIMEFRENCH(detail.getCreatedAt())).setFont(fontPlain).setFontSize(8));
                    table.addCell(new Paragraph(detail.getUpc()).setFont(fontPlain).setFontSize(8));
                    table.addCell(new Paragraph(track).setFont(fontPlain).setFontSize(8));
                    table.addCell(new Paragraph(detail.getCategory().getDescription()).setFont(fontPlain).setFontSize(8));
                    table.addCell(new Paragraph(detail.getPounds()+" lbs").setFont(fontPlain).setFontSize(8));
                    table.addCell(new Paragraph(frais).setFont(fontPlain).setFontSize(8));
                    table.addCell(new Paragraph(douane).setFont(fontPlain).setFontSize(8));
                    table.addCell(new Paragraph(total).setFont(fontPlain).setFontSize(8));
                });
            }
            document.add(table);
            document.add(new Paragraph(""));
            Table paymentTable = new Table(UnitValue.createPercentArray(new float[]{2,3,5})).useAllAvailableWidth();

// Antèt
            paymentTable.addCell(
                    new Cell(1,3)
                            .add(new Paragraph("METHODE DE PAIEMENT"))
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
            );

// Zelle
            paymentTable.addCell("Zelle").setFontSize(8);
            paymentTable.addCell("786 928 1241 ").setFontSize(8);
            paymentTable.addCell("SAINT DELIVRANCE MULTI-SERVICES, " +
                    "LLC").setFontSize(8);

// MonCash
            paymentTable.addCell("MonCash").setFontSize(8);
            paymentTable.addCell("+509 3712-9095 ").setFontSize(8);
            paymentTable.addCell("VELOG XPRESS").setFontSize(8);

// NatCash
            paymentTable.addCell("NatCash").setFontSize(8);
            paymentTable.addCell("+509 4005-6080 ").setFontSize(8);
            paymentTable.addCell("VELOG XPRESS").setFontSize(8);
            // UNIBANK US
            paymentTable.addCell("UNIBANK USD").setFontSize(7);
            paymentTable.addCell("560-1522-1856141").setFontSize(7);
            paymentTable.addCell("VELOG XPRESS").setFontSize(7);
            // UNIBANK GDES
            paymentTable.addCell("UNIBANK GDES").setFontSize(7);
            paymentTable.addCell("560-1521-1856133").setFontSize(7);
            paymentTable.addCell("VELOG XPRESS").setFontSize(7);

            // BUH USD
            paymentTable.addCell("BUH USD").setFontSize(7);
            paymentTable.addCell("12000099064").setFontSize(7);
            paymentTable.addCell("VELOG XPRESS").setFontSize(7);
            // BUH GDES
            paymentTable.addCell("BUH GDES").setFontSize(7);
            paymentTable.addCell("12000099056").setFontSize(7);
            paymentTable.addCell("VELOG XPRESS").setFontSize(7);


            paymentTable.setMargin(5);
            document.add(paymentTable);

            document.add(new Paragraph(""));
            qr = new Image(
                    generateQrCode(orderDetails.get(0).getShip().getShiporder())
            )
                    .setWidth(80)
                    .setHeight(80)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            document.add(qr);

            document.add(new Paragraph(""));
            document.add(new Paragraph("Les documents et appareils électroniques ne sont pas taxés au poids.\n " +
                    "Un frais fixe est appliqué selon le type de document ou d’appareil.").setFontSize(8));
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF", e);
        }
    }

    @Override
    public byte[] manifestDownload(String usercode, Long city) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // 1 pouce = 25.4 mm, 1 pouce = 72 points
            float mmToPt = 72f / 25.4f;
            float width = 215.9f * mmToPt;  // 215.9 mm ≈ 8.5 pouces
            float height = 279.4f * mmToPt; // 279.4 mm ≈ 11 pouces

            PageSize pageSize = new PageSize(width, height);

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, pageSize);
            document.setMargins(20, 10, 20, 10);
            int increase=0;
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            PdfFont fontPlain = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);


            Table table = new Table(UnitValue.createPercentArray(new float[]{6f,12f,20f,10f,20f,10f,11f,11f})).useAllAvailableWidth();

            table.addCell(new Paragraph("#").setHorizontalAlignment(HorizontalAlignment.CENTER).setFont(fontBold));
            table.addCell(new Paragraph("No Traçage").setFont(fontBold));
            table.addCell(new Paragraph("Exp.").setFont(fontBold));
            table.addCell(new Paragraph("Tel").setFont(fontBold));
            table.addCell(new Paragraph("Client").setFont(fontBold));
            table.addCell(new Paragraph("Tel").setFont(fontBold));
            table.addCell(new Paragraph("Poids").setHorizontalAlignment(HorizontalAlignment.CENTER).setFont(fontBold));
            table.addCell(new Paragraph("Signature").setHorizontalAlignment(HorizontalAlignment.CENTER).setFont(fontBold));
            List<OrderDetails> detailLis=orderDetailsRepository.findShipIDCity(usercode, city);
            for(OrderDetails details:detailLis){

                table.addCell(new Paragraph(valueOrNA(details.getShip().getShiporder())).setFont(fontPlain).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.CENTER));
                table.addCell(new Paragraph(valueOrNA(details.getUpc())).setFont(fontPlain).setFontSize(8));
                table.addCell(new Paragraph(valueOrNA(details.getExp_name())).setFont(fontPlain).setFontSize(8));
                table.addCell(new Paragraph(valueOrNA(details.getExp_phone())).setFont(fontPlain).setFontSize(8));
                table.addCell(new Paragraph(valueOrNA(details.getRec_name())).setFont(fontPlain).setFontSize(8));
                table.addCell(new Paragraph(valueOrNA(details.getRec_phone())).setFont(fontPlain).setFontSize(8));
                table.addCell(new Paragraph(numberOrNA(details.getPounds()," lbs")).setFont(fontPlain).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.CENTER));
                table.addCell(new Paragraph(" ").setFont(fontPlain).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.CENTER));
           increase++;
            }

            ClassPathResource resource = new ClassPathResource(Variables.logoPath);
            ImageData imageData = ImageDataFactory.create(resource.getPath());
            Image image = new Image(imageData).scaleToFit(100, 50).setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(image);

            Mainaddress address = new Mainaddress();
            Mainaddress addr = null;
            Optional<Mainaddress> mainaddress = Optional.of(address);
            mainaddress=mainaddressRepository.findById(1L);
            if (mainaddress.isPresent()) {
                addr = mainaddress.get();
            }
            assert addr != null;
            document.add(new Paragraph(addr.getAddressline()+", "+addr.getCity()+", "+addr.getState()+" "+addr.getZipcode()).setFont(fontBold).setFontSize(10).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(addr.getPhone()+" | info@velogxpress.com").setFont(fontBold).setFontSize(10).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("___________________________________________________________________").setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("COURRIER MANIFEST").setFont(fontBold).setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("___________________________________________________________________").setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(""));

            Table tableInfo = new Table(UnitValue.createPercentArray(new float[]{2,1,7})).useAllAvailableWidth();
            tableInfo.addCell(noBorderCell("Commande",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(detailLis.get(0).getShip().getShiporder(),9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("Date",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(detailLis.get(0).getShip().getDate(),9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("Destination",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(detailLis.get(0).getCitypoundfee().getCity().getDescription(),9)).setTextAlignment(TextAlignment.LEFT);
            document.add(tableInfo);
            document.add(new Paragraph(" "));

            Image qr = new Image(generateBarCode(detailLis.get(0).getShip().getShiporder())).setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(qr).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);

            document.add(new Paragraph(" "));
            document.add(table);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Quantité Colis  : "+increase).setFontSize(11).setFont(fontBold));
            document.add(new Paragraph("Signature Agent : _______________________________________").setFont(fontBold).setFontSize(11));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Date de reception   : _________ / ___________ / _____________").setFont(fontBold).setFontSize(11));

            document.close();
        return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF", e);
        }
    }

    @Override
    public byte[] factureDownload(String facturecode) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            float TICKET_WIDTH = 204f;   // printable width
            float MARGIN = 2f;
            float CONTENT_WIDTH = TICKET_WIDTH - (MARGIN * 2);

            PageSize ticketSize = new PageSize(TICKET_WIDTH, 2000);

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf, ticketSize);

            doc.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);

            // 🔒 TOP SAFE ZONE POUR 80MM (OBLIGATOIRE)
            doc.add(
                    new Table(1)
                            .setWidth(UnitValue.createPointValue(CONTENT_WIDTH))
                            .addCell(
                                    new Cell()
                                            .setBorder(Border.NO_BORDER)
                                            .setMinHeight(25) // ≈ 8–9 mm
                            )
            );

            PdfFont fontBold = null;
            PdfFont fontPlain = null;
            try {
                fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                fontPlain = PdfFontFactory.createFont(StandardFonts.HELVETICA);
//                  fontBold = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);
//                  fontPlain = PdfFontFactory.createFont(StandardFonts.COURIER);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            // --------------------------------------------------------
            // BUSINESS INFO
            // --------------------------------------------------------
            Table header = new Table(UnitValue.createPointArray(new float[]{216}));
            header.setWidth(UnitValue.createPointValue(CONTENT_WIDTH));
            header.setBorder(Border.NO_BORDER);

            ClassPathResource resource = new ClassPathResource(Variables.logoPath);
            ImageData imageData = ImageDataFactory.create(resource.getPath());
            Image image = new Image(imageData)
                    .setWidth(90)
                    .setHeight(55)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            header.addCell(
                    new Cell()
                            .setBorder(Border.NO_BORDER)
                            .setPadding(0)
                            .setMargin(0)
                            .setTextAlignment(TextAlignment.CENTER)
                            .add(image)
            );
            Mainaddress address = new Mainaddress();
            Mainaddress addr = null;
            Optional<Mainaddress> mainaddress = Optional.of(address);
            mainaddress = mainaddressRepository.findById(1L);
            if (mainaddress.isPresent()) {
                addr = mainaddress.get();
            }
            assert addr != null;

            header.addCell(
                    new Cell()
                            .setBorder(Border.NO_BORDER)
                            .setPadding(0)
                            .setMargin(0)
                            .add(
                                    new Paragraph(addr.getAddressline() + ", " +
                                            addr.getCity() + ", " +
                                            addr.getState() + " " +
                                            addr.getZipcode())
                                            .setFontSize(10).setFont(fontBold)
                                            .setTextAlignment(TextAlignment.CENTER)
                            )
            );


            header.addCell(
                    noBorder("info@velogxpress.com", true)
                            .setFontSize(10)
                            .setTextAlignment(TextAlignment.CENTER)
            );

            header.addCell(
                    noBorder(addr.getPhone(), true)
                            .setFontSize(10)
                            .setTextAlignment(TextAlignment.CENTER)
            );

            header.addCell(
                    noBorder("Lundi - Samedi : 9h00 AM - 5h00 PM", true)
                            .setFontSize(10)
                            .setTextAlignment(TextAlignment.CENTER)
            );
            header.setMarginBottom(0);
            doc.add(header);
            doc.add(new Paragraph("____________________________").setFont(fontBold)
                    .setMargin(2)
                    .setMultipliedLeading(0.8f)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("FACTURE CONSOMMATION FINALE").setFont(fontBold).setFontSize(10).setMarginBottom(0)
                    .setMultipliedLeading(0.8f).setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("____________________________").setFont(fontBold)
                    .setMargin(2)
                    .setMultipliedLeading(0.8f)
                    .setTextAlignment(TextAlignment.CENTER));


            // --------------------------------------------------------
            // INVOICE INFO
            // --------------------------------------------------------
            Facture facture = factureRepository.findByCode(facturecode);
            List<FactureDetails> details = factureDetailsRepository.findByFactureCode(facture.getId());
            Table infoTable = new Table(new float[]{90, 5, 121});
            infoTable.setWidth(CONTENT_WIDTH);
            //infoTable.setFixedLayout();

            infoTable.addCell(noBorders("Date", true)).setFontSize(9);
            infoTable.addCell(noBorders(":", true)).setFontSize(9);
            infoTable.addCell(noBorders(facture.getDate(), false)).setFontSize(9);

            infoTable.addCell(noBorders("Facture ID", true)).setFontSize(9);
            infoTable.addCell(noBorders(":", true)).setFontSize(9);
            infoTable.addCell(noBorders(facture.getCode(), false)).setFontSize(9);

            infoTable.addCell(noBorders("Order ID", true)).setFontSize(9);
            infoTable.addCell(noBorders(":", true)).setFontSize(9);
            infoTable.addCell(noBorders(facture.getShip().getShiporder(), false)).setFontSize(9);
            infoTable.addCell(noBorders("Client", true)).setFontSize(9);
            infoTable.addCell(noBorders(":", true)).setFontSize(9);
            infoTable.addCell(noBorders(facture.getClient(), false)).setFontSize(9);
            infoTable.addCell(noBorders("Destination", true)).setFontSize(9);
            infoTable.addCell(noBorders(":", true)).setFontSize(9);
            infoTable.addCell(noBorders(valueOrNA(facture.getDestination()), false)).setFontSize(9);
            infoTable.addCell(noBorders("Telephone", true)).setFontSize(9);
            infoTable.addCell(noBorders(":", true)).setFontSize(9);
            infoTable.addCell(noBorders(facture.getClientphone(), false)).setFontSize(9);
            infoTable.addCell(noBorders("Status", true)).setFontSize(9);
            infoTable.addCell(noBorders(":", true)).setFontSize(9);
            infoTable.addCell(noBorders(facture.getStatus(), false)).setFontSize(9);

            infoTable.setMargin(2);

            doc.add(infoTable);

            doc.add(new Paragraph(" "));

            // --------------------------------------------------------
            // PRODUCTS TABLE
            // --------------------------------------------------------
            //Table table = new Table(new float[]{55, 30, 20, 25, 25, 35});
            Table table = new Table(new float[]{55, 30, 20, 23, 22, 26});
            //table.setWidth(UnitValue.createPercentValue(100));
            table.setWidth(UnitValue.createPointValue(CONTENT_WIDTH));
            table.setFixedLayout();
            table.setKeepTogether(false);
            table.setMarginLeft(0);
            table.setMarginRight(0);

            table.addCell(cell("TRACKING", 9, fontBold));
            table.addCell(cell("CAT", 9, fontBold));
            table.addCell(cell("LB", 9, fontBold));
            table.addCell(cell("PRI", 9, fontBold));
            table.addCell(cell("DO/HA", 9, fontBold));
            table.addCell(cell("TOT", 9, fontBold));


            double pound = 0;

            for (FactureDetails p : details) {
                table.addCell(cell(valueOrNA(p.getColis()), 9, fontPlain));
                table.addCell(cell(p.getCategory().getDescription(), 9, fontPlain));
                table.addCell(cell(String.format("%.2f", p.getPounds()), 9, fontPlain));
                table.addCell(cell(String.format("%.2f", p.getFixedprice()), 9, fontPlain));
                table.addCell(cell(String.format("%.2f", p.getFee()), 9, fontPlain));
                table.addCell(cell(String.format("%.2f", p.getSoubtotal()), 9, fontPlain));
                pound = pound + p.getPounds();
            }

            doc.add(table);
            doc.add(new Paragraph(""));
            // --------------------------------------------------------
            // TOTAL FINAL
            // --------------------------------------------------------
            Table totalTable = new Table(new float[]{90, 5, 121});
            totalTable.setWidth(CONTENT_WIDTH);
            //totalTable.setFixedLayout();
            double to=tauxRepository.findByDevise("Dollars US").getSale();
            totalTable.addCell(noBorders("Poids Total", true).setFontSize(9));
            totalTable.addCell(noBorders(":", true).setFontSize(9));
            totalTable.addCell(valueCell(DecimalFormat.round2(pound) + " lbs", fontBold).setFontSize(9));
            totalTable.addCell(noBorders("Frais d'assurance", true).setFontSize(9));
            totalTable.addCell(noBorders(":", true).setFontSize(9));
            totalTable.addCell(valueCell(facture.getAssurance() + " $US", fontBold).setFontSize(9));
            totalTable.addCell(noBorders("Montant Total(US)", true).setFontSize(9));
            totalTable.addCell(noBorders(":", true).setFontSize(9));
            totalTable.addCell(valueCell(String.format("%.2f",facture.getAmount()) + " $US", fontBold).setFontSize(9));
            totalTable.addCell(noBorders("Taux de change", true).setFontSize(9));
            totalTable.addCell(noBorders(":", true).setFontSize(9));
            totalTable.addCell(valueCell(to + " GDES", fontBold).setFontSize(9));
            totalTable.addCell(noBorders("Montant Total(GDES)", true).setFontSize(9));
            totalTable.addCell(noBorders(":", true).setFontSize(9));
            totalTable.addCell(valueCell(String.format("%.2f",(facture.getAmount()*to)) + " GDES", fontBold).setFontSize(9));
            totalTable.addCell(noBorders("Effectif", true).setFontSize(9));
            totalTable.addCell(noBorders(":", true).setFontSize(9));
            totalTable.addCell(valueCell(String.format("%.2f",facture.getEffectif()) + " $US", fontBold).setFontSize(9));
            totalTable.addCell(noBorders("Rabais", true).setFontSize(9));
            totalTable.addCell(noBorders(":", true).setFontSize(9));
            totalTable.addCell(valueCell(numberOrZero(facture.getDiscount()) + " $US", fontBold).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
            totalTable.addCell(noBorders("Monnaie", true).setFontSize(9));
            totalTable.addCell(noBorders(":", true).setFontSize(9));
            totalTable.addCell(valueCell(String.format("%.2f", calculateChange(facture)) + " $US", fontBold).setFontSize(9));
            totalTable.addCell(noBorders("Balance", true).setFontSize(9));
            totalTable.addCell(noBorders(":", true).setFontSize(9));
            totalTable.addCell(valueCell(DecimalFormat.round2(facture.getBalance()) + " $US", fontBold).setFontSize(9));

            totalTable.setMargin(5);
            doc.add(totalTable);

            // --------------------------------------------------------
            // FOOTER
            // --------------------------------------------------------
            Table qrTable = new Table(new float[]{111, 90});
            qrTable.setWidth(CONTENT_WIDTH);
            qrTable.setBorder(Border.NO_BORDER);

// QR Images
            Image order = new Image(generateQrCode(facture.getShip().getShiporder()))
                    .setHorizontalAlignment(HorizontalAlignment.LEFT)
                    .setWidth(50)
                    .setHeight(50);

            Image client = new Image(generateQrCode(facture.getClient()))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setWidth(50)
                    .setHeight(50);

// ✅ Cells SANS border
            qrTable.addCell(new Cell().add(order).setBorder(Border.NO_BORDER));
            qrTable.addCell(new Cell().add(client).setBorder(Border.NO_BORDER));

            doc.add(new Paragraph("").setMarginBottom(6));
            doc.add(qrTable);

            doc.add(new Paragraph("").setMarginBottom(6));
            Image code = new Image(generateBarCode(facturecode)).setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(code);

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF", e);
        }
    }

    private Cell labelCell(String text, PdfFont font) {
        return new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(10))
                .setBorder(Border.NO_BORDER);
    }

    private Cell valueCells(String text, PdfFont font) {
        return new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(10))
                .setBorder(Border.NO_BORDER);
    }

    private Cell headerCell(String text, PdfFont font) {
        return new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(11))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY);
    }

    private Cell bodyCell(String text, PdfFont font) {
        return new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(10));
    }

    @Override
    public byte[] factureDownloadA4(String facturecode) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // ✅ PAGE SETUP (LETTER 8.5 x 11)
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf, PageSize.LETTER);

            // margins nòmal (0.5 inch)
            doc.setMargins(36, 36, 36, 36);

            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fontPlain = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // --------------------------------------------------------
            // HEADER (LOGO + BUSINESS INFO)
            // --------------------------------------------------------
            Table header = new Table(new float[]{1, 2});
            header.setWidth(UnitValue.createPercentValue(100));

            // LOGO
            ClassPathResource resource = new ClassPathResource(Variables.logoPath);
            ImageData imageData = ImageDataFactory.create(resource.getPath());
            Image image = new Image(imageData)
                    .setWidth(120)
                    .setAutoScaleHeight(true);

            header.addCell(new Cell()
                    .add(image)
                    .setBorder(Border.NO_BORDER));

            // BUSINESS INFO
            Optional<Mainaddress> mainaddress = mainaddressRepository.findById(1L);
            Mainaddress addr = mainaddress.orElse(new Mainaddress());

            Cell businessInfo = new Cell().setBorder(Border.NO_BORDER);

            businessInfo.add(new Paragraph("\n")
                    .setFont(fontBold).setFontSize(14));

            businessInfo.add(new Paragraph(
                    addr.getAddressline() + ", " +
                            addr.getCity() + ", " +
                            addr.getState() + " " +
                            addr.getZipcode()
            ).setFontSize(10));

            businessInfo.add(new Paragraph("info@velogxpress.com").setFontSize(10));
            businessInfo.add(new Paragraph(addr.getPhone()).setFontSize(10));
            businessInfo.add(new Paragraph("Lundi - Samedi : 9h00 AM - 5h00 PM").setFontSize(10));

            header.addCell(businessInfo);
            header.setMarginBottom(0);
            doc.add(header);

//            doc.add(new Paragraph("\n"));

            doc.add(new Paragraph("___________________________________________________________________________").setFont(fontBold)
                    .setMargin(2)
                    .setMultipliedLeading(0.8f)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("FACTURE CONSOMMATION FINALE").setFont(fontBold).setFontSize(12).setMarginBottom(0)
                    .setMultipliedLeading(0.8f).setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("___________________________________________________________________________").setFont(fontBold)
                    .setMargin(2)
                    .setMultipliedLeading(0.8f)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("\n"));

            // --------------------------------------------------------
            // INVOICE INFO
            // --------------------------------------------------------
            Facture facture = factureRepository.findByCode(facturecode);
            List<FactureDetails> details = factureDetailsRepository.findByFactureCode(facture.getId());

            Table infoTable = new Table(new float[]{2, 4});
            infoTable.setWidth(UnitValue.createPercentValue(100));

            infoTable.addCell(labelCell("Date", fontBold));
            infoTable.addCell(valueCells(facture.getDate(), fontPlain));

            infoTable.addCell(labelCell("Facture ID", fontBold));
            infoTable.addCell(valueCells(facture.getCode(), fontPlain));

            infoTable.addCell(labelCell("Order ID", fontBold));
            infoTable.addCell(valueCells(facture.getShip().getShiporder(), fontPlain));

            infoTable.addCell(labelCell("Client", fontBold));
            infoTable.addCell(valueCells(facture.getClient(), fontPlain));

            infoTable.addCell(labelCell("Telephone", fontBold));
            infoTable.addCell(valueCells(facture.getClientphone(), fontPlain));

            infoTable.addCell(labelCell("Destination", fontBold));
            infoTable.addCell(valueCells(valueOrNA(facture.getDestination()), fontPlain));

            infoTable.addCell(labelCell("Status", fontBold));
            infoTable.addCell(valueCells(valueOrNA(facture.getStatus()), fontPlain));


            doc.add(infoTable);

            doc.add(new Paragraph("\n"));

            // --------------------------------------------------------
            // PRODUCTS TABLE
            // --------------------------------------------------------
            Table table = new Table(new float[]{2,1, 2, 1, 2, 2, 2});
            table.setWidth(UnitValue.createPercentValue(100));

            table.addHeaderCell(headerCell("TRACKING", fontBold));
            table.addHeaderCell(headerCell("VX TRACK", fontBold));
            table.addHeaderCell(headerCell("CATEGORIE", fontBold));
            table.addHeaderCell(headerCell("LBS", fontBold));
            table.addHeaderCell(headerCell("PRIX", fontBold));
            table.addHeaderCell(headerCell("DOUA/HAZM", fontBold));
            table.addHeaderCell(headerCell("TOTAL", fontBold));

            double pound = 0;

            for (FactureDetails p : details) {
                table.addCell(bodyCell(valueOrNA(p.getDescription()), fontPlain));
                table.addCell(bodyCell(valueOrNA(p.getColis()), fontPlain));
                table.addCell(bodyCell(p.getCategory().getDescription(), fontPlain));
                table.addCell(bodyCell(String.format("%.2f", p.getPounds()), fontPlain));
                table.addCell(bodyCell(String.format("%.2f", p.getFixedprice()), fontPlain));
                table.addCell(bodyCell(String.format("%.2f", p.getFee()), fontPlain));
                table.addCell(bodyCell(String.format("%.2f", p.getSoubtotal()), fontPlain));

                pound += p.getPounds();
            }

            doc.add(table);

            doc.add(new Paragraph("\n"));

            // --------------------------------------------------------
            // TOTALS
            // --------------------------------------------------------
            double taux = tauxRepository.findByDevise("Dollars US").getSale();

            Table totalTable = new Table(new float[]{3, 2});
            totalTable.setWidth(UnitValue.createPercentValue(50));
            totalTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);

            totalTable.addCell(labelCell("Poids Total", fontBold));
            totalTable.addCell(valueCells(DecimalFormat.round2(pound) + " lbs", fontPlain));

            totalTable.addCell(labelCell("Frais d'assurance", fontBold));
            totalTable.addCell(valueCells(String.format("%.2f", facture.getAssurance()) + " $US", fontPlain));

            totalTable.addCell(labelCell("Montant Total (USD)", fontBold));
            totalTable.addCell(valueCells(String.format("%.2f", facture.getAmount()) + " $US", fontPlain));

            totalTable.addCell(labelCell("Taux de change", fontBold));
            totalTable.addCell(valueCells(taux + " GDES pour 1 $US", fontPlain));

            totalTable.addCell(labelCell("Montant Total(GDES)", fontBold));
            totalTable.addCell(valueCells(String.format("%.2f", facture.getAmount() * taux)+ " GDES", fontPlain));

            totalTable.addCell(labelCell("Effectif", fontBold));
            totalTable.addCell(valueCells(DecimalFormat.round2(facture.getEffectif()) + " $US", fontPlain));

            totalTable.addCell(labelCell("Rabais", fontBold));
            totalTable.addCell(valueCells(numberOrZero(facture.getDiscount()) + " $US", fontPlain));

            totalTable.addCell(labelCell("Monnaie", fontBold));
            totalTable.addCell(valueCells(String.format("%.2f", calculateChange(facture)) + " $US", fontPlain));

            totalTable.addCell(labelCell("Balance", fontBold));
            totalTable.addCell(valueCells(DecimalFormat.round2(facture.getBalance()) + " $US", fontPlain));

            doc.add(totalTable);


            // --------------------------------------------------------
            // FOOTER
            // --------------------------------------------------------
            Table qrTable = new Table(new float[]{111, 90});
            qrTable.setWidth(UnitValue.createPercentValue(100));
           // qrTable.setWidth(CONTENT_WIDTH);
            qrTable.setBorder(Border.NO_BORDER);

// QR Images
            Image order = new Image(generateQrCode(facture.getShip().getShiporder()))
                    .setHorizontalAlignment(HorizontalAlignment.LEFT)
                    .setWidth(80)
                    .setHeight(80);

            Image client = new Image(generateQrCode(facture.getSurcursal().getClient().getUsercode()))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setWidth(80)
                    .setHeight(80);

// ✅ Cells SANS border
            qrTable.addCell(new Cell().add(order).setBorder(Border.NO_BORDER));
            qrTable.addCell(new Cell().add(client).setBorder(Border.NO_BORDER));

            doc.add(new Paragraph("").setMarginBottom(6));
            doc.add(qrTable);

            doc.add(new Paragraph("").setMarginBottom(6));
            Image code = new Image(generateBarCode(facturecode)).setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(code);

            doc.add(new Paragraph("\n\n"));


            doc.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF", e);
        }
    }

    @Override
    public String movefactureDownloadA4(String facturecode) {
        try {
            String objectKey="products/"+facturecode+".pdf";
            // ✅ PAGE SETUP (LETTER 8.5 x 11)
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf, PageSize.LETTER);

            // margins nòmal (0.5 inch)
            doc.setMargins(36, 36, 36, 36);

            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fontPlain = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // --------------------------------------------------------
            // HEADER (LOGO + BUSINESS INFO)
            // --------------------------------------------------------
            Table header = new Table(new float[]{1, 2});
            header.setWidth(UnitValue.createPercentValue(100));

            // LOGO
            ClassPathResource resource = new ClassPathResource(Variables.logoPath);
            ImageData imageData = ImageDataFactory.create(resource.getPath());
            Image image = new Image(imageData)
                    .setWidth(120)
                    .setAutoScaleHeight(true);

            header.addCell(new Cell()
                    .add(image)
                    .setBorder(Border.NO_BORDER));

            // BUSINESS INFO
            Optional<Mainaddress> mainaddress = mainaddressRepository.findById(1L);
            Mainaddress addr = mainaddress.orElse(new Mainaddress());

            Cell businessInfo = new Cell().setBorder(Border.NO_BORDER);

            businessInfo.add(new Paragraph("\n")
                    .setFont(fontBold).setFontSize(14));

            businessInfo.add(new Paragraph(
                    addr.getAddressline() + ", " +
                            addr.getCity() + ", " +
                            addr.getState() + " " +
                            addr.getZipcode()
            ).setFontSize(10));

            businessInfo.add(new Paragraph("info@velogxpress.com").setFontSize(10));
            businessInfo.add(new Paragraph(addr.getPhone()).setFontSize(10));
            businessInfo.add(new Paragraph("Lundi - Samedi : 9h00 AM - 5h00 PM").setFontSize(10));

            header.addCell(businessInfo);
            header.setMarginBottom(0);
            doc.add(header);

//            doc.add(new Paragraph("\n"));

            doc.add(new Paragraph("___________________________________________________________________________").setFont(fontBold)
                    .setMargin(2)
                    .setMultipliedLeading(0.8f)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("FACTURE CONSOMMATION FINALE").setFont(fontBold).setFontSize(12).setMarginBottom(0)
                    .setMultipliedLeading(0.8f).setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("___________________________________________________________________________").setFont(fontBold)
                    .setMargin(2)
                    .setMultipliedLeading(0.8f)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("\n"));

            // --------------------------------------------------------
            // INVOICE INFO
            // --------------------------------------------------------
            Facture facture = factureRepository.findByCode(facturecode);
            List<FactureDetails> details = factureDetailsRepository.findByFactureCode(facture.getId());

            Table infoTable = new Table(new float[]{2, 4});
            infoTable.setWidth(UnitValue.createPercentValue(100));

            infoTable.addCell(labelCell("Date", fontBold));
            infoTable.addCell(valueCells(facture.getDate(), fontPlain));

            infoTable.addCell(labelCell("Facture ID", fontBold));
            infoTable.addCell(valueCells(facture.getCode(), fontPlain));

            infoTable.addCell(labelCell("Order ID", fontBold));
            infoTable.addCell(valueCells(facture.getShip().getShiporder(), fontPlain));

            infoTable.addCell(labelCell("Client", fontBold));
            infoTable.addCell(valueCells(facture.getClient(), fontPlain));

            infoTable.addCell(labelCell("Telephone", fontBold));
            infoTable.addCell(valueCells(facture.getClientphone(), fontPlain));

            infoTable.addCell(labelCell("Destination", fontBold));
            infoTable.addCell(valueCells(valueOrNA(facture.getDestination()), fontPlain));

            infoTable.addCell(labelCell("Status", fontBold));
            infoTable.addCell(valueCells(valueOrNA(facture.getStatus()), fontPlain));


            doc.add(infoTable);

            doc.add(new Paragraph("\n"));

            // --------------------------------------------------------
            // PRODUCTS TABLE
            // --------------------------------------------------------
            Table table = new Table(new float[]{2,1, 2, 1, 2, 2, 2});
            table.setWidth(UnitValue.createPercentValue(100));

            table.addHeaderCell(headerCell("TRACKING", fontBold));
            table.addHeaderCell(headerCell("VX TRACK", fontBold));
            table.addHeaderCell(headerCell("CATEGORIE", fontBold));
            table.addHeaderCell(headerCell("LBS", fontBold));
            table.addHeaderCell(headerCell("PRIX", fontBold));
            table.addHeaderCell(headerCell("DOUA/HAZM", fontBold));
            table.addHeaderCell(headerCell("TOTAL", fontBold));

            double pound = 0;

            for (FactureDetails p : details) {
                table.addCell(bodyCell(valueOrNA(p.getDescription()), fontPlain));
                table.addCell(bodyCell(valueOrNA(p.getColis()), fontPlain));
                table.addCell(bodyCell(p.getCategory().getDescription(), fontPlain));
                table.addCell(bodyCell(String.format("%.2f", p.getPounds()), fontPlain));
                table.addCell(bodyCell(String.format("%.2f", p.getFixedprice()), fontPlain));
                table.addCell(bodyCell(String.format("%.2f", p.getFee()), fontPlain));
                table.addCell(bodyCell(String.format("%.2f", p.getSoubtotal()), fontPlain));

                pound += p.getPounds();
            }

            doc.add(table);

            doc.add(new Paragraph("\n"));

            // --------------------------------------------------------
            // TOTALS
            // --------------------------------------------------------
            double taux = tauxRepository.findByDevise("Dollars US").getSale();

            Table totalTable = new Table(new float[]{3, 2});
            totalTable.setWidth(UnitValue.createPercentValue(50));
            totalTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);

            totalTable.addCell(labelCell("Poids Total", fontBold));
            totalTable.addCell(valueCells(DecimalFormat.round2(pound) + " lbs", fontPlain));

            totalTable.addCell(labelCell("Frais d'assurance", fontBold));
            totalTable.addCell(valueCells(String.format("%.2f", facture.getAssurance()) + " $US", fontPlain));

            totalTable.addCell(labelCell("Montant Total (USD)", fontBold));
            totalTable.addCell(valueCells(String.format("%.2f", facture.getAmount()) + " $US", fontPlain));

            totalTable.addCell(labelCell("Taux de change", fontBold));
            totalTable.addCell(valueCells(taux + " GDES pour 1 $US", fontPlain));

            totalTable.addCell(labelCell("Montant Total(GDES)", fontBold));
            totalTable.addCell(valueCells(String.format("%.2f", facture.getAmount() * taux)+ " GDES", fontPlain));

            totalTable.addCell(labelCell("Effectif", fontBold));
            totalTable.addCell(valueCells(DecimalFormat.round2(facture.getEffectif()) + " $US", fontPlain));

            totalTable.addCell(labelCell("Rabais", fontBold));
            totalTable.addCell(valueCells(numberOrZero(facture.getDiscount()) + " $US", fontPlain));

            totalTable.addCell(labelCell("Monnaie", fontBold));
            totalTable.addCell(valueCells(String.format("%.2f", calculateChange(facture)) + " $US", fontPlain));

            totalTable.addCell(labelCell("Balance", fontBold));
            totalTable.addCell(valueCells(DecimalFormat.round2(facture.getBalance()) + " $US", fontPlain));

            doc.add(totalTable);


            // --------------------------------------------------------
            // FOOTER
            // --------------------------------------------------------
            Table qrTable = new Table(new float[]{111, 90});
            qrTable.setWidth(UnitValue.createPercentValue(100));
            // qrTable.setWidth(CONTENT_WIDTH);
            qrTable.setBorder(Border.NO_BORDER);

// QR Images
            Image order = new Image(generateQrCode(facture.getShip().getShiporder()))
                    .setHorizontalAlignment(HorizontalAlignment.LEFT)
                    .setWidth(80)
                    .setHeight(80);

            Image client = new Image(generateQrCode(facture.getSurcursal().getClient().getUsercode()))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setWidth(80)
                    .setHeight(80);

// ✅ Cells SANS border
            qrTable.addCell(new Cell().add(order).setBorder(Border.NO_BORDER));
            qrTable.addCell(new Cell().add(client).setBorder(Border.NO_BORDER));

            doc.add(new Paragraph("").setMarginBottom(6));
            doc.add(qrTable);

            doc.add(new Paragraph("").setMarginBottom(6));
            Image code = new Image(generateBarCode(facturecode)).setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(code);

            doc.add(new Paragraph("\n\n"));

            doc.close();
            r2Service.upload(out.toByteArray(), objectKey, "application/pdf");
            return r2Service.publicUrl(objectKey);

        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF", e);
        }
    }

    @Override
    public byte[] labelDownload(String upc) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);

            PageSize labelSize = new PageSize(288, 432); // 4×6"
            Document doc = new Document(pdf, labelSize);
            doc.setMargins(10, 10, 10, 10);

            SolidBorder solid = new SolidBorder(ColorConstants.BLACK, 0.8f);

            OrderDetails orderDetails = orderDetailsRepository.findByUpc(upc);
            String type="CD";
            String legende="LB";
            if(!orderDetails.getType().equals("Directe")) {
               type="CI";
            }

            if(orderDetails.getPounds()>1){
                legende="LBS";
            }

            // ---------------- FRAME PRINCIPAL ----------------
            Table frame = new Table(1)
                    .setWidth(UnitValue.createPercentValue(100))
                    .setBorder(solid)
                    .setKeepTogether(true);

            // ---------------- HEADER ----------------
            float[] headerCols = {200, 60};
            Table header = new Table(headerCols);
            header.setWidth(UnitValue.createPercentValue(100));

            Image order = new Image(generateQrCode(orderDetails.getShip().getShiporder()))
                    .setHorizontalAlignment(HorizontalAlignment.LEFT)
                    .setWidth(80)
                    .setHeight(80);

            header.addCell(new Cell()
                    .add(order).setBorder(Border.NO_BORDER));

            header.addCell(new Cell()
                    .add(new Paragraph(type).setFontSize(30).setBold())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(Border.NO_BORDER));

            Cell headerWrap = new Cell().add(header);
            headerWrap.setBorder(Border.NO_BORDER).setBorderBottom(solid);
            frame.addCell(headerWrap);

            // ---------------- SHIP TO ----------------
            Cell ship = new Cell().setBorder(Border.NO_BORDER).setBorderBottom(solid);
            if(!orderDetails.getType().equals("Directe")) {
                Table shipTable = new Table(new float[]{1, 1});
                shipTable.setWidth(UnitValue.createPercentValue(100));

// ------------------
// LEFT (EXPEDITEUR)
// ------------------
                Cell left = new Cell().setBorder(Border.NO_BORDER);

                left.add(new Paragraph("EXPEDITEUR").setFontSize(9));
                left.add(new Paragraph(valueOrNA(orderDetails.getExp_name()).toUpperCase())
                        .setBold().setFontSize(11));
                left.add(new Paragraph(valueOrNA(orderDetails.getExp_phone()))
                        .setBold().setFontSize(11));

// ------------------
// RIGHT (DESTINATAIRE)
// ------------------
                Cell right = new Cell()
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.RIGHT);

                right.add(new Paragraph("EXPEDIER A").setFontSize(9));
                right.add(new Paragraph(valueOrNA(orderDetails.getRec_name()).toUpperCase())
                        .setBold().setFontSize(11));
                right.add(new Paragraph(valueOrNA(orderDetails.getRec_phone()))
                        .setBold().setFontSize(11));

                right.add(new Paragraph(
                        safe(() -> orderDetails.getCitypoundfee().getCity().getDescription()).toUpperCase()
                                + ", " +
                                safe(() -> orderDetails.getCitypoundfee().getCity().getRegion().getDescription()).toUpperCase()
                ).setBold().setFontSize(12));

// ADD TO TABLE
                shipTable.addCell(left);
                shipTable.addCell(right);

// ADD TO DOC
                frame.addCell(shipTable);
            }else {
                ship.add(new Paragraph("EXPEDIER A").setFontSize(8));
                ship.add(new Paragraph(valueOrNA(orderDetails.getRec_name()).toUpperCase()).setBold().setFontSize(11));
                ship.add(new Paragraph(valueOrNA(orderDetails.getRec_phone())).setBold().setFontSize(11));
                ship.add(new Paragraph(safe(() -> orderDetails.getCitypoundfee().getCity().getDescription()).toUpperCase() + ", "
                        + safe(() -> orderDetails.getCitypoundfee().getCity().getRegion().getDescription()).toUpperCase()).setFontSize(13)).setBold();
                frame.addCell(ship);
            }


            // ---------------- WEIGHT & 2-DAY ----------------
            float[] zoneCols = {100, 140};
            Table zone = new Table(zoneCols).setWidth(UnitValue.createPercentValue(100));

            zone.addCell(new Cell()
                    .add(new Paragraph(orderDetails.getPounds()+" "+legende).setFontSize(18).setBold())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorderRight(solid)
                    .setBorder(Border.NO_BORDER));

            zone.addCell(new Cell()
                    .add(new Paragraph(orderDetails.getShip().getDate()).setFontSize(18).setBold())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorder(Border.NO_BORDER));

            Cell zoneWrap = new Cell().add(zone);
            zoneWrap.setBorder(Border.NO_BORDER).setBorderBottom(solid);
            frame.addCell(zoneWrap);

            // ---------------- BARCODE ----------------
            String tracking = orderDetails.getUpc();

            Barcode128 barcode128 = new Barcode128(pdf);
            barcode128.setCode(tracking);
            barcode128.setCodeType(Barcode128.CODE128);

            Image barcodeImg = new Image(barcode128.createFormXObject(pdf))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setWidth(150);

            Cell barcodeCell = new Cell().setBorder(Border.NO_BORDER).setBorderBottom(solid);
            barcodeCell.add(barcodeImg);
            frame.addCell(barcodeCell);

            doc.add(frame);
            doc.add(new Paragraph("").setMarginBottom(15));
            ClassPathResource resource = new ClassPathResource(Variables.logoPath);
            ImageData imageData = ImageDataFactory.create(resource.getPath());
            Image image = null;
            if(!orderDetails.getType().equals("Directe")) {
                image = new Image(imageData).scaleToFit(150, 80).setHorizontalAlignment(HorizontalAlignment.CENTER);
            }else{
                image = new Image(imageData).scaleToFit(150, 100).setHorizontalAlignment(HorizontalAlignment.CENTER);
            }

            doc.add(image);

            doc.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF", e);
        }
    }

    @Override
    public byte[] labelamnistyDownload(String upc) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);

            PageSize labelSize = new PageSize(288, 432); // 4×6"
            Document doc = new Document(pdf, labelSize);
            doc.setMargins(10, 10, 10, 10);

            SolidBorder solid = new SolidBorder(ColorConstants.BLACK, 0.8f);

            Amnisty amnisty = amnistyRepository.findByTracking(upc);
            String type="AM";
            String legende="LB";

            if(amnisty.getPounds()>1){
                legende="LBS";
            }

            // ---------------- FRAME PRINCIPAL ----------------
            Table frame = new Table(1)
                    .setWidth(UnitValue.createPercentValue(100))
                    .setBorder(solid)
                    .setKeepTogether(true);

            // ---------------- HEADER ----------------
            float[] headerCols = {200, 60};
            Table header = new Table(headerCols);
            header.setWidth(UnitValue.createPercentValue(100));

            Image order = new Image(generateQrCode(amnisty.getTracking()))
                    .setHorizontalAlignment(HorizontalAlignment.LEFT)
                    .setWidth(80)
                    .setHeight(80);

            header.addCell(new Cell()
                    .add(order).setBorder(Border.NO_BORDER));

            header.addCell(new Cell()
                    .add(new Paragraph(type).setFontSize(30).setBold())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(Border.NO_BORDER));

            Cell headerWrap = new Cell().add(header);
            headerWrap.setBorder(Border.NO_BORDER).setBorderBottom(solid);
            frame.addCell(headerWrap);

            // ---------------- SHIP TO ----------------
            Cell ship = new Cell().setBorder(Border.NO_BORDER).setBorderBottom(solid);
            ship.add(new Paragraph("EXPEDIER A").setBold().setFontSize(8));
            ship.add(new Paragraph(valueOrNA(amnisty.getName()).toUpperCase()).setBold().setFontSize(11));
            ship.add(new Paragraph(valueOrNA(amnisty.getTelephone())).setBold().setFontSize(11));
            ship.add(new Paragraph(safe(()->amnisty.getCitypoundfee().getCity().getDescription()).toUpperCase()+", "
                    +safe(()->amnisty.getCitypoundfee().getCity().getRegion().getDescription()).toUpperCase()).setFontSize(13)).setBold();
            frame.addCell(ship);

            // ---------------- WEIGHT & 2-DAY ----------------
            float[] zoneCols = {100, 140};
            Table zone = new Table(zoneCols).setWidth(UnitValue.createPercentValue(100));

            zone.addCell(new Cell()
                    .add(new Paragraph(amnisty.getPounds()+" "+legende).setFontSize(22).setBold())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorderRight(solid)
                    .setBorder(Border.NO_BORDER));

            zone.addCell(new Cell()
                    .add(new Paragraph(DateTime.CURRENTDATE()).setFontSize(22).setBold())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorder(Border.NO_BORDER));

            Cell zoneWrap = new Cell().add(zone);
            zoneWrap.setBorder(Border.NO_BORDER).setBorderBottom(solid);
            frame.addCell(zoneWrap);

            // ---------------- BARCODE ----------------
            String tracking = amnisty.getTracking();

            Barcode128 barcode128 = new Barcode128(pdf);
            barcode128.setCode(tracking);
            barcode128.setCodeType(Barcode128.CODE128);

            Image barcodeImg = new Image(barcode128.createFormXObject(pdf))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setWidth(150);

            Cell barcodeCell = new Cell().setBorder(Border.NO_BORDER).setBorderBottom(solid);
            barcodeCell.add(barcodeImg);
            frame.addCell(barcodeCell);

            doc.add(frame);
            doc.add(new Paragraph("").setMarginBottom(25));
            ClassPathResource resource = new ClassPathResource(Variables.logoPath);
            ImageData imageData = ImageDataFactory.create(resource.getPath());
            Image image = new Image(imageData).scaleToFit(150, 100).setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(image);

            doc.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF", e);
        }
    }

    @Override
    public byte[] rapportDownload(String upc) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Order order = orderRepository.findByShiporder(upc);
            List<OrderDetails> detailsville=orderDetailsRepository.countOrderDetailsAmountParVille(order.getId());
            OrderDetails factureTotal=orderDetailsRepository.countFacture(order.getShiporder());
            OrderDetails facturePayer=orderDetailsRepository.countFacturePayer(order.getShiporder());
            OrderDetails factureDue=orderDetailsRepository.countFactureDue(order.getShiporder());
            OrderDetails factureNA=orderDetailsRepository.countFactureNA(order.getShiporder());
            List<OrderDetails> detailsPayer=orderDetailsRepository.countFacturePayerParVille(order.getId());
            List<OrderDetails> detailsDue=orderDetailsRepository.countFactureDueParVille(order.getId());
            List<OrderDetails> detailsNA=orderDetailsRepository.countFactureNAParVille(order.getId());

            OrderDetails colisLivre=orderDetailsRepository.countColisLivre(order.getId());
            OrderDetails colisStocker=orderDetailsRepository.countColisStocker(order.getId());
            List<OrderDetails> detailscolisLivre=orderDetailsRepository.countColisLivreParVille(order.getId());
            List<OrderDetails> detailscolisStocker=orderDetailsRepository.countColisStockerParVille(order.getId());

            // =========================
            // PAGE SIZE 8.5 x 11
            // =========================
            float mmToPt = 72f / 25.4f;
            float width = 215.9f * mmToPt;
            float height = 279.4f * mmToPt;
            PageSize pageSize = new PageSize(width, height);

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc, pageSize);
            doc.setMargins(40, 40, 40, 40);

            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            PdfFont fontPlain = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);

            // =========================
            // MAIN ADDRESS
            // =========================
            Mainaddress addr = mainaddressRepository.findById(1L).orElse(null);

            // =========================
            // ===== PAGE 1 : COVER ====
            // =========================

            // --- LOGO CENTRÉ PARFAITEMENT ---
            ClassPathResource resource = new ClassPathResource(Variables.logoPath);
            ImageData imageData = ImageDataFactory.create(resource.getPath());
            Image logo = new Image(imageData);

            float logoWidth = 220;
            float logoHeight = 160;

            float centerX = (pageSize.getWidth() - logoWidth) / 2;
            float centerY = (pageSize.getHeight() - logoHeight) / 2 + 60;

            logo.scaleToFit(logoWidth, logoHeight);
            logo.setFixedPosition(centerX, centerY);
            doc.add(logo);

            // --- TITRE ---
            doc.add(new Paragraph("\n\n\n\n\n\n\n\n")); // espace sous logo

            doc.add(new Paragraph("RAPPORT GÉNÉRAL DE COMMANDE")
                    .setFont(fontBold)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("(" + order.getShiporder() + ")")
                    .setFont(fontPlain)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("\n"));

            doc.add(new Paragraph("IMPRIMÉ LE : " + DateTime.CURRENTDATE())
                    .setFont(fontPlain)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER));

            // --- FOOTER (adresse bas de page) ---
            if (addr != null) {
                doc.showTextAligned(
                        new Paragraph(addr.getAddressline() + ", " + addr.getCity() + ", "
                                + addr.getState() + " " + addr.getZipcode())
                                .setFont(fontBold)
                                .setFontSize(10),
                        pageSize.getWidth() / 2,
                        60,
                        TextAlignment.CENTER
                );

                doc.showTextAligned(
                        new Paragraph(addr.getPhone() + " | info@velogxpress.com")
                                .setFont(fontPlain)
                                .setFontSize(10),
                        pageSize.getWidth() / 2,
                        45,
                        TextAlignment.CENTER
                );
            }

            // =========================
            // ===== PAGE 2 : CONTENU ==
            // =========================
            doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            doc.add(new Paragraph("DÉTAILS DE LA COMMANDE")
                    .setFont(fontBold)
                    .setFontSize(16));

            doc.add(new Paragraph("Numéro de commande : " + order.getShiporder())
                    .setFont(fontPlain)
                    .setFontSize(12));

            doc.add(new Paragraph("\n"));

            // QR CODE
            Image qrorder = new Image(generateBarCode(order.getShiporder()))
                    .setWidth(150)
                    .setHeight(30);
            doc.add(qrorder);
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("TABLEAU DE LA COMMANDE")
                    .setFont(fontBold)
                    .setFontSize(12));

            Table table = new Table(UnitValue.createPercentArray(new float[]{1,1,1,1,1,2,2})).useAllAvailableWidth();

// ---------- HEADER ----------
            table.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("COMMANDE").setFont(fontBold).setFontSize(7)));
            table.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("CREATION").setFont(fontBold).setFontSize(7)));
            table.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE COLIS").setFont(fontBold).setFontSize(7)));
            table.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE POIDS").setFont(fontBold).setFontSize(7)));
            table.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("MONTANT").setFont(fontBold).setFontSize(7)));
            table.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("DATE EXPEDITION").setFont(fontBold).setFontSize(7)));
            table.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("STATUS").setFont(fontBold).setFontSize(7)));

// ---------- ROW DATA ----------
            String date="N/A";
            if (order.getShipdate() != null) {
                date = order.getShipdate().toString();
            }
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(order.getShiporder()).setFont(fontPlain).setFontSize(7)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(order.getDate()).setFont(fontPlain).setFontSize(7)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(order.getColisQty())).setFont(fontPlain).setFontSize(7)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(DecimalFormat.round2(order.getPoundQty()) + " lbs").setFont(fontPlain).setFontSize(7)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(DecimalFormat.round2(order.getAmount()) + " $US").setFont(fontPlain).setFontSize(7)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(date).setFont(fontPlain).setFontSize(7)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(order.getStatus()).setFont(fontPlain).setFontSize(7)));

            doc.add(table);
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("LES DESTINATIONS DE LA COMMANDE")
                    .setFont(fontBold)
                    .setFontSize(12));
            Table tableville = new Table(UnitValue.createPercentArray(new float[]{4,2,2,2})).useAllAvailableWidth();

            tableville.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("DESTINATION").setFont(fontBold).setFontSize(7)));
            tableville.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE COLIS").setFont(fontBold).setFontSize(7)));
            tableville.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE POIDS").setFont(fontBold).setFontSize(7)));
            tableville.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("MONTANT").setFont(fontBold).setFontSize(7)));
            for(OrderDetails ville : detailsville) {
                tableville.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(ville.getCitypoundfee().getCity().getDescription()).setFont(fontPlain).setFontSize(7)));
                tableville.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(ville.getId()+"").setFont(fontPlain).setFontSize(7)));
                tableville.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(DecimalFormat.round2(ville.getPounds())+"").setFont(fontPlain).setFontSize(7)));
                tableville.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(DecimalFormat.round2(ville.getSubtotal())+" $US").setFont(fontPlain).setFontSize(7)));
            }
            doc.add(tableville);
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("TABLEAU DES FACTURE")
                    .setFont(fontBold)
                    .setFontSize(12));
            Table tablefacture= new Table(UnitValue.createPercentArray(new float[]{4,2,2,2})).useAllAvailableWidth();

            tablefacture.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE FACTUE").setFont(fontBold).setFontSize(7)));
            tablefacture.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE PAYER").setFont(fontBold).setFontSize(7)));
            tablefacture.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE DUE").setFont(fontBold).setFontSize(7)));
            tablefacture.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE N/A").setFont(fontBold).setFontSize(7)));
            tablefacture.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(factureTotal.getId()+"").setFont(fontPlain).setFontSize(7)));
            tablefacture.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(facturePayer.getId()+"").setFont(fontPlain).setFontSize(7)));
            tablefacture.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(factureDue.getId()+"").setFont(fontPlain).setFontSize(7)));
            tablefacture.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(factureNA.getId()+"").setFont(fontPlain).setFontSize(7)));
            doc.add(tablefacture);

            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("TABLEAU DES FACTURE PAR VILLE")
                    .setFont(fontBold)
                    .setFontSize(12));

            Table innerPayer = new Table(UnitValue.createPercentArray(new float[]{2,1,1}))
                    .useAllAvailableWidth();

            innerPayer.addHeaderCell(new Cell().add(new Paragraph("VILLE").setFont(fontPlain).setFontSize(7)));
            innerPayer.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE").setFont(fontPlain).setFontSize(7)));
            innerPayer.addHeaderCell(new Cell().add(new Paragraph("MONTANT").setFont(fontPlain).setFontSize(7)));

            for (OrderDetails payer : detailsPayer) {
                innerPayer.addCell(new Cell().add(new Paragraph(
                        payer.getCitypoundfee().getCity().getDescription()).setFont(fontPlain).setFontSize(7)));
                innerPayer.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(
                        String.valueOf(payer.getId())).setFont(fontPlain).setFontSize(7)));
                innerPayer.addCell(new Cell().add(new Paragraph(
                        DecimalFormat.round2(payer.getSubtotal())+" $US").setFont(fontPlain).setFontSize(7)));
            }

            Table innerDue = new Table(UnitValue.createPercentArray(new float[]{2,1,1}))
                    .useAllAvailableWidth();

// headers
            innerDue.addHeaderCell(new Cell().add(new Paragraph("VILLE").setFont(fontPlain).setFontSize(7)));
            innerDue.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE").setFont(fontPlain).setFontSize(7)));
            innerDue.addHeaderCell(new Cell().add(new Paragraph("MONTANT").setFont(fontPlain).setFontSize(7)));

            for (OrderDetails due : detailsDue) {
                innerDue.addCell(new Cell().add(new Paragraph(
                        due.getCitypoundfee().getCity().getDescription()).setFont(fontPlain).setFontSize(7)));
                innerDue.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(
                        String.valueOf(due.getId())).setFont(fontPlain).setFontSize(7)));
                innerDue.addCell(new Cell().add(new Paragraph(
                        DecimalFormat.round2(due.getSubtotal())+" $US").setFont(fontPlain).setFontSize(7)));
            }

            Table innerNA = new Table(UnitValue.createPercentArray(new float[]{2,1,1}))
                    .useAllAvailableWidth();

            innerNA.addHeaderCell(new Cell().add(new Paragraph("VILLE").setFont(fontPlain).setFontSize(7)));
            innerNA.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE").setFont(fontPlain).setFontSize(7)));
            innerNA.addHeaderCell(new Cell().add(new Paragraph("MONTANT").setFont(fontPlain).setFontSize(7)));

            for (OrderDetails na : detailsNA) {
                innerNA.addCell(new Cell().add(new Paragraph(
                        na.getCitypoundfee().getCity().getDescription()).setFont(fontPlain).setFontSize(7)));
                innerNA.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(
                        String.valueOf(na.getId())).setFont(fontPlain).setFontSize(7)));
                innerNA.addCell(new Cell().add(new Paragraph(
                        DecimalFormat.round2(na.getSubtotal())+" $US").setFont(fontPlain).setFontSize(7)));
            }

            Table tablefactureville = new Table(UnitValue.createPercentArray(new float[]{4,3,3}))
                    .useAllAvailableWidth();

            tablefactureville.addHeaderCell(
                    new Cell().setTextAlignment(TextAlignment.CENTER)
                            .add(new Paragraph("FACTURE PAYER").setFont(fontBold).setFontSize(7)));

            tablefactureville.addHeaderCell(
                    new Cell().setTextAlignment(TextAlignment.CENTER)
                            .add(new Paragraph("FACTURE DUE").setFont(fontBold).setFontSize(7)));

            tablefactureville.addHeaderCell(
                    new Cell().setTextAlignment(TextAlignment.CENTER)
                            .add(new Paragraph("FACTURE N/A").setFont(fontBold).setFontSize(7)));

// 👇 table nan cell
            tablefactureville.addCell(new Cell().add(innerPayer));
            tablefactureville.addCell(new Cell().add(innerDue));
            tablefactureville.addCell(new Cell().add(innerNA));
            doc.add(tablefactureville);

            // =========================
            // ===== NEXT PAGE  : CONTENU ==
            // =========================
            doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            doc.add(new Paragraph("DÉTAILS DE LA COMMANDE")
                    .setFont(fontBold)
                    .setFontSize(16));

            doc.add(new Paragraph("Numéro de commande : " + order.getShiporder())
                    .setFont(fontPlain)
                    .setFontSize(12));

            doc.add(new Paragraph("\n"));

            // QR CODE
            qrorder = new Image(generateBarCode(order.getShiporder()))
                    .setWidth(150)
                    .setHeight(30);
            doc.add(qrorder);
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("TABLEAU DE LIVRAISON")
                    .setFont(fontBold)
                    .setFontSize(12));

            Table livraison = new Table(UnitValue.createPercentArray(new float[]{5,5}))
                    .useAllAvailableWidth();

            livraison.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE COLIS LIVRE").setFont(fontBold).setFontSize(7)));
            livraison.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE COLIS EN STOCK").setFont(fontBold).setFontSize(7)));
            livraison.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(colisLivre.getId()+"").setFont(fontPlain).setFontSize(7)));
            livraison.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(colisStocker.getId()+"").setFont(fontPlain).setFontSize(7)));
            doc.add(livraison);

            Table innerColisLivre = new Table(UnitValue.createPercentArray(new float[]{3,1,1}))
                    .useAllAvailableWidth();

            innerColisLivre.addHeaderCell(new Cell().add(new Paragraph("VILLE").setFont(fontPlain).setFontSize(7)));
            innerColisLivre.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE").setFont(fontPlain).setFontSize(7)));
            innerColisLivre.addHeaderCell(new Cell().add(new Paragraph("MONTANT").setFont(fontPlain).setFontSize(7)));

            for (OrderDetails livre : detailscolisLivre) {
                innerColisLivre.addCell(new Cell().add(new Paragraph(
                        livre.getCitypoundfee().getCity().getDescription()).setFont(fontPlain).setFontSize(7)));
                innerColisLivre.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(
                        String.valueOf(livre.getId())).setFont(fontPlain).setFontSize(7)));
                innerColisLivre.addCell(new Cell().add(new Paragraph(
                        DecimalFormat.round2(livre.getSubtotal())+" $US").setFont(fontPlain).setFontSize(7)));
            }

            Table innerColisStocker = new Table(UnitValue.createPercentArray(new float[]{3,1,1}))
                    .useAllAvailableWidth();

            innerColisStocker.addHeaderCell(new Cell().add(new Paragraph("VILLE").setFont(fontPlain).setFontSize(7)));
            innerColisStocker.addHeaderCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("QTE").setFont(fontPlain).setFontSize(7)));
            innerColisStocker.addHeaderCell(new Cell().add(new Paragraph("MONTANT").setFont(fontPlain).setFontSize(7)));

            for (OrderDetails stocker : detailscolisStocker) {
                innerColisStocker.addCell(new Cell().add(new Paragraph(
                        stocker.getCitypoundfee().getCity().getDescription()).setFont(fontPlain).setFontSize(7)));
                innerColisStocker.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(
                        String.valueOf(stocker.getId())).setFont(fontPlain).setFontSize(7)));
                innerColisStocker.addCell(new Cell().add(new Paragraph(
                        DecimalFormat.round2(stocker.getSubtotal())+" $US").setFont(fontPlain).setFontSize(7)));
            }

            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("TABLEAU DES LIVRAISON PAR VILLE")
                    .setFont(fontBold)
                    .setFontSize(12));

            Table tablelivraisonville = new Table(UnitValue.createPercentArray(new float[]{5,5}))
                    .useAllAvailableWidth();

            tablelivraisonville.addHeaderCell(
                    new Cell().setTextAlignment(TextAlignment.CENTER)
                            .add(new Paragraph("COLIS LIVRE").setFont(fontBold).setFontSize(7)));

            tablelivraisonville.addHeaderCell(
                    new Cell().setTextAlignment(TextAlignment.CENTER)
                            .add(new Paragraph("COLIS STOCKER").setFont(fontBold).setFontSize(7)));


// 👇 table nan cell
            tablelivraisonville.addCell(new Cell().add(innerColisLivre));
            tablelivraisonville.addCell(new Cell().add(innerColisStocker));
            doc.add(tablelivraisonville);

            // 👉 ICI TU PEUX AJOUTER :
            // - adresse complète
            // - table des colis
            // - détails client
            // - montants
            // - signatures

            doc.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF rapport", e);
        }
    }

    @Override
    public String create80PdfMove(OrderDetails orderDetails,String path) {
        try{
            String objectKey="products/"+orderDetails.getRec_name()+"_"+orderDetails.getShip().getShiporder()+".pdf";
            float MARGIN = 36f;
            float CONTENT_WIDTH = PageSize.LETTER.getWidth() - (MARGIN * 2);


            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);

            Document doc = new Document(pdf, PageSize.LETTER);
            doc.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
            // 🔒 TOP SAFE ZONE POUR 80MM (OBLIGATOIRE)
            doc.add(
                    new Table(1)
                            .setWidth(UnitValue.createPointValue(CONTENT_WIDTH))
                            .addCell(
                                    new Cell()
                                            .setBorder(Border.NO_BORDER)
                                            .setMinHeight(25) // ≈ 8–9 mm
                            )
            );

            PdfFont fontBold = null;
            PdfFont fontPlain = null;
            try {
                fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                fontPlain = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            // --------------------------------------------------------
            // BUSINESS INFO
            // --------------------------------------------------------
            Table header = new Table(UnitValue.createPointArray(new float[]{CONTENT_WIDTH}));
            header.setWidth(UnitValue.createPointValue(CONTENT_WIDTH));
            header.setBorder(Border.NO_BORDER);

            ClassPathResource resource = new ClassPathResource(Variables.logoPath);
            ImageData imageData = ImageDataFactory.create(resource.getPath());
            Image image = new Image(imageData)
                    .setWidth(90)
                    .setHeight(55)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            header.addCell(
                    new Cell()
                            .setBorder(Border.NO_BORDER)
                            .setPadding(0)
                            .setMargin(0)
                            .setTextAlignment(TextAlignment.CENTER)
                            .add(image)
            );
            Mainaddress address = new Mainaddress();
            Mainaddress addr = null;
            Optional<Mainaddress> mainaddress = Optional.of(address);
            mainaddress = mainaddressRepository.findById(1L);
            if (mainaddress.isPresent()) {
                addr = mainaddress.get();
            }
            assert addr != null;

            header.addCell(
                    new Cell()
                            .setBorder(Border.NO_BORDER)
                            .setPadding(0)
                            .setMargin(0)
                            .add(
                                    new Paragraph(addr.getAddressline() + ", " +
                                            addr.getCity() + ", " +
                                            addr.getState() + " " +
                                            addr.getZipcode())
                                            .setFontSize(8).setFont(fontBold)
                                            .setTextAlignment(TextAlignment.CENTER)
                            )
            );


            header.addCell(
                    noBorder("info@velogxpress.com", true)
                            .setFontSize(8)
                            .setTextAlignment(TextAlignment.CENTER)
            );

            header.addCell(
                    noBorder(addr.getPhone(), true)
                            .setFontSize(8)
                            .setTextAlignment(TextAlignment.CENTER)
            );

            header.addCell(
                    noBorder("Lundi - Samedi : 9h00 AM - 5h00 PM", true)
                            .setFontSize(8)
                            .setTextAlignment(TextAlignment.CENTER)
            );
            header.setMarginBottom(0);
            doc.add(header);
            doc.add(new Paragraph("____________________________").setFont(fontBold)
                    .setMargin(2)
                    .setMultipliedLeading(0.8f)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("FACTURE TEMPORAIRE").setFont(fontBold).setFontSize(7).setMarginBottom(0)
                    .setMultipliedLeading(0.8f).setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("____________________________").setFont(fontBold)
                    .setMargin(2)
                    .setMultipliedLeading(0.8f)
                    .setTextAlignment(TextAlignment.CENTER));


            // --------------------------------------------------------
            // INVOICE INFO
            // --------------------------------------------------------
            List<OrderDetails> details = orderDetailsRepository.findOrderDetails(orderDetails.getShip().getId(), orderDetails.getRec_name(), orderDetails.getRec_phone());
            Table infoTable = new Table(UnitValue.createPointArray(new float[]{95, 10, 255}));
            infoTable.setWidth(UnitValue.createPointValue(360));
            infoTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
            //infoTable.setFixedLayout();
            String destination="N/A";
            String det=orderDetails.getCitypoundfee().getCity().getDescription();
            if(det!=null) {
                destination=det;
            }

            infoTable.addCell(noBorders("Order ID", true)).setFontSize(7);
            infoTable.addCell(noBorders(":", true)).setFontSize(7);
            infoTable.addCell(noBorders(orderDetails.getShip().getShiporder(), false)).setFontSize(7);
            infoTable.addCell(noBorders("Client", true)).setFontSize(7);
            infoTable.addCell(noBorders(":", true)).setFontSize(7);
            infoTable.addCell(noBorders(orderDetails.getRec_name(), false)).setFontSize(7);
            infoTable.addCell(noBorders("Telephone", true)).setFontSize(7);
            infoTable.addCell(noBorders(":", true)).setFontSize(7);
            infoTable.addCell(noBorders(orderDetails.getRec_phone(), false)).setFontSize(7);
            infoTable.addCell(noBorders("Destination", true)).setFontSize(7);
            infoTable.addCell(noBorders(":", true)).setFontSize(7);
            infoTable.addCell(noBorders(destination, false)).setFontSize(7);

            infoTable.setMargin(2);

            doc.add(infoTable);

            doc.add(new Paragraph(" "));

            // --------------------------------------------------------
            // PRODUCTS TABLE
            // --------------------------------------------------------
            Table table = new Table(
                    UnitValue.createPercentArray(
                            new float[]{15, 12, 10, 8, 8, 9, 9, 11, 18}
                    )
            );

            table.setWidth(UnitValue.createPercentValue(100));
            table.setFixedLayout();
            table.setKeepTogether(false);
            table.setMarginLeft(0);
            table.setMarginRight(0);

            table.addCell(cell("TRACKING", 5, fontBold));
            table.addCell(cell("VX TRACK.", 5, fontBold));
            table.addCell(cell("CATEG.", 5, fontBold));
            boolean hasElectronic = details.stream().anyMatch(this::isElectronicCategory);
            boolean hasWeightedColis = details.stream().anyMatch(detail -> !isElectronicCategory(detail));
            String quantityHeader = hasElectronic
                    ? (hasWeightedColis ? "LBS/UNITÉ" : "UNITÉ")
                    : "LBS.";
            table.addCell(cell(quantityHeader, 5, fontBold));
            table.addCell(cell("PRIX", 5, fontBold));
            table.addCell(cell("DOU/HAZ", 5, fontBold));
            table.addCell(cell("TOTAL", 5, fontBold));
            table.addCell(cell("DATE", 5, fontBold));
            table.addCell(cell("NOTE", 5, fontBold));


            double pound = 0;
            double montantTotal = 0;
            BigDecimal dwa;
            for (OrderDetails p : details) {
                if(p.getDouane() != null) {
                    dwa=p.getDouane();
                }else{
                    dwa= BigDecimal.valueOf(0);
                }
                table.addCell(cell(valueOrNA(p.getTracking()), 5, fontPlain));
                table.addCell(cell(valueOrNA(p.getUpc()), 5, fontPlain));
                table.addCell(cell(p.getCategory().getDescription(), 5, fontPlain));
                table.addCell(cell(formatPoundsOrQuantity(p), 5, fontPlain));
                table.addCell(cell(String.format("%.2f", p.getPrice()), 5, fontPlain));
                table.addCell(cell(String.format("%.2f", dwa), 5, fontPlain));
                table.addCell(cell(String.format("%.2f", p.getSubtotal()), 5, fontPlain));
                table.addCell(cell(p.getCreatedAt() == null ? "N/A" : p.getCreatedAt().format(FRENCH_DATE_TIME_FORMATTER), 5, fontPlain));
                table.addCell(cell(valueOrNA(p.getNote()), 5, fontPlain));
                pound = pound + p.getPounds();
                montantTotal=montantTotal+p.getSubtotal();
            }

            doc.add(table);
            doc.add(new Paragraph(" "));
            // --------------------------------------------------------
            // TOTAL FINAL
            // --------------------------------------------------------
            Table totalTable = new Table(UnitValue.createPointArray(new float[]{135, 10, 215}));
            totalTable.setWidth(UnitValue.createPointValue(360));
            totalTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
            //totalTable.setFixedLayout();
            double to=tauxRepository.findByDevise("Dollars US").getSale();
            totalTable.addCell(noBorders("Poids Total", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(DecimalFormat.round2(pound) + " lbs", true).setFontSize(7));
            totalTable.addCell(noBorders("Montant(US)", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(String.format("%.2f",montantTotal) + " $US", true).setFontSize(7));
            totalTable.addCell(noBorders("Frais d'assurance", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(orderDetails.getCitypoundfee().getInsurance().getAmount()+ " $US", true).setFontSize(7));
            totalTable.addCell(noBorders("Montant Total(US)", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(String.format("%.2f",(montantTotal+orderDetails.getCitypoundfee().getInsurance().getAmount())) + " $US", true).setFontSize(7));
            totalTable.addCell(noBorders("Taux de change", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(to + " GDES pour 1 $US", true).setFontSize(7));
            totalTable.addCell(noBorders("Montant Total(GDES)", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(String.format("%.2f",((montantTotal+orderDetails.getCitypoundfee().getInsurance().getAmount())*to)) + " GDES", true).setFontSize(7));

            String Agent="N/A";
            if(orderDetails.getUser() != null && orderDetails.getUser().getUsercode() != null && !orderDetails.getUser().getUsercode().isEmpty()){
                Agent=cleanVelogCode(orderDetails.getUser().getUsercode());
            }
            totalTable.addCell(noBorders("Agent", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(Agent, true).setFontSize(7));

            totalTable.setMargin(5);
            doc.add(totalTable);
            doc.add(new  Paragraph("\n"));

            Table paymentTable = new Table(UnitValue.createPercentArray(new float[]{20, 4, 76}));
            paymentTable.setWidth(UnitValue.createPercentValue(100));
            paymentTable.setFixedLayout();

// Antèt
            paymentTable.addCell(
                    new Cell(1, 3)
                            .add(new Paragraph("METHODE DE PAIEMENT").setFontSize(7))
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBorder(Border.NO_BORDER)
            );

// Zelle
            paymentTable.addCell(noBorders("Zelle", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("786 928 1241", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(
                    noBorders("SAINT DELIVRANCE MULTI-SERVICES, LLC", false)
                            .setFontSize(7)
            );

// MonCash
            paymentTable.addCell(noBorders("MonCash", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("+509 3712-9095", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));

// NatCash
            paymentTable.addCell(noBorders("NatCash", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("+509 4005-6080", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));

            // UNIBANK US
            paymentTable.addCell(noBorders("UNIBANK USD", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("560-1522-1856141", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));
            // UNIBANK GDES
            paymentTable.addCell(noBorders("UNIBANK GDES", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("560-1521-1856133", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));

            // BUH USD
            paymentTable.addCell(noBorders("BUH USD", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("12000099064", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));
            // BUH GDES
            paymentTable.addCell(noBorders("BUH GDES", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("12000099056", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));

            paymentTable.setMargin(5);
            doc.add(paymentTable);
            // --------------------------------------------------------
            // FOOTER
            // --------------------------------------------------------
            Table qrTable = new Table(new float[]{111, 90});
            qrTable.setWidth(CONTENT_WIDTH);
            qrTable.setBorder(Border.NO_BORDER);

// QR Images
            Image order = new Image(generateQrCode(orderDetails.getShip().getShiporder()))
                    .setHorizontalAlignment(HorizontalAlignment.LEFT)
                    .setWidth(50)
                    .setHeight(50);

            Image client = new Image(generateQrCode(orderDetails.getRec_name()))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setWidth(50)
                    .setHeight(50);

// ✅ Cells SANS border
            qrTable.addCell(new Cell().add(order).setBorder(Border.NO_BORDER));
            qrTable.addCell(new Cell().add(client).setBorder(Border.NO_BORDER));

            doc.add(new Paragraph("").setMarginBottom(6));
            doc.add(qrTable);

            doc.add(new Paragraph("").setMarginBottom(6));
            Image code = new Image(generateBarCode(orderDetails.getShip().getShiporder())).setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(code);

            doc.add(new Paragraph("")).setBottomMargin(5);

            doc.add(new Paragraph("Attention cher(e) client(e):").setTextAlignment(TextAlignment.LEFT).setFontSize(5).setFont(fontPlain));
            doc.add(new Paragraph("Le montant indiqué sur cette facture est temporaire, c’est-à-dire que les "
                    +DecimalFormat.round2((montantTotal+orderDetails.getCitypoundfee().getInsurance().getAmount()))
                    +" $US à régler sont susceptibles de changer si l’un des colis " +
                    "nécessite un dédouanement. Dans ce cas, des frais de douane " +
                    "supplémentaires seront ajoutés à la facture générale.").setFont(fontPlain).setFontSize(5).setTextAlignment(TextAlignment.JUSTIFIED));

            doc.close();

            r2Service.upload(out.toByteArray(), objectKey, "application/pdf");
            return r2Service.publicUrl(objectKey);
        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF", e);
        }
    }

    @Override
    public String create80PdfMoves(OrderDetails orderDetails,String path) {
        try{
            String objectKey="products/"+orderDetails.getRec_phone()+"_"+orderDetails.getShip().getShiporder()+".pdf";
            float MARGIN = 36f;
            float CONTENT_WIDTH = PageSize.LETTER.getWidth() - (MARGIN * 2);


            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);

            Document doc = new Document(pdf, PageSize.LETTER);
            doc.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
            // 🔒 TOP SAFE ZONE POUR 80MM (OBLIGATOIRE)
            doc.add(
                    new Table(1)
                            .setWidth(UnitValue.createPointValue(CONTENT_WIDTH))
                            .addCell(
                                    new Cell()
                                            .setBorder(Border.NO_BORDER)
                                            .setMinHeight(25) // ≈ 8–9 mm
                            )
            );

            PdfFont fontBold = null;
            PdfFont fontPlain = null;
            try {
                fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                fontPlain = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            // --------------------------------------------------------
            // BUSINESS INFO
            // --------------------------------------------------------
            Table header = new Table(UnitValue.createPointArray(new float[]{CONTENT_WIDTH}));
            header.setWidth(UnitValue.createPointValue(CONTENT_WIDTH));
            header.setBorder(Border.NO_BORDER);

            ClassPathResource resource = new ClassPathResource(Variables.logoPath);
            ImageData imageData = ImageDataFactory.create(resource.getPath());
            Image image = new Image(imageData)
                    .setWidth(90)
                    .setHeight(55)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            header.addCell(
                    new Cell()
                            .setBorder(Border.NO_BORDER)
                            .setPadding(0)
                            .setMargin(0)
                            .setTextAlignment(TextAlignment.CENTER)
                            .add(image)
            );
            Mainaddress address = new Mainaddress();
            Mainaddress addr = null;
            Optional<Mainaddress> mainaddress = Optional.of(address);
            mainaddress = mainaddressRepository.findById(1L);
            if (mainaddress.isPresent()) {
                addr = mainaddress.get();
            }
            assert addr != null;

            header.addCell(
                    new Cell()
                            .setBorder(Border.NO_BORDER)
                            .setPadding(0)
                            .setMargin(0)
                            .add(
                                    new Paragraph(addr.getAddressline() + ", " +
                                            addr.getCity() + ", " +
                                            addr.getState() + " " +
                                            addr.getZipcode())
                                            .setFontSize(8).setFont(fontBold)
                                            .setTextAlignment(TextAlignment.CENTER)
                            )
            );


            header.addCell(
                    noBorder("info@velogxpress.com", true)
                            .setFontSize(8)
                            .setTextAlignment(TextAlignment.CENTER)
            );

            header.addCell(
                    noBorder(addr.getPhone(), true)
                            .setFontSize(8)
                            .setTextAlignment(TextAlignment.CENTER)
            );

            header.addCell(
                    noBorder("Lundi - Samedi : 9h00 AM - 5h00 PM", true)
                            .setFontSize(8)
                            .setTextAlignment(TextAlignment.CENTER)
            );
            header.setMarginBottom(0);
            doc.add(header);
            doc.add(new Paragraph("____________________________").setFont(fontBold)
                    .setMargin(2)
                    .setMultipliedLeading(0.8f)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("FACTURE TEMPORAIRE").setFont(fontBold).setFontSize(7).setMarginBottom(0)
                    .setMultipliedLeading(0.8f).setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("____________________________").setFont(fontBold)
                    .setMargin(2)
                    .setMultipliedLeading(0.8f)
                    .setTextAlignment(TextAlignment.CENTER));


            // --------------------------------------------------------
            // INVOICE INFO
            // --------------------------------------------------------
            List<OrderDetails> details = orderDetailsRepository.findOrderDetails(orderDetails.getShip().getId(), orderDetails.getRec_name(), orderDetails.getRec_phone());
            Table infoTable = new Table(UnitValue.createPointArray(new float[]{95, 10, 255}));
            infoTable.setWidth(UnitValue.createPointValue(360));
            infoTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
            //infoTable.setFixedLayout();
            String destination="N/A";
            String det=orderDetails.getCitypoundfee().getCity().getDescription();
            if(det!=null) {
                destination=det;
            }

            infoTable.addCell(noBorders("Order ID", true)).setFontSize(7);
            infoTable.addCell(noBorders(":", true)).setFontSize(7);
            infoTable.addCell(noBorders(orderDetails.getShip().getShiporder(), false)).setFontSize(7);
            infoTable.addCell(noBorders("Client", true)).setFontSize(7);
            infoTable.addCell(noBorders(":", true)).setFontSize(7);
            infoTable.addCell(noBorders(orderDetails.getRec_name(), false)).setFontSize(7);
            infoTable.addCell(noBorders("Telephone", true)).setFontSize(7);
            infoTable.addCell(noBorders(":", true)).setFontSize(7);
            infoTable.addCell(noBorders(orderDetails.getRec_phone(), false)).setFontSize(7);
            infoTable.addCell(noBorders("Destination", true)).setFontSize(7);
            infoTable.addCell(noBorders(":", true)).setFontSize(7);
            infoTable.addCell(noBorders(destination, false)).setFontSize(7);

            infoTable.setMargin(2);

            doc.add(infoTable);

            doc.add(new Paragraph(" "));

            // --------------------------------------------------------
            // PRODUCTS TABLE
            // --------------------------------------------------------
            Table table = new Table(UnitValue.createPercentArray(
                    new float[]{15, 12, 10, 8, 8, 9, 9, 11, 18}
            ));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setFixedLayout();
            table.setKeepTogether(false);
            table.setMarginLeft(0);
            table.setMarginRight(0);



            table.addCell(cell("TRACKING", 5, fontBold));
            table.addCell(cell("VX TRACK", 5, fontBold));
            table.addCell(cell("CATEG.", 5, fontBold));
            boolean hasElectronic = details.stream().anyMatch(this::isElectronicCategory);
            boolean hasWeightedColis = details.stream().anyMatch(detail -> !isElectronicCategory(detail));
            String quantityHeader = hasElectronic
                    ? (hasWeightedColis ? "LBS/UNITÉ" : "UNITÉ")
                    : "LBS.";
            table.addCell(cell(quantityHeader, 5, fontBold));
            table.addCell(cell("PRIX", 5, fontBold));
            table.addCell(cell("DOU/HAZ", 5, fontBold));
            table.addCell(cell("TOTAL", 5, fontBold));
            table.addCell(cell("DATE", 5, fontBold));
            table.addCell(cell("NOTE", 5, fontBold));


            double pound = 0;
            double montantTotal = 0;
            BigDecimal dwa;
            for (OrderDetails p : details) {
                if(p.getDouane() != null) {
                    dwa=p.getDouane();
                }else{
                    dwa= BigDecimal.valueOf(0);
                }
                table.addCell(cell(valueOrNA(p.getTracking()), 5, fontPlain));
                table.addCell(cell(valueOrNA(p.getUpc()), 5, fontPlain));
                table.addCell(cell(p.getCategory().getDescription(), 5, fontPlain));
                table.addCell(cell(formatPoundsOrQuantity(p), 5, fontPlain));
                table.addCell(cell(String.format("%.2f", p.getPrice()), 5, fontPlain));
                table.addCell(cell(String.format("%.2f", dwa), 5, fontPlain));
                table.addCell(cell(String.format("%.2f", p.getSubtotal()), 5, fontPlain));
                table.addCell(cell(p.getCreatedAt() == null ? "N/A" : p.getCreatedAt().format(FRENCH_DATE_TIME_FORMATTER), 5, fontPlain));
                table.addCell(cell(valueOrNA(p.getNote()), 5, fontPlain));
                pound = pound + p.getPounds();
                montantTotal=montantTotal+p.getSubtotal();
            }

            doc.add(table);
            doc.add(new Paragraph(" "));
            // --------------------------------------------------------
            // TOTAL FINAL
            // --------------------------------------------------------
            Table totalTable = new Table(UnitValue.createPointArray(new float[]{135, 10, 215}));
            totalTable.setWidth(UnitValue.createPointValue(360));
            totalTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
            //totalTable.setFixedLayout();
            double to=tauxRepository.findByDevise("Dollars US").getSale();
            totalTable.addCell(noBorders("Poids Total", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(DecimalFormat.round2(pound) + " lbs", true).setFontSize(7));
            totalTable.addCell(noBorders("Montant(US)", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(String.format("%.2f",montantTotal) + " $US", true).setFontSize(7));
            totalTable.addCell(noBorders("Frais d'assurance", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(orderDetails.getCitypoundfee().getInsurance().getAmount()+ " $US", true).setFontSize(7));
            totalTable.addCell(noBorders("Montant Total(US)", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(String.format("%.2f",(montantTotal+orderDetails.getCitypoundfee().getInsurance().getAmount())) + " $US", true).setFontSize(7));
            totalTable.addCell(noBorders("Taux de change", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(to + " GDES pour 1 $US", true).setFontSize(7));
            totalTable.addCell(noBorders("Montant Total(GDES)", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(String.format("%.2f",((montantTotal+orderDetails.getCitypoundfee().getInsurance().getAmount())*to)) + " GDES", true).setFontSize(7));

            String Agent="N/A";
            if(orderDetails.getUser() != null && orderDetails.getUser().getUsercode() != null && !orderDetails.getUser().getUsercode().isEmpty()){
                Agent=cleanVelogCode(orderDetails.getUser().getUsercode());
            }
            totalTable.addCell(noBorders("Agent", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(Agent, true).setFontSize(7));

            totalTable.setMargin(5);
            doc.add(totalTable);

            Table paymentTable = new Table(UnitValue.createPercentArray(new float[]{20, 4, 76}));
            paymentTable.setWidth(UnitValue.createPercentValue(100));
            paymentTable.setFixedLayout();

// Antèt
            paymentTable.addCell(
                    new Cell(1, 3)
                            .add(new Paragraph("METHODE DE PAIEMENT").setFontSize(7))
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBorder(Border.NO_BORDER)
            );

// Zelle
            paymentTable.addCell(noBorders("Zelle", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("786 928 1241", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(
                    noBorders("SAINT DELIVRANCE MULTI-SERVICES, LLC", false)
                            .setFontSize(7)
            );

// MonCash
            paymentTable.addCell(noBorders("MonCash", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("+509 3712-9095", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));

// NatCash
            paymentTable.addCell(noBorders("NatCash", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("+509 4005-6080", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));
            // UNIBANK US
            paymentTable.addCell(noBorders("UNIBANK USD", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("560-1522-1856141", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));
            // UNIBANK GDES
            paymentTable.addCell(noBorders("UNIBANK GDES", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("560-1521-1856133", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));

            // BUH USD
            paymentTable.addCell(noBorders("BUH USD", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("12000099064", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));
            // BUH GDES
            paymentTable.addCell(noBorders("BUH GDES", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("12000099056", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));

            paymentTable.setMargin(5);
            doc.add(paymentTable);
            // --------------------------------------------------------
            // FOOTER
            // --------------------------------------------------------
            Table qrTable = new Table(new float[]{111, 90});
            qrTable.setWidth(CONTENT_WIDTH);
            qrTable.setBorder(Border.NO_BORDER);

// QR Images
            Image order = new Image(generateQrCode(orderDetails.getShip().getShiporder()))
                    .setHorizontalAlignment(HorizontalAlignment.LEFT)
                    .setWidth(50)
                    .setHeight(50);

            Image client = new Image(generateQrCode(orderDetails.getRec_name()))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setWidth(50)
                    .setHeight(50);

// ✅ Cells SANS border
            qrTable.addCell(new Cell().add(order).setBorder(Border.NO_BORDER));
            qrTable.addCell(new Cell().add(client).setBorder(Border.NO_BORDER));

            doc.add(new Paragraph("").setMarginBottom(6));
            doc.add(qrTable);

            doc.add(new Paragraph("").setMarginBottom(6));
            Image code = new Image(generateBarCode(orderDetails.getShip().getShiporder())).setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(code);

            doc.add(new Paragraph("")).setBottomMargin(5);

            doc.add(new Paragraph("Attention cher(e) client(e):").setTextAlignment(TextAlignment.LEFT).setFontSize(5).setFont(fontPlain));
            doc.add(new Paragraph("Le montant indiqué sur cette facture est temporaire, c’est-à-dire que les "
                    +DecimalFormat.round2((montantTotal+orderDetails.getCitypoundfee().getInsurance().getAmount()))
                    +" $US à régler sont susceptibles de changer si l’un des colis " +
                    "nécessite un dédouanement. Dans ce cas, des frais de douane " +
                    "supplémentaires seront ajoutés à la facture générale.").setFont(fontPlain).setFontSize(5).setTextAlignment(TextAlignment.JUSTIFIED));

            doc.close();

            r2Service.upload(out.toByteArray(), objectKey, "application/pdf");
            return r2Service.publicUrl(objectKey);
        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF", e);
        }
    }

    @Override
    public byte[] amnistyDownload(String name, String telephone, String tracking) {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
            float TICKET_WIDTH = 226f;   // 80mm
            float MARGIN = 3f;
            float CONTENT_WIDTH = TICKET_WIDTH - (MARGIN * 2); // 216 pt


            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);

            // Largeur 80mm = 226 pts / Hauteur 600 pts
            PageSize ticketSize = new PageSize(TICKET_WIDTH, 2000);
            Document doc = new Document(pdf, ticketSize);
            doc.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
            // 🔒 TOP SAFE ZONE POUR 80MM (OBLIGATOIRE)
            doc.add(
                    new Table(1)
                            .setWidth(UnitValue.createPointValue(CONTENT_WIDTH))
                            .addCell(
                                    new Cell()
                                            .setBorder(Border.NO_BORDER)
                                            .setMinHeight(25) // ≈ 8–9 mm
                            )
            );

            PdfFont fontBold = null;
            PdfFont fontPlain = null;
            try {
                fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                fontPlain = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            // --------------------------------------------------------
            // BUSINESS INFO
            // --------------------------------------------------------
            Table header = new Table(UnitValue.createPointArray(new float[]{216}));
            header.setWidth(UnitValue.createPointValue(CONTENT_WIDTH));
            header.setBorder(Border.NO_BORDER);

            ClassPathResource resource = new ClassPathResource(Variables.logoPath);
            ImageData imageData = ImageDataFactory.create(resource.getPath());
            Image image = new Image(imageData)
                    .setWidth(90)
                    .setHeight(55)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            header.addCell(
                    new Cell()
                            .setBorder(Border.NO_BORDER)
                            .setPadding(0)
                            .setMargin(0)
                            .setTextAlignment(TextAlignment.CENTER)
                            .add(image)
            );
            Mainaddress address = new Mainaddress();
            Mainaddress addr = null;
            Optional<Mainaddress> mainaddress = Optional.of(address);
            mainaddress = mainaddressRepository.findById(1L);
            if (mainaddress.isPresent()) {
                addr = mainaddress.get();
            }
            assert addr != null;

            header.addCell(
                    new Cell()
                            .setBorder(Border.NO_BORDER)
                            .setPadding(0)
                            .setMargin(0)
                            .add(
                                    new Paragraph(addr.getAddressline() + ", " +
                                            addr.getCity() + ", " +
                                            addr.getState() + " " +
                                            addr.getZipcode())
                                            .setFontSize(8).setFont(fontBold)
                                            .setTextAlignment(TextAlignment.CENTER)
                            )
            );


            header.addCell(
                    noBorder("info@velogxpress.com", true)
                            .setFontSize(8)
                            .setTextAlignment(TextAlignment.CENTER)
            );

            header.addCell(
                    noBorder(addr.getPhone(), true)
                            .setFontSize(8)
                            .setTextAlignment(TextAlignment.CENTER)
            );

            header.addCell(
                    noBorder("Lundi - Samedi : 9h00 AM - 5h00 PM", true)
                            .setFontSize(8)
                            .setTextAlignment(TextAlignment.CENTER)
            );
            header.setMarginBottom(0);
            doc.add(header);
            doc.add(new Paragraph("____________________________").setFont(fontBold)
                    .setMargin(2)
                    .setMultipliedLeading(0.8f)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("INVOICE").setFont(fontBold).setFontSize(7).setMarginBottom(0)
                    .setMultipliedLeading(0.8f).setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("____________________________").setFont(fontBold)
                    .setMargin(2)
                    .setMultipliedLeading(0.8f)
                    .setTextAlignment(TextAlignment.CENTER));


            // --------------------------------------------------------
            // INVOICE INFO
            // --------------------------------------------------------
            List<Amnisty> details = amnistyRepository.findAllAmnistyByClient(name,telephone);
            if (details.isEmpty() && tracking != null && !tracking.isBlank()) {
                Amnisty amnisty = amnistyRepository.findByTracking(tracking);
                if (amnisty != null) {
                    details = List.of(amnisty);
                }
            }

            if (details.isEmpty()) {
                return new byte[0];
            }
            Amnisty firstDetail = details.get(0);
            Table infoTable = new Table(new float[]{90, 5, 121});
            infoTable.setWidth(CONTENT_WIDTH);
            //infoTable.setFixedLayout();
            String destination = safe(() -> firstDetail.getCitypoundfee().getCity().getDescription());
            double insuranceAmount = 0;
            String insurance = safe(() -> firstDetail.getCitypoundfee().getInsurance().getAmount());
            if (!"N/A".equals(insurance)) {
                try {
                    insuranceAmount = Double.parseDouble(insurance);
                } catch (NumberFormatException ignored) {
                    insuranceAmount = 0;
                }
            }

            infoTable.addCell(noBorders("Client", true)).setFontSize(7);
            infoTable.addCell(noBorders(":", true)).setFontSize(7);
            infoTable.addCell(noBorders(valueOrNA(details.get(0).getName()), false)).setFontSize(7);
            infoTable.addCell(noBorders("Telephone", true)).setFontSize(7);
            infoTable.addCell(noBorders(":", true)).setFontSize(7);
            infoTable.addCell(noBorders(valueOrNA(details.get(0).getTelephone()), false)).setFontSize(7);
            infoTable.addCell(noBorders("Destination", true)).setFontSize(7);
            infoTable.addCell(noBorders(":", true)).setFontSize(7);
            infoTable.addCell(noBorders(valueOrNA(destination), false)).setFontSize(7);

            infoTable.setMargin(2);

            doc.add(infoTable);

            doc.add(new Paragraph(" "));

            // --------------------------------------------------------
            // PRODUCTS TABLE
            // --------------------------------------------------------
            Table table = new Table(new float[]{58, 37,25, 30, 30, 36});
            table.setWidth(216);
            table.setFixedLayout();
            table.setKeepTogether(false);
            table.setMarginLeft(0);
            table.setMarginRight(0);



            table.addCell(cell("TRACKING", 5, fontBold));
            table.addCell(cell("CATEG.", 5, fontBold));
            table.addCell(cell("LBS.", 5, fontBold));
            table.addCell(cell("PRIX", 5, fontBold));
            table.addCell(cell("DOU/HAZ", 5, fontBold));
            table.addCell(cell("TOTAL", 5, fontBold));


            double pound = 0;
            BigDecimal montantTotal = BigDecimal.valueOf(0);
            BigDecimal sousTotal = BigDecimal.valueOf(0);
            BigDecimal dwa;
            for (Amnisty p : details) {
                if(p.getDouane() != null) {
                    dwa=p.getDouane();
                    sousTotal=p.getPrice().add(p.getDouane());
                }else{
                    dwa= BigDecimal.valueOf(0);
                    sousTotal=p.getPrice();
                }
                table.addCell(cell(p.getTracking(), 5, fontPlain));
                table.addCell(cell(p.getCategory().getDescription(), 5, fontPlain));
                table.addCell(cell(String.format("%.2f", p.getPounds()), 5, fontPlain));
                table.addCell(cell(String.format("%.2f", p.getPrice()), 5, fontPlain));
                table.addCell(cell(String.format("%.2f", dwa), 5, fontPlain));

                table.addCell(cell(String.format("%.2f", (sousTotal)), 5, fontPlain));
                pound = pound + p.getPounds();
                montantTotal=montantTotal.add(sousTotal);
            }

            doc.add(table);
            doc.add(new Paragraph(" "));
            // --------------------------------------------------------
            // TOTAL FINAL
            // --------------------------------------------------------
            Table totalTable = new Table(new float[]{90, 5, 121});
            totalTable.setWidth(CONTENT_WIDTH);
            //totalTable.setFixedLayout();
            double to=tauxRepository.findByDevise("Dollars US").getSale();
            totalTable.addCell(noBorders("Poids Total", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(DecimalFormat.round2(pound) + " lbs", true).setFontSize(7));
            totalTable.addCell(noBorders("Montant(US)", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(String.format("%.2f",montantTotal) + " $US", true).setFontSize(7));
            totalTable.addCell(noBorders("Frais d'assurance", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(insuranceAmount + " $US", true).setFontSize(7));
            totalTable.addCell(noBorders("Montant Total(US)", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(String.format("%.2f",(montantTotal.add(BigDecimal.valueOf(insuranceAmount)))) + " $US", true).setFontSize(7));
            totalTable.addCell(noBorders("Taux de change", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(to + " $HT pour 1 $US", true).setFontSize(7));
            totalTable.addCell(noBorders("Montant Total($HT)", true).setFontSize(7));
            totalTable.addCell(noBorders(":", true).setFontSize(7));
            totalTable.addCell(noBorders(String.format("%.2f",((montantTotal.add(BigDecimal.valueOf(insuranceAmount))).multiply(BigDecimal.valueOf(to)))) + " $HT", true).setFontSize(7));

            totalTable.setMargin(5);
            doc.add(totalTable);

            Table paymentTable = new Table(new float[]{45, 5, 110});
            paymentTable.setWidth(CONTENT_WIDTH);

// Antèt
            paymentTable.addCell(
                    new Cell(1, 3)
                            .add(new Paragraph("METHODE DE PAIEMENT").setFontSize(7))
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBorder(Border.NO_BORDER)
            );

// Zelle
            paymentTable.addCell(noBorders("Zelle", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("786 928 1241", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(
                    noBorders("SAINT DELIVRANCE MULTI-SERVICES, LLC", false)
                            .setFontSize(7)
            );

// MonCash
            paymentTable.addCell(noBorders("MonCash", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("+509 3712-9095", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));

// NatCash
            paymentTable.addCell(noBorders("NatCash", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("+509 4005-6080", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));
            // UNIBANK US
            paymentTable.addCell(noBorders("UNIBANK USD", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("560-1522-1856141", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));
            // UNIBANK GDES
            paymentTable.addCell(noBorders("UNIBANK GDES", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("560-1521-1856133", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));

            // BUH USD
            paymentTable.addCell(noBorders("BUH USD", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("12000099064", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));
            // BUH GDES
            paymentTable.addCell(noBorders("BUH GDES", true).setFontSize(7));
            paymentTable.addCell(noBorders(":", true).setFontSize(7));
            paymentTable.addCell(noBorders("12000099056", true).setFontSize(7));

            paymentTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
            paymentTable.addCell(noBorders("VELOG XPRESS", false).setFontSize(7));

            paymentTable.setMargin(5);
            doc.add(paymentTable);
            // --------------------------------------------------------
            // FOOTER
            // --------------------------------------------------------
            Table qrTable = new Table(new float[]{111, 90});
            qrTable.setWidth(CONTENT_WIDTH);
            qrTable.setBorder(Border.NO_BORDER);

// QR Images
            Image order = new Image(generateQrCode(details.get(0).getName()))
                    .setHorizontalAlignment(HorizontalAlignment.LEFT)
                    .setWidth(50)
                    .setHeight(50);

            Image client = new Image(generateQrCode(details.get(0).getTelephone()))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setWidth(50)
                    .setHeight(50);

// ✅ Cells SANS border
            qrTable.addCell(new Cell().add(order).setBorder(Border.NO_BORDER));
            qrTable.addCell(new Cell().add(client).setBorder(Border.NO_BORDER));

            doc.add(new Paragraph("").setMarginBottom(6));
            doc.add(qrTable);

            doc.add(new Paragraph("").setMarginBottom(6));
            Image code = new Image(generateBarCode(details.get(0).getTracking())).setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(code);

            doc.add(new Paragraph("")).setBottomMargin(5);

            doc.add(new Paragraph("Attention cher(e) client(e):").setTextAlignment(TextAlignment.LEFT).setFontSize(7).setFont(fontPlain).setMargin(5));
            doc.add(new Paragraph("Veuillez noter que le traitement de votre colis ne pourra pas être effectué tant que cette facture " +
                    "n’aura pas été réglée. Merci de procéder au paiement afin d’éviter tout retard dans le processus.").setFont(fontPlain).setFontSize(7).setTextAlignment(TextAlignment.JUSTIFIED).setMargin(5));

            doc.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF", e);
        }
    }

    @Override
    public byte[] ClientDownloadA4(String param) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // 1 pouce = 25.4 mm, 1 pouce = 72 points
            float mmToPt = 72f / 25.4f;
            float width = 215.9f * mmToPt;  // 215.9 mm ≈ 8.5 pouces
            float height = 279.4f * mmToPt; // 279.4 mm ≈ 11 pouces

            PageSize pageSize = new PageSize(width, height);

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, pageSize);
            document.setMargins(20, 10, 20, 10);
            int increase=0;
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            PdfFont fontPlain = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);


            Table table = new Table(UnitValue.createPercentArray(new float[]{12f,12f,21f,11f,21f,10f,12f})).useAllAvailableWidth();

            table.addCell(new Paragraph("#").setHorizontalAlignment(HorizontalAlignment.CENTER).setFont(fontBold));
            table.addCell(new Paragraph("UPC Colis").setFont(fontBold));
            table.addCell(new Paragraph("No Traçage").setFont(fontBold));
            table.addCell(new Paragraph("Destinat.").setFont(fontBold));
            table.addCell(new Paragraph("Categorie").setFont(fontBold));
            table.addCell(new Paragraph("Poids").setHorizontalAlignment(HorizontalAlignment.CENTER).setFont(fontBold));
            table.addCell(new Paragraph("Status").setHorizontalAlignment(HorizontalAlignment.CENTER).setFont(fontBold));
            List<OrderDetails> detailLis=orderDetailsRepository.findAllByParamForDownload(param);
            for(OrderDetails details:detailLis){

                table.addCell(new Paragraph(valueOrNA(details.getShip().getShiporder())).setFont(fontPlain).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.CENTER));
                table.addCell(new Paragraph(valueOrNA(details.getUpc())).setFont(fontPlain).setFontSize(8));
                table.addCell(new Paragraph(valueOrNA(details.getTracking())).setFont(fontPlain).setFontSize(8));
                table.addCell(new Paragraph(valueOrNA(details.getCitypoundfee().getCity().getDescription())).setFont(fontPlain).setFontSize(8));
                table.addCell(new Paragraph(valueOrNA(details.getCategory().getDescription())).setFont(fontPlain).setFontSize(8));
                table.addCell(new Paragraph(numberOrNA(details.getPounds()," lbs")).setFont(fontPlain).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.CENTER));
                table.addCell(new Paragraph(valueOrNA(details.getStatus())).setFont(fontPlain).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.CENTER));
                increase++;
            }

            ClassPathResource resource = new ClassPathResource(Variables.logoPath);
            ImageData imageData = ImageDataFactory.create(resource.getPath());
            Image image = new Image(imageData).scaleToFit(100, 50).setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(image);

            Mainaddress address = new Mainaddress();
            Mainaddress addr = null;
            Optional<Mainaddress> mainaddress = Optional.of(address);
            mainaddress=mainaddressRepository.findById(1L);
            if (mainaddress.isPresent()) {
                addr = mainaddress.get();
            }
            assert addr != null;
            document.add(new Paragraph(addr.getAddressline()+", "+addr.getCity()+", "+addr.getState()+" "+addr.getZipcode()).setFont(fontBold).setFontSize(10).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(addr.getPhone()+" | info@velogxpress.com").setFont(fontBold).setFontSize(10).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("___________________________________________________________________").setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("RAPPORT DES COLIS DES CLIENTS").setFont(fontBold).setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("___________________________________________________________________").setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(""));

            Table tableInfo = new Table(UnitValue.createPercentArray(new float[]{2,1,7})).useAllAvailableWidth();
            tableInfo.addCell(noBorderCell("Client",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(valueOrNA(detailLis.get(0).getRec_name()),9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("Telephone",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(valueOrNA(detailLis.get(0).getExp_phone()),9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("Email",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderValueCell(valueOrNA(detailLis.get(0).getRec_email()),9)).setTextAlignment(TextAlignment.LEFT);
            tableInfo.addCell(noBorderCell("Quantité Colis",true,9));
            tableInfo.addCell(noBorderCell(":",true,9));
            tableInfo.addCell(noBorderCell(increase+" Colis",false,9));
            document.add(tableInfo);
            document.add(new Paragraph(" "));

            Image qr = new Image(generateBarCode(valueOrNA(detailLis.get(0).getExp_name())+"-"+valueOrNA(detailLis.get(0).getRec_name()))).setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(qr).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);

            document.add(new Paragraph(" "));
            document.add(table);
            document.add(new Paragraph(" "));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF", e);
        }
    }


}
