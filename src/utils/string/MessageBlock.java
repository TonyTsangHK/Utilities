package utils.string;

import utils.constants.HorizontalAlignment;

import java.text.AttributedString;

public class MessageBlock {
    private HorizontalAlignment horizontalAlignment;
    private AttributedString message;
    private String underlayingMessage;
    
    private int x = 0, y = 0, width = 1;
    
    public MessageBlock(String message, int x, int y) {
        this(new AttributedString(message), x, y, 1, HorizontalAlignment.CENTER);
    }
    
    public MessageBlock(String message, int x, int y, int w) {
        this(new AttributedString(message), x, y, w, HorizontalAlignment.CENTER);
    }
    
    public MessageBlock(String message, int x, int y, int w, HorizontalAlignment hAlign) {
        this(new AttributedString(message), x, y, w, hAlign);
    }
    
    public MessageBlock(AttributedString message, int x, int y) {
        this(message, x, y, 1, HorizontalAlignment.CENTER);
    }
    
    public MessageBlock(AttributedString message, int x, int y, int w) {
        this(message, x, y, w, HorizontalAlignment.CENTER);
    }
    
    public MessageBlock(AttributedString message, int x, int y, int w, HorizontalAlignment hAlign) {
        setMessage(message);
        setX(x);
        setY(y);
        setWidth(w);
        setHorizontalAlignment(hAlign);
    }
    
    public void setHorizontalAlignment(HorizontalAlignment hAlign) {
        if (hAlign != null) {
            this.horizontalAlignment = hAlign;
        }
    }
    
    public HorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }
    
    public void setMessage(AttributedString msg) {
        this.message = msg;
        this.underlayingMessage = StringUtil.extractUnderlyingStringFromAttributedString(message);
    }
    
    public void setMessage(String msg) {
        this.message = new AttributedString(msg);
        this.underlayingMessage = msg;
    }
    
    public AttributedString getMessage() {
        return this.message;
    }
    
    public String getUnderlayingMessage() {
        return underlayingMessage;
    }
    
    public void setX(int x) {
        if (x >= 0) {
            this.x = x;
        }
    }
    
    public void setY(int y) {
        if (y >= 0) {
            this.y = y;
        }
    }
    
    public void setWidth(int w) {
        if (w > 0) {
            this.width = w;
        }
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getEndX() {
        return x + width - 1;
    }
    
    @Override
    public MessageBlock clone() {
        MessageBlock clone = new MessageBlock(message, x, y, width);
        
        clone.horizontalAlignment = horizontalAlignment;
        
        return clone;
    }
}
