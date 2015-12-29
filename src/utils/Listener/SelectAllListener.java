package utils.listener;

import java.awt.event.*;

import javax.swing.text.*;

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