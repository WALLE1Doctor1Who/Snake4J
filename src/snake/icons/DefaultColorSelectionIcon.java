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
 * @see ColorSelectionPainter
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
     * @param width
     * @param height 
     */
    public DefaultColorSelectionIcon(Color color, int width, int height){
        super(width, height);
        this.color = color;
    }
    
    public DefaultColorSelectionIcon(Color color, int size){
        this(color,size,size);
    }
    
    public DefaultColorSelectionIcon(Color color){
        this(color,16);
    }
    
    public DefaultColorSelectionIcon(int width, int height){
        this(null,width,height);
    }
    
    public DefaultColorSelectionIcon(int size){
        this(null,size);
    }
    
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
