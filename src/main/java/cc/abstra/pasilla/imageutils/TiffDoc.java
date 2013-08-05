package cc.abstra.pasilla.imageutils;

/**
 *
 * @author nando
 */

import cc.abstra.pasilla.imageutils.exceptions.*;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.imageio.plugins.tiff.TIFFDirectory;
import com.sun.media.jai.codecimpl.TIFFImageDecoder;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TiffDoc {

    private ImageReader tiffImageReader = null;
    private ImageWriter pngImageWriter = null;
    private File tiffInputFile;
    private int numPages;
    private HashMap<String,Integer> dpiRes = new LinkedHashMap<>();

    static {
        IIORegistry registry = IIORegistry.getDefaultInstance();
        registry.registerServiceProvider(new com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi());
        registry.registerServiceProvider(new com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi());

        if (!getImageReaders().contains(Consts.TIF)) {
            throw new NoSuchImageIOReader("TIFF reader");
        }
        if (!getImageWriters().contains(Consts.TIF)) {
            throw new NoSuchImageIOWriter("TIFF writer");
        }
    }

    public TiffDoc(String tiff) throws IOException, ImageReadException, InsufficientResolution, CorruptMetadata {

        Iterator<ImageReader> readersIterator = ImageIO.getImageReadersByFormatName(Consts.TIF);
        this.tiffImageReader = readersIterator.next();

        Iterator<ImageWriter> writersIterator = ImageIO.getImageWritersByFormatName(Consts.PNG);
        this.pngImageWriter = writersIterator.next();

        this.tiffInputFile = new File(tiff);
        final ImageInfo metadata = Sanselan.getImageInfo(tiffInputFile);
        // For all EXIF tags explained, see: http://topo.math.u-psud.fr/~bousch/exifdump.py

        if(Consts.MIN_DPI > metadata.getPhysicalHeightDpi() ||
                Consts.MIN_DPI > metadata.getPhysicalWidthDpi()){

            if(Consts.CORRUPT_DPI == metadata.getPhysicalHeightDpi() ||
                    Consts.CORRUPT_DPI == metadata.getPhysicalWidthDpi()) {
                Logger.getLogger(TiffDoc.class.getName()).log(Level.WARNING,
                        "EXIF info is corrupt. Trying to read TIFF metadata");
                this.dpiRes = getResolutionFromTIFFMetadata(tiffInputFile);
            } else {
                throw new InsufficientResolution(
                    "This image is unsuitable for storage. The minimum resolution is "+
                    Integer.toString(Consts.MIN_DPI)+". Got: ("+
                    Integer.toString(metadata.getPhysicalWidthDpi())+","+
                    Integer.toString(metadata.getPhysicalHeightDpi())+")");
            }
        }
        this.dpiRes.put(Consts.TIFFMetadata.X_RES, metadata.getPhysicalWidthDpi());
        this.dpiRes.put(Consts.TIFFMetadata.Y_RES, metadata.getPhysicalHeightDpi());

        try {
            this.numPages = getPageCount(tiffInputFile);
        } catch (PagesInImageFileIsNull | NoSuchImageDecoder ex) {
            Logger.getLogger(TiffDoc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void toPngPreview(String output) throws IOException {

        try (ImageOutputStream pngOutputStream = ImageIO.createImageOutputStream(new File(output));
             ImageInputStream tiffInputStream = ImageIO.createImageInputStream(tiffInputFile)) {
            BufferedImage imgFirstPage = getTiffImageReaderForImage(tiffInputStream).read(Consts.FIRST_PAGE, null);

            ImageWriter writer = getPNGImageWriterForImage(Consts.PREVIEW_DPI, pngOutputStream);
            IIOMetadata writerMetadata = getWriterMetadata(writer);
            writer.write(writerMetadata, new IIOImage(ImageHelper.sanitizeImage(imgFirstPage, false), null, writerMetadata),
                    writer.getDefaultWriteParam());
            imgFirstPage.flush();

        }
    }

    public void toPdf(String output) throws IOException, NoSuchImageDecoder {

        List<Map<String, Object>> imgList = new ArrayList<>();

        try (FileSeekableStream tss = new FileSeekableStream(tiffInputFile);
             ImageInputStream tis = ImageIO.createImageInputStream(tiffInputFile)) {

            TIFFImageDecoder dec = new TIFFImageDecoder(tss,null);

            for (int page = Consts.FIRST_PAGE; page < numPages; page++) {
                Map<String, Object> imgInfo = new LinkedHashMap<>();
                Logger.getLogger(TiffDoc.class.getName()).log(Level.INFO, "Page "+(page+1)+" of "+numPages+" read.");
                RenderedImage ri;
                try {
                    ri = dec.decodeAsRenderedImage(page);
                } catch (IOException ex){
                    Logger.getLogger(TiffDoc.class.getName()).log(Level.WARNING,
                        "The image couldn't be read as a FileSeekableStream. Trying with ImageInputStream...");
                    ri = getTiffImageReaderForImage(tis).read(Consts.FIRST_PAGE, null);
                }
                BufferedImage img = ImageHelper.resizeImageToDINA4WithDPI(
                        ImageHelper.sanitizeImage(ri, true), Consts.PREVIEW_DPI, Consts.PREVIEW_DPI,
                        dpiRes.get(Consts.TIFFMetadata.X_RES), dpiRes.get(Consts.TIFFMetadata.Y_RES));

                imgInfo.put(Consts.IMAGE_KEY, img);
                imgInfo.put(Consts.LANDSCAPE_KEY, ImageHelper.hasLandscapeOrientation(img));
                imgInfo.put(Consts.PAGE_SIZE_KEY, PDPage.PAGE_SIZE_A4);  //default preview size

                imgList.add(imgInfo);
                img.flush();
            }
            PdfDoc.writePageListToPdf(imgList, output);
        }
    }

    public static List<String> getImageReaders() {
        return Arrays.asList(ImageIO.getReaderFormatNames());
    }

    public static List<String> getImageWriters() {
        return Arrays.asList(ImageIO.getWriterFormatNames());
    }

    private ImageReader getTiffImageReaderForImage(ImageInputStream iis) throws NoSuchImageIOReader, IOException {
        tiffImageReader.setInput(iis, true, true);
        return tiffImageReader;
    }

    private ImageWriter getPNGImageWriterForImage(int dpi, ImageOutputStream img) throws NoSuchImageIOWriter {
        setDpiPNG(getWriterMetadata(pngImageWriter), dpi);
        pngImageWriter.setOutput(img);
        return pngImageWriter;
    }

    private static IIOMetadata getWriterMetadata(ImageWriter writer) {
        ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_3BYTE_BGR);
        return writer.getDefaultImageMetadata(typeSpecifier, writer.getDefaultWriteParam());
    }

    private static int getPageCount(File file) throws IOException, PagesInImageFileIsNull, NoSuchImageDecoder {
        Integer numPages = null;

        try (FileSeekableStream tiffSeekableStream = new FileSeekableStream(file)) {
            TIFFImageDecoder dec = new TIFFImageDecoder(tiffSeekableStream, null);
            numPages = dec.getNumPages();
        } catch (NullPointerException ex) {
            throw new NoSuchImageDecoder("TIFF");
        }
        if (null == numPages) {
            throw new PagesInImageFileIsNull("TIFF file " + file.getName());
        }
        return numPages;
    }

    private static void setDpiPNG(IIOMetadata metadata, int dpi) {
        //
        // See: http://stackoverflow.com/questions/10660695/how-to-set-dots-per-square-inch-dpi-for-png-in-java
        //

        //for PNG, it's dots per millimeter
        String dotsPerMilli = Consts.getDotsPerMilliAsString(dpi);

        IIOMetadataNode horiz = new IIOMetadataNode(Consts.PNGMetadata.H_PIXEL_SIZE);
        horiz.setAttribute(Consts.PNGMetadata.VALUE, dotsPerMilli);

        IIOMetadataNode vert = new IIOMetadataNode(Consts.PNGMetadata.V_PIXEL_SIZE);
        vert.setAttribute(Consts.PNGMetadata.VALUE, dotsPerMilli);

        IIOMetadataNode dim = new IIOMetadataNode(Consts.PNGMetadata.DIMENSION);
        dim.appendChild(horiz);
        dim.appendChild(vert);

        IIOMetadataNode root = new IIOMetadataNode(Consts.IMAGEIO_API_VERSION);
        root.appendChild(dim);
        try {
            metadata.mergeTree(Consts.IMAGEIO_API_VERSION, root);
        } catch (IIOInvalidTreeException ex) {
            Logger.getLogger(TiffDoc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private HashMap<String, Integer> getResolutionFromTIFFMetadata(File tiffInputFile)
            throws IOException, CorruptMetadata {

        HashMap<String, Integer> resInfo = new LinkedHashMap<>();

        try (ImageInputStream tiffInputStream = new FileImageInputStream(tiffInputFile)) {
            ImageReader reader = getTiffImageReaderForImage(tiffInputStream);
            IIOMetadata imageMetadata = reader.getImageMetadata(Consts.FIRST_PAGE);

            TIFFDirectory ifd = TIFFDirectory.createFromMetadata(imageMetadata);

            Integer xRes, yRes;
            try {
                xRes = Integer.parseInt(ifd.getTIFFField(Consts.TIFFMetadata.X_RES_TAG).getValueAsString(0).split(
                        Consts.TIFFMetadata.RES_DELIMITER)[0]);
                yRes = Integer.parseInt(ifd.getTIFFField(Consts.TIFFMetadata.X_RES_TAG).getValueAsString(0).split(
                        Consts.TIFFMetadata.RES_DELIMITER)[0]);
            } catch (NullPointerException npe){
                // Some TIFF reading/generating tools default to 72dpi
                // See: http://www.imagemagick.org/discourse-server/viewtopic.php?f=3&t=22457
                // However:
                throw new CorruptMetadata("Unable to read resolution");
            }

            String resUnit;
            try{
                resUnit = ifd.getTIFFField(Consts.TIFFMetadata.RESOLUTION_UNIT_TAG).getValueAsString(0);
            } catch (NullPointerException npe) {
                Logger.getLogger(TiffDoc.class.getName()).log(Level.WARNING,
                        "Unable to read resolution unit. Defaulting to inches...");
                resUnit = Consts.TIFFMetadata.RESOLUTION_UNIT_INCH;
            }

            if (Consts.TIFFMetadata.RESOLUTION_UNIT_CM.equals(resUnit)) {
                xRes = new Double(xRes / Consts.INCH_TO_CM).intValue();
                yRes = new Double(yRes / Consts.INCH_TO_CM).intValue();
            }

            resInfo.put(Consts.TIFFMetadata.X_RES, xRes);
            resInfo.put(Consts.TIFFMetadata.Y_RES, yRes);

        }

        return resInfo;
    }
}