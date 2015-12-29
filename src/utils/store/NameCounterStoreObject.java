package utils.store;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

public class NameCounterStoreObject implements StoreObject {
    public static final Charset UTF8 = Charset.forName("UTF-8");
    
    private String name;
    private int counter;
    
    private byte[] nameBytes = null;

    public NameCounterStoreObject(String name, int counter) {
        this.name = name;
        this.counter = counter;
        this.nameBytes = this.name.getBytes(UTF8);
    }
    
    public String getName() {
        return name;
    }
    
    public void setCounter(int count) {
        if (count > 0) {
            counter = count;
        }
    }
    
    public void incrementCounter() {
        counter++;
    }
    
    public void decrementCounter() {
        if (counter > 0) {
            counter--;
        }
    }
    
    public int getCounter() {
        return counter;
    }
    
    @Override
    public int compareTo(StoreObject o) {
        if (o == null || !(o instanceof NameCounterStoreObject)) {
            return -1;
        } else {
            NameCounterStoreObject obj = (NameCounterStoreObject) o;
            return name.compareTo(obj.name);
        }
    }
    
    @Override
    public int storeCompare(RandomAccessStore<? extends StoreObject> store, long location) throws IOException {
        RandomAccessFile contentFile = seekTo(store, location);
        
        byte[] bytes = new byte[contentFile.readInt()];
        contentFile.readFully(bytes);
        
        return name.compareTo(new String(bytes, UTF8));
    }
    
    @Override
    public void refreshRead(RandomAccessStore<? extends StoreObject> store, long location) throws IOException {
        RandomAccessFile contentFile = store.getUnderlyingContentFile();
        contentFile.seek(location);
        
        this.nameBytes = new byte[contentFile.readInt()];
        contentFile.readFully(this.nameBytes);
        
        this.name = new String(this.nameBytes, UTF8);
        this.counter = contentFile.readInt();
    }

    @Override
    public void updateWrite(RandomAccessStore<? extends StoreObject> store, long location) throws IOException {
        RandomAccessFile contentFile = seekTo(store, location+4+nameBytes.length);
        
        contentFile.writeInt(counter);
    }

    @Override
    public void write(RandomAccessStore<? extends StoreObject> store, long location) throws IOException {
        RandomAccessFile contentFile = seekTo(store, location);
        
        contentFile.writeInt(nameBytes.length);
        contentFile.write(nameBytes);
        contentFile.writeInt(counter);
    }
    
    @Override
    public long getByteLength() {
        return nameBytes.length + 8;
    }
    
    private static RandomAccessFile seekTo(RandomAccessStore<? extends StoreObject> store, long location) throws IOException {
        RandomAccessFile contentFile = store.getUnderlyingContentFile();
        contentFile.seek(location);
        
        return contentFile;
    }
    
    public static NameCounterStoreObject read(
            RandomAccessStore<? extends NameCounterStoreObject> store, long location
    ) throws IOException {
        RandomAccessFile contentFile = seekTo(store, location);
        byte[] bytes = new byte[contentFile.readInt()];
        contentFile.readFully(bytes);
        return new NameCounterStoreObject(new String(bytes, UTF8), contentFile.readInt());
    }
}
