/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.playfield;

import java.awt.*;
import java.awt.geom.*;
import java.util.Objects;
import javax.swing.*;
import snake.*;
import snake.event.*;

/**
 * This is the default implementation of PlayFieldRenderer. This will render a 
 * rectangular grid of square tiles of equal size.
 * @author Milo Steier
 * @see PlayFieldRenderer
 * @see JPlayField
 */
public class DefaultPlayFieldRenderer implements PlayFieldRenderer {
    /**
     * This constructs a DefaultPlayFieldRenderer.
     */
    public DefaultPlayFieldRenderer(){
    };
    /**
     * This returns the size for the given JPlayField based off its model and 
     * the given multiplier. The width is calculated by multiplying the {@link 
     * JPlayField#getColumnCount() column count} by the {@code multiplier}, 
     * which is then added to the left and right insets. The height is 
     * calculated by multiplying the {@link JPlayField#getRowCount() row count} 
     * by the {@code multiplier}, which is then added to the top and bottom 
     * insets.
     * @param c The JPlayField to get the size for.
     * @param multiplier The multiplier to use to multiply the row and column 
     * count to get the width and height to get the size.
     * @return The dimensions derived from the JPlayField and multiplier, or 
     * null if the JPlayField or its model are null.
     * @see JPlayField#getModel 
     * @see JPlayField#getRowCount 
     * @see JPlayField#getColumnCount 
     * @see JPlayField#getInsets 
     */
    protected Dimension getSizeFromModel(JPlayField c, int multiplier){
            // If the play field or its model are null
        if (c == null || c.getModel() == null)
            return null;
            // Get the dimensions with the columns, rows, and multiplier
        Dimension dim = new Dimension(c.getColumnCount()*multiplier,
                c.getRowCount()*multiplier);
        Insets insets = c.getInsets();  // Get the insets from the play field
        if (insets != null){            // If the insets are not null
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;
        }
        return dim;
    }
    /**
     * {@inheritDoc } <p>
     * 
     * The minimum size will be calculated by adding the {@link 
     * JPlayField#getColumnCount() column count} to the left and right insets 
     * to get the width, and by adding the {@link JPlayField#getRowCount() row 
     * count} to the top and bottom insets to get the height. If the JPlayField 
     * and/or its model are null, then this will return null.
     * 
     * @param c {@inheritDoc }
     * @return {@inheritDoc }
     * @see JPlayField#getMinimumSize 
     * @see JComponent#getMinimumSize 
     * @see JPlayField#getModel 
     * @see JPlayField#getColumnCount 
     * @see JPlayField#getRowCount 
     */
    @Override
    public Dimension getMinimumSize(JPlayField c){
        return getSizeFromModel(c,1);
    }
    /**
     * {@inheritDoc } <p>
     * 
     * This returns a maximum size of {@link Integer#MAX_VALUE} x {@link 
     * Integer#MAX_VALUE}.
     * 
     * @param c {@inheritDoc }
     * @return {@inheritDoc }
     * @see JPlayField#getMaximumSize 
     * @see JComponent#getMaximumSize 
     */
    @Override
    public Dimension getMaximumSize(JPlayField c){
        return new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE);
    }
    /**
     * {@inheritDoc } <p>
     * 
     * The preferred size will be calculated by multiplying the {@link 
     * JPlayField#getColumnCount() column count} by 32 and then adding the left 
     * and right insets to get the width, and by multiplying the {@link 
     * JPlayField#getRowCount() row count} by 32 and then adding the top and 
     * bottom insets to get the height. If the JPlayField and/or its model are 
     * null, then this will return null.
     * 
     * @param c {@inheritDoc }
     * @return {@inheritDoc }
     * @see JPlayField#getPreferredSize 
     * @see JComponent#getPreferredSize 
     * @see JPlayField#getModel 
     * @see JPlayField#getColumnCount 
     * @see JPlayField#getRowCount 
     */
    @Override
    public Dimension getPreferredSize(JPlayField c){
        return getSizeFromModel(c,32);
    }
    /**
     * This checks to see if the source of the event is a component, and if so, 
     * revalidate and repaints it.
     * @param source The source of the event being processed.
     * @see Component#revalidate 
     * @see Component#repaint 
     */
    private void repaintField(Object source){
        if (source instanceof Component){       // If the object is a component
            Component c = (Component)source;    // Get the object as a component
            c.revalidate();
            c.repaint();
        }
    }
    /**
     * {@inheritDoc } 
     */
    @Override
    public void tilesAdded(PlayFieldEvent evt) {
        repaintField(evt.getSource());
    }
    /**
     * {@inheritDoc } 
     */
    @Override
    public void tilesRemoved(PlayFieldEvent evt) {
        repaintField(evt.getSource());
    }
    /**
     * {@inheritDoc } 
     */
    @Override
    public void tilesChanged(PlayFieldEvent evt) {
        // TODO: Remove the commented out code if it turns out there is no 
        // significant downside to repainting the entire play field when a tile 
        // changes. However, if there is a downside, rework the commented out 
        // code to work with the current system.
//            // If the play field has changed completely
//        if (evt.getFirstRow() < 0 || evt.getFirstColumn() < 0){
//            repaintField(evt.getSource());
//        }   // If the source is a JPlayField
//        else if (evt.getSource() instanceof JPlayField){
//                // Get the JPlayField that originated the event
//            JPlayField c = (JPlayField)evt.getSource();
//                // Get the bounds of the region of tiles that were affected
//            Rectangle2D bounds = getTileBounds(c,evt.getFirstRow(),
//                    evt.getLastRow(),evt.getFirstColumn(),evt.getLastColumn());
//                // If the returned bounds is not null
//            if (bounds != null){    
//                    // Get the bound in integer precision
//                Rectangle rect = bounds.getBounds();
//                    // Expand it by one pixel in all directions to ensure that 
//                    // the affected tiles are completely repainted
//                rect.grow(1, 1);    
//                c.repaint(rect);
//            }
//            else
//                c.repaint();
//        }
        repaintField(evt.getSource());
    }
    /**
     * This checks to see if either the given JPlayField or its {@link 
     * JPlayField#getModel() model} are null, and if so, throws a 
     * NullPointerException.
     * @param c The JPlayField to check.
     * @return The given JPlayField.
     * @throws NullPointerException If {@code c} or its model are null.
     * @see JPlayField#getModel 
     */
    protected JPlayField requireNonNullModel(JPlayField c){
        Objects.requireNonNull(Objects.requireNonNull(c).getModel());
        return c;
    }
    /**
     * {@inheritDoc } <p>
     * 
     * This forwards the call to {@link 
     * SnakeUtilities#calculateTileSize SnakeUtilities.calculateTileSize} with 
     * the given JPlayField's {@link JPlayField#getModel() model} and the size 
     * of the {@link #getPlayFieldBounds(JPlayField) play field bounds}.
     * 
     * @param c {@inheritDoc }
     * @param dim {@inheritDoc }
     * @return {@inheritDoc } 
     * @throws NullPointerException If either {@code c} or its model are null.
     * @see SnakeUtilities#calculateTileSize
     * @see JPlayField#getModel 
     * @see #getTileSize(JPlayField) 
     * @see #getTileBounds 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see JPlayField#getTileSize(Dimension2D) 
     * @see JPlayField#getTileSize() 
     */
    @Override
    public Dimension2D getTileSize(JPlayField c, Dimension2D dim) {
            // Check if the component or its model are null
        requireNonNullModel(c);
        Rectangle2D rect = getPlayFieldBounds(c);
        return SnakeUtilities.calculateTileSize(c.getModel(), rect.getWidth(), 
                rect.getHeight(), dim);
    }
    /**
     * {@inheritDoc } <p>
     * 
     * This forwards the call to {@link 
     * SnakeUtilities#calculatePlayFieldBounds(PlayFieldModel, Rectangle2D, 
     * Rectangle2D) SnakeUtilities.calculatePlayFieldBounds} with the given 
     * JPlayField's {@link JPlayField#getModel() model} and {@link 
     * SwingUtilities#calculateInnerArea inner area}. 
     * 
     * @param c {@inheritDoc }
     * @param rect {@inheritDoc }
     * @return {@inheritDoc } 
     * @throws NullPointerException If either {@code c} or its model are null.
     * @see SnakeUtilities#calculatePlayFieldBounds(PlayFieldModel, double, 
     * double, double, double, Rectangle2D) 
     * @see SnakeUtilities#calculatePlayFieldBounds(PlayFieldModel, Rectangle2D, 
     * Rectangle2D) 
     * @see JPlayField#getModel 
     * @see SwingUtilities#calculateInnerArea 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getTileSize(JPlayField) 
     * @see #getTileBounds 
     * @see JPlayField#getPlayFieldBounds(Rectangle2D) 
     * @see JPlayField#getPlayFieldBounds() 
     */
    @Override
    public Rectangle2D getPlayFieldBounds(JPlayField c, Rectangle2D rect) {
            // Check if the component or its model are null
        requireNonNullModel(c);
        return SnakeUtilities.calculatePlayFieldBounds(c.getModel(),
                SwingUtilities.calculateInnerArea(c, null),rect);
    }
    /**
     * {@inheritDoc }
     * 
     * @throws NullPointerException If either {@code c} or its model are null.
     * @see #tileToLocation(JPlayField, int, int) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getTileSize(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see JPlayField#containsTile 
     * @see JPlayField#getTileBounds(int, int, int, int) 
     * @see JPlayField#getTileBounds(int, int) 
     * @see JPlayField#getTileBounds(Tile) 
     * @see JPlayField#getModel 
     */
    @Override
    public Rectangle2D getTileBounds(JPlayField c,int row0,int row1,int column0,
            int column1) {
            // Check if the component or its model are null
        requireNonNullModel(c);
            // Get the size of the tiles
        Dimension2D tileS = getTileSize(c);
            // Get the bounds of the play field
        Rectangle2D fieldR = getPlayFieldBounds(c);
            // This gets the bounds of the tiles at the locations. If the 
            // smaller row and column are out of bounds, this will return null. 
            // If the larger row and column are out of bounds, the bounds of the 
            // first tile will be returned. The shape returned will most likely 
            // be a Rectangle2D object when not null, though that is not 
            // guaranteed.
        RectangularShape bounds = setFrameFromTileDiagonal(null,c,row0,row1,
                column0,column1,tileS,fieldR,0,0,0,0,null,null);
            // If the bounds shape is a Rectangle2D object
        if (bounds instanceof Rectangle2D)
            return (Rectangle2D) bounds;
        else    // If the bounds shape is not null, get the bounds of the shape
                // and return that. Otherwise, return null
            return (bounds != null) ? bounds.getBounds2D() : null;
    }
    /**
     * This stores the origin of the tile at the given row and column in the 
     * given JPlayField, in the given JPlayField's coordinate system, into the 
     * given Point2D object. If the given Point2D object is null, then a new 
     * Point2D object will be returned. If the given row and/or column are 
     * invalid, then this returns null. <p>
     * 
     * The {@link #tileToLocation(JPlayField, int, int) tileToLocation} method 
     * delegates the task of getting the tile's origin to this method. This 
     * method is also used to get the tile's origin when the {@link 
     * #getTileSize(JPlayField, Dimension2D) tile size} and {@link 
     * #getPlayFieldBounds(JPlayField, Rectangle2D) play field bounds} are 
     * known. 
     * 
     * @param c The JPlayField which is being queried. This cannot be null.
     * @param row The row of the tile to get the location of.
     * @param column The column of the tile to get the location of.
     * @param tileS The size of the tiles. This cannot be null.
     * @param fieldR The bounds of the play field. This cannot be null.
     * @param point The Point2D object to return with the origin of the tile, or 
     * null.
     * @return The origin of the tile, or null.
     * @throws NullPointerException If either {@code c}, its model, {@code 
     * tileS}, or {@code fieldR} are null.
     * @see #tileToLocation(JPlayField, int, int) 
     * @see #getTileSize(JPlayField) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getTileBounds 
     * @see JPlayField#getModel
     * @see JPlayField#containsTile
     * @see #setFrameFromTileDiagonal(RectangularShape, JPlayField, int, int, 
     * int, int, Dimension2D, Rectangle2D, double, double, double, double, 
     * Point2D, Point2D) 
     * @see #setFrameFromTileDiagonal(RectangularShape, JPlayField, Tile, Tile, 
     * Dimension2D, Rectangle2D, double, double, double, double, Point2D, 
     * Point2D) 
     */
    protected Point2D tileToLocation(JPlayField c, int row, int column, 
            Dimension2D tileS, Rectangle2D fieldR, Point2D point){
            // Check if the component or its model are null
        requireNonNullModel(c);
            // If the play field does not contain the row and/or column
        if (!c.containsTile(row, column))
            return null;
        if (point == null)  // If the given point is null
            point = new Point2D.Double();
            // Calculate the x by multiplying the tile width by the column and 
            // adding the play field bounds x-coordinate, and calculate the y by 
            // multiplying the tile height by the row and adding the play field 
            // bounds y-coordinate.
        point.setLocation(fieldR.getX()+(tileS.getWidth()*column), 
                fieldR.getY()+(tileS.getHeight()*row));
        return point;
    }
    /**
     * This sets the frame of the given rectangular shape based off the diagonal 
     * produced by the tiles at the given rows {@code r0} and {@code r1} and the 
     * columns {@code c0} and {@code c1}. Note that {@code r0} does not 
     * necessarily need to be less than or equal to {@code r1}, and that {@code 
     * c0} does not necessarily need to be less than or equal to {@code c1}. The 
     * diagonal will start at the upper-left corner of the tile at the smaller 
     * row and column, and end at the lower-right corner of the tile at the 
     * larger row and column. If the given rectangular shape is null, then a new 
     * Rectangle2D object will be created and returned. <p>
     * 
     * If either of the smaller row or column are outside the play field's range 
     * of tiles, then this method will return null. If the smaller row and 
     * column are both valid, but either of the larger row or column are outside 
     * the play field's range, then only the tile at the smaller row and column 
     * will be used. Otherwise, the frame will be set based off the diagonal 
     * starting from the upper-left corner of the first tile and ending at the 
     * lower-right corner of the second tile. This uses the {@link 
     * #tileToLocation tileToLocation} method to get the upper-left corner of 
     * each tile, and adds the tile width and height to the origin of the second 
     * tile to get the lower-right corner. <p>
     * 
     * The {@code xOff1}, {@code yOff1}, {@code xOff2}, and {@code yOff2} 
     * values can be used to modify the diagonal by applying offsets to the 
     * corners of the tiles. The {@code xOff1} and {@code yOff1} offsets are 
     * added to the x and y of the upper-left corner of the first tile, 
     * respectively, and the {@code xOff2} and {@code yOff2} offsets are 
     * subtracted to the x and y of the lower-right corner of the second tile, 
     * respectively. This can be used to, for example, get the shape for the 
     * contents of one or more tiles by applying the offset for the tile 
     * contents to both ends. This can also be used to get the bounds for a 
     * range of tiles. <p>
     * 
     * The {@code point1} and {@code point2} objects are scratch Point2D objects 
     * to be used to store the origins of the tiles. These are provided to allow 
     * the reuse of Point2D objects as opposed to creating new ones. However, 
     * {@code point1} and {@code point2} must be two separate instances of 
     * Point2D. They cannot be the same instance of Point2D, as that may result 
     * in the retrieved origin of the first tile being overwritten when getting 
     * the origin of the second tile. If either of the two point objects are 
     * null, then new Point2D objects will be used instead. 
     * 
     * @param shape The shape to set the frame of. If this is null, then a 
     * Rectangle2D object will be returned instead.
     * @param c The JPlayField which is being queried. This cannot be null.
     * @param r0 One end of the range of rows to get the diagonal of.
     * @param r1 The other end of the range of rows to get the diagonal of.
     * @param c0 One end of the range of columns to get the diagonal of.
     * @param c1 The other end of the range of columns to get the diagonal of.
     * @param tileS The size of the tiles. This cannot be null.
     * @param fieldR The bounds of the play field. This cannot be null.
     * @param xOff1 The x offset for the start point of the diagonal.
     * @param yOff1 The y offset for the start point of the diagonal.
     * @param xOff2 The x offset for the end point of the diagonal.
     * @param yOff2 The y offset for the end point of the diagonal.
     * @param point1 A scratch Point2D object to use to get the origin of the 
     * first tile (start point of the diagonal), or null.
     * @param point2 A scratch Point2D object to use to get the origin of the 
     * second tile (added to the tile size to get the end point of the 
     * diagonal), or null.
     * @return The {@code shape} (or a new Rectangle2D object if the {@code 
     * shape} is null) with its frame set based off the diagonal for the 
     * specified tiles, or null.
     * @throws NullPointerException If either {@code c}, its model, {@code 
     * tileS}, or {@code fieldR} are null.
     * @see #tileToLocation(JPlayField, int, int, Dimension2D, Rectangle2D, 
     * Point2D) 
     * @see #tileToLocation(JPlayField, int, int) 
     * @see #setFrameFromTileDiagonal(RectangularShape, JPlayField, Tile, Tile, 
     * Dimension2D, Rectangle2D, double, double, double, double, Point2D, 
     * Point2D) 
     * @see #getTileSize(JPlayField) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getTileBounds 
     * @see #getTileContentsOffset
     * @see JPlayField#containsTile
     */
    protected RectangularShape setFrameFromTileDiagonal(RectangularShape shape, 
            JPlayField c, int r0, int r1, int c0, int c1, Dimension2D tileS, 
            Rectangle2D fieldR, double xOff1, double yOff1, double xOff2, 
            double yOff2, Point2D point1, Point2D point2){
            // If point1 is not null and the two point objects are the same 
        if (point1 != null && point1 == point2)     // instance of Point2D
            point2 = null;
            // Get the origin of the first tile
        point1 = tileToLocation(c,Math.min(r0,r1),Math.min(c0,c1),tileS,fieldR,
                point1);
        if (point1 == null) // If the origin is null (tile is out of bounds)
            return null;
            // Get the origin of the other tile
        point2 = tileToLocation(c,Math.max(r0,r1),Math.max(c0,c1),tileS,fieldR,
                point2);
        if (point2 == null) // If the other tile's origin is null (tile is out 
                // of bounds), use the origin of the first tile
            point2 = point1;
        if (shape == null)  // If the shape is null
            shape = new Rectangle2D.Double();
        shape.setFrameFromDiagonal(point1.getX()+xOff1, point1.getY()+yOff1, 
                point2.getX()+tileS.getWidth()-xOff2, 
                point2.getY()+tileS.getHeight()-yOff2);
        return shape;
    }
    /**
     * This sets the frame of the given rectangular shape based off the diagonal 
     * produced by the origins of the given tiles. Note that {@code tile0}'s row 
     * and column do not necessarily need to be less than or equal to {@code 
     * tile1}'s row and column, respectively. The diagonal will start at the 
     * upper-left corner of the tile with the smaller row and column, and end at 
     * the lower-right corner of the tile with the larger row and column. If the 
     * given rectangular shape is null, then a Rectangle2D object will be 
     * created and returned. <p>
     * 
     * If both tiles are null, then this will return null. If only one of the 
     * tiles is null, then only the non-null tile will be used. If the tile with 
     * the smaller row and column is outside of the play field, then this method 
     * will return null. If the tile with the smaller row and column is within 
     * range, but the tile with the larger row and column is not, then only the 
     * tile with the smaller row and column will be used. Otherwise, the frame 
     * will be set based off the diagonal starting from the upper-left corner of 
     * the first tile and ending at the lower-right corner of the second tile. 
     * This uses the {@link #tileToLocation tileToLocation} method to get the 
     * upper-left corner of each tile, and adds the tile width and height to the 
     * origin of the second tile to get the lower-right corner. <p>
     * 
     * The {@code xOff1}, {@code yOff1}, {@code xOff2}, and {@code yOff2} 
     * values can be used to modify the diagonal by applying offsets to the 
     * corners of the tiles. The {@code xOff1} and {@code yOff1} offsets are 
     * added to the x and y of the upper-left corner of the first tile, 
     * respectively, and the {@code xOff2} and {@code yOff2} offsets are 
     * subtracted to the x and y of the lower-right corner of the second tile, 
     * respectively. This can be used to, for example, get the shape for the 
     * contents of one or more tiles by applying the offset for the tile 
     * contents to both ends. This can also be used to get the bounds for a 
     * range of tiles. <p>
     * 
     * The {@code point1} and {@code point2} objects are scratch Point2D objects 
     * to be used to store the origins of the tiles. These are provided to allow 
     * the reuse of Point2D objects as opposed to creating new ones. However, 
     * {@code point1} and {@code point2} must be two separate instances of 
     * Point2D. They cannot be the same instance of Point2D, as that may result 
     * in the retrieved origin of the first tile being overwritten when getting 
     * the origin of the second tile. If either of the two point objects are 
     * null, then new Point2D objects will be used instead. <p>
     * 
     * This is effectively equivalent to calling {@link 
     * #setFrameFromTileDiagonal setFrameFromTileDiagonal}{@code (shape, c, 
     * tile0.}{@link Tile#getRow() getRow()}{@code , tile1.}{@link 
     * Tile#getRow() getRow()}{@code , tile0.}{@link Tile#getColumn() 
     * getColumn()}{@code , tile1.}{@link Tile#getColumn() getColumn()}{@code , 
     * tileS, fieldR, xOff1, yOff1, xOff2, yOff2, point1, point2)}.
     * 
     * @param shape The shape to set the frame of. If this is null, then a 
     * Rectangle2D object will be returned instead.
     * @param c The JPlayField which is being queried. This cannot be null.
     * @param tile0 One of the tiles to get the diagonal between, or null. 
     * @param tile1 The other tile to get the diagonal between, or null. 
     * @param tileS The size of the tiles. This cannot be null.
     * @param fieldR The bounds of the play field. This cannot be null.
     * @param xOff1 The x offset for the start point of the diagonal.
     * @param yOff1 The y offset for the start point of the diagonal.
     * @param xOff2 The x offset for the end point of the diagonal.
     * @param yOff2 The y offset for the end point of the diagonal.
     * @param point1 A scratch Point2D object to use to get the origin of the 
     * first tile (start point of the diagonal), or null.
     * @param point2 A scratch Point2D object to use to get the origin of the 
     * second tile (added to the tile size to get the end point of the 
     * diagonal), or null.
     * @return The {@code shape} (or a new Rectangle2D object if the {@code 
     * shape} is null) with its frame set based off the diagonal for the 
     * specified tiles, or null.
     * @throws NullPointerException If either {@code c}, its model, {@code 
     * tileS}, or {@code fieldR} are null.
     * @see #tileToLocation(JPlayField, int, int, Dimension2D, Rectangle2D, 
     * Point2D) 
     * @see #tileToLocation(JPlayField, int, int) 
     * @see #setFrameFromTileDiagonal(RectangularShape, JPlayField, int, int, 
     * int, int, Dimension2D, Rectangle2D, double, double, double, double, 
     * Point2D, Point2D) 
     * @see #getTileSize(JPlayField) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getTileContentsOffset 
     * @see JPlayField#containsTile 
     * @see JPlayField#getTile 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     */
    protected RectangularShape setFrameFromTileDiagonal(RectangularShape shape, 
            JPlayField c, Tile tile0, Tile tile1, Dimension2D tileS, 
            Rectangle2D fieldR, double xOff1, double yOff1, double xOff2, 
            double yOff2, Point2D point1, Point2D point2){
        if (tile0 == null && tile1 == null) // If both tiles are null
            return null;
        else if (tile0 == null)             // If the first tile is null
            tile0 = tile1;
        else if (tile1 == null)             // If the second tile is null
            tile1 = tile0;
        return setFrameFromTileDiagonal(shape,c,tile0.getRow(),tile1.getRow(),
                tile0.getColumn(),tile1.getColumn(),tileS,fieldR,xOff1,yOff1,
                xOff2,yOff2,point1,point2);
    }
    /**
     * {@inheritDoc }
     * @param c {@inheritDoc }
     * @param row {@inheritDoc }
     * @param column {@inheritDoc }
     * @return {@inheritDoc }
     * @throws NullPointerException If either {@code c} or its model are null.
     * @see #getTileBounds 
     * @see #locationToTile 
     * @see #locationToTile2D 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getTileSize(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see JPlayField#containsTile 
     * @see JPlayField#tileToLocation(int, int) 
     * @see JPlayField#tileToLocation(playfield.Tile) 
     * @see JPlayField#getModel 
     */
    @Override
    public Point2D tileToLocation(JPlayField c,int row,int column){
        return tileToLocation(c,row,column,getTileSize(c),getPlayFieldBounds(c),
                null);
    }
    /**
     * This returns the tile in the given JPlayField that is closest to the 
     * given location in the JPlayField's coordinate system. <p>
     * 
     * The {@link #locationToTile(JPlayField, int, int) locationToTile} and 
     * {@link #locationToTile2D(JPlayField, double, double) locationToTile2D} 
     * methods delegate to this method to get the tile at a given location.
     * 
     * @param c The JPlayField which is being queried. This cannot be null.
     * @param x The x-coordinate of the point to get the tile at.
     * @param y The y-coordinate of the point to get the tile at.
     * @param fieldR The bounds of the play field. This cannot be null.
     * @return The tile closest to the given location, or null.
     * @throws NullPointerException If either {@code c}, its model, or {@code 
     * fieldR} are null.
     * @see JPlayField#getModel() 
     * @see #locationToTile 
     * @see #locationToTile2D 
     * @see #getTileBounds 
     * @see #getTileSize(JPlayField) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     */
    private Tile locationToTile(JPlayField c,double x,double y,Rectangle2D fieldR){
            // Check if the component or its model are null
        requireNonNullModel(c);
            // If the play field does not contain the point
        if (!fieldR.contains(x, y)) 
            return null;
        x -= fieldR.getX();
        y -= fieldR.getY();
        Dimension2D tileS = getTileSize(c);     // Get the tile size
            // Calculate the row and column based off the x and y and the tile 
            // width and height
        return c.getTile(
                Math.min((int)Math.floor(y/tileS.getHeight()),c.getColumnCount()-1), 
                Math.min((int)Math.floor(x/tileS.getWidth()),c.getRowCount()-1));
    }
    /**
     * {@inheritDoc }
     * @throws NullPointerException If either {@code c} or its model are null.
     * @see #locationToTile 
     * @see #tileToLocation 
     * @see #getTileBounds 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getTileSize(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see JPlayField#getTile 
     * @see JPlayField#locationToTile(int, int) 
     * @see JPlayField#locationToTile(Point) 
     * @see JPlayField#locationToTile2D(double, double) 
     * @see JPlayField#locationToTile2D(Point2D) 
     * @see JPlayField#getModel 
     * @see JPlayField#getRowCount 
     * @see JPlayField#getColumnCount 
     */
    @Override
    public Tile locationToTile2D(JPlayField c, double x, double y){
        return locationToTile(c,x,y,getPlayFieldBounds(c));
    }
    /**
     * {@inheritDoc }
     * @throws NullPointerException If either {@code c} or its model are null.
     * @see #locationToTile2D 
     * @see #tileToLocation 
     * @see #getTileBounds 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getTileSize(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see JPlayField#getTile 
     * @see JPlayField#locationToTile(int, int) 
     * @see JPlayField#locationToTile(Point) 
     * @see JPlayField#locationToTile2D(double, double) 
     * @see JPlayField#locationToTile2D(Point2D) 
     * @see JPlayField#getModel 
     * @see JPlayField#getRowCount 
     * @see JPlayField#getColumnCount 
     */
    @Override
    public Tile locationToTile(JPlayField c, int x, int y){
        return locationToTile(c,x,y,getPlayFieldBounds(c).getBounds());
    }
    /**
     * This returns the offset to use when rendering the contents of the given 
     * tile for the given value. This is called twice for each tile, once with 
     * the tile's width to get the x offset, and again with the tile's height to 
     * get the y offset. <p>
     * 
     * This forwards the call to {@link SnakeUtilities#computeTileContentsOffset 
     * SnakeUtilities.computeTileContentsOffset}. This method is here so that 
     * a subclass could do JPlayField specific things or to change the offset 
     * for the contents of the tiles. Note that changing the offset will change 
     * the size at which the contents are rendered at.
     * 
     * @param c The JPlayField being rendered.
     * @param tile The tile currently being rendered.
     * @param value The value to get the offset for. This will either be the 
     * width or the height of the tile.
     * @return The offset for the contents of the tile.
     * @see SnakeUtilities#computeTileContentsOffset 
     * @see SnakeUtilities#computeTileContentsSize 
     * @see #getTileSize(JPlayField) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see Tile#isEmpty 
     * @see Tile#isApple 
     * @see Tile#isSnake 
     */
    protected double getTileContentsOffset(JPlayField c,Tile tile,double value){
        return SnakeUtilities.computeTileContentsOffset(tile,value);
    }
    /**
     * This returns the color to use for the given tile from the given 
     * JPlayField. If the tile is an {@link Tile#isApple() apple tile}, then 
     * this will return the JPlayField's {@link JPlayField#getAppleColor() apple 
     * color}. If the tile is a {@link Tile#isSnake() snake tile} with its 
     * {@link Tile#getType() type flag} set, then this will return the 
     * JPlayField's {@link JPlayField#getSecondarySnakeColor() secondary snake 
     * color}. If the tile is a {@link Tile#isSnake() snake tile} that does not 
     * have its {@link Tile#getType() type flag} set, then this will return the 
     * JPlayField's {@link JPlayField#getPrimarySnakeColor() primary snake 
     * color}. Otherwise, this returns null.
     * @param c The JPlayField to query for the color. This cannot be null.
     * @param tile The tile to get the color for.
     * @return The color for the given tile, or null.
     * @throws NullPointerException If {@code c} is null.
     * @see Tile#isEmpty 
     * @see Tile#isApple 
     * @see Tile#isSnake 
     * @see Tile#getType 
     * @see JPlayField#getAppleColor 
     * @see JPlayField#getPrimarySnakeColor 
     * @see JPlayField#getSecondarySnakeColor 
     * @see APPLE_COLOR
     * @see SECONDARY_SNAKE_COLOR
     * @see PRIMARY_SNAKE_COLOR
     * @see #paintTile
     */
    protected Color getTileColor(JPlayField c, Tile tile){
        Objects.requireNonNull(c);  // Ensure the play field is not null
        if (tile == null)           // If the tile is null
            return null;
        else if (tile.isApple())    // If the tile is an apple tile
                // Return the play field's apple color, or the default apple 
                // color if its null
            return Objects.requireNonNullElse(c.getAppleColor(),APPLE_COLOR);
        else if (tile.isSnake()){   // If the tile is a snake tile
                // If the snake tile has its type flag set, return the play 
                // field's secondary snake color (or the default secondary snake 
                // color if its null). Otherwise, return the play field's 
                // primary snake color (or the default primary snake color if 
            return (tile.getType()) ? // its null)
                    Objects.requireNonNullElse(c.getSecondarySnakeColor(),
                            SECONDARY_SNAKE_COLOR) : 
                    Objects.requireNonNullElse(c.getPrimarySnakeColor(),
                            PRIMARY_SNAKE_COLOR);
        }
        return null;
    }
    /**
     * This returns the Stroke object to use to render the border around the 
     * tiles. <p>
     * 
     * This constructs a BasicStroke using the line width calculated by {@link 
     * SnakeUtilities#computeTileBorderThickness 
     * SnakeUtilities.computeTileBorderThickness} using the smaller of the given 
     * tile width and height. This method is here so that a subclass could do 
     * JPlayField specific things or to change the Stroke used to render the 
     * border.
     * 
     * @param c The JPlayField being rendered.
     * @param tileS The size of the tiles.
     * @param fieldR The bounds of the play field.
     * @return The stroke to use to render the border, or null to use the 
     * default stroke.
     * @see SnakeUtilities#computeTileBorderThickness 
     * @see #getTileSize(JPlayField) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #paintTileBorder
     */
    protected Stroke getBorderStroke(JPlayField c, Dimension2D tileS, Rectangle2D fieldR){
        return new BasicStroke(SnakeUtilities.computeTileBorderThickness(
                Math.min(tileS.getWidth(), tileS.getHeight())));
    }
    /**
     * This returns whether this should render the tile border. <p>
     * 
     * This calls the given JPlayField's {@link JPlayField#isTileBorderPainted() 
     * isTileBorderPainted} and checks to see if the tile size is at least 3x3 
     * (since the tile border ends up effectively covering some of the tiles at 
     * tile sizes smaller than 3x3) to see if the tile border should be 
     * rendered. This method is here so that a subclass could add or remove 
     * restrictions on whether to render the tile border. <p>
     * 
     * Note that {@link #paintTileBorder paintTileBorder} will ignore this and 
     * not render the border if the Path2D object it is given is null, as it is 
     * the Path2D object that determines what to draw for the border.
     * 
     * @param c The JPlayField being rendered.
     * @param tileS The size of the tiles.
     * @param fieldR The bounds of the play field.
     * @return Whether this should render the tile border.
     * @see JPlayField#isTileBorderPainted 
     * @see #getTileSize(JPlayField) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #paintTileBorder
     */
    protected boolean getTileBorderShouldBePainted(JPlayField c, 
            Dimension2D tileS, Rectangle2D fieldR){
        return c.isTileBorderPainted() && tileS.getWidth() >= 3 && 
                tileS.getHeight() >= 3;
    }
    /**
     * {@inheritDoc } <p>
     * 
     * This method actually delegates the work of rendering the play field to 
     * four protected methods: {@code configureGraphics}, {@code 
     * paintTileBackground}, {@code paintTiles}, and {@code paintTileBorder}. 
     * They are called in the order listed to ensure that the given graphics 
     * context is configured before anything is rendered, the tiles appear on 
     * top of the tile background, and the tile border appears on top of the 
     * tiles. The {@code paintTiles} method is also responsible for outlining 
     * and returning the path {@code paintTileBorder} will take when rendering 
     * the tile border.
     * 
     * @param g {@inheritDoc }
     * @param c {@inheritDoc }
     * @param width {@inheritDoc }
     * @param height {@inheritDoc }
     * @see #configureGraphics 
     * @see #paintTileBackground 
     * @see #paintTiles 
     * @see #paintTileBorder 
     * @see JPlayField#paintComponent 
     * @see JPlayField#getWidth 
     * @see JPlayField#getHeight 
     * @see JPlayField#getModel 
     */
    @Override
    public void paint(Graphics2D g, JPlayField c, int width, int height) {
            // If the play field or its model are null, or if either the width, 
            // height, play field width, or play field height are less than or 
            // equal to zero
        if (c == null || c.getModel() == null || width <= 0 || height <= 0 || 
                c.getWidth() <= 0 || c.getHeight() <= 0)
            return;
        try{    // Scale it so that we can just use the width and height of the 
                // play field instead of the given width and height (it will 
                // most likely be the play field's width and height anyway)
            g.scale(((double)width)/c.getWidth(),((double)height)/c.getHeight());
                // Get the size for the tiles
            Dimension2D tileS = getTileSize(c);
                // Get the bounds for the play field
            Rectangle2D fieldR = getPlayFieldBounds(c);
                // Configure the graphics
            g = configureGraphics(g,c); 
                // Paint the tile background
            paintTileBackground(g,c,tileS,fieldR);
                // Paint the tiles and get the path for the border
            Path2D borderP = paintTiles(g,c,tileS,fieldR);
                // Paint the border
            paintTileBorder(g,c,tileS,fieldR,borderP);
        }
        catch(NullPointerException ex) {}
    }
    /**
     * This is used to configure the graphics context used to render the play 
     * field. It's assumed that the returned graphics context is the same as the 
     * given graphics context, or at least that the returned graphics context 
     * references the given graphics context in some way. 
     * @param g The graphics context to render to.
     * @param c The JPlayField being rendered.
     * @return The given graphics context, now configured for rendering the play 
     * field.
     * @see JPlayField#isHighQuality 
     * @see #paint 
     */
    protected Graphics2D configureGraphics(Graphics2D g, JPlayField c){
            // If the play field is to be rendered in high quality
        if (c.isHighQuality()){     
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, 
                    RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, 
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_DITHERING, 
                    RenderingHints.VALUE_DITHER_ENABLE);
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, 
                    RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        }
        else{
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_OFF);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, 
                    RenderingHints.VALUE_RENDER_SPEED);
        }
        return g;
    }
    /**
     * This is used to render the tile background. If the {@link 
     * JPlayField#isTileBackgroundPainted() tile background is to be painted}, 
     * then this will fill the play field area (the area defined by {@code 
     * fieldR}) with the {@link JPlayField#getTileBackground() tile background 
     * color}. This renders to a copy of the given graphics context, so as to 
     * protect the rest of the paint code from changes made to the graphics 
     * context while rendering the tile background.
     * @param g The graphics context to render to.
     * @param c The JPlayField being rendered.
     * @param tileS The size of the tiles.
     * @param fieldR The bounds of the play field.
     * @see #paint 
     * @see JPlayField#isTileBackgroundPainted 
     * @see JPlayField#getTileBackground 
     * @see TILE_BACKGROUND_COLOR
     * @see #getTileSize(JPlayField) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     */
    protected void paintTileBackground(Graphics2D g, JPlayField c, 
            Dimension2D tileS, Rectangle2D fieldR){
            // If the tile background is to be painted
        if (c.isTileBackgroundPainted()){
            g = (Graphics2D) g.create();
                // Use the play field's tile background color if its not null.
                // Otherwise, use the default tile background color
            g.setColor(Objects.requireNonNullElse(c.getTileBackground(),
                    TILE_BACKGROUND_COLOR));
            g.fill(fieldR);
            g.dispose();
        }
    }
    /**
     * This is used to render the tiles in the play field. This delegates the 
     * task of rendering each tile to the {@link #paintTile paintTile} method. 
     * The {@code paintTile} method is passed a copy of the given graphics 
     * context, so as to protect the rest of the paint code from changes made to 
     * the graphics context while rendering the tiles. The {@code paintTile} 
     * method is also given a scratch Ellipse2D object, a scratch Rectangle2D 
     * object, two scratch Point2D objects, and an array of tiles to use as a 
     * buffer for the snake segments. As this is going through the tiles and 
     * having {@code paintTile} render each one, this is also constructing a 
     * Path2D object that will be passed to {@link #paintTileBorder 
     * paintTileBorder} to use to render the border.
     * 
     * @param g The graphics context to render to.
     * @param c The JPlayField being rendered.
     * @param tileS The size of the tiles.
     * @param fieldR The bounds of the play field.
     * @return The Path2D object with the path to use to render the border, or 
     * null.
     * @see #paint
     * @see #paintTile
     * @see #paintTileBorder
     * @see #getTileSize(JPlayField) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getTileBounds 
     * @see #tileToLocation
     * @see JPlayField#getRowCount 
     * @see JPlayField#getColumnCount
     * @see JPlayField#getTile
     * @see JPlayField#getModel
     * @see Tile#getRow
     * @see Tile#getColumn
     * @see Tile#getState
     */
    protected Path2D paintTiles(Graphics2D g, JPlayField c, Dimension2D tileS, 
            Rectangle2D fieldR){
        g = (Graphics2D) g.create();
        g.clip(fieldR);
            // The path object to return for rendering the border
        Path2D borderP = new Path2D.Double(fieldR);
            // An ellipse to use to get the center circle of the tiles
        Ellipse2D circle = new Ellipse2D.Double();
            // A rectangle to use to get snake segments
        Rectangle2D rect = new Rectangle2D.Double();
            // A point object to use for the tile diagonal calculations
        Point2D point1 = new Point2D.Double();
            // A second point object to use for the tile diagonal calculations
        Point2D point2 = new Point2D.Double();
            // An array of tiles to contain the tiles at the start of the 
            // current snake segments. The last one in the array (index is 
            // c.getColumnCount()) is the start of the current horizontal snake 
            // segment, and the rest are the start of the vertical snake 
            // segments on their column.
        Tile[] start = new Tile[c.getColumnCount()+1];
            // A for loop to go through the rows of tiles
        for (int r = 0; r < c.getRowCount(); r++){
                // A for loop to go through the tiles in the current row
            for (int col = 0; col < c.getColumnCount(); col++){
                    // Get the tile at the current row and column
                Tile tile = c.getTile(r, col);
                    // If the tile is not null, paint the tile
                if (tile != null)
                    paintTile(g,c,tile,tileS,fieldR,start,circle,rect,point1,
                            point2);
                    // If this is the first row and not the first column (this 
                    // is to prevent adding repeated vertical lines to the 
                if (r == 0 && col > 0){     // border path)
                        // Get the origin of the first tile in the column, so as 
                        // to get the x-coordinate for the vertical border line 
                        // between this column and the previous column
                    point1 = tileToLocation(c,0,col,tileS,fieldR,point1);
                    borderP.moveTo(point1.getX(), fieldR.getMinY());
                    borderP.lineTo(point1.getX(), fieldR.getMaxY());
                }
            }   // If this is not the first row (this is to prevent adding 
            if (r > 0){ // repeated horizontal lines to the border path)
                    // Get the origin of the first tile in the row, so as to get 
                    // the y-coordinate for the horizontal border line between 
                    // this row and the previous row
                point1 = tileToLocation(c,r,0,tileS,fieldR,point1);
                borderP.moveTo(fieldR.getMinX(), point1.getY());
                borderP.lineTo(fieldR.getMaxX(), point1.getY());
            }
        }
        g.dispose();
        return borderP;
    }
    /**
     * This is used to render the given tile in the play field. This is called 
     * by {@link #paintTiles paintTiles} with the tile currently being rendered. 
     * <p>
     * 
     * When rendering a tile, it is first checked to see what kind tile it is. 
     * An {@link Tile#isEmpty() empty tile} results in this method doing 
     * nothing since empty tiles are, well, empty, and thus have nothing to 
     * render. If the tile is not empty, then the color to use for the tile is 
     * retrieved using the {@link #getTileColor getTileColor} method, and the x 
     * and y offsets for the contents of the tile are retrieved using the {@link 
     * #getTileContentsOffset getTileContentsOffset} method using the width and 
     * height from the given tile size, respectively. The tile is then checked 
     * to see if it is either an {@link Tile#isApple() apple tile} or if it at 
     * least isn't facing opposing directions simultaneously (i.e. the tile is 
     * not facing both {@link Tile#isFacingUp() up} and {@link 
     * Tile#isFacingDown() down} or both {@link Tile#isFacingLeft() left} and 
     * {@link Tile#isFacingRight() right} at the same tile), and if so, this 
     * will render the tile's center circle, as provided by the {@link 
     * #getTileCenterCircle getTileCenterCircle} method. The reason why this is 
     * checked for is to avoid pointlessly rendering the center circle if it 
     * will be covered up by a snake segment, and thus will not be visible 
     * anyway. <p>
     * 
     * Afterwards, this will call {@link #getSnakeSegment getSnakeSegment} with 
     * the given {@code startTiles} array so as to get any snake segments to be 
     * rendered. If {@code getSnakeSegment} returns a non-null Rectangle2D 
     * object, then that is what will be rendered. This is done twice, once to 
     * get the vertical snake segment to render, and again to get the horizontal 
     * snake segment to render. Refer to the documentation for {@link 
     * #getSnakeSegment getSnakeSegment} for more information on how snake 
     * segments are handled and rendered. <p>
     * 
     * While optional, this can be given an Ellipse2D object, a Rectangle2D 
     * object, and two Point2D objects to reuse when getting the shapes to use 
     * to render the tiles. This can be used to avoid the creation of new shape 
     * and point objects. However, {@code point1} and {@code point2} must be 
     * two separate instances of Point2D. If any of the shape and/or point 
     * objects are null, then new shape/point objects will be used in their 
     * place.
     * 
     * @param g The graphics context to render to.
     * @param c The JPlayField being rendered.
     * @param tile The tile currently being rendered.
     * @param tileS The size for the tile.
     * @param fieldR The bounds of the play field. 
     * @param startTiles The array storing the start tiles for the snake 
     * segments (should be 1+{@link JPlayField#getColumnCount() 
     * c.getColumnCount()} in length). 
     * @param circle An ellipse to reuse for getting the center circle, or null.
     * @param rect A rectangle to reuse for getting snake segments, or null.
     * @param point1 A scratch Point2D object to reuse while calculating the 
     * shapes for the tile, or null.
     * @param point2 A second scratch Point2D object to reuse while calculating 
     * the shapes for the tile, or null.
     * @see #paintTiles 
     * @see #getTileColor 
     * @see #getTileContentsOffset 
     * @see #getTileCenterCircle 
     * @see #getSnakeSegment 
     * @see #getSnakeSegmentRectangle 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see Tile#getState 
     * @see Tile#getFlag 
     * @see Tile#isEmpty 
     * @see Tile#isApple 
     * @see Tile#isSnake 
     * @see Tile#isFacingUp 
     * @see Tile#isFacingDown
     * @see Tile#isFacingLeft 
     * @see Tile#isFacingRight
     * @see Tile#getType 
     * @see JPlayField#getTile 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getTileSize(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see #getTileBounds 
     */
    protected void paintTile(Graphics2D g, JPlayField c, Tile tile, 
            Dimension2D tileS, Rectangle2D fieldR, Tile[] startTiles, 
            Ellipse2D circle, Rectangle2D rect, Point2D point1, Point2D point2){
        if (tile.isEmpty()) // If the tile is empty (nothing is to be rendered)
            return;
        g.setColor(getTileColor(c,tile));
            // Get the x offset for the tile contents
        double xOff = getTileContentsOffset(c,tile,tileS.getWidth());
            // Get the y offset for the tile contents
        double yOff = getTileContentsOffset(c,tile,tileS.getHeight());
            // If the tile is either an apple tile or the tile does not have 
            // both horizontal or both vertical direction flags set (the tile is 
            // not facing opposite directions simultaneously)
        if (tile.isApple() || (!tile.getFlag(VERTICAL_DIRECTIONS) && 
                !tile.getFlag(HORIZONTAL_DIRECTIONS))){
                // Render the center circle for the tile
            g.fill(getTileCenterCircle(c,tile,tileS,fieldR,xOff,yOff,circle,
                    point1,point2));
        }   // A for loop to render the horizontal and vertical snake segments 
        for (int i = 0; i < 2; i++){    // that may end at this tile
                // Get the rectangle for the snake segment, or null
            Rectangle2D temp = getSnakeSegment(c,tile,startTiles,tileS,fieldR,
                    xOff,yOff,i%2==0,rect,point1,point2);
            if (temp != null)   // If the snake segment is not null
                g.fill(temp);
        }
    }
    /**
     * This stores the ellipse to use to render the center circle of the given 
     * tile into the given Ellipse2D object. If {@code circle} is null, then a 
     * new Ellipse2D object will be returned. <p>
     * 
     * As the name implies, this ellipse is the circle in the center of the 
     * tile. The width and height of the ellipse are based off the given tile 
     * size and the given x and y {@link #getTileContentsOffset offsets for the 
     * tile contents}. <p>
     * 
     * The {@code point1} and {@code point2} objects are scratch Point2D objects 
     * to be used by {@link #setFrameFromTileDiagonal(RectangularShape, 
     * JPlayField, Tile, Tile, Dimension2D, Rectangle2D, double, double, double, 
     * double, Point2D, Point2D) setFrameFromTileDiagonal} to calculate the 
     * diagonal to use to set the ellipse's frame. These are provided to allow 
     * the reuse of Point2D objects as opposed to creating new ones. However, 
     * {@code point1} and {@code point2} must be two separate instances of 
     * Point2D. If either of the two point objects are null, then new Point2D 
     * objects will be used instead. 
     * 
     * @param c The JPlayField being rendered.
     * @param tile The tile currently being rendered.
     * @param tileS The size for the tile.
     * @param fieldR The bounds of the play field. 
     * @param xOff The x offset for the tile contents.
     * @param yOff The y offset for the tile contents.
     * @param circle An ellipse to reuse for getting the center circle, or null.
     * @param point1 A scratch Point2D object to reuse while calculating the 
     * diagonal, or null.
     * @param point2 A second scratch Point2D object to reuse while calculating 
     * the diagonal, or null.
     * @return The ellipse to use to render the tile's center circle.
     * @see #paintTile
     * @see #getTileContentsOffset
     * @see #setFrameFromTileDiagonal(RectangularShape, JPlayField, Tile, Tile, 
     * Dimension2D, Rectangle2D, double, double, double, double, Point2D, 
     * Point2D) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getTileSize(JPlayField) 
     * @see #getTileBounds 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see #getSnakeSegment
     * @see #getSnakeSegmentRectangle
     */
    protected Ellipse2D getTileCenterCircle(JPlayField c, Tile tile, 
            Dimension2D tileS, Rectangle2D fieldR, double xOff, double yOff, 
            Ellipse2D circle, Point2D point1, Point2D point2){
        if (circle == null)     // If the circle is null
            circle = new Ellipse2D.Double();
        setFrameFromTileDiagonal(circle,c,tile,tile,tileS,fieldR,xOff,yOff,xOff,
                yOff,point1,point2);
        return circle;
    }
    /**
     * This stores the rectangle to use to render the snake segment starting at 
     * tile {@code tile0} and ending at tile {@code tile1} into the given 
     * Rectangle2D object. If {@code rect} is null, then a new Rectangle2D 
     * object will be returned. If only one non-null tile is provided, then the 
     * snake segment will only span that tile. If both tiles are null, then this 
     * will return null. <p>
     * 
     * The {@code vertical} value determines whether this will be a vertical or 
     * horizontal snake segment. When getting a vertical snake segment, the 
     * snake segment will start at either the top or middle of the first tile, 
     * depending on whether the first tile is facing {@link Tile#isFacingDown() 
     * down} or not, and will end at either the bottom or middle of the last 
     * tile, depending on whether the last tile is facing {@link Tile#isFacingUp 
     * up} or not. When getting a horizontal snake segment, the snake segment 
     * will start at either the left side or middle of the first tile, depending 
     * on whether the first tile is facing to the {@link Tile#isFacingRight() 
     * right} or not, and will end at either the right side or middle of the 
     * last tile, depending on whether the last tile is facing to the {@link 
     * Tile#isFacingLeft() left} or not. The given x offset is only applied when 
     * getting a vertical snake segment, and the given y offset is only applied 
     * when getting a horizontal snake segment. <p>
     * 
     * The {@code point1} and {@code point2} objects are scratch Point2D objects 
     * to be used by {@link #setFrameFromTileDiagonal(RectangularShape, 
     * JPlayField, Tile, Tile, Dimension2D, Rectangle2D, double, double, double, 
     * double, Point2D, Point2D) setFrameFromTileDiagonal} to calculate the 
     * diagonal to use to set the rectangle's frame. These are provided to allow 
     * the reuse of Point2D objects as opposed to creating new ones. However, 
     * {@code point1} and {@code point2} must be two separate instances of 
     * Point2D. If either of the two point objects are null, then new Point2D 
     * objects will be used instead. 
     * 
     * @param c The JPlayField being rendered.
     * @param tile0 The first tile in the range of tiles that the snake segment 
     * will span, or null.
     * @param tile1 The last tile in the range of tiles that the snake segment 
     * will span, or null.
     * @param tileS The size for the tiles.
     * @param fieldR The bounds of the play field. 
     * @param xOff The x offset for the tile contents.
     * @param yOff The y offset for the tile contents.
     * @param vertical Determines whether this will be a horizontal or vertical 
     * snake segment ({@code true} to get a vertical snake segment, {@code 
     * false} to get a horizontal snake segment).
     * @param rect A rectangle to reuse for getting the snake segment, or null.
     * @param point1 A scratch Point2D object to reuse while calculating the 
     * diagonal, or null.
     * @param point2 A second scratch Point2D object to reuse while calculating 
     * the diagonal, or null.
     * @return The rectangle to use to render the snake segment spanning the 
     * given tiles.
     * @see #paintTile 
     * @see #getSnakeSegment 
     * @see #getTileContentsOffset 
     * @see #setFrameFromTileDiagonal(RectangularShape, JPlayField, Tile, Tile, 
     * Dimension2D, Rectangle2D, double, double, double, double, Point2D, 
     * Point2D) 
     * @see Tile#isFacingUp 
     * @see Tile#isFacingDown 
     * @see Tile#isFacingLeft 
     * @see Tile#isFacingRight 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getTileSize(JPlayField) 
     * @see #getTileBounds 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     */
    protected Rectangle2D getSnakeSegmentRectangle(JPlayField c, Tile tile0, 
            Tile tile1, Dimension2D tileS, Rectangle2D fieldR, double xOff, 
            double yOff, boolean vertical, Rectangle2D rect, Point2D point1, 
            Point2D point2){
        if (tile0 == null && tile1 == null) // If both tiles are null
            return null;
        if (tile0 == null)      // If only the last tile was provided
            tile0 = tile1;
        else if (tile1 == null) // If only the first tile was provided
            tile1 = tile0;
        if (rect == null)       // If the rectangle is null
            rect = new Rectangle2D.Double();
            // These are the x and y offsets for the diagonal
        double x1, y1, x2, y2;
        if (vertical){      // If this is getting a vertical snake segment
            double half = tileS.getHeight()/2;  // Get half of the tile height
            x1 = x2 = xOff;
                // If the first tile is facing down, the segment will start at 
                // the top of the tile. Otherwise, the segment will start in the 
                // middle of the tile
            y1 = (tile0.isFacingDown())?0:half;
                // If the second tile is facing up, the segment will end at the 
                // bottom of the tile. Otherwise, the segment will end in the 
                // middle of the tile
            y2 = (tile1.isFacingUp())?0:half;
        } else {
            double half = tileS.getWidth()/2;   // Get half of the tile width
            y1 = y2 = yOff;
                // If the first tile is facing to the right, the segment will 
                // start on the left of the tile. Otherwise, the segment will 
                // start in the middle of the tile
            x1 = (tile0.isFacingRight())?0:half;
                // If the second tile is facing to the left, the segment will 
                // end on the right of the tile. Otherwise, the segment will end 
                // in the middle of the tile
            x2 = (tile1.isFacingLeft())?0:half;
        }
        setFrameFromTileDiagonal(rect,c,tile0,tile1,tileS,fieldR,x1,y1,x2,y2,
                point1,point2);
        return rect;
    }
    /**
     * This stores the rectangle to use to render the snake segment that ends at 
     * the given tile into the given Rectangle2D object and returns it. If 
     * {@code rect} is null, then a new Rectangle2D object will be returned. 
     * This will return null if the tile is not a {@link Tile#isSnake() snake 
     * tile} that contains a snake segment of the requested orientation (as 
     * determined by the {@code vertical} value) or if the snake segment should 
     * not be rendered yet. <p>
     * 
     * To improve rendering efficiency and reduce the chance for visual 
     * artifacts appearing between tiles when a transform is applied to the 
     * rendering graphics, snake segments are drawn as one large rectangle 
     * spanning multiple tiles instead of individual rectangles. Each snake 
     * segment is only rendered once the tile at the end of that snake segment 
     * has been reached. To accomplish this, a buffer is used to store the tiles 
     * at the start of the snake segment. The {@code startTiles} array is used 
     * for this purpose. The {@code startTiles} array should be at least 1 + 
     * {@link JPlayField#getColumnCount() c.getColumnCount()} in length, with 
     * the last index in that range used to store the start of the current 
     * horizontal snake segment, and the rest are used to store the start of the 
     * current vertical snake segments in the index corresponding to their 
     * column. When this reaches a tile at the start of a snake segment, then 
     * it's placed into the corresponding index in the array for the snake 
     * segment, and when the end of the snake segment is reached, the 
     * corresponding spot is set to null. <p>
     * 
     * The {@code vertical} value determines whether this will be a vertical or 
     * horizontal snake segment. Vertical snake segments depend on whether tiles 
     * are facing {@link Tile#isFacingUp() up} and/or {@link Tile#isFacingDown() 
     * down}, whereas horizontal snake segments depend on whether tiles are 
     * facing {@link Tile#isFacingLeft() left} and/or {@link Tile#isFacingRight 
     * right}. If a tile is not facing at least one of the directions listed for 
     * the snake segment that is being retrieved, then this returns null since 
     * the tile does not contain a snake segment of the requested orientation. 
     * Otherwise, this will get the tile in the {@code startTiles} array at the 
     * index corresponding to the snake segment to return. If this is getting a 
     * vertical snake segment, then the starting tile for the segment will be at 
     * the index equal to the given tile's {@link Tile#getColumn() column}. If 
     * this is getting a horizontal snake segment, then the starting tile for 
     * the segment will be at the index equal to the {@link 
     * JPlayField#getColumnCount() number of columns} in the play field (i.e. 
     * the index will be {@code c.getColumnCount()}). If the starting tile is 
     * null, then the given tile is the start of the current snake segment. This 
     * will then get the tile {@link JPlayField#getAdjacentTile adjacent} to the 
     * given tile that might be the next tile in the snake segment. That is to 
     * say, this will get the tile {@link #DOWN_DIRECTION under} the given tile 
     * if this is getting a vertical snake segment, and the tile to the {@link 
     * #LEFT_DIRECTION left} of the given tile if this getting a horizontal 
     * snake segment. If the given tile is facing up (vertical) or to the left 
     * (horizontal) and the next tile is a non-null snake tile that is facing 
     * down (vertical) or to the right (horizontal) and that is the same {@link 
     * Tile#getType type} of tile as the given tile, then the given tile is 
     * deemed to not be the end of the current snake segment and if it's the 
     * start of the segment, then it is placed into {@code startTiles} at the 
     * index corresponding to the snake segment. If the tile is not the end of 
     * the current snake segment, then this will return null. Otherwise, this 
     * will call {@link #getSnakeSegmentRectangle getSnakeSegmentRectangle} with 
     * the starting tile and the given tile to get the rectangle for the snake 
     * segment and returns it. If the end of the snake segment has been reached, 
     * then the tile at the index of the starting tile in the {@code startTiles} 
     * array will be set to null. <p>
     * 
     * The {@code point1} and {@code point2} objects are scratch Point2D objects 
     * to be used by {@link #getSnakeSegmentRectangle getSnakeSegmentRectangle} 
     * to calculate the snake segment rectangle. These are provided to allow the 
     * reuse of Point2D objects as opposed to creating new ones. However, {@code 
     * point1} and {@code point2} must be two separate instances of Point2D. If 
     * either of the two point objects are null, then new Point2D objects will 
     * be used instead. 
     * 
     * @param c The JPlayField being rendered.
     * @param tile The tile currently being rendered.
     * @param startTiles The array storing the start tiles for the snake 
     * segments (should be 1+{@link JPlayField#getColumnCount() 
     * c.getColumnCount()} in length). 
     * @param tileS The size for the tile.
     * @param fieldR The bounds of the play field. 
     * @param xOff The x offset for the tile contents.
     * @param yOff The y offset for the tile contents.
     * @param vertical Determines whether this is getting a horizontal or 
     * vertical snake segment ({@code true} to get a vertical snake segment, 
     * {@code false} to get a horizontal snake segment).
     * @param rect A rectangle to reuse for getting the snake segment, or null.
     * @param point1 A scratch Point2D object to reuse while calculating the 
     * snake segment, or null.
     * @param point2 A second scratch Point2D object to reuse while calculating 
     * the snake segment, or null.
     * @return The rectangle to use to render the snake segment that ends at the 
     * given tile, or null.
     * @see #getSnakeSegmentRectangle
     * @see #paintTile
     * @see #getTileContentsOffset
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see Tile#isSnake 
     * @see Tile#isFacingUp 
     * @see Tile#isFacingDown 
     * @see Tile#isFacingLeft 
     * @see Tile#isFacingRight 
     * @see Tile#getType 
     * @see JPlayField#getAdjacentTile 
     * @see JPlayField#getRowCount 
     * @see JPlayField#getColumnCount 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     */
    protected Rectangle2D getSnakeSegment(JPlayField c, Tile tile, 
            Tile[] startTiles, Dimension2D tileS, Rectangle2D fieldR, 
            double xOff, double yOff, boolean vertical, Rectangle2D rect, 
            Point2D point1, Point2D point2){
            // This gets whether the tile is facing up (if this is getting a 
            // vertical snake segment) or to the left (if this is getting a 
            // horizontal snake segment)
        boolean upLeft = ((vertical)?tile.isFacingUp():tile.isFacingLeft());
            // If this is getting a vertical snake segment, return null if the 
            // tile is facing neither up or down. If this is getting a 
            // horizontal snake segment, then return null if this is facing 
            // neither left or right.
        if (!upLeft && !((vertical)?tile.isFacingDown():tile.isFacingRight()))
            return null;
            // This will get the tile at the start of the current snake segment 
            // (if the start tile array is null, then the given tile is treated 
        Tile tile0 = tile;      // as the start tile)
            // If the array of start tiles is not null
        if (startTiles != null){
                // Get the index of the starting tile in the current snake 
                // segment (this will be the given tile's column if the snake 
                // segment is vertical, and will be the last tile in the array 
                // if this is getting a horizontal snake segment).
            int startIndex = (vertical)?tile.getColumn():c.getColumnCount();
                // Get the tile that is below or to the right of (depending on 
                // whether this is getting a vertical or horizontal snake 
                // segment) the current tile
            Tile next = c.getAdjacentTile(tile, (vertical) ? DOWN_DIRECTION : 
                    RIGHT_DIRECTION, false);
                // Get the actual tile at the start of the snake segment
            tile0 = startTiles[startIndex];
                // If the current tile is facing up (vertical) or to the left 
                // (horizontal), the next tile is a non-null snake tile that is 
                // the same type of snake as the current tile, and the next tile 
                // is either facing down (vertical) or to the right (horizontal)
                // (If the next tile is a part of the 
            if (upLeft && next != null && next.isSnake() && 
                    tile.getType() == next.getType() && 
                    ((vertical)?next.isFacingDown() : next.isFacingRight())){
                    // If the start tile is null (this is the start of the snake 
                if (tile0 == null)  // segment)
                    startTiles[startIndex] = tile;
                return null;
            }
            else    // Go ahead and remove the starting tile from the array, 
                    // since we're about to render the snake segment that 
                startTiles[startIndex] = null;  // starts at this tile
        }
        return getSnakeSegmentRectangle(c,tile0,tile,tileS,fieldR,xOff,yOff,
                vertical,rect,point1,point2);
    }
    /**
     * This is used to render the border around the tiles. If the {@link 
     * #getTileBorderShouldBePainted tile border is to be painted} and the given 
     * path for the border ({@code borderP}) is not null, then this will render 
     * the given border path using the {@link JPlayField#getTileBorder tile 
     * border color}. The border is rendered with no antialiasing, regardless of 
     * whether the play field is rendered in {@link JPlayField#isHighQuality() 
     * high quality}, and uses the {@link #getBorderStroke border stroke} if not 
     * null. This renders to a copy of the given graphics context, so as to 
     * protect the rest of the paint code from changes made to the graphics 
     * context while rendering the tile border. <p>
     * 
     * Note that the border path, if not null, should already outline the path 
     * for the border. This method does not calculate the path for the border, 
     * and merely renders any path it is given. The {@link #paintTiles 
     * paintTiles} method is responsible for calculating the path for the 
     * border.
     * 
     * @param g The graphics context to render to.
     * @param c The JPlayField being rendered.
     * @param tileS The size of the tiles.
     * @param fieldR The bounds of the play field.
     * @param borderP The path to use to render the border. If this is null, 
     * then the border is not rendered regardless of whether it should be.
     * @see #paint 
     * @see #paintTiles 
     * @see #getTileBorderShouldBePainted 
     * @see JPlayField#isTileBorderPainted 
     * @see JPlayField#getTileBorder 
     * @see TILE_BORDER_COLOR
     * @see #getBorderStroke 
     * @see #getTileSize(JPlayField) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     */
    protected void paintTileBorder(Graphics2D g,JPlayField c,Dimension2D tileS, 
            Rectangle2D fieldR, Path2D borderP){
            // If the tile border should be painted and the border path is not 
        if (getTileBorderShouldBePainted(c,tileS,fieldR)&&borderP!=null){// null
            g = (Graphics2D) g.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_OFF);
                // Use the play field's tile border color if its not null.
                // Otherwise, use the default tile border color
            g.setColor(Objects.requireNonNullElse(c.getTileBorder(),
                    TILE_BORDER_COLOR));
            g.clip(fieldR);
                // Get the stroke to use for the border
            Stroke stroke = getBorderStroke(c,tileS,fieldR);
            if (stroke != null) // If the border stroke is not null
                g.setStroke(stroke);
            g.draw(borderP);
            g.dispose();
        }
    }
}
