package cc.abstra.trantor.pdfconverter.exceptions;

/**
 *
 * @author nando
 */
@SuppressWarnings("serial")
public class InsufficientResolution extends RuntimeException {

    public InsufficientResolution() {
    }

    public InsufficientResolution(String msg) {
        super(msg);
    }

}
