package utils.controller;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UndoController implements ActionListener {
    private JButton undoButton, redoButton;
    private UndoManager undoManager;
    
    public UndoController(JButton ub, JButton rb, UndoManager um) {
        this.undoButton = ub;
        this.redoButton = rb;
        this.undoManager = um;
        
        undoButton.addActionListener(this);
        redoButton.addActionListener(this);
        refreshUndoButtons();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source == undoButton) {
            undoManager.undo();
            refreshUndoButtons();
        } else if (source == redoButton) {
            undoManager.redo();
            refreshUndoButtons();
        }
    }
    
    public void notifyUndoableEditHappended(UndoableEdit edit) {
        undoManager.undoableEditHappened(new UndoableEditEvent(this, edit));
        refreshUndoButtons();
    }
    
    public void refreshUndoButtons() {
        undoButton.setEnabled(undoManager.canUndo());
        redoButton.setEnabled(undoManager.canRedo());
    }
    
    public void discardAllEdits() {
        undoManager.discardAllEdits();
        refreshUndoButtons();
    }
}
