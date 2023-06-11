/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.painters;

import java.awt.*;
import java.util.Objects;
import javax.swing.*;
import snake.SnakeConstants;
import snake.SnakeUtilities;

/**
 * This is a painter used to render a representation of a key on the keyboard. 
 * Each setter method will return the calling KeyPainter so that they can be 
 * chained together to change multiple properties with a single line of code.
 * @author Milo Steier
 * @param <T> The datatype of the optional configuration argument for the 
 * symbol.
 * @since 1.1.0
 */
public abstract class KeyPainter<T> implements Painter<Component>,SnakeConstants {
    /**
     * This stores the bevel for the keys rendered by this painter.
     */
    private int bevel;
    /**
     * This stores the color to use for the background for the keys rendered by 
     * this painter. When this is null, the background of the provided component 
     * will be used instead.
     */
    private Color bg;
    /**
     * This stores the color to use for the foreground for the keys rendered by 
     * this painter. When this is null, the foreground of the provided component 
     * will be used instead.
     */
    private Color fg;
    /**
     * This stores the color to use for the outline of the keys rendered by this 
     * painter. When this is null, then {@link #GRAPHICS_OUTLINE_COLOR} will be 
     * used instead.
     */
    private Color outline;
    /**
     * This stores the object to provide to the symbol painting method.
     */
    private T object;
    /**
     * This constructs a KeyPainter with the given argument for the symbol, 
     * along with a bevel of 8. The foreground, background, and outline colors 
     * are all set to null by default.
     * @param symbolObject The optional configuration argument to use for the 
     * symbol. This may be null.
     */
    public KeyPainter(T symbolObject){
        this.object = symbolObject;
        this.bevel = 8;
        this.bg = null;
        this.fg = null;
        this.outline = null;
    }
    /**
     * This constructs a KeyPainter with the a null argument for the symbol, 
     * along with a bevel of 8. The foreground, background, and outline colors 
     * are all set to null by default.
     */
    public KeyPainter(){
        this((T)null);
    }
    /**
     * This constructs a KeyPainter that is a copy of the given KeyPainter and 
     * with the given argument for the symbol.
     * @param keyPainter The KeyPainter to get the settings from. This cannot be 
     * null.
     * @param symbolObject The optional configuration argument to use for the 
     * symbol. This may be null.
     * @throws NullPointerException If the KeyPainter being duplicated is null.
     */
    public KeyPainter(KeyPainter<?> keyPainter, T symbolObject){
        this.object = symbolObject;
        Objects.requireNonNull(keyPainter); // Check if the key painter is null
        this.bevel = keyPainter.bevel;
        this.bg = keyPainter.bg;
        this.fg = keyPainter.fg;
        this.outline = keyPainter.outline;
    }
    /**
     * This constructs a KeyPainter that is a copy of the given KeyPainter and 
     * with a null argument for the symbol.
     * @param keyPainter The KeyPainter to get the settings from. This cannot be 
     * null.
     * @throws NullPointerException If the KeyPainter being duplicated is null.
     */
    public KeyPainter(KeyPainter<?> keyPainter){
        this(keyPainter,null);
    }
    /**
     * This renders a key to the given graphics context. This method  may modify 
     * the state of the graphics object and is not required to restore that 
     * state once they're finished. It's recommended that the caller should pass 
     * in a scratch graphics object. The graphics object must not be null. <p>
     * 
     * The width and height parameters specify the width and height that the key 
     * should be rendered into. Any specified clip on the graphics context will 
     * further constrain the region. It is assumed that the key should be 
     * rendered at the origin. <p>
     * 
     * This method delegates rendering the symbol for the key to the {@link 
     * #paintSymbol paintSymbol} method. The {@code paintSymbol} method will be 
     * provided the location and size of the raised portion of the key (if the 
     * bevel is negative, then this will be the lowered portion of the key). The 
     * {@code paintSymbol} method will also be provided the optional symbol 
     * argument returned by the {@link #getSymbolArgument getSymbolArgument} 
     * method.
     * 
     * @param g The graphics context to render to. This cannot be null.
     * @param c A {@code Component} to get useful properties for painting the 
     * key. This may be null.
     * @param width The width of the area to paint.
     * @param height The height of the area to paint.
     * @see #paintSymbol
     * @see #getKeyBevel 
     * @see #getBackground 
     * @see #getForeground 
     * @see #getOutlineColor 
     * @see #getSymbolArgument 
     */
    @Override
    public void paint(Graphics2D g, Component c, int width, int height) {
        if (g == null)  // If the graphics context is null
            throw new NullPointerException();
            // If the width or height is less than or equal to zero
        else if (width <= 0 || height <= 0) 
            return;
        width--;        // Shrink the width by 1 to fit within the given area
        height--;       // Shrink the height by 1 to fit within the given area
        Color bg = null;        // This gets the color for the body of the key
        if (this.bg != null)    // If a color is set
            bg = this.bg;       // Use the color that is set
            // If a non-null component has been provided and it has a 
        else if (c != null && c.getBackground() != null)    // background color
            bg = c.getBackground(); // Use the component's background color
        if (bg != null){        // If we have a color to use for the key body
            g.setColor(bg);
            g.setBackground(bg);
        }   // Fill a 3D rectangle to represent the body of the key
        SnakeUtilities.fill3DRectangle(g, 0, 0, width, height, bevel);
            // If the outline color is set, use it. Otherwise, default to the 
            // default outline color
        g.setColor((outline != null)?outline:GRAPHICS_OUTLINE_COLOR);
            // Outline the body of the key
        SnakeUtilities.draw3DRectangle(g, 0, 0, width, height, bevel);
        int b = Math.abs(bevel);    // Get the absolute value of the bevel
        width-=(b+1);       // Shrink the width to get the width of the symbol
        height-=(b+1);      // Shrink the height to get the width of the symbol
            // If there's enough room for the key symbol
        if (width > 0 && height > 0){
            if (fg != null)     // If there is a color set for the foreground
                g.setColor(fg);
                // If a non-null component has been provided and it has a 
            else if (c != null && c.getForeground() != null)// foreground color
                g.setColor(c.getForeground());
                // This will offset the symbol by the absolute value of the 
                // bevel plus 1 if the bevel is negative, and by one if the 
                // bevel is positive or zero.
            int off = 1+Math.abs(Math.min(bevel, 0));
                // Draw the symbol for the key
            paintSymbol(g,c,getSymbolArgument(),off,off,width,height);
        }
    }
    /**
     * This is used by the {@link #paint paint} method to render the symbol for 
     * the key. Implementations of this method may modify the state of the 
     * graphics object and are not required to restore that state once they're 
     * finished. The graphics object must not be null. <p>
     * 
     * The {@code Component} may be used to get properties useful for painting 
     * the symbol of the key. The supplied object parameter acts as an optional 
     * configuration argument, which, when invoked by the {@code paint} method, 
     * is provided by the {@link #getSymbolArgument getSymbolArgument} method. 
     * Both the {@code Component} and the configuration argument may be null, 
     * and implementations must not throw a NullPointerException if that is the 
     * case. <p>
     * 
     * The x and y parameters specify the location of the top-left corner of the 
     * area in which the symbol is to be painted into, and the width and height 
     * parameters specify the size of the area. More specifically, the given x, 
     * y, width, and height instruct this method that it should render the 
     * symbol fully within this region. Any specified clip on the graphics 
     * context will further constrain the region. 
     * 
     * @param g The graphics context to render to. This cannot be null.
     * @param c A {@code Component} to get useful properties for painting the 
     * symbol. This may be null.
     * @param object The optional configuration argument for the symbol. This 
     * may be null.
     * @param x The x-coordinate of the top-left corner of the symbol for the 
     * key.
     * @param y The y-coordinate of the top-left corner of the symbol for the 
     * key.
     * @param w The width of the symbol for the key.
     * @param h The height of the symbol for the key.
     * @see #paint
     * @see #getSymbolArgument 
     * @see #setSymbolArgument
     */
    public abstract void paintSymbol(Graphics2D g, Component c, T object, int x, 
            int y, int w, int h);
    /**
     * This returns the value to use for the bevel for the keys rendered by this 
     * {@code KeyPainter}. This is the amount by which the keys are raised. If 
     * this is negative, then the keys will be lowered instead of raised.
     * @return The amount by which the keys are raised.
     * @see #setKeyBevel
     */
    public int getKeyBevel(){
        return bevel;
    }
    /**
     * This sets the value to use for the bevel for the keys rendered by this 
     * {@code KeyPainter}. This is the amount by which the keys will be raised. 
     * If this is negative, then the keys will be lowered instead of raised.
     * @param bevel The amount by which the keys will be raised.
     * @return This {@code KeyPainter}.
     * @see #getKeyBevel 
     */
    public KeyPainter setKeyBevel(int bevel){
        this.bevel = bevel;
        return this;
    }
    /**
     * This returns the color used as the background color for the keys rendered 
     * by this {@code KeyPainter}. This is the color used for the body of the 
     * keys. If this is null, then the keys will be rendered using the 
     * background color of the component provided to this painter.
     * @return The background color used for the keys, or null.
     * @see #setBackground
     * @see #getForeground 
     * @see #setForeground
     * @see #getOutlineColor 
     * @see #setOutlineColor
     * @see Component#getBackground 
     */
    public Color getBackground(){
        return bg;
    }
    /**
     * This sets the color to use as the background color for the keys rendered 
     * by this {@code KeyPainter}. This is the color used for the body of the 
     * keys. If this is set to null, then the keys will be rendered using the 
     * background color of the component provided to this painter. 
     * @param bg The background color to use for the keys, or null.
     * @return This {@code KeyPainter}.
     * @see #getBackground
     * @see #getForeground 
     * @see #setForeground
     * @see #getOutlineColor 
     * @see #setOutlineColor
     * @see Component#getBackground 
     */
    public KeyPainter setBackground(Color bg){
        this.bg = bg;
        return this;
    }
    /**
     * This returns the color used as the foreground color for the keys rendered 
     * by this {@code KeyPainter}. This is the color used for the symbol of the 
     * keys, if there is one. If this is null, then the keys will be rendered 
     * using the foreground color of the component provided to this painter.
     * @return The foreground color used for the keys, or null.
     * @see #setForeground
     * @see #getBackground 
     * @see #setBackground
     * @see #getOutlineColor 
     * @see #setOutlineColor
     * @see #paintSymbol
     * @see Component#getForeground 
     */
    public Color getForeground(){
        return fg;
    }
    /**
     * This sets the color to use as the foreground color for the keys rendered 
     * by this {@code KeyPainter}. This is the color used for the symbol of the 
     * keys, if there is one. If this is set to null, then the keys will be 
     * rendered using the foreground color of the component provided to this 
     * painter. 
     * @param fg The foreground color to use for the keys, or null.
     * @return This {@code KeyPainter}.
     * @see #getForeground
     * @see #getBackground 
     * @see #setBackground
     * @see #getOutlineColor 
     * @see #setOutlineColor
     * @see #paintSymbol
     * @see Component#getForeground 
     */
    public KeyPainter setForeground(Color fg){
        this.fg = fg;
        return this;
    }
    /**
     * This returns the color used to draw the outline for the keys rendered by 
     * this {@code KeyPainter}. If this is null, then this will use {@link 
     * #GRAPHICS_OUTLINE_COLOR} to draw the outline of the keys.
     * @return The outline color for the keys, or null.
     * @see #setOutlineColor
     * @see #getForeground 
     * @see #setForeground
     * @see #getBackground 
     * @see #setBackground
     * @see #GRAPHICS_OUTLINE_COLOR
     */
    public Color getOutlineColor(){
        return outline;
    }
    /**
     * This sets the color used to draw the outline for the keys rendered by 
     * this {@code KeyPainter}. If this is set to null, then this will use 
     * {@link #GRAPHICS_OUTLINE_COLOR} to draw the outline of the keys.
     * @param color The outline color to use for the keys, or null.
     * @return This {@code KeyPainter}.
     * @see #getOutlineColor
     * @see #getForeground 
     * @see #setForeground
     * @see #getBackground 
     * @see #setBackground
     * @see #GRAPHICS_OUTLINE_COLOR
     */
    public KeyPainter setOutlineColor(Color color){
        this.outline = color;
        return this;
    }
    /**
     * This returns the optional configuration argument used for when this 
     * {@code KeyPainter} is painting a key. This is the object that is provided 
     * to the {@link #paintSymbol paintSymbol} method by the {@link #paint 
     * paint} method when painting the symbol for the key.
     * @return The optional configuration argument for the symbol. This may be 
     * null.
     * @see #setSymbolArgument
     * @see #paintSymbol 
     * @see #paint 
     */
    public T getSymbolArgument(){
        return object;
    }
    /**
     * This sets the optional configuration argument to use when this {@code 
     * KeyPainter} is painting a key. This is the object that is provided to the 
     * {@link #paintSymbol paintSymbol} method by the {@link #paint paint} 
     * method when painting the symbol for the key. 
     * @param object The optional configuration argument to use for the symbol. 
     * This may be null.
     * @return This {@code KeyPainter}.
     * @see #getSymbolArgument
     * @see #paintSymbol 
     * @see #paint 
     */
    public KeyPainter setSymbolArgument(T object){
        this.object = object;
        return this;
    }
    /**
     * This returns a String representation of this key painter. This method is 
     * primarily intended to be used only for debugging purposes, and the 
     * content and format of the returned String may vary between 
     * implementations.
     * @return A String representation of this key painter.
     */
    protected String paramString(){
        return "keyBevel="+getKeyBevel()+
                ",background="+Objects.toString(getBackground(),"")+
                ",foreground="+Objects.toString(getForeground(),"")+
                ",outlineColor="+Objects.toString(getOutlineColor(),"")+
                ",symbolArgument="+Objects.toString(getSymbolArgument(),"");
    }
    /**
     * This returns a String representation of this key painter.
     * @return A String representation of this key painter.
     */
    @Override
    public String toString(){
        return this.getClass().getName() + "[" + paramString() + "]";
    }
}
