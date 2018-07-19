package utils.listener;

import javax.swing.text.JTextComponent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SelectAllListener implements FocusListener {
    private JTextComponent tf;
    
    public SelectAllListener(JTextComponent tf) {
        this.tf = tf;
    }
    
    @Override
    public void focusGained(FocusEvent evt) {
        tf.selectAll();
    }
    
    @Override
    public void focusLost(FocusEvent evt) {
        // Ignored
    }
}