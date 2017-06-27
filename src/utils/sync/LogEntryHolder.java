package utils.sync;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2015-03-25
 * Time: 11:00
 */
public class LogEntryHolder {
    private Map<String, LogEntry> logEntryMap;

    public static LogEntryHolder readFromFile(String filePath) throws IOException {
        return readFromFile(new File(filePath));
    }

    public static LogEntryHolder readFromFile(File file) throws IOException {
        LogEntryHolder hashEntryHolder = new LogEntryHolder();

        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();

            while (line != null) {
                LogEntry entry = new LogEntry(line);

                hashEntryHolder.addLogEntry(entry);

                line = reader.readLine();
            }

            reader.close();
        }

        return hashEntryHolder;
    }

    public LogEntryHolder() {
        this.logEntryMap = new TreeMap<>();
    }

    public void addLogEntry(LogEntry logEntry) {
        logEntryMap.put(logEntry.getPath(), logEntry);
    }

    public boolean checkModified(File file) {
        String absolutePath = file.getAbsolutePath();
        if (logEntryMap.containsKey(absolutePath)) {
            LogEntry logEntry = logEntryMap.get(absolutePath);

            boolean modified = logEntry.isModified(file);

            if (modified) {
                logEntryMap.put(absolutePath, new LogEntry(file));
            }

            return logEntry.isModified(file);
        } else {
            logEntryMap.put(absolutePath, new LogEntry(file));
            return true;
        }
    }

    public void saveToFile(String filePath) throws IOException {
        saveToFile(new File(filePath));
    }

    public void saveToFile(File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        for (String filePath : this.logEntryMap.keySet()) {
            LogEntry logEntry = logEntryMap.get(filePath);

            if (logEntry.isNew() || logEntry.isCompared()) {
                writer.write(logEntry.toString());
                writer.write('\n');
            }
        }

        writer.close();
    }
    
    public String getSaveContent() {
        StringBuilder builder = new StringBuilder();
        
        for (String filePath : this.logEntryMap.keySet()) {
            LogEntry logEntry = logEntryMap.get(filePath);
            
            builder.append(logEntry.toString()).append('\n');
        }
        
        return builder.toString();
    }
    
    public void removeNotComparedEntries() {
        List<String> filePathNotCompared = new ArrayList<>();
        
        for (String filePath : this.logEntryMap.keySet()) {
            LogEntry logEntry = logEntryMap.get(filePath);
            
            if (!logEntry.isNew() && !logEntry.isCompared()) {
                filePathNotCompared.add(filePath);
            }
        }
        
        for (String filePath : filePathNotCompared) {
            this.logEntryMap.remove(filePath);
        }
    }
}
