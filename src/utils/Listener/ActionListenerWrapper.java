package utils.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ActionListenerWrapper implements ActionListener {
    private List<ActionListener> listeners;
    
    public ActionListenerWrapper() {
        listeners = new ArrayList<ActionListener>();
    }
    
    public ActionListenerWrapper(ActionListener l) {
        this();
        addActionListener(l);
    }
    
    public ActionListener removeActionListener(int i) {
        if (i >= 0 && i < listeners.size()) {
            return listeners.remove(i);
        } else {
            return null;
        }
    }
    
    public boolean removeActionListener(ActionListener l) {
        return listeners.remove(l);
    }
    
    public void addActionListener(ActionListener l) {
        if (l != null) {
            listeners.add(l);
        }
    }
    
    public void setActionListener(ActionListener l) {
        listeners.clear();
        if (l != null) {
            listeners.add(l);
        }
    }
    
    public void clearActionListeners() {
        listeners.clear();
    }
    
    public List<ActionListener> getListeners() {
        return listeners;
    }
    
    public void actionPerformed(ActionEvent evt) {
        for (ActionListener l : listeners) {
            l.actionPerformed(evt);
        }
    }
}