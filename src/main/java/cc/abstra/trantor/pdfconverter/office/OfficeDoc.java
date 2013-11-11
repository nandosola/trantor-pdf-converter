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
import org.artofsolving.jodconverter.document.DocumentFormat;

public abstract class OfficeDoc{

    private File OfficeFile;
    private OfficeDocumentConverter officeConverter;
 
    public OfficeDoc(String OfficeDocPath){
        this.OfficeFile = new File(OfficeDocPath);
        this.officeConverter = new OfficeDocumentConverter(OpenOfficeService.getOfficeManager());
    }
    
    public void toPdf(String pdfPath) throws IOException {
        File pdfOutput = new File(pdfPath);
        long startTime = System.currentTimeMillis();
        officeConverter.convert(OfficeFile, pdfOutput);
    }

  /*public void toPngPreview(String output) throws IOException {
   TO-DO: http://upload.wikimedia.org/wikipedia/commons/8/86/Word_2013_Icon.PNG
  }*/
    
    

}
