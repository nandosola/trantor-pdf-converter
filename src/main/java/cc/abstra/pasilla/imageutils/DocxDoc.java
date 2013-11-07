package cc.abstra.pasilla.imageutils;

/**
 *
 * @author nando
 */

import cc.abstra.pasilla.imageutils.services.OpenOfficeService;
import org.artofsolving.jodconverter.OfficeDocumentConverter;

import java.io.File;
import java.io.IOException;

public class DocxDoc {

    private File docxFile;
    private OfficeDocumentConverter officeConverter;

    public DocxDoc(String docxPath){
        this.docxFile = new File(docxPath);
        this.officeConverter = new OfficeDocumentConverter(OpenOfficeService.getOfficeManager());
    }

    public void toPdf(String pdfPath) throws IOException {
        File pdfOutput = new File(pdfPath);
        long startTime = System.currentTimeMillis();
        officeConverter.convert(docxFile, pdfOutput);
    }

  /*public void toPngPreview(String output) throws IOException {
   TO-DO: http://upload.wikimedia.org/wikipedia/commons/8/86/Word_2013_Icon.PNG
  }*/

}