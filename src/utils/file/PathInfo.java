package utils.file;

import java.io.File;

public class PathInfo {
        private File parent;
        private String prefix;
        
        public PathInfo(File parent, String prefix) {
            setParent(parent);
            setPrefix(prefix);
        }
        
        public void setParent(File parent) {
            this.parent = parent;
        }
        
        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }
        
        public File getParent() {
            return parent;
        }
        
        public String getPrefix() {
            return prefix;
        }
    }