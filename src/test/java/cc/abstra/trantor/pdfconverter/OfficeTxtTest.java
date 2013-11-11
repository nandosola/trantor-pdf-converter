/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cc.abstra.trantor.pdfconverter;

import cc.abstra.trantor.pdfconverter.office.FactoryMethodOfficeDoc;
import cc.abstra.trantor.pdfconverter.office.FactoryOfficeDoc;
import cc.abstra.trantor.pdfconverter.office.OfficeDoc;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
/**
 *
 * @author obs
 */
public class OfficeTxtTest {
    FactoryMethodOfficeDoc factory;
    String packagePath;
    @Before
    public void setUp(){
        this.factory = new FactoryOfficeDoc();
        this.packagePath = "class cc.abstra.trantor.pdfconverter.office.";
    }
    
    @Test
    public void testTxtFactoryBuilder(){
        OfficeDoc returnedObject = this.factory.createOfficeDocConversor("rockandroll.txt");
        Assert.assertEquals(this.packagePath+"Txt", returnedObject.getClass().toString());
    }
}