package utils.event;

import java.io.File;
import java.util.EventObject;

public class FileChoosedEvent extends EventObject {
    private static final long serialVersionUID = 1L;
    
    private File originalFile, choosedFile;
    
    public FileChoosedEvent(Object source, File originalFile, File choosedFile) {
        super(source);
        this.originalFile = originalFile;
        this.choosedFile = choosedFile;
    }
    
    public File getOriginalFile() {
        return originalFile;
    }
    
    public File getChoosedFile() {
        return choosedFile;
    }
}
