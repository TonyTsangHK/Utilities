package utils.stream;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2012-06-05
 * Time: 11:45
 */
public class CharacterStreamHandler {
    public static final char EOT = (char)4;
    public static final int CACHE_SIZE = 4096;
    private Reader reader;
    private char[] cache, nextCache;
    private int offset, available, nextAvailable;
    private boolean endReached = false;

    /**
     * Initialize stream handler on provided input stream
     *
     * @param in input stream
     *
     * @throws IOException
     */
    public CharacterStreamHandler(InputStream in) throws IOException {
        this(in, CACHE_SIZE);
    }

    /**
     * Initialize stream handler on provided input stream and cache size
     *
     * @param in input stream
     * @param cacheSize read ahead cache size
     *
     * @throws IOException
     */
    public CharacterStreamHandler(InputStream in, int cacheSize) throws IOException {
        this(in, cacheSize, "UTF-8");
    }

    /**
     * Initialize stream handler on provided input stream and charset
     *
     * @param in input stream
     * @param charset input stream charset
     *
     * @throws IOException
     */
    public CharacterStreamHandler(InputStream in, String charset) throws IOException {
        this(in, CACHE_SIZE, charset);
    }

    /**
     * Initialize stream handler on provided input stream, cache size and charset
     *
     * @param in input stream
     * @param cacheSize read ahead cache size
     * @param charset input stream charset
     *
     * @throws IOException
     */
    public CharacterStreamHandler(InputStream in, int cacheSize, String charset) throws IOException {
        this.reader = new InputStreamReader(in, charset);
        this.cache = new char[CACHE_SIZE];
        this.nextCache = new char[CACHE_SIZE];

        available = this.reader.read(this.cache);
        offset = 0;
        if (available <= 0) {
            endReached = true;
        }
    }

    /**
     * Initialize stream handler on provided reader
     *
     * @param reader character reader
     *
     * @throws IOException
     */
    public CharacterStreamHandler(Reader reader) throws IOException {
        this(reader, CACHE_SIZE);
    }

    /**
     * Initialize stream handler on provided reader and cache size
     * @param reader character reader
     * @param cacheSize read ahead cache size
     * @throws IOException
     */
    public CharacterStreamHandler(Reader reader, int cacheSize) throws IOException {
        this.reader = reader;
        this.cache = new char[cacheSize];
        this.nextCache = new char[cacheSize];

        available = this.reader.read(this.cache);
        offset = 0;
        if (available <= 0) {
            endReached = true;
        }
    }

    /**
     * Initialize stream handler on target string
     *
     * @param target target string
     */
    public CharacterStreamHandler(String target) {
        this(target, CACHE_SIZE);
    }

    /**
     * Initialize stream handler on target string with provided cache size
     *
     * @param target target string
     * @param cacheSize read ahead cache size
     */
    public CharacterStreamHandler(String target, int cacheSize) {
        try {
            this.reader = new StringReader(target);
            this.cache = new char[cacheSize];
            this.nextCache = new char[cacheSize];

            available = this.reader.read(this.cache);
            offset = 0;
            if (available <= 0) {
                endReached = true;
            }
        } catch (IOException iox) {
            // Don't expect any exception here
        }
    }

    /**
     * Read next available cache
     *
     * @throws IOException
     */
    private void readNext() throws IOException {
        if (!endReached && nextAvailable == 0) {
            nextAvailable = reader.read(nextCache);
            if (nextAvailable == -1) {
                endReached = true;
            }
        }
    }

    /**
     * Swap next available cache as current cache.
     * Programmatically current cache will become next available cache but with next available set to 0.
     *
     */
    private void swapCache() {
        char[] t = cache;
        cache = nextCache;
        nextCache = t;
        available = nextAvailable;
        offset = 0;
        nextAvailable = 0;
    }

    /**
     * Discard current cache and shift to next available cache, similar to swap, this will read also the next available cache
     *
     * @throws IOException
     */
    private void shiftNext() throws IOException {
        if (nextAvailable > 0) {
            swapCache();
        } else {
            if (!endReached) {
                nextAvailable = this.reader.read(nextCache);
                if (nextAvailable == -1) {
                    offset = available;
                    endReached = true;
                    nextAvailable = 0;
                } else {
                    swapCache();
                }
            }
        }
    }

    /**
     * Skip the next available characters if it matches the pattern
     *
     * @param pattern pattern to skip
     * @param repeat repeat flag
     *
     * @return length of characters skipped
     *
     * @throws IOException
     */
    public long skipPattern(String pattern, boolean repeat) throws IOException {
        long skipped = 0;

        if (repeat) {
            while (checkPattern(pattern)) {
                skip(pattern.length());
                skipped += pattern.length();
            }
        } else {
            if (checkPattern(pattern)) {
                skip(pattern.length());
                skipped += pattern.length();
            }
        }

        return skipped;
    }

    /**
     * Skip the next available characters if it matches the pattern
     *
     * @param pattern pattern to skip
     * @param repeat repeat flag
     *
     * @return length of characters skipped
     *
     * @throws IOException
     */
    public long skipPattern(char[] pattern, boolean repeat) throws IOException {
        long skipped = 0;

        if (repeat) {
            while (checkPattern(pattern)) {
                skip(pattern.length);
                skipped += pattern.length;
            }
        } else {
            if (checkPattern(pattern)) {
                skip(pattern.length);
                skipped += pattern.length;
            }
        }

        return skipped;
    }

    /**
     * Skip the next character if it match the input character
     *
     * @param ch character to skip
     * @param repeat repeat flag
     *
     * @return length of characters skipped
     *
     * @throws IOException
     */
    public long skipChar(char ch, boolean repeat) throws IOException {
        long skipped = 0;

        if (repeat) {
            while (peek() == ch) {
                skip(1);
                skipped++;
            }
        } else {
            if (peek() == ch) {
                skip(1);
                skipped++;
            }
        }

        return skipped;
    }

    /**
     * Skip the specified length of character, stop if end reached
     *
     * @param len length of character to skip
     *
     * @return length of character skipped
     *
     * @throws IOException
     */
    public long skip(long len) throws IOException {
        if (len < 0) {
            throw new IOException("Cannot skip backward: " + len);
        } else if (len == 0) {
            return 0;
        } else {
            if (offset+len < available) {
                offset += len;
                return len;
            } else {
                long skipped = available - offset;
                offset = available;
                len -= skipped;
                while (len > 0) {
                    shiftNext();
                    int l = available - offset;

                    if (l == 0) {
                        return skipped;
                    } else {
                        if (len <= l) {
                            offset = (int) len;
                            return skipped + len;
                        } else {
                            skipped += l;
                            len -= l;
                            offset = available;
                        }
                    }
                }
                return skipped;
            }
        }
    }

    /**
     * Read next available character, EOT if end reached
     *
     * @return next available character, EOT if end reached
     *
     * @throws IOException
     */
    public char read() throws IOException {
        if (offset == available) {
            shiftNext();
        }
        if (offset < available) {
            return cache[offset++];
        } else {
            return EOT;
        }
    }

    /**
     * Read next available characters with specified length
     *
     * @param len length of character to read
     *
     * @return characters read in string
     *
     * @throws IOException
     */
    public String read(int len) throws IOException {
        StringBuilder builder = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            if (hasNext()) {
                builder.append(read());
            }
        }

        return builder.toString();
    }

    /**
     * Peek character at advanced position
     *
     * @param advancedPos advanced position, negative & zero mean the character already read (within cache)
     * @return character at advanced position
     *
     * @throws IOException
     */
    public char peek(int advancedPos) throws IOException {
        if (advancedPos <= 0) {
            if (offset+advancedPos-1 >= 0) {
                return cache[offset+advancedPos-1];
            } else {
                throw new IOException("Peek exceed cache: " + advancedPos);
            }
        } else {
            if (offset+advancedPos-1 < available) {
                return cache[offset+advancedPos-1];
            } else {
                if (nextAvailable == 0) {
                    readNext();
                }
                int noffset = advancedPos-available+offset-1;
                if (noffset < nextAvailable) {
                    return nextCache[noffset];
                } else {
                    if (endReached) {
                        return EOT;
                    } else {
                        throw new IOException("Peek exceed cache.");
                    }
                }
            }
        }
    }

    /**
     * Peek next available character, EOT if end reached
     *
     * @return next available character, EOT if end reached
     *
     * @throws IOException
     */
    public char peek() throws IOException {
        if (offset < available) {
            return cache[offset];
        } else if (offset == available) {
            readNext();
            if (nextAvailable > 0) {
                return nextCache[0];
            } else {
                return EOT;
            }
        } else {
            return EOT;
        }
    }

    /**
     * Check if next available character exists
     *
     * @return next available character existence
     *
     * @throws IOException
     */
    public boolean hasNext() throws IOException {
        if (offset < available || nextAvailable > 0) {
            return true;
        } else {
            readNext();
            return nextAvailable > 0;
        }
    }

    /**
     * Check if next available characters exists with specified length
     *
     * @param len next available character length
     *
     * @return next available character existence
     *
     * @throws IOException
     */
    public boolean hasNext(int len) throws IOException {
        if (offset+len-1 < available) {
            return true;
        } else {
            if (nextAvailable == 0) {
                readNext();
            }
            int noffset = len-available+offset-1;
            if (noffset < nextAvailable) {
                return true;
            } else {
                if (endReached) {
                    return false;
                } else {
                    throw new IOException("Peek exceed cache.");
                }
            }
        }
    }

    /**
     * Check next available characters with the specified pattern
     *
     * @param pattern pattern to check
     *
     * @return pattern existence
     *
     * @throws IOException
     */
    public boolean checkPattern(String pattern) throws IOException {
        for (int i = 0; i < pattern.length(); i++) {
            if (peek(i+1) != pattern.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check next available characters with the specified pattern
     *
     * @param pattern pattern to check
     *
     * @return pattern existence
     *
     * @throws IOException
     */
    public boolean checkPattern(char[] pattern) throws IOException {
        for (int i = 0; i < pattern.length; i++) {
            if (peek(i+1) != pattern[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Continue to read util specified character is read
     *
     * @param ch character to stop reading
     *
     * @return Characters read before stop
     *
     * @throws IOException
     */
    public String readUntil(char ch) throws IOException {
        return readUntil(ch, new StringBuilder()).toString();
    }

    /**
     * Continue to read util specified character pattern is read
     *
     * @param chars character pattern to stop reading
     *
     * @return Characters read before stop
     *
     * @throws IOException
     */
    public String readUntil(char ... chars) throws IOException {
        return readUntil(new StringBuilder(), chars).toString();
    }

    /**
     * Continue to read util specified character is read, result will be stored in provided StringBuilder
     *
     * @param ch character to stop reading
     * @param builder builder to hold result
     *
     * @return the provided StringBuilder, for method chaining
     *
     * @throws IOException
     */
    public StringBuilder readUntil(char ch, StringBuilder builder) throws IOException {
        while (hasNext()) {
            if (peek() == ch) {
                break;
            } else {
                builder.append(read());
            }
        }

        return builder;
    }

    /**
     * Continue to read util specified characters pattern is read, result will be stored in provided StringBuilder
     *
     * @param builder builder to hold result
     * @param chars character pattern to stop reading
     *
     * @return the provided StringBuilder, for method chaining
     *
     * @throws IOException
     */
    public StringBuilder readUntil(StringBuilder builder, char ... chars) throws IOException {
        while (hasNext()) {
            char c = peek();
            boolean matched = false;

            for (char ch : chars) {
                if (c == ch) {
                    matched = true;
                    break;
                }
            }

            if (matched) {
                break;
            } else {
                builder.append(read());
            }
        }

        return builder;
    }

    /**
     * Continue reading util specified pattern is read
     *
     * @param pattern character pattern to stop reading
     *
     * @return Characters read before stop
     *
     * @throws IOException
     */
    public String readUntil(String pattern) throws IOException {
        return readUntil(pattern, new StringBuilder()).toString();
    }

    /**
     * Continue reading util specified pattern is read, result will be stored in provided StringBuilder
     *
     * @param pattern Character pattern to stop reading
     * @param builder build to hold result
     *
     * @return the provided builder, for method chaining
     *
     * @throws IOException
     */
    public StringBuilder readUntil(String pattern, StringBuilder builder) throws IOException {
        char[] patternChars = pattern.toCharArray();

        while (hasNext()) {
            if (checkPattern(patternChars)) {
                break;
            } else {
                builder.append(read());
            }
        }

        return builder;
    }

    /**
     * Close this handling's reader, hide exception
     */
    public void close() {
        try {
            reader.close();
        } catch (IOException iox) {
            // Ignored
        }
    }
}
