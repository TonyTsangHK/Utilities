package utils.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2015-05-29
 * Time: 17:12
 */
public class GZipUtils {
    private GZipUtils() {}

    public static byte[] zipStringContent(String content) {
        return zipStringContent(content, "UTF-8");
    }

    public static byte[] zipStringContent(String content, String charset) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            GZIPOutputStream gos = new GZIPOutputStream(baos);

            gos.write(content.getBytes(charset));

            gos.close();
            baos.close();

            return baos.toByteArray();
        } catch (IOException iox) {
            // Not expected, except memory overflow
            return null;
        }
    }

    public static String unzipStringContent(byte[] contentBytes) {
        return unzipStringContent(contentBytes, "UTF-8");
    }

    public static String unzipStringContent(byte[] contentBytes, String charset) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(contentBytes);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            GZIPInputStream gis = new GZIPInputStream(bais);

            byte[] bytes = new byte[4096];

            int len = gis.read(bytes);

            while (len != -1) {
                baos.write(bytes, 0, len);
                len = gis.read(bytes);
            }

            gis.close();
            bais.close();
            baos.close();

            return new String(baos.toByteArray(), charset);
        } catch (IOException iox) {
            // Not expected, except memory overflow
            return null;
        }
    }
}
