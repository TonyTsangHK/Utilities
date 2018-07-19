package utils.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMotionListenerWrapper implements MouseMotionListener {
    private MouseMotionListener listener;
    
    public MouseMotionListenerWrapper() {
        this(null);
    }
    
    public MouseMotionListenerWrapper(MouseMotionListener l) {
        setListener(l);
    }
    
    public void setListener(MouseMotionListener l) {
        listener = l;
    }
    
    public void mouseMoved(MouseEvent evt) {
        if (listener != null) {
            listener.mouseMoved(evt);
        }
    }
    
    public void mouseDragged(MouseEvent evt) {
        if (listener != null) {
            listener.mouseDragged(evt);
        }
    }
}
