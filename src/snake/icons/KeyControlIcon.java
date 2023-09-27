/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.icons;

import java.awt.*;
import java.util.Objects;
import snake.painters.KeyPainter;

/**
 * This is an icon that can be used to represent a key on the keyboard. The icon 
 * will resemble a key on the keyboard, and will be {@link #getIconWidth 
 * getIconWidth()} pixels wide by {@link #getIconHeight getIconHeight()} pixels 
 * tall. The symbol for the key is drawn using the {@link #paintKeySymbol 
 * paintKeySymbol} method. This is effectively an icon equivalent for {@link 
 * KeyPainter KeyPainter}, with a {@code KeyPainter} used internally to render 
 * the key for this icon. 
 * @author Milo Steier
 */
public abstract class KeyControlIcon implements Icon2D{
    /**
     * This stores the width of the key.
     */
    private final int width;
    /**
     * This stores the height of the key.
     */
    private final int height;
    /**
     * This is the painter used to render the key.
     * @since 1.1.0
     */
    protected KeyPainter<Object> keyPainter;
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
                    "Width and/or height are invalid (width: "+width+
                            ", height: "+height+")");
        else if (color == null)     // If the color is null
            throw new NullPointerException();
            // Create the key painter to paint the key using the paintKeySymbol
            // method
        keyPainter = new KeyPainter<>() {
            @Override
            public void paintSymbol(Graphics2D g,Component c,Object object,
                    int x,int y,int w,int h) {
                paintKeySymbol(c,g,x,y,w,h);
            }
        }.setKeyBevel(bevel).setBackground(color);
        this.width = width;
        this.height = height;
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
     * {@inheritDoc }
     * 
     * @see #paintKeySymbol 
     * @see #paintIcon 
     */
    @Override
    public void paintIcon2D(Component c, Graphics2D g, int x, int y) {
        g.translate(x, y);      // Translate the location
            // Enable antialiasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
            // Prioritize rendering quality over speed
        g.setRenderingHint(RenderingHints.KEY_RENDERING, 
                RenderingHints.VALUE_RENDER_QUALITY);
            // Use the key painter to render the key
        keyPainter.paint(g, c, getIconWidth(), getIconHeight());
    }
    /**
     * This is used to render the symbol for the key. This is called by {@link 
     * #paintIcon2D paintIcon2D} to paint the key's symbol over the raised 
     * portion of the rendered key. The given coordinates and size are the 
     * location and size of the raised section of the key.
     * @param c A {@code Component} to get useful properties for painting the 
     * symbol.
     * @param g The graphics context to render to. This will usually be a {@code 
     * Graphics2D} object, but allows a {@code Graphics} objects for backwards 
     * compatibility.
     * @param x The x-coordinate of the top-left corner of the raised portion of 
     * the key.
     * @param y The y-coordinate of the top-left corner of the raised portion of 
     * the key.
     * @param w The width of the raised portion of the key.
     * @param h The height of the raised portion of the key.
     * @see #paintIcon2D 
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
        return keyPainter.getKeyBevel();
    }
    /**
     * This returns the color used as the background color for the key.
     * @return The color for the background of the key.
     */
    public Color getColor(){
        return keyPainter.getBackground();
    }
    /**
     * This sets the color to use as the background color for the key.
     * @param color The color to use for the background of the key (cannot be 
     * null).
     * @throws NullPointerException If the color is null.
     * @since 1.1.0
     */
    public void setColor(Color color){
        keyPainter.setBackground(Objects.requireNonNull(color));
    }
    /**
     * This returns a String representation of this icon. This method is 
     * primarily intended to be used only for debugging purposes, and the 
     * content and format of the returned String may vary between 
     * implementations.
     * @return A String representation of this icon.
     * @since 1.1.0
     */
    protected String paramString(){
        return getIconWidth()+"x"+getIconHeight()+
                ",keyBevel="+getKeyBevel()+
                ",color="+getColor();
    }
    /**
     * This returns a String representation of this icon.
     * @return A String representation of this icon.
     */
    @Override
    public String toString(){
        return this.getClass().getName() + "[" + paramString() + "]";
    }
}
