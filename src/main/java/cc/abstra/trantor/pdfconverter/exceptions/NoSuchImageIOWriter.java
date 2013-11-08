package cc.abstra.trantor.pdfconverter.exceptions;

/**
 *
 * @author nando
 */
@SuppressWarnings("serial")
public class NoSuchImageIOWriter extends RuntimeException {

    public NoSuchImageIOWriter() {
    }

    public NoSuchImageIOWriter(String msg) {
        super(msg);
    }

}
