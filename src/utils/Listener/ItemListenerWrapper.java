package utils.listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class ItemListenerWrapper implements ItemListener {
    private List<ItemListener> listeners;
    
    public ItemListenerWrapper() {
        listeners = new ArrayList<ItemListener>();
    }
    
    public ItemListenerWrapper(ItemListener l) {
        this();
        addItemListener(l);
    }
    
    public ItemListener removeItemListener(int index) {
        if (index >= 0 && index < listeners.size()) {
            return listeners.remove(index);
        } else {
            return null;
        }
    }
    
    public boolean removeItemListener(ItemListener l) {
        return listeners.remove(l);
    }
    
    public void addItemListener(ItemListener l) {
        if (l != null) {
            listeners.add(l);
        }
    }
    
    public void setItemListener(ItemListener l) {
        listeners.clear();
        if (l != null) {
            listeners.add(l);
        }
    }
    
    public void clearItemListeners() {
        listeners.clear();
    }
    
    public List<ItemListener> getItemListeners() {
        return listeners;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        for (ItemListener l : listeners) {
            l.itemStateChanged(e);
        }
    }
}