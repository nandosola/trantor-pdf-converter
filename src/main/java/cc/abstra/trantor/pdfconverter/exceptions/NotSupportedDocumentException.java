/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.abstra.trantor.pdfconverter.exceptions;

/**
 *
 * @author obs
 */
public class NotSupportedDocumentException extends RuntimeException{
    public NotSupportedDocumentException() {}

    public NotSupportedDocumentException(String msg) {
        super(msg);
    }
}
