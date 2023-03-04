/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake;

import java.awt.geom.*;
import java.util.function.Predicate;
import snake.playfield.PlayFieldModel;
import snake.playfield.Tile;

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
     * This returns whether the given flag has been set. 
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
     * @param flag The flag to set or clear.
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
     * @param flag The flag to toggle.
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
     * @param bits The amount of bits to display.
     * @return A String stating the binary representation of the given value.
     * @see Integer#toBinaryString
     */
    public static String getStateBinaryString(int value, int bits){
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
        if (count < 1)      // If there are no direction flags set
            throw new IllegalArgumentException("No direction set");
        else if (count > 1) // If there are more than 1 direction flags set
            throw new IllegalArgumentException("Too many directions set "
                    + "(Expected: 1, Actual: "+count+")");
        else
            return getDirections(value);
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
     * @see #getDirections(int) 
     * @see #getDirectionString 
     * @see #getStateBinaryString 
     * @see Integer#toBinaryString 
     */
    public static String getDirectionBinary(int direction){
        return getStateBinaryString(getDirections(direction),
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
     * object is created and returned. <p>
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
     * @see #calculatePlayFieldBounds(snake.playfield.PlayFieldModel, double, 
     * double, double, double, java.awt.geom.Rectangle2D) 
     * @see #calculatePlayFieldBounds(snake.playfield.PlayFieldModel, 
     * java.awt.geom.Rectangle2D, java.awt.geom.Rectangle2D) 
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
     * rect} is null, then a new Rectangle2D object is created and returned. <p>
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
     * @see #calculatePlayFieldBounds(snake.playfield.PlayFieldModel, 
     * java.awt.geom.Rectangle2D, java.awt.geom.Rectangle2D) 
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
     * null, then a new Rectangle2D object is created and returned. <p>
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
     * @see #calculatePlayFieldBounds(snake.playfield.PlayFieldModel, double, 
     * double, double, double, java.awt.geom.Rectangle2D) 
     */
    public static Rectangle2D calculatePlayFieldBounds(PlayFieldModel model, 
            Rectangle2D viewR, Rectangle2D rect){
            // If the view rectangle is not null, calculate the play field 
            // bounds. Otherwise, return null.
        return (viewR!=null)?calculatePlayFieldBounds(model,viewR.getX(),
                viewR.getY(),viewR.getWidth(),viewR.getHeight(),rect):null;
    }
    
    
}
