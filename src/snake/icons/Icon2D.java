/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package snake.icons;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.Icon;

/**
 * This is an icon that will always render to a Graphics2D object. When given 
 * a graphics context that is not a Graphics2D object, then this renders to an 
 * image and draws that image.
 * @author Milo Steier
 * @since 1.1.0
 */
public interface Icon2D extends Icon{
    /**
     * This draws the icon at the specified location. Icon implementations may 
     * use the given Component to get properties useful for painting, such as 
     * the foreground or background. This delegates to the {@link #paintIcon2D 
     * paintIcon2D} method, providing a scratch Graphics2D object to it. If the 
     * given graphics context is not a Graphics2D object, then this will draw to 
     * an image that will then be drawn to the given graphics context.
     * @param c A {@code Component} to get useful properties for painting.
     * @param g The graphics context to render to.
     * @param x The x-coordinate of the icon's top-left corner.
     * @param y The y-coordinate of the icon's top-left corner.
     * @see #paintIcon2D
     */
    @Override
    public default void paintIcon(Component c,Graphics g,int x, int y){
        int width = getIconWidth();     // Gets the width of the icon
        int height = getIconHeight();   // Gets the height of the icon
            // If this icon wouldn't paint anything due to a width or height of 
        if (width <= 0 || height <= 0)    // zero or less
            return;
        g = g.create();
            // An image to render to if the given graphics context is not a 
        BufferedImage img = null;       // Graphics2D object
        Graphics2D g2D;                 // This will get the Graphics2D object to render to.
        if (g instanceof Graphics2D)    // If the graphics context is a Graphics2D object
            g2D = (Graphics2D) g;
        else if (g != null){            // If the graphics context is not null
                // Create the image to render to
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                // Create a graphics context for the image
            g2D = img.createGraphics();
                // Configure the image graphics context to match the given 
                // graphics context
            g2D.setFont(g.getFont());
            g2D.setColor(g.getColor());
                // Translate the image graphics context so that the coordinate 
                // space for the image graphics context matches the given 
                // graphics context
            g2D.translate(-1*x, -1*y);
        }
        else                            //If the graphics conext is somehow null
            return;
        paintIcon2D(c,g2D,x,y);
        if (img != null){               // If this rendered to an image
            g2D.dispose();
            g.drawImage(img, x, y, width, height, c);
        }
        g.dispose();
    }
    /**
     * This draws the icon at the specified location. Icon implementations may 
     * use the given Component to get properties useful for painting, such as 
     * the foreground or background. The graphics context passed to this method 
     * by {@link #paintIcon paintIcon} is always a scratch Graphics2D object.
     * @param c A {@code Component} to get useful properties for painting.
     * @param g The graphics context to render to.
     * @param x The x-coordinate of the icon's top-left corner.
     * @param y The y-coordinate of the icon's top-left corner.
     * @see #paintIcon
     */
    public void paintIcon2D(Component c, Graphics2D g, int x, int y);
}
