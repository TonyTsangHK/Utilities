package utils.store;

import java.io.IOException;

public interface StoreObject extends Comparable<StoreObject> {
    public abstract int storeCompare(RandomAccessStore<? extends StoreObject> store, long location) throws IOException;
    public abstract void refreshRead(RandomAccessStore<? extends StoreObject> store, long location) throws IOException;
    public abstract void updateWrite(RandomAccessStore <? extends StoreObject> store, long location) throws IOException;
    public abstract void write(RandomAccessStore<? extends StoreObject> store, long location) throws IOException;
    public abstract long getByteLength();
}
