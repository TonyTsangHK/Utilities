package utils.file;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomFileFilter extends FileFilter {
    private List<String> extensions;
    private String description;
    
    public CustomFileFilter(String[] extensions, String desc){
        this.extensions = new ArrayList<String>(extensions.length);
        for (int i = 0; i < extensions.length; i++) {
            this.extensions.add(extensions[i].toLowerCase());
        }
        description = desc;
    }
    
    public CustomFileFilter(String ... extensions) {
        this.extensions = new ArrayList<String>(extensions.length);
        for (int i = 0; i < extensions.length; i++) {
            String s = extensions[i].toLowerCase();
            this.extensions.add(s);
        }
        refreshDescriptionWithExtensions();
    }

    public void refreshDescriptionWithExtensions() {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < extensions.size(); i++) {
            if (i > 0) {
                builder.append(",");
            }
            String s = extensions.get(i).toLowerCase();
            builder.append("*." + s);
        }
        description = builder.toString();
    }

    public void addExtension(String s) {
        extensions.add(s);
    }
    
    public void addExtension(String ... exts) {
        for (String ext : exts) {
            this.extensions.add(ext);
        }
    }
    
    public void removeExtension(int i) {
        if (i >= 0 && i < extensions.size()) {
            extensions.remove(i);
        }
    }
    
    public void removeExtension(String s) {
        extensions.remove(s);
    }
    
    public void removeExtensions(String ... exts) {
        for (String s : exts) {
            removeExtension(s);
        }
    }

    public void setExtensions(String ... exts) {
        extensions.clear();
        for (String ext : exts) {
            extensions.add(ext);
        }
    }
    
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        
        for (String s : extensions) {
            if (f.getName().toLowerCase().endsWith("."+s)) {
                return true;
            }
        }
        
        return false;
    }
    
    public void setDescription(String desc) {
        description = desc;
    }

    public String getDescription() {
        return description;
    }
}
