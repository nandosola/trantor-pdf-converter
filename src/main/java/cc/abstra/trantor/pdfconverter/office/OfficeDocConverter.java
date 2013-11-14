/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cc.abstra.trantor.pdfconverter.office;

/**
 *
 * @author obs
 */

import cc.abstra.trantor.pdfconverter.services.OpenOfficeService;
import org.artofsolving.jodconverter.OfficeDocumentConverter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class OfficeDocConverter {
    private OfficeDocumentConverter converter;
    private File document;
    
    public OfficeDocConverter(){}

    public void setConverter(OfficeDocumentConverter converter) {
        this.converter = converter;
    }

    public void setDocument(File document) {
        this.document = document;
    }

    public void toPdf(String pdfPath) throws IOException {
        File pdfOutput = new File(pdfPath);
        long startTime = System.currentTimeMillis();
        converter.convert(document, pdfOutput);
        OpenOfficeService.logElapsedTime(startTime, "Converted document to PDF");
    }

    /*public void toPngPreview(String output) throws IOException {
     TO-DO: http://upload.wikimedia.org/wikipedia/commons/8/86/Word_2013_Icon.PNG
    }*/
}
