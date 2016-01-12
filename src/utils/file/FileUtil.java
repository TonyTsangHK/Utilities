package utils.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.swing.filechooser.FileFilter;

public class FileUtil {
    public static final boolean CaseSensitive = File.separatorChar == '/';
    
    public static String getExtension(File f) {
        if (f == null) {
            return "";
        } else {
            return getExtension(f.getName());
        }
    }
    
    public static String getExtension(String fileName) {
        if (fileName == null) {
            return "";
        } else {
            int lastDotIndex = fileName.lastIndexOf(".");
            if (lastDotIndex > -1) {
                return fileName.substring(lastDotIndex+1);
            } else {
                return "";
            }
        }
    }
    
    public static String getNamePart(File file) {
        if (file == null) {
            return "";
        } else {
            return getNamePart(file.getName());
        }
    }
    
    public static String getNamePart(String fileName) {
        if (fileName == null) {
            return "";
        } else {
            int lastDotIndex = fileName.lastIndexOf(".");
            if (lastDotIndex > -1) {
                return fileName.substring(0, lastDotIndex);
            } else {
                return fileName;
            }
        }
    }
    
    public static String replaceExtension(File file, String newExtension) {
        return getNamePart(file) + "." + newExtension;
    }
    
    public static String replaceExtension(String fileName, String newExtension) {
        return getNamePart(fileName) + "." + newExtension;
    }
    
    public static FileFilter createFileFilter(String[] extensions, String desc) {
        return new CustomFileFilter(extensions, desc);
    }
    
    public static FileFilter createFileFilter(String desc, String ... extensions) {
        return new CustomFileFilter(extensions, desc);
    }
    
    public static String getFileContent(String path) {
        return getFileContent(new File(path));
    }
    
    public static String getFileContent(File file) {
        if (file == null) {
            return null;
        }
        try {
            FileReader reader = new FileReader(file);
            String fileContent = getFileContent(reader);
            reader.close();
            return fileContent;
        } catch (IOException iox) {
            return null;
        }
    }
    
    public static String getFileContent(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        } else {
            return getFileContent(new InputStreamReader(inputStream));
        }
    }
    
    public static String getFileContent(String path, String charsetName) {
        return getFileContent(new File(path), charsetName);
    }
    
    public static String getFileContent(File file, String charsetName) {
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), charsetName);
            String fileContent = getFileContent(reader);
            reader.close();
            return fileContent;
        } catch (IOException iox) {
            return null;
        }
    }
    
    public static String getFileContent(InputStream inputStream, String charsetName) {
        if (inputStream == null) {
            return null;
        }
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charsetName);
            String fileContent = getFileContent(inputStreamReader);
            inputStreamReader.close();
            return fileContent;
        } catch (UnsupportedEncodingException usee) {
            return null;
        } catch (IOException iox) {
            // Nothing to be down here!
            return null;
        }
    }
    
    public static String getFileContent(Reader reader) {
        if (reader == null) {
            return null;
        }
        try {
            StringBuilder builder = new StringBuilder();
            char[] chars = new char[1024];
            
            int len = reader.read(chars);
            
            while (len != -1) {
                builder.append(chars, 0, len);
                len = reader.read(chars);
            }
            
            return builder.toString();
        } catch (IOException iox) {
            return null;
        }
    }

    public static byte[] getFileContentBytes(String filePath) {
        try {
            return getFileContentBytes(new FileInputStream(filePath));
        } catch (IOException iox) {
            return null;
        }
    }

    public static byte[] getFileContentBytes(File file) {
        try {
            InputStream inputStream = new FileInputStream(file);
            byte[] fileBytes = getFileContentBytes(inputStream);
            inputStream.close();
            return fileBytes;
        } catch (IOException iox) {
            return null;
        }
    }

    public static byte[] getFileContentBytes(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];

            int len = inputStream.read(bytes);

            while (len != -1) {
                baos.write(bytes, 0, len);
                len = inputStream.read(bytes);
            }

            byte[] result = baos.toByteArray();

            baos.close();

            return result;
        } catch (IOException e) {
            return null;
        }
    }
    
    public static boolean isAbsolutePath(String path) {
        for (File root : File.listRoots()) {
            if (CaseSensitive) {
                if (path.startsWith(root.getPath())) {
                    return true;
                }
            } else {
                path = path.replace('/', '\\');
                if (path.toLowerCase().startsWith(root.getPath().toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static PathInfo extractPathInfo(File currentDir, String path) {
        boolean abs = isAbsolutePath(path);
        
        int li = Math.max(path.lastIndexOf('\\'), path.lastIndexOf('/'));
        
        File parent;
        String prefix = "";
        
        if (li == -1) {
            parent = currentDir;
            prefix = path;
        } else if (li == path.length() - 1) {
            if (abs) {
                parent = new File(path);
            } else {
                parent = new File(currentDir, path);
            }
        } else {
            if (abs) {
                parent = new File(path.substring(0, li+1));
            } else {
                parent = new File(currentDir, path.substring(0, li+1));
            }
            prefix = path.substring(li+1);
        }
        
        return new PathInfo(parent, prefix);
    }
    
    public static List<File> findPossibleDirectories(File currDir, String parameter) {
        List<File> possibles = new ArrayList<>();
        
        if (currDir == null || !currDir.exists() || currDir.isFile()) {
            return possibles;
        }
        
        PathInfo pathInfo = extractPathInfo(currDir, parameter);
        
        File parent = pathInfo.getParent();
        
        if (parent == null || parent.listFiles() == null || !parent.exists() || parent.isFile()) {
            return possibles;
        } else {
            for (File f : parent.listFiles()) {
                if (
                        f.isDirectory() &&
                        (
                            CaseSensitive && f.getName().startsWith(pathInfo.getPrefix()) ||
                                !CaseSensitive &&
                                f.getName().toLowerCase().startsWith(pathInfo.getPrefix().toLowerCase())
                        )
                ) {
                    possibles.add(f);
                }
            }

            return possibles;
        }
    }
    
    public static List<File> findPossibleDirectorieAndFiles(File currDir, String parameter) {
        List<File> possibles = new ArrayList<>();
        
        if (currDir == null || !currDir.exists() || currDir.isFile()) {
            return possibles;
        }
        
        PathInfo pathInfo = extractPathInfo(currDir, parameter);
        
        File parent = pathInfo.getParent();
        
        if (parent == null || !parent.exists() || parent.isFile()) {
            return possibles;
        }
        
        if (pathInfo.getParent() == null) {
            return possibles;
        }
        
        for (File f : pathInfo.getParent().listFiles()) {
            if (
                    CaseSensitive && f.getName().startsWith(pathInfo.getPrefix()) || 
                    !CaseSensitive && f.getName().toLowerCase().startsWith(pathInfo.getPrefix().toLowerCase())
            ) {
                possibles.add(f);
            }
        }
        
        return possibles;
    }
    
    public static List<String> findPossibleDrive(String parameter) {
        List<String> drvs = new ArrayList<>();
        
        for (File f : File.listRoots()) {
            if (f.getPath().toLowerCase().startsWith(parameter.toLowerCase())) {
                drvs.add(f.getPath());
            }
        }
        
        return drvs;
    }

    public static String getRelativePath(String parent, String target) {
        return getRelativePath(new File(parent), new File(target));
    }

    public static String getRelativePath(File parent, File target) {
        if (parent == null || target == null) {
            return null;
        } else {
            return parent.toURI().relativize(target.toURI()).getPath();
        }
    }

    public static boolean deleteDirectory(String dirPath) {
        return deleteDirectory(new File(dirPath));
    }

    public static boolean deleteDirectory(File dir) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (
                    (file.isDirectory() && !deleteDirectory(file)) ||
                    (file.isFile() && !file.delete())
                ) {
                    return false;
                }
            }
            return dir.delete();
        } else {
            return false;
        }
    }

    public static boolean copyFile(String srcPath, String destPath) {
        try {
            return copyFile(new FileInputStream(srcPath), new FileOutputStream(destPath));
        } catch (IOException iox) {
            return false;
        }
    }

    public static boolean copyFile(File src, File dest) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);
            boolean result = copyFile(in, out);

            in.close();
            out.close();

            return result;
        } catch (IOException iox) {
            return false;
        }
    }

    public static boolean copyFile(InputStream in, OutputStream out) {
        byte[] bytes = new byte[4096];

        try {
            BufferedInputStream bin = new BufferedInputStream(in);
            BufferedOutputStream bout = new BufferedOutputStream(out);

            int len = bin.read(bytes);

            while (len != -1) {
                bout.write(bytes, 0, len);
                len = bin.read(bytes);
            }

            bin.close();
            bout.close();

            return true;
        } catch (IOException iox) {
            return false;
        }
    }

    public static Reader getReader(File file) throws IOException {
        return new InputStreamReader(getProperInputStream(new FileInputStream(file)));
    }

    public static InputStream getProperInputStream(InputStream input) throws IOException {
        PushbackInputStream pb = new PushbackInputStream(input, 2); //we need a PushbackInputStream to look ahead
        byte [] signature = new byte[2];
        int l = pb.read(signature); //read the signature
        if (l == 2) {
            pb.unread(signature); //push back the signature to the stream
            if( signature[0] == (byte) 0x1f && signature[1] == (byte) 0x8b ) {
                //check if matches standard gzip magic number
                return new GZIPInputStream(pb);
            } else {
                return pb;
            }
        } else {
            // Failed read 2 bytes, return pb
            if (l > 0) {
                pb.unread(signature, 0, l);
            }
            return pb;
        }
    }

    public static File[] listFiles(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();

            if (files == null) {
                return new File[0];
            } else {
                return files;
            }
        } else {
            return new File[0];
        }
    }

    /**
     * Close reader without handling IOException. Raised exception will be ignored, be careful
     *
     * @param reader Reader to be closed
     */
    public static void safeClose(Reader reader) {
        try {
            reader.close();
        } catch (IOException iox) {
            // IOException is ignored.
        }
    }

    /**
     * Close input stream without handling IOException. Raised exception will be ignored, be careful.
     *
     * @param inputStream
     */
    public static void safeClose(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException iox) {
            // IOException is ignored
        }
    }
}
