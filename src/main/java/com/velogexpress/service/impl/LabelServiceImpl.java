package com.velogexpress.service.impl;

import com.velogexpress.print.CreateLabel;
import com.velogexpress.service.LabelService;
import org.springframework.stereotype.Service;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
@Service
public class LabelServiceImpl implements LabelService {
    @Override
    public void printSticker() {
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pf = job.defaultPage();
        Paper paper = new Paper();
        paper.setSize(350, 200);
        double margin = 10;
        paper.setImageableArea(0, 0, 350, 842);
        pf.setPaper(paper);
        pf.setOrientation(PageFormat.PORTRAIT);
        job.setPrintable(new CreateLabel(), pf);
        job.setJobName("Facture");

        try {
            job.print();
        } catch (PrinterException e) {
            System.out.println(e);
        }
    }
}
