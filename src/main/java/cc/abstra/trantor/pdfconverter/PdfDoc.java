package cc.abstra.trantor.pdfconverter;

/**
 *
 * @author nando
 */

import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.exception.SyntaxValidationException;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import org.apache.pdfbox.util.PDFOperator;

import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PdfDoc {
    public static final float PDFBOX_DEFAULT_USER_SPACE_UNIT_DPI = 72.0f;

    //TODO: isSigned: See http://blog.javabien.net/2009/05/01/pdfbox-to-unit-test-pdf-files/

    static boolean isPDFA1bCompliant(String pdf){
        ValidationResult result = null;
        FileDataSource fd = new FileDataSource(pdf);
        try {
            PreflightParser parser = new PreflightParser(fd);
            parser.parse();
            PreflightDocument document = parser.getPreflightDocument();
            document.validate();

            result = document.getResult();
            document.close();

        } catch (SyntaxValidationException ex) {
            result = ex.getResult();

        } catch (IOException ex) {
            Logger.getLogger(PdfDoc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(PdfDoc.class.getName()).log(Level.INFO, "There was an error validating "+pdf+
                    "\nValidation marked as failing"+
                    "\nPlease create a ticket at " +
                    "https://issues.apache.org/jira/browse/PDFBOX/component/12315215 and paste the exception and "+
                    "the output from http://www.pdf-tools.com/pdf/validate-pdfa-online.aspx", ex);
            return false;
        }

        assert result != null;
        if (result.isValid()) {
            Logger.getLogger(PdfDoc.class.getName()).log(Level.INFO, "The file " + pdf + " is a valid PDF/A-1b file");
            return true;
        } else {
            Logger.getLogger(PdfDoc.class.getName()).log(Level.INFO, "The file" + pdf + " is not valid, error(s) :");
            for (ValidationResult.ValidationError error : result.getErrorsList()) {
                Logger.getLogger(PdfDoc.class.getName()).log(Level.INFO, error.getErrorCode() + " : " + error.getDetails());
            }
            return false;
        }
    }

    public static void pdfToPngPreview(String pdf, String output) throws IOException {

        PDDocument pdDoc = null;

        try {
            pdDoc = PDDocument.load(pdf);

            List pdPages = pdDoc.getDocumentCatalog().getAllPages();
            ListIterator pageIter = pdPages.listIterator();
            PDPage firstPage = (PDPage)pageIter.next();
            BufferedImage img = firstPage.convertToImage(BufferedImage.TYPE_INT_RGB, Consts.PREVIEW_DPI);
            ImageIO.write(img, Consts.PNG, new File(output));

        } catch (Exception ex) {
            Logger.getLogger(PdfDoc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (null != pdDoc) {
                pdDoc.close();
            }
        }
    }

    public static void pdfToPdfPreview(String pdf, String output) throws IOException {
        PDDocument pdDoc = null;
        List<Map<String, Object>> pageList = new ArrayList<>();

        try {
            pdDoc = PDDocument.load(pdf);

            List pdPages = pdDoc.getDocumentCatalog().getAllPages();
            for (Object pdPage : pdPages) {
                Map<String, Object> pageInfo = new LinkedHashMap<>();
                PDPage currentPage = (PDPage)pdPage;

                BufferedImage img = currentPage.convertToImage(BufferedImage.TYPE_INT_RGB, Consts.PREVIEW_DPI);
                float scaleDownFactor = Consts.PREVIEW_DPI/PDFBOX_DEFAULT_USER_SPACE_UNIT_DPI;  //required by PDFBox.convertToImage()

                //TODO Subclass org.apache.pdfbox.pdfviewer.PageDrawer if there's too much antialiasing

                pageInfo.put(Consts.IMAGE_KEY, ImageHelper.resizeImageToDINA4WithDPI(img, Consts.PREVIEW_DPI, scaleDownFactor));

                pageInfo.put(Consts.PAGE_SIZE_KEY, PDPage.PAGE_SIZE_A4);  //default preview size
                pageInfo.put(Consts.LANDSCAPE_KEY, ImageHelper.hasLandscapeOrientation(img));
                pageList.add(pageInfo);
            }

            writePageListToPdf(pageList, output);

        } catch (Exception ex) {
            Logger.getLogger(PdfDoc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (null != pdDoc) {
                pdDoc.close();
            }
        }
    }

    static void writePageListToPdf(List<Map<String, Object>> images, String output) throws IOException {

        PDDocument pdDoc = null;

        try {
            pdDoc = new PDDocument();

            for (Map<String, Object> image1 : images) {
                PDPage page = null;
                PDXObjectImage ximage = null;
                PDPageContentStream contentStream = null;
                Map<String, Object> image = image1;

                Object imgKey = image.get(Consts.IMAGE_KEY);

                if (imgKey instanceof BufferedImage) {
                    BufferedImage bi = (BufferedImage) imgKey;

                    PDRectangle pageSize = (PDRectangle)image.get(Consts.PAGE_SIZE_KEY);
                    page = new PDPage(pageSize);
                    page.setMediaBox(pageSize);

                    ximage = new PDPixelMap(pdDoc, bi);  //embeds PNG image

                    double vOffset, hOffset;
                    if ((boolean)image.get(Consts.LANDSCAPE_KEY)){
                        float pageWidth = pageSize.getWidth();
                        page.setRotation(90);
                        contentStream = new PDPageContentStream(pdDoc, page);
                        contentStream.concatenate2CTM(0, 1, -1, 0, pageWidth, 0);
                        vOffset = 0;
                        hOffset = ((Consts.A4_W_INCHES * Consts.INCH_TO_POINT) - ximage.getWidth())/2.0;
                    } else {
                        vOffset = ((Consts.A4_H_INCHES * Consts.INCH_TO_POINT) - ximage.getHeight())/2.0;
                        hOffset = 0;
                        contentStream = new PDPageContentStream(pdDoc, page);
                    }

                    contentStream.drawImage(ximage, (float) hOffset, (float) vOffset);

                    contentStream.close();
                    bi.flush();

                } else {
                    if (imgKey instanceof PDPage) {
                        page = (PDPage) imgKey;
                    } else {
                        throw new Exception("Unrecognized object found in 'img' value: " + imgKey.getClass().getName());
                    }
                }

                pdDoc.addPage(page);
            }

            pdDoc.save(output);

        } catch (Exception ex) {
            Logger.getLogger(PdfDoc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (null != pdDoc) {
                pdDoc.close();
            }
        }
    }

    private static boolean hasText(PDPage page) throws IOException {

        PDStream contents = page.getContents();
        PDFStreamParser parser = new PDFStreamParser(contents.getStream());
        parser.parse();
        List tokens = parser.getTokens();

        for (Object next : tokens) {
            if (next instanceof PDFOperator) {
                PDFOperator op = (PDFOperator) next;

                if (op.getOperation().equalsIgnoreCase(Consts.PDFMetadata.TEXT_OPERATOR)) {
                    return true;
                }
            }
        }
        return false;
    }


    //TODO: PDF Metadata: http://www.docjar.com/html/api/org/apache/pdfbox/examples/pdmodel/ExtractMetadata.java.html
    private static boolean checkVersion(String requiredVersion, String file) throws IOException {
        PDDocument document = null;
        boolean res = false;
        try {
            document = PDDocument.load(file);
            // some pdf-documents are broken and the pdf-version is in the headers and
            // not in the metadata section. See: pdfbox/pdfparser/PDFParser.java
            String version = Float.toString(document.getDocument().getVersion());
            Logger.getLogger(PdfDoc.class.getName()).log(Level.INFO, "The file" + file + " has version: "+ version);
            if(version.equals(requiredVersion))  // gte
                res = true;
        } finally {
            if(document != null)
                document.close();
        }

        return res;
    }
}