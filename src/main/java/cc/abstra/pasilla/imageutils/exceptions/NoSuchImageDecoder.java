package cc.abstra.pasilla.imageutils.exceptions;

/**
 *
 * @author nando
 */
@SuppressWarnings("serial")
public class NoSuchImageDecoder extends RuntimeException {

    public NoSuchImageDecoder() {
    }

    public NoSuchImageDecoder(String msg) {
        super(msg);
    }
}
