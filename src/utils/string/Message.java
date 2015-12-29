package utils.string;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private Map<String, String> messages;
    
    public Message() {
        messages = new HashMap<String, String>();
    }
    
    public Message addLanguageMessage(String lang, String msg) {
        messages.put(lang, msg);
        return this;
    }
    
    public Message removeLanguageMessage(String lang) {
        messages.remove(lang);
        return this;
    }
    
    public String getMessage(String lang) {
        return messages.get(lang);
    }
    
    public void clear() {
        messages.clear();
    }
}
