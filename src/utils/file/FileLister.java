package utils.file;

import utils.regex.WildcardToRegularExpression;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class FileLister {
    private List<FilePattern> includes, excludes;
    
    public FileLister() {
        this.includes = new ArrayList<FilePattern>();
        this.excludes = new ArrayList<FilePattern>();
    }
    
    public List<File> listFiles() {
        List<File> files = new ArrayList<File>();
        
        for (FilePattern include : includes) {
            List<File> includedFiles = include.listFiles();
            
            for (File includedFile : includedFiles) {
                if (!files.contains(includedFile)) {
                    files.add(includedFile);
                }
            }
        }
        
        for (FilePattern exclude : excludes) {
            List<File> excludedFiles = exclude.listFiles();
            
            files.removeAll(excludedFiles);
        }
        
        return files;
    }
    
    public void addInclude(File dir, String pattern) {
        includes.add(new FilePattern(dir, pattern));
    }
    
    public void addInclude(String dir, String pattern) {
        addInclude(new File(dir), pattern);
    }
    
    public void addIncludes(File dir, String ... patterns) {
        for (String pattern : patterns) {
            addInclude(dir, pattern);
        }
    }
    
    public void addIncludes(String dir, String ... patterns) {
        addIncludes(new File(dir), patterns);
    }
    
    public void addInclude(FilePattern filePattern) {
        includes.add(filePattern);
    }
    
    public void addIncludes(FilePattern ... filePatterns) {
        for (FilePattern filePattern : filePatterns) {
            addInclude(filePattern);
        }
    }
    
    public void addExclude(File dir, String pattern) {
        excludes.add(new FilePattern(dir, pattern));
    }
    
    public void addExclude(String dir, String pattern) {
        addExclude(new File(dir), pattern);
    }
    
    public void addExcludes(File dir, String ... patterns) {
        for (String pattern : patterns) {
            addExclude(dir, pattern);
        }
    }
    
    public void addExcludes(String dir, String...patterns) {
        for (String pattern : patterns) {
            addExclude(dir, pattern);
        }
    }
    
    public void addExclude(FilePattern filePattern) {
        excludes.add(filePattern);
    }
    
    public void addExcludes(FilePattern...filePatterns) {
        for (FilePattern filePattern : filePatterns) {
            addExclude(filePattern);
        }
    }
    
    public static class FilePattern {
        private File targetDirectory;
        private List<FilePatternSection> sections;
        
        public FilePattern(File targetDirectory, FilePatternSection ... sections) {
            this.targetDirectory = targetDirectory;
            this.sections = new ArrayList<FileLister.FilePatternSection>();
        }
        
        public FilePattern(String targetDirectory, FilePatternSection ... sections) {
            this(new File(targetDirectory), sections);
        }
        
        public FilePattern(File targetDirectory, String pattern) {
            this.targetDirectory = targetDirectory;
            this.sections = new ArrayList<FileLister.FilePatternSection>();
            
            String[] strSections = pattern.split("/|\\\\");
            
            for (int i = 0; i < strSections.length; i++) {
                String strSection = strSections[i];
                
                if (strSection.equals("**")) {
                    this.sections.add(new IncludeAllSubDirectoriesPatternSection());
                } else {
                    if (i == strSections.length - 1) {
                        this.sections.add(new FileNamePatternSection(strSection));
                    } else {
                        this.sections.add(new DirectoryNamePatternSection(strSection));
                    }
                }
            }
        }
        
        public FilePattern(String targetDirectory, String pattern) {
            this(new File(targetDirectory, pattern));
        }
        
        public FilePattern(File targetDirectory, Collection<? extends FilePatternSection> sections) {
            this.targetDirectory = targetDirectory;
            this.sections = new ArrayList<FilePatternSection>(sections);
        }
        
        public FilePattern(String targetDirectory, Collection<? extends FilePatternSection> sections) {
            this(new File(targetDirectory), sections);
        }
        
        private List<File> listAll(File file) {
            List<File> results = new ArrayList<File>();
            
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    results.addAll(listAll(f));
                }
            } else {
                results.add(file);
            }
            
            return results;
        }
        
        private List<File> process(File file, int currentIndex) {
            List<File> results = new ArrayList<File>();
            
            if (currentIndex >= sections.size()) {
                return results;
            }
            
            FilePatternSection current = sections.get(currentIndex),
                next = (currentIndex + 1 < sections.size())? sections.get(currentIndex+1) : null;
            
            if (current instanceof IncludeAllSubDirectoriesPatternSection) {
                if (next == null) {
                    results.addAll(listAll(file));
                } else if (next != null) {
                    if (next.accept(file)) {
                        if (file.isDirectory()) {
                            for (File f : file.listFiles()) {
                                List<File> fs = process(f, currentIndex+2);
                                
                                if (fs.size() > 0) {
                                    results.addAll(fs);
                                } else {
                                    results.addAll(process(f, currentIndex));
                                }
                            }
                        } else {
                            results.add(file);
                        }
                    } else {
                        if (file.isDirectory()) {
                            for (File f : file.listFiles()) {
                                results.addAll(process(f, currentIndex));
                            }
                        }
                    }
                }
            } else {
                if (current.accept(file)) {
                    if (file.isDirectory()) {
                        for (File f : file.listFiles()) {
                            results.addAll(process(f, currentIndex+1));
                        }
                    } else {
                        results.add(file);
                    }
                }
            }
            
            return results;
        }
        
        public List<File> listFiles() {
            List<File> results = new ArrayList<File>();
            
            if (targetDirectory.isDirectory()) {
                for (File file : targetDirectory.listFiles()) {
                    results.addAll(process(file, 0));
                }
            }
            
            return results;
        }
    }
    
    public static interface FilePatternSection {
        public boolean accept(File file);
    }
    
    public static class IncludeAllSubDirectoriesPatternSection implements FilePatternSection {
        @Override
        public boolean accept(File file) {
            return true;
        }
        
        @Override
        public String toString() {
            return "IncludeAllSubDirectoriesPatternSection";
        }
    }
    
    public static abstract class NamePatternSection implements FilePatternSection {
        private String wildcardPattern, regex;
        
        public NamePatternSection(String pattern) {
            this.regex = WildcardToRegularExpression.wildcardToRegex(pattern);
            this.wildcardPattern = pattern;
        }
        
        public boolean nameMatch(String name) {
            return Pattern.compile(regex).matcher(name).matches();
        }
        
        public String getWildcardPattern() {
            return wildcardPattern;
        }
        
        public String getRegex() {
            return regex;
        }
    }
    
    public static class DirectoryNamePatternSection extends NamePatternSection {
        public DirectoryNamePatternSection(String pattern) {
            super(pattern);
        }
        
        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return nameMatch(file.getName());
            } else {
                return false;
            }
        }
        
        @Override
        public String toString() {
            return "DirectoryNamePatternSection: " + super.wildcardPattern;
        }
    }
    
    public static class FileNamePatternSection extends NamePatternSection {
        public FileNamePatternSection(String pattern) {
            super(pattern);
        }
        
        @Override
        public boolean accept(File file) {
            if (file.isFile()) {
                return nameMatch(file.getName());
            } else {
                return false;
            }
        }
        
        @Override
        public String toString() {
            return "FileNamePatternSection: " + super.wildcardPattern;
        }
    }
}
