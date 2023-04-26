/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.icons;

import java.awt.*;
import javax.swing.Icon;
import snake.SnakeUtilities;

/**
 * This is an icon that can be used to represent a key on the keyboard. The 
 * {@link #paintKeySymbol paintKeySymbol} method is used by the {@link 
 * #paintIcon paintIcon} method to paint the symbol for the key.
 * @author Milo Steier
 */
public abstract class KeyControlIcon implements Icon{
    /**
     * This stores the width of the key.
     */
    private final int width;
    /**
     * This stores the height of the key.
     */
    private final int height;
    /**
     * This stores the bevel for the key.
     */
    private final int bevel;
    /**
     * This stores the color to use for the background of the key.
     */
    private final Color color;
    /**
     * This constructs a KeyControlIcon with the given color, width, height, and 
     * bevel for the key.
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
    public KeyControlIcon(Color color, int width, int height, int bevel){
            // If either the width or height are less than or equal to zero
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException(
                    "Width and/ or height are invalid (width: "+width+
                            ", height: "+height+")");
        else if (color == null)     // If the color is null
            throw new NullPointerException();
        this.width = width;
        this.height = height;
        this.bevel = bevel;
        this.color = color;
    }
    /**
     * This constructs a KeyControlIcon with the given color, width, height, and 
     * a bevel of 8 for the key. 
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
    public KeyControlIcon(Color color, int width, int height){
        this(color,width,height,8);
    }
    /**
     * This constructs a KeyControlIcon with the given color, a width and height 
     * of 40, and a bevel of 8 for the key. 
     * @param color The color to use for the background of the key (cannot be 
     * null).
     * @throws NullPointerException If the color is null.
     */
    public KeyControlIcon(Color color){
        this(color, 40, 40);
    }
    /**
     * This constructs a KeyControlIcon with a white background and the given 
     * width, height, and bevel for the key.
     * @param width The width of the key and icon (must be a positive, non-zero 
     * integer).
     * @param height The height of the key and icon (must be a positive, 
     * non-zero integer).
     * @param bevel The amount by which the top of the key is raised.
     * @throws IllegalArgumentException If either the width or height are 
     * negative or equal to zero.
     */
    public KeyControlIcon(int width, int height, int bevel){
        this(Color.WHITE, width, height, bevel);
    }
    /**
     * This constructs a KeyControlIcon with a white background and the given 
     * width, height, and bevel of 8 for the key.
     * @param width The width of the key and icon (must be a positive, non-zero 
     * integer).
     * @param height The height of the key and icon (must be a positive, 
     * non-zero integer).
     * @throws IllegalArgumentException If either the width or height are 
     * negative or equal to zero.
     */
    public KeyControlIcon(int width, int height){
        this(Color.WHITE,width,height);
    }
    /**
     * This constructs a KeyControlIcon with a white background, a width and 
     * height of 40, and bevel of 8 for the key.
     */
    public KeyControlIcon(){
        this(Color.WHITE);
    }
    /**
     * This draws the icon at the specified location. The icon will resemble a 
     * key on the keyboard, and will be {@link #getIconWidth getIconWidth()} 
     * pixels wide by {@link #getIconHeight getIconHeight()} pixels tall. The 
     * The symbol for the key is drawn using the {@link #paintKeySymbol 
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
            // If the graphics context is a Graphic2D object
        if (g instanceof Graphics2D){
            Graphics2D g2D = (Graphics2D) g;    // Get it as a Graphic2D object
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setRenderingHint(RenderingHints.KEY_RENDERING, 
                    RenderingHints.VALUE_RENDER_QUALITY);
        }
        else if (g == null) // If the graphics context is somehow null
            return;
        int w = getIconWidth()-1;   // Get the width for the key
        int h = getIconHeight()-1;  // Get the height for the key
        int b = getKeyBevel();      // Get the bevel for the key
            // Fill the background of the key
        g.setColor(getColor());
        SnakeUtilities.fill3DRectangle(g, x, y, w, h, b);
            // Draw the outline of the key
        g.setColor(new Color(0x303030));
        SnakeUtilities.draw3DRectangle(g, x, y, w, h, b);
            // Draw the symbol for the key
        g.setColor(Color.BLACK);
        b = Math.abs(b);
        if (getKeyBevel() < 0){     // If the set key bevel is negative
            x += b;     // Offset the location by the key bevel to align it
            y += b;
        }
        paintKeySymbol(c,g,x+1,y+1,w-b-2,h-b-2);
        g.dispose();
    }
    /**
     * This is used to render the symbol for the key. This is called by {@link 
     * #paintIcon paintIcon} to paint the key's symbol over the raised portion 
     * of the rendered key. The given coordinates and size are the location and 
     * size of the raised section of the key.
     * @param c A {@code Component} to get useful properties for painting the 
     * symbol.
     * @param g The graphics context to render to.
     * @param x The x-coordinate of the top-left corner of the raised portion of 
     * the key.
     * @param y The y-coordinate of the top-left corner of the raised portion of 
     * the key.
     * @param w The width of the raised portion of the key.
     * @param h The height of the raised portion of the key.
     * @see #paintIcon 
     */
    protected abstract void paintKeySymbol(Component c,Graphics g,int x,int y,
            int w, int h);
    /**
     * This returns the icon's width. This is also used for the width of the 
     * key.
     * @return The width of the icon.
     */
    @Override
    public int getIconWidth() {
        return width;
    }
    /**
     * This returns the icon's height. This is also used for the height of the 
     * key.
     * @return The height of the icon.
     */
    @Override
    public int getIconHeight() {
        return height;
    }
    /**
     * This returns the value to use for the bevel for the key. This is the 
     * amount by which the key is raised.
     * @return The amount by which the key is raised.
     */
    public int getKeyBevel(){
        return bevel;
    }
    /**
     * This returns the color used as the background color for the key.
     * @return The color for the background of the key.
     */
    public Color getColor(){
        return color;
    }
}
