package cc.abstra.pasilla.imageutils;

import java.awt.color.ColorSpace;

/**
 *+
 * @author nando
 */
public class Consts {

    public static final String IMAGEIO_API_VERSION = "javax_imageio_1.0";
    public static final int PREVIEW_DPI = 300;
    public static final int MIN_DPI = 72;
    public static final int CORRUPT_DPI = -1;
    public static final double A4_W_INCHES = 8.26771654;
    public static final double A4_H_INCHES = 11.6929134;
    public static final int A4_W_MM = 210;
    public static final int A4_H_MM = 297;
    public static final String PNG = "png";
    public static final String TIF = "tif";
    public static final int FIRST_PAGE = 0;
    public static final double INCH_TO_MM = 25.4;
    public static final double INCH_TO_CM = 2.54;
    public static final double INCH_TO_POINT = 72;
    public static final String LANDSCAPE_KEY = "landscape";
    public static final String IMAGE_KEY = "img";
    public static final String PAGE_SIZE_KEY = "size";
    public static final String PDF_KEY = "pdf";
    public static final String A4 = "A4";
    public static final double CMYK_RGB_GAMMA_CORRECTION = 0.25;  //manually set. no magic here.

    public class PNGMetadata {

        public static final String H_PIXEL_SIZE = "HorizontalPixelSize";
        public static final String V_PIXEL_SIZE = "VerticalPixelSize";
        public static final String DIMENSION = "Dimension";
        public static final String VALUE = "value";
    }

    public class TIFFMetadata {

        public static final String RESOLUTION_UNIT = "ResolutionUnit";
        public static final int RESOLUTION_UNIT_TAG = 296;
        public static final String RESOLUTION_UNIT_NONE = "1";
        public static final String RESOLUTION_UNIT_INCH = "2";
        public static final String RESOLUTION_UNIT_CM = "3";
        public static final String X_RES = "XResolution";
        public static final int X_RES_TAG = 282;
        public static final String Y_RES = "YResolution";
        public static final int Y_RES_TAG = 283;
        public static final String RES_DELIMITER = "/";
    }

    public class PDFMetadata {
        //Tj and TJ are the two operators that display
        //strings in a PDF
        public static final String TEXT_OPERATOR = "tj";
        public static final String REQUIRED_VERSION = "1.4";
    }

    private Consts() {
    }

    public static String getDotsPerMilliAsString(int dpi) {
        return Double.toString(getDotsPerMilli(dpi));
    }

    public static double getDotsPerMilli(int dpi) {
        return 1.0 * dpi / 10 / INCH_TO_CM;
    }

    public static double millisToPixels(int mm, int dpi) {
        return (mm * dpi) / INCH_TO_MM;
    }

    public static double pixelsToMillis(int pixels, int dpi) {
        return (pixels * INCH_TO_MM) / dpi;
    }
}
