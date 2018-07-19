package utils.hash;

import utils.string.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HashUtil {
    private HashUtil() {}

    static {
        // Add missing crc32 hash algorithm
        Security.addProvider(new CRC32Provider());
    }

    public static long getFileCrc32HashValue(String path) throws IOException {
        return getFileCrc32HashValue(new File(path));
    }

    public static long getFileCrc32HashValue(File file) throws IOException {
        if (file == null || !file.exists()) {
            return -1;
        } else {
            CRC32MessageDigest md = new CRC32MessageDigest();
            byte[] bytes = new byte[4096];

            FileInputStream fileInputStream = new FileInputStream(file);

            int readBytes = fileInputStream.read(bytes);

            while (readBytes != -1) {
                md.update(bytes, 0, readBytes);

                readBytes = fileInputStream.read(bytes);
            }

            fileInputStream.close();

            return md.getValue();
        }
    }

    public static String getFileCrc32Hash(String path) throws IOException {
        return getFileCrc32Hash(new File(path));
    }

    public static String getFileCrc32Hash(File file) throws IOException {
        return getFileHash(file, HashAlgorithm.CRC32);
    }

    public static String getFileMd5Hash(String path) throws IOException {
        return getFileMd5Hash(new File(path));
    }
    
    public static String getFileMd5Hash(File file) throws IOException {
        return getFileHash(file, HashAlgorithm.MD5);
    }
    
    public static String getFilesMd5Hash(Collection<? extends File> files) throws IOException {
        return getFilesHash(files, HashAlgorithm.MD5);
    }
    
    public static String getFileSha1Hash(File file) throws IOException {
        return getFileHash(file, HashAlgorithm.SHA1);
    }
    
    public static String getFileSha1Hash(Collection<? extends File> files) throws IOException {
        return getFilesHash(files, HashAlgorithm.SHA1);
    }
    
    public static String getFileHash(String path, HashAlgorithm algorithm) throws IOException {
        return getFileHash(new File(path), algorithm);
    }
    
    public static byte[] getFileHashDigest(String path, HashAlgorithm algorithm) throws IOException {
        return getFileHashDigest(new File(path), algorithm);
    }
    
    public static String getFileHash(File file, HashAlgorithm algorithm) throws IOException {
        return StringUtil.getHexStringFromBytes(getFileHashDigest(file, algorithm));
    }
    
    public static byte[] getFileHashDigest(File file, HashAlgorithm algorithm) throws IOException {
        if (file == null || !file.exists()) {
            return new byte[0];
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithm.desc);
                byte[] bytes = new byte[4096];
                
                FileInputStream fileInputStream = new FileInputStream(file);
                
                int readBytes = fileInputStream.read(bytes);
                
                while (readBytes != -1) {
                    md.update(bytes, 0, readBytes);
                    
                    readBytes = fileInputStream.read(bytes);
                }
                
                fileInputStream.close();
                
                return md.digest();
            } catch (NoSuchAlgorithmException nsae) {
                return new byte[0];
            }
        }
    }
    
    private static String getFilesHash(Collection<? extends File> files, HashAlgorithm algorithm) throws IOException {
        return StringUtil.getHexStringFromBytes(getFilesHashDigest(files, algorithm));
    }
    
    private static byte[] getFilesHashDigest(Collection<? extends File> files, HashAlgorithm algorithm) throws IOException {
        if (files == null) {
            return new byte[0];
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithm.desc);
                byte[] bytes = new byte[4096];
                
                for (File file : files) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    
                    int readBytes = fileInputStream.read(bytes);
                    
                    while (readBytes != -1) {
                        md.update(bytes, 0, readBytes);
                        
                        readBytes = fileInputStream.read(bytes);
                    }
                    fileInputStream.close();
                }
                
                return md.digest();
            } catch (NoSuchAlgorithmException nsae) {
                return new byte[0];
            }
        }
    }

    public static long getContentCrc32HashValue(byte[] content) {
        CRC32MessageDigest md = new CRC32MessageDigest();

        md.update(content);

        return md.getValue();
    }

    public static String getContentCrc32Hash(byte[] content) {
        return getContentHash(content, HashAlgorithm.CRC32);
    }

    public static String getContentSha1Hash(byte[] content) {
        return getContentHash(content, HashAlgorithm.SHA1);
    }

    public static String getContentSha1Hash(String content) {
        return getContentHash(content, HashAlgorithm.SHA1);
    }

    public static String getContentMd5Hash(byte[] content) {
        return getContentHash(content, HashAlgorithm.MD5);
    }

    public static String getContentMd5Hash(String content) {
        return getContentHash(content, HashAlgorithm.MD5);
    }

    public static String getContentHash(String content, HashAlgorithm algorithm) {
        return StringUtil.getHexStringFromBytes(getContentHashDigest(content, algorithm));
    }

    public static String getContentHash(byte[] content, HashAlgorithm algorithm) {
        return StringUtil.getHexStringFromBytes(getContentHashDigest(content, algorithm));
    }

    public static byte[] getContentHashDigest(String content, HashAlgorithm algorithm) {
        return getContentHashDigest(content.getBytes(), algorithm);
    }

    public static byte[] getContentHashDigest(byte[] content, HashAlgorithm algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.desc);
            md.update(content);
            return md.digest();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return new byte[0];
        }
    }
    
    public static List<String> getIndividualFilesHash(Collection<Object> objs, HashAlgorithm algorithm) 
            throws IOException {
        List<String> hashes = new ArrayList<>(objs.size());
        
        for (Object obj : objs) {
            if (obj instanceof File) {
                hashes.add(getFileHash((File) obj, algorithm));
            } else {
                hashes.add(getFileHash(String.valueOf(obj), algorithm));
            }
        }
        
        return hashes;
    }
    
    public static List<byte[]> getIndividualFilesHashDigest(Collection<Object> objs, HashAlgorithm algorithm) 
            throws IOException {
        List<byte[]> hashes = new ArrayList<>(objs.size());
        
        for (Object obj : objs) {
            if (obj instanceof File) {
                hashes.add(getFileHashDigest((File) obj, algorithm));
            } else {
                hashes.add(getFileHashDigest(String.valueOf(obj), algorithm));
            }
        }
        
        return hashes;
    }

    /**
     * General hash code generation method
     *
     * @return hashcode of hashables
     */
    public static int hashCodeOfHashables(Object ... hashables) {
        int hash = 23;

        for (Object hashable : hashables) {
            hash = hash * 31 + hashable.hashCode();
        }

        return hash;
    }
}
