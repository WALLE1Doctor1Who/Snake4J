/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.icons;

import java.awt.*;
import snake.*;

/**
 * This is an icon that can be used to represent the "W", "A", "S", and "D" keys 
 * on the keyboard. 
 * @author Milo Steier
 */
public class WASDControlIcon extends MovementControlIcon{
    /**
     * This constructs a WASDControlIcon with the given color, key width, key 
     * height, and bevel for the keys.
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
    public WASDControlIcon(Color color, int width, int height, int bevel){
        super(color,width,height,bevel);
    }
    /**
     * This constructs a WASDControlIcon with the given color, key width, key 
     * height, and bevel of 8 for the keys.
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
    public WASDControlIcon(Color color, int width, int height){
        super(color,width,height);
    }
    /**
     * This constructs a WASDControlIcon with the given color, a key width and 
     * key height of 40, and bevel of 8 for the keys.
     * @param color The color to use for the background of the keys (cannot be 
     * null).
     * @throws NullPointerException If the color is null.
     */
    public WASDControlIcon(Color color){
        super(color);
    }
    /**
     * This constructs a WASDControlIcon with a cyan background and the given 
     * key width, key height, and bevel for the key.
     * @param width The width of the key and icon (must be a positive, non-zero 
     * integer).
     * @param height The height of the key and icon (must be a positive, 
     * non-zero integer).
     * @param bevel The amount by which the top of the key is raised.
     * @throws IllegalArgumentException If either the key width or height are 
     * negative or equal to zero.
     */
    public WASDControlIcon(int width, int height, int bevel){
        this(Color.CYAN,width,height,bevel);
    }
    /**
     * This constructs a WASDControlIcon with a cyan background and the given 
     * key width, key height, and bevel of 8 for the key.
     * @param width The width of the key and icon (must be a positive, non-zero 
     * integer).
     * @param height The height of the key and icon (must be a positive, 
     * non-zero integer).
     * @throws IllegalArgumentException If either the key width or height are 
     * negative or equal to zero.
     */
    public WASDControlIcon(int width, int height){
        this(Color.CYAN,width,height);
    }
    /**
     * This constructs a WASDControlIcon with a cyan background, a key width and 
     * key height of 40, and bevel of 8 for the key.
     */
    public WASDControlIcon(){
        this(Color.CYAN);
    }
    /**
     * {@inheritDoc } <p>
     * 
     * This draws a centered String with the letter used for the given 
     * direction. This will be the letter "W" for {@link #UP_DIRECTION}, "A" for 
     * {@link #LEFT_DIRECTION}, "S" for {@link #DOWN_DIRECTION}, and "D" for 
     * {@link #RIGHT_DIRECTION}.
     * 
     * @param c {@inheritDoc }
     * @param g {@inheritDoc }
     * @param x {@inheritDoc }
     * @param y {@inheritDoc }
     * @param w {@inheritDoc }
     * @param h {@inheritDoc }
     * @param direction {@inheritDoc }
     * @see #paintIcon2D 
     * @see SnakeUtilities#drawCenteredString 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     */
    @Override
    protected void paintKeySymbol(Component c, Graphics g, int x, int y, int w, 
            int h, int direction) {
        String text;                // This gets the letter to render
        switch(direction){          // Determine the direction for the key
            case(UP_DIRECTION):     // If the direction is up
                text = "W";
                break;
            case(LEFT_DIRECTION):   // If the direction is down
                text = "A";
                break;
            case(DOWN_DIRECTION):   // If the direction is left
                text = "S";
                break;
            case(RIGHT_DIRECTION):  // If the direction is right
                text = "D";
                break;
            default:                // This is an unknown direction
                return;
        }
        SnakeUtilities.drawCenteredString(g, text, x, y, w, h);
    }
    /**
     * {@inheritDoc } This has been overridden to return a fourth of the key 
     * width, so that the top "W" key will be rendered where it would be on a 
     * standard QWERTY keyboard.
     * @return {@inheritDoc }
     * @see #paintIcon2D 
     * @see #getKeyWidth 
     */
    @Override
    protected int getUpKeyOffset(){
        return Math.floorDiv(getKeyWidth(), 4);
    }
}
