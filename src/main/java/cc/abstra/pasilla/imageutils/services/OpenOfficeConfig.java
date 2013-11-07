package cc.abstra.pasilla.imageutils.services;

import java.util.Iterator;
import java.util.List;

class OpenOfficeConfig {

    private static int[] ports;

    // process List<Long> b/c JRuby casts [1,2,3] as List<Long>
    public static void setPorts(List<Long> listenAtPorts) {
        int[] ret = new int[listenAtPorts.size()];
        Iterator<Long> it = listenAtPorts.iterator();
        for(int i=0; i < ret.length; i++) {
            Long port = it.next();
            if (port < Integer.MIN_VALUE || port > Integer.MAX_VALUE) {
                throw new IllegalArgumentException(port + " cannot be cast to int without changing its value.");
            } else {
                ret[i] = port.intValue();
            }
        }
        ports = ret;
    }

    public static  int[] getPorts(){
        return ports;
    }
}
