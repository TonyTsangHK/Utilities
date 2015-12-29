package utils.listener;

import java.awt.event.*;

public class MouseListenerWrapper implements MouseListener {
    private MouseListener listener;
    
    public MouseListenerWrapper() {
        this(null);
    }
    
    public MouseListenerWrapper(MouseListener l) {
        setMouseListener(l);
    }
    
    public void setMouseListener(MouseListener l) {
        listener = l;
    }
    
    public void mouseClicked(MouseEvent evt) {
        if (listener != null) {
            listener.mouseClicked(evt);
        }
    }
    
    public void mousePressed(MouseEvent evt) {
        if (listener != null) {
            listener.mousePressed(evt);
        }
    }
    
    public void mouseReleased(MouseEvent evt) {
        if (listener != null) {
            listener.mouseReleased(evt);
        }
    }
    
    public void mouseEntered(MouseEvent evt) {
        if (listener != null) {
            listener.mouseEntered(evt);
        }
    }
    
    public void mouseExited(MouseEvent evt) {
        if (listener != null) {
            listener.mouseExited(evt);
        }
    }
}