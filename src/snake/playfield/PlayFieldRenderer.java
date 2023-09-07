/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package snake.playfield;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import snake.*;
import snake.event.*;

/**
 * This is an interface used by JPlayField to delegate operations such as 
 * rendering and tile related calculations. This is responsible for rendering 
 * the contents of the play field via the {@link #paint paint} method. This 
 * is also responsible for providing some of the default values to use for 
 * rendering the play field and handling repainting the play field in response 
 * to events. <p>
 * 
 * PlayFieldRenderers implement the {@link PlayFieldListener} interface so as to 
 * be informed about changes to the tiles in the play field. However, they are 
 * not typically registered as a {@code PlayFieldListener} on JPlayFields, since 
 * JPlayFields will directly notify their set renderer of the changes. 
 * Registering a PlayFieldRenderer as a {@code PlayFieldListener} on the 
 * JPlayField they render for may result in the PlayFieldRenderer being notified 
 * of changes to the tiles twice, once while notifying the registered {@code 
 * PlayFieldListener}s, and again when the JPlayField goes to notify its 
 * renderer specifically.
 * 
 * @author Milo Steier
 * @see JPlayField
 * @see DefaultPlayFieldRenderer
 * @see PlayFieldListener
 */
public interface PlayFieldRenderer extends Painter<JPlayField>, SnakeConstants, 
        PlayFieldListener {
    /**
     * This returns the default minimum size for the given JPlayField. If null 
     * is returned, then the minimum size will fall back to the component's 
     * default implementation of querying the UI delegate's {@code 
     * getMinimumSize} method and falling back on the component's layout manager 
     * if that returns null.
     * 
     * @implSpec The default implementation returns null.
     * 
     * @param c The JPlayField whose minimum size is being queried.
     * @return A Dimension object with the minimum size for the JPlayField, or 
     * null.
     * @see JPlayField#getMinimumSize 
     * @see JComponent#getMinimumSize 
     */
    public default Dimension getMinimumSize(JPlayField c){
        return null;
    }
    /**
     * This returns the default maximum size for the given JPlayField. If null 
     * is returned, then the maximum size will fall back to the component's 
     * default implementation of querying the UI delegate's {@code 
     * getMaximumSize} method and falling back on the component's layout manager 
     * if that returns null.
     * 
     * @implSpec The default implementation returns null.
     * 
     * @param c The JPlayField whose maximum size is being queried.
     * @return A Dimension object with the maximum size for the JPlayField, or 
     * null.
     * @see JPlayField#getMaximumSize 
     * @see JComponent#getMaximumSize 
     */
    public default Dimension getMaximumSize(JPlayField c){
        return null;
    }
    /**
     * This returns the default preferred size for the given JPlayField. If null 
     * is returned, then the preferred size will fall back to the component's 
     * default implementation of querying the UI delegate's {@code 
     * getPreferredSize} method and falling back on the component's layout 
     * manager if that returns null.
     * 
     * @implSpec The default implementation returns null.
     * 
     * @param c The JPlayField whose preferred size is being queried.
     * @return A Dimension object with the preferred size for the JPlayField, or 
     * null.
     * @see JPlayField#getPreferredSize 
     * @see JComponent#getPreferredSize 
     */
    public default Dimension getPreferredSize(JPlayField c){
        return null;
    }
    /**
     * This returns the default color to use for drawing apples for the given 
     * JPlayField. That is to say, this is returns the color to use to draw 
     * {@link Tile#isApple() apple tiles}. If null is returned, then the color 
     * will default to {@link APPLE_COLOR}.
     * 
     * @implSpec The default implementation returns null.
     * 
     * @param c The JPlayField whose apple color is being queried.
     * @return The color for apples, or null.
     * @see JPlayField#getAppleColor 
     * @see JPlayField#setAppleColor 
     * @see JPlayField#isAppleColorSet 
     * @see APPLE_COLOR
     * @see Tile#isApple 
     */
    public default Color getAppleColor(JPlayField c){
        return null;
    }
    /**
     * This returns the default primary color to use for drawing snakes for the 
     * given JPlayField. That is to say, this is returns the color to use to 
     * draw {@link Tile#isSnake() snake tiles} that do not have their {@link 
     * Tile#getType() type flag} set. If null is returned, then the color will 
     * default to {@link #PRIMARY_SNAKE_COLOR}.
     * 
     * @implSpec The default implementation returns null.
     * 
     * @param c The JPlayField whose primary snake color is being queried.
     * @return The primary color for snakes, or null.
     * @see JPlayField#getPrimarySnakeColor 
     * @see JPlayField#setPrimarySnakeColor 
     * @see JPlayField#isPrimarySnakeColorSet 
     * @see PRIMARY_SNAKE_COLOR
     * @see Tile#isSnake 
     * @see Tile#getType 
     */
    public default Color getPrimarySnakeColor(JPlayField c){
        return null;
    }
    /**
     * This returns the default secondary color to use for drawing snakes for 
     * the given JPlayField. That is to say, this is returns the color to use to 
     * draw {@link Tile#isSnake() snake tiles} that have their {@link 
     * Tile#getType() type flag} set. If null is returned, then the color will 
     * default to {@link #SECONDARY_SNAKE_COLOR}.
     * 
     * @implSpec The default implementation returns null.
     * 
     * @param c The JPlayField whose secondary snake color is being queried.
     * @return The secondary color for snakes, or null.
     * @see JPlayField#getSecondarySnakeColor 
     * @see JPlayField#setSecondarySnakeColor 
     * @see JPlayField#isSecondarySnakeColorSet 
     * @see SECONDARY_SNAKE_COLOR
     * @see Tile#isSnake 
     * @see Tile#getType 
     */
    public default Color getSecondarySnakeColor(JPlayField c){
        return null;
    }
    /**
     * This returns the default color to use for the background of the tiles for 
     * the given JPlayField. If null is returned, then the color will default to 
     * {@link #TILE_BACKGROUND_COLOR}.
     * 
     * @implSpec The default implementation returns null.
     * 
     * @param c The JPlayField whose tile background color is being queried.
     * @return The color for the tile background, or null.
     * @see JPlayField#getTileBackground 
     * @see JPlayField#setTileBackground 
     * @see JPlayField#isTileBackgroundSet 
     * @see TILE_BACKGROUND_COLOR
     * @see #isTileBackgroundPainted 
     * @see JPlayField#isTileBackgroundPainted 
     * @see JPlayField#setTileBackgroundPainted 
     */
    public default Color getTileBackground(JPlayField c){
        return null;
    }
    /**
     * This returns the default color to use for the border around the tiles for 
     * the given JPlayField. If null is returned, then the color will default to 
     * {@link #TILE_BORDER_COLOR}.
     * 
     * @implSpec The default implementation returns null.
     * 
     * @param c The JPlayField whose tile border color is being queried.
     * @return The color for the tile border, or null.
     * @see JPlayField#getTileBorder 
     * @see JPlayField#setTileBorder 
     * @see JPlayField#isTileBorderSet 
     * @see TILE_BORDER_COLOR
     * @see #isTileBorderPainted 
     * @see JPlayField#isTileBorderPainted 
     * @see JPlayField#setTileBorderPainted 
     */
    public default Color getTileBorder(JPlayField c){
        return null;
    }
    /**
     * This returns whether the tile background should be painted by default for 
     * the given JPlayField. If null is returned, then this property will 
     * default to {@code true}.
     * 
     * @implSpec The default implementation returns null.
     * 
     * @param c The JPlayField which is being queried as to whether to paint the 
     * tile background.
     * @return Whether the tile background should be painted, or null.
     * @see JPlayField#isTileBackgroundPainted 
     * @see JPlayField#setTileBackgroundPainted 
     * @see #getTileBackground 
     * @see JPlayField#getTileBackground 
     * @see JPlayField#setTileBackground 
     */
    public default Boolean isTileBackgroundPainted(JPlayField c){
        return null;
    }
    /**
     * This returns whether the tile border should be painted by default for the 
     * given JPlayField. If null is returned, then this property will default to 
     * {@code true}.
     * 
     * @implSpec The default implementation returns null.
     * 
     * @param c The JPlayField which is being queried as to whether to paint the 
     * tile border.
     * @return Whether the tile border should be painted, or null.
     * @see JPlayField#isTileBorderPainted 
     * @see JPlayField#setTileBorderPainted 
     * @see #getTileBorder 
     * @see JPlayField#getTileBorder 
     * @see JPlayField#setTileBorder 
     */
    public default Boolean isTileBorderPainted(JPlayField c){
        return null;
    }
    /**
     * This returns whether the play field should be rendered in high quality by 
     * default for the given JPlayField. If null is returned, then this property 
     * will default to {@code false}.
     * 
     * @implSpec The default implementation returns null.
     * 
     * @param c The JPlayField which is being queried as to whether to render 
     * the play field in high quality.
     * @return Whether the play field should be rendered in high quality, or 
     * null.
     * @see JPlayField#isHighQuality 
     * @see JPlayField#setHighQuality 
     */
    public default Boolean isHighQuality(JPlayField c){
        return null;
    }
    /**
     * This installs this renderer onto the given JPlayField. This can be used 
     * to register listeners on the JPlayField or to configure the JPlayField 
     * appropriately for this renderer. Note that PlayFieldRenderers do not need 
     * to be registered as a {@code PlayFieldListener} on the JPlayField they 
     * render for, as JPlayFields will directly notify their renderer. In fact, 
     * it is recommended not to register a PlayFieldRenderer as a {@code 
     * PlayFieldListener}, as it may result in the PlayFieldRenderer being 
     * notified twice for the same event (once while notifying the registered 
     * {@code PlayFieldListener}s, and a second time when notifying the renderer 
     * specifically).
     * 
     * @implSpec The default implementation does nothing.
     * 
     * @param c The JPlayField where this is being installed on.
     * @see #uninstallRenderer 
     * @see JPlayField#setRenderer 
     * @see JPlayField#getRenderer 
     */
    public default void installRenderer(JPlayField c){ }
    /**
     * This uninstalls this renderer from the given JPlayField. In other words, 
     * this reverses what was done by {@link #installRenderer installRenderer}. 
     * This may involve removing listeners or reversing any changes made to the 
     * JPlayField. Note that it is unnecessary to remove this from the 
     * JPlayField as if this were a {@code PlayFieldListener} unless it was 
     * installed as one by {@code installRenderer}.
     * 
     * @implSpec The default implementation does nothing.
     * 
     * @param c The JPlayField where this is being removed from.
     * 
     * @see #installRenderer 
     * @see JPlayField#setRenderer 
     * @see JPlayField#getRenderer 
     */
    public default void uninstallRenderer(JPlayField c){ }
    /**
     * {@inheritDoc }
     * @param evt {@inheritDoc }
     */
    @Override
    public void tilesAdded(PlayFieldEvent evt);
    /**
     * {@inheritDoc }
     * @param evt {@inheritDoc }
     */
    @Override
    public void tilesRemoved(PlayFieldEvent evt);
    /**
     * {@inheritDoc }
     * @param evt {@inheritDoc }
     */
    @Override
    public void tilesChanged(PlayFieldEvent evt);
    /**
     * This calculates the size of the tiles in the given JPlayField and stores 
     * the results in {@code dim} before returning {@code dim}. If {@code dim} 
     * is null, then a new Dimension2D object is created and returned. If not 
     * all of the tiles are the same size, then this will calculate and return 
     * the average size of the tiles. The {@link #getTileBounds getTileBounds} 
     * method can be used to get a more exact size for any particular tile.
     * @param c The JPlayField which is being queried. This cannot be null.
     * @param dim The Dimension2D object to store the results in, may be null.
     * @return A Dimension2D object with the size of the tiles.
     * @throws NullPointerException If {@code c} is null.
     * @see #getTileSize(JPlayField) 
     * @see #getTileBounds 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see JPlayField#getTileSize(Dimension2D) 
     * @see JPlayField#getTileSize() 
     */
    public Dimension2D getTileSize(JPlayField c, Dimension2D dim);
    /**
     * This returns the size of the tiles in the given JPlayField. If not 
     * all of the tiles are the same size, then this will return the average 
     * size of the tiles. The {@link #getTileBounds getTileBounds} method can be 
     * used to get a more exact size for any particular tile. This is equivalent 
     * to calling {@link #getTileSize(JPlayField, Dimension2D) 
     * getTileSize}{@code (c, null)}.
     * @param c The JPlayField which is being queried. This cannot be null.
     * @return A Dimension2D object with the size of the tiles.
     * @throws NullPointerException If {@code c} is null.
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getTileBounds 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see JPlayField#getTileSize(Dimension2D) 
     * @see JPlayField#getTileSize() 
     */
    public default Dimension2D getTileSize(JPlayField c){
        return getTileSize(c,null);
    }
    /**
     * This calculates the position and size, in the given JPlayField's 
     * coordinate system, of the painted play field area and stores the results 
     * in {@code rect} before returning {@code rect}. If {@code rect} is null, 
     * then a new Rectangle2D object will be created and returned. If the 
     * painted play field takes up the entire painting area of a JPlayField, 
     * then the bounds of the inner area of the JPlayField will be returned. 
     * Otherwise, this will be the bounds of the region within the JPlayField in 
     * which the play field will be painted into.
     * @param c The JPlayField which is being queried. This cannot be null.
     * @param rect The Rectangle2D object to store the results in, may be null.
     * @return A Rectangle2D object with the bounds for the painted paint field.
     * @throws NullPointerException If {@code c} is null.
     * @see #getPlayFieldBounds(JPlayField) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getTileSize(JPlayField) 
     * @see #getTileBounds 
     * @see JPlayField#getPlayFieldBounds(Rectangle2D) 
     * @see JPlayField#getPlayFieldBounds() 
     */
    public Rectangle2D getPlayFieldBounds(JPlayField c, Rectangle2D rect);
    /**
     * This returns the position and size, in the given JPlayField's coordinate 
     * system, of the painted play field area. If the painted play field takes 
     * up the entire painting area of a JPlayField, then the bounds of the inner 
     * area of the JPlayField will be returned. Otherwise, this will be the 
     * bounds of the region within the JPlayField in which the play field will 
     * be painted into. This is equivalent to calling {@link 
     * #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * getPlayFieldBounds}{@code (c, null)}.
     * @param c The JPlayField which is being queried. This cannot be null.
     * @return A Rectangle2D object with the bounds for the painted paint field.
     * @throws NullPointerException If {@code c} is null.
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getTileSize(JPlayField) 
     * @see #getTileBounds 
     * @see JPlayField#getPlayFieldBounds(Rectangle2D) 
     * @see JPlayField#getPlayFieldBounds() 
     */
    public default Rectangle2D getPlayFieldBounds(JPlayField c){
        return getPlayFieldBounds(c,null);
    }
    /**
     * This returns the position and size, in the given JPlayField's coordinate 
     * system, for the region of tiles specified by the rows {@code row0} and 
     * {@code row1} and the columns {@code column0} and {@code column1}. Note 
     * that {@code row0} does not necessarily need to be less than or equal to 
     * {@code row1}, and that {@code column0} does not necessarily need to be 
     * less than or equal to {@code column1}. <p>
     * 
     * If either of the smaller row or column are outside the play field's range 
     * of tiles, then this method will return null. If the smaller row and 
     * column are both valid, but either of the larger row or column are outside 
     * the play field's range, then the returned bounds will be for the tile at 
     * the smaller row and column only. Otherwise, the returned bounds will 
     * encompass the valid range of tiles.
     * 
     * @param c The JPlayField which is being queried. This cannot be null.
     * @param row0 One end of the range of rows to get the bounds of.
     * @param row1 The other end of the range of rows to get the bounds of.
     * @param column0 One end of the range of columns to get the bounds of.
     * @param column1 The other end of the range of columns to get the bounds 
     * of.
     * @return A Rectangle2D object with the bounds for the range of tiles, or 
     * null.
     * @throws NullPointerException If {@code c} is null.
     * @see #tileToLocation 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getTileSize(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see JPlayField#containsTile(int, int) 
     * @see JPlayField#getTileBounds(int, int, int, int) 
     * @see JPlayField#getTileBounds(int, int) 
     * @see JPlayField#getTileBounds(Tile) 
     */
    public Rectangle2D getTileBounds(JPlayField c,int row0,int row1,int column0,
            int column1);
    /**
     * This returns the origin of the tile at the given row and column in the 
     * given JPlayField, in the given JPlayField's coordinate system, or null if 
     * the given row and/or column are invalid.
     * @param c The JPlayField which is being queried. This cannot be null.
     * @param row The row of the tile to get the location of.
     * @param column The column of the tile to get the location of.
     * @return The origin of the tile, or null.
     * @throws NullPointerException If {@code c} is null.
     * @see #getTileBounds 
     * @see #locationToTile 
     * @see #locationToTile2D 
     * @see #getTileSize(JPlayField, Dimension2D) 
     * @see #getTileSize(JPlayField) 
     * @see #getPlayFieldBounds(JPlayField, Rectangle2D) 
     * @see #getPlayFieldBounds(JPlayField) 
     * @see JPlayField#containsTile(int, int) 
     * @see JPlayField#tileToLocation(int, int) 
     * @see JPlayField#tileToLocation(Tile) 
     */
    public Point2D tileToLocation(JPlayField c, int row, int column);
    /**
     * This returns the tile in the given JPlayField that is closest to the 
     * given location in the JPlayField's coordinate system. To determine if the 
     * tile actually contains the specified location, compare the point against 
     * the {@link #getTileBounds tile's bounds}. This returns null if there is 
     * no tile at the given location. <p>
     * 
     * This method uses floating-point precision, so as to be more precise. To 
     * get a more rough estimate of the closest tile, in integer precision, use 
     * the {@link #locationToTile locationToTile} method.
     * 
     * @param c The JPlayField which is being queried. This cannot be null.
     * @param x The x-coordinate of the point to get the tile at.
     * @param y The y-coordinate of the point to get the tile at.
     * @return The tile closest to the given location, or null.
     * @throws NullPointerException If {@code c} is null.
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
     */
    public Tile locationToTile2D(JPlayField c, double x, double y);
    /**
     * This returns the tile in the given JPlayField that is closest to the 
     * given location in the JPlayField's coordinate system. To determine if the 
     * tile actually contains the specified location, compare the point against 
     * the {@link #getTileBounds tile's bounds}. This returns null if there is 
     * no tile at the given location. <p>
     * 
     * This method uses integer precision, where as tiles may be rendered using 
     * floating-point precision. If more precision is needed, use the {@link 
     * #locationToTile2D locationToTile2D} method instead.
     * 
     * @implSpec The default implementation calls {@link #locationToTile2D 
     * locationToTile2D}.
     * 
     * @param c The JPlayField which is being queried. This cannot be null.
     * @param x The x-coordinate of the point to get the tile at.
     * @param y The y-coordinate of the point to get the tile at.
     * @return The tile closest to the given location, or null.
     * @throws NullPointerException If {@code c} is null.
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
     * @see JPlayField#locationToTile2D(geom.Point2D) 
     */
    public default Tile locationToTile(JPlayField c, int x, int y){
        return locationToTile2D(c,x,y);
    }
    /**
     * This renders the given play field to the given graphics context. 
     * Implementations of this method may modify the state of the graphics 
     * object and are not required to restore that state once it's finished. 
     * It is recommended that the caller should pass in a scratch graphics 
     * object. The graphics object must not be null. <p>
     * 
     * The given JPlayField is used to get the play field to render. 
     * PlayFieldRenderer implementations may also use the given JPlayField to 
     * get properties useful for painting the play field, such as the colors for 
     * the tiles and whether to paint certain elements. The given JPlayField may 
     * be null, however this may result in undefined and unpredictable behavior. 
     * Most often this will just result in nothing being rendered. In accordance 
     * to the Painter interface, implementations must not throw a 
     * NullPointerException if the given JPlayField is null. <p>
     * 
     * The width and height parameters specify the width and height that the 
     * renderer should paint into. More specifically, the given width and height 
     * instruct the renderer that it should render the play field fully within 
     * this width and height. The width and height will typically be the width 
     * and height of the JPlayField, however that may not always be the case. 
     * Any specified clip on the graphics context will further constrain the 
     * region. It is assumed that the play field should be rendered at the 
     * origin.
     * 
     * @param g The {@code Graphics2D} object to render to (cannot be null).
     * @param c The JPlayField being rendered. While this may be null, it may 
     * result in undefined and unpredictable behavior.
     * @param width The width of the area to paint the play field in.
     * @param height The height of the area to paint the play field in.
     * @throws NullPointerException If the {@code Graphics2D} object is null.
     * @see JPlayField#paintComponent 
     * @see JPlayField#getWidth 
     * @see JPlayField#getHeight 
     * @see JPlayField#getModel 
     */
    @Override
    public void paint(Graphics2D g, JPlayField c, int width, int height);
}
