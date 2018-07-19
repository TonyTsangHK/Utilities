package utils.listener;

import utils.event.ValueChangeEvent;

import java.util.EventListener;

public interface ValueChangeListener<T> extends EventListener {
    public void valueChanged(ValueChangeEvent<T> evt);
}