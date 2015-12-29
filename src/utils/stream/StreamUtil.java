package utils.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

public class StreamUtil {
    public static String readStreamAsString(InputStream stream, String charset) {
        if (stream == null) {
            return "";
        } else {
            try {
                char[] chars = new char[1024];
                
                InputStreamReader reader = new InputStreamReader(stream, Charset.forName(charset));
                StringBuilder builder = new StringBuilder();
                int len = reader.read(chars);
                while (len != -1) {
                    builder.append(chars, 0, len);
                    len = reader.read(chars);
                }
                
                reader.close();
                
                return builder.toString();
            } catch (IOException iox) {
                return null;
            }
        }
    }
    
    public static boolean writeStringToStream(OutputStream stream, String output, String charset) {
        if (stream != null) {
            try {
                stream.write(output.getBytes(Charset.forName(charset)));
                stream.close();
                return true;
            } catch (IOException iox) {
                return false;
            }
        } else {
            return false;
        }
    }
    
    public static int readStream(InputStream in, byte[] bytes) throws IOException {
        int len = 0, offset = 0;
        do {
            len = in.read(bytes, offset, bytes.length - offset);
            offset += (len != -1)? len : 0;
        } while (len != -1 && offset < bytes.length);
        
        return offset;
    }
    
    public static byte[] readStream(InputStream in, int length) throws IOException {
        byte[] bytes = new byte[length];
        int len = readStream(in, bytes);
        
        if (len < bytes.length) {
            return Arrays.copyOf(bytes, len);
        } else {
            return bytes;
        }
    }
}
