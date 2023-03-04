/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package snake;

import java.awt.Color;

/**
 * This is a collection of constants generally used for the game Snake.
 * @author Milo Steier
 */
public interface SnakeConstants {
    /**
     * This is the flag for the up direction. This can be combined with the 
     * other direction flags to indicate multiple directions.
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #VERTICAL_DIRECTIONS
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     */
    public static final int UP_DIRECTION = 0x01;
    /**
     * This is the flag for the down direction. This can be combined with the 
     * other direction flags to indicate multiple directions.
     * @see #UP_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #VERTICAL_DIRECTIONS
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     */
    public static final int DOWN_DIRECTION = 0x02;
    /**
     * This is the flag for the left direction. This can be combined with the 
     * other direction flags to indicate multiple directions.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #HORIZONTAL_DIRECTIONS
     * @see #UP_LEFT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #ALL_DIRECTIONS
     */
    public static final int LEFT_DIRECTION = 0x04;
    /**
     * This is the flag for the right direction. This can be combined with the 
     * other direction flags to indicate multiple directions.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #HORIZONTAL_DIRECTIONS
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     */
    public static final int RIGHT_DIRECTION = 0x08;
    /**
     * This is the flags for the vertical directions. In other words, this is a 
     * value with both {@link #UP_DIRECTION} and {@link #DOWN_DIRECTION} set.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #HORIZONTAL_DIRECTIONS
     * @see #ALL_DIRECTIONS
     */
    public static final int VERTICAL_DIRECTIONS = UP_DIRECTION | DOWN_DIRECTION;
    /**
     * This is the flags for the horizontal directions. In other words, this is 
     * a value with both {@link #LEFT_DIRECTION} and {@link #RIGHT_DIRECTION} 
     * set.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #VERTICAL_DIRECTIONS
     * @see #ALL_DIRECTIONS
     */
    public static final int HORIZONTAL_DIRECTIONS = LEFT_DIRECTION | RIGHT_DIRECTION;
    /**
     * This is a value with all the directions set.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     * @see #VERTICAL_DIRECTIONS
     * @see #HORIZONTAL_DIRECTIONS
     */
    public static final int ALL_DIRECTIONS = VERTICAL_DIRECTIONS | HORIZONTAL_DIRECTIONS;
    /**
     * This is the upper left direction. In other words, this is a value with 
     * both the {@link #UP_DIRECTION} and {@link #LEFT_DIRECTION} set.
     * @see #UP_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     */
    public static final int UP_LEFT_DIRECTION = UP_DIRECTION | LEFT_DIRECTION;
    /**
     * This is the upper right direction. In other words, this is a value with 
     * both the {@link #UP_DIRECTION} and {@link #RIGHT_DIRECTION} set.
     * @see #UP_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     */
    public static final int UP_RIGHT_DIRECTION = UP_DIRECTION | RIGHT_DIRECTION;
    /**
     * This is the lower left direction. In other words, this is a value with 
     * both the {@link #DOWN_DIRECTION} and {@link #LEFT_DIRECTION} set.
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     */
    public static final int DOWN_LEFT_DIRECTION = DOWN_DIRECTION | LEFT_DIRECTION;
    /**
     * This is the lower right direction. In other words, this is a value with 
     * both the {@link #DOWN_DIRECTION} and {@link #RIGHT_DIRECTION} set.
     * @see #DOWN_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #UP_LEFT_DIRECTION
     * @see #UP_RIGHT_DIRECTION
     * @see #DOWN_LEFT_DIRECTION
     * @see #ALL_DIRECTIONS
     */
    public static final int DOWN_RIGHT_DIRECTION = DOWN_DIRECTION | RIGHT_DIRECTION;
    /**
     * This is a flag used to set an alternate type or mode. This is primarily 
     * used by {@link snake.playfield.Tile tiles} to indicate that {@link 
     * snake.playfield.Tile#isSnake() snake tiles} should be drawn in a 
     * different way. Tiles also use this to indicate whether a tile should be 
     * an {@link snake.playfield.Tile#isApple() apple tile} or an {@link 
     * snake.playfield.Tile#isEmpty() empty tile} when a tile is not a snake 
     * tile, with apple tiles being an alternate version of empty tiles. This 
     * flag is also used by {@link Snake snakes} to determine what {@link 
     * snake.playfield.Tile#getType() type} of snake tiles should they be 
     * comprised of.
     */
    public static final int ALTERNATE_TYPE_FLAG = 0x10;
    /**
     * This is the color that is used as the default background color behind the 
     * tiles. This is the same as the color {@link Color#BLACK black}.
     * @see Color#BLACK
     */
    public static final Color TILE_BACKGROUND_COLOR = Color.BLACK;
    /**
     * This is the color that is used as the default color for {@link 
     * snake.playfield.Tile#isApple() apple tiles}. This is the same as the 
     * color {@link Color#RED red}.
     * @see Color#RED
     */
    public static final Color APPLE_COLOR = Color.RED;
    /**
     * This is the color that is used as the default color for {@link 
     * snake.playfield.Tile#isSnake() snake tiles} that do not have their {@link 
     * snake.playfield.Tile#getType() type flag} set. This is the same as the 
     * color {@link Color#GREEN green}.
     * @see Color#GREEN
     */
    public static final Color PRIMARY_SNAKE_COLOR = Color.GREEN;
    /**
     * This is the color that is used as the default color for {@link 
     * snake.playfield.Tile#isSnake() snake tiles} that have their {@link 
     * snake.playfield.Tile#getType() type flag} set. This is the same as the 
     * color {@link Color#BLUE blue}.
     * @see Color#BLUE
     */
    public static final Color SECONDARY_SNAKE_COLOR = Color.BLUE;
    /**
     * This is the color that is typically used to draw the border around the 
     * tiles. This color is a translucent white.
     */
    public static final Color TILE_BORDER_COLOR = new Color(0x60FFFFFF,true);
    
}
