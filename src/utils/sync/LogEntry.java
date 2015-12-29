package utils.sync;

import utils.hash.HashAlgorithm;
import utils.hash.HashUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2015-03-25
 * Time: 10:54
 */
public class LogEntry {
    private String path, hash;
    private long lastModificationTime, length;
    private boolean isNew;
    private int compareCount = 0;

    public LogEntry(File file) {
        path = file.getAbsolutePath();
        lastModificationTime = file.lastModified();
        length = file.length();
        try {
            hash = HashUtil.getFileHash(file, HashAlgorithm.SHA1);
        } catch (IOException iox) {
            // Dummy catch
        }
        isNew = true;
    }

    public LogEntry(String entry) {
        int i1 = entry.lastIndexOf(","), i2 = entry.lastIndexOf(",", i1-1), i3 = entry.lastIndexOf(",", i2-1);
        path = entry.substring(0, i3);
        hash = entry.substring(i3+1, i2).trim();
        lastModificationTime = Long.parseLong(entry.substring(i2+1, i1).trim());
        length = Long.parseLong(entry.substring(i1+1).trim());
        isNew = false;
    }

    public LogEntry(String path, String hash, long lastModificationTime, long length) {
        this.path = path;
        this.hash = hash;
        this.lastModificationTime = lastModificationTime;
        this.length = length;
    }

    public boolean isModified(File file) {
        compareCount++;
        if (trivialModificationCheck(file.length())) {
            return true;
        } else {
            try {
                return !HashUtil.getFileSha1Hash(file).equals(hash);
            } catch (IOException iox) {
                return false;
            }
        }
    }

    private boolean trivialModificationCheck(long length) {
        return this.length != length;
    }

    public void setEntry(File file) {
        setLastModificationTime(file.lastModified());
        setLength(file.length());
        try {
            setHash(HashUtil.getFileSha1Hash(file));
        } catch (IOException iox) {
            // Dummy catch, do nothing!
        }
    }

    public void setLastModificationTime(long lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getLastModificationTime() {
        return lastModificationTime;
    }

    public long getLength() {
        return length;
    }

    public String getPath() {
        return path;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public boolean isCompared() {
        return compareCount > 0;
    }

    public int getCompareCount() {
        return compareCount;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    @Override
    public String toString() {
        return path + "," + hash + "," + lastModificationTime + "," + length;
    }
}
