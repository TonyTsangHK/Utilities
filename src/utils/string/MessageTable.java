package utils.string;

import java.util.HashMap;
import java.util.Map;

public class MessageTable {
    private Map<String, Message> messageTable;
    
    public MessageTable() {
        messageTable = new HashMap<String, Message>();
    }
    
    public MessageTable addMessage(String name, Message msg) {
        messageTable.put(name, msg);
        return this;
    }
    
    public MessageTable removeMessage(String name) {
        messageTable.remove(name);
        return this;
    }
    
    public Message getMessage(String name) {
        return messageTable.get(name);
    }
    
    public void clearMessage() {
        messageTable.clear();
    }
}
