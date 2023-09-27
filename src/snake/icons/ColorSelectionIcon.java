/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.icons;

import java.awt.*;
import java.util.Objects;
import snake.SnakeConstants;
import snake.painters.ColorSelectionPainter;

/**
 * This is an icon that renders a color selection box to use to select a color. 
 * The color displayed by the color selection box is determined by the {@link 
 * #getColor getColor} method. If the {@code getColor} method returns null, then 
 * a hue and saturation gradient will be used instead. This is effectively an 
 * icon equivalent for {@link ColorSelectionPainter ColorSelectionPainter}, with 
 * a {@code ColorSelectionPainter} used internally to render the color selection 
 * box.
 * @author Milo Steier
 * @since 1.1.0
 * @see ColorSelectionPainter
 * @see DefaultColorSelectionIcon
 */
public abstract class ColorSelectionIcon implements Icon2D, SnakeConstants{
    /**
     * This stores the width for the icon.
     */
    private final int width;
    /**
     * This stores the height for the icon.
     */
    private final int height;
    /**
     * The ColorSelectionPainter used to render the color selection box.
     */
    private ColorSelectionPainter painter;
    /**
     * This constructs a ColorSelectionIcon with the given width and height.
     * @param width The width of the icon (must be a positive, non-zero 
     * integer).
     * @param height The height of the icon (must be a positive, non-zero 
     * integer).
     * @throws IllegalArgumentException If either the width or height are 
     * negative or equal to zero.
     */
    public ColorSelectionIcon(int width, int height){
            // If either the width or height are less than or equal to zero
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException(
                    "Width and/or height are invalid (width: "+width+
                            ", height: "+height+")");
        this.width = width;
        this.height = height;
        painter = new ColorSelectionPainter();
    }
    /**
     * This constructs a ColorSelectionIcon with the given size.
     * @param size The width and height of the icon (must be a positive, 
     * non-zero integer).
     * @throws IllegalArgumentException If the size is negative or equal to 
     * zero.
     */
    public ColorSelectionIcon(int size){
        this(size,size);
    }
    /**
     * This constructs a 16x16 ColorSelectionIcon.
     */
    public ColorSelectionIcon(){
        this(16);
    }
    /**
     * {@inheritDoc } <p>
     * 
     * This implementation uses the internal {@link #getSelectionPainter 
     * ColorSelectionPainter} to render the color selection box using the icon's 
     * width and height and the color returned by {@link #getColor getColor}.
     * @see #getSelectionPainter 
     * @see #getColor 
     * @see #getIconWidth 
     * @see #getIconHeight 
     * @see #paintIcon
     */
    @Override
    public void paintIcon2D(Component c, Graphics2D g, int x, int y) {
        g.translate(x, y);
            // Render the color selection box
        painter.paint(g, getColor(), getIconWidth(), getIconHeight());
    }
    /**
     * This returns the icon's width.
     * @return The width of the icon.
     */
    @Override
    public int getIconWidth() {
        return width;
    }
    /**
     * This returns the icon's height.
     * @return The height of the icon.
     */
    @Override
    public int getIconHeight() {
        return height;
    }
    /**
     * This returns the color being displayed by this icon. If this is null, 
     * then this icon will display a hue and saturation gradient instead.
     * @return The color displayed by this icon, or null.
     * @see #getSelectionPainter 
     * @see #paintIcon2D 
     */
    public abstract Color getColor();
    /**
     * This gets the ColorSelectionPainter used by this icon to render the color 
     * selection box.
     * @return The ColorSelectionPainter used to render the color selection box.
     * @see #getColor 
     * @see #paintIcon2D
     */
    public ColorSelectionPainter getSelectionPainter(){
        return painter;
    }
    /**
     * This sets the ColorSelectionPainter used by this icon to render the color 
     * selection box.
     * @param painter The ColorSelectionPainter used to render the color 
     * selection box.
     * @see #getSelectionPainter 
     */
    protected void setSelectionPainter(ColorSelectionPainter painter){
        this.painter = Objects.requireNonNull(painter);
    }
    /**
     * This returns the amount of space between the outline and the contents of 
     * the box. This delegates to the {@link ColorSelectionPainter#getContentGap 
     * getContentGap} method of the {@link #getSelectionPainter 
     * ColorSelectionPainter}.
     * @return The amount of pixels between the outline and the contents of the 
     * box.
     * @see #setContentGap 
     * @see #getSelectionPainter 
     * @see ColorSelectionPainter#getContentGap 
     */
    public int getContentGap(){
        return painter.getContentGap();
    }
    /**
     * This sets the amount of space between the outline and the contents of the 
     * box. This delegates to the {@link ColorSelectionPainter#setContentGap 
     * setContentGap} method of the {@link #getSelectionPainter 
     * ColorSelectionPainter}.
     * @param gap The amount of pixels between the outline and the contents of 
     * the box (cannot be negative).
     * @throws IllegalArgumentException If the gap between the outline and the 
     * contents is negative.
     * @see #getContentGap 
     * @see #getSelectionPainter 
     * @see ColorSelectionPainter#setContentGap 
     */
    public void setContentGap(int gap){
        painter.setContentGap(gap);
    }
    /**
     * This returns the color used as the background color for the box rendered 
     * by this icon. If this is set to null, then this icon will not have a 
     * background. This delegates to the {@link 
     * ColorSelectionPainter#getBackground getBackground} method of the {@link 
     * #getSelectionPainter ColorSelectionPainter}.
     * @return The background color used for the box, or null.
     * @see #setBackground
     * @see #getOutlineColor 
     * @see #setOutlineColor
     * @see #getSelectionPainter 
     * @see ColorSelectionPainter#getBackground
     */
    public Color getBackground(){
        return painter.getBackground();
    }
    /**
     * This sets the color to use as the background color for the box rendered 
     * by this icon. If this is set to null, then this icon will not have a 
     * background. This delegates to the {@link 
     * ColorSelectionPainter#setBackground setBackground} method of the {@link 
     * #getSelectionPainter ColorSelectionPainter}.
     * @param bg The background color to use for the box, or null.
     * @see #getBackground
     * @see #getOutlineColor 
     * @see #setOutlineColor
     * @see #getSelectionPainter 
     * @see ColorSelectionPainter#setBackground
     */
    public void setBackground(Color bg){
        painter.setBackground(bg);
    }
    /**
     * This returns the color used to draw the outline for the box rendered by 
     * this icon. If this is null, then this will use {@link 
     * #GRAPHICS_OUTLINE_COLOR} to draw the outline of the box. This delegates 
     * to the {@link ColorSelectionPainter#getOutlineColor getOutlineColor} 
     * method of the {@link #getSelectionPainter ColorSelectionPainter}.
     * @return The color to use for the outline, or null.
     * @see #setOutlineColor
     * @see #getBackground 
     * @see #setBackground
     * @see #GRAPHICS_OUTLINE_COLOR
     * @see #getSelectionPainter 
     * @see ColorSelectionPainter#getOutlineColor
     */
    public Color getOutlineColor(){
        return painter.getOutlineColor();
    }
    /**
     * This sets the color used to draw the outline for the box rendered by this 
     * icon. If this is set to null, then this will use {@link 
     * #GRAPHICS_OUTLINE_COLOR} to draw the outline of the box. This delegates 
     * to the {@link ColorSelectionPainter#setOutlineColor setOutlineColor} 
     * method of the {@link #getSelectionPainter ColorSelectionPainter}.
     * @param color The color to use for the outline, or null.
     * @see #getOutlineColor
     * @see #getBackground 
     * @see #setBackground
     * @see #GRAPHICS_OUTLINE_COLOR
     * @see #getSelectionPainter
     * @see ColorSelectionPainter#setOutlineColor
     */
    public void setOutlineColor(Color color){
        painter.setOutlineColor(color);
    }
    /**
     * This returns a String representation of this color selection icon. This 
     * method is primarily intended to be used only for debugging purposes, and 
     * the content and format of the returned String may vary between 
     * implementations.
     * @return A String representation of this color selection icon.
     */
    protected String paramString(){
        return getIconWidth()+"x"+getIconHeight()+
                ",color="+Objects.toString(getColor(), "")+
                ",selectionPainter="+painter;
    }
    /**
     * This returns a String representation of this color selection icon.
     * @return A String representation of this color selection icon.
     */
    @Override
    public String toString(){
        return this.getClass().getName() + "[" + paramString() + "]";
    }
}
