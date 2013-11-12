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
//import cc.abstra.trantor.pdfconverter.exceptions.NoSuchFileException;
import cc.abstra.trantor.pdfconverter.exceptions.NotSupportedDocumentException;
import cc.abstra.trantor.pdfconverter.services.OpenOfficeService;
import org.artofsolving.jodconverter.OfficeDocumentConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OfficeDocConverterFactory{

    private OfficeDocConverterFactory(){}
    
    public static OfficeDocConverter getConverter(String OfficeDocumentPath) 
            throws NotSupportedDocumentException, FileNotFoundException{
        String className = null;
        String extension = getOfficeFileExtension(OfficeDocumentPath);
        File officeFile = new File(OfficeDocumentPath);
        OfficeDocumentConverter officeConverter = new OfficeDocumentConverter(OpenOfficeService.getOfficeManager());
        OfficeDocConverter d;
        try {
            className = extension.substring(0, 1).toUpperCase() + extension.substring(1);
            Class c = Class.forName("cc.abstra.trantor.pdfconverter.office."+className+"Converter");
            d = (OfficeDocConverter) c.newInstance();
            d.setConverter(officeConverter);
            d.setDocument(officeFile);
            return d;
        
        } catch ( InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(OfficeDocConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch(ClassNotFoundException ex){
            throw new NotSupportedDocumentException("Could'nt find converter for extension"+className);
        }
            
        return null;
        

    }
    
    private static String getOfficeFileExtension(String fileName){
        String extension = "The extension haven't finded in the file received";
        int i = fileName.lastIndexOf('.');
        if(i>0){
          extension = fileName.substring(i+1);
        }
        return extension;
   }
}
