package com.velogexpress.print;
import com.velogexpress.tools.Variables;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.File;
import java.io.IOException;

public class CreateLabel implements Printable {
    public CreateLabel() {

    }
    @Override
    public int print(Graphics g, PageFormat f, int pageIndex) {
        Font ft = new Font("Arial", Font.BOLD, 14);//tip de lèt epi grosè karaktè yo
        g.setFont(ft);

        //variable
        BufferedImage logo = null;
        try {
            logo = ImageIO.read(new File(Variables.LogoPath));
        } catch (IOException e) {

        }
        BufferedImage line = null;
        try {
            line = ImageIO.read(new File(Variables.LinePath));
        } catch (IOException e) {

        }

        String date =Variables.SelectDate;
        String kliyan =Variables.SelectKliyan;
        String kod =Variables.SelectUserCode;
        String adresse =Variables.SelectAdresse;
        String ville =Variables.SelectVille+", "+Variables.SelectRegion;
        String phone =Variables.SelectPhone;
        String email =Variables.SelectEmail;
        String shiporderID =Variables.BarQRCodePath+Variables.shiporderID+".png";
        String colisID=Variables.BarQRCodePath+Variables.colisID+".png";
        BufferedImage orderID = null;
        try {
            orderID = ImageIO.read(new File(shiporderID));
        } catch (IOException e) {

        }
        BufferedImage colis = null;
        try {
            colis = ImageIO.read(new File(colisID));
        } catch (IOException e) {

        }
        if (pageIndex == 0) {
            g.drawImage(logo,10, 5,100,50,null);
            g.setFont(ft);
            g.drawString(date, 200, 38);
            g.setFont(ft);
            g.drawImage(line, 0, 60,6000,3,null);
            g.setFont(ft);
            g.drawImage(orderID,10, 70,70,70,null);
            g.setFont(ft);
            g.drawString(ville, 100, 105);
            g.drawImage(line, 0, 140,6000,3,null);
            g.drawString("CLIENT:", 20, 160);
            //g.drawString(kliyan, 10, 180);
            g.drawString(kod, 20, 180);
            g.drawString(phone, 20, 200);
            g.drawImage(line, 0, 220,6000,3,null);
            g.setFont(ft);
            g.drawImage(colis,10, 240,250,50,null);
            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
    }
}
