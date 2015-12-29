package utils.string;

import java.awt.Color;

public class MessageBlockDecoration implements Cloneable {
    public static final Color DEFAULT_COLOR = Color.BLACK;
    
    private int startX = -1, startY = -1, endX = -1, endY = -1;
    
    private Color color;
    
    public MessageBlockDecoration(
            int startX, int startY, int endX, int endY, Color color
    ) {
        this.color = color;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
    
    public MessageBlockDecoration(int startX, int startY, int endX, int endY) {
        this(startX, startY, endX, endY, Color.BLACK);
    }
    
    public int getStartX() {
        return startX;
    }
    
    public int getStartY() {
        return startY;
    }
    
    public int getEndX() {
        return endX;
    }
    
    public int getEndY() {
        return endY;
    }
    
    public Color getColor() {
        return color;
    }
    
    @Override
    public String toString() {
        return "[startX: " + startX + ", startY: " + startY + 
            ", endX: " + endX + ", endY: " + endY + ", color: " + color.toString() + "]";
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof MessageBlockDecoration) {
            MessageBlockDecoration m = (MessageBlockDecoration) o;
            
            return startX == m.startX && startY == m.startY && 
                endX == m.endX && endY == m.endY && color.equals(m.color);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return 121 * color.hashCode() + 91 * startX + 61 * startY + 31 * endX + endY; 
    }
    
    @Override
    public MessageBlockDecoration clone() {
        return new MessageBlockDecoration(startX, startY, endX, endY, color);
    }
}
