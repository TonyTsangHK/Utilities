package utils.data;

public interface ValueHolder<T> {
    public abstract T getValue();
    public abstract void setValue(T o);
}
