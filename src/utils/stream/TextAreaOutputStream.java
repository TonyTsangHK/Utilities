package utils.stream;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2013-09-24
 * Time: 09:57
 */
public class TextAreaOutputStream extends OutputStream {
    private JTextArea textArea;
    private byte[] buffer;
    private int bufferSize;

    public TextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
        buffer = new byte[1024];
        bufferSize = 0;
    }

    @Override
    public void write(int b) throws IOException {
        if (bufferSize == buffer.length) {
            flush();
        }

        byte byt = (byte) (b & 0xFF);
        buffer[bufferSize++] = byt;
    }

    @Override
    public void flush() throws IOException {
        if (bufferSize > 0) {
            textArea.append(new String(buffer, 0, bufferSize));
            bufferSize = 0;
        }
    }
}
