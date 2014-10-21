package utils;

/**
 * Created by WojtawDesktop on 21.10.2014.
 */
public class Utils {
    public static String toHexString(byte[] digest) {
        StringBuilder tmpBuilder = new StringBuilder();
        for (byte b : digest) {
            tmpBuilder.append(String.format("%02x", b & 0xff));
        }

        return tmpBuilder.toString();
    }

}
