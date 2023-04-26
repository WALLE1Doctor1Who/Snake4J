/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.icons;

import java.awt.*;
import snake.*;

/**
 * This is an icon that can be used to represent the arrow keys on the keyboard.
 * @author Milo Steier
 */
public class ArrowKeyControlIcon extends MovementControlIcon{
    /**
     * The width of the triangles on the keys.
     */
    private final int arrowW;
    /**
     * The height of the triangles on the keys.
     */
    private final int arrowH;
    /**
     * This constructs an ArrowKeyControlIcon with the given color, key width, 
     * key height, key bevel, arrow width, and arrow height.
     * @param color The color to use for the background of the keys (cannot be 
     * null).
     * @param width The width for each of the keys (must be a positive, non-zero 
     * integer). 
     * @param height The height for each of the keys (must be a positive, 
     * non-zero integer). 
     * @param bevel The amount by which the top of the keys are raised.
     * @param arrowW The width for the arrows on the keys (must be a positive 
     * integer). 
     * @param arrowH The height for the arrows on the keys (must be a positive 
     * integer). 
     * @throws NullPointerException If the color is null.
     * @throws IllegalArgumentException If either the key width or height are 
     * negative or equal to zero, or if the arrow width or height are negative.
     */
    public ArrowKeyControlIcon(Color color, int width, int height, int bevel, 
            int arrowW, int arrowH){
        super(color,width,height,bevel);
            // If either the arrow width or height are negative.
        if (arrowW < 0 || arrowH < 0)
            throw new IllegalArgumentException(
                    "Arrow width and/or height cannot be negative (arrowW: " + 
                            arrowW + ", arrowH: " + arrowH + ")");
        this.arrowW = arrowW;
        this.arrowH = arrowH;
    }
    /**
     * This constructs an ArrowKeyControlIcon with the given color, key width, 
     * key height, key bevel, and arrow size.
     * @param color The color to use for the background of the keys (cannot be 
     * null).
     * @param width The width for each of the keys (must be a positive, non-zero 
     * integer). 
     * @param height The height for each of the keys (must be a positive, 
     * non-zero integer). 
     * @param bevel The amount by which the top of the keys are raised.
     * @param arrowS The width and height for the arrows on the keys (must be a 
     * positive integer). 
     * @throws NullPointerException If the color is null.
     * @throws IllegalArgumentException If either the key width or height are 
     * negative or equal to zero, or if the arrow size is negative.
     */
    public ArrowKeyControlIcon(Color color, int width, int height, int bevel, 
            int arrowS){
        this(color,width,height,bevel,arrowS,arrowS);
    }
    /**
     * This constructs an ArrowKeyControlIcon with the given color, key width, 
     * key height, key bevel, and an arrow size of 16x16.
     * @param color The color to use for the background of the keys (cannot be 
     * null).
     * @param width The width for each of the keys (must be a positive, non-zero 
     * integer). 
     * @param height The height for each of the keys (must be a positive, 
     * non-zero integer). 
     * @param bevel The amount by which the top of the keys are raised.
     * @throws NullPointerException If the color is null.
     * @throws IllegalArgumentException If either the key width or height are 
     * negative or equal to zero.
     */
    public ArrowKeyControlIcon(Color color, int width, int height, int bevel){
        this(color,width,height,bevel,16);
    }
    /**
     * This constructs an ArrowKeyControlIcon with the given color, key width, 
     * key height, a key bevel of 8, and an arrow size of 16x16.
     * @param color The color to use for the background of the keys (cannot be 
     * null).
     * @param width The width for each of the keys (must be a positive, non-zero 
     * integer). 
     * @param height The height for each of the keys (must be a positive, 
     * non-zero integer). 
     * @throws NullPointerException If the color is null.
     * @throws IllegalArgumentException If either the key width or height are 
     * negative or equal to zero.
     */
    public ArrowKeyControlIcon(Color color, int width, int height){
        super(color,width,height);
        arrowW = arrowH = 16;
    }
    /**
     * This constructs an ArrowKeyControlIcon with the given color, a key width 
     * and key height of 40, a key bevel of 8, and an arrow size of 16x16.
     * @param color The color to use for the background of the keys (cannot be 
     * null).
     * @throws NullPointerException If the color is null.
     */
    public ArrowKeyControlIcon(Color color){
        super(color);
        arrowW = arrowH = 16;
    }
    /**
     * This constructs an ArrowKeyControlIcon with a magenta background and the 
     * given key width, key height, key bevel, arrow width, and arrow height.
     * @param width The width for each of the keys (must be a positive, non-zero 
     * integer). 
     * @param height The height for each of the keys (must be a positive, 
     * non-zero integer). 
     * @param bevel The amount by which the top of the keys are raised.
     * @param arrowW The width for the arrows on the keys (must be a positive 
     * integer). 
     * @param arrowH The height for the arrows on the keys (must be a positive 
     * integer). 
     * @throws IllegalArgumentException If either the key width or height are 
     * negative or equal to zero, or if the arrow width or height are negative.
     */
    public ArrowKeyControlIcon(int width, int height, int bevel, int arrowW, 
            int arrowH){
        this(Color.MAGENTA,width,height,bevel,arrowW,arrowH);
    }
    /**
     * This constructs an ArrowKeyControlIcon with a magenta background and the 
     * given key width, key height, key bevel, and arrow size.
     * @param width The width for each of the keys (must be a positive, non-zero 
     * integer). 
     * @param height The height for each of the keys (must be a positive, 
     * non-zero integer). 
     * @param bevel The amount by which the top of the keys are raised.
     * @param arrowS The width and height for the arrows on the keys (must be a 
     * positive integer). 
     * @throws IllegalArgumentException If either the key width or height are 
     * negative or equal to zero, or if the arrow size is negative.
     */
    public ArrowKeyControlIcon(int width, int height, int bevel, int arrowS){
        this(width,height,bevel,arrowS,arrowS);
    }
    /**
     * This constructs an ArrowKeyControlIcon with a magenta background and the 
     * given key width, key height, key bevel, and an arrow size of 16x16.
     * @param width The width for each of the keys (must be a positive, non-zero 
     * integer). 
     * @param height The height for each of the keys (must be a positive, 
     * non-zero integer). 
     * @param bevel The amount by which the top of the keys are raised.
     * @throws IllegalArgumentException If either the key width or height are 
     * negative or equal to zero.
     */
    public ArrowKeyControlIcon(int width, int height, int bevel){
        this(width,height,bevel,16);
    }
    /**
     * This constructs an ArrowKeyControlIcon with a magenta background and the 
     * given key width, key height, a key bevel of 8, and an arrow size of 
     * 16x16.
     * @param width The width for each of the keys (must be a positive, non-zero 
     * integer). 
     * @param height The height for each of the keys (must be a positive, 
     * non-zero integer). 
     * @throws IllegalArgumentException If either the key width or height are 
     * negative or equal to zero.
     */
    public ArrowKeyControlIcon(int width, int height){
        this(Color.MAGENTA,width,height);
    }
    /**
     * This constructs an ArrowKeyControlIcon with a magenta background, a key 
     * width and key height of 40, a key bevel of 8, and an arrow size of 16x16.
     */
    public ArrowKeyControlIcon(){
        this(Color.MAGENTA);
    }
    /**
     * {@inheritDoc } <p>
     * 
     * This draws a centered triangle pointing in the given direction.
     * 
     * @param c {@inheritDoc }
     * @param g {@inheritDoc }
     * @param x {@inheritDoc }
     * @param y {@inheritDoc }
     * @param w {@inheritDoc }
     * @param h {@inheritDoc }
     * @param direction {@inheritDoc }
     * @see #paintIcon 
     * @see SnakeUtilities#getTriangle 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     */
    @Override
    protected void paintKeySymbol(Component c, Graphics g, int x, int y, int w,
            int h, int direction) {
        g.fillPolygon(SnakeUtilities.getTriangle(
                x+Math.floorDiv(w-getArrowWidth(), 2)+1, 
                y+Math.floorDiv(h-getArrowHeight(), 2)+1, 
                getArrowWidth(), getArrowHeight(), direction));
    }
    /**
     * This returns the width of the arrows on the keys.
     * @return The widths of the arrows.
     */
    public int getArrowWidth(){
        return arrowW;
    }
    /**
     * This returns the height of the arrows on the keys.
     * @return The height of the arrows.
     */
    public int getArrowHeight(){
        return arrowH;
    }
}
