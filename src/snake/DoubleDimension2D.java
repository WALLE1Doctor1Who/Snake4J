/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake;

import java.awt.geom.Dimension2D;

/**
 * This is an implementation of {@link Dimension2D} that stores a width and 
 * height in double precision floating point numbers. Normally the width and 
 * height are non-negative, but there is nothing to prevent them from being 
 * negative. Unexpected behavior may occur if the width or height are negative.
 * @author Milo Steier
 */
public class DoubleDimension2D extends Dimension2D{
    /**
     * The width of the dimension.
     */
    public double width;
    /**
     * The height of the dimension.
     */
    public double height;
    /**
     * This constructs a DoubleDimension2D with the given width and height. 
     * @param width The width.
     * @param height The height.
     */
    public DoubleDimension2D(double width, double height){
        this.width = width;
        this.height = height;
    }
    /**
     * This constructs a DoubleDimension2D with the width and height of the 
     * given dimensions.
     * @param dim The dimensions to get the width and height from.
     */
    public DoubleDimension2D(Dimension2D dim){
        this(dim.getWidth(),dim.getHeight());
    }
    /**
     * This constructs a DoubleDimension2D with a width and height of zero.
     */
    public DoubleDimension2D(){
        this(0,0);
    }
    @Override
    public double getWidth() {
        return width;
    }
    @Override
    public double getHeight() {
        return height;
    }
    @Override
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
    @Override
    public String toString(){
        return getClass().getName()+"[width="+width+",height="+height+"]";
    }
}
