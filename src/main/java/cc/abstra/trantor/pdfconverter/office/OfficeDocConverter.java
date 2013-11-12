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

import org.artofsolving.jodconverter.OfficeDocumentConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class OfficeDocConverter {
    private OfficeDocumentConverter converter;
    private File document;
    
    public OfficeDocConverter(){}

    public void setConverter(OfficeDocumentConverter converter) {
        this.converter = converter;
    }

    public void setDocument(File document) throws FileNotFoundException {
        if(document.exists()) {
            this.document = document;
        } else {
            throw new FileNotFoundException();
        }
    }
    
    
    public void toPdf(String pdfPath) throws IOException {
        long startTime = System.currentTimeMillis();
        converter.convert(document,  new File(pdfPath));
    }
    
    

  /*public void toPngPreview(String output) throws IOException {
   TO-DO: http://upload.wikimedia.org/wikipedia/commons/8/86/Word_2013_Icon.PNG
  }*/
    
    

}
