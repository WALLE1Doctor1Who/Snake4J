/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.icons;

import java.awt.*;
import snake.*;

/**
 * This is an icon that can be used to represent keys on the keyboard that are 
 * used to control movement. 
 * @author Milo Steier
 */
public abstract class MovementControlIcon extends KeyControlIcon implements 
        SnakeConstants{
    /**
     * An array containing the directions in the order that the keys are 
     * rendered in.
     */
    private static final int[] RENDERED_DIRECTIONS = {
        UP_DIRECTION,
        LEFT_DIRECTION,
        DOWN_DIRECTION,
        RIGHT_DIRECTION
    };
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
        super(color,width,height,bevel);
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
     * tall. The symbol for each key is drawn using the directional {@link 
     * #paintKeySymbol(Component, Graphics, int, int, int, int, int) 
     * paintKeySymbol} method.
     * @param c A {@code Component} to get useful properties for painting the 
     * icon. 
     * @param g The graphics context to render to.
     * @param x The x-coordinate of the icon's top-left corner.
     * @param y The x-coordinate of the icon's top-left corner.
     * @see #paintKeySymbol(Component, Graphics, int, int, int, int, int) 
     * @see #paintKeySymbol(Component, Graphics, int, int, int, int) 
     * @see #paintIcon 
     */
    @Override
    public void paintIcon2D(Component c, Graphics2D g, int x, int y) {
        int w = getKeyWidth();          // Get the width for the keys
        int h = getKeyHeight();         // Get the height for the keys
        g.translate(x, y);              // Translate the location
            // Enable antialiasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
            // Prioritize rendering quality over speed
        g.setRenderingHint(RenderingHints.KEY_RENDERING, 
                RenderingHints.VALUE_RENDER_QUALITY);
            // A for loop to render the movement keys
        for (int i = 0; i < RENDERED_DIRECTIONS.length; i++){
                // Create a scratch graphics context to render the key
            Graphics2D keyG = (Graphics2D) g.create();
            if (i == 0) // If this is the up key, render it on the first row, 
                // second column, offset by the up key offset
                keyG.translate(w-1-getUpKeyOffset(), 0);
            else    // Render the key on the second row, and the corresponding 
                keyG.translate((w-1)*(i-1), h-1);   // column
                // Use the key renderer to render the current key
            keyPainter.setSymbolArgument(RENDERED_DIRECTIONS[i]).paint(keyG,c,w,h);
            keyG.dispose();
        }
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
     * This returns the direction for the next key symbol that will be rendered 
     * by the directionless {@link #paintKeySymbol(Component, Graphics, int, 
     * int, int, int) paintKeySymbol} method.
     * @return The direction of the symbol for the key currently being rendered.
     * @since 1.1.0
     * @see #paintKeySymbol(Component, Graphics, int, int, int, int) 
     * @see #paintKeySymbol(Component, Graphics, int, int, int, int, int) 
     * @see #paintIcon2D 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     */
    protected synchronized int getPaintedKeyDirection(){
            // If the keyPainter has an integer set for it's symbol argument
        if (keyPainter.getSymbolArgument() instanceof Integer)
            return (Integer) keyPainter.getSymbolArgument();
        return UP_DIRECTION;
    }
    /**
     * {@inheritDoc } <p>
     * 
     * This implementation forwards the call to the directional {@link 
     * #paintKeySymbol(Component, Graphics, int, int, int, int, int) 
     * paintKeySymbol} method with the given {@code Component}, graphics 
     * context, x, y, width, and height, along with the {@link 
     * #getPaintedKeyDirection direction of the key being rendered}.
     * 
     * @since 1.1.0
     * @see #paintIcon2D 
     * @see #paintKeySymbol(Component, Graphics, int, int, int, int, int) 
     * @see #getPaintedKeyDirection 
     */
    @Override
    protected void paintKeySymbol(Component c,Graphics g,int x,int y,int w,int h){
        paintKeySymbol(c,g,x,y,w,h,getPaintedKeyDirection());
    }
    /**
     * This is used to render the symbols for the keys. This is called by the 
     * other {@link #paintKeySymbol(Component, Graphics, int, int, int, int) 
     * paintKeySymbol} method with the direction being rendered to paint each 
     * key's symbol over the raised portion of the respective rendered key. The 
     * given coordinates and size are the location and size of the raised 
     * section of the key being rendered. The given direction is the direction 
     * that this key is used to move in. The direction should be one of the 
     * following direction flags: {@link #UP_DIRECTION}, {@link 
     * #DOWN_DIRECTION}, {@link #LEFT_DIRECTION}, or {@link #RIGHT_DIRECTION}. 
     * If the direction is not one of the previously mentioned values, then this 
     * may exhibit unpredictable and undefined behavior.
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
     * @param direction The direction the key is used to move in. This should be 
     * one of the following: 
     *      {@link #UP_DIRECTION}, 
     *      {@link #DOWN_DIRECTION}, 
     *      {@link #LEFT_DIRECTION}, or 
     *      {@link #RIGHT_DIRECTION}.
     * @see #paintIcon2D 
     * @see #paintKeySymbol(Component, Graphics, int, int, int, int) 
     * @see #getPaintedKeyDirection 
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
        return super.getIconWidth();
    }
    /**
     * This returns the height to use for each of the keys.
     * @return The height for the keys.
     */
    public int getKeyHeight(){
        return super.getIconHeight();
    }
    /**
     * This returns the icon's width. This will be equal to {@code (}{@link 
     * #getKeyWidth getKeyWidth()}{@code *3)-2}.
     * @return The width of the icon.
     * @see #getKeyWidth 
     */
    @Override
    public int getIconWidth() {
        return (getKeyWidth()*3)-2;
    }
    /**
     * This returns the icon's height. This will be equal to {@code (}{@link 
     * #getKeyHeight getKeyHeight()}{@code *2)-1}.
     * @return The height of the icon.
     * @see #getKeyHeight 
     */
    @Override
    public int getIconHeight() {
        return (getKeyHeight()*2)-1;
    }
    /**
     * This returns the value to use for the bevel for the keys. This is the 
     * amount by which the keys are raised.
     * @return The amount by which the keys are raised.
     */
    @Override
    public int getKeyBevel(){
        return super.getKeyBevel();
    }
    /**
     * This returns the color used as the background color for the keys.
     * @return The color for the background of the keys.
     */
    @Override
    public Color getColor(){
        return super.getColor();
    }
    /**
     * This sets the color to use as the background color for the keys.
     * @param color The color to use for the background of the keys (cannot be 
     * null).
     * @throws NullPointerException If the color is null.
     * @since 1.1.0
     */
    @Override
    public void setColor(Color color){
        super.setColor(color);
    }
    /**
     * {@inheritDoc }
     */
    @Override
    protected String paramString(){
        return super.paramString()+
                ",keySize="+getKeyWidth()+"x"+getKeyHeight();
    }
}
