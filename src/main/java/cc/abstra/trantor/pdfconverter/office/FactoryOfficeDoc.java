/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cc.abstra.trantor.pdfconverter.office;

import java.io.File;

/**
 *
 * @author obs
 */
public class FactoryOfficeDoc implements FactoryMethodOfficeDoc{

    @Override
    public OfficeDoc createOfficeDocConversor(String OfficeDocumentPath) {
        String Extension = getOfficeFileExtension(OfficeDocumentPath);

        if(Extension.equalsIgnoreCase("docx")){
            return new Docx(OfficeDocumentPath);
        }else if(Extension.equalsIgnoreCase("doc")){
            return new Doc(OfficeDocumentPath);
        }else if(Extension.equalsIgnoreCase("odt")){
            return new Odt(OfficeDocumentPath);
        }else if(Extension.equalsIgnoreCase("txt")){
            return new Txt(OfficeDocumentPath);
        };

        return null;

    }
  
    private String getOfficeFileExtension(String fileName){
        String extension = "The extension haven't finded in the file received";
        int i = fileName.lastIndexOf('.');
        if(i>0){
          extension = fileName.substring(i+1);
        }
        return extension;
   }
}
