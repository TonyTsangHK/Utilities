package utils.listener;

import java.util.*;

import utils.event.*;

public interface ValueChangeListener<T> extends EventListener {
    public void valueChanged(ValueChangeEvent<T> evt);
}