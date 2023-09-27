/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.function.Predicate;
import javax.swing.*;
import javax.swing.plaf.*;
import snake.event.*;
import snake.playfield.*;

/**
 * This is a component that displays the play field for the game Snake. The play 
 * field is made up of a grid of {@link Tile tiles} which is maintained by the 
 * model for {@code JPlayField}, the {@link PlayFieldModel PlayFieldModel}. <p>
 * 
 * The contents of the {@code PlayFieldModel} do not need to be static, as the 
 * number of rows and columns and the states of the tiles can change over time. 
 * When correctly implemented, a {@code PlayFieldModel} will notify the {@code 
 * PlayFieldListeners} that have been added to it whenever a change occurs. 
 * These changes are characterized by a {@code PlayFieldEvent}, which identifies 
 * the range of tiles that were changed, added, or removed. The preferred way to 
 * listen to changes made to the play field is to add {@code PlayFieldListener}s 
 * directly to the {@code JPlayField}, so that {@code JPlayField} can then 
 * listen to changes made to the play field model and notify the listeners of 
 * the change. <p>
 * 
 * Simple, dynamic {@code JPlayField} applications can use the {@link 
 * DefaultPlayFieldModel DefaultPlayFieldModel} class to maintain the grid of 
 * tiles. The {@code DefaultPlayFieldModel} class is an implementation of {@code 
 * PlayFieldModel} that provides support for a dynamic grid of tiles, and can 
 * handle any grid size ranging from {@value 
 * DefaultPlayFieldModel#MINIMUM_ROW_COUNT} x {@value 
 * DefaultPlayFieldModel#MINIMUM_COLUMN_COUNT} up to {@value 
 * DefaultPlayFieldModel#MAXIMUM_ROW_COUNT} x {@value 
 * DefaultPlayFieldModel#MAXIMUM_COLUMN_COUNT}. Applications that need a 
 * more custom implementation of {@code PlayFieldModel} may wish to subclass 
 * {@link AbstractPlayFieldModel AbstractPlayFieldModel}, which 
 * provides default implementations for most of the methods in the {@code 
 * PlayFieldModel} interface. <p>
 * 
 * The painting of the play field is handled by a delegate called a play field 
 * renderer which implements the {@link PlayFieldRenderer PlayFieldRenderer} 
 * interface. When it comes time to paint this component, the play field 
 * renderer will be asked to paint the play field via its {@link 
 * PlayFieldRenderer#paint paint}. The play field render is also used for 
 * calculating tile sizing and positioning, along with providing default values 
 * for multiple of the properties related to rendering the play field. A default 
 * play field renderer is provided by the protected {@code getDefaultRenderer} 
 * method. This will typically be a shared instance of a {@link 
 * DefaultPlayFieldRenderer DefaultPlayFieldRenderer}, which is an 
 * implementation of {@code PlayFieldRenderer} that uses simple geometry to 
 * render the play field. <p>
 * 
 * While {@code JPlayField} does not react to input events, it's easy to add a 
 * {@code MouseListener} if you wish to take action on mouse events. The {@code 
 * locationToTile} and {@code locationToTile2D} methods can be used to determine 
 * what tile was clicked. The {@code locationToTile} methods use integer 
 * precision whereas the {@code locationToTile2D} methods use floating point 
 * precision. As such, the {@code locationToTile2D} methods are generally 
 * preferable over the {@code locationToTile} methods when getting the tile at 
 * an exact location. 
 * 
 * @author Milo Steier
 * @see Tile
 * @see PlayFieldModel
 * @see AbstractPlayFieldModel
 * @see DefaultPlayFieldModel
 * @see PlayFieldRenderer
 * @see DefaultPlayFieldRenderer
 */
public class JPlayField extends JPanel implements SnakeConstants{
    /**
     * This identifies that the play field renderer used to delegate the task of 
     * rendering the play field and handling tile calculations has changed.
     */
    public static final String RENDERER_PROPERTY_CHANGED = 
            "RendererPropertyChanged";
    /**
     * This identifies that the play field model has changed.
     */
    public static final String MODEL_PROPERTY_CHANGED = "ModelPropertyChanged";
    /**
     * This identifies that the rendering quality setting has changed.
     */
    public static final String HIGH_QUALITY_PROPERTY_CHANGED = 
            "HighQualityPropertyChanged";
    /**
     * This identifies that the color for the tile background has changed.
     */
    public static final String TILE_BACKGROUND_PROPERTY_CHANGED = 
            "TileBackgroundPropertyChanged";
    /**
     * This identifies that a change has been made to whether the tile 
     * background should be painted.
     */
    public static final String TILE_BACKGROUND_PAINTED_PROPERTY_CHANGED = 
            "TileBackgroundPaintedPropertyChanged";
    /**
     * This identifies that the color for the tile border has changed.
     */
    public static final String TILE_BORDER_PROPERTY_CHANGED = 
            "TileBorderPropertyChanged";
    /**
     * This identifies that a change has been made to whether the tile border 
     * should be painted.
     */
    public static final String TILE_BORDER_PAINTED_PROPERTY_CHANGED = 
            "TileBorderPaintedPropertyChanged";
    /**
     * This identifies that the color for apples has changed.
     */
    public static final String APPLE_COLOR_PROPERTY_CHANGED = 
            "AppleColorPropertyChanged";
    /**
     * This identifies that the primary color for snakes has changed.
     */
    public static final String PRIMARY_SNAKE_COLOR_PROPERTY_CHANGED = 
            "PrimarySnakeColorPropertyChanged";
    /**
     * This identifies that the secondary color for snakes has changed.
     */
    public static final String SECONDARY_SNAKE_COLOR_PROPERTY_CHANGED = 
            "SecondarySnakeColorPropertyChanged";
    /**
     * This is the default renderer to use when no renderer has been set. This 
     * is initialized the first time it is requested.
     */
    private static PlayFieldRenderer defaultRenderer = null;
    /**
     * This is the handler to use to handle play field events.
     */
    private Handler handler;
    /**
     * This is the play field model used to store and manage the tiles.
     */
    private PlayFieldModel model = null;
    /**
     * This stores whether the tile background should be painted. The default is 
     * null, which indicates that the renderer should be queried.
     */
    private Boolean paintTileBG = null;
    /**
     * This stores whether the tile border should be painted. The default is 
     * null, which indicates that the renderer should be queried.
     */
    private Boolean paintTileBorder = null;
    /**
     * This stores whether the play field should render in high quality. The 
     * default is null, which indicates that the renderer should be queried.
     */
    private Boolean hqEnabled = null;
    /**
     * This is the color to use for the tile background. The default is null, 
     * which indicates that the renderer should be queried.
     */
    private Color tileBG = null;
    /**
     * This is the color to use for the tile border. The default is null, which 
     * indicates that the renderer should be queried.
     */
    private Color tileBorder = null;
    /**
     * This is the primary color to use for snakes. The default is null, which 
     * indicates that the renderer should be queried.
     */
    private Color primaryColor = null;
    /**
     * This is the secondary color to use for snakes. The default is null, which 
     * indicates that the renderer should be queried.
     */
    private Color secondaryColor = null;
    /**
     * This is the color to use for apples. The default is null, which indicates 
     * that the renderer should be queried.
     */
    private Color appleColor = null;
    /**
     * This is the renderer to use to render the play field. Null indicates that 
     * the default renderer should be used.
     */
    private PlayFieldRenderer renderer = null;
    /**
     * This is used to initialize this JPlayField and set some of the properties 
     * of this JPlayField to their default values.
     * @param model The model for the JPlayField.
     */
    private void initialize(PlayFieldModel model){
        setOpaque(false);
        handler = new Handler();
        setModel(model);
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
            // Get the renderer to install it on this play field
        PlayFieldRenderer r = getRenderer();
        if (r != null)  // If the renderer is not null
            r.installRenderer(this);
    }
    /**
     * This constructs a JPlayField with the given, non-null model. All the 
     * JPlayField constructors delegate to this one.
     * @param model The model for this JPlayField (cannot be null).
     * @throws NullPointerException If the model is null.
     */
    public JPlayField(PlayFieldModel model){
        super();
        initialize(model);
    }
    /**
     * This constructs a JPlayField with the given number of rows and columns. 
     * This constructor creates a {@link DefaultPlayFieldModel 
     * DefaultPlayFieldModel} from the given number of rows and columns. 
     * @param rows The number of rows for the model (must be between {@value 
     * DefaultPlayFieldModel#MINIMUM_ROW_COUNT} and {@value 
     * DefaultPlayFieldModel#MAXIMUM_ROW_COUNT}, inclusive).
     * @param columns The number of columns for the model (must be between 
     * {@value DefaultPlayFieldModel#MINIMUM_COLUMN_COUNT} and {@value 
     * DefaultPlayFieldModel#MAXIMUM_COLUMN_COUNT}, inclusive).
     * @throws IllegalArgumentException If either the number of rows or columns 
     * are out of bounds.
     * @see DefaultPlayFieldModel
     * @see DefaultPlayFieldModel#DefaultPlayFieldModel(int, int) 
     * @see DefaultPlayFieldModel#MINIMUM_ROW_COUNT
     * @see DefaultPlayFieldModel#MAXIMUM_ROW_COUNT
     * @see DefaultPlayFieldModel#MINIMUM_COLUMN_COUNT
     * @see DefaultPlayFieldModel#MAXIMUM_COLUMN_COUNT
     */
    public JPlayField(int rows, int columns){
        this(new DefaultPlayFieldModel(rows,columns));
    }
    /**
     * This constructs a JPlayField with {@value 
     * DefaultPlayFieldModel#MINIMUM_ROW_COUNT} rows and {@value 
     * DefaultPlayFieldModel#MINIMUM_COLUMN_COUNT} columns. This constructor 
     * creates a {@link DefaultPlayFieldModel DefaultPlayFieldModel} with no 
     * specified number of rows or columns.
     * @see DefaultPlayFieldModel
     * @see DefaultPlayFieldModel#DefaultPlayFieldModel() 
     * @see DefaultPlayFieldModel#MINIMUM_ROW_COUNT
     * @see DefaultPlayFieldModel#MAXIMUM_ROW_COUNT
     * @see DefaultPlayFieldModel#MINIMUM_COLUMN_COUNT
     * @see DefaultPlayFieldModel#MAXIMUM_COLUMN_COUNT
     */
    public JPlayField(){
        this(new DefaultPlayFieldModel());
    }
    /**
     * This returns the minimum size for this component. If the minimum size has 
     * been set to a non-null value, then that is what is returned. Otherwise, 
     * this will query the {@link #getRenderer() play field renderer}'s {@link 
     * PlayFieldRenderer#getMinimumSize getMinimumSize} method and returns that 
     * if it's not null. Otherwise, this will fall back on querying the UI 
     * delegate's {@code getMinimumSize} method and returning that if it returns 
     * a non-null value, and deferring to the component's layout manager if it 
     * does returns null.
     * @return The minimum size for this component.
     * @see #setMinimumSize 
     * @see #getRenderer 
     * @see PlayFieldRenderer#getMinimumSize 
     * @see PlayFieldRenderer
     * @see ComponentUI
     * @see JComponent#getMinimumSize 
     */
    @Override
    public Dimension getMinimumSize(){
        Dimension dim = null;       // This will get the minimum size
            // If the minimum size is not set and the renderer is not null
        if (!isMinimumSizeSet() && getRenderer() != null)
                // Get the minimum size from the renderer
            dim = getRenderer().getMinimumSize(this);
            // If the retrieved minimum size is not null, return it. Otherwise, 
            // return the component minimum size 
        return (dim != null) ? dim : super.getMinimumSize();
    }
    /**
     * This sets the minimum size for this component. Any subsequent call to 
     * {@code getMinimumSize} will return this value, and neither the {@link 
     * #getRenderer() play field renderer} nor the component's UI will be asked 
     * to compute it. Setting the minimum size to null will reset it back to the 
     * default behavior.
     * @param minimumSize The new minimum size for this component, or null.
     * @see #getMinimumSize 
     */
    @Override
    public void setMinimumSize(Dimension minimumSize){
        super.setMinimumSize(minimumSize);
    }
    /**
     * This returns the maximum size for this component. If the maximum size has 
     * been set to a non-null value, then that is what is returned. Otherwise, 
     * this will query the {@link #getRenderer() play field renderer}'s {@link 
     * PlayFieldRenderer#getMaximumSize getMaximumSize} method and returns that 
     * if it's not null. Otherwise, this will fall back on querying the UI 
     * delegate's {@code getMaximumSize} method and returning that if it returns 
     * a non-null value, and deferring to the component's layout manager if it 
     * does returns null.
     * @return The maximum size for this component.
     * @see #setMaximumSize 
     * @see #getRenderer 
     * @see PlayFieldRenderer#getMaximumSize 
     * @see PlayFieldRenderer
     * @see ComponentUI
     * @see JComponent#getMaximumSize 
     */
    @Override
    public Dimension getMaximumSize(){
        Dimension dim = null;       // This will get the maximum size
            // If the maximum size is not set and the renderer is not null
        if (!isMaximumSizeSet() && getRenderer() != null)
                // Get the maximum size from the renderer
            dim = getRenderer().getMaximumSize(this);
            // If the retrieved maximum size is not null, return it. Otherwise, 
            // return the component maximum size 
        return (dim != null) ? dim : super.getMaximumSize();
    }
    /**
     * This sets the maximum size for this component. Any subsequent call to 
     * {@code getMaximumSize} will return this value, and neither the {@link 
     * #getRenderer() play field renderer} nor the component's UI will be asked 
     * to compute it. Setting the maximum size to null will reset it back to the 
     * default behavior.
     * @param maximumSize The new maximum size for this component, or null.
     * @see #getMaximumSize 
     */
    @Override
    public void setMaximumSize(Dimension maximumSize){
        super.setMaximumSize(maximumSize);
    }
    /**
     * This returns the preferred size for this component. If the preferred size 
     * has been set to a non-null value, then that is what is returned. 
     * Otherwise, this will query the {@link #getRenderer() play field 
     * renderer}'s {@link PlayFieldRenderer#getPreferredSize getPreferredSize} 
     * method and returns that if it's not null. Otherwise, this will fall back 
     * on querying the UI delegate's {@code getPreferredSize} method and 
     * returning that if it returns a non-null value, and deferring to the 
     * component's layout manager if it does returns null.
     * @return The preferred size for this component.
     * @see #setPreferredSize
     * @see #getRenderer 
     * @see PlayFieldRenderer#getPreferredSize 
     * @see PlayFieldRenderer
     * @see ComponentUI
     * @see JComponent#getPreferredSize 
     */
    @Override
    public Dimension getPreferredSize(){
        Dimension dim = null;       // This will get the preferred size
            // If the preferred size is not set and the renderer is not null
        if (!isPreferredSizeSet() && getRenderer() != null)
                // Get the preferred size from the renderer
            dim = getRenderer().getPreferredSize(this);
            // If the retrieved preferred size is not null, return it. 
            // Otherwise, return the component preferred size 
        return (dim != null) ? dim : super.getPreferredSize();
    }
    /**
     * This sets the preferred size for this component. Any subsequent call to 
     * {@code getPreferredSize} will return this value, and neither the {@link 
     * #getRenderer() play field renderer} nor the component's UI will be asked 
     * for it. Setting the preferred size to null will reset it back to the 
     * default behavior.
     * @param preferredSize The new preferred size for this component, or null.
     * @see #getPreferredSize 
     */
    @Override
    public void setPreferredSize(Dimension preferredSize){
        super.setPreferredSize(preferredSize);
    }
    /**
     * This sets whether this component will paint every pixel within its 
     * bounds. If this is {@code false}, then the component may not paint some 
     * or all of its pixels, allowing the underlying pixels to show through. The 
     * default value for this is {@code false}.
     * @param isOpaque {@code true} if this component should be opaque, {@code 
     * false} otherwise.
     * @see #isOpaque 
     */
    @Override
    public void setOpaque(boolean isOpaque){
        boolean old = isOpaque();   // Get if this was opaque before
        super.setOpaque(isOpaque);
        if (old != isOpaque)        // If the opacity property changed
            repaint();
    }
    /**
     * This returns the play field renderer to fall back on if there is no play 
     * field renderer set for this JPlayField. 
     * @return The renderer to use for the default renderer.
     * @see #isRendererSet 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see DefaultPlayFieldRenderer
     */
    protected PlayFieldRenderer getDefaultRenderer(){
            // If the default renderer has not been initialized
        if (defaultRenderer == null)    
            defaultRenderer = new DefaultPlayFieldRenderer();
        return defaultRenderer;
    }
    /**
     * This returns whether the play field renderer has been set to a non-null 
     * value.
     * @return Whether {@code setRenderer} has been invoked with a non-null 
     * value.
     * @see #getRenderer 
     * @see #setRenderer 
     */
    public boolean isRendererSet(){
        return renderer != null;
    }
    /**
     * This returns the play field renderer to use to render the play field. If 
     * {@code setRenderer} has been invoked with a non-null value, then that is 
     * what is returned. Otherwise, this will return a default renderer provided 
     * by the protected {@code getDefaultRenderer} method.
     * @return The renderer used to render the play field.
     * @see #setRenderer 
     * @see #isRendererSet 
     * @see #getDefaultRenderer 
     */
    public PlayFieldRenderer getRenderer(){
            // If the renderer is set, return it. Otherwise, return the default 
        return (isRendererSet())?renderer:getDefaultRenderer(); // renderer
    }
    /**
     * This sets the play field renderer to use to render the play field. Any 
     * subsequent call to {@code getRenderer} will return this value, and the 
     * protected {@code getDefaultRenderer} method will not be asked to provide 
     * a default renderer to use. Setting the renderer to null will reset it 
     * back to the default behavior.
     * @param renderer The renderer to use to render the play field, or null.
     * @see #getRenderer 
     * @see #isRendererSet 
     * @see #getDefaultRenderer 
     */
    public void setRenderer(PlayFieldRenderer renderer){
            // If the new renderer is the same as the old one
        if (Objects.equals(renderer, this.renderer))
            return;
        PlayFieldRenderer old = this.renderer;      // Get the old renderer
        this.renderer = renderer;
        firePropertyChange(RENDERER_PROPERTY_CHANGED,old,renderer);
            // If the old renderer was null, then the default renderer was being 
            // used and now needs to be uninstalled
        old = (old != null) ? old : getDefaultRenderer();
            // Get the renderer being used (either the renderer that was just 
            // set or the default renderer if the renderer was set to null)
        renderer = getRenderer();
        if (old != null)        // If the old renderer is not null
            old.uninstallRenderer(this);
        if (renderer != null)   // If the new renderer is not null
            renderer.installRenderer(this);
        revalidate();
        repaint();
    }
    /**
     * This stores the average width and height of the tiles into the given 
     * Dimension2D object. If the given Dimension2D object is null, then a new 
     * Dimension2D object will be returned. This version is useful as to avoid 
     * creating a new Dimension2D object. <p>
     * 
     * The {@link #getTileBounds(int, int) getTileBounds} method can be used to 
     * get a more exact size for any particular tile. <p>
     * 
     * This delegates to the {@link 
     * PlayFieldRenderer#getTileSize(JPlayField, Dimension2D) getTileSize} 
     * method of the {@link #getRenderer() play field renderer}. This will 
     * return null if the renderer is null.
     * 
     * @param dim The Dimension2D object to return with the average dimensions 
     * of the tiles, or null.
     * @return The Dimension2D object holding the average dimensions of the 
     * tiles.
     * @see #getTileSize() 
     * @see #getTileBounds(int, int, int, int) 
     * @see #getTileBounds(int, int) 
     * @see #getTileBounds(Tile) 
     * @see #getPlayFieldBounds(Rectangle2D) 
     * @see #getPlayFieldBounds() 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#getTileSize(JPlayField, Dimension2D) 
     */
    public Dimension2D getTileSize(Dimension2D dim){
            // If the renderer is not null, then return the tile size. 
            // Otherwise, return null
        return (getRenderer() != null)?getRenderer().getTileSize(this,dim):null;
    }
    /**
     * This returns a Dimension2D object containing the average width and height 
     * of the tiles. This is equivalent to calling {@link 
     * #getTileSize(Dimension2D) getTileSize}{@code (null)}. <p>
     * 
     * The {@link #getTileBounds(int, int) getTileBounds} method can be used to 
     * get a more exact size for any particular tile. <p>
     * 
     * This delegates to the {@link PlayFieldRenderer#getTileSize(JPlayField) 
     * getTileSize} method of the {@link #getRenderer() play field renderer}. 
     * This will return null if the renderer is null.
     * 
     * @return The Dimension2D object holding the average dimensions of the 
     * tiles.
     * @see #getTileSize(Dimension2D) 
     * @see #getTileBounds(int, int, int, int) 
     * @see #getTileBounds(int, int) 
     * @see #getTileBounds(Tile) 
     * @see #getPlayFieldBounds(Rectangle2D) 
     * @see #getPlayFieldBounds() 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#getTileSize(JPlayField) 
     */
    public Dimension2D getTileSize(){
            // If the renderer is not null, then return the tile size. 
            // Otherwise, return null
        return (getRenderer() != null)?getRenderer().getTileSize(this):null;
    }
    /**
     * This stores the bounds, in this component's coordinate system, for the 
     * play field into the given Rectangle2D object. If the given Rectangle2D 
     * object is null, then a new Rectangle2D object will be returned. This 
     * version is useful as to avoid creating a new Rectangle2D object. <p>
     * 
     * This delegates to the {@link 
     * PlayFieldRenderer#getPlayFieldBounds(JPlayField, Rectangle2D) 
     * getPlayFieldBounds} method of the {@link #getRenderer() play field 
     * renderer}. This will return null if the renderer is null.
     * 
     * @param rect The Rectangle2D object to return with the bounds of the play 
     * field, or null.
     * @return The Rectangle2D object holding the bounds of the play field.
     * @see #getPlayFieldBounds() 
     * @see #getTileSize(Dimension2D) 
     * @see #getTileSize() 
     * @see #getTileBounds(int, int, int, int) 
     * @see #getTileBounds(int, int) 
     * @see #getTileBounds(Tile) 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#getPlayFieldBounds(JPlayField, Rectangle2D) 
     */
    public Rectangle2D getPlayFieldBounds(Rectangle2D rect){
            // If the renderer is not null, then return the play field bounds. 
            // Otherwise, return null
        return (getRenderer() != null)?getRenderer().getPlayFieldBounds(this, 
                rect) : null;
    }
    /**
     * This returns the bounds, in this component's coordinate system, for the 
     * play field. This is equivalent to calling {@link 
     * #getPlayFieldBounds(Rectangle2D) getPlayFieldBounds}{@code (null)}. <p>
     * 
     * This delegates to the {@link 
     * PlayFieldRenderer#getPlayFieldBounds(JPlayField) getPlayFieldBounds} 
     * method of the {@link #getRenderer() play field renderer}. This will 
     * return null if the renderer is null.
     * 
     * @return The Rectangle2D object holding the bounds of the play field.
     * @see #getPlayFieldBounds(Rectangle2D) 
     * @see #getTileSize(Dimension2D) 
     * @see #getTileSize() 
     * @see #getTileBounds(int, int, int, int) 
     * @see #getTileBounds(int, int) 
     * @see #getTileBounds(Tile) 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#getPlayFieldBounds(JPlayField) 
     */
    public Rectangle2D getPlayFieldBounds(){
            // If the renderer is not null, then return the play field bounds. 
            // Otherwise, return null
        return (getRenderer() != null)?getRenderer().getPlayFieldBounds(this) : 
                null;
    }
    /**
     * This returns the bounds, in this component's coordinate system, for the 
     * range of tiles specified by the rows {@code row0} and {@code row1} and 
     * the columns {@code column0} and {@code column1}. Note that {@code row0} 
     * does not necessarily need to be less than or equal to {@code row1}, and 
     * that {@code column0} does not necessarily need to be less than or equal 
     * to {@code column1}. <p>
     * 
     * If the smaller row and/or column are outside the play field's range of 
     * tiles, then this method will return null. If the smaller row and column 
     * are both valid, but the larger row and/or column are outside the play 
     * field's range, then the bounds for only the tile at the smaller row and 
     * column will be returned. Otherwise, the bounds of the valid range of 
     * tiles will be returned. <p>
     * 
     * This delegates to the {@link PlayFieldRenderer#getTileBounds 
     * getTileBounds} method of the {@link #getRenderer() play field renderer}. 
     * This will return null if the renderer is null.
     * 
     * @param row0 One end of the range of rows to get the bounds of.
     * @param row1 The other end of the range of rows to get the bounds of.
     * @param column0 One end of the range of columns to get the bounds of.
     * @param column1 The other end of the range of columns to get the bounds 
     * of.
     * @return The Rectangle2D object with the bounds for the range of tiles, or 
     * null.
     * @see #getTileBounds(int, int) 
     * @see #getTileBounds(Tile) 
     * @see #tileToLocation(int, int) 
     * @see #tileToLocation(Tile) 
     * @see #containsTile(int, int) 
     * @see #getTileSize(Dimension2D) 
     * @see #getTileSize() 
     * @see #getPlayFieldBounds(Rectangle2D) 
     * @see #getPlayFieldBounds() 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#getTileBounds 
     */
    public Rectangle2D getTileBounds(int row0,int row1,int column0,int column1){
            // If the renderer is not null, then return the tile bounds. 
            // Otherwise, return null
        return (getRenderer() != null)?getRenderer().getTileBounds(this,row0,
                row1,column0,column1) : null;
    }
    /**
     * This returns the bounds, in this component's coordinate system, for the 
     * tile at the given row and column. If the row and/or column are outside 
     * the range of the play field, then this returns null. <p>
     * 
     * This is equivalent to calling {@link #getTileBounds(int, int, int, int) 
     * getTileBounds} with the given row for {@code row0} and {@code row1}, and 
     * with the given column for {@code column0} and {@code column1}. As such, 
     * this will return null if the {@link #getRenderer() play field renderer} 
     * is null.
     * 
     * @param row The row of the tile to get the bounds of.
     * @param column The column of the tile to get the bounds of.
     * @return The Rectangle2D object with the bounds of the tile at the given 
     * row and column, or null.
     * @see #getTileBounds(int, int, int, int) 
     * @see #getTileBounds(Tile) 
     * @see #tileToLocation(int, int) 
     * @see #tileToLocation(Tile) 
     * @see #containsTile(int, int) 
     * @see #getTileSize(Dimension2D) 
     * @see #getTileSize() 
     * @see #getPlayFieldBounds(Rectangle2D) 
     * @see #getPlayFieldBounds() 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#getTileBounds 
     */
    public Rectangle2D getTileBounds(int row, int column){
        return getTileBounds(row,row,column,column);
    }
    /**
     * This returns the bounds, in this component's coordinate system, for the 
     * given tile. If the given tile is not in the play field, then this returns 
     * null. <p>
     * 
     * This is equivalent to calling {@link #getTileBounds(int, int) 
     * getTileBounds} with the tile's {@link Tile#getRow() row} and {@link 
     * Tile#getColumn() column} if this component {@link #containsTile(Tile) 
     * contains} the tile. As such, this will return null if either the play 
     * field does not contain the tile or if the {@link #getRenderer() play 
     * field renderer} is null.
     * 
     * @param tile The tile to get the bounds of.
     * @return The Rectangle2D object with the bounds of the tile, or null.
     * @see #getTileBounds(int, int, int, int) 
     * @see #getTileBounds(int, int) 
     * @see #getTile 
     * @see #containsTile(int, int) 
     * @see #containsTile(Tile) 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #tileToLocation(int, int) 
     * @see #tileToLocation(Tile) 
     * @see #getTileSize(Dimension2D) 
     * @see #getTileSize() 
     * @see #getPlayFieldBounds(Rectangle2D) 
     * @see #getPlayFieldBounds() 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#getTileBounds 
     */
    public Rectangle2D getTileBounds(Tile tile){
            // If this play field contains the tile, return its bounds. 
        return (containsTile(tile))?getTileBounds(tile.getRow(),
                tile.getColumn()):null;     // Otherwise, return null
    }
    /**
     * This returns the origin of the tile at the given row and column, in this 
     * component's coordinate system, or null if the given row and/or column are 
     * invalid. <p>
     * 
     * This delegates to the {@link PlayFieldRenderer#tileToLocation 
     * tileToLocation} method of the {@link #getRenderer() play field renderer}. 
     * This will return null if the renderer is null.
     * 
     * @param row The row of the tile to get the location of.
     * @param column The column of the tile to get the location of.
     * @return The origin of the tile, or null.
     * @see #tileToLocation(Tile) 
     * @see #getTileBounds(int, int) 
     * @see #getTileBounds(Tile) 
     * @see #containsTile(int, int) 
     * @see #locationToTile(int, int) 
     * @see #locationToTile(Point) 
     * @see #locationToTile2D(double, double) 
     * @see #locationToTile2D(Point2D) 
     * @see #getTileSize(Dimension2D) 
     * @see #getTileSize() 
     * @see #getPlayFieldBounds(Rectangle2D) 
     * @see #getPlayFieldBounds() 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#tileToLocation 
     */
    public Point2D tileToLocation(int row, int column){
            // If the renderer is not null, then return the origin of the tile. 
            // Otherwise, return null
        return (getRenderer() != null)?getRenderer().tileToLocation(this,row,
                column) : null;
    }
    /**
     * This returns the origin of the given tile, in this component's coordinate 
     * system, or null if the given tile is not in the play field. <p>
     * 
     * This is equivalent to calling {@link #tileToLocation(int, int) 
     * tileToLocation} with the tile's {@link Tile#getRow() row} and {@link 
     * Tile#getColumn() column} if this component {@link #containsTile(Tile) 
     * contains} the tile. As such, this will return null if either the play 
     * field does not contain the tile or if the {@link #getRenderer() play 
     * field renderer} is null.
     * 
     * @param tile The tile to get the location of.
     * @return The origin of the tile, or null.
     * @see #tileToLocation(int, int) 
     * @see #getTileBounds(int, int) 
     * @see #getTileBounds(Tile) 
     * @see #getTile 
     * @see #containsTile(int, int) 
     * @see #containsTile(Tile) 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #locationToTile(int, int) 
     * @see #locationToTile(Point) 
     * @see #locationToTile2D(double, double) 
     * @see #locationToTile2D(Point2D) 
     * @see #getTileSize(Dimension2D) 
     * @see #getTileSize() 
     * @see #getPlayFieldBounds(Rectangle2D) 
     * @see #getPlayFieldBounds() 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#tileToLocation 
     */
    public Point2D tileToLocation(Tile tile){
        // If this play field contains the tile, return its location. Otherwise, 
        return (containsTile(tile))?tileToLocation(tile.getRow(),// return null
                tile.getColumn()):null;
    }
    /**
     * This returns the tile that is closest to the given location in this 
     * component's coordinate system. To determine if the tile actually contains 
     * the specified location, compare the point against the {@link 
     * #getTileBounds(Tile) tile's bounds}. This returns null if there is no 
     * tile at the given location. <p> 
     * 
     * This method uses integer precision, and as such the tile returned may be 
     * a rough approximation for the tile closest to the given location. The 
     * {@link #locationToTile2D(double, double) locationToTile2D} method can be 
     * used to get a more accurate result if necessary. <p>
     * 
     * This delegates to the {@link PlayFieldRenderer#locationToTile 
     * locationToTile} method of the {@link #getRenderer() play field renderer}. 
     * This will return null if the renderer is null.
     * 
     * @param x The x-coordinate of the point to get the tile at.
     * @param y The y-coordinate of the point to get the tile at.
     * @return The tile closest to the given location, or null.
     * @see #locationToTile(Point) 
     * @see #locationToTile2D(double, double) 
     * @see #locationToTile2D(Point2D) 
     * @see #tileToLocation(Tile) 
     * @see #tileToLocation(int, int) 
     * @see #getTileBounds(Tile) 
     * @see #getTileBounds(int, int) 
     * @see #getTile 
     * @see #getTileSize(Dimension2D) 
     * @see #getTileSize() 
     * @see #getPlayFieldBounds(Rectangle2D) 
     * @see #getPlayFieldBounds() 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#locationToTile 
     */
    public Tile locationToTile(int x, int y){
            // If the renderer is not null, then return the tile at the 
            // location. Otherwise, return null
        return (getRenderer()!=null)?getRenderer().locationToTile(this,x,y):null;
    }
    /**
     * This returns the tile that is closest to the given location in this 
     * component's coordinate system. To determine if the tile actually contains 
     * the specified location, compare the point against the {@link 
     * #getTileBounds(Tile) tile's bounds}. This returns null if there is no 
     * tile at the given location. <p> 
     * 
     * This method uses integer precision, and as such the tile returned may be 
     * a rough approximation for the tile closest to the given location. The 
     * {@link #locationToTile2D(Point2D) locationToTile2D} method can be used to 
     * get a more accurate result if necessary. <p>
     * 
     * This is equivalent to calling {@link #locationToTile locationToTile} with 
     * the given point's x and y coordinates. As such, this will return null if 
     * the {@link #getRenderer() play field renderer} is null.
     * 
     * @param point The point to get the tile at.
     * @return The tile closest to the given location, or null.
     * @throws NullPointerException If the point is null.
     * @see #locationToTile(int, int) 
     * @see #locationToTile2D(double, double) 
     * @see #locationToTile2D(Point2D) 
     * @see #tileToLocation(Tile) 
     * @see #tileToLocation(int, int) 
     * @see #getTileBounds(Tile) 
     * @see #getTileBounds(int, int) 
     * @see #getTile 
     * @see #getTileSize(Dimension2D) 
     * @see #getTileSize() 
     * @see #getPlayFieldBounds(Rectangle2D) 
     * @see #getPlayFieldBounds() 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#locationToTile 
     */
    public Tile locationToTile(Point point){
        return locationToTile(point.x,point.y);
    }
    /**
     * This returns the tile that is closest to the given location in this 
     * component's coordinate system. To determine if the tile actually contains 
     * the specified location, compare the point against the {@link 
     * #getTileBounds(Tile) tile's bounds}. This returns null if there is no 
     * tile at the given location. <p> 
     * 
     * This delegates to the {@link PlayFieldRenderer#locationToTile2D 
     * locationToTile2D} method of the {@link #getRenderer() play field 
     * renderer}. This will return null if the renderer is null.
     * 
     * @param x The x-coordinate of the point to get the tile at.
     * @param y The y-coordinate of the point to get the tile at.
     * @return The tile closest to the given location, or null.
     * @see #locationToTile2D(Point2D) 
     * @see #locationToTile(int, int) 
     * @see #locationToTile(Point) 
     * @see #tileToLocation(Tile) 
     * @see #tileToLocation(int, int) 
     * @see #getTileBounds(Tile) 
     * @see #getTileBounds(int, int) 
     * @see #getTile 
     * @see #getTileSize(Dimension2D) 
     * @see #getTileSize() 
     * @see #getPlayFieldBounds(Rectangle2D) 
     * @see #getPlayFieldBounds() 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#locationToTile2D 
     */
    public Tile locationToTile2D(double x, double y){
            // If the renderer is not null, then return the tile at the 
            // location. Otherwise, return null
        return (getRenderer()!=null)?getRenderer().locationToTile2D(this,x,y):null;
    }
    /**
     * This returns the tile that is closest to the given location in this 
     * component's coordinate system. To determine if the tile actually contains 
     * the specified location, compare the point against the {@link 
     * #getTileBounds(Tile) tile's bounds}. This returns null if there is no 
     * tile at the given location. <p> 
     * 
     * This is equivalent to calling {@link #locationToTile2D(double, double) 
     * locationToTile2D} with the given point's x and y coordinates. As such, 
     * this will return null if the {@link #getRenderer() play field renderer} 
     * is null.
     * 
     * @param point The point to get the tile at.
     * @return The tile closest to the given location, or null.
     * @throws NullPointerException If the point is null.
     * @see #locationToTile2D(double, double) 
     * @see #locationToTile(int, int) 
     * @see #locationToTile(Point) 
     * @see #tileToLocation(Tile) 
     * @see #tileToLocation(int, int) 
     * @see #getTileBounds(Tile) 
     * @see #getTileBounds(int, int) 
     * @see #getTile 
     * @see #getTileSize(Dimension2D) 
     * @see #getTileSize() 
     * @see #getPlayFieldBounds(Rectangle2D) 
     * @see #getPlayFieldBounds() 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#locationToTile2D 
     */
    public Tile locationToTile2D(Point2D point){
        return locationToTile2D(point.getX(),point.getY());
    }
    /**
     * This paints this component to the given graphics context. This will first 
     * call the UI delegate's paint method if the UI delegate is non-null. A 
     * copy of the {@code Graphics} object is passed to the UI delegate so as to 
     * protect the rest of the paint code from irrevocable changes (for example, 
     * {@code Graphics.translate}). After the UI delegate finishes, this will 
     * call the {@link #getRenderer play field renderer}'s {@link 
     * PlayFieldRenderer#paint paint} method to paint the play field if the 
     * renderer is non-null. A second copy of the {@code Graphics} object, now 
     * in the form of a {@code Graphics2D} object, is passed to the renderer to 
     * again protect the rest of the paint code from irrevocable changes. If the 
     * {@code Graphics} object is not a {@code Graphics2D} object, then the 
     * renderer will paint to an image which is then painted by the {@code 
     * Graphics} object. <p>
     * 
     * If you override this in a subclass you should not make any permanent 
     * changes to the given {@code Graphics} object. You may find it easier to 
     * create and use a copy of the given {@code Graphics} object. Further, if 
     * you do not invoke super's implementation you must honor the opaque 
     * property (i.e. if this component is opaque then you must completely fill 
     * in the background with an opaque color), least risk the appearance of 
     * visual artifacts. <p>
     * 
     * The given {@code Graphics} object may have a transform other than the 
     * identity transform applied to it. If this is the case, then you might get 
     * unexpected results if you cumulatively apply another transform.
     * 
     * @param g The {@code Graphics} object to render to.
     * @see #paint 
     * @see ComponentUI
     * @see PlayFieldRenderer
     * @see #getRenderer 
     * @see PlayFieldRenderer#paint 
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        int w = getWidth();             // Get the width of the component
        int h = getHeight();            // Get the height of the component
            // Get the renderer to use to paint the play field
        PlayFieldRenderer r = getRenderer();
            // If the width or height are 0 or less, the graphics context is 
            // null, or the renderer is null
        if (w <= 0 || h <= 0 || g == null || r == null)
            return;
        g = g.create(0, 0, w, h);
            // An image to render to if the given graphics context is not a 
        BufferedImage img = null;       // Graphics2D object
        Graphics2D g2D; // This will get the Graphics2D object to render to.
            // If the graphics context is a Graphics2D object
        if (g instanceof Graphics2D)
            g2D = (Graphics2D) g;
        else if (g != null){            // If the graphics context is not null
                // Create the image to render to
            img = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
                // Create a graphics context for the image
            g2D = img.createGraphics();
                // Configure the image graphics context to match the given 
                // graphics context
            g2D.setFont(g.getFont());
            g2D.setColor(g.getColor());
        }
        else                            //If the graphics conext is somehow null
            return;
        r.paint(g2D, this, w, h);
        if (img != null){               // If this rendered to an image
            g2D.dispose();
            g.drawImage(img, 0, 0, w, h, this);
        }
        g.dispose();
    }
    /**
     * This returns the model currently used by this JPlayField to obtain the 
     * {@link Tile tiles}.
     * @return The PlayFieldModel that provides the displayed tiles.
     * @see #setModel
     * @see PlayFieldModel
     * @see AbstractPlayFieldModel
     * @see DefaultPlayFieldModel
     */
    public PlayFieldModel getModel(){
        return model;
    }
    /**
     * This sets the model that this JPlayField uses to obtain the {@link Tile 
     * tiles}.
     * @param model The PlayFieldModel that provides the displayed tiles 
     * (cannot be null).
     * @throws NullPointerException If the model is null.
     * @see #getModel
     * @see PlayFieldModel
     * @see AbstractPlayFieldModel
     * @see DefaultPlayFieldModel
     */
    public void setModel(PlayFieldModel model){
            // Check if the model is null
        Objects.requireNonNull(model, "Model cannot be null");
        if (model == this.model)                // If the model would not change
            return;
        PlayFieldModel old = this.model;        // Get the old model
        this.model = model;
        if (old != null)                        // If the old model is not null
            old.removePlayFieldListener(handler);
        model.addPlayFieldListener(handler);
        firePropertyChange(MODEL_PROPERTY_CHANGED,old,model);
        firePlayFieldStructureChanged();
    }
    /**
     * This returns the number of rows of tiles that are currently in the model. 
     * This delegates to the {@link PlayFieldModel#getRowCount() getRowCount} 
     * method of the currently set {@link #getModel() model}.
     * @return The number of rows in the model.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#getRowCount 
     * @see #setRowCount 
     * @see #getColumnCount 
     * @see #setColumnCount 
     * @see #getTileCount 
     */
    public int getRowCount(){
        return getModel().getRowCount();
    }
    /**
     * This sets the number of rows of tiles that are in the model. This 
     * delegates to the {@link PlayFieldModel#setRowCount setRowCount} method 
     * of the currently set {@link #getModel() model}. As such, this method only 
     * works if the model implements the {@code setRowCount} method.
     * @param rows The number of rows for the model.
     * @throws UnsupportedOperationException If the {@code setRowCount} method 
     * is not supported by the model.
     * @throws IllegalArgumentException If the model does not support {@code 
     * rows} number of rows.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#setRowCount 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #setColumnCount 
     * @see #getTileCount 
     */
    public void setRowCount(int rows){
        getModel().setRowCount(rows);
    }
    /**
     * This returns the number of columns of tiles that are currently in the 
     * model. This delegates to the {@link PlayFieldModel#getColumnCount() 
     * getColumnCount} method of the currently set {@link #getModel() model}.
     * @return The number of column in the model.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#getColumnCount 
     * @see #setColumnCount 
     * @see #getRowCount 
     * @see #setRowCount 
     * @see #getTileCount 
     */
    public int getColumnCount(){
        return getModel().getColumnCount();
    }
    /**
     * This sets the number of columns of tiles that are in the model. This 
     * delegates to the {@link PlayFieldModel#setColumnCount setColumnCount} 
     * method of the currently set {@link #getModel() model}. As such, this 
     * method only works if the model implements the {@code setColumnCount} 
     * method.
     * @param columns The number of columns for the model.
     * @throws UnsupportedOperationException If the {@code setColumnCount} 
     * method is not supported by the model.
     * @throws IllegalArgumentException If the model does not support {@code 
     * columns} number of columns.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#setColumnCount 
     * @see #getColumnCount 
     * @see #getRowCount 
     * @see #setRowCount 
     * @see #getTileCount 
     */
    public void setColumnCount(int columns){
        getModel().setColumnCount(columns);
    }
    /**
     * This returns the total number of tiles in the model. This delegates to 
     * the {@link PlayFieldModel#getTileCount() getTileCount} method of the 
     * currently set {@link #getModel() model}.
     * @return The number of tiles in the model.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#getTileCount 
     * @see #getRowCount 
     * @see #setRowCount 
     * @see #getColumnCount 
     * @see #setColumnCount 
     */
    public int getTileCount(){
        return getModel().getTileCount();
    }
    /**
     * This returns the tile located at the given row and column in the model. 
     * This delegates to the {@link PlayFieldModel#getTile getTile} method of 
     * the currently set {@link #getModel() model}. <p>
     * 
     * Please note that tiles store their corresponding row and column, which 
     * can be retrieved from the tile at any time by using its {@link 
     * Tile#getRow() getRow} and {@link Tile#getColumn() getColumn} methods.
     * 
     * @param row The row of the tile to get.
     * @param column The column of the tile to get.
     * @return The tile at the given row and column.
     * @throws IndexOutOfBoundsException If either the row or column are out of 
     * bounds.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#getTile 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #containsTile(int, int) 
     * @see #containsTile(Tile) 
     * @see #getRandomTile 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     */
    public Tile getTile(int row, int column){
        return getModel().getTile(row, column);
    }
    /**
     * This returns a randomly selected tile from the model. <p>
     * 
     * Please note that tiles store their corresponding row and column, which 
     * can be retrieved from the tile at any time by using its {@link 
     * Tile#getRow() getRow} and {@link Tile#getColumn() getColumn} methods.
     * 
     * @param rand The random number generator to use to get the row and column 
     * (cannot be null).
     * @return A random tile.
     * @throws NullPointerException If the random number generator is null.
     * @see #getTile 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #containsTile(int, int) 
     * @see #containsTile(Tile) 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #getRandomFilteredTile
     * @see #getRandomEmptyTile 
     */
    public Tile getRandomTile(Random rand){
        return getTile(rand.nextInt(getRowCount()),
                rand.nextInt(getColumnCount()));
    }
    /**
     * This returns a randomly selected tile from the given list of tiles. <p>
     * 
     * Please note that tiles store their corresponding row and column, which 
     * can be retrieved from the tile at any time by using its {@link 
     * Tile#getRow() getRow} and {@link Tile#getColumn() getColumn} methods.
     * 
     * @param rand The random number generator to use to get the tile (cannot be 
     * null).
     * @param tiles The list of tiles to get a random tile from. If this is null 
     * or empty, then this returns null.
     * @return A random tile from the given list, or null if the list is null or 
     * empty.
     * @throws NullPointerException If the random number generator is null.
     * @see #getTile 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #containsTile(int, int) 
     * @see #containsTile(Tile) 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #getRandomTile(Random) 
     * @see #getRandomFilteredTile(Random, Predicate) 
     * @see #getRandomEmptyTile(Random) 
     */
    protected Tile getRandomTile(Random rand, List<Tile> tiles){
            // If the list is null or empty, return null. Otherwise, return a 
            // random tile from the list
        return (tiles == null || tiles.isEmpty()) ? null : 
                tiles.get(rand.nextInt(tiles.size()));
    }
    /**
     * This returns a randomly selected tile from the model that satisfies the 
     * given predicate. <p>
     * 
     * Please note that tiles store their corresponding row and column, which 
     * can be retrieved from the tile at any time by using its {@link 
     * Tile#getRow() getRow} and {@link Tile#getColumn() getColumn} methods.
     * 
     * @param rand The random number generator to use to get the tile (cannot be 
     * null).
     * @param filter The predicate to use to filter the tiles (cannot be 
     * null).
     * @return A random tile that matches the given predicate, or null if there 
     * are no tiles in the model that match the predicate.
     * @throws NullPointerException If either the random number generator or the 
     * predicate are null.
     * @see #getTile 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #containsTile(int, int) 
     * @see #containsTile(Tile) 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #getRandomTile 
     * @see #getRandomEmptyTile 
     * @see #getModel
     * @see PlayFieldModel#getFilteredTileList 
     * @see PlayFieldModel#getFilteredTileCount 
     */
    public Tile getRandomFilteredTile(Random rand,Predicate<? super Tile>filter){
        return getRandomTile(rand,getModel().getFilteredTileList(filter));
    }
    /**
     * This returns a randomly selected {@link Tile#isEmpty() empty tile} from 
     * the model. <p>
     * 
     * Please note that tiles store their corresponding row and column, which 
     * can be retrieved from the tile at any time by using its {@link 
     * Tile#getRow() getRow} and {@link Tile#getColumn() getColumn} methods.
     * 
     * @param rand The random number generator to use to get the tile (cannot be 
     * null).
     * @return A random empty tile, or null if there are no empty tiles in the 
     * model.
     * @throws NullPointerException If the random number generator is null.
     * @see #getTile 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #containsTile(int, int) 
     * @see #containsTile(Tile) 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see Tile#isEmpty 
     * @see #getRandomTile 
     * @see #getRandomFilteredTile 
     * @see #getEmptyTiles 
     * @see #getEmptyTileCount 
     */
    public Tile getRandomEmptyTile(Random rand){
        return getRandomTile(rand,getEmptyTiles());
    }
    /**
     * This returns whether the given row and column are within the range of the 
     * model. This delegates to the {@link PlayFieldModel#contains(int, int) 
     * contains} method of the currently set {@link #getModel() model}. 
     * @param row The row to check.
     * @param column The column to check.
     * @return Whether the given row and column are within range of the model.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#contains(int, int) 
     * @see #containsTile(Tile) 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTile 
     */
    public boolean containsTile(int row, int column){
        return getModel().contains(row, column);
    }
    /**
     * This returns whether the model contains the given tile. This delegates to 
     * the {@link PlayFieldModel#contains(Tile) contains} method of the 
     * currently set {@link #getModel() model}.
     * @param tile The tile to check for.
     * @return Whether the model contains the given tile.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#contains(Tile) 
     * @see #containsTile(int, int) 
     * @see #getTile 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     */
    public boolean containsTile(Tile tile){
        return getModel().contains(tile);
    }
    /**
     * This {@link Tile#clear() clears} all the tiles in the model that are 
     * between the rows {@code fromRow} and {@code toRow}, exclusive, and 
     * between the columns {@code fromColumn} and {@code toColumn}, exclusive. 
     * This delegates to the {@link PlayFieldModel#clearTiles clearTiles} method 
     * of the currently set {@link #getModel() model}.
     * @param fromRow The index of the first row of tiles to be cleared.
     * @param toRow The index of the row after the last row of tiles to be 
     * cleared.
     * @param fromColumn The index of the first column of tiles to be cleared.
     * @param toColumn The index of the column after the last column of tiles to 
     * be cleared.
     * @throws IndexOutOfBoundsException If either the {@code fromRow}, {@code 
     * toRow}, {@code fromColumn}, or {@code toColumn} are out of bounds or 
     * either the {@code fromRow} is greater than the {@code toRow} or the 
     * {@code fromColumn} is greater than the {@code toColumn}.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#clearTiles 
     * @see #clearTiles() 
     * @see #getTile 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     */
    public void clearTiles(int fromRow,int toRow,int fromColumn,int toColumn){
        getModel().clearTiles(fromRow, toRow, fromColumn, toColumn);
    }
    /**
     * This {@link Tile#clear() clears} all the tiles in the model. This 
     * delegates to the {@link PlayFieldModel#clearTiles() clearTiles} method of 
     * the currently set {@link #getModel() model}.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#clearTiles()
     * @see #clearTiles(int, int, int, int) 
     * @see #getTile 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     */
    public void clearTiles(){
        getModel().clearTiles();
    }
    /**
     * This returns the tile in this model that is adjacent to the given tile in 
     * the given direction. The {@code wrapAround} value determines how to treat 
     * getting the tile adjacent to the tiles at the edges of the play field 
     * when the adjacent tile would be out of bounds. If {@code wrapAround} is 
     * {@code true}, then this will wrap around and get a tile from the other 
     * side of the play field when the adjacent tile would be out of bounds. 
     * Otherwise, this will return null when the adjacent tile is out of bounds. 
     * This delegates to the {@link PlayFieldModel#getAdjacentTile 
     * getAdjacentTile} method of the currently set {@link #getModel() model}.
     * @param tile The tile to get the adjacent tile of (cannot be null).
     * @param direction The direction indicating which adjacent tile to return. 
     * This should be one of the following: 
     *      {@link #UP_DIRECTION} to get the tile above, 
     *      {@link #DOWN_DIRECTION} to get the tile below, 
     *      {@link #LEFT_DIRECTION} to get the tile to the left, or 
     *      {@link #RIGHT_DIRECTION} to get the tile to the right.
     * @param wrapAround Whether this should wrap around and get the tile on the 
     * opposite side of the play field if the adjacent tile would be out of 
     * bounds.
     * @return The tile adjacent to the given tile. If the given tile is out of 
     * bounds or the requested adjacent tile is out of bounds and {@code 
     * wrapAround} is false, then this will return null. 
     * @throws NullPointerException If the given tile is null.
     * @throws IllegalArgumentException If the given direction is not one of the 
     * four direction flags.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see SnakeUtilities#getDirections 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#getAdjacentTile 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTile 
     * @see #containsTile(int, int) 
     * @see #containsTile(Tile) 
     * @see #getAdjacentTile(Tile, int) 
     */
    public Tile getAdjacentTile(Tile tile, int direction, boolean wrapAround){
        return getModel().getAdjacentTile(tile, direction, wrapAround);
    }
    /**
     * This returns the tile in this model that is adjacent to the given tile in 
     * the given direction. This version uses whether the {@link 
     * #ALTERNATE_TYPE_FLAG} flag is set on the {@code direction} to determine 
     * whether this should wrap around when getting an adjacent tile that would 
     * be out of bounds, with the {@code ALTERNATE_MODE_FLAG} flag indicating 
     * that this should not wrap around. This delegates to the {@link 
     * PlayFieldModel#getAdjacentTile(Tile, int) getAdjacentTile} method of the 
     * currently set {@link #getModel() model}.
     * @param tile The tile to get the adjacent tile of (cannot be null).
     * @param direction The direction indicating which adjacent tile to return. 
     * This should be one of the following: 
     *      {@link #UP_DIRECTION} to get the tile above, 
     *      {@link #DOWN_DIRECTION} to get the tile below, 
     *      {@link #LEFT_DIRECTION} to get the tile to the left, 
     *      {@link #RIGHT_DIRECTION} to get the tile to the right, or
     *      {@link #ALTERNATE_TYPE_FLAG} {@code OR}'d with any of the previously 
     *          stated values to indicate that this should not wrap around. 
     * @return The tile adjacent to the given tile. If the given tile is out of 
     * bounds or the requested adjacent tile is out of bounds and {@link 
     * #ALTERNATE_TYPE_FLAG} is set on the given {@code direction}, then this 
     * will return null. 
     * @throws NullPointerException If the given tile is null.
     * @throws IllegalArgumentException If the given direction is not one of the 
     * four direction flags with or without the {@link #ALTERNATE_TYPE_FLAG} 
     * set.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALTERNATE_TYPE_FLAG
     * @see SnakeUtilities#getDirections 
     * @see SnakeUtilities#getFlag 
     * @see SnakeUtilities#setFlag 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#getAdjacentTile(Tile, int) 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTile 
     * @see #containsTile(int, int) 
     * @see #containsTile(Tile) 
     * @see #getAdjacentTile(Tile, int, boolean) 
     */
    public Tile getAdjacentTile(Tile tile, int direction){
        return getModel().getAdjacentTile(tile, direction);
    }
    /**
     * This returns a list of tiles in the model that are currently {@link 
     * Tile#isEmpty empty}. This delegates to the {@link 
     * PlayFieldModel#getEmptyTiles getEmptyTiles} method of the currently set 
     * {@link #getModel model}.
     * @return A list of tiles that are empty, or an empty list if no tiles are 
     * empty.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#getEmptyTiles 
     * @see #getTile 
     * @see #containsTile(int, int) 
     * @see #containsTile(Tile) 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTileCount 
     * @see #getEmptyTileCount 
     * @see #getAppleTiles 
     * @see #getAppleTileCount 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     * @see #clearTiles() 
     * @see #clearTiles(int, int, int, int) 
     */
    public List<Tile> getEmptyTiles(){
        return getModel().getEmptyTiles();
    }
    /**
     * This returns the number of tiles that are currently {@link Tile#isEmpty 
     * empty}. This delegates to the {@link PlayFieldModel#getEmptyTileCount 
     * getEmptyTileCount} method of the currently set {@link #getModel model}.
     * @return The number of tiles that are empty.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#getEmptyTileCount 
     * @see #getTile 
     * @see #containsTile(int, int) 
     * @see #containsTile(Tile) 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTileCount 
     * @see #getEmptyTiles 
     * @see #getAppleTiles 
     * @see #getAppleTileCount 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     * @see #clearTiles(int, int, int, int) 
     * @see #clearTiles() 
     */
    public int getEmptyTileCount(){
        return getModel().getEmptyTileCount();
    }
    /**
     * This returns a list of tiles in the model that are currently {@link 
     * Tile#isApple apple tiles}. This delegates to the {@link 
     * PlayFieldModel#getAppleTiles getAppleTiles} method of the currently 
     * set {@link #getModel model}.
     * @return A list of the apple tiles in the model, or an empty list if no 
     * tiles are apple tiles.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#getAppleTiles 
     * @see #getTile 
     * @see #containsTile(int, int) 
     * @see #containsTile(Tile) 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTileCount 
     * @see #getAppleTileCount 
     * @see #getEmptyTiles 
     * @see #getEmptyTileCount 
     * @see Tile#isApple 
     * @see Tile#setApple 
     * @see Tile#setAppleIfEmpty 
     */
    public List<Tile> getAppleTiles(){
        return getModel().getAppleTiles();
    }
    /**
     * This returns the number of tiles that are currently {@link Tile#isApple  
     * apple tiles}. This delegates to the {@link 
     * PlayFieldModel#getAppleTileCount getAppleTileCount} method of the 
     * currently set {@link #getModel model}.
     * @return The number of tiles that are apple tiles.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#getAppleTileCount 
     * @see #getTile 
     * @see #containsTile(int, int) 
     * @see #containsTile(Tile) 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTileCount 
     * @see #getAppleTiles 
     * @see #getEmptyTiles 
     * @see #getEmptyTileCount 
     * @see Tile#isApple 
     * @see Tile#setApple 
     * @see Tile#setAppleIfEmpty 
     */
    public int getAppleTileCount(){
        return getModel().getAppleTileCount();
    }
    /**
     * This returns whether the tiles are undergoing a series of changes. <p> 
     * 
     * This delegates to the {@link PlayFieldModel#getTilesAreAdjusting() 
     * getTilesAreAdjusting} method of the currently set {@link #getModel() 
     * model}.
     * @return Whether the tiles are undergoing a series of changes.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#getTilesAreAdjusting 
     * @see #setTilesAreAdjusting 
     */
    public boolean getTilesAreAdjusting(){
        return getModel().getTilesAreAdjusting();
    }
    /**
     * This sets whether the tiles are undergoing a series of changes. This 
     * indicates whether or not upcoming changes to the tiles should be 
     * considered part of a single change. This allows listeners to update only 
     * when a change has been finalized as opposed to handling all the 
     * individual tile changes. <p>
     * 
     * You may want to use this directly if making a series of changes that 
     * should be considered part of a single change. <p>
     * 
     * This delegates to the {@link PlayFieldModel#setTilesAreAdjusting 
     * setTilesAreAdjusting} method of the currently set {@link #getModel() 
     * model}.
     * 
     * @param isAdjusting Whether upcoming changes to the tiles should be 
     * considered part of a single change.
     * @see #getModel 
     * @see #setModel 
     * @see PlayFieldModel#setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see PlayFieldEvent#getTilesAreAdjusting 
     */
    public void setTilesAreAdjusting(boolean isAdjusting){
        getModel().setTilesAreAdjusting(isAdjusting);
    }
    /**
     * This returns whether the play field will be rendered in high quality. It 
     * is up to the {@link #getRenderer() play field renderer} to define what 
     * that this entails. The default value for this is defined by the renderer.
     * @return Whether the play field should be rendered in high quality.
     * @see #setHighQuality 
     * @see #getRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#isHighQuality 
     */
    public boolean isHighQuality(){
        Boolean value = hqEnabled;  // Get whether this renders in high quality
            // If the value has not been set and the renderer is not null
        if (value == null && getRenderer() != null)
                //Get whether the renderer says this is rendered in high quality
            value = getRenderer().isHighQuality(this);
            // If the high quality property is not null, return it. Otherwise, 
        return (value != null) ? value : false;     // return false.
    }
    /**
     * This sets whether the play field will be rendered in high quality. What 
     * this means is up to the interpretation of the {@link #getRenderer play 
     * field renderer}, with some choosing to use different rendering algorithms 
     * and settings to favor quality over speed or to use different techniques 
     * to render the tiles. Some renderers may choose to ignore this property. 
     * The default value for this is defined by the renderer.
     * @param value Whether to render the play field in high quality.
     * @see #isHighQuality 
     * @see #getRenderer 
     * @see PlayFieldRenderer#isHighQuality 
     * @see PlayFieldRenderer
     */
    public void setHighQuality(boolean value){
        boolean old = isHighQuality();  // Get the old value for this
        hqEnabled = value;
        firePropertyChange(HIGH_QUALITY_PROPERTY_CHANGED,old,value);
        if (old != value)               // If the value has changed
            repaint();
    }
    /**
     * This returns whether the tile background will be painted. If this is 
     * true, then the {@link #getTileBackground() tile background color} will be 
     * used to fill the area behind the tiles. Otherwise, the tiles will have no 
     * background other than the component's {@link #getBackground background} 
     * if {@link #isOpaque opaque}, and whatever is behind the component if it's 
     * not. The default value for this is defined by the {@link #getRenderer 
     * play field renderer}.
     * @return Whether the tile background should be painted.
     * @see #setTileBackgroundPainted 
     * @see #setTileBackground 
     * @see #getTileBackground 
     * @see #getRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#isTileBackgroundPainted 
     */
    public boolean isTileBackgroundPainted(){
        Boolean value = paintTileBG;  // Get whether this paints the background
            // If the value has not been set and the renderer is not null
        if (value == null && getRenderer() != null)
                //Get whether the renderer says the background is rendererd
            value = getRenderer().isTileBackgroundPainted(this);
            // If the paint background property is not null, return it. 
        return (value != null) ? value : true;  // Otherwise, return true.
    }
    /**
     * This sets whether the tile background will be painted. If this is true, 
     * then the {@link #getTileBackground() tile background color} will be used 
     * to fill the area behind the tiles. Otherwise, the tiles will have no 
     * background other than the component's {@link #getBackground background} 
     * if {@link #isOpaque opaque}, and whatever is behind the component if it's 
     * not.It is up to the {@link #getRenderer() play field renderer} to honor 
     * this property, and some may choose to ignore it. The default value for 
     * this is defined by the renderer.
     * @param value Whether the tile background should be painted.
     * @see #isTileBackgroundPainted 
     * @see #setTileBackground 
     * @see #getTileBackground 
     * @see #getRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#isTileBackgroundPainted 
     */
    public void setTileBackgroundPainted(boolean value){
        boolean old = isTileBackgroundPainted();// Get the old value for this
        paintTileBG = value;
        firePropertyChange(TILE_BACKGROUND_PAINTED_PROPERTY_CHANGED,old,value);
        if (old != value)               // If the value has changed
            repaint();
    }
    /**
     * This returns whether the tile border will be painted. The default value 
     * for this is defined by the {@link #getRenderer() play field renderer}.
     * @return Whether the tile border should be painted.
     * @see #setTileBorderPainted 
     * @see #setTileBorder 
     * @see #getTileBorder 
     * @see #getRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#isTileBorderPainted 
     */
    public boolean isTileBorderPainted(){
        Boolean value = paintTileBorder;   // Get whether this paints the border
            // If the value has not been set and the renderer is not null
        if (value == null && getRenderer() != null)
                //Get whether the renderer says the border is rendererd
            value = getRenderer().isTileBorderPainted(this);
            // If the paint border property is not null, return it. Otherwise, 
        return (value != null) ? value : true;  // return true.
    }
    /**
     * This sets whether the tile border will be painted. It is up to the {@link 
     * #getRenderer() play field renderer} to honor this property, and some may 
     * choose to ignore it. The default value for this is defined by the 
     * renderer.
     * @param value Whether the tile border should be painted.
     * @see #isTileBorderPainted 
     * @see #setTileBorder 
     * @see #getTileBorder 
     * @see #getRenderer 
     * @see PlayFieldRenderer
     * @see PlayFieldRenderer#isTileBorderPainted 
     */
    public void setTileBorderPainted(boolean value){
        boolean old = isTileBorderPainted();    // Get the old value for this
        paintTileBorder = value;
        firePropertyChange(TILE_BORDER_PAINTED_PROPERTY_CHANGED,old,value);
        if (old != value)               // If the value has changed
            repaint();
    }
    /**
     * This returns whether the tile background color has been set to a non-null 
     * value.
     * @return Whether {@code setTileBackground} has been invoked with a 
     * non-null value.
     * @see #getTileBackground 
     * @see #setTileBackground 
     */
    public boolean isTileBackgroundSet(){
        return tileBG != null;
    }
    /**
     * This returns the color to use for drawing the background of the tiles. If 
     * the tile background color has been set to a non-null value, then that is 
     * what is returned. Otherwise, if the {@link #getRenderer() play field 
     * renderer}'s {@link PlayFieldRenderer#getTileBackground 
     * getTileBackground} method returns a non-null value, then that will be 
     * returned. Otherwise, this will return {@link #TILE_BACKGROUND_COLOR}.
     * @return The color to use for drawing the tile background.
     * @see #setTileBackground 
     * @see #isTileBackgroundSet 
     * @see #isTileBackgroundPainted 
     * @see #setTileBackgroundPainted 
     * @see #getRenderer 
     * @see PlayFieldRenderer#getTileBackground 
     * @see PlayFieldRenderer
     * @see #TILE_BACKGROUND_COLOR
     * @see #getBackground 
     * @see #setBackground 
     */
    public Color getTileBackground(){
        Color color = tileBG;   // Get the currently set tile background color
            // If the color has not been set and the renderer is not null
        if (color == null && getRenderer() != null)
                // Get the tile background color from the renderer
            color = getRenderer().getTileBackground(this);
            // If the tile background color is not null, return it. Otherwise, 
            // return the default color
        return (color!=null)?color:TILE_BACKGROUND_COLOR;
    }
    /**
     * This sets the color to use for drawing the background of the tiles. Any 
     * subsequent call to {@code getTileBackground} will return this value, and 
     * the {@link #getRenderer() play field renderer} will not be asked for the 
     * color to use. Setting the tile background color to null will reset it 
     * back to the default behavior.
     * @param bg The color to use for drawing the tile background, or null.
     * @see #getTileBackground 
     * @see #isTileBackgroundSet 
     * @see #TILE_BACKGROUND_COLOR
     * @see #isTileBackgroundPainted 
     * @see #setTileBackgroundPainted 
     * @see #getBackground 
     * @see #setBackground 
     */
    public void setTileBackground(Color bg){
            // If the new tile background is the same as the old tile background
        if (Objects.equals(tileBG, bg))
            return;
        Color old = tileBG;         // Get the old tile background
        tileBG = bg;
        firePropertyChange(TILE_BACKGROUND_PROPERTY_CHANGED,old,bg);
        repaint();
    }
    /**
     * This returns whether the tile border color has been set to a non-null 
     * value.
     * @return Whether {@code setTileBorder} has been invoked with a non-null 
     * value.
     * @see #getTileBorder 
     * @see #setTileBorder 
     */
    public boolean isTileBorderSet(){
        return tileBorder != null;
    }
    /**
     * This returns the color to use for drawing the border around the tiles. If 
     * the tile border color has been set to a non-null value, then that is what 
     * is returned. Otherwise, if the {@link #getRenderer() play field 
     * renderer}'s {@link PlayFieldRenderer#getTileBorder getTileBorder} method 
     * returns a non-null value, then that will be returned. Otherwise, this 
     * will return {@link #TILE_BORDER_COLOR}.
     * @return The color to use for drawing the tile border.
     * @see #setTileBorder 
     * @see #isTileBorderSet 
     * @see #isTileBorderPainted 
     * @see #setTileBorderPainted 
     * @see #getRenderer 
     * @see PlayFieldRenderer#getTileBorder 
     * @see PlayFieldRenderer
     * @see #TILE_BORDER_COLOR
     */
    public Color getTileBorder(){
        Color color = tileBorder;   // Get the currently set tile border color
            // If the color has not been set and the renderer is not null
        if (color == null && getRenderer() != null)
                // Get the tile border color from the renderer
            color = getRenderer().getTileBorder(this);
            // If the tile border color is not null, return it. Otherwise, 
            // return the default color
        return (color!=null)?color:TILE_BORDER_COLOR;   
    }
    /**
     * This sets the color to use for drawing the border around the tiles. Any 
     * subsequent call to {@code getTileBorder} will return this value, and the 
     * {@link #getRenderer() play field renderer} will not be asked for the 
     * color to use. Setting the tile border color to null will reset it back to 
     * the default behavior.
     * @param color The color to use for drawing the tile border, or null.
     * @see #getTileBorder 
     * @see #isTileBorderSet 
     * @see #TILE_BORDER_COLOR
     * @see #isTileBorderPainted 
     * @see #setTileBorderPainted 
     */
    public void setTileBorder(Color color){
            // If the new tile border color is the same as the old tile border 
        if (Objects.equals(tileBorder, color))  // color 
            return;
        Color old = tileBorder;     // Get the old tile border color
        tileBorder = color;
        firePropertyChange(TILE_BORDER_PROPERTY_CHANGED,old,color);
        repaint();
    }
    /**
     * This returns whether the primary snake color has been set to a non-null 
     * value.
     * @return Whether {@code setPrimarySnakeColor} has been invoked with a 
     * non-null value.
     * @see #getPrimarySnakeColor 
     * @see #setPrimarySnakeColor 
     */
    public boolean isPrimarySnakeColorSet(){
        return primaryColor != null;
    }
    /**
     * This returns the primary color to use for drawing snakes. That is to say, 
     * this returns the color to use to draw {@link Tile#isSnake() snake tiles} 
     * that do not have their {@link Tile#getType() type flag} set. If the 
     * primary snake color has been set to a non-null value, then that is what 
     * is returned. Otherwise, if the {@link #getRenderer() play field 
     * renderer}'s {@link PlayFieldRenderer#getPrimarySnakeColor 
     * getPrimarySnakeColor} method returns a non-null value, then that will be 
     * returned. Otherwise, this will return {@link #PRIMARY_SNAKE_COLOR}.
     * @return The primary color to use for drawing snakes.
     * @see #setPrimarySnakeColor 
     * @see #isPrimarySnakeColorSet 
     * @see #getRenderer 
     * @see PlayFieldRenderer#getPrimarySnakeColor 
     * @see PlayFieldRenderer
     * @see #PRIMARY_SNAKE_COLOR
     * @see #setSecondarySnakeColor 
     * @see #getSecondarySnakeColor 
     * @see Tile#isSnake 
     * @see Tile#getType 
     */
    public Color getPrimarySnakeColor(){
            // Get the currently set primary snake color
        Color color = primaryColor;
            // If the color has not been set and the renderer is not null
        if (color == null && getRenderer() != null)
                // Get the primary snake color from the renderer
            color = getRenderer().getPrimarySnakeColor(this);
            // If the primary snake color is not null, return it. Otherwise, 
            // return the default color
        return (color!=null)?color:PRIMARY_SNAKE_COLOR; 
    }
    /**
     * This sets the primary color to use for drawing snakes. That is to say, 
     * this sets the color used to draw {@link Tile#isSnake() snake tiles} that 
     * do not have their {@link Tile#getType() type flag} set. Any subsequent 
     * call to {@code getPrimarySnakeColor} will return this value, and the 
     * {@link #getRenderer() play field renderer} will not be asked for the 
     * color to use. Setting the primary snake color to null will reset it back 
     * to the default behavior.
     * @param color The primary color to use for drawing snakes, or null.
     * @see #getPrimarySnakeColor 
     * @see #isPrimarySnakeColorSet 
     * @see #PRIMARY_SNAKE_COLOR
     * @see #getSecondarySnakeColor 
     * @see #setSecondarySnakeColor 
     * @see Tile#isSnake 
     * @see Tile#getType 
     */
    public void setPrimarySnakeColor(Color color){
            // If the new primary snake color is the same as the old primary 
        if (Objects.equals(primaryColor, color))    // snake color 
            return;
        Color old = primaryColor;       // Get the old primary snake color
        primaryColor = color;
        firePropertyChange(PRIMARY_SNAKE_COLOR_PROPERTY_CHANGED,old,color);
        repaint();
    }
    /**
     * This returns whether the secondary snake color has been set to a non-null 
     * value.
     * @return Whether {@code setSecondarySnakeColor} has been invoked with a 
     * non-null value.
     * @see #getSecondarySnakeColor 
     * @see #setSecondarySnakeColor 
     */
    public boolean isSecondarySnakeColorSet(){
        return secondaryColor != null;
    }
    /**
     * This returns the secondary color to use for drawing snakes. That is to 
     * say, this returns the color to use to draw {@link Tile#isSnake() snake 
     * tiles} that have their {@link Tile#getType() type flag} set. If the 
     * secondary snake color has been set to a non-null value, then that is what 
     * is returned. Otherwise, if the {@link #getRenderer() play field 
     * renderer}'s {@link PlayFieldRenderer#getSecondarySnakeColor 
     * getSecondarySnakeColor} method returns a non-null value, then that will 
     * be returned. Otherwise, this will return {@link #SECONDARY_SNAKE_COLOR}.
     * @return The secondary color to use for drawing snakes.
     * @see #setSecondarySnakeColor 
     * @see #isSecondarySnakeColorSet 
     * @see #getRenderer 
     * @see PlayFieldRenderer#getSecondarySnakeColor 
     * @see PlayFieldRenderer
     * @see #SECONDARY_SNAKE_COLOR
     * @see #getPrimarySnakeColor 
     * @see #setSecondarySnakeColor 
     * @see Tile#isSnake 
     * @see Tile#getType 
     */
    public Color getSecondarySnakeColor(){
            // Get the currently set secondary snake color
        Color color = secondaryColor;   
            // If the color has not been set and the renderer is not null
        if (color == null && getRenderer() != null)
                // Get the secondary snake color from the renderer
            color = getRenderer().getSecondarySnakeColor(this);
            // If the secondary snake color is not null, return it. Otherwise, 
            // return the default color
        return (color!=null)?color:SECONDARY_SNAKE_COLOR;   
    }
    /**
     * This sets the secondary color to use for drawing snakes. That is to say, 
     * this sets the color used to draw {@link Tile#isSnake() snake tiles} that 
     * have their {@link Tile#getType() type flag} set. Any subsequent call to 
     * {@code getSecondarySnakeColor} will return this value, and the {@link 
     * #getRenderer() play field renderer} will not be asked for the color to 
     * use. Setting the secondary snake color to null will reset it back to the 
     * default behavior.
     * @param color The secondary color to use for drawing snakes, or null.
     * @see #getSecondarySnakeColor 
     * @see #isSecondarySnakeColorSet 
     * @see #SECONDARY_SNAKE_COLOR
     * @see #getPrimarySnakeColor 
     * @see #setPrimarySnakeColor 
     * @see Tile#isSnake 
     * @see Tile#getType 
     */
    public void setSecondarySnakeColor(Color color){
            // If the new secondary snake color is the same as the old secondary 
        if (Objects.equals(secondaryColor, color))  // snake color 
            return;
        Color old = secondaryColor;     // Get the old secondary snake color
        secondaryColor = color;
        firePropertyChange(SECONDARY_SNAKE_COLOR_PROPERTY_CHANGED,old,color);
        repaint();
    }
    /**
     * This returns whether the apple color has been set to a non-null value.
     * @return Whether {@code setAppleColor} has been invoked with a non-null 
     * value.
     * @see #getAppleColor 
     * @see #setAppleColor 
     */
    public boolean isAppleColorSet(){
        return appleColor != null;
    }
    /**
     * This returns the color to use for drawing apples. That is to say, this 
     * returns the color to use to draw {@link Tile#isApple() apple tiles}. If 
     * the apple color has been set to a non-null value, then that is what is 
     * returned. Otherwise, if the {@link #getRenderer() play field renderer}'s 
     * {@link PlayFieldRenderer#getAppleColor getAppleColor} method returns a 
     * non-null value, then that will be returned. Otherwise, this will return 
     * {@link #APPLE_COLOR}.
     * @return The color to use for drawing apples.
     * @see #setAppleColor 
     * @see #isAppleColorSet 
     * @see #getRenderer 
     * @see PlayFieldRenderer#getAppleColor 
     * @see PlayFieldRenderer
     * @see #APPLE_COLOR
     * @see Tile#isApple 
     */
    public Color getAppleColor(){
        Color color = appleColor;   // Get the currently set apple color
            // If the color has not been set and the renderer is not null
        if (color == null && getRenderer() != null)
                // Get the apple color from the renderer
            color = getRenderer().getAppleColor(this);
            // If the apple color is not null, return it. Otherwise, return the 
        return (color!=null)?color:APPLE_COLOR; // default color
    }
    /**
     * This sets the color to use for drawing apples. That is to say, this sets 
     * the color used to draw {@link Tile#isApple() apple tiles}. Any subsequent 
     * call to {@code getAppleColor} will return this value, and the {@link 
     * #getRenderer() play field renderer} will not be asked for the color to 
     * use. Setting the apple color to null will reset it back to the default 
     * behavior.
     * @param color The color to use for drawing apples, or null.
     * @see #getAppleColor 
     * @see #isAppleColorSet 
     * @see APPLE_COLOR
     * @see Tile#isApple 
     */
    public void setAppleColor(Color color){
            // If the new apple color is the same as the old apple color 
        if (Objects.equals(appleColor, color))
            return;
        Color old = appleColor;     // Get the old apple color
        appleColor = color;
        firePropertyChange(APPLE_COLOR_PROPERTY_CHANGED,old,color);
        repaint();
    }
    
    
    /**
     * This returns a String representation of this JPlayField. This method is 
     * primarily intended to be used only for debugging purposes, and the 
     * content and format of the returned String may vary between 
     * implementations.
     * @return A String representation of this JPlayField.
     */
    @Override
    protected String paramString(){
            // This returns a string with the parameters for this play field
        return super.paramString() + 
                ",rows="+getRowCount()+
                ",columns="+getColumnCount()+
                ",tileCount="+getTileCount()+
                ",highQuality="+isHighQuality()+
                ",tileBackground="+Objects.toString(getTileBackground(),"")+
                ",paintTileBackground="+isTileBackgroundPainted()+
                ",tileBorder="+Objects.toString(getTileBorder(),"")+
                ",paintTileBorder="+isTileBorderPainted()+
                ",appleColor="+Objects.toString(getAppleColor(),"")+
                ",primarySnakeColor="+Objects.toString(getPrimarySnakeColor(),"")+
                ",secondarySnakeColor="+Objects.toString(getSecondarySnakeColor(),"");
    }
    /**
     * This adds the given {@code PlayFieldListener} to this component.
     * @param l The listener to add.
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     */
    public void addPlayFieldListener(PlayFieldListener l){
        if (l != null)  // If the listener is not null
            listenerList.add(PlayFieldListener.class, l);
    }
    /**
     * This removes the given {@code PlayFieldListener} from this component.
     * @param l The listener to remove.
     * @see #addPlayFieldListener 
     * @see #getPlayFieldListeners 
     */
    public void removePlayFieldListener(PlayFieldListener l){
        listenerList.remove(PlayFieldListener.class, l);
    }
    /**
     * This returns an array containing all the {@code PlayFieldListener}s that 
     * have been added to this component.
     * @return An array containing the {@code PlayFieldListener}s that have been 
     * added, or an empty array if none have been added.
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     */
    public PlayFieldListener[] getPlayFieldListeners(){
        return listenerList.getListeners(PlayFieldListener.class);
    }
    /**
     * This notifies the given {@code PlayFieldListener} of the given event. If 
     * the listener is null, then this does nothing. Otherwise, this checks the 
     * given event's {@link PlayFieldEvent#getType() type} and invokes the 
     * listener's corresponding method. This is used by {@link 
     * #firePlayFieldChange(PlayFieldEvent) firePlayFieldChange} to notify each 
     * listener of the event given to it.
     * @param evt The event to notify the listener of (cannot be null).
     * @param l The listener to notify.
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see PlayFieldEvent#getType 
     * @see PlayFieldEvent#TILES_CHANGED
     * @see PlayFieldEvent#TILES_ADDED
     * @see PlayFieldEvent#TILES_REMOVED
     * @see PlayFieldListener#tilesChanged 
     * @see PlayFieldListener#tilesAdded 
     * @see PlayFieldListener#tilesRemoved 
     */
    protected void notifyPlayFieldListener(PlayFieldEvent evt, PlayFieldListener l){
        if (l == null)          // If the listener is null
            return;
        switch(evt.getType()){  // Get the type of event
            case(PlayFieldEvent.TILES_CHANGED): // If tiles were changed
                l.tilesChanged(evt);
                return;
            case(PlayFieldEvent.TILES_ADDED):   // If tiles were added
                l.tilesAdded(evt);
                return;
            case(PlayFieldEvent.TILES_REMOVED): // If tiles were removed
                l.tilesRemoved(evt);
        }
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this component and the {@link #getRenderer renderer} for this component 
     * of the given {@code PlayFieldEvent} if the {@code PlayFieldEvent} is not 
     * null. If the source of the given {@code PlayFieldEvent} is not this 
     * component, then the {@code PlayFieldEvent} will be redirected to have 
     * this component as its source. The renderer will be the last to be 
     * notified of the event.
     * @param evt The {@code PlayFieldEvent} to be fired.
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see #notifyPlayFieldListener 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see #fireTilesChanged 
     * @see #fireTilesAdded 
     * @see #fireTilesRemoved 
     * @see #firePlayFieldStructureChanged 
     */
    protected void firePlayFieldChange(PlayFieldEvent evt){
        if (evt == null)                // If the event is null
            return;
            // If the source of the event is not this component
        if (evt.getSource() != this)    
            evt = new PlayFieldEvent(this,evt.getFirstRow(),evt.getLastRow(),
                    evt.getFirstColumn(),evt.getLastColumn(),
                    evt.getTilesAreAdjusting(),evt.getType());
            // A for loop to notify the play field listeners
        for (PlayFieldListener l : getPlayFieldListeners()){
            notifyPlayFieldListener(evt,l);
        }
        notifyPlayFieldListener(evt,getRenderer());
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this component and the {@link #getRenderer renderer} for this component 
     * that the tiles in the region between the rows {@code r0} and {@code r1}, 
     * inclusive, and between the columns {@code c0} and {@code c1}, inclusive, 
     * have changed in some way. Note that {@code r0} does not necessarily need 
     * to be less than or equal to {@code r1}, and that {@code c0} does not 
     * necessarily need to be less than or equal to {@code c1}. The renderer 
     * will be the last to be notified of the event.
     * @param r0 One end of the range of rows that have changed.
     * @param r1 The other end of the range of rows that have changed.
     * @param c0 One end of the range of columns that have changed.
     * @param c1 The other end of the range of columns that have changed.
     * @param type The type of event. This should be one of the following: 
     *      {@link PlayFieldEvent#TILES_ADDED TILES_ADDED}, 
     *      {@link PlayFieldEvent#TILES_CHANGED TILES_CHANGED}, or
     *      {@link PlayFieldEvent#TILES_REMOVED TILES_REMOVED}.
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see PlayFieldEvent
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #fireTilesChanged 
     * @see #fireTilesAdded 
     * @see #fireTilesRemoved 
     * @see #firePlayFieldStructureChanged 
     */
    protected void firePlayFieldChange(int r0, int r1, int c0, int c1, int type){
        firePlayFieldChange(new PlayFieldEvent(this,r0,r1,c0,c1,
                getTilesAreAdjusting(),type));
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this component and the {@link #getRenderer renderer} for this component 
     * that the tiles in the region between the rows {@code r0} and {@code r1}, 
     * inclusive, and between the columns {@code c0} and {@code c1}, inclusive, 
     * have been changed or updated. Note that {@code r0} does not necessarily 
     * need to be less than or equal to {@code r1}, and that {@code c0} does not 
     * necessarily need to be less than or equal to {@code c1}. The renderer 
     * will be the last to be notified of the event.
     * @param r0 One end of the range of rows that have been updated.
     * @param r1 The other end of the range of rows that have been updated.
     * @param c0 One end of the range of columns that have been updated.
     * @param c1 The other end of the range of columns that have been updated.
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see #fireTilesChanged(int, int) 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see PlayFieldEvent#TILES_CHANGED
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #fireTilesAdded 
     * @see #fireTilesRemoved 
     * @see #firePlayFieldStructureChanged 
     */
    protected void fireTilesChanged(int r0, int r1, int c0, int c1){
        firePlayFieldChange(r0,r1,c0,c1,PlayFieldEvent.TILES_CHANGED);
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this component and the {@link #getRenderer renderer} for this component 
     * that the tile at the given {@code row} and {@code column} has been 
     * changed or updated. The renderer will be the last to be notified of the 
     * event.
     * @param row The row of the tile that has been updated.
     * @param column The column of the tile that has been updated.
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see #fireTilesChanged 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see PlayFieldEvent#TILES_CHANGED
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #fireTilesAdded 
     * @see #fireTilesRemoved 
     * @see #firePlayFieldStructureChanged 
     */
    protected void fireTilesChanged(int row, int column){
        fireTilesChanged(row,row,column,column);
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this component and the {@link #getRenderer renderer} for this component 
     * that tiles have been added to the region between the rows {@code r0} and 
     * {@code r1}, inclusive, and between the columns {@code c0} and {@code c1}, 
     * inclusive. Note that {@code r0} does not necessarily need to be less than 
     * or equal to {@code r1}, and that {@code c0} does not necessarily need to 
     * be less than or equal to {@code c1}. The renderer will be the last to be 
     * notified of the event.
     * @param r0 One end of the range of rows that have added.
     * @param r1 The other end of the range of rows that have added.
     * @param c0 One end of the range of columns that have added.
     * @param c1 The other end of the range of columns that have added.
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see PlayFieldEvent#TILES_ADDED
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #setRowCount 
     * @see #setColumnCount 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #fireTilesChanged 
     * @see #fireTilesRemoved 
     * @see #firePlayFieldStructureChanged 
     */
    protected void fireTilesAdded(int r0, int r1, int c0, int c1){
        firePlayFieldChange(r0,r1,c0,c1,PlayFieldEvent.TILES_ADDED);
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this component and the {@link #getRenderer renderer} for this component 
     * that tiles in the region between the rows {@code r0} and {@code r1}, 
     * inclusive, and between the columns {@code c0} and {@code c1}, inclusive, 
     * have been removed. Note that {@code r0} does not necessarily need to be 
     * less than or equal to {@code r1}, and that {@code c0} does not 
     * necessarily need to be less than or equal to {@code c1}. The renderer 
     * will be the last to be notified of the event.
     * @param r0 One end of the range of rows that have removed.
     * @param r1 The other end of the range of rows that have removed.
     * @param c0 One end of the range of columns that have removed.
     * @param c1 The other end of the range of columns that have removed.
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see PlayFieldEvent#TILES_REMOVED
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #setRowCount 
     * @see #setColumnCount 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #fireTilesChanged 
     * @see #fireTilesAdded 
     * @see #firePlayFieldStructureChanged 
     */
    protected void fireTilesRemoved(int r0, int r1, int c0, int c1){
        firePlayFieldChange(r0,r1,c0,c1,PlayFieldEvent.TILES_REMOVED);
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this component and the {@link #getRenderer renderer} for this component 
     * that the structure of the play field has completely changed, either 
     * because the current {@link #getModel model} had its structure changed or 
     * because the model itself was {@link #setModel changed}. The renderer will 
     * be the last to be notified of the event.
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #getRenderer 
     * @see #setRenderer 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see PlayFieldEvent#PlayFieldEvent(Object, boolean) 
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #getModel 
     * @see #setModel 
     * @see #fireTilesChanged 
     * @see #fireTilesAdded 
     * @see #fireTilesRemoved 
     */
    protected void firePlayFieldStructureChanged(){
        firePlayFieldChange(new PlayFieldEvent(this,getTilesAreAdjusting()));
    }
    /**
     * This is a handler used to listen to the PlayFieldModel and forward the 
     * {@code PlayFieldEvent}s that it fires to the listeners and renderer.
     */
    private class Handler implements PlayFieldListener{
        /**
         * {@inheritDoc }
         */
        @Override
        public void tilesAdded(PlayFieldEvent evt) {
            firePlayFieldChange(evt);
        }
        /**
         * {@inheritDoc }
         */
        @Override
        public void tilesRemoved(PlayFieldEvent evt) {
            firePlayFieldChange(evt);
        }
        /**
         * {@inheritDoc }
         */
        @Override
        public void tilesChanged(PlayFieldEvent evt) {
            firePlayFieldChange(evt);
        }
    }
}
