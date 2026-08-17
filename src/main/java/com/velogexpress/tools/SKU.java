package com.velogexpress.tools;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SKU {
    public String CURRENTSHIPDATE(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMyy");
        Calendar cal = Calendar.getInstance();
        String fch = sdf.format(cal.getTime());
        return fch;
    }

    public String SHIPORDERCODE(){
        String left="";
        String right="";
        for(int i=0;i<=3;i++){
           // left=left+(int)(Math.random()*9 + 1)+"";
            right=right+(int)(Math.random()*9 + 1)+"";
        }
        return CURRENTSHIPDATE()+"VELOG"+right;
    }

    public String COLISCODE(){
        String code="";
        for(int i=0;i<12;i++){
            code=code+(int)(Math.random()*9 + 1)+"";
        }
        return code;
    }
    public String AMNISTYCODE(){
        String code="";
        for(int i=0;i<12;i++){
            code=code+(int)(Math.random()*9 + 1)+"";
        }
        return "AMY"+code;
    }

    public String FACTURECODE(){
        String code="";
        for(int i=0;i<13;i++){
            code=code+(int)(Math.random()*9 + 1)+"";
        }
        return code;
    }

    public void BARCODEGENERATOR(String txt,int width,int length){
        try {
            String path="src\\main\\java\\com\\velogexpress\\QRCode\\"+txt+".png";
            Code128Writer writer=new Code128Writer();
            BitMatrix matrix=writer.encode(txt, BarcodeFormat.CODE_128,width,length);
            MatrixToImageWriter.writeToPath(matrix,"png", Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void QRCODEGENERATOR(String txt,int width,int length){
        try {
            String path="src\\main\\java\\com\\velogexpress\\QRCode\\"+txt+".png";
            QRCodeWriter writer=new QRCodeWriter();
            BitMatrix matrix=writer.encode(txt,BarcodeFormat.QR_CODE,width,length);
            MatrixToImageWriter.writeToPath(matrix,"png", Paths.get(path));
        } catch (IOException | WriterException e) {
            throw new RuntimeException(e);
        }

    }
}
