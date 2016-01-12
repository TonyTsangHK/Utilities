package utils.hash;

import java.security.Provider;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-01-12
 * Time: 09:52
 */
public class CRC32Provider extends Provider {
    public CRC32Provider() {
        super("CRC32 Provider", 1.0, "Hash utilities");
        put("MessageDigest.CRC32", "utils.hash.CRC32MessageDigest");
    }
}
