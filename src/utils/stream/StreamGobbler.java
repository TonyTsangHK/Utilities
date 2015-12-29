package utils.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-11-11
 * Time: 15:28
 */
public class StreamGobbler extends Thread {
    private InputStream is;
    private Type type;
    private String encoding;

    private StringBuilder builder;

    public StreamGobbler(InputStream is, Type type) {
        this(is, type, "UTF-8");
    }

    public StreamGobbler(InputStream is, Type type, String encoding) {
        this.is = is;
        this.type = type;
        this.encoding = encoding;
        this.builder = new StringBuilder();
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is, encoding);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }

    public Type getType() {
        return type;
    }

    public String getOutput() {
        return builder.toString();
    }

    public enum Type {
        ERR("err", "error"), OUT("out", "output");

        public final String code, desc;

        Type(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }
}