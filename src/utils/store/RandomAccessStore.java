package utils.store;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

public class RandomAccessStore <E extends StoreObject> {
    public static final int BLOCK_SIZE = 4096;
    
    private final Class<E> clz;
    
    private RandomAccessFile indexFile, contentFile;
    private FileLock indexLock, contentLock;
    
    private int size;
    private long length;
    
    public RandomAccessStore(Class<E> clz, String indexPath, String contentPath) throws IOException{
        this(clz, new File(indexPath), new File(contentPath), false, true);
    }
    
    public RandomAccessStore(Class<E> clz, String indexPath, String contentPath, boolean lock, boolean exclusive) throws IOException {
        this(clz, new File(indexPath), new File(contentPath), lock, exclusive);
    }
    
    public RandomAccessStore(Class<E> clz, File indexFile, File contentFile) throws IOException {
        this(clz, indexFile, contentFile, false, true);
    }
    
    public RandomAccessStore(
            Class<E> clz, File indexFile, File contentFile, boolean lock, boolean exclusive
    ) throws IOException {
        this.clz = clz;
        
        this.indexFile = new RandomAccessFile(indexFile, "rw");
        this.contentFile = new RandomAccessFile(contentFile, "rw");
        
        if (lock) {
            lock(exclusive);
        }
        
        length = this.contentFile.length();
        if (length == 0) {
            size = 0;
        } else {
            size = (int)(indexFile.length() / 8);
        }
    }
    
    public FileLock[] lock(boolean exclusive) throws IOException {
        if (indexLock == null && contentLock == null) {
            return new FileLock[]{
                    indexFile.getChannel().lock(0L, Long.MAX_VALUE, !exclusive),
                    contentFile.getChannel().lock(0L, Long.MAX_VALUE, !exclusive)
            };
        } else {
            return new FileLock[]{indexLock, contentLock};
        }
    }
    
    public void release() throws IOException {
        if (indexLock != null) {
            indexLock.release();
            indexLock = null;
        }
        if (contentLock != null) {
            contentLock.release();
            contentLock = null;
        }
    }
    
    private long getActualLocation(int index) throws IOException {
        if (index >= 0 && index < size) {
            indexFile.seek(index*8);
            return indexFile.readLong();
        } else {
            return -1;
        }
    }
    
    public int indexOf(E obj) throws IOException {
        return indexOf(obj, 0);
    }
    
    public boolean contains(E obj) throws IOException {
        return indexOf(obj) > -1;
    }
    
    public int indexOf(E obj, int offset) throws IOException {
        if (offset >= 0 && offset < size) {
            return indexOf(obj, offset, size-1).index;
        } else {
            return -1;
        }
    }
    
    private Index indexOf(E obj, int s, int e) throws IOException {
        if (size == 0) {
            return new Index(-1, 0);
        }
        
        int m = (s + e) / 2;
        
        int cmp = obj.storeCompare(this, getActualLocation(m));
        
        if (cmp == 0) {
            return new Index(m, m);
        } else if (cmp < 0) {
            if (s == m) {
                return new Index(-1, m);
            } else {
                return indexOf(obj, s, m-1);
            }
        } else {
            if (e == m) {
                return new Index(-1, m+1);
            } else {
                return indexOf(obj, m+1, e);
            }
        }
        
    }
    
    protected RandomAccessFile getUnderlyingIndexFile() {
        return indexFile;
    }
    
    protected RandomAccessFile getUnderlyingContentFile() {
        return contentFile;
    }
    
    @SuppressWarnings("unchecked")
    public List<E> toList() throws IOException {
        List<E> list = new ArrayList<E>(size);
        
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                try {
                    list.add(
                            (E)clz.getDeclaredMethod("read", RandomAccessStore.class, long.class)
                                    .invoke(null, this, getActualLocation(i))
                    );
                } catch (IllegalAccessException iae) {
                    iae.printStackTrace();
                    // Not expected to happend!!
                } catch (NoSuchMethodException nsme) {
                    nsme.printStackTrace();
                    // Not expected to happend!!
                } catch (InvocationTargetException ite) {
                    ite.printStackTrace();
                    // Not expected to happend!!
                }
            }
        }
        
        return list;
    }
    
    public int getCount() {
        return size;
    }
    
    public long getStorageSize() {
        return length + size * 8;
    }
    
    public String getReadableSize() {
        String[] byteTexts = {"B", "KB", "MB", "GB", "TB"};
        
        int c = 4;
        long l = 1099511627776l, storageSize = getStorageSize();
        
        while (l > storageSize && c > 0) {
            l /= 1024;
            c--;
        }
        
        String unit = byteTexts[c];
        
        BigDecimal result = new BigDecimal(storageSize).divide(new BigDecimal(l));
        
        return result.setScale(2, RoundingMode.HALF_UP).toString() + unit;
    }
    
    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index >= 0 && index < size) {
            try {
                return (E) clz.getDeclaredMethod("read", RandomAccessStore.class, long.class)
                                .invoke(null, this, getActualLocation(index));
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    private void insertToIndex(int index, E obj) throws IOException {
        if (index < 0) {
            index = 0;
        } else if (index > size) {
            index = size;
        }
        
        obj.write(this, length);
        
        if (index == size) {
            indexFile.writeLong(length);
        } else {
            long loc = 8*index;
            shiftBytes(indexFile, loc, indexFile.length() - loc, 8, BLOCK_SIZE);
            indexFile.seek(loc);
            indexFile.writeLong(length);
        }
        
        size += 1;
        length += obj.getByteLength();
    }
    
    public void save(E obj) throws IOException {
        Index index = indexOf(obj, 0, size-1);
        
        if (index.index == -1) {
            if (size == 0) {
                indexFile.seek(0);
                indexFile.writeLong(0l);
                obj.write(this, 0l);
                
                length = obj.getByteLength();
                size = 1;
            } else {
                int insertionIndex = index.insertionIndex;
                
                insertToIndex(insertionIndex, obj);
            }
        } else {
            long len = obj.getByteLength(), s1 = -1, s2 = -1, expectedLen = -1;
            
            if (index.index == size-1) {
                s1 = getActualLocation(index.index);
                expectedLen = length - s1;
            } else {
                indexFile.seek(8*index.index);
                
                s1 = indexFile.readLong();
                s2 = indexFile.readLong();
                
                expectedLen = s2 - s1;
            }
            
            if (len == expectedLen) {
                obj.updateWrite(this, getActualLocation(index.index));
            } else {
                if (s2 == -1) {
                    obj.updateWrite(this, s1);
                    
                    length += len - expectedLen;
                } else {
                    long diff = len - expectedLen;
                    if (diff > 0) {
                        shiftBytes(contentFile, s2, length-s2, len-expectedLen, BLOCK_SIZE);
                        obj.updateWrite(this, s1);
                    } else {
                        obj.updateWrite(this, s1);
                        shiftBytes(contentFile, s2, length-s2, len-expectedLen, BLOCK_SIZE);
                    }
                    
                    if (index.index < size-1) {
                        indexFile.seek(8*index.index+8);
                        long[] newLocs = new long[size-index.index-1];
                        for (int i = 0; i < newLocs.length; i++) {
                            newLocs[i] = indexFile.readLong() + diff;
                        }
                        indexFile.seek(8*index.index+8);
                        for (int i = 0; i < newLocs.length; i++) {
                            indexFile.writeLong(newLocs[i]);
                        }
                    }
                    
                    length += diff;
                    if (len < expectedLen) {
                        contentFile.setLength(length);
                    }
                }
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        File indexFile = new File("C:/temp/store.idx"), contentFile = new File("C:/temp/store.ras");
        
        final RandomAccessStore<NameCounterStoreObject> 
            store = new RandomAccessStore<NameCounterStoreObject>(NameCounterStoreObject.class, indexFile, contentFile);
        
        NameCounterStoreObject o = new NameCounterStoreObject("A", 0);
        store.save(o);
        
        new Thread() {
            @Override
            public void run() {
                NameCounterStoreObject o = new NameCounterStoreObject("A", 0);
                
                try {
                    for (int i = 0; i < 100; i++) {
                        store.lock(true);
                        System.out.println(i + "(1)");
                        int c = store.indexOf(o);
                        if (c != -1) {
                            o.refreshRead(store, store.getActualLocation(c));
                            o.incrementCounter();
                            store.save(o);
                        }
                        store.release();
                    }
                } catch (IOException iox) {
                    iox.printStackTrace();
                }
                
                System.out.println("1: " + o.getCounter());
            }
        }.start();
        
        new Thread() {
            @Override
            public void run() {
                NameCounterStoreObject o = new NameCounterStoreObject("A", 0);
                
                try {
                    for (int i = 0; i < 100; i++) {
                        store.lock(true);
                        System.out.println(i + "(2)");
                        int c = store.indexOf(o);
                        if (c != -1) {
                            o.refreshRead(store, store.getActualLocation(c));
                            o.incrementCounter();
                            store.save(o);
                        }
                        store.release();
                    }
                } catch (IOException iox) {
                    iox.printStackTrace();
                }
                
                System.out.println("2: " + o.getCounter());
            }
        }.start();
    }
    
    public void remove(E obj) throws IOException {
        remove(indexOf(obj));
    }
    
    public void remove(int index) throws IOException {
        if (index >= 0 && index < size) {
            if (index < size-1) {
                long loc = index * 8;
                shiftBytes(indexFile, loc+8, indexFile.length()-loc-8, -8, BLOCK_SIZE);
            }
            
            indexFile.setLength((size-1)*8);
            size-=1;
        }
    }
    
    public void close() throws IOException {
        indexFile.close();
        contentFile.close();
    }
    
    public static void shiftBytes(
            RandomAccessFile randomAccessFile, long start, long len, long shift, int blockSize
    ) throws IOException {
        // Validate parameters, stop process for any invalid parameter
        long length = randomAccessFile.length();
        if (len <= 0 || shift == 0|| start < 0 || start >= length || start + len > length || start + shift < 0) {
            return;
        }
        
        byte[] block = new byte[blockSize];
        
        if (shift < 0) {
            long curr = start;
            
            while (len > 0) {
                randomAccessFile.seek(curr);
                if (len > blockSize) {
                    randomAccessFile.readFully(block);
                    randomAccessFile.seek(curr+shift);
                    randomAccessFile.write(block);
                    curr += blockSize;
                    len -= blockSize;
                } else {
                    randomAccessFile.readFully(block, 0, (int)len);
                    randomAccessFile.seek(curr+shift);
                    randomAccessFile.write(block, 0, (int)len);
                    break;
                }
            }
        } else if (shift > 0) {
            long curr = start + len - 1;
            
            while (len > 0) {
                if (len > blockSize) {
                    randomAccessFile.seek(curr-blockSize+1);
                    randomAccessFile.readFully(block);
                    randomAccessFile.seek(curr-blockSize+1+shift);
                    randomAccessFile.write(block);
                    curr -= blockSize;
                    len -= blockSize;
                } else {
                    randomAccessFile.seek(start);
                    randomAccessFile.readFully(block, 0, (int)len);
                    randomAccessFile.seek(start+shift);
                    randomAccessFile.write(block, 0, (int)len);
                    break;
                }
            }
        }
    }
    
    private static class Index {
        public final int index, insertionIndex;
        
        public Index(int index, int insertionIndex) {
            this.index = index;
            this.insertionIndex = insertionIndex;
        }
    }
}
