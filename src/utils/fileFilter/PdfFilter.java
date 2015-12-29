package utils.fileFilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import utils.file.FileUtil;

public class PdfFilter extends FileFilter {
    private String desc;
    
    public PdfFilter() {
        this("*.pdf (Adobe Pdf document)");
    }
    
    public PdfFilter(String desc) {
        super();
        if (desc != null && !desc.equals("")) {
            this.desc = desc;
        }
    }
    
    @Override
    public boolean accept(File f) {
        return f.isDirectory() || FileUtil.getExtension(f).toLowerCase().equals("pdf");
    }

    @Override
    public String getDescription() {
        return desc;
    }
}
