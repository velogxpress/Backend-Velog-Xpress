package com.velogexpress.service;

import com.velogexpress.entity.OrderDetails;

public interface PdfService {
    byte[] clientFactureDownload(String usercode,String order);
    byte[] manifestDownload(String usercode,Long city);
    byte[] factureDownload(String facturecode);
    byte[] factureDownloadA4(String facturecode);
    String movefactureDownloadA4(String facturecode);
    byte[] labelDownload(String upc);
    byte[] labelamnistyDownload(String upc);
    byte[] rapportDownload(String upc);
    String create80PdfMove(OrderDetails orderDetails, String path);
    String create80PdfMoves(OrderDetails orderDetails, String path);
    byte[] amnistyDownload(String name,String telephone,String tracking);
    byte[] ClientDownloadA4(String param);
}
