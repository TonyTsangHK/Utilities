package utils.string;

import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import utils.constants.HorizontalAlignment;

public class MessageBlockHolder {
    private List<MessageBlock> blocks;
    private List<MessageBlockDecoration> decorations;
    
    private int width = 0, height = 0;
    
    private char spacingCharacter = ' ';
    
    private MessageBlockSortComparator sortComparator = new MessageBlockSortComparator();
    
    private HorizontalAlignment defaultHorizontalAlignment = HorizontalAlignment.CENTER;
    
    public MessageBlockHolder() {
        width = height = 0;
        blocks = new ArrayList<MessageBlock>();
        decorations = new ArrayList<MessageBlockDecoration>();
    }
    
    public void addMessage(String message, int x, int y) {
        addMessage(new AttributedString(message), x, y, 1, defaultHorizontalAlignment);
    }
    
    public void addMessage(String message, int x, int y, int w) {
        addMessage(new AttributedString(message), x, y, w, defaultHorizontalAlignment);
    }
    
    public void addMessage(String message, int x, int y, int w, HorizontalAlignment hAlign) {
        addMessage(new AttributedString(message), x, y, w, hAlign);
    }
    
    public void addMessage(AttributedString message, int x, int y) {
        addMessage(message, x, y, 1, defaultHorizontalAlignment);
    }
    
    public void addMessage(AttributedString message, int x, int y, int w) {
        addMessage(message, x, y, w, defaultHorizontalAlignment);
    }
    
    public void addMessage(AttributedString message, int x, int y, int w, HorizontalAlignment hAlign) {
        addMessageBlock(new MessageBlock(message, x, y, w, hAlign));
    }
    
    public void addRowMessages(int rowIndex, String ... messages) {
        addRowMessages(rowIndex, 1, 1, defaultHorizontalAlignment, messages);
    }
    
    public void addRowMessages(
            int rowIndex, int startColumnIndex, int width, HorizontalAlignment hAlign, String ... messages
    ) {
        for (int i = 0; i < messages.length; i++) {
            addMessageBlock(new MessageBlock(messages[i], startColumnIndex + i, rowIndex, width, hAlign));
        }
    }
    
    public void addColumnMessages(int columnIndex, String ... messages) {
        addColumnMessages(columnIndex, 1, 1, defaultHorizontalAlignment, messages);
    }
    
    public void addColumnMessages(
            int columnIndex, int startRowIndex, int width, HorizontalAlignment hAlign, String ... messages
    ) {
        for (int i = 0; i < messages.length; i++) {
            addMessageBlock(new MessageBlock(messages[i], columnIndex, startRowIndex + i, width, hAlign));
        }
    }
    
    public void addMessageBlock(MessageBlock messageBlock) {
        blocks.add(messageBlock);
        if (messageBlock.getEndX() >= width) {
            width = messageBlock.getEndX() + 1;
        }
        if (messageBlock.getY() >= height) {
            height = messageBlock.getY() + 1;
        }
    }
    
    public void addMessageBlocks(MessageBlock ... messageBlocks) {
        for (MessageBlock messageBlock : messageBlocks) {
            addMessageBlock(messageBlock);
        }
    }
    
    public void addMessageBlocks(Collection<? extends MessageBlock> messageBlocks) {
        for (MessageBlock messageBlock : messageBlocks) {
            addMessageBlock(messageBlock);
        }
    }
    
    public void addMessageBlockDecoration(MessageBlockDecoration decoration) {
        this.decorations.add(decoration);
    }
    
    public void addMessageBlockDecorations(MessageBlockDecoration ... decorations) {
        for (MessageBlockDecoration decoration : decorations) {
            addMessageBlockDecoration(decoration);
        }
    }
    
    public void addMessageBlockDecorations(Collection<? extends MessageBlockDecoration> messageDecorations) {
        for (MessageBlockDecoration decoration : decorations) {
            addMessageBlockDecoration(decoration);
        }
    }
    
    public void removeMessageBlock(MessageBlock messageBlock) {
        blocks.remove(messageBlock);
        int maxY = 0, maxX = 0;
        for (MessageBlock block : blocks) {
            if (maxY < block.getY()) {
                maxY = block.getY();
            }
            if (maxX < block.getEndX()) {
                maxX = block.getEndX();
            }
        }
        width = maxX + 1;
        height = maxY + 1;
    }
    
    public void removeDecoration(MessageBlockDecoration decoration) {
        decorations.remove(decoration);
    }
    
    public int getMessageBlockCount() {
        return blocks.size();
    }
    
    public int getDecorationCount() {
        return decorations.size();
    }
    
    public MessageBlock getMessageBlock(int index) {
        if (index >= 0 && index < blocks.size()) {
            return blocks.get(index);
        } else {
            return null;
        }
    }
    
    public MessageBlockDecoration getDecoration(int index) {
        if (index >= 0 && index < decorations.size()) {
            return decorations.get(index);
        } else {
            return null;
        }
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void reset() {
        blocks.clear();
        decorations.clear();
        width = height = 0;
    }
    
    public void reorderMessageBlocks() {
        Collections.sort(blocks, sortComparator);
    }
    
    private int[] computeByteLengths(String seperateString) {
        int[] byteLengths = new int[width];
        Arrays.fill(byteLengths, 0);
        List<MessageBlock> processBlocks = new ArrayList<MessageBlock>(blocks);
        Iterator<MessageBlock> processIterator = processBlocks.listIterator();
        while (processIterator.hasNext()) {
            MessageBlock block = processIterator.next();
            int x = block.getX(), w = block.getWidth();
            
            if (w == 1) {
                int size = StringUtil.getStringSize(block.getUnderlayingMessage());
                if (size > byteLengths[x]) {
                    byteLengths[x] = size;
                }
                
                processIterator.remove();
            }
        }
        
        int seperateStringSize = StringUtil.getStringSize(seperateString);
        
        for (MessageBlock block : processBlocks) {
            int x = block.getX(), ex = block.getEndX(),
                size = StringUtil.getStringSize(block.getUnderlayingMessage()),
                availableSize = seperateStringSize * (ex - x);
            
            for (int i = x; i <= ex; i++) {
                availableSize += byteLengths[i];
            }
            
            if (availableSize < size) {
                HorizontalAlignment orientation = block.getHorizontalAlignment();
                int diff = size - availableSize;
                switch (orientation) {
                    case CENTER:
                        int half = diff / 2;
                        
                        if ((diff & 1) == 1) {
                            byteLengths[x] += half + 1;
                            byteLengths[ex] += half;
                        } else {
                            byteLengths[x] += half;
                            byteLengths[ex] += half;
                        }
                        
                        break;
                    case LEFT:
                        byteLengths[x] += diff;
                        break;
                    case RIGHT:
                        byteLengths[ex] += diff;
                        break;
                }
            }
        }
        return byteLengths;
    }
    
    public String getText(int spacingCount) {
        return getText(StringUtil.getSpacesString(spacingCount));
    }
    
    public String getText(int spacingCount, char spacingCharacter) {
        return getText(StringUtil.getCharacterString(spacingCount, spacingCharacter));
    }
    
    public String getText(String seperateString) {
        reorderMessageBlocks();
        
        int[] byteLengths = computeByteLengths(seperateString);
        String[] lines = new String[height];
        int[] processedIndexes = new int[height];
        
        Arrays.fill(processedIndexes, 0);
        for (int i = 0; i < lines.length; i++) {
            lines[i] = "";
        }
        
        for (MessageBlock block : blocks) {
            int x = block.getX(), ex = block.getEndX(), y = block.getY();
            String line = lines[y];
            int processedIndex = processedIndexes[y];
            
            if (x > processedIndex) {
                boolean shouldAppendSpaces = false;
                for (int i = processedIndex; i < x; i++) {
                    if (shouldAppendSpaces) {
                        line += seperateString;
                    }
                    
                    int byteLen = byteLengths[i];
                    if (byteLen == 0) {
                        shouldAppendSpaces = false;
                    } else {
                        shouldAppendSpaces = true;
                        line += StringUtil.getSpacesString(byteLen);
                    }
                }
                if (shouldAppendSpaces) {
                    line += seperateString;
                }
            } else {
                if (processedIndex > 0 && byteLengths[processedIndex] > 0) {
                    line += seperateString;
                }
            }
            
            String msg = block.getUnderlayingMessage();
            int size = StringUtil.getStringSize(msg);
            
            if (x == ex) {
                if (size == byteLengths[x]) {
                    line += msg;
                } else {
                    int diff = byteLengths[x] - size, half = diff / 2;
                    switch(block.getHorizontalAlignment()) {
                        case CENTER:
                            String spaces = StringUtil.getSpacesString(half);
                            if ((diff & 1) == 0) {
                                line += spaces + msg + spaces;
                            } else {
                                line += spaces + msg + spaces + " ";
                            }
                            break;
                        case LEFT:
                            line += msg + StringUtil.getSpacesString(diff);
                            break;
                        case RIGHT:
                            line += StringUtil.getSpacesString(diff) + msg;
                            break;
                        default:
                            line += msg;
                    }
                }
            } else {
                int availableSize = StringUtil.getStringSize(seperateString) * (ex - x);
                for (int i = x; i <= ex; i++) {
                    availableSize += byteLengths[i];
                }
                
                if (availableSize == size) {
                    line += msg;
                } else {
                    int diff = availableSize - size, half = diff / 2;
                    switch(block.getHorizontalAlignment()) {
                        case CENTER:
                            String spaces = StringUtil.getSpacesString(half);
                            if ((diff & 1) == 0) {
                                line += spaces + msg + spaces;
                            } else {
                                line += spaces + msg + spaces + " ";
                            }
                            break;
                        case LEFT:
                            line += msg + StringUtil.getSpacesString(diff);
                            break;
                        case RIGHT:
                            line += StringUtil.getSpacesString(diff) + msg;
                            break;
                        default:
                            line += msg;
                    }
                }
            }
            
            lines[y] = line;
            processedIndexes[y] = ex + 1;
        }
        
        StringBuilder builder = new StringBuilder();
        for (int i = lines.length-1; i >= 0; i--) {
            if (i < lines.length-1) {
                builder.append('\n');
            }
            builder.append(lines[i]);
        }
        
        return builder.toString();
    }
    
    private class MessageBlockSortComparator implements Comparator<MessageBlock> {
        public int compare(MessageBlock b1, MessageBlock b2) {
            if (b1.getY() > b2.getY()) {
                return 1;
            } else if (b1.getY() < b2.getY()) {
                return -1;
            } else {
                if (b1.getX() > b2.getX()) {
                    return 1;
                } else if (b1.getX() < b2.getX()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }
    
    public void setSpacingCharacter(char spacingCharacter) {
        this.spacingCharacter = spacingCharacter;
    }
    
    public void setDefaultHorizontalAlignment(HorizontalAlignment hAlign) {
        this.defaultHorizontalAlignment = hAlign;
    }
    
    public HorizontalAlignment getDefaultHorizontalAlignment() {
        return this.defaultHorizontalAlignment;
    }
    
    @Override
    public String toString() {
        return toString(3, spacingCharacter);
    }
    
    public String toString(char spacingCharacter) {
        return toString(3, spacingCharacter);
    }
    
    public String toString(int spacingCount, char spacingCharacter) {
        return getText(spacingCount, spacingCharacter);
    }
    
    public void horizontalReverse() {
        for (MessageBlock block : blocks) {
            int ex = block.getEndX();
            
            block.setX((2 * ex - ((2 * ex - (width - 1)) * 2)) / 2);
        }
    }
    
    public void verticalReverse() {
        for (MessageBlock block : blocks) {
            int y = block.getY();
            block.setY((2 * y - ((2 * y - (height - 1)) * 2)) / 2);
        }
    }
    
    @Override
    public MessageBlockHolder clone() {
        MessageBlockHolder clone = new MessageBlockHolder();
        
        for (MessageBlock block : blocks) {
            clone.blocks.add(block.clone());
        }
        for (MessageBlockDecoration deco : decorations) {
            clone.decorations.add(deco.clone());
        }
        
        clone.width  = width;
        clone.height = height;
        
        clone.spacingCharacter = spacingCharacter;
        
        clone.defaultHorizontalAlignment = defaultHorizontalAlignment;
        
        return clone;
    }
}
