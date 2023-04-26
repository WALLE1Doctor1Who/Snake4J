/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.icons;

import java.awt.*;
import snake.SnakeUtilities;

/**
 * This is an icon that can be used to represent the escape key on the keyboard. 
 * @author Milo Steier
 */
public class EscControlIcon extends KeyControlIcon {
    /**
     * This is a String to be used to render the symbol for the escape key.
     */
    public static final String ESCAPE_KEY_TEXT = "ESC";
    /**
     * This constructs an EscControlIcon with the given color, width, height, 
     * and bevel for the key.
     * @param color The color to use for the background of the key (cannot be 
     * null).
     * @param width The width of the key and icon (must be a positive, non-zero 
     * integer).
     * @param height The height of the key and icon (must be a positive, 
     * non-zero integer).
     * @param bevel The amount by which the top of the key is raised.
     * @throws NullPointerException If the color is null.
     * @throws IllegalArgumentException If either the width or height are 
     * negative or equal to zero.
     */
    public EscControlIcon(Color color, int width, int height, int bevel){
        super(color, width, height, bevel);
    }
    /**
     * This constructs an EscControlIcon with the given color, width, height, 
     * and a bevel of 8 for the key. 
     * @param color The color to use for the background of the key (cannot be 
     * null).
     * @param width The width of the key and icon (must be a positive, non-zero 
     * integer).
     * @param height The height of the key and icon (must be a positive, 
     * non-zero integer).
     * @throws NullPointerException If the color is null.
     * @throws IllegalArgumentException If either the width or height are 
     * negative or equal to zero.
     */
    public EscControlIcon(Color color, int width, int height){
        super(color,width,height);
    }
    /**
     * This constructs an EscControlIcon with the given color, a width of 64, a 
     * height of 40, and a bevel of 8 for the key. 
     * @param color The color to use for the background of the key (cannot be 
     * null).
     * @throws NullPointerException If the color is null.
     */
    public EscControlIcon(Color color){
        this(color, 64, 40);
    }
    /**
     * This constructs an EscControlIcon with a yellow background and the given 
     * width, height, and bevel for the key.
     * @param width The width of the key and icon (must be a positive, non-zero 
     * integer).
     * @param height The height of the key and icon (must be a positive, 
     * non-zero integer).
     * @param bevel The amount by which the top of the key is raised.
     * @throws IllegalArgumentException If either the width or height are 
     * negative or equal to zero.
     */
    public EscControlIcon(int width, int height, int bevel){
        this(Color.YELLOW, width, height, bevel);
    }
    /**
     * This constructs an EscControlIcon with a yellow background and the given 
     * width, height, and bevel of 8 for the key.
     * @param width The width of the key and icon (must be a positive, non-zero 
     * integer).
     * @param height The height of the key and icon (must be a positive, 
     * non-zero integer).
     * @throws IllegalArgumentException If either the width or height are 
     * negative or equal to zero.
     */
    public EscControlIcon(int width, int height){
        this(Color.YELLOW,width,height);
    }
    /**
     * This constructs an EscControlIcon with a yellow background, a width of 
     * 64, a height of 40, and bevel of 8 for the key.
     */
    public EscControlIcon(){
        this(Color.YELLOW);
    }
    /**
     * {@inheritDoc } <p>
     * 
     * This draws a String with the text "ESC" in the center of the key as the 
     * key's symbol.
     * 
     * @param c {@inheritDoc }
     * @param g {@inheritDoc }
     * @param x {@inheritDoc }
     * @param y {@inheritDoc }
     * @param w {@inheritDoc }
     * @param h  {@inheritDoc }
     * @see #paintIcon 
     * @see SnakeUtilities#drawCenteredString 
     * @see #ESCAPE_KEY_TEXT
     */
    @Override
    protected void paintKeySymbol(Component c,Graphics g,int x,int y,int w,
            int h) {
        SnakeUtilities.drawCenteredString(g,ESCAPE_KEY_TEXT,x,y,w,h);
    }
}
