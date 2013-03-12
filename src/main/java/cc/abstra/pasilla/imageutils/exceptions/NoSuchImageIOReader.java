package cc.abstra.pasilla.imageutils.exceptions;

/**
 *
 * @author nando
 */
@SuppressWarnings("serial")
public class NoSuchImageIOReader extends RuntimeException {

    public NoSuchImageIOReader() {
    }

    public NoSuchImageIOReader(String msg) {
        super(msg);
    }

}
