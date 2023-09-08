/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.painters;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.Painter;
import snake.*;

/**
 * This is a painter used to paint an arrow pointing in a given direction. For 
 * best results, it's recommended to {@link RenderingHints#VALUE_ANTIALIAS_ON 
 * enable antialiasing} when rendering the arrow.
 * @author Milo Steier
 * @since 1.1.0
 */
public class ArrowPainter implements Painter<Integer>, SnakeConstants{
    /**
     * This constructs an ArrowPainter.
     */
    public ArrowPainter(){}
    /**
     * This renders an arrow to the given graphics context. This method may 
     * modify the state of the graphics object and is not required to restore 
     * that state once it's finished. It is recommended that the caller should 
     * pass in a scratch graphics object. It is also recommended that the 
     * graphics object should have {@link RenderingHints#VALUE_ANTIALIAS_ON 
     * antialiasing enabled}. The graphics object must not be null. <p>
     * 
     * The width and height parameters specify the width and height that the 
     * arrow should be rendered into. Any specified clip on the graphics context 
     * will further constrain the region. It is assumed that the arrow should be 
     * rendered at the origin. <p>
     * 
     * The arrow painted will depend on the value for the {@code direction} 
     * parameter if provided. If the {@code direction} is either one of the four 
     * direction flags ({@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link 
     * #LEFT_DIRECTION}, or {@link #RIGHT_DIRECTION}) or any combination of a 
     * single horizontal flag and a single vertical flag ({@link 
     * #UP_LEFT_DIRECTION}, {@link #UP_RIGHT_DIRECTION}, {@link 
     * #DOWN_LEFT_DIRECTION}, or {@link #DOWN_RIGHT_DIRECTION}), then the arrow 
     * will be pointing in the given direction. If the {@code direction} is 
     * either null or zero, then the arrow rendered will represent going 
     * forward. If the {@code direction} is none of the values previously 
     * listed, then this will render an oval indicating an invalid direction.
     * 
     * @param g The graphics context to render to. This cannot be null.
     * @param direction The direction for the arrow. It's recommended for this 
     * to be one of but is not limited to the following: 
     *      {@code null},
     *      {@code 0}, 
     *      {@link #UP_DIRECTION}, 
     *      {@link #DOWN_DIRECTION}, 
     *      {@link #LEFT_DIRECTION}, 
     *      {@link #RIGHT_DIRECTION}, 
     *      {@link #UP_LEFT_DIRECTION}, 
     *      {@link #UP_RIGHT_DIRECTION}, 
     *      {@link #DOWN_LEFT_DIRECTION}, or
     *      {@link #DOWN_RIGHT_DIRECTION}.
     * @param width The width of the area to paint.
     * @param height The height of the area to paint.
     * @throws NullPointerException If the graphics context is null.
     * @see SnakeUtilities#getDirections 
     * @see SnakeUtilities#getDirectionCount 
     * @see SnakeUtilities#hasNoOpposingDirections
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #VERTICAL_DIRECTIONS
     * @see #HORIZONTAL_DIRECTIONS
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     */
    @Override
    public void paint(Graphics2D g, Integer direction, int width, int height) {
        if (g == null)  // If the graphics context is null
            throw new NullPointerException();
            // If the width or height are less than or equal to zero
        else if (width <= 0 || height <= 0) 
            return;
            // Get the shape to use to draw the arrow
        Shape shape = getArrowShape(direction,width,height);
        if (shape == null)  // If no shape was returned, get the default shape
            shape = getDefaultArrowShape(direction,width,height);
        g.fill(shape);  // Draw the arrow
    }
    /**
     * This returns a shape to use to render the arrow based off the given 
     * direction. This is called by {@link #getArrowShape getArrowShape} to get 
     * the shape to draw the arrow with when given a non-null, non-zero 
     * direction. If this returns null, then the {@link #getDefaultArrowShape 
     * default arrow shape} will be used to render the arrow instead. This 
     * should not throw an {@code IllegalArgumentException} if the direction is 
     * invalid.
     * @param direction The direction for the arrow (will be a non-zero value).
     * @param w The width for the arrow.
     * @param h The height for the arrow.
     * @return A shape to use to render the arrow for the given direction.
     * @see #getArrowShape 
     * @see #getForwardArrowShape 
     * @see #getDefaultArrowShape 
     * @see SnakeUtilities#getTriangle2D 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     */
    protected Shape getDirectionArrowShape(int direction, int w, int h){
        try{
            return SnakeUtilities.getTriangle2D(0, 0, w, h, direction);
        }
        catch(IllegalArgumentException ex){
            return null;
        }
    }
    /**
     * This returns the shape to use to render the arrow when no direction is 
     * specified. This is called by {@link #getArrowShape getArrowShape} to get 
     * the shape to draw the arrow with when given either null or zero for the 
     * direction. If this returns null, then the {@link #getDefaultArrowShape 
     * default arrow shape} will be used to render the arrow instead. This 
     * should not throw a {@code NullPointerException} if the direction is null.
     * @param direction The direction for the arrow (will either be null or 
     * zero).
     * @param w The width for the arrow.
     * @param h The height for the arrow.
     * @return A shape to use to render the arrow when no direction is given.
     * @see #getArrowShape 
     * @see #getDirectionArrowShape 
     * @see #getDefaultArrowShape 
     */
    protected Shape getForwardArrowShape(Integer direction, int w, int h){
        // The shape that will be returned is a rhombus
            // A Path2D object to use to generate the shape to return
        Path2D path = new Path2D.Double();
        double halfW = w/2.0;   // Get half the width
        double halfH = h/2.0;   // Get half the height
        path.moveTo(halfW, 0);  // Start at the top center
        path.lineTo(w, halfH);  // Line to center right
        path.lineTo(halfW, h);  // Line to bottom center
        path.lineTo(0, halfH);  // Line to center left
        path.closePath();       // Close the shape
        return path;
    }
    /**
     * This returns the shape to use to render the arrow when {@link 
     * #getArrowShape getArrowShape} returns null. This is called by the {@link 
     * #paint paint} to get the shape to draw the arrow with when the shape 
     * returned by {@code getArrowShape} is null. This should not throw a {@code 
     * NullPointerException} if the direction is null, nor should it throw a 
     * {@code IllegalArgumentException} if the direction is invalid.
     * @param direction The direction for the arrow (may be null).
     * @param w The width for the arrow.
     * @param h The height for the arrow.
     * @return A shape to use to render the arrow when no shape is available 
     * (this should not be null).
     * @see #paint 
     * @see #getArrowShape 
     * @see #getDirectionArrowShape 
     * @see #getForwardArrowShape 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     */
    protected Shape getDefaultArrowShape(Integer direction, int w, int h){
        return new Ellipse2D.Double(0, 0, w, h);
    }
    /**
     * This returns a shape to use to render the arrow based off the given 
     * direction and which is the given width and height. If this returns null, 
     * then a {@link #getDefaultArrowShape default arrow shape} will be used 
     * instead. This should not throw a {@code NullPointerException} if the 
     * direction is null, nor should it throw a {@code IllegalArgumentException} 
     * if the direction is invalid.
     * @param direction The direction for the arrow (may be null).
     * @param w The width for the arrow.
     * @param h The height for the arrow.
     * @return A shape to use to render the arrow for the given direction.
     * @see #paint 
     * @see #getDirectionArrowShape 
     * @see #getForwardArrowShape 
     * @see #getDefaultArrowShape 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     */
    protected Shape getArrowShape(Integer direction, int w, int h){
        if (direction != null)  // If the direction is not null
            direction = SnakeUtilities.getDirections(direction);
            // If the direction is either null or zero
        if (direction == null || direction == 0)
            return getForwardArrowShape(direction,w,h);
        return getDirectionArrowShape(direction,w,h);
    }
}
