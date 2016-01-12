package utils.hash;

import java.security.MessageDigest;
import java.util.zip.CRC32;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-01-12
 * Time: 09:55
 */
public class CRC32MessageDigest extends MessageDigest {
    private CRC32 crc32;

    public CRC32MessageDigest() {
        super("CRC32");
        crc32 = new CRC32();
    }

    @Override
    protected void engineUpdate(byte input) {
        crc32.update(input);
    }

    @Override
    protected void engineUpdate(byte[] input, int offset, int len) {
        crc32.update(input, offset, len);
    }

    @Override
    protected byte[] engineDigest() {
        long v = crc32.getValue();
        byte[] bytes = new byte[4];

        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) ((v >>> ((3 - i) * 8)) & 0xFF);
        }

        return bytes;
    }

    @Override
    protected void engineReset() {
        crc32.reset();
    }

    public long getValue() {
        return crc32.getValue();
    }
}
