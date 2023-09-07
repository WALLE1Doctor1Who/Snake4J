/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.icons;

import java.awt.*;
import java.util.Objects;
import snake.*;
import snake.painters.ArrowPainter;

/**
 * This is an icon that displays an arrow pointing in a given direction.
 * @author Milo Steier
 * @since 1.1.0
 */
public class ArrowIcon implements Icon2D, SnakeConstants{
    /**
     * This stores the width for the icon.
     */
    private final int width;
    /**
     * This stores the height for the icon.
     */
    private final int height;
    /**
     * This stores the color for the arrow. If this is null, then the arrow will 
     * use the component's foreground color instead.
     */
    private Color color;
    /**
     * This stores the direction in which the arrow is pointing in.
     */
    private final int direction;
    /**
     * This is the painter used to render the arrow for the icon.
     */
    protected ArrowPainter arrowPainter = new ArrowPainter();
    /**
     * This constructs an ArrowIcon with the given direction, color, width, and 
     * height. The direction must be either zero, one of the four direction 
     * flags ({@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link 
     * #LEFT_DIRECTION}, or {@link #RIGHT_DIRECTION}), or any combination of a 
     * single horizontal flag and a single vertical flag ({@link 
     * #UP_LEFT_DIRECTION}, {@link #UP_RIGHT_DIRECTION}, {@link 
     * #DOWN_LEFT_DIRECTION}, or {@link #DOWN_RIGHT_DIRECTION}).
     * @param direction The direction in which the arrow should be pointing in. 
     * This should be one of the following: 
     *      {@code 0}, 
     *      {@link #UP_DIRECTION}, 
     *      {@link #DOWN_DIRECTION}, 
     *      {@link #LEFT_DIRECTION}, 
     *      {@link #RIGHT_DIRECTION}, 
     *      {@link #UP_LEFT_DIRECTION}, 
     *      {@link #UP_RIGHT_DIRECTION}, 
     *      {@link #DOWN_LEFT_DIRECTION}, or
     *      {@link #DOWN_RIGHT_DIRECTION}.
     * @param color The color to render the arrow in, or null.
     * @param width The width of the arrow and icon (must be a positive, 
     * non-zero integer).
     * @param height The height of the arrow and icon (must be a positive, 
     * non-zero integer).
     * @throws IllegalArgumentException If either the width or height are 
     * negative or equal to zero.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     */
    public ArrowIcon(int direction,Color color,int width,int height){
            // If either the width or height are less than or equal to zero
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException(
                    "Width and/or height are invalid (width: "+width+
                            ", height: "+height+")");
        if (direction != 0) // If the direction is not zero
                // Check the direction for the triangle
            SnakeUtilities.requireDirectionForTriangle(direction);
        this.direction = direction;
        this.width = width;
        this.height = height;
        this.color = color;
    }
    /**
     * This constructs an ArrowIcon with the given direction, color, and size. 
     * The direction must be either zero, one of the four direction flags 
     * ({@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link #LEFT_DIRECTION}, 
     * or {@link #RIGHT_DIRECTION}), or any combination of a single horizontal 
     * flag and a single vertical flag ({@link #UP_LEFT_DIRECTION}, {@link 
     * #UP_RIGHT_DIRECTION}, {@link #DOWN_LEFT_DIRECTION}, or {@link 
     * #DOWN_RIGHT_DIRECTION}).
     * @param direction The direction in which the arrow should be pointing in. 
     * This should be one of the following: 
     *      {@code 0}, 
     *      {@link #UP_DIRECTION}, 
     *      {@link #DOWN_DIRECTION}, 
     *      {@link #LEFT_DIRECTION}, 
     *      {@link #RIGHT_DIRECTION}, 
     *      {@link #UP_LEFT_DIRECTION}, 
     *      {@link #UP_RIGHT_DIRECTION}, 
     *      {@link #DOWN_LEFT_DIRECTION}, or
     *      {@link #DOWN_RIGHT_DIRECTION}.
     * @param color The color to render the arrow in, or null.
     * @param size The width and height of the arrow and icon (must be a 
     * positive, non-zero integer).
     * @throws IllegalArgumentException If the size is negative or equal to 
     * zero.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     */
    public ArrowIcon(int direction, Color color, int size){
        this(direction,color,size,size);
    }
    /**
     * This constructs an ArrowIcon with the given direction, color, and a size 
     * of 16x16. The direction must be either zero, one of the four direction 
     * flags ({@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link 
     * #LEFT_DIRECTION}, or {@link #RIGHT_DIRECTION}), or any combination of a 
     * single horizontal flag and a single vertical flag ({@link 
     * #UP_LEFT_DIRECTION}, {@link #UP_RIGHT_DIRECTION}, {@link 
     * #DOWN_LEFT_DIRECTION}, or {@link #DOWN_RIGHT_DIRECTION}).
     * @param direction The direction in which the arrow should be pointing in. 
     * This should be one of the following: 
     *      {@code 0}, 
     *      {@link #UP_DIRECTION}, 
     *      {@link #DOWN_DIRECTION}, 
     *      {@link #LEFT_DIRECTION}, 
     *      {@link #RIGHT_DIRECTION}, 
     *      {@link #UP_LEFT_DIRECTION}, 
     *      {@link #UP_RIGHT_DIRECTION}, 
     *      {@link #DOWN_LEFT_DIRECTION}, or
     *      {@link #DOWN_RIGHT_DIRECTION}.
     * @param color The color to render the arrow in, or null.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     */
    public ArrowIcon(int direction, Color color){
        this(direction,color,16);
    }
    /**
     * This constructs an ArrowIcon with the given direction, width, and height. 
     * The direction must be either zero, one of the four direction flags 
     * ({@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link #LEFT_DIRECTION}, 
     * or {@link #RIGHT_DIRECTION}), or any combination of a single horizontal 
     * flag and a single vertical flag ({@link #UP_LEFT_DIRECTION}, {@link 
     * #UP_RIGHT_DIRECTION}, {@link #DOWN_LEFT_DIRECTION}, or {@link 
     * #DOWN_RIGHT_DIRECTION}).
     * @param direction The direction in which the arrow should be pointing in. 
     * This should be one of the following: 
     *      {@code 0}, 
     *      {@link #UP_DIRECTION}, 
     *      {@link #DOWN_DIRECTION}, 
     *      {@link #LEFT_DIRECTION}, 
     *      {@link #RIGHT_DIRECTION}, 
     *      {@link #UP_LEFT_DIRECTION}, 
     *      {@link #UP_RIGHT_DIRECTION}, 
     *      {@link #DOWN_LEFT_DIRECTION}, or
     *      {@link #DOWN_RIGHT_DIRECTION}.
     * @param width The width of the arrow and icon (must be a positive, 
     * non-zero integer).
     * @param height The height of the arrow and icon (must be a positive, 
     * non-zero integer).
     * @throws IllegalArgumentException If either the width or height are 
     * negative or equal to zero.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     */
    public ArrowIcon(int direction, int width, int height){
        this(direction,null,width,height);
    }
    /**
     * This constructs an ArrowIcon with the given direction and size. The 
     * direction must be either zero, one of the four direction flags ({@link 
     * #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link #LEFT_DIRECTION}, or 
     * {@link #RIGHT_DIRECTION}), or any combination of a single horizontal flag 
     * and a single vertical flag ({@link #UP_LEFT_DIRECTION}, {@link 
     * #UP_RIGHT_DIRECTION}, {@link #DOWN_LEFT_DIRECTION}, or {@link 
     * #DOWN_RIGHT_DIRECTION}).
     * @param direction The direction in which the arrow should be pointing in. 
     * This should be one of the following: 
     *      {@code 0}, 
     *      {@link #UP_DIRECTION}, 
     *      {@link #DOWN_DIRECTION}, 
     *      {@link #LEFT_DIRECTION}, 
     *      {@link #RIGHT_DIRECTION}, 
     *      {@link #UP_LEFT_DIRECTION}, 
     *      {@link #UP_RIGHT_DIRECTION}, 
     *      {@link #DOWN_LEFT_DIRECTION}, or
     *      {@link #DOWN_RIGHT_DIRECTION}.
     * @param size The width and height of the arrow and icon (must be a 
     * positive, non-zero integer).
     * @throws IllegalArgumentException If the size is negative or equal to 
     * zero.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     */
    public ArrowIcon(int direction, int size){
        this(direction,size,size);
    }
    /**
     * This constructs an ArrowIcon with the given direction and a size of 
     * 16x16. The direction must be either zero, one of the four direction flags 
     * ({@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link #LEFT_DIRECTION}, 
     * or {@link #RIGHT_DIRECTION}), or any combination of a single horizontal 
     * flag and a single vertical flag ({@link #UP_LEFT_DIRECTION}, {@link 
     * #UP_RIGHT_DIRECTION}, {@link #DOWN_LEFT_DIRECTION}, or {@link 
     * #DOWN_RIGHT_DIRECTION}).
     * @param direction The direction in which the arrow should be pointing in. 
     * This should be one of the following: 
     *      {@code 0}, 
     *      {@link #UP_DIRECTION}, 
     *      {@link #DOWN_DIRECTION}, 
     *      {@link #LEFT_DIRECTION}, 
     *      {@link #RIGHT_DIRECTION}, 
     *      {@link #UP_LEFT_DIRECTION}, 
     *      {@link #UP_RIGHT_DIRECTION}, 
     *      {@link #DOWN_LEFT_DIRECTION}, or
     *      {@link #DOWN_RIGHT_DIRECTION}.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     */
    public ArrowIcon(int direction){
        this(direction,null);
    }
    /**
     * This draws the icon at the specified location. The icon will display an 
     * arrow pointing in the set {@link #getDirection direction} using the set 
     * {@link #getColor color}. If the set direction is zero, then the icon will 
     * display an oval instead. If no direction is set, then the {@link 
     * Component#getForeground foreground} color of the component will be used 
     * instead.
     * @param c A {@code Component} to get useful properties for painting the 
     * icon. 
     * @param g The graphics context to render to.
     * @param x The x-coordinate of the icon's top-left corner.
     * @param y The x-coordinate of the icon's top-left corner.
     * @see #getDirection
     * @see #getColor
     * @see SnakeUtilities#getTriangle 
     */
    @Override
    public void paintIcon2D(Component c, Graphics2D g, int x, int y) {
        g.translate(x, y);      // Translate the graphics context
        if (color == null)      // If no color is set, use the foreground color
            g.setColor(c.getForeground());
        else
            g.setColor(color);  // Use the color currently set
            // Enable antialiasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
            // Prioritize rendering quality over speed
        g.setRenderingHint(RenderingHints.KEY_RENDERING, 
                RenderingHints.VALUE_RENDER_QUALITY);
            // Use the painter to render the arrow
        arrowPainter.paint(g, direction, width, height);
    }
    /**
     * This returns the icon's width. This is also used for the width of the 
     * arrow.
     * @return The width of the icon.
     */
    @Override
    public int getIconWidth() {
        return width;
    }
    /**
     * This returns the icon's height. This is also used for the height of the 
     * arrow.
     * @return The height of the icon.
     */
    @Override
    public int getIconHeight() {
        return height;
    }
    /**
     * This returns the direction in which the arrow is pointing in. This will 
     * be an integer with the direction flags set on it that represents the 
     * direction in which the arrow is pointing in. The direction will be either 
     * zero, one of the four direction flags ({@link #UP_DIRECTION}, {@link 
     * #DOWN_DIRECTION}, {@link #LEFT_DIRECTION}, or {@link #RIGHT_DIRECTION}), 
     * or any combination of a single horizontal flag and a single vertical flag 
     * ({@link #UP_LEFT_DIRECTION}, {@link #UP_RIGHT_DIRECTION}, {@link 
     * #DOWN_LEFT_DIRECTION}, or {@link #DOWN_RIGHT_DIRECTION}). If the 
     * direction is zero, then an oval will be displayed instead.
     * @return The direction in which the arrow is pointing in.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     * @see #isPointingUp 
     * @see #isPointingDown 
     * @see #isPointingLeft 
     * @see #isPointingRight 
     */
    public int getDirection(){
        return direction;
    }
    /**
     * This returns whether arrow drawn by this icon is pointing upwards. 
     * @return Whether the arrow is pointing up.
     * @see #UP_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #getDirection 
     * @see #isPointingDown 
     * @see #isPointingLeft 
     * @see #isPointingRight 
     */
    public boolean isPointingUp(){
        return SnakeUtilities.getFlag(getDirection(), UP_DIRECTION);
    }
    /**
     * This returns whether arrow drawn by this icon is pointing downwards. 
     * @return Whether the arrow is pointing down.
     * @see #DOWN_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     * @see #getDirection 
     * @see #isPointingUp 
     * @see #isPointingLeft 
     * @see #isPointingRight 
     */
    public boolean isPointingDown(){
        return SnakeUtilities.getFlag(getDirection(), DOWN_DIRECTION);
    }
    /**
     * This returns whether arrow drawn by this icon is pointing to the left. 
     * @return Whether the arrow is pointing left.
     * @see #LEFT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #getDirection 
     * @see #isPointingUp 
     * @see #isPointingDown 
     * @see #isPointingRight 
     */
    public boolean isPointingLeft(){
        return SnakeUtilities.getFlag(getDirection(), LEFT_DIRECTION);
    }
    /**
     * This returns whether arrow drawn by this icon is pointing to the right. 
     * @return Whether the arrow is pointing right.
     * @see #RIGHT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     * @see #getDirection 
     * @see #isPointingUp 
     * @see #isPointingDown 
     * @see #isPointingLeft 
     */
    public boolean isPointingRight(){
        return SnakeUtilities.getFlag(getDirection(), RIGHT_DIRECTION);
    }
    /**
     * This returns the color used to draw the arrow. If this is null, then the 
     * foreground color of the component that this icon is drawn on will be used 
     * instead.
     * @return The color used for the arrow, or null.
     */
    public Color getColor(){
        return color;
    }
    /**
     * This sets the color to use for the arrow. If this is set to null, then 
     * the arrow will be drawn in the foreground color of the component that 
     * this icon is drawn on.
     * @param color The color to use for the arrow, or null.
     */
    public void setColor(Color color){
        this.color = color;
    }
    /**
     * This returns a String representation of this arrow icon. This method is 
     * primarily intended to be used only for debugging purposes, and the 
     * content and format of the returned String may vary between 
     * implementations.
     * @return A String representation of this arrow icon.
     */
    protected String paramString(){
        return getIconWidth()+"x"+getIconHeight()+
                ","+SnakeUtilities.getDirectionString(getDirection())+
                ",color="+Objects.toString(getColor(), "");
    }
    /**
     * This returns a String representation of this arrow icon.
     * @return A String representation of this arrow icon.
     */
    @Override
    public String toString(){
        return this.getClass().getName() + "[" + paramString() + "]";
    }
}
