package utils.stream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Locale;

import utils.date.DateTimeParser;

public class ErrorPrintStream extends PrintStream {
    private boolean started = false;
    
    public ErrorPrintStream(File file) throws FileNotFoundException {
        super(new FileOutputStream(file, true));
        started = false;
    }
    
    private void checkStarted() {
        if (!started) {
            started = true;
            println("===Error on " + DateTimeParser.getCurrentTimeString() + "===");
        }
    }
    
    @Override
    public void print(boolean b) {
        checkStarted();
        super.print(b);
    }
    
    @Override
    public void print(char c) {
        checkStarted();
        super.print(c);
    }
    
    @Override
    public void print(char[] s) {
        checkStarted();
        super.print(s);
    }
    
    @Override
    public void print(double d) {
        checkStarted();
        super.print(d);
    }
    
    @Override
    public void print(float f) {
        checkStarted();
        super.print(f);
    }
    
    @Override
    public void print(int i) {
        started = true;
        super.print(i);
    }
    
    @Override
    public void print(long l) {
        checkStarted();
        super.print(l);
    }
    
    @Override
    public void print(Object obj) {
        checkStarted();
        super.print(obj);
    }
    
    @Override
    public void print(String s) {
        checkStarted();
        super.print(s);
    }
    
    @Override
    public PrintStream printf(Locale l, String format, Object... args) {
        checkStarted();
        return super.printf(l, format, args);
    }
    
    @Override
    public PrintStream printf(String format, Object... args) {
        checkStarted();
        return super.printf(format, args);
    }
    
    @Override
    public void println() {
        checkStarted();
        super.println();
    }
    
    @Override
    public void println(boolean x) {
        checkStarted();
        super.println(x);
    }
    
    @Override
    public void println(char x) {
        checkStarted();
        super.println(x);
    }
    
    @Override
    public void println(char[] x) {
        checkStarted();
        super.println(x);
    }
    
    @Override
    public void println(double x) {
        checkStarted();
        super.println(x);
    }
    
    @Override
    public void println(float x) {
        checkStarted();
        super.println(x);
    }
    
    @Override
    public void println(int x) {
        checkStarted();
        super.println(x);
    }
    
    @Override
    public void println(long x) {
        checkStarted();
        super.println(x);
    }
    
    @Override
    public void println(Object x) {
        checkStarted();
        super.println(x);
    }
    
    @Override
    public void println(String x) {
        checkStarted();
        super.println(x);
    }
    
    @Override
    public void write(byte[] buf, int off, int len) {
        checkStarted();
        super.write(buf, off, len);
    }
    
    @Override
    public void write(int b) {
        checkStarted();
        super.write(b);
    }
}
