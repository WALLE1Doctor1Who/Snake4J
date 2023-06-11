/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.icons;

import java.awt.Color;

/**
 * This is the default implementation of {@link ColorSelectionIcon 
 * ColorSelectionIcon} that stores the color to be used for the color selection 
 * box.
 * @author Milo Steier
 * @since 1.1.0
 * @see snake.painters.ColorSelectionPainter
 * @see ColorSelectionIcon
 */
public class DefaultColorSelectionIcon extends ColorSelectionIcon{
    /**
     * The color to be displayed by this icon.
     */
    private Color color;
    /**
     * This constructs a DefaultColorSelectionIcon with the given color, width, 
     * and height.
     * @param color The color to be displayed by this icon, or null.
     * @param width The width of the icon (must be a positive, non-zero 
     * integer).
     * @param height The height of the icon (must be a positive, non-zero 
     * integer).
     * @throws IllegalArgumentException If either the width or height are 
     * negative or equal to zero.
     */
    public DefaultColorSelectionIcon(Color color, int width, int height){
        super(width, height);
        this.color = color;
    }
    /**
     * This constructs a DefaultColorSelectionIcon with the given color and 
     * size.
     * @param color The color to be displayed by this icon, or null.
     * @param size The width and height of the icon (must be a positive, 
     * non-zero integer).
     * @throws IllegalArgumentException If the size is negative or equal to 
     * zero.
     */
    public DefaultColorSelectionIcon(Color color, int size){
        this(color,size,size);
    }
    /**
     * This constructs a 16x16 DefaultColorSelectionIcon with the given color.
     * @param color The color to be displayed by this icon, or null.
     */
    public DefaultColorSelectionIcon(Color color){
        this(color,16);
    }
    /**
     * This constructs a DefaultColorSelectionIcon with a null color and the 
     * given width and height.
     * @param width The width of the icon (must be a positive, non-zero 
     * integer).
     * @param height The height of the icon (must be a positive, non-zero 
     * integer).
     * @throws IllegalArgumentException If either the width or height are 
     * negative or equal to zero.
     */
    public DefaultColorSelectionIcon(int width, int height){
        this(null,width,height);
    }
    /**
     * This constructs a DefaultColorSelectionIcon with a null color and the 
     * given size.
     * @param size The width and height of the icon (must be a positive, 
     * non-zero integer).
     * @throws IllegalArgumentException If the size is negative or equal to 
     * zero.
     */
    public DefaultColorSelectionIcon(int size){
        this(null,size);
    }
    /**
     * This constructs a 16x16 DefaultColorSelectionIcon with a null color.
     */
    public DefaultColorSelectionIcon(){
        this(null);
    }
    /**
     * {@inheritDoc }
     * @see #getSelectionPainter 
     * @see #paintIcon2D 
     * @see #setColor 
     */
    @Override
    public Color getColor() {
        return color;
    }
    /**
     * This sets the color being displayed by this icon. If this is set to null, 
     * then this icon will display a hue and saturation gradient instead.
     * @param color The color to be displayed by this icon, or null.
     * @see #getSelectionPainter 
     * @see #paintIcon2D 
     * @see #getColor 
     */
    public void setColor(Color color){
        this.color = color;
    }
}
