/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.painters;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import javax.swing.Painter;
import snake.SnakeConstants;

/**
 * This is a painter used to render a color selection box to be used to display 
 * the currently selected color. Each setter method will return the calling 
 * ColorSelectionPainter so that they can be chained together to change multiple 
 * properties with a single line of code.
 * @author Milo Steier
 * @since 1.1.0
 * @see snake.icons.ColorSelectionIcon
 * @see snake.icons.DefaultColorSelectionIcon
 */
public class ColorSelectionPainter implements Painter<Color>, SnakeConstants{
    /**
     * This contains the colors for the hue gradient used when no color is 
     * provided.
     * @see #HUE_GRADIENT_FRACTIONS
     */
    protected static final Color[] HUE_GRADIENT_COLORS = {
        Color.RED,Color.YELLOW,Color.GREEN,Color.CYAN,Color.BLUE,Color.MAGENTA,Color.RED
    };
    /**
     * This generates the color distribution for the hue gradient based off of 
     * the {@link #HUE_GRADIENT_COLORS hue gradient colors}.
     * @return An array containing the hue gradient color distribution.
     * @see #HUE_GRADIENT_COLORS
     * @see #HUE_GRADIENT_FRACTIONS
     */
    private static float[] getHueGradientFractions(){
            // This gets the generated array
        float[] arr = new float[HUE_GRADIENT_COLORS.length];
        float d = arr.length-1;     // Get the denominator for the fractions
            // A for loop to create the fractions.
        for (int i = 0; i < arr.length; i++){
            arr[i] = i / d;
        }
        return arr;
    }
    /**
     * This contains the color distribution for the hue gradient used when no 
     * color is provided. The colors are evenly distributed.
     * @see #HUE_GRADIENT_COLORS
     */
    protected static final float[] HUE_GRADIENT_FRACTIONS = 
            getHueGradientFractions();
    /**
     * This contains a completely transparent color.
     */
    protected static final Color TRANSPARENT_COLOR = new Color(0,0,0,0);
    /**
     * This contains a scratch Rectangle2D object used when rendering the 
     * contents of the box.
     */
    private Rectangle2D rect = null;
    /**
     * This stores the color to use for the outline of the box rendered by this 
     * painter. When this is null, then {@link #GRAPHICS_OUTLINE_COLOR} will be 
     * used instead.
     */
    private Color outline;
    /**
     * This stores the color to use for the background for the keys rendered by 
     * this painter. When this is null, then no background will be drawn.
     */
    private Color bg;
    /**
     * The gap between the outline of the outline and the box contents.
     */
    private int gap;
    /**
     * This constructs a ColorSelectionPainter with the background and outline 
     * colors set to null and a gap of 2 pixels.
     */
    public ColorSelectionPainter(){
        outline = null;
        bg = null;
        gap = 2;
    }
    /**
     * This constructs a ColorSelectionPainter that is a copy of the given 
     * ColorSelectionPainter.
     * @param painter The ColorSelectionPainter to get the settings from. This 
     * cannot be null.
     * @throws NullPointerException If the ColorSelectionPainter being 
     * duplicated is null.
     */
    public ColorSelectionPainter(ColorSelectionPainter painter){
        this.outline = painter.outline;
        this.bg = painter.bg;
        this.gap = painter.gap;
    }
    /**
     * This renders a color selection box to the given graphics context. This 
     * method  may modify the state of the graphics object and is not required 
     * to restore that state once they're finished. It's recommended that the 
     * caller should pass in a scratch graphics object. The graphics object must 
     * not be null. <p>
     * 
     * The width and height parameters specify the width and height that the box 
     * should be rendered into. Any specified clip on the graphics context will 
     * further constrain the region. It is assumed that the box should be 
     * rendered at the origin. <p>
     * 
     * When given a color for the {@code color} parameter, the given color will 
     * be used to fill the contents of the color selection box. Otherwise, the 
     * color selection box will be filled using both a hue gradient and a 
     * saturation gradient. The {@link #getContentGap content gap} is used as 
     * the maximum intended gap to add between the outline and contents. Any 
     * transforms applied to the given graphics context may affect the final 
     * result. If a {@link #getBackground background color} is set, then it will 
     * be used as the background for the color selection box. Otherwise, the 
     * color selection box will have no background.
     * 
     * @param g The graphics context to render to. This cannot be null.
     * @param color The color to display in the box, or null to show a color 
     * hue and saturation graphic.
     * @param width The width of the area to paint.
     * @param height The height of the area to paint.
     * @see #getContentGap 
     * @see #setContentGap 
     * @see #getBackground 
     * @see #setBackground
     * @see #getOutlineColor 
     * @see #setOutlineColor 
     */
    @Override
    public void paint(Graphics2D g, Color color, int width, int height) {
        if (g == null)  // If the graphics context is null
            throw new NullPointerException();
            // If the width or height is less than or equal to zero
        else if (width <= 0 || height <= 0) 
            return;
        if (bg != null){        // If there is a background set
            g.setColor(bg);
            g.fillRect(0, 0, width, height);
        }
        double gap = 0;         // The gap to use for the contents, default to 0
        double cX = width/2.0;  // Get the center x-coordinate
        double cY = height/2.0; // Get the center y-coordinate
            // If the width and height are greater than two pixels
        if (width > 2 && height > 2)
                // Get the gap to use, with the maximum being the content gap+1
                // (to account for the outline) and 1 pixel
            gap = Math.min(getContentGap()+1,Math.min(cX, cY)-0.5);
        if (rect == null)   // If the scratch rectangle is not initialized
            rect = new Rectangle2D.Double();
            // Set the scratch rectangle's frame
        rect.setFrameFromCenter(cX, cY, gap, gap);
            // Render the contents of the box
        paintContents(g,color,rect);
            // If the width and height are greater than two pixels, then draw 
        if (width > 2 && height > 2){   // the outline
                // If the outline color is set, use it. Otherwise, default to 
                // the default outline color
            g.setColor((outline != null)?outline:GRAPHICS_OUTLINE_COLOR);
            g.drawRect(0, 0, width-1, height-1);
        }
    }
    /**
     * This returns the gradient used to render the hue values. The gradient 
     * goes from left to right across the given area.
     * @param rect The rectangle containing the area to fill with the gradient.
     * @return The hue gradient.
     * @see #getSaturationGradient
     * @see #paintContents 
     * @see #paint 
     */
    protected java.awt.LinearGradientPaint getHueGradient(Rectangle2D rect){
        return new LinearGradientPaint(
                (float)rect.getMinX(),(float)rect.getMinY(),
                (float)rect.getMaxX(),(float)rect.getMinY(),
                HUE_GRADIENT_FRACTIONS,HUE_GRADIENT_COLORS);
    }
    /**
     * This returns the gradient used to represent the saturation when combined 
     * with the {@link #getHueGradient(int) hue gradient}. The gradient goes 
     * from top to bottom across the given area and starts off transparent and 
     * ends being gray.
     * @param rect The rectangle containing the area to fill with the gradient.
     * @return The saturation gradient.
     * @see #getHueGradient
     * @see #paintContents 
     * @see #paint 
     */
    protected java.awt.GradientPaint getSaturationGradient(Rectangle2D rect){
        return new GradientPaint(
                (float)rect.getMinX(),(float)rect.getMinY(),TRANSPARENT_COLOR,
                (float)rect.getMinX(),(float)rect.getMaxY(), Color.GRAY);
    }
    /**
     * This renders the contents of the color selection box with the area 
     * defined by the given rectangle.
     * @param g The graphics context to render to. This cannot be null.
     * @param color The color to display in the box, or null to show a color 
     * hue and saturation graphic.
     * @param rect The rectangle defining the area to render the contents 
     * within.
     * @see #paint 
     * @see #getHueGradient 
     * @see #getSaturationGradient 
     * @see #getContentGap 
     */
    protected void paintContents(Graphics2D g, Color color, Rectangle2D rect){
            // This gets an array of paints to use to render the contents
        Paint[] paints;     
        if (color != null)  // If a color is provided
            paints = new Paint[]{color};    //The only paint needed is the color
        else    // There are two gradients used to render the contents when 
                // no color is provided.
            paints = new Paint[]{getHueGradient(rect),  // Hue Gradient
                getSaturationGradient(rect)};           // Saturation Gradient
            // Go through the paints to render the contents of the box
        for (Paint paint : paints){     
            g.setPaint(paint);
            g.fill(rect);
        }
    }
    /**
     * This returns the amount of space between the outline and the contents of 
     * the box.
     * @return The amount of pixels between the outline and the contents of the 
     * box.
     * @see #setContentGap 
     */
    public int getContentGap(){
        return gap;
    }
    /**
     * This sets the amount of space between the outline and the contents of the 
     * box.
     * @param gap The amount of pixels between the outline and the contents of 
     * the box (cannot be negative).
     * @return This {@code ColorSelectionPainter}.
     * @throws IllegalArgumentException If the gap between the outline and the 
     * contents is negative.
     * @see #getContentGap 
     */
    public ColorSelectionPainter setContentGap(int gap){
        if (gap < 0)    // If the gap is negative.
            throw new IllegalArgumentException(
                    "The gap between the outline and the contents cannot be "
                            + "negative ("+gap+" < 0)");
        this.gap = gap;
        return this;
    }
    /**
     * This returns the color used as the background color for the box rendered 
     * by this {@code ColorSelectionPainter}. If this is set to null, then no 
     * background will be drawn by this painter. 
     * @return The background color used for the box, or null.
     * @see #setBackground
     * @see #getOutlineColor 
     * @see #setOutlineColor
     */
    public Color getBackground(){
        return bg;
    }
    /**
     * This sets the color to use as the background color for the box rendered 
     * by this {@code ColorSelectionPainter}. If this is set to null, then no 
     * background will be drawn by this painter. 
     * @param bg The background color to use for the box, or null.
     * @return This {@code ColorSelectionPainter}.
     * @see #getBackground
     * @see #getOutlineColor 
     * @see #setOutlineColor
     */
    public ColorSelectionPainter setBackground(Color bg){
        this.bg = bg;
        return this;
    }
    /**
     * This returns the color used to draw the outline for the box rendered by 
     * this {@code ColorSelectionPainter}. If this is null, then this will use 
     * {@link #GRAPHICS_OUTLINE_COLOR} to draw the outline of the box.
     * @return The color to use for the outline, or null.
     * @see #setOutlineColor
     * @see #getBackground 
     * @see #setBackground
     * @see #GRAPHICS_OUTLINE_COLOR
     */
    public Color getOutlineColor(){
        return outline;
    }
    /**
     * This sets the color used to draw the outline for the box rendered by this 
     * {@code ColorSelectionPainter}. If this is set to null, then this will use 
     * {@link #GRAPHICS_OUTLINE_COLOR} to draw the outline of the box.
     * @param color The color to use for the outline, or null.
     * @return This {@code ColorSelectionPainter}.
     * @see #getOutlineColor
     * @see #getBackground 
     * @see #setBackground
     * @see #GRAPHICS_OUTLINE_COLOR
     */
    public ColorSelectionPainter setOutlineColor(Color color){
        this.outline = color;
        return this;
    }
    /**
     * This returns a String representation of this color selection painter. 
     * This method is primarily intended to be used only for debugging purposes, 
     * and the content and format of the returned String may vary between 
     * implementations.
     * @return A String representation of this color selection painter.
     */
    protected String paramString(){
        return "contentGap="+getContentGap()+
                ",background="+Objects.toString(getBackground(),"")+
                ",outlineColor="+Objects.toString(getOutlineColor(),"");
    }
    /**
     * This returns a String representation of this color selection painter.
     * @return A String representation of this color selection painter.
     */
    @Override
    public String toString(){
        return this.getClass().getName() + "[" + paramString() + "]";
    }
}
