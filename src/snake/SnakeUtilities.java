/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake;

import java.awt.*;
import java.awt.geom.*;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import snake.playfield.*;

/**
 * This is a collection of utility methods for the game Snake.
 * @author Milo Steier
 */
public class SnakeUtilities implements SnakeConstants{
    /**
     * This is the mask used when inverting the directions set on a value. This 
     * takes advantage of the fact that the {@link #DOWN_DIRECTION} and {@link 
     * #RIGHT_DIRECTION} direction flags are effectively the {@link 
     * #UP_DIRECTION} and {@link #LEFT_DIRECTION} direction flags bit shifted to 
     * the left, respectively. As such, to invert the directions set on a value, 
     * a mask can be used to get the up and left direction flags so that they 
     * can be bit shifted to the left to become the down and right direction 
     * flags, respectively. To get the up and left direction flags from the down 
     * and right direction flags, the value can be bit shifted to the right 
     * before the mask is applied. These two values can then be {@code OR}'d 
     * together to produce a value with the directions inverted.
     * @see #UP_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #invertDirections
     */
    private static final int INVERT_DIRECTIONS_MASK = UP_DIRECTION | LEFT_DIRECTION;
    /**
     * This is the amount by which a given value is offset before dividing it 
     * when deriving the thickness for the tile border.
     * @see #TILE_BORDER_THICKNESS_DIVISOR
     * @see #computeTileBorderThickness
     */
    protected static final int TILE_BORDER_THICKNESS_OFFSET = 16;
    /**
     * This is the amount by which to divide a given value when deriving the 
     * thickness for the tile border.
     * @see #TILE_BORDER_THICKNESS_OFFSET
     * @see #computeTileBorderThickness
     */
    protected static final int TILE_BORDER_THICKNESS_DIVISOR = 32;
    /**
     * This is a predicate used to filter out all but empty tiles.
     */
    private static Predicate<Tile> emptyFilter = null;
    /**
     * This is a predicate used to filter out all but apple tiles.
     */
    private static Predicate<Tile> appleFilter = null;
    /**
     * This class cannot be constructed.
     */
    private SnakeUtilities(){}
    /**
     * This returns whether the given flag has been set on the given value. 
     * @param flags The value to check whether the flag is set for.
     * @param flag The flag to check for.
     * @return Whether the given flag is set.
     * @see #setFlag
     * @see #toggleFlag
     */
    public static boolean getFlag(int flags, int flag){
        return (flags & flag) == flag;
    }
    /**
     * This sets whether the given flag is set based off the given {@code 
     * value}.
     * @param flags The value to set the flag on.
     * @param flag The flag to be set or cleared based off {@code value}.
     * @param value Whether the flag should be set or cleared.
     * @return The value with the given flag either set or cleared.
     * @see #getFlag
     * @see #toggleFlag
     */
    public static int setFlag(int flags, int flag, boolean value){
            // If the flag is to be set, OR the flags with the flag. Otherwise, 
            // AND the flags with the inverse of the flag.
        return (value) ? flags | flag : flags & ~flag;
    }
    /**
     * This toggles whether the given flag is set.
     * @param flags The value to toggle the flag on.
     * @param flag The flag to be toggled.
     * @return The value with the given flag toggled.
     * @see #getFlag
     * @see #setFlag
     */
    public static int toggleFlag(int flags, int flag){
        return flags ^ flag;
    }
    /**
     * This returns a String stating the binary representation of the given 
     * value. This is equivalent to calling {@link Integer#toBinaryString 
     * Integer.toBinaryString}, only that the returned String will be at least 
     * {@code bits} characters long with leading zeros added if necessary.
     * @param value The value to get the binary String of.
     * @param bits The number of bits to display (cannot be negative).
     * @return A String stating the binary representation of the given value.
     * @throws IllegalArgumentException If the number of bits to display is 
     * negative.
     * @see Integer#toBinaryString
     */
    public static String getFormattedBinaryString(int value, int bits){
        if (bits < 0)       // If the number of bits is negative
            throw new IllegalArgumentException(
                    "Cannot display a negative number of bits (bits: "+bits+")");
        else if (bits == 0) // If the number of bits is zero
            return Integer.toBinaryString(value);
        return String.format("%"+bits+"s",Integer.toBinaryString(value))
                .replace(' ','0');
    }
    /**
     * This returns the direction flags that are set on the given value. 
     * @param value The value to get the direction flags from.
     * @return The direction flags that are set on the given value.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #VERTICAL_DIRECTIONS
     * @see #HORIZONTAL_DIRECTIONS
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     * @see #getNonDirectionFlags
     */
    public static int getDirections(int value){
        return value & ALL_DIRECTIONS;
    }
    /**
     * This returns the non-direction flags that are set on the given value. 
     * This is effectively the inverse of {@link #getDirections getDirections}, 
     * and as such will return the portion of the given value that is removed by 
     * {@code getDirections}.
     * @param value The value to get the non-direction flags from.
     * @return The given value with the direction flags removed from it.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #VERTICAL_DIRECTIONS
     * @see #HORIZONTAL_DIRECTIONS
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     * @see #getDirections
     */
    public static int getNonDirectionFlags(int value){
        return value & ~ALL_DIRECTIONS;
    }
    /**
     * This returns the number of directions that are set for the given value.
     * @param value The value to get the amount of directions for.
     * @return The number of directions currently set for the value.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #VERTICAL_DIRECTIONS
     * @see #HORIZONTAL_DIRECTIONS
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     * @see #getDirections
     */
    public static int getDirectionCount(int value){
        return Integer.bitCount(getDirections(value));
    }
    /**
     * This checks the given value to see how many directions are set, and if 
     * there is only one direction set, then this returns that flag. Otherwise, 
     * an IllegalArgumentException will be thrown.
     * @param value The value to check.
     * @return The direction from the given value.
     * @throws IllegalArgumentException If either no directions are set or there 
     * are more than 1 directions set on the value.
     * @see #getDirections 
     * @see #getDirectionCount 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     */
    public static int requireSingleDirection(int value){
            // Gets the amount of direction flags set
        int count = getDirectionCount(value);
        if (count == 1)     // If there is exactly one direction set
            return getDirections(value);
            // Throw an IllegalArgumentException stating the issue
        throw new IllegalArgumentException(String.format(
                "%s directions set on %d (Expected: 1, Actual: %d)", 
                    // If there are no directions set, say that there are no 
                    // directions set. Otherwise, say there are too many 
                (count == 0)?"No":"Too many",value,count)); // directions set
    }
    /**
     * This inverts which directions are set for the given value. In other 
     * words, {@link #UP_DIRECTION up} becomes {@link DOWN_DIRECTION down}, 
     * {@link LEFT_DIRECTION left} becomes {@link RIGHT_DIRECTION right}, and 
     * vice versa. All other flags are left as they are with no changes made to 
     * them.
     * @param value The value to get the directions to invert from.
     * @return A value with the opposite directions set.
     * @see #getFlag 
     * @see #getDirections 
     * @see #getNonDirectionFlags 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #VERTICAL_DIRECTIONS
     * @see #HORIZONTAL_DIRECTIONS
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     */
    public static int invertDirections(int value){
            // Store the non-direction flags so that they can be added back later
        int temp = getNonDirectionFlags(value);
        value = getDirections(value);   // Remove all but the direction flags
        // Mask and bit shift the flags into their new locations
        return ((value & INVERT_DIRECTIONS_MASK) << 1) | 
                ((value >> 1) & INVERT_DIRECTIONS_MASK) | temp;
    }
    /**
     * This returns a String stating the directions set on the given value. This 
     * is primarily intended for producing Strings for debugging purposes.
     * @param direction The value to get the directions for.
     * @return A String stating the directions.
     * @see #getFlag 
     * @see #getDirections 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #getDirectionBinary 
     */
    public static String getDirectionString(int direction){
        return "up="+getFlag(direction,UP_DIRECTION)+
                ",down="+getFlag(direction,DOWN_DIRECTION)+
                ",left="+getFlag(direction,LEFT_DIRECTION)+
                ",right="+getFlag(direction,RIGHT_DIRECTION);
    }
    /**
     * This returns a String stating the binary representation for the 
     * directions set on the given value. This is primarily intended for 
     * producing Strings for debugging purposes.
     * @param direction The value to get the directions for.
     * @return A String stating the binary representation for the directions.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #getDirections 
     * @see #getDirectionString 
     * @see #getFormattedBinaryString 
     * @see Integer#toBinaryString 
     */
    public static String getDirectionBinary(int direction){
        return getFormattedBinaryString(getDirections(direction),
                Integer.bitCount(ALL_DIRECTIONS));
    }
    /**
     * This returns a predicate that matches non-null, {@link Tile#isEmpty() 
     * empty} tiles. In other words, the {@link Predicate#test test} method of 
     * the returned predicate will return {@code true} when given a non-null 
     * tile that is empty ({@link Tile#isEmpty() isEmpty} returns {@code true}).
     * @return A predicate that matches non-null, empty tiles.
     * @see Tile#isEmpty 
     * @see Tile#clear 
     * @see #getAppleTilePredicate 
     */
    public static Predicate<Tile> getEmptyTilePredicate(){
            // If the empty tile predicate has not been initialized yet
        if (emptyFilter == null)    
            emptyFilter = (Tile t) -> t != null && t.isEmpty();
        return emptyFilter;
    }
    /**
     * This returns a predicate that matches non-null, {@link Tile#isApple() 
     * apple} tiles. In other words, the {@link Predicate#test test} method of 
     * the returned predicate will return {@code true} when given a non-null 
     * tile that is an apple tile ({@link Tile#isApple() isApple} returns {@code 
     * true}).
     * @return A predicate that matches non-null, apple tiles.
     * @see Tile#isApple 
     * @see Tile#setApple 
     * @see Tile#setAppleIfEmpty 
     * @see #getEmptyTilePredicate 
     */
    public static Predicate<Tile> getAppleTilePredicate(){
            // If the apple tile predicate has not been initialized yet
        if (appleFilter == null)    
            appleFilter = (Tile t) -> t != null && t.isApple();
        return appleFilter;
    }
    /**
     * This computes the line width to use for the border around the tiles based 
     * off the given value.
     * @param value The value to use to get the border thickness from.
     * @return The line width for the border.
     * @see #calculateTileSize
     */
    public static float computeTileBorderThickness(double value){
        return (float)(Math.floor((value+TILE_BORDER_THICKNESS_OFFSET)/
                TILE_BORDER_THICKNESS_DIVISOR)+1)*2;
    }
    /**
     * This computes the offset for the contents of the given tile based off the 
     * given value. <p>
     * 
     * If the tile is {@link Tile#isEmpty() empty} (or null), then this returns 
     * half of the given value. This is so that, assuming that the given value 
     * is the size to draw the tile, nothing will be drawn as the contents will 
     * have a size of zero. Otherwise, the value will be divided by either 4 or 
     * 6, depending on whether the given tile is an {@link Tile#isApple() apple 
     * tile} or not. This should result in apple tiles being approximately 75% 
     * the size of other non-empty tiles. The resulting offset is then checked 
     * to see if it would cause the contents of the tile to be less than 1 pixel 
     * in size. If this is the case, then the offset will be equal to {@code 
     * (value-1)} divided by 2. Afterwards, if the offset is negative, then this 
     * will return zero.
     * 
     * @param tile The tile to get the offset for the contents. If this is null, 
     * then this is treated as an empty tile.
     * @param value The value to use to get the offset for the tile contents 
     * from. This is often the width or height of a tile.
     * @return The offset for the contents of the tile.
     * @see Tile#isEmpty 
     * @see Tile#isApple 
     * @see Tile#isSnake 
     * @see #calculateTileSize
     * @see #computeTileContentsSize 
     * @see #computeTileBorderThickness 
     */
    public static double computeTileContentsOffset(Tile tile, double value){
        double off = value;     // This will get the offset
            // If the tile is null or empty
        if (tile == null || tile.isEmpty())
            off /= 2;   
        else if (tile.isApple())    // If the tile is an apple tile
            off /= 4;
        else
            off /= 6;
            // If the tile is not null or empty and the offset will cause the 
            // contents of the tile to be less than 1 pixel in size
        if (tile != null && !tile.isEmpty() && value - (off * 2) < 1)
            off = (value-1)/2;
        return Math.max(off, 0);
    }
    /**
     * This computes the size for the contents of the given tile based off the 
     * given value. This is based off the {@link #computeTileContentsOffset 
     * offset} for the contents, with this multiplying the offset by 2 and 
     * subtracting it from the given value. In other words, this effectively 
     * returns the result of the calculation: {@code value - } {@link 
     * #computeTileContentsOffset computeTileContentsOffset}{@code (tile, 
     * value) * 2}.
     * 
     * @param tile The tile to get the offset for the contents. If this is null, 
     * then this is treated as an empty tile.
     * @param value The value to use to get the offset for the tile contents 
     * from. This is often the width or height of a tile.
     * @return The size of the contents of the tile.
     * @see Tile#isEmpty 
     * @see Tile#isApple 
     * @see Tile#isSnake 
     * @see #calculateTileSize
     * @see #computeTileContentsOffset 
     */
    public static double computeTileContentsSize(Tile tile, double value){
        return Math.max(value - (computeTileContentsOffset(tile,value)*2), 0);
    }
    /**
     * This calculates the size to use for the tiles in the given PlayFieldModel 
     * based off the given width and height, stores the results in {@code dim}, 
     * and returns {@code dim}. If {@code dim} is null, then a new Dimension2D 
     * object will be created and returned. <p>
     * 
     * The size is calculated by dividing the given width and height by the 
     * PlayFieldModel's {@link PlayFieldModel#getColumnCount column count} 
     * and {@link PlayFieldModel#getRowCount row count}, respectively, to get 
     * the width and height for the tiles. If either of these results in a 
     * negative value, then it will be substituted with a zero.
     * 
     * @param model The model in question. If this is null, then this returns 
     * null.
     * @param w The width of the area to display the tiles in.
     * @param h The height of the area to display the tiles in.
     * @param dim The Dimension2D object to store the results in, may be null.
     * @return A Dimension2D object with the size for the tiles, or null if the 
     * model is null.
     * @see PlayFieldModel#getColumnCount 
     * @see PlayFieldModel#getRowCount 
     * @see #calculatePlayFieldBounds(PlayFieldModel, double, double, double, 
     * double, Rectangle2D) 
     * @see #calculatePlayFieldBounds(PlayFieldModel, Rectangle2D, Rectangle2D) 
     */
    public static Dimension2D calculateTileSize(PlayFieldModel model, double w, 
            double h, Dimension2D dim){
        if (model == null)  // If the model is null
            return null;
        if (dim == null)    // If the dimension object is null
            dim = new DoubleDimension2D();
        dim.setSize(Math.max(w/model.getColumnCount(), 0), 
                Math.max(h/model.getRowCount(), 0));
        return dim;
    }
    /**
     * This calculates the position and size of the painted play field region 
     * for the given PlayFieldModel based off the given size and location, 
     * stores the results in {@code rect}, and returns {@code rect}. If {@code 
     * rect} is null, then a new Rectangle2D object will be created and 
     * returned. <p>
     * 
     * To calculate the size for the play field, this first calculates the size 
     * for the tiles. This is done by dividing the given width and height by the 
     * PlayFieldModel's {@link PlayFieldModel#getColumnCount column count} and 
     * {@link PlayFieldModel#getRowCount row count}, respectively. The smaller 
     * of the two is then used as both the width and height of the tiles, so as 
     * to make them squares. The width and height of the tiles is then 
     * multiplied by the column count and row count, respectively, to get the 
     * width and height of the play field. If the play field width and height 
     * are negative, then they will be set to zero. The x and y coordinates will 
     * then be calculated in a way that the play field will be in the center of 
     * the given bounds.
     * 
     * @param model The model in question. If this is null, then this returns 
     * null.
     * @param x The x-coordinate for the area to paint the play field in.
     * @param y The y-coordinate for the area to paint the play field in.
     * @param w The width of the area to paint the play field in.
     * @param h The height of the area to paint the play field in.
     * @param rect The Rectangle2D object to store the results in, may be null.
     * @return A Rectangle2D object with the bounds for the painted paint field, 
     * or null if the model is null.
     * @see #calculateTileSize
     * @see PlayFieldModel#getRowCount
     * @see PlayFieldModel#getColumnCount
     * @see #calculatePlayFieldBounds(PlayFieldModel, Rectangle2D, Rectangle2D) 
     */
    public static Rectangle2D calculatePlayFieldBounds(PlayFieldModel model, 
            double x, double y, double w, double h, Rectangle2D rect){
        if (model == null)      // If the model is null
            return null;
        if (rect == null)       // If the rectangle object is null
            rect = new Rectangle2D.Double();
            // Calculate the width and height for the tiles
        double tileS = Math.max(Math.min(w/model.getColumnCount(), 
                h/model.getRowCount()), 0);
            // This gets the width for the field
        double fieldW = Math.max(tileS * model.getColumnCount(), 0);
            // This gets the height for the field
        double fieldH = Math.max(tileS * model.getRowCount(), 0);
        rect.setFrame(x+((w-fieldW)/2), y+((h-fieldH)/2), fieldW, fieldH);
        return rect;
    }
    /**
     * This calculates the position and size of the painted play field region 
     * for the given PlayFieldModel based off the given bounds, stores the 
     * results in {@code rect}, and returns {@code rect}. If {@code rect} is 
     * null, then a new Rectangle2D object will be created and returned. <p>
     * 
     * To calculate the size for the play field, this first calculates the size 
     * for the tiles. This is done by dividing the width and height of {@code 
     * viewR} by the PlayFieldModel's {@link PlayFieldModel#getColumnCount 
     * column count} and {@link PlayFieldModel#getRowCount row count}, 
     * respectively. The smaller of the two is then used as both the width and 
     * height of the tiles, so as to make them squares. The width and height of 
     * the tiles is then multiplied by the column count and row count, 
     * respectively, to get the width and height of the play field. If the play 
     * field width and height are negative, then they will be set to zero. The x 
     * and y coordinates will then be calculated in a way that the play field 
     * will be in the center of the given bounds.
     * 
     * @param model The model in question. If this is null, then this returns 
     * null.
     * @param viewR The area in which to paint the play field in. If this is 
     * null, then this returns null.
     * @param rect The Rectangle2D object to store the results in, may be null.
     * @return A Rectangle2D object with the bounds for the painted paint field, 
     * or null if the model is null.
     * @see #calculateTileSize
     * @see PlayFieldModel#getRowCount
     * @see PlayFieldModel#getColumnCount
     * @see #calculatePlayFieldBounds(PlayFieldModel, double, double, double, 
     * double, Rectangle2D) 
     */
    public static Rectangle2D calculatePlayFieldBounds(PlayFieldModel model, 
            Rectangle2D viewR, Rectangle2D rect){
            // If the view rectangle is not null, calculate the play field 
            // bounds. Otherwise, return null.
        return (viewR!=null)?calculatePlayFieldBounds(model,viewR.getX(),
                viewR.getY(),viewR.getWidth(),viewR.getHeight(),rect):null;
    }
    /**
     * This calculates the size for the given JPlayField based off its model and 
     * the given multiplier, stores the results in {@code dim}, and returns 
     * {@code dim}. If {@code dim} is null, then a new Dimension object will be 
     * created and returned. <p>
     * 
     * The width is calculated by multiplying the {@link 
     * JPlayField#getColumnCount() column count} by the {@code multiplier}, 
     * which is then added to the left and right insets. The height is 
     * calculated by multiplying the {@link JPlayField#getRowCount() row count} 
     * by the {@code multiplier}, which is then added to the top and bottom 
     * insets.
     * 
     * @param c The JPlayField to get the size for. If this is null, then this 
     * returns null.
     * @param multiplier The multiplier to use to multiply the row and column 
     * count to get the width and height to get the size.
     * @param dim The Dimension object to store the results in, may be null.
     * @return A Dimension object with the size for the given JPlayField, or 
     * null if the JPlayField or its model is null. 
     * @see JPlayField#getModel 
     * @see JPlayField#getRowCount 
     * @see JPlayField#getColumnCount 
     * @see JPlayField#getInsets 
     */
    public static Dimension computePlayFieldSize(JPlayField c, int multiplier,
            Dimension dim){
            // If the play field or its model are null
        if (c == null || c.getModel() == null)
            return null;
        if (dim == null)    // If the dimension object is null
            dim = new Dimension();
            // Get the dimensions with the columns, rows, and multiplier
        dim.setSize(c.getColumnCount()*multiplier,c.getRowCount()*multiplier);
        Insets insets = c.getInsets();  // Get the insets from the play field
        if (insets != null){            // If the insets are not null
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;
        }
        return dim;
    }
    /**
     * This goes through the given collection of snakes and invokes each of 
     * their {@link Snake#doNextAction() doNextAction} methods. If any of the 
     * snakes throws an exception while performing their next action, the 
     * exception will be relayed to the caller and the remaining snakes will not 
     * perform their next action.
     * @param snakes The collection of snakes that are to perform their next 
     * actions (cannot be null).
     * @throws NullPointerException If the given collection is null.
     * @see Snake#doNextAction 
     */
    public static void snakesDoNextAction(Collection<? extends Snake> snakes){
        Objects.requireNonNull(snakes); // Check to see if the collection is null
        for (Snake snake : snakes){     // A for loop to go through the snakes
            if (snake != null)          // If the current snake is non-null
                snake.doNextAction();   // Have the snake perform its next action
        }
    }
    /**
     * This returns a polygon that can be used to render a triangle pointing in 
     * the given direction. The direction must be either one of the four 
     * direction flags ({@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link 
     * #LEFT_DIRECTION}, or {@link #RIGHT_DIRECTION}) or any combination of a 
     * single horizontal flag and a single vertical flag ({@link 
     * #UP_LEFT_DIRECTION}, {@link #UP_RIGHT_DIRECTION}, {@link 
     * #DOWN_LEFT_DIRECTION}, or {@link #DOWN_RIGHT_DIRECTION}).
     * @param x The x-coordinate of the top-left corner of the bounding 
     * rectangle for the triangle.
     * @param y The y-coordinate of the top-left corner of the bounding 
     * rectangle for the triangle.
     * @param w The width of the triangle.
     * @param h The height of the triangle.
     * @param direction The direction the triangle should be pointing. This 
     * should be one of the following: 
     *      {@link #UP_DIRECTION}, 
     *      {@link #DOWN_DIRECTION}, 
     *      {@link #LEFT_DIRECTION}, 
     *      {@link #RIGHT_DIRECTION}, 
     *      {@link #UP_LEFT_DIRECTION}, 
     *      {@link #UP_RIGHT_DIRECTION}, 
     *      {@link #DOWN_LEFT_DIRECTION}, or
     *      {@link #DOWN_RIGHT_DIRECTION}.
     * @return A polygon representing a triangle pointing in the given 
     * direction.
     * @throws IllegalArgumentException If the direction is invalid.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     */
    public static Polygon getTriangle(int x,int y,int w,int h,int direction){
            // This gets the triangle to return
        Polygon triangle = new Polygon(new int[3],new int[3],3);
        switch(direction){  // Determine the direction for the triangle
            case(UP_DIRECTION):     // If pointing up
            case(DOWN_DIRECTION):   // If pointing down
                    // Both up and down have the same x points
                triangle.xpoints[0] = 0;    // Start at the left-most point
                triangle.xpoints[1] = w;    // Go to the right-most point
                    // End at the point in the middle
                triangle.xpoints[2] = Math.floorDiv(w, 2);
                break;
            case(LEFT_DIRECTION):   // If pointing left
            case(RIGHT_DIRECTION):  // If pointing right
                    // Both left and right have the same y points
                triangle.ypoints[0] = 0;    // Start at the top point
                triangle.ypoints[1] = h;    // Go to the bottom point
                    // End at the point in the middle
                triangle.ypoints[2] = Math.floorDiv(h, 2);
                break;
            case(UP_LEFT_DIRECTION):    // If pointing up and left
            case(UP_RIGHT_DIRECTION):   // If pointing up and right
            case(DOWN_LEFT_DIRECTION):  // If pointing down and left
                    // All three start at the top point. Down and right 
                    // starts at a different y point
                triangle.ypoints[0] = 0;
            case(DOWN_RIGHT_DIRECTION): // If pointing down and right
                    // All four corners have the following in common
                triangle.xpoints[0] = triangle.ypoints[2] = 0;
                triangle.xpoints[2] = w;
                triangle.ypoints[1] = h;
                break;
            default:    // Invalid direction
                throw new IllegalArgumentException(
                        "Cannot get triangle for direction " + direction);
        }   // Determine the direction for the triangle again to fill in the 
        switch(direction){  // more specific points
            case(UP_DIRECTION):     // If pointing up
                    // Start at the bottom and end at the top
                triangle.ypoints[0] = triangle.ypoints[1] = h;
                triangle.ypoints[2] = 0;
                break;
            case(DOWN_DIRECTION):   // If pointing down
                    // Start at the top and end at the bottom
                triangle.ypoints[0] = triangle.ypoints[1] = 0;
                triangle.ypoints[2] = h;
                break;
            case(LEFT_DIRECTION):   // If pointing left
                    // Start at the right and end at the left
                triangle.xpoints[0] = triangle.xpoints[1] = w;
                triangle.xpoints[2] = 0;
                break;
            case(RIGHT_DIRECTION):  // If pointing right
                    // Start at the left and end at the right
                triangle.xpoints[0] = triangle.xpoints[1] = 0;
                triangle.xpoints[2] = w;
                break;
            case(DOWN_LEFT_DIRECTION):  // If pointing down and left
                    // Down and left ends at the bottom point
                triangle.ypoints[2] = h;
            case(UP_LEFT_DIRECTION):    // If pointing up and left
                    // Both left corners have the middle x be the to the left
                triangle.xpoints[1] = 0;
                break;
            case(DOWN_RIGHT_DIRECTION): // If pointing down and right
                    // Down and right starts at the bottom point
                triangle.ypoints[0] = h;
            case(UP_RIGHT_DIRECTION):   // If pointing up and right
                    // Both right corners have the middle x be the to the right
                triangle.xpoints[1] = w;
        }   // Invalidate the polygon since we messed with the points
        triangle.invalidate();  
            // Translate the polygon to put it where it should be
        triangle.translate(x, y);   
        return triangle;
    }
    /**
     * This draws a 3-D outline of the specified rectangle. Lines are drawn 
     * within the rectangle in a way so that it appears to be the outline of a 
     * rectangular prism viewed from the lower right corner. <p>
     * 
     * The resulting rectangle covers an area that is {@code (w + 1)} pixels 
     * wide by {@code (h + 1)} pixels tall. If {@code bevel} is positive, then 
     * the lines will be drawn in a way to make the rectangle appear to be 
     * raised above the surface. If {@code bevel} is negative, then the lines 
     * will make the rectangle appear to be sunk into the surface. The absolute 
     * value for {@code bevel} determines the distance between the edges of the 
     * rectangle and the perceived raised/lowered rectangle. In other words, the 
     * perceived raised/lowered rectangle will cover an area that is {@code (w + 
     * 1 - |bevel|)} pixels wide by {@code (h + 1 - |bevel|)} pixels tall. If 
     * {@code bevel} is positive, then the top-left corner of the perceived 
     * rectangle will be at point {@code (x, y)}. Otherwise, the top-left corner 
     * of the perceived rectangle will be at point {@code (x + |bevel|, y + 
     * |bevel|)}. If the absolute value of {@code bevel} exceeds either the 
     * given width or height, then the smaller of the two will be used instead 
     * of the absolute value of {@code bevel}. If {@code bevel} is zero, then 
     * this will just draw a regular rectangle. <p>
     * 
     * This method uses the current {@code Color} exclusively, and ignores the 
     * current {@code Paint} set on the graphics context if it is an instance of 
     * {@code Graphics2D}.
     * 
     * @param g The graphics context to render to (cannot be null).
     * @param x The x-coordinate of the rectangle to be drawn.
     * @param y The y-coordinate of the rectangle to be drawn.
     * @param w The width of the rectangle to be drawn.
     * @param h The height of the rectangle to be drawn.
     * @param bevel The amount by which to shrink and offset the perceived 
     * raised/lowered rectangle. If this is positive, then the rectangle will 
     * appear to be raised above the surface. If this is negative, then the 
     * rectangle will appear to be sunk into the surface.
     * @throws NullPointerException If {@code g} is null.
     * @see #fill3DRectangle
     * @see #drawBeveledRectangle 
     * @see #fillBeveledRectangle 
     * @see Graphics#draw3DRect 
     * @see Graphics#fill3DRect 
     */
    public static void draw3DRectangle(Graphics g,int x,int y,int w,int h,
            int bevel){
        boolean raised = bevel > 0;     // If the rectangle should be raised
            // Get the absolute value for the bevel and bound it to the size of 
        bevel = Math.min(Math.abs(bevel),Math.min(w, h));   // the rectangle
            // This temporarily stores the paint that is currently set so that 
        Paint p = null;     // it can be restored later
            // If the graphics context is a Graphics2D object
        if (g instanceof Graphics2D){
                // Get the currently set paint
            p = ((Graphics2D)g).getPaint();
                // Set the paint to the currently set color
            ((Graphics2D)g).setPaint(g.getColor());
        }
        else if (g == null) // If the graphics context is null
            throw new NullPointerException();
            // If the bevel is less than or equal to zero after being bounded 
            // (either the bevel is zero or the width or height are less than or 
        if (bevel <= 0){    // equal to zero)
            g.drawRect(x, y, w, h);
                // If the graphics context is a Graphics2D object
            if (g instanceof Graphics2D)
                ((Graphics2D) g).setPaint(p);   // Restore the paint
            return;
        }
        int bW = w - bevel; // This gets the width of the inner rectangle
        int bH = h - bevel; // This gets the height of the inner rectangle
            // If the graphics context is a Graphics2D object
        if (g instanceof Graphics2D){
                // Create a path to render the outline of the 3D rectangle, 
                // starting with the outer rectangle
            Path2D path = new Path2D.Double(new Rectangle(x,y,w,h));
                // Create a rectangle to draw the inner rectangle
            Rectangle inner = new Rectangle(x, y, bW, bH);
            if (raised){    // If the rectangle is to be raised
                    // Draw a line connecting the two bottom-right points
                path.moveTo(x+w, y+h);
                path.lineTo(inner.getMaxX(), inner.getMaxY());
            }
            else{   // Move the inner rectangle down and right
                inner.translate(bevel, bevel);
                    // Draw a line connecting the two top-left points
                path.moveTo(x, y);
                path.lineTo(inner.getMinX(), inner.getMinY());
            }
            path.append(inner, false);      // Add the inner rectangle
            ((Graphics2D)g).draw(path);     // Draw the outline
            ((Graphics2D)g).setPaint(p);    // Restore the paint
        }
        else{
            g.drawRect(x, y, w, h);
            if (raised){    // If the rectangle is to be raised
                g.drawRect(x, y, bW, bH);
                g.drawLine(x+bW, y+bH, x+w, y+h);
            }
            else{
                g.drawRect(x+bevel, y+bevel, bW, bH);
                g.drawLine(x, y, x+bevel, y+bevel);
            }
        }
    }
    /**
     * This renders a 3-D rectangle filled with the current color. The edges of 
     * the rectangle are highlighted in a way that it appears as a rectangular 
     * prism lit from the upper left corner and viewed from the lower right 
     * corner. <p>
     * 
     * The colors used for the highlighting effect and for filling the rectangle 
     * are based off the currently set color. The resulting rectangle covers an 
     * area that is {@code w} pixels wide by {@code h} pixels tall. If {@code 
     * bevel} is positive, then the rectangle will be highlighted in a way that 
     * it will appear as if it was raised above the surface. If {@code bevel} is 
     * negative, then the rectangle will be highlighted in a way that it will 
     * appear as if it was sunk into the surface. The absolute value for {@code 
     * bevel} determines the distance between the edges of the rectangle and the 
     * perceived raised/lowered rectangle. In other words, the perceived 
     * raised/lowered rectangle will cover an area that is {@code (w - |bevel|)} 
     * pixels wide by {@code (h - |bevel|)} pixels tall. If {@code bevel} is 
     * positive, then the top-left corner of the perceived rectangle will be at 
     * point {@code (x, y)}. Otherwise, the top-left corner of the perceived 
     * rectangle will be at point {@code (x + |bevel|, y + |bevel|)}. If the 
     * absolute value of {@code bevel} exceeds either the given width or height, 
     * then the smaller of the two will be used instead of the absolute value of 
     * {@code bevel}. If {@code bevel} is zero, then this will just fill a 
     * regular rectangle. <p>
     * 
     * This method uses the current {@code Color} exclusively, and ignores the 
     * current {@code Paint} set on the graphics context if it is an instance of 
     * {@code Graphics2D}.
     * 
     * @param g The graphics context to render to (cannot be null).
     * @param x The x-coordinate of the rectangle to be filled.
     * @param y The y-coordinate of the rectangle to be filled.
     * @param w The width of the rectangle to be filled.
     * @param h The height of the rectangle to be filled.
     * @param bevel The amount by which to shrink and offset the perceived 
     * raised/lowered area. If this is positive, then the rectangle will appear 
     * to be raised above the surface. If this is negative, then the rectangle 
     * will appear to be sunk into the surface.
     * @throws NullPointerException If {@code g} is null.
     * @see #draw3DRectangle 
     * @see #drawBeveledRectangle 
     * @see #fillBeveledRectangle 
     * @see Graphics#draw3DRect 
     * @see Graphics#fill3DRect 
     */
    public static void fill3DRectangle(Graphics g,int x,int y,int w,int h,
            int bevel){
        Objects.requireNonNull(g);  // Require a non-null graphics context
        Color c = g.getColor();     // Get the currently set color
            // This temporarily stores the paint that is currently set so that 
        Paint p = null;     // it can be restored later
            // If the graphics context is a Graphics2D object
        if (g instanceof Graphics2D){
                // Get the currently set paint
            p = ((Graphics2D)g).getPaint();
                // Set the paint to the currently set color
            ((Graphics2D)g).setPaint(g.getColor());
        }
        g.fillRect(x, y, w, h);     // Fill in the rectangle
        boolean raised = bevel > 0; // If the rectangle should be raised
            // Get the absolute value for the bevel and bound it to the size of 
        bevel = Math.min(Math.abs(bevel),Math.min(w, h));   // the rectangle
            // If the bevel is less than or equal to zero after being bounded 
            // (either the bevel is zero or the width or height are less than or 
        if (bevel <= 0){    // equal to zero)
                // If the graphics context is a Graphics2D object
            if (g instanceof Graphics2D)
                ((Graphics2D) g).setPaint(p);   // Restore the paint
            return;
        }
        g.setColor(c.darker());     // Make the color darker
            // This is an array of y coordinates to be used for the trapezoid 
            // on either the left or right of the rectangle, depending on 
            // whether it's raised or lowered. The second point is either at the 
            // top or offset by the bevel, depending on whether the rectangle is 
        int[] yPoints = new int[]{y,y+((raised)?0:bevel),y+h,h-bevel};// raised.
            // If the rectangle is to be raised, offset the y by the last y 
            // coord in the array (since it's currently set to the offset for 
            // the last coord). Otherwise, draw this rectangle at the top.
        g.fillRect(x, y+((raised)?yPoints[3]:0), w, bevel);
        g.setColor(g.getColor().darker());  // Make the color even darker
            // Set the last point to the second point offset by the last point.
            // (I.e. If this is raised, the last point is y+height-bevel, and if 
        yPoints[3] += yPoints[1];   // not, the last point is y+h)
            // Create the array to store the x coordinates for the trapesoid
        int[] xPoints = new int[yPoints.length];
            // The first and last points will be the right-most point of the 
            // inner rectangle if raised, and will be the left-most point of the 
            // outer rectangle if not. 
        xPoints[0] = xPoints[3] = x+((raised)?w-bevel:0);
        xPoints[1] = xPoints[2] = xPoints[0]+bevel;
        g.fillPolygon(xPoints, yPoints, yPoints.length);
        g.setColor(c);  // Restore the color
             // If the graphics context is a Graphics2D object
        if (g instanceof Graphics2D)
            ((Graphics2D) g).setPaint(p);   // Restore the paint
    }
    /**
     * This draws a 3-D beveled outline of the specified rectangle. A smaller 
     * rectangle is drawn in the center and lines are drawn to join the corners 
     * of the outer rectangle to the corners of the inner rectangle. <p>
     * 
     * The color used is based off the current color. The resulting rectangle 
     * covers an area that is {@code (w + 1)} pixels wide by {@code (h + 1)} 
     * pixels tall. The absolute value of {@code bevel} determines the distance 
     * between the edges of the outer rectangle and the inner rectangle. In 
     * other words, the top-left corner of the inner rectangle will be located 
     * at point {@code (x + |bevel|, y + |bevel|)}, and the inner rectangle will 
     * cover an area that is {@code (w + 1 - 2|bevel|)} pixels wide by {@code 
     * (h + 1 - 2|bevel|)} pixels tall. If the absolute value of {@code bevel} 
     * is greater than half the given width or half the given height (whichever 
     * is smaller), then the smaller halved value will be used instead of the 
     * absolute value of {@code bevel}. If {@code bevel} is zero, then this will 
     * just draw a regular rectangle. <p>
     * 
     * This method uses the current {@code Color} exclusively, and ignores the 
     * current {@code Paint} set on the graphics context if it is an instance of 
     * {@code Graphics2D}.
     * 
     * @param g The graphics context to render to (cannot be null).
     * @param x The x-coordinate of the rectangle to be drawn.
     * @param y The y-coordinate of the rectangle to be drawn.
     * @param w The width of the rectangle to be drawn.
     * @param h The height of the rectangle to be drawn.
     * @param bevel The amount by which to shrink and offset the inner 
     * rectangle. This can be positive or negative, as it is the absolute value 
     * of this that is used.
     * @throws NullPointerException If {@code g} is null.
     * @see #fillBeveledRectangle 
     * @see #draw3DRectangle 
     * @see #fill3DRectangle 
     */
    public static void drawBeveledRectangle(Graphics g,int x,int y,int w,int h,
            int bevel){
        int max = Math.min(w, h);   // Get the smaller of the width and height
        bevel = Math.abs(bevel);    // Get the absolute value for the bevel
            // Create a Rectangle object for the outer rectangle
        Rectangle rect = new Rectangle(x,y,w,h);
            // If the graphics context is a Graphics2D object
        if (g instanceof Graphics2D){
                // This temporarily stores the paint that is currently set so 
            Paint p = ((Graphics2D)g).getPaint();//that it can be restored later
                // Set the paint to the currently set color
            ((Graphics2D)g).setPaint(g.getColor());
                // This gets the floating-point number to use as the bevel. This 
                // is the given bevel bounded to half of the smaller of the 
            double value = Math.min(bevel, max/2.0);    // width and height
                // If the bevel is less than or equal to zero after being 
                // bounded (either the bevel is zero or the width or height are 
            if (value <= 0){    // less than or equal to zero when halved)
                g.drawRect(x, y, w, h);
                ((Graphics2D)g).setPaint(p);    // Restore the paint
                return;
            }   // Create a rectangle to represent the inner centered rectangle
            Rectangle2D inner = new Rectangle2D.Double();
                // Set the inner rectangle from the center of the outer 
                // rectangle, and with its x and y to be the outer rectangle 
                // plus the bevel
            inner.setFrameFromCenter(rect.getCenterX(), rect.getCenterY(), 
                    rect.x+value, rect.y+value);
                // Create a path to render the outline
            Path2D path = new Path2D.Double(rect);
                // Add the inner rectangle to the outline
            path.append(inner, false);
                // A for loop to add the lines connecting the inner and outer 
            for (int i = 0; i < 4; i++){    // rectangles
                    // Whether this line will use the rectangles' min x or max x
                boolean minX = i % 2 == 0;
                    // Whether this line will use the rectangles' min y or max y
                boolean minY = i >> 1 == 0;
                    // Move to the outer rectangle's point (Use min x when i is 
                    // even, and max x when i is odd. Use min y for the first 
                    // two lines, and max y for the last two lines)
                path.moveTo((minX)?rect.getMinX():rect.getMaxX(),
                        (minY)?rect.getMinY():rect.getMaxY());
                    // Draw a line to the corresponding point for the inner 
                    // rectangle (Use min x when i is even, and max x when i is 
                    // odd. Use min y for the first two lines, and max y for the 
                    // last two lines)
                path.lineTo((minX)?inner.getMinX():inner.getMaxX(),
                        (minY)?inner.getMinY():inner.getMaxY());
            }
            ((Graphics2D)g).draw(path);     // Draw the outline
            ((Graphics2D)g).setPaint(p);    // Restore the paint
        }
        else if (g == null)     // If the graphics context is null
            throw new NullPointerException();
        else{   // Bound the bevel by half of the smaller width and height, plus 
                // its parity
            bevel = Math.min(bevel, Math.floorDiv(max, 2) + (max % 2));
            g.drawRect(x, y, w, h);
                // If the bevel is less than or equal to zero after being 
                // bounded (either the bevel is zero or the width or height are 
            if (bevel <= 0)    // less than or equal to zero when halved)
                return;
                // Shrink the rectangle by the bevel to get the inner rectangle
            rect.grow(-1*bevel, -1*bevel);
                // Bound the rectangle's width by 0 to prevent a negative width
            rect.width = Math.max(rect.width, 0);
                // Bound the rectangle's height by 0 to prevent a negative 
            rect.height = Math.max(rect.height, 0); // height
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
                // A for loop to add the lines connecting the inner and outer 
            for (int i = 0; i < 4; i++){    // rectangles
                    // The x of the rectangles are offset by their respective 
                    // widths when i is odd, and the y of the rectangles are 
                    // offset by their respective heights for the last two lines
                g.drawLine(x+(w*(i%2)),y+(h*(i>>1)),
                        rect.x+(rect.width*(i%2)),
                        rect.y+(rect.height*(i>>1)));
            }
        }
    }
    /**
     * This renders a 3-D beveled rectangle filled with the current color. The 
     * edges of the rectangle are highlighted in a way that it appears as if it 
     * were beveled and lit from the upper left corner. <p>
     * 
     * The colors used for the highlighting effect and for filling the rectangle 
     * are based off the currently set color. The resulting rectangle covers an 
     * area that is {@code w} pixels wide by {@code h} pixels tall. If {@code 
     * bevel} is positive, then the rectangle will be highlighted in a way that 
     * it will appear as if it was raised above the surface. If {@code bevel} is 
     * negative, then the rectangle will be highlighted in a way that it will 
     * appear as if it was sunk into the surface. The absolute value for {@code 
     * bevel} determines the distance between the edges of the outer rectangle 
     * and a smaller inner rectangle. In other words, the top-left corner of the 
     * inner rectangle will be located at point {@code (x + |bevel|, y + 
     * |bevel|)}, and the inner rectangle will cover an area that is {@code (w + 
     * 1 - 2|bevel|)} pixels wide by {@code (h + 1 - 2|bevel|)} pixels tall. If 
     * the absolute value of {@code bevel} is greater than half the given width 
     * or half the given height (whichever is smaller), then the smaller halved 
     * value will be used instead of the absolute value of {@code bevel}. If 
     * {@code bevel} is zero, then this will just fill a regular rectangle. <p>
     * 
     * This method uses the current {@code Color} exclusively, and ignores the 
     * current {@code Paint} set on the graphics context if it is an instance of 
     * {@code Graphics2D}.
     * 
     * @param g The graphics context to render to (cannot be null).
     * @param x The x-coordinate of the rectangle to be filled.
     * @param y The y-coordinate of the rectangle to be filled.
     * @param w The width of the rectangle to be filled.
     * @param h The height of the rectangle to be filled.
     * @param bevel The amount by which to shrink and offset the inner 
     * rectangle. If this is positive, then the inner rectangle will appear to 
     * be raised above the surface. If this is negative, then the inner 
     * rectangle will appear to be sunk into the surface.
     * @throws NullPointerException If {@code g} is null.
     * @see #drawBeveledRectangle 
     * @see #draw3DRectangle 
     * @see #fill3DRectangle 
     */
    public static void fillBeveledRectangle(Graphics g,int x,int y,int w,int h,
            int bevel){
        Objects.requireNonNull(g);  // Require a non-null graphics context
        boolean raised = bevel > 0; // If the rectangle should be raised
        bevel = Math.abs(bevel);    // Get the absolute value for the bevel
        int max = Math.min(w, h);   // Get the smaller of the width and height
        Color c = g.getColor();     // Get the currently set color
            // This is an array of colors to use to create the illusion of 
            // depth. The first color is the bottom color, second is the top, 
            // third is the right, and fourth is the left.
        Color[] colors = new Color[4];
            // This gets the darker color to use. If the rectangle is raised, 
            // then this will be the first (bottom) color. Otherwise, this will 
            // be the second (top) color.
        colors[(raised)?0:1] = c.darker();
            // This gets the brighter color to use. If the rectangle is raised, 
            // then this will be the second (top) color. Otherwise, this will be 
            // the first (bottom) color.
        colors[(raised)?1:0] = c.brighter();
            // This gets the even darker color to use. If the rectangle is 
            // raised, then this will be the third (right) color derived from 
            // the first color. Otherwise, this will be the fourth (left) color 
            // derived from the second color.
        colors[(raised)?2:3] = colors[(raised)?0:1].darker();
            // This gets the even brighter color to use. If the rectangle is 
            // raised, then this will be the fourth (left) color derived from 
            // the second color. Otherwise, this will be the third (right) color 
            // derived from the first color.
        colors[(raised)?3:2] = colors[(raised)?1:0].brighter();
            // If the graphics context is a Graphics2D object
        if (g instanceof Graphics2D){
                // Get the graphics context as a Graphics2D object
            Graphics2D g2D = (Graphics2D) g;    
                // This temporarily stores the paint that is currently set so 
            Paint p = g2D.getPaint();   // that it can be restored later
            g2D.setPaint(c);        // Set the paint to the currently set color
            double half = max/2.0;  // Get half of the smaller of the dimensions
                // This gets the floating-point number to use as the bevel. This 
                // is the given bevel bounded to half of the smaller of the 
            double value = Math.min(bevel, half);   // dimensions)
                // If the bevel is less than or equal to zero after being 
                // bounded (either the bevel is zero or the width or height are 
            if (value <= 0){    // less than or equal to zero when halved)
                g2D.fillRect(x, y, w, h);
                g2D.setPaint(p);    // Restore the paint
                return;
            }   // Create a Rectangle object for the outer rectangle
            Rectangle rect = new Rectangle(x,y,w,h);
                // Create a worker Rectangle2D object to use to render some of 
            Rectangle2D temp = new Rectangle2D.Double();    // the rectangles
                // Set the worker rectangle's frame to cover the top half of 
                // the outer rectangle
            temp.setFrameFromDiagonal(rect.getMinX(), rect.getMinY(), 
                    rect.getMaxX(), rect.getCenterY());
            g2D.setColor(colors[0]);    // Set the color to the bottom color
            g2D.fill(rect);             // Fill the entire rectangle
            g2D.setColor(colors[1]);    // Set the color to the top color
            g2D.fill(temp);             // Fill the top half of the rectangle
                // Create a path object to render the side trapezoids
            Path2D path = new Path2D.Double();
                // Create the right trapezoid, starting at the top-right corner
            path.moveTo(rect.getMaxX(), rect.getMinY());
                // Get half of the height
            double halfH = rect.getHeight() / 2.0;
                // Get the width for the right trapezoid. If the rectangle's 
                // width is large enough to fit half the height, then this will 
                // become a triangle. Otherwise, this will be a trapezoid 
                // covering the entire width of the rectangle
            double triW = Math.min(halfH, rect.getWidth());
                // Get the difference between half of the height and the width 
                // of the side trapezoid/triangle (If it's a triangle, this will 
            double diff = halfH - triW;     // be zero)
                // Add a line to the top-left point of the trapezoid
            path.lineTo(rect.getMaxX()-triW, rect.getCenterY()-diff);
                // Add a line to the bottom-left point of the trapezoid. (If 
                // triangle, then this will be the same as the previous point).
            path.lineTo(rect.getMaxX()-triW, rect.getCenterY()+diff);
                // End at the bottom-right corner of the rectangle
            path.lineTo(rect.getMaxX(), rect.getMaxY());
            g2D.setColor(colors[2]);    // Set the color to be the right color
            g2D.fill(path);             // Fill the right trapezoid
            path.reset();               // Reset the path for the left shape
                // Start at the top-left corner
            path.moveTo(rect.getMinX(), rect.getMinY());
                // Add a line to the top-right point of the triangle.
                // (This is a triangle if half == halfH)
            path.lineTo(rect.getMinX()+half, rect.getMinY()+half);
                // Add a line to the bottom-right point of the trapezoid. (If 
                // triangle, then this will be the same as the previous point).
            path.lineTo(rect.getMinX()+half, rect.getMaxY()-half);
                // End at the bottom-left corner of the rectangle
            path.lineTo(rect.getMinX(), rect.getMaxY());
            g2D.setColor(colors[3]);    // Set the color to be the left color
            g2D.fill(path);             // Fill the left trapezoid
                // Set the worker rectangle's frame to the inner rectangle
            temp.setFrameFromCenter(rect.getCenterX(), rect.getCenterY(), 
                    rect.x+value, rect.y+value);
            g2D.setColor(c);            // Set the color to the original color
            g2D.fill(temp);             // Fill the inner rectangle
            g2D.setPaint(p);            // Restore the paint
        }
        else{   // Get half of the smaller width and height plus its parity
            int half = Math.floorDiv(max, 2) + (max % 2);
                // Bound the bevel by the calculated half
            bevel = Math.max(Math.min(bevel, half), 0);
            if (bevel > 0){     // If the bounded bevel is greater than zero
                g.setColor(colors[0]);  // Set the color to the bottom color
                g.fillRect(x, y, w, h); // Fill the entire rectangle
                g.setColor(colors[1]);  // Set the color to the top color
                    // Fill the top half of the rectangle
                g.fillRect(x, y, w, Math.floorDiv(h, 2) + (h % 2));
                    // Create the y points array for the side trapezoids
                int[] yPoints = new int[]{y,y+half,y+Math.max(h-half,half),y+h};
                    // Create the x points array for the side trapezoids
                int[] xPoints = new int[yPoints.length];
                    // Set the x points for the right trapezoid
                xPoints[0] = xPoints[3] = x+w;
                xPoints[1] = xPoints[2] = x+Math.max(w-half,half);
                g.setColor(colors[2]);  // Set the color to be the right color
                    // Fill the right trapezoid
                g.fillPolygon(xPoints, yPoints, yPoints.length);
                    // Set the x points for the left trapezoid
                xPoints[0] = xPoints[3] = x;
                xPoints[1] = xPoints[2] = x+half;
                g.setColor(colors[3]);    // Set the color to be the left color
                    // Fill the left trapezoid
                g.fillPolygon(xPoints, yPoints, yPoints.length);
            }
            g.setColor(c);              // Set the color to the original color
                // Fill the inner rectangle
            g.fillRect(x+bevel, y+bevel, Math.max(w-bevel-bevel,0),
                    Math.max(h-bevel-bevel,0));
        }
    }
    /**
     * This renders the given String using the given graphics context and in the 
     * center of the specified rectangle. This assumes that there are no 
     * conflicting rendering attributes have been applied to the graphics 
     * context, such as a {@code Transform}.
     * @param g The graphics context to render to (cannot be null).
     * @param str The String to be rendered (cannot be null)
     * @param x The x-coordinate of the rectangle to render the String in the 
     * center of.
     * @param y The y-coordinate of the rectangle to render the String in the 
     * center of.
     * @param w The width of the rectangle to render the String in the center 
     * of.
     * @param h The height of the rectangle to render the String in the center 
     * of.
     * @throws NullPointerException If either {@code g} or {@code str} are null.
     * @see Graphics#drawString(String, int, int) 
     * @see Graphics2D#drawString(String, int, int) 
     * @see Graphics2D#drawString(String, float, float) 
     * @see Graphics#getFontMetrics() 
     * @see Graphics#setFont 
     */
    public static void drawCenteredString(Graphics g, String str, int x, int y, 
            int w, int h){
        Objects.requireNonNull(g);      // Require a non-null graphics context
        Objects.requireNonNull(str);    // Require a non-null string
            // Get the font metrics for the set font
        FontMetrics metrics = g.getFontMetrics();
            // Get the x-coord for the text.
        float textX = x+((w-metrics.stringWidth(str))/(float)2.0);
            // Get the y-coord for the baseline for the text
        float textY = y+((h+metrics.getAscent()-metrics.getDescent())/(float)2.0);
            // If the graphics context is a Graphics2D object
        if (g instanceof Graphics2D)
            ((Graphics2D)g).drawString(str, textX, textY);
        else//Round the coordinates since we don't have floating-point precision
            g.drawString(str, Math.round(textX), Math.round(textY));
    }
}
