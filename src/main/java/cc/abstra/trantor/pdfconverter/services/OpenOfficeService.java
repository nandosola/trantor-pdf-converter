package cc.abstra.trantor.pdfconverter.services;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;


public class OpenOfficeService {

    private static OfficeManager officeManager = null; //Couldn't make NullObject b/c of poor OfficeManager declaration

    public static void initialize(List<Long> listenAtPorts) {
        if(null == officeManager) {
            OpenOfficeConfig.setPorts(listenAtPorts);
            int[] ports = OpenOfficeConfig.getPorts();

            Logger.getLogger(OpenOfficeService.class.getName()).log(Level.INFO,
                    "Initializing OfficeManager in ports: "+Arrays.toString(ports));

            //OS X: cd /Applications/LibreOffice.app/Contents/MacOS/
            //      sudo ln -s ./soffice soffice.bin
            // FIXED in master:
            //   https://github.com/nuxeo/jodconverter/commit/30eb0644a217081ce936a866ac6be411c62c6f49
            officeManager = new DefaultOfficeManagerConfiguration().
                    setPortNumbers(ports).
                    buildOfficeManager();
        } else {
            Logger.getLogger(OpenOfficeService.class.getName()).log(Level.WARNING,
                    "OfficeManager is already initialized!");
        }
    }

    public static void start() {
        long startTime = System.currentTimeMillis();
        officeManager.start();
        logElapsedTime(startTime, "Started");
    }

    public static void stop() {
        long startTime = System.currentTimeMillis();
        officeManager.stop();
        logElapsedTime(startTime, "Stopped");
    }

    public static OfficeManager getOfficeManager(){
        return officeManager;
    }

    public static void logElapsedTime(long startTime, String verb) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        Logger.getLogger(OpenOfficeService.class.getName()).log(Level.INFO,
                String.format(verb + " OfficeManager in %dms", elapsedTime));
    }
}
