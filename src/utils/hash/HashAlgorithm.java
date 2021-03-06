package utils.hash;

public enum HashAlgorithm {
    CRC32("CRC32"), MD2("MD2"), MD5("MD5"), SHA1("SHA-1"), SHA256("SHA-256"), SHA384("SHA-384"), SHA512("SHA-512");
    
    public final String desc;
    
    private HashAlgorithm(String desc) {
        this.desc = desc;
    }
    
    @Override
    public String toString() {
        return desc;
    }
}
