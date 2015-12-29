package utils.store;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

public class StringStoreObject implements StoreObject {
    public static final Charset UTF8 = Charset.forName("UTF-8");
    
    private String value;
    private byte[] valueBytes;
    
    public StringStoreObject(String value) {
        this.value = value;
        valueBytes = this.value.getBytes(UTF8);
    }
    
    public StringStoreObject(byte[] bytes) {
        this.valueBytes = bytes;
        this.value = new String(this.valueBytes, UTF8);
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
        this.valueBytes = this.value.getBytes(UTF8);
    }

    @Override
    public int compareTo(StoreObject o) {
        if (o == null || !(o instanceof StringStoreObject)) {
            return -1;
        } else {
            return value.compareTo(((StringStoreObject)o).getValue());
        }
    }
    
    @Override
    public long getByteLength() {
        return 4+valueBytes.length;
    }

    @Override
    public int storeCompare(RandomAccessStore<? extends StoreObject> store, long location) throws IOException {
        RandomAccessFile contentFile = store.getUnderlyingContentFile();
        contentFile.seek(location);
        byte[] bytes = new byte[contentFile.readInt()];
        contentFile.readFully(bytes);
        
        return value.compareTo(new String(bytes, UTF8));
    }

    @Override
    public void refreshRead(RandomAccessStore<? extends StoreObject> store, long location) throws IOException {
        RandomAccessFile contentFile = store.getUnderlyingContentFile();
        
        contentFile.seek(location);
        this.valueBytes = new byte[contentFile.readInt()];
        contentFile.readFully(this.valueBytes);
        
        this.value = new String(this.valueBytes, UTF8);
    }
    
    @Override
    public void updateWrite(RandomAccessStore<? extends StoreObject> store, long location) throws IOException {
        write(store, location);
    }
    
    @Override
    public void write(RandomAccessStore<? extends StoreObject> store, long location) throws IOException {
        RandomAccessFile contentFile = store.getUnderlyingContentFile();
        
        contentFile.seek(location);
        
        byte[] bytes = value.getBytes(UTF8);
        
        contentFile.writeInt(bytes.length);
        contentFile.write(bytes);
    }
    
    public static StringStoreObject read(RandomAccessStore<StringStoreObject> store, long location) throws IOException {
        RandomAccessFile contentFile = store.getUnderlyingContentFile();
        contentFile.seek(location);
        byte[] bytes = new byte[contentFile.readInt()];
        contentFile.readFully(bytes);
        return new StringStoreObject(new String(bytes, UTF8));
    }
}
