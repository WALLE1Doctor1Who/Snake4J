/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.icons;

import java.awt.*;
import javax.swing.Icon;
import snake.*;

/**
 * This is an icon that can be used to represent keys on the keyboard that are 
 * used to control movement. 
 * @author Milo Steier
 */
public abstract class MovementControlIcon implements Icon, SnakeConstants{
    /**
     * This stores the width for the keys.
     */
    private final int width;
    /**
     * This stores the height for the keys.
     */
    private final int height;
    /**
     * This stores the bevel for the keys.
     */
    private final int bevel;
    /**
     * This stores the color to use for the background for the keys.
     */
    private final Color color;
    /**
     * This constructs a MovementControlIcon with the given color, key width, 
     * key height, and bevel for the keys.
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
    public MovementControlIcon(Color color, int width, int height, int bevel){
            // If either the width or height are less than or equal to zero
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("Key width/height are invalid (width: "+
                    width+", height: "+height+")");
        else if (color == null)     // If the color is null
            throw new NullPointerException();
        this.width = width;
        this.height = height;
        this.bevel = bevel;
        this.color = color;
    }
    /**
     * This constructs a MovementControlIcon with the given color, key width, 
     * key height, and bevel of 8 for the keys.
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
    public MovementControlIcon(Color color, int width, int height){
        this(color,width,height,8);
    }
    /**
     * This constructs a MovementControlIcon with the given color, a key width 
     * and key height of 40, and bevel of 8 for the keys.
     * @param color The color to use for the background of the keys (cannot be 
     * null).
     * @throws NullPointerException If the color is null.
     */
    public MovementControlIcon(Color color){
        this(color,40,40);
    }
    /**
     * This draws the icon at the specified location. This will draw four keys, 
     * each representing one of four directions {@link #UP_DIRECTION up}, {@link 
     * #LEFT_DIRECTION left}, {@link #DOWN_DIRECTION down}, and {@link 
     * #RIGHT_DIRECTION right}. Each key will be {@link #getKeyWidth 
     * getKeyWidth()} pixels wide by {@link #getKeyHeight getKeyHeight()} pixels 
     * tall. The symbol for each key is drawn using the {@link #paintKeySymbol 
     * paintKeySymbol} method.
     * @param c A {@code Component} to get useful properties for painting the 
     * icon. 
     * @param g The graphics context to render to.
     * @param x The x-coordinate of the icon's top-left corner.
     * @param y The x-coordinate of the icon's top-left corner.
     * @see #paintKeySymbol 
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g = g.create();
        g.translate(x, y);
            // If the graphics context is a Graphic2D object
        if (g instanceof Graphics2D){
            Graphics2D g2D = (Graphics2D) g;    // Get it as a Graphic2D object
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setRenderingHint(RenderingHints.KEY_RENDERING, 
                    RenderingHints.VALUE_RENDER_QUALITY);
        }
        int w = getKeyWidth()-1;    // Get the width for the key
        int h = getKeyHeight()-1;   // Get the height for the key
        int b = getKeyBevel();      // Get the bevel for the key
            // An array containing the directions in the order that the keys are 
            // rendered in
        int[] directions = {UP_DIRECTION,LEFT_DIRECTION,DOWN_DIRECTION,RIGHT_DIRECTION};
            // This is an array to store the x coords for the keys
        int[] xPoints = new int[directions.length];
            // The first (up) key is offset by the value from the up key offset
        xPoints[0] = w-getUpKeyOffset();
            // A for loop to generate the remaining x coordinates
        for (int i = 1; i < xPoints.length; i++)
            xPoints[i] = (w)*(i-1);
            // Fill the background of the keys
        g.setColor(getColor());
            // A for loop to fill the key backgrounds
        for (int i = 0; i < directions.length; i++)
                // First key is at the top row, the others are on the bottom row
            SnakeUtilities.fill3DRectangle(g,xPoints[i],(i==0)?0:h,w,h,b);
            // Draw the outline of the keys
        g.setColor(new Color(0x303030));
            // A for loop to draw the key outlines
        for (int i = 0; i < directions.length; i++)
                // First key is at the top row, the others are on the bottom row
            SnakeUtilities.draw3DRectangle(g,xPoints[i],(i==0)?0:h,w,h,b);
            // Draw the symbols for the keys
        g.setColor(Color.BLACK);
        b = Math.abs(b);
            // Get the offsets for the x and y coordinates for the symbols. If 
            // the bevel is negative, then the symbols are offset by the bevel.
        int off = (getKeyBevel()<0)?b:0;
            // A for loop to draw the symbols for the keys
        for (int i = 0; i < directions.length; i++)
                // First key is at the top row, the others are on the bottom row
            paintKeySymbol(c,g,xPoints[i]+1+off,((i==0)?0:h)+off+1,w-b-2,h-b-2,
                    directions[i]);
        g.dispose();
    }
    /**
     * This returns the value to subtract from the x-coordinate for the top key 
     * used for moving upwards.
     * @return The offset for the up key.
     * @see #paintIcon 
     */
    protected int getUpKeyOffset(){
        return 0;
    }
    /**
     * This is used to render the symbols for the keys. This is called by {@link 
     * #paintIcon paintIcon} to paint each key's symbol over the raised portion 
     * of the respective rendered key. The given coordinates and size are the 
     * location and size of the raised section of the key being rendered. The 
     * given direction is the direction that this key is used to move in. The 
     * direction should be one of the following direction flags: {@link 
     * #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link #LEFT_DIRECTION}, or 
     * {@link #RIGHT_DIRECTION}. If the direction is not one of the previously 
     * mentioned values, then this may exhibit unpredictable and undefined 
     * behavior.
     * @param c A {@code Component} to get useful properties for painting the 
     * symbol.
     * @param g The graphics context to render to.
     * @param x The x-coordinate of the top-left corner of the raised portion of 
     * the key.
     * @param y The y-coordinate of the top-left corner of the raised portion of 
     * the key.
     * @param w The width of the raised portion of the key.
     * @param h The height of the raised portion of the key.
     * @param direction The direction the key is used to move in. This should be 
     * one of the following: 
     *      {@link #UP_DIRECTION}, 
     *      {@link #DOWN_DIRECTION}, 
     *      {@link #LEFT_DIRECTION}, or 
     *      {@link #RIGHT_DIRECTION}.
     * @see #paintIcon 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     */
    protected abstract void paintKeySymbol(Component c,Graphics g,int x,int y,
            int w, int h, int direction);
    /**
     * This returns the width to use for each of the keys.
     * @return The width for the keys.
     */
    public int getKeyWidth(){
        return width;
    }
    /**
     * This returns the height to use for each of the keys.
     * @return The height for the keys.
     */
    public int getKeyHeight(){
        return height;
    }
    /**
     * This returns the icon's width. This will be equal to {@code (}{@link 
     * #getKeyWidth getKeyWidth()}{@code *3)-2}.
     * @return The width of the icon.
     * @see #getKeyWidth 
     */
    @Override
    public int getIconWidth() {
        return (width*3)-2;
    }
    /**
     * This returns the icon's height. This will be equal to {@code (}{@link 
     * #getKeyHeight getKeyHeight()}{@code *2)-1}.
     * @return The height of the icon.
     * @see #getKeyHeight 
     */
    @Override
    public int getIconHeight() {
        return (height*2)-1;
    }
    /**
     * This returns the value to use for the bevel for the keys. This is the 
     * amount by which the keys are raised.
     * @return The amount by which the keys are raised.
     */
    public int getKeyBevel(){
        return bevel;
    }
    /**
     * This returns the color used as the background color for the keys.
     * @return The color for the background of the keys.
     */
    public Color getColor(){
        return color;
    }
}
