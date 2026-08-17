package com.velogexpress.print;

import com.velogexpress.entity.Agentsurcursal;
import com.velogexpress.entity.Facture;
import com.velogexpress.entity.OrderDetails;
import com.velogexpress.service.AgentsurcursalService;
import com.velogexpress.tools.Variables;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CreateFacture implements Printable {

    public int print(Graphics g, PageFormat f, int pageIndex){
        Font ft = new Font ("Monospaced",Font.PLAIN, 7);
        Font fts = new Font ("Monospaced",Font.BOLD, 7);
        g.setFont(ft);
        if(pageIndex == 0){
            Facture facture=Variables.getFacture;
            List<OrderDetails> orderDetailsList= Variables.allDetails;
            //variable
            BufferedImage logo = null;
            try {
                logo = ImageIO.read(new File(Variables.LogoPath));
            } catch (IOException e) {

            }
            String address =Variables.getAgent.getSurcursal().getAddress()+", " +
                    Variables.getAgent.getSurcursal().getVille().getDescription()+", "+
                    Variables.getAgent.getSurcursal().getVille().getRegion().getDescription()+", Haiti";
            String phone =Variables.getAgent.getSurcursal().getPhone();
            String horaire =Variables.getAgent.getSurcursal().getHoraire();
            String description = "FACTURE DE LIVRAISON";

            //facture table
            String codefacture = facture.getCode();
            String codefacturePath = Variables.BarQRCodePath+codefacture+".png";
            String date =facture.getDate();
            String kliyan = facture.getClient();
            String order =facture.getShip().getShiporder();
            String orderPtah =Variables.BarQRCodePath+order+".png";
            String user = facture.getUser().getName();
            Double total= facture.getAmount();
            BufferedImage orderCode = null;
            try {
                orderCode = ImageIO.read(new File(orderPtah));
            } catch (IOException e) {

            }
            BufferedImage factureCode = null;
            try {
                factureCode = ImageIO.read(new File(codefacturePath));
            } catch (IOException e) {

            }

            g.drawImage(logo, 105, 5,100,80,null);
            g.setFont(fts);
            g.drawString("Adresse", 10, 90);g.drawString(":", 45, 90);g.drawString(address, 50, 90);
            g.drawString("Tél", 10, 100);g.drawString(":", 45, 100);g.drawString(phone, 50, 100);
            g.drawString("Horaire", 10, 110);g.drawString(":", 45, 110);g.drawString(horaire, 50, 110);
            g.drawString("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", 8, 120);
            g.drawString(description, 120, 130);
            g.drawString("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", 8, 140);

            g.drawString("No", 10, 150);g.drawString(":", 45, 150);g.drawString(codefacture, 50, 150);
            g.drawString("Date", 10, 160);g.drawString(":", 45, 160);g.drawString(date, 50, 160);
            g.drawString("Order", 10, 170);g.drawString(":", 45, 170);g.drawString(order, 50, 170);
            g.drawString("Client", 10, 180);g.drawString(":", 45, 180);g.drawString(kliyan, 50, 180);

            g.drawString("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", 8, 195);
            g.drawString("Colis", 10, 205);
            g.drawString("Catégorie", 80, 205);
            g.drawString("Qté lb(s)", 160, 205);
            g.drawString("Prix/lb", 220, 205);
            g.drawString("Assur.", 265, 205);
            g.drawString("Sous-Total", 305, 205);
            g.drawString("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", 8, 215);
            int row=215;
            Double prix=0.0;
            Double colislbs=0.0;
            Double seguro=0.0;
            for(int i=0;i<orderDetailsList.size();i++){
                String colis=orderDetailsList.get(i).getUpc();
                String categorie=orderDetailsList.get(i).getCategory().getDescription();
                Double qtyLbs=orderDetailsList.get(i).getPounds();
                if(categorie.equals("Normal")){
                    prix=orderDetailsList.get(i).getCitypoundfee().getPounds().getAmount();
                }else{
                    prix=orderDetailsList.get(i).getCitypoundfee().getSpecialfee().getAmount();
                }
                Double assurance=orderDetailsList.get(i).getCitypoundfee().getInsurance().getAmount();
                Double subTotal=orderDetailsList.get(i).getSubtotal();
                colislbs=colislbs+orderDetailsList.get(i).getPounds();
                seguro=seguro+orderDetailsList.get(i).getCitypoundfee().getInsurance().getAmount();
                g.setFont(ft);
                g.drawString(colis, 10, row+10);
                g.drawString(categorie, 80, row+10);
                g.drawString(String.valueOf(qtyLbs), 160, row+10);
                g.drawString(prix+" USD", 220, row+10);
                g.drawString(assurance+" USD", 265, row+10);
                g.drawString(subTotal+" USD", 305, row+10);
                row+=10;
            }
            int ligne=row;
            g.setFont(fts);
            g.drawString("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", 8, ligne+10);
            int j=ligne+20;
            g.drawString("TOTAL LB(S)", 10, j);g.drawString(":", 100, j);g.drawString(colislbs+" lb(s)", 110, j);
            g.drawString("TOTAL ASSURANCE", 10, j+10);g.drawString(":", 100, j+10);g.drawString(seguro+" USD", 110, j+10);
            g.drawString("MONTANT TOTAL", 10, j+20);g.drawString(":", 100, j+20);g.drawString(total+" USD", 110, j+20);
            g.drawImage(orderCode, 10, j+22,60,60,null);
            g.drawImage(factureCode,70, j+29,200,40,null);
            g.drawString("AGENT", 10, j+90);g.drawString(":", 100, j+90);g.drawString(user, 110, j+90);
            g.drawString("Merçi pour votre confiance!", 10, j+110);

            return PAGE_EXISTS;
        }else{
            return NO_SUCH_PAGE;
        }

    }
}
