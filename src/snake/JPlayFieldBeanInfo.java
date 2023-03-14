/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Beans/BeanInfo.java to edit this template
 */
package snake;

import java.beans.*;

/**
 * This is the beans info for JPlayField.
 * @author Milo Steier
 * @see JPlayField
 */
public class JPlayFieldBeanInfo extends SimpleBeanInfo{

    // Bean descriptor//GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( snake.JPlayField.class , null ); // NOI18N
        beanDescriptor.setShortDescription ( "This is a panel used to display the play field for the game Snake." );//GEN-HEADEREND:BeanDescriptor
        // Here you can add code for customizing the BeanDescriptor.

        return beanDescriptor;     }//GEN-LAST:BeanDescriptor


    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_accessibleContext = 0;
    private static final int PROPERTY_actionMap = 1;
    private static final int PROPERTY_alignmentX = 2;
    private static final int PROPERTY_alignmentY = 3;
    private static final int PROPERTY_ancestorListeners = 4;
    private static final int PROPERTY_appleColor = 5;
    private static final int PROPERTY_appleColorSet = 6;
    private static final int PROPERTY_appleTileCount = 7;
    private static final int PROPERTY_appleTiles = 8;
    private static final int PROPERTY_autoscrolls = 9;
    private static final int PROPERTY_background = 10;
    private static final int PROPERTY_backgroundSet = 11;
    private static final int PROPERTY_baselineResizeBehavior = 12;
    private static final int PROPERTY_border = 13;
    private static final int PROPERTY_bounds = 14;
    private static final int PROPERTY_colorModel = 15;
    private static final int PROPERTY_columnCount = 16;
    private static final int PROPERTY_component = 17;
    private static final int PROPERTY_componentCount = 18;
    private static final int PROPERTY_componentListeners = 19;
    private static final int PROPERTY_componentOrientation = 20;
    private static final int PROPERTY_componentPopupMenu = 21;
    private static final int PROPERTY_components = 22;
    private static final int PROPERTY_containerListeners = 23;
    private static final int PROPERTY_cursor = 24;
    private static final int PROPERTY_cursorSet = 25;
    private static final int PROPERTY_debugGraphicsOptions = 26;
    private static final int PROPERTY_displayable = 27;
    private static final int PROPERTY_doubleBuffered = 28;
    private static final int PROPERTY_dropTarget = 29;
    private static final int PROPERTY_emptyTileCount = 30;
    private static final int PROPERTY_emptyTiles = 31;
    private static final int PROPERTY_enabled = 32;
    private static final int PROPERTY_focusable = 33;
    private static final int PROPERTY_focusCycleRoot = 34;
    private static final int PROPERTY_focusCycleRootAncestor = 35;
    private static final int PROPERTY_focusListeners = 36;
    private static final int PROPERTY_focusOwner = 37;
    private static final int PROPERTY_focusTraversable = 38;
    private static final int PROPERTY_focusTraversalKeys = 39;
    private static final int PROPERTY_focusTraversalKeysEnabled = 40;
    private static final int PROPERTY_focusTraversalPolicy = 41;
    private static final int PROPERTY_focusTraversalPolicyProvider = 42;
    private static final int PROPERTY_focusTraversalPolicySet = 43;
    private static final int PROPERTY_font = 44;
    private static final int PROPERTY_fontSet = 45;
    private static final int PROPERTY_foreground = 46;
    private static final int PROPERTY_foregroundSet = 47;
    private static final int PROPERTY_graphics = 48;
    private static final int PROPERTY_graphicsConfiguration = 49;
    private static final int PROPERTY_height = 50;
    private static final int PROPERTY_hierarchyBoundsListeners = 51;
    private static final int PROPERTY_hierarchyListeners = 52;
    private static final int PROPERTY_highQuality = 53;
    private static final int PROPERTY_ignoreRepaint = 54;
    private static final int PROPERTY_inheritsPopupMenu = 55;
    private static final int PROPERTY_inputContext = 56;
    private static final int PROPERTY_inputMap = 57;
    private static final int PROPERTY_inputMethodListeners = 58;
    private static final int PROPERTY_inputMethodRequests = 59;
    private static final int PROPERTY_inputVerifier = 60;
    private static final int PROPERTY_insets = 61;
    private static final int PROPERTY_keyListeners = 62;
    private static final int PROPERTY_layout = 63;
    private static final int PROPERTY_lightweight = 64;
    private static final int PROPERTY_locale = 65;
    private static final int PROPERTY_location = 66;
    private static final int PROPERTY_locationOnScreen = 67;
    private static final int PROPERTY_managingFocus = 68;
    private static final int PROPERTY_maximumSize = 69;
    private static final int PROPERTY_maximumSizeSet = 70;
    private static final int PROPERTY_minimumSize = 71;
    private static final int PROPERTY_minimumSizeSet = 72;
    private static final int PROPERTY_mixingCutoutShape = 73;
    private static final int PROPERTY_model = 74;
    private static final int PROPERTY_mouseListeners = 75;
    private static final int PROPERTY_mouseMotionListeners = 76;
    private static final int PROPERTY_mousePosition = 77;
    private static final int PROPERTY_mouseWheelListeners = 78;
    private static final int PROPERTY_name = 79;
    private static final int PROPERTY_nextFocusableComponent = 80;
    private static final int PROPERTY_opaque = 81;
    private static final int PROPERTY_optimizedDrawingEnabled = 82;
    private static final int PROPERTY_paintingForPrint = 83;
    private static final int PROPERTY_paintingTile = 84;
    private static final int PROPERTY_parent = 85;
    private static final int PROPERTY_playFieldBounds = 86;
    private static final int PROPERTY_playFieldListeners = 87;
    private static final int PROPERTY_preferredSize = 88;
    private static final int PROPERTY_preferredSizeSet = 89;
    private static final int PROPERTY_primarySnakeColor = 90;
    private static final int PROPERTY_primarySnakeColorSet = 91;
    private static final int PROPERTY_propertyChangeListeners = 92;
    private static final int PROPERTY_registeredKeyStrokes = 93;
    private static final int PROPERTY_renderer = 94;
    private static final int PROPERTY_rendererSet = 95;
    private static final int PROPERTY_requestFocusEnabled = 96;
    private static final int PROPERTY_rootPane = 97;
    private static final int PROPERTY_rowCount = 98;
    private static final int PROPERTY_secondarySnakeColor = 99;
    private static final int PROPERTY_secondarySnakeColorSet = 100;
    private static final int PROPERTY_showing = 101;
    private static final int PROPERTY_size = 102;
    private static final int PROPERTY_tileBackground = 103;
    private static final int PROPERTY_tileBackgroundPainted = 104;
    private static final int PROPERTY_tileBackgroundSet = 105;
    private static final int PROPERTY_tileBorder = 106;
    private static final int PROPERTY_tileBorderPainted = 107;
    private static final int PROPERTY_tileBorderSet = 108;
    private static final int PROPERTY_tileCount = 109;
    private static final int PROPERTY_tilesAreAdjusting = 110;
    private static final int PROPERTY_tileSize = 111;
    private static final int PROPERTY_toolkit = 112;
    private static final int PROPERTY_toolTipText = 113;
    private static final int PROPERTY_topLevelAncestor = 114;
    private static final int PROPERTY_transferHandler = 115;
    private static final int PROPERTY_treeLock = 116;
    private static final int PROPERTY_UI = 117;
    private static final int PROPERTY_UIClassID = 118;
    private static final int PROPERTY_valid = 119;
    private static final int PROPERTY_validateRoot = 120;
    private static final int PROPERTY_verifyInputWhenFocusTarget = 121;
    private static final int PROPERTY_vetoableChangeListeners = 122;
    private static final int PROPERTY_visible = 123;
    private static final int PROPERTY_visibleRect = 124;
    private static final int PROPERTY_width = 125;
    private static final int PROPERTY_x = 126;
    private static final int PROPERTY_y = 127;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[128];
    
        try {
            properties[PROPERTY_accessibleContext] = new PropertyDescriptor ( "accessibleContext", snake.JPlayField.class, "getAccessibleContext", null ); // NOI18N
            properties[PROPERTY_accessibleContext].setHidden ( true );
            properties[PROPERTY_actionMap] = new PropertyDescriptor ( "actionMap", snake.JPlayField.class, "getActionMap", "setActionMap" ); // NOI18N
            properties[PROPERTY_actionMap].setHidden ( true );
            properties[PROPERTY_alignmentX] = new PropertyDescriptor ( "alignmentX", snake.JPlayField.class, "getAlignmentX", "setAlignmentX" ); // NOI18N
            properties[PROPERTY_alignmentX].setShortDescription ( "The preferred horizontal alignment of the component." );
            properties[PROPERTY_alignmentY] = new PropertyDescriptor ( "alignmentY", snake.JPlayField.class, "getAlignmentY", "setAlignmentY" ); // NOI18N
            properties[PROPERTY_alignmentY].setShortDescription ( "The preferred vertical alignment of the component." );
            properties[PROPERTY_ancestorListeners] = new PropertyDescriptor ( "ancestorListeners", snake.JPlayField.class, "getAncestorListeners", null ); // NOI18N
            properties[PROPERTY_ancestorListeners].setHidden ( true );
            properties[PROPERTY_appleColor] = new PropertyDescriptor ( "appleColor", snake.JPlayField.class, "getAppleColor", "setAppleColor" ); // NOI18N
            properties[PROPERTY_appleColor].setPreferred ( true );
            properties[PROPERTY_appleColor].setShortDescription ( "The color to use for apple tiles." );
            properties[PROPERTY_appleColorSet] = new PropertyDescriptor ( "appleColorSet", snake.JPlayField.class, "isAppleColorSet", null ); // NOI18N
            properties[PROPERTY_appleColorSet].setHidden ( true );
            properties[PROPERTY_appleTileCount] = new PropertyDescriptor ( "appleTileCount", snake.JPlayField.class, "getAppleTileCount", null ); // NOI18N
            properties[PROPERTY_appleTileCount].setHidden ( true );
            properties[PROPERTY_appleTiles] = new PropertyDescriptor ( "appleTiles", snake.JPlayField.class, "getAppleTiles", null ); // NOI18N
            properties[PROPERTY_appleTiles].setHidden ( true );
            properties[PROPERTY_autoscrolls] = new PropertyDescriptor ( "autoscrolls", snake.JPlayField.class, "getAutoscrolls", "setAutoscrolls" ); // NOI18N
            properties[PROPERTY_autoscrolls].setShortDescription ( "Determines if this component automatically scrolls its contents when dragged." );
            properties[PROPERTY_background] = new PropertyDescriptor ( "background", snake.JPlayField.class, "getBackground", "setBackground" ); // NOI18N
            properties[PROPERTY_background].setPreferred ( true );
            properties[PROPERTY_background].setShortDescription ( "The background color of the component." );
            properties[PROPERTY_backgroundSet] = new PropertyDescriptor ( "backgroundSet", snake.JPlayField.class, "isBackgroundSet", null ); // NOI18N
            properties[PROPERTY_backgroundSet].setHidden ( true );
            properties[PROPERTY_baselineResizeBehavior] = new PropertyDescriptor ( "baselineResizeBehavior", snake.JPlayField.class, "getBaselineResizeBehavior", null ); // NOI18N
            properties[PROPERTY_baselineResizeBehavior].setHidden ( true );
            properties[PROPERTY_border] = new PropertyDescriptor ( "border", snake.JPlayField.class, "getBorder", "setBorder" ); // NOI18N
            properties[PROPERTY_border].setShortDescription ( "The component's border." );
            properties[PROPERTY_bounds] = new PropertyDescriptor ( "bounds", snake.JPlayField.class, "getBounds", "setBounds" ); // NOI18N
            properties[PROPERTY_bounds].setHidden ( true );
            properties[PROPERTY_colorModel] = new PropertyDescriptor ( "colorModel", snake.JPlayField.class, "getColorModel", null ); // NOI18N
            properties[PROPERTY_colorModel].setHidden ( true );
            properties[PROPERTY_columnCount] = new PropertyDescriptor ( "columnCount", snake.JPlayField.class, "getColumnCount", "setColumnCount" ); // NOI18N
            properties[PROPERTY_columnCount].setPreferred ( true );
            properties[PROPERTY_columnCount].setShortDescription ( "The number of columns in the play field." );
            properties[PROPERTY_component] = new IndexedPropertyDescriptor ( "component", snake.JPlayField.class, null, null, "getComponent", null ); // NOI18N
            properties[PROPERTY_component].setHidden ( true );
            properties[PROPERTY_componentCount] = new PropertyDescriptor ( "componentCount", snake.JPlayField.class, "getComponentCount", null ); // NOI18N
            properties[PROPERTY_componentCount].setHidden ( true );
            properties[PROPERTY_componentListeners] = new PropertyDescriptor ( "componentListeners", snake.JPlayField.class, "getComponentListeners", null ); // NOI18N
            properties[PROPERTY_componentListeners].setHidden ( true );
            properties[PROPERTY_componentOrientation] = new PropertyDescriptor ( "componentOrientation", snake.JPlayField.class, "getComponentOrientation", "setComponentOrientation" ); // NOI18N
            properties[PROPERTY_componentOrientation].setHidden ( true );
            properties[PROPERTY_componentPopupMenu] = new PropertyDescriptor ( "componentPopupMenu", snake.JPlayField.class, "getComponentPopupMenu", "setComponentPopupMenu" ); // NOI18N
            properties[PROPERTY_componentPopupMenu].setShortDescription ( "The popup menu to show" );
            properties[PROPERTY_components] = new PropertyDescriptor ( "components", snake.JPlayField.class, "getComponents", null ); // NOI18N
            properties[PROPERTY_components].setHidden ( true );
            properties[PROPERTY_containerListeners] = new PropertyDescriptor ( "containerListeners", snake.JPlayField.class, "getContainerListeners", null ); // NOI18N
            properties[PROPERTY_containerListeners].setHidden ( true );
            properties[PROPERTY_cursor] = new PropertyDescriptor ( "cursor", snake.JPlayField.class, "getCursor", "setCursor" ); // NOI18N
            properties[PROPERTY_cursorSet] = new PropertyDescriptor ( "cursorSet", snake.JPlayField.class, "isCursorSet", null ); // NOI18N
            properties[PROPERTY_cursorSet].setHidden ( true );
            properties[PROPERTY_debugGraphicsOptions] = new PropertyDescriptor ( "debugGraphicsOptions", snake.JPlayField.class, "getDebugGraphicsOptions", "setDebugGraphicsOptions" ); // NOI18N
            properties[PROPERTY_debugGraphicsOptions].setShortDescription ( "Diagnostic options for graphics operations." );
            properties[PROPERTY_displayable] = new PropertyDescriptor ( "displayable", snake.JPlayField.class, "isDisplayable", null ); // NOI18N
            properties[PROPERTY_displayable].setHidden ( true );
            properties[PROPERTY_doubleBuffered] = new PropertyDescriptor ( "doubleBuffered", snake.JPlayField.class, "isDoubleBuffered", "setDoubleBuffered" ); // NOI18N
            properties[PROPERTY_dropTarget] = new PropertyDescriptor ( "dropTarget", snake.JPlayField.class, "getDropTarget", "setDropTarget" ); // NOI18N
            properties[PROPERTY_dropTarget].setHidden ( true );
            properties[PROPERTY_emptyTileCount] = new PropertyDescriptor ( "emptyTileCount", snake.JPlayField.class, "getEmptyTileCount", null ); // NOI18N
            properties[PROPERTY_emptyTileCount].setHidden ( true );
            properties[PROPERTY_emptyTiles] = new PropertyDescriptor ( "emptyTiles", snake.JPlayField.class, "getEmptyTiles", null ); // NOI18N
            properties[PROPERTY_emptyTiles].setHidden ( true );
            properties[PROPERTY_enabled] = new PropertyDescriptor ( "enabled", snake.JPlayField.class, "isEnabled", "setEnabled" ); // NOI18N
            properties[PROPERTY_enabled].setShortDescription ( "The enabled state of the component." );
            properties[PROPERTY_focusable] = new PropertyDescriptor ( "focusable", snake.JPlayField.class, "isFocusable", "setFocusable" ); // NOI18N
            properties[PROPERTY_focusCycleRoot] = new PropertyDescriptor ( "focusCycleRoot", snake.JPlayField.class, "isFocusCycleRoot", "setFocusCycleRoot" ); // NOI18N
            properties[PROPERTY_focusCycleRootAncestor] = new PropertyDescriptor ( "focusCycleRootAncestor", snake.JPlayField.class, "getFocusCycleRootAncestor", null ); // NOI18N
            properties[PROPERTY_focusCycleRootAncestor].setHidden ( true );
            properties[PROPERTY_focusListeners] = new PropertyDescriptor ( "focusListeners", snake.JPlayField.class, "getFocusListeners", null ); // NOI18N
            properties[PROPERTY_focusListeners].setHidden ( true );
            properties[PROPERTY_focusOwner] = new PropertyDescriptor ( "focusOwner", snake.JPlayField.class, "isFocusOwner", null ); // NOI18N
            properties[PROPERTY_focusOwner].setHidden ( true );
            properties[PROPERTY_focusTraversable] = new PropertyDescriptor ( "focusTraversable", snake.JPlayField.class, "isFocusTraversable", null ); // NOI18N
            properties[PROPERTY_focusTraversable].setHidden ( true );
            properties[PROPERTY_focusTraversalKeys] = new IndexedPropertyDescriptor ( "focusTraversalKeys", snake.JPlayField.class, null, null, null, "setFocusTraversalKeys" ); // NOI18N
            properties[PROPERTY_focusTraversalKeys].setHidden ( true );
            properties[PROPERTY_focusTraversalKeysEnabled] = new PropertyDescriptor ( "focusTraversalKeysEnabled", snake.JPlayField.class, "getFocusTraversalKeysEnabled", "setFocusTraversalKeysEnabled" ); // NOI18N
            properties[PROPERTY_focusTraversalKeysEnabled].setHidden ( true );
            properties[PROPERTY_focusTraversalPolicy] = new PropertyDescriptor ( "focusTraversalPolicy", snake.JPlayField.class, "getFocusTraversalPolicy", "setFocusTraversalPolicy" ); // NOI18N
            properties[PROPERTY_focusTraversalPolicyProvider] = new PropertyDescriptor ( "focusTraversalPolicyProvider", snake.JPlayField.class, "isFocusTraversalPolicyProvider", "setFocusTraversalPolicyProvider" ); // NOI18N
            properties[PROPERTY_focusTraversalPolicySet] = new PropertyDescriptor ( "focusTraversalPolicySet", snake.JPlayField.class, "isFocusTraversalPolicySet", null ); // NOI18N
            properties[PROPERTY_focusTraversalPolicySet].setHidden ( true );
            properties[PROPERTY_font] = new PropertyDescriptor ( "font", snake.JPlayField.class, "getFont", "setFont" ); // NOI18N
            properties[PROPERTY_font].setShortDescription ( "The font for the component." );
            properties[PROPERTY_fontSet] = new PropertyDescriptor ( "fontSet", snake.JPlayField.class, "isFontSet", null ); // NOI18N
            properties[PROPERTY_fontSet].setHidden ( true );
            properties[PROPERTY_foreground] = new PropertyDescriptor ( "foreground", snake.JPlayField.class, "getForeground", "setForeground" ); // NOI18N
            properties[PROPERTY_foreground].setPreferred ( true );
            properties[PROPERTY_foreground].setShortDescription ( "The foreground color of the component." );
            properties[PROPERTY_foregroundSet] = new PropertyDescriptor ( "foregroundSet", snake.JPlayField.class, "isForegroundSet", null ); // NOI18N
            properties[PROPERTY_foregroundSet].setHidden ( true );
            properties[PROPERTY_graphics] = new PropertyDescriptor ( "graphics", snake.JPlayField.class, "getGraphics", null ); // NOI18N
            properties[PROPERTY_graphics].setHidden ( true );
            properties[PROPERTY_graphicsConfiguration] = new PropertyDescriptor ( "graphicsConfiguration", snake.JPlayField.class, "getGraphicsConfiguration", null ); // NOI18N
            properties[PROPERTY_graphicsConfiguration].setHidden ( true );
            properties[PROPERTY_height] = new PropertyDescriptor ( "height", snake.JPlayField.class, "getHeight", null ); // NOI18N
            properties[PROPERTY_height].setHidden ( true );
            properties[PROPERTY_hierarchyBoundsListeners] = new PropertyDescriptor ( "hierarchyBoundsListeners", snake.JPlayField.class, "getHierarchyBoundsListeners", null ); // NOI18N
            properties[PROPERTY_hierarchyBoundsListeners].setHidden ( true );
            properties[PROPERTY_hierarchyListeners] = new PropertyDescriptor ( "hierarchyListeners", snake.JPlayField.class, "getHierarchyListeners", null ); // NOI18N
            properties[PROPERTY_hierarchyListeners].setHidden ( true );
            properties[PROPERTY_highQuality] = new PropertyDescriptor ( "highQuality", snake.JPlayField.class, "isHighQuality", "setHighQuality" ); // NOI18N
            properties[PROPERTY_highQuality].setShortDescription ( "Determines if the play field will be rendered in high quality." );
            properties[PROPERTY_ignoreRepaint] = new PropertyDescriptor ( "ignoreRepaint", snake.JPlayField.class, "getIgnoreRepaint", "setIgnoreRepaint" ); // NOI18N
            properties[PROPERTY_ignoreRepaint].setHidden ( true );
            properties[PROPERTY_inheritsPopupMenu] = new PropertyDescriptor ( "inheritsPopupMenu", snake.JPlayField.class, "getInheritsPopupMenu", "setInheritsPopupMenu" ); // NOI18N
            properties[PROPERTY_inheritsPopupMenu].setShortDescription ( "Whether or not the JPopupMenu is inherited" );
            properties[PROPERTY_inputContext] = new PropertyDescriptor ( "inputContext", snake.JPlayField.class, "getInputContext", null ); // NOI18N
            properties[PROPERTY_inputContext].setHidden ( true );
            properties[PROPERTY_inputMap] = new PropertyDescriptor ( "inputMap", snake.JPlayField.class, "getInputMap", null ); // NOI18N
            properties[PROPERTY_inputMap].setHidden ( true );
            properties[PROPERTY_inputMethodListeners] = new PropertyDescriptor ( "inputMethodListeners", snake.JPlayField.class, "getInputMethodListeners", null ); // NOI18N
            properties[PROPERTY_inputMethodListeners].setHidden ( true );
            properties[PROPERTY_inputMethodRequests] = new PropertyDescriptor ( "inputMethodRequests", snake.JPlayField.class, "getInputMethodRequests", null ); // NOI18N
            properties[PROPERTY_inputMethodRequests].setHidden ( true );
            properties[PROPERTY_inputVerifier] = new PropertyDescriptor ( "inputVerifier", snake.JPlayField.class, "getInputVerifier", "setInputVerifier" ); // NOI18N
            properties[PROPERTY_inputVerifier].setShortDescription ( "The component's input verifier." );
            properties[PROPERTY_insets] = new PropertyDescriptor ( "insets", snake.JPlayField.class, "getInsets", null ); // NOI18N
            properties[PROPERTY_insets].setExpert ( true );
            properties[PROPERTY_keyListeners] = new PropertyDescriptor ( "keyListeners", snake.JPlayField.class, "getKeyListeners", null ); // NOI18N
            properties[PROPERTY_keyListeners].setHidden ( true );
            properties[PROPERTY_layout] = new PropertyDescriptor ( "layout", snake.JPlayField.class, "getLayout", "setLayout" ); // NOI18N
            properties[PROPERTY_layout].setHidden ( true );
            properties[PROPERTY_lightweight] = new PropertyDescriptor ( "lightweight", snake.JPlayField.class, "isLightweight", null ); // NOI18N
            properties[PROPERTY_lightweight].setHidden ( true );
            properties[PROPERTY_locale] = new PropertyDescriptor ( "locale", snake.JPlayField.class, "getLocale", "setLocale" ); // NOI18N
            properties[PROPERTY_locale].setHidden ( true );
            properties[PROPERTY_location] = new PropertyDescriptor ( "location", snake.JPlayField.class, "getLocation", "setLocation" ); // NOI18N
            properties[PROPERTY_location].setHidden ( true );
            properties[PROPERTY_locationOnScreen] = new PropertyDescriptor ( "locationOnScreen", snake.JPlayField.class, "getLocationOnScreen", null ); // NOI18N
            properties[PROPERTY_locationOnScreen].setHidden ( true );
            properties[PROPERTY_managingFocus] = new PropertyDescriptor ( "managingFocus", snake.JPlayField.class, "isManagingFocus", null ); // NOI18N
            properties[PROPERTY_managingFocus].setHidden ( true );
            properties[PROPERTY_maximumSize] = new PropertyDescriptor ( "maximumSize", snake.JPlayField.class, "getMaximumSize", "setMaximumSize" ); // NOI18N
            properties[PROPERTY_maximumSize].setShortDescription ( "The maximum size of the component." );
            properties[PROPERTY_maximumSizeSet] = new PropertyDescriptor ( "maximumSizeSet", snake.JPlayField.class, "isMaximumSizeSet", null ); // NOI18N
            properties[PROPERTY_maximumSizeSet].setHidden ( true );
            properties[PROPERTY_minimumSize] = new PropertyDescriptor ( "minimumSize", snake.JPlayField.class, "getMinimumSize", "setMinimumSize" ); // NOI18N
            properties[PROPERTY_minimumSize].setShortDescription ( "The minimum size of the component." );
            properties[PROPERTY_minimumSizeSet] = new PropertyDescriptor ( "minimumSizeSet", snake.JPlayField.class, "isMinimumSizeSet", null ); // NOI18N
            properties[PROPERTY_minimumSizeSet].setHidden ( true );
            properties[PROPERTY_mixingCutoutShape] = new PropertyDescriptor ( "mixingCutoutShape", snake.JPlayField.class, null, "setMixingCutoutShape" ); // NOI18N
            properties[PROPERTY_mixingCutoutShape].setHidden ( true );
            properties[PROPERTY_model] = new PropertyDescriptor ( "model", snake.JPlayField.class, "getModel", "setModel" ); // NOI18N
            properties[PROPERTY_model].setPreferred ( true );
            properties[PROPERTY_model].setShortDescription ( "The model used to contain the tiles in the play field." );
            properties[PROPERTY_mouseListeners] = new PropertyDescriptor ( "mouseListeners", snake.JPlayField.class, "getMouseListeners", null ); // NOI18N
            properties[PROPERTY_mouseListeners].setHidden ( true );
            properties[PROPERTY_mouseMotionListeners] = new PropertyDescriptor ( "mouseMotionListeners", snake.JPlayField.class, "getMouseMotionListeners", null ); // NOI18N
            properties[PROPERTY_mouseMotionListeners].setHidden ( true );
            properties[PROPERTY_mousePosition] = new PropertyDescriptor ( "mousePosition", snake.JPlayField.class, "getMousePosition", null ); // NOI18N
            properties[PROPERTY_mousePosition].setHidden ( true );
            properties[PROPERTY_mouseWheelListeners] = new PropertyDescriptor ( "mouseWheelListeners", snake.JPlayField.class, "getMouseWheelListeners", null ); // NOI18N
            properties[PROPERTY_mouseWheelListeners].setHidden ( true );
            properties[PROPERTY_name] = new PropertyDescriptor ( "name", snake.JPlayField.class, "getName", "setName" ); // NOI18N
            properties[PROPERTY_nextFocusableComponent] = new PropertyDescriptor ( "nextFocusableComponent", snake.JPlayField.class, "getNextFocusableComponent", "setNextFocusableComponent" ); // NOI18N
            properties[PROPERTY_opaque] = new PropertyDescriptor ( "opaque", snake.JPlayField.class, "isOpaque", "setOpaque" ); // NOI18N
            properties[PROPERTY_opaque].setShortDescription ( "The component's opacity" );
            properties[PROPERTY_optimizedDrawingEnabled] = new PropertyDescriptor ( "optimizedDrawingEnabled", snake.JPlayField.class, "isOptimizedDrawingEnabled", null ); // NOI18N
            properties[PROPERTY_optimizedDrawingEnabled].setHidden ( true );
            properties[PROPERTY_paintingForPrint] = new PropertyDescriptor ( "paintingForPrint", snake.JPlayField.class, "isPaintingForPrint", null ); // NOI18N
            properties[PROPERTY_paintingForPrint].setHidden ( true );
            properties[PROPERTY_paintingTile] = new PropertyDescriptor ( "paintingTile", snake.JPlayField.class, "isPaintingTile", null ); // NOI18N
            properties[PROPERTY_paintingTile].setHidden ( true );
            properties[PROPERTY_parent] = new PropertyDescriptor ( "parent", snake.JPlayField.class, "getParent", null ); // NOI18N
            properties[PROPERTY_parent].setHidden ( true );
            properties[PROPERTY_playFieldBounds] = new PropertyDescriptor ( "playFieldBounds", snake.JPlayField.class, "getPlayFieldBounds", null ); // NOI18N
            properties[PROPERTY_playFieldBounds].setHidden ( true );
            properties[PROPERTY_playFieldListeners] = new PropertyDescriptor ( "playFieldListeners", snake.JPlayField.class, "getPlayFieldListeners", null ); // NOI18N
            properties[PROPERTY_playFieldListeners].setHidden ( true );
            properties[PROPERTY_preferredSize] = new PropertyDescriptor ( "preferredSize", snake.JPlayField.class, "getPreferredSize", "setPreferredSize" ); // NOI18N
            properties[PROPERTY_preferredSize].setShortDescription ( "The preferred size of the component." );
            properties[PROPERTY_preferredSizeSet] = new PropertyDescriptor ( "preferredSizeSet", snake.JPlayField.class, "isPreferredSizeSet", null ); // NOI18N
            properties[PROPERTY_preferredSizeSet].setHidden ( true );
            properties[PROPERTY_primarySnakeColor] = new PropertyDescriptor ( "primarySnakeColor", snake.JPlayField.class, "getPrimarySnakeColor", "setPrimarySnakeColor" ); // NOI18N
            properties[PROPERTY_primarySnakeColor].setPreferred ( true );
            properties[PROPERTY_primarySnakeColor].setShortDescription ( "The primary color for snake tiles." );
            properties[PROPERTY_primarySnakeColorSet] = new PropertyDescriptor ( "primarySnakeColorSet", snake.JPlayField.class, "isPrimarySnakeColorSet", null ); // NOI18N
            properties[PROPERTY_primarySnakeColorSet].setHidden ( true );
            properties[PROPERTY_propertyChangeListeners] = new PropertyDescriptor ( "propertyChangeListeners", snake.JPlayField.class, "getPropertyChangeListeners", null ); // NOI18N
            properties[PROPERTY_propertyChangeListeners].setHidden ( true );
            properties[PROPERTY_registeredKeyStrokes] = new PropertyDescriptor ( "registeredKeyStrokes", snake.JPlayField.class, "getRegisteredKeyStrokes", null ); // NOI18N
            properties[PROPERTY_registeredKeyStrokes].setHidden ( true );
            properties[PROPERTY_renderer] = new PropertyDescriptor ( "renderer", snake.JPlayField.class, "getRenderer", "setRenderer" ); // NOI18N
            properties[PROPERTY_renderer].setShortDescription ( "The renderer used to render the play field." );
            properties[PROPERTY_rendererSet] = new PropertyDescriptor ( "rendererSet", snake.JPlayField.class, "isRendererSet", null ); // NOI18N
            properties[PROPERTY_rendererSet].setHidden ( true );
            properties[PROPERTY_requestFocusEnabled] = new PropertyDescriptor ( "requestFocusEnabled", snake.JPlayField.class, "isRequestFocusEnabled", "setRequestFocusEnabled" ); // NOI18N
            properties[PROPERTY_rootPane] = new PropertyDescriptor ( "rootPane", snake.JPlayField.class, "getRootPane", null ); // NOI18N
            properties[PROPERTY_rootPane].setHidden ( true );
            properties[PROPERTY_rowCount] = new PropertyDescriptor ( "rowCount", snake.JPlayField.class, "getRowCount", "setRowCount" ); // NOI18N
            properties[PROPERTY_rowCount].setPreferred ( true );
            properties[PROPERTY_rowCount].setShortDescription ( "The number of rows in the play field." );
            properties[PROPERTY_secondarySnakeColor] = new PropertyDescriptor ( "secondarySnakeColor", snake.JPlayField.class, "getSecondarySnakeColor", "setSecondarySnakeColor" ); // NOI18N
            properties[PROPERTY_secondarySnakeColor].setPreferred ( true );
            properties[PROPERTY_secondarySnakeColor].setShortDescription ( "The secondary color for snake tiles." );
            properties[PROPERTY_secondarySnakeColorSet] = new PropertyDescriptor ( "secondarySnakeColorSet", snake.JPlayField.class, "isSecondarySnakeColorSet", null ); // NOI18N
            properties[PROPERTY_secondarySnakeColorSet].setHidden ( true );
            properties[PROPERTY_showing] = new PropertyDescriptor ( "showing", snake.JPlayField.class, "isShowing", null ); // NOI18N
            properties[PROPERTY_showing].setHidden ( true );
            properties[PROPERTY_size] = new PropertyDescriptor ( "size", snake.JPlayField.class, "getSize", "setSize" ); // NOI18N
            properties[PROPERTY_size].setHidden ( true );
            properties[PROPERTY_tileBackground] = new PropertyDescriptor ( "tileBackground", snake.JPlayField.class, "getTileBackground", "setTileBackground" ); // NOI18N
            properties[PROPERTY_tileBackground].setPreferred ( true );
            properties[PROPERTY_tileBackground].setShortDescription ( "The background color for the tiles." );
            properties[PROPERTY_tileBackgroundPainted] = new PropertyDescriptor ( "tileBackgroundPainted", snake.JPlayField.class, "isTileBackgroundPainted", "setTileBackgroundPainted" ); // NOI18N
            properties[PROPERTY_tileBackgroundPainted].setShortDescription ( "Determines if the tile background is to be rendered." );
            properties[PROPERTY_tileBackgroundSet] = new PropertyDescriptor ( "tileBackgroundSet", snake.JPlayField.class, "isTileBackgroundSet", null ); // NOI18N
            properties[PROPERTY_tileBackgroundSet].setHidden ( true );
            properties[PROPERTY_tileBorder] = new PropertyDescriptor ( "tileBorder", snake.JPlayField.class, "getTileBorder", "setTileBorder" ); // NOI18N
            properties[PROPERTY_tileBorder].setShortDescription ( "The color for the border around the tiles." );
            properties[PROPERTY_tileBorderPainted] = new PropertyDescriptor ( "tileBorderPainted", snake.JPlayField.class, "isTileBorderPainted", "setTileBorderPainted" ); // NOI18N
            properties[PROPERTY_tileBorderPainted].setShortDescription ( "Determines if the tile border is to be rendered." );
            properties[PROPERTY_tileBorderSet] = new PropertyDescriptor ( "tileBorderSet", snake.JPlayField.class, "isTileBorderSet", null ); // NOI18N
            properties[PROPERTY_tileBorderSet].setHidden ( true );
            properties[PROPERTY_tileCount] = new PropertyDescriptor ( "tileCount", snake.JPlayField.class, "getTileCount", null ); // NOI18N
            properties[PROPERTY_tileCount].setHidden ( true );
            properties[PROPERTY_tilesAreAdjusting] = new PropertyDescriptor ( "tilesAreAdjusting", snake.JPlayField.class, "getTilesAreAdjusting", "setTilesAreAdjusting" ); // NOI18N
            properties[PROPERTY_tileSize] = new PropertyDescriptor ( "tileSize", snake.JPlayField.class, "getTileSize", null ); // NOI18N
            properties[PROPERTY_tileSize].setHidden ( true );
            properties[PROPERTY_toolkit] = new PropertyDescriptor ( "toolkit", snake.JPlayField.class, "getToolkit", null ); // NOI18N
            properties[PROPERTY_toolkit].setHidden ( true );
            properties[PROPERTY_toolTipText] = new PropertyDescriptor ( "toolTipText", snake.JPlayField.class, "getToolTipText", "setToolTipText" ); // NOI18N
            properties[PROPERTY_toolTipText].setPreferred ( true );
            properties[PROPERTY_toolTipText].setShortDescription ( "The text to display in a tool tip." );
            properties[PROPERTY_topLevelAncestor] = new PropertyDescriptor ( "topLevelAncestor", snake.JPlayField.class, "getTopLevelAncestor", null ); // NOI18N
            properties[PROPERTY_topLevelAncestor].setHidden ( true );
            properties[PROPERTY_transferHandler] = new PropertyDescriptor ( "transferHandler", snake.JPlayField.class, "getTransferHandler", "setTransferHandler" ); // NOI18N
            properties[PROPERTY_transferHandler].setHidden ( true );
            properties[PROPERTY_treeLock] = new PropertyDescriptor ( "treeLock", snake.JPlayField.class, "getTreeLock", null ); // NOI18N
            properties[PROPERTY_treeLock].setHidden ( true );
            properties[PROPERTY_UI] = new PropertyDescriptor ( "UI", snake.JPlayField.class, "getUI", "setUI" ); // NOI18N
            properties[PROPERTY_UI].setHidden ( true );
            properties[PROPERTY_UI].setShortDescription ( "The UI object that implements the Component's LookAndFeel." );
            properties[PROPERTY_UIClassID] = new PropertyDescriptor ( "UIClassID", snake.JPlayField.class, "getUIClassID", null ); // NOI18N
            properties[PROPERTY_valid] = new PropertyDescriptor ( "valid", snake.JPlayField.class, "isValid", null ); // NOI18N
            properties[PROPERTY_valid].setHidden ( true );
            properties[PROPERTY_validateRoot] = new PropertyDescriptor ( "validateRoot", snake.JPlayField.class, "isValidateRoot", null ); // NOI18N
            properties[PROPERTY_validateRoot].setHidden ( true );
            properties[PROPERTY_verifyInputWhenFocusTarget] = new PropertyDescriptor ( "verifyInputWhenFocusTarget", snake.JPlayField.class, "getVerifyInputWhenFocusTarget", "setVerifyInputWhenFocusTarget" ); // NOI18N
            properties[PROPERTY_verifyInputWhenFocusTarget].setShortDescription ( "Whether the Component verifies input before accepting focus." );
            properties[PROPERTY_vetoableChangeListeners] = new PropertyDescriptor ( "vetoableChangeListeners", snake.JPlayField.class, "getVetoableChangeListeners", null ); // NOI18N
            properties[PROPERTY_vetoableChangeListeners].setHidden ( true );
            properties[PROPERTY_visible] = new PropertyDescriptor ( "visible", snake.JPlayField.class, "isVisible", "setVisible" ); // NOI18N
            properties[PROPERTY_visible].setHidden ( true );
            properties[PROPERTY_visibleRect] = new PropertyDescriptor ( "visibleRect", snake.JPlayField.class, "getVisibleRect", null ); // NOI18N
            properties[PROPERTY_visibleRect].setHidden ( true );
            properties[PROPERTY_width] = new PropertyDescriptor ( "width", snake.JPlayField.class, "getWidth", null ); // NOI18N
            properties[PROPERTY_width].setHidden ( true );
            properties[PROPERTY_x] = new PropertyDescriptor ( "x", snake.JPlayField.class, "getX", null ); // NOI18N
            properties[PROPERTY_x].setHidden ( true );
            properties[PROPERTY_y] = new PropertyDescriptor ( "y", snake.JPlayField.class, "getY", null ); // NOI18N
            properties[PROPERTY_y].setHidden ( true );
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Properties
        // Here you can add code for customizing the properties array.

        return properties;     }//GEN-LAST:Properties

    // EventSet identifiers//GEN-FIRST:Events
    private static final int EVENT_ancestorListener = 0;
    private static final int EVENT_componentListener = 1;
    private static final int EVENT_containerListener = 2;
    private static final int EVENT_focusListener = 3;
    private static final int EVENT_hierarchyBoundsListener = 4;
    private static final int EVENT_hierarchyListener = 5;
    private static final int EVENT_inputMethodListener = 6;
    private static final int EVENT_keyListener = 7;
    private static final int EVENT_mouseListener = 8;
    private static final int EVENT_mouseMotionListener = 9;
    private static final int EVENT_mouseWheelListener = 10;
    private static final int EVENT_playFieldListener = 11;
    private static final int EVENT_propertyChangeListener = 12;
    private static final int EVENT_vetoableChangeListener = 13;

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[14];
    
        try {
            eventSets[EVENT_ancestorListener] = new EventSetDescriptor ( snake.JPlayField.class, "ancestorListener", javax.swing.event.AncestorListener.class, new String[] {"ancestorAdded", "ancestorRemoved", "ancestorMoved"}, "addAncestorListener", "removeAncestorListener" ); // NOI18N
            eventSets[EVENT_componentListener] = new EventSetDescriptor ( snake.JPlayField.class, "componentListener", java.awt.event.ComponentListener.class, new String[] {"componentResized", "componentMoved", "componentShown", "componentHidden"}, "addComponentListener", "removeComponentListener" ); // NOI18N
            eventSets[EVENT_containerListener] = new EventSetDescriptor ( snake.JPlayField.class, "containerListener", java.awt.event.ContainerListener.class, new String[] {"componentAdded", "componentRemoved"}, "addContainerListener", "removeContainerListener" ); // NOI18N
            eventSets[EVENT_focusListener] = new EventSetDescriptor ( snake.JPlayField.class, "focusListener", java.awt.event.FocusListener.class, new String[] {"focusGained", "focusLost"}, "addFocusListener", "removeFocusListener" ); // NOI18N
            eventSets[EVENT_hierarchyBoundsListener] = new EventSetDescriptor ( snake.JPlayField.class, "hierarchyBoundsListener", java.awt.event.HierarchyBoundsListener.class, new String[] {"ancestorMoved", "ancestorResized"}, "addHierarchyBoundsListener", "removeHierarchyBoundsListener" ); // NOI18N
            eventSets[EVENT_hierarchyListener] = new EventSetDescriptor ( snake.JPlayField.class, "hierarchyListener", java.awt.event.HierarchyListener.class, new String[] {"hierarchyChanged"}, "addHierarchyListener", "removeHierarchyListener" ); // NOI18N
            eventSets[EVENT_inputMethodListener] = new EventSetDescriptor ( snake.JPlayField.class, "inputMethodListener", java.awt.event.InputMethodListener.class, new String[] {"inputMethodTextChanged", "caretPositionChanged"}, "addInputMethodListener", "removeInputMethodListener" ); // NOI18N
            eventSets[EVENT_keyListener] = new EventSetDescriptor ( snake.JPlayField.class, "keyListener", java.awt.event.KeyListener.class, new String[] {"keyTyped", "keyPressed", "keyReleased"}, "addKeyListener", "removeKeyListener" ); // NOI18N
            eventSets[EVENT_mouseListener] = new EventSetDescriptor ( snake.JPlayField.class, "mouseListener", java.awt.event.MouseListener.class, new String[] {"mouseClicked", "mousePressed", "mouseReleased", "mouseEntered", "mouseExited"}, "addMouseListener", "removeMouseListener" ); // NOI18N
            eventSets[EVENT_mouseMotionListener] = new EventSetDescriptor ( snake.JPlayField.class, "mouseMotionListener", java.awt.event.MouseMotionListener.class, new String[] {"mouseDragged", "mouseMoved"}, "addMouseMotionListener", "removeMouseMotionListener" ); // NOI18N
            eventSets[EVENT_mouseWheelListener] = new EventSetDescriptor ( snake.JPlayField.class, "mouseWheelListener", java.awt.event.MouseWheelListener.class, new String[] {"mouseWheelMoved"}, "addMouseWheelListener", "removeMouseWheelListener" ); // NOI18N
            eventSets[EVENT_playFieldListener] = new EventSetDescriptor ( snake.JPlayField.class, "playFieldListener", snake.event.PlayFieldListener.class, new String[] {"tilesAdded", "tilesRemoved", "tilesChanged"}, "addPlayFieldListener", "removePlayFieldListener" ); // NOI18N
            eventSets[EVENT_propertyChangeListener] = new EventSetDescriptor ( snake.JPlayField.class, "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] {"propertyChange"}, "addPropertyChangeListener", "removePropertyChangeListener" ); // NOI18N
            eventSets[EVENT_vetoableChangeListener] = new EventSetDescriptor ( snake.JPlayField.class, "vetoableChangeListener", java.beans.VetoableChangeListener.class, new String[] {"vetoableChange"}, "addVetoableChangeListener", "removeVetoableChangeListener" ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Events
        // Here you can add code for customizing the event sets array.

        return eventSets;     }//GEN-LAST:Events

    // Method information will be obtained from introspection.//GEN-FIRST:Methods
    private static MethodDescriptor[] methods = null;
    private static MethodDescriptor[] getMdescriptor(){//GEN-HEADEREND:Methods
        // Here you can add code for customizing the methods array.

        return methods;     }//GEN-LAST:Methods

    private static java.awt.Image iconColor16 = null;//GEN-BEGIN:IconsDef
    private static java.awt.Image iconColor32 = null;
    private static java.awt.Image iconMono16 = null;
    private static java.awt.Image iconMono32 = null;//GEN-END:IconsDef
    private static String iconNameC16 = null;//GEN-BEGIN:Icons
    private static String iconNameC32 = null;
    private static String iconNameM16 = null;
    private static String iconNameM32 = null;//GEN-END:Icons

    private static final int defaultPropertyIndex = -1;//GEN-BEGIN:Idx
    private static final int defaultEventIndex = -1;//GEN-END:Idx


//GEN-FIRST:Superclass
    // Here you can add code for customizing the Superclass BeanInfo.

//GEN-LAST:Superclass
    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     *
     * @return BeanDescriptor describing the editable properties of this bean.
     * May return null if the information should be obtained by automatic
     * analysis.
     */
    @Override
    public BeanDescriptor getBeanDescriptor() {
        return getBdescriptor();
    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     *
     * @return An array of PropertyDescriptors describing the editable
     * properties supported by this bean. May return null if the information
     * should be obtained by automatic analysis.
     * <p>
     * If a property is indexed, then its entry in the result array will belong
     * to the IndexedPropertyDescriptor subclass of PropertyDescriptor. A client
     * of getPropertyDescriptors can use "instanceof" to check if a given
     * PropertyDescriptor is an IndexedPropertyDescriptor.
     */
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return getPdescriptor();
    }

    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     *
     * @return An array of EventSetDescriptors describing the kinds of events
     * fired by this bean. May return null if the information should be obtained
     * by automatic analysis.
     */
    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        return getEdescriptor();
    }

    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     *
     * @return An array of MethodDescriptors describing the methods implemented
     * by this bean. May return null if the information should be obtained by
     * automatic analysis.
     */
    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        return getMdescriptor();
    }

    /**
     * A bean may have a "default" property that is the property that will
     * mostly commonly be initially chosen for update by human's who are
     * customizing the bean.
     *
     * @return Index of default property in the PropertyDescriptor array
     * returned by getPropertyDescriptors.
     * <P>
     * Returns -1 if there is no default property.
     */
    @Override
    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }

    /**
     * A bean may have a "default" event that is the event that will mostly
     * commonly be used by human's when using the bean.
     *
     * @return Index of default event in the EventSetDescriptor array returned
     * by getEventSetDescriptors.
     * <P>
     * Returns -1 if there is no default event.
     */
    @Override
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }
    /**
     * This is a two-dimensional array containing some of the colors to use when 
     * rendering the icon representing JPlayField. The first array contains the 
     * colors to use for a color icon, and the second array contains the colors 
     * to use for a monochrome icon. The first two colors of both arrays are the 
     * colors used for the outline gradient, the third color is the apple color, 
     * and the fourth and fifth colors are the primary and secondary snake 
     * colors, respectively.
     * @see SnakeConstants#APPLE_COLOR
     * @see SnakeConstants#PRIMARY_SNAKE_COLOR
     * @see SnakeConstants#SECONDARY_SNAKE_COLOR
     */
    private static final java.awt.Color[][] ICON_COLORS = {
        {new java.awt.Color(0x8694A2),new java.awt.Color(0x55606B),
            SnakeConstants.APPLE_COLOR,SnakeConstants.PRIMARY_SNAKE_COLOR,
            SnakeConstants.SECONDARY_SNAKE_COLOR},
        {new java.awt.Color(0x949494),new java.awt.Color(0x606060),
            java.awt.Color.GRAY,java.awt.Color.LIGHT_GRAY,
            java.awt.Color.DARK_GRAY}
    };
    /**
     * This generates the image to use for an icon that represents JPlayField. 
     * If a location for a picture is given (i.e. {@code iconName} is not null), 
     * then the image is loaded from that location. Otherwise, this will 
     * generate the image to use based off the given values. 
     * @param iconName The location for the picture to use as an icon. If this 
     * is null, then the icon will be generated.
     * @param scale The scale for the generated icon (must be either 1 for a 
     * 16x16 icon or 2 for a 32x32 icon).
     * @param mono Whether the icon should be monochrome (false for color, true 
     * for monochrome).
     * @return The image to use as an icon representing JPlayField.
     * @see #getIcon(int) 
     * @see #loadImage(String) 
     */
    private java.awt.Image generateIcon(String iconName,int scale,boolean mono){
        if (iconName != null)   // If the icon name is not null
            return loadImage(iconName);
            // Get the array of colors to use based off whether this is a 
            // monochrome icon or not.
        java.awt.Color[] colors = ICON_COLORS[(mono)?1:0];
            // Create a buffered image to render to
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                16*scale,16*scale,java.awt.image.BufferedImage.TYPE_INT_ARGB);
            // The graphics object used to paint the image
        java.awt.Graphics2D g = img.createGraphics();
        
            // Paint a transparent shadow behind everything
        g.setColor(new java.awt.Color(0x80808080,true));
        g.fillRect(0, 0, img.getWidth()-1, img.getHeight());
            // Paint a gradient to outline the panel
        g.setPaint(new java.awt.GradientPaint(0,0,colors[0],0,img.getHeight()-2,
                colors[1]));
        g.fillRect(0, 0, img.getWidth()-1, img.getHeight()-1);
            // Fill the panel with the tile background color
        g.setColor(SnakeConstants.TILE_BACKGROUND_COLOR);
        g.fillRect(1, 1, img.getWidth()-3, img.getHeight()-3);
        
            // Render a representation of tiles
            // Calculate a size to use for the represented tiles.
        int tileS = 3*scale+(scale>>1);
        if (scale == 2){    // If the icon is a 32x32 icon
            g.setColor(colors[2]);
                // A for loop to draw 2 rectangles to represent apple tiles
            for (int i = 0; i < 2; i++)
                g.fillRect(11+(14*i), 4, 2, 2);
                // A for loop to render some snake tiles in both the primary and 
            for (int i = 0; i < 2; i++){    // secondary default snake colors
                    // The x offset to use to render the rectangles, so as to 
                    // shift the secondary snake tiles over
                int xOff = 14*i;
                    // The y offset to use to render the rectangles, so as to 
                    // shift the secondary snake tiles over
                int yOff = 7*i;
                g.setColor(colors[3+i]);
                g.fillRect(4+xOff, 11+yOff, 2, 11);
                g.fillRect(11+xOff, 9+yOff, 2, 4);
                g.fillRect(9+xOff, 18+yOff, 6, 2);
                g.fillRect(18-xOff, 11+yOff+yOff, 9, 2);
            }
        }
        else{   // If the icon is a 16x16 icon
                // We'll use darker colors in some areas to recreate details 
                // that would otherwise not fit. This is a for loop to render 
                // the darker colors first and then draw the normal colors over 
                // the darker ones.
            for (int d = 0; d < 2; d++){
                    // If this is rendering the darker colors, use a darker 
                    // version of the apple color to use. Otherwise, use the 
                g.setColor((d==0)?colors[2].darker():colors[2]);// apple color
                    // A for loop to draw 2 rectangles to represent apple tiles. 
                    // The darker color takes up most of the area, while the 
                    // normal color takes up a single pixel
                for (int i = 0; i < 2; i++)
                    g.fillRect(5+d+(6*i), 2, 2-d, 2-d);
                    // A for loop to render some snake tiles in both the primary 
                for (int i = 0; i < 2; i++){//and secondary default snake colors
                        // The x offset to use to render the rectangles, so as 
                        // to shift the secondary snake tiles over
                    int xOff = 6*i;
                        // The y offset to use to render the rectangles, so as 
                        // to shift the secondary snake tiles over
                    int yOff = 3*i;
                        // If this is rendering the darker colors, use a darker 
                        // version of the current snake color. Otherwise, use 
                        // the current snake color
                    g.setColor((d==0)?colors[3+i].darker():colors[3+i]);
                    g.fillRect(2+xOff, 5+d+yOff, 2, 5-d);
                    g.fillRect(5+xOff, 5+yOff, 2, 2-d);
                        // If this is rendering the normal colors (this segment 
                    if (d > 0)  // does not need additional details)
                        g.fillRect(5+xOff, 8+yOff, 2, 2);
                    g.fillRect(8+d-xOff, 5+yOff+yOff, 5-d-d, 2);
                }
            }
        }
            // Render the tile border
        g.setColor(SnakeConstants.TILE_BORDER_COLOR);
            // Start by outlining the play field
        g.drawRect(1, 1, img.getWidth()-4,img.getHeight()-4);
            // A for loop to draw the lines between the tiles. This is to avoid 
            // lines overlapping each other.
        for (int i = 0; i < 4; i++){
                // Get the x coordinate for the horizontal lines
            int x = 2 + (tileS*i);
                // If this is not rendering the first set of lines, render a 
                // vertical line at the x coordinate before the one for the 
            if (i > 0)      // horizontal lines
                g.drawLine(x-1, 2, x-1, img.getHeight()-4);
                // A for loop to render 3 horizontal lines.
            for (int j = 0; j < 3; j++){
                    // Get the y coordinate for the current line
                int y = (4*scale)+(tileS*j);    
                g.drawLine(x, y, x+tileS-2, y);
            }
        }
        
        g.dispose();
        return img;
    }
    
    /**
     * This method returns an image object that can be used to represent the
     * bean in toolboxes, toolbars, etc. Icon images will typically be GIFs, but
     * may in future include other formats.
     * <p>
     * Beans aren't required to provide icons and may return null from this
     * method.
     * <p>
     * There are four possible flavors of icons (16x16 color, 32x32 color, 16x16
     * mono, 32x32 mono). If a bean choses to only support a single icon we
     * recommend supporting 16x16 color.
     * <p>
     * We recommend that icons have a "transparent" background so they can be
     * rendered onto an existing background.
     *
     * @param iconKind The kind of icon requested. This should be one of the
     * constant values ICON_COLOR_16x16, ICON_COLOR_32x32, ICON_MONO_16x16, or
     * ICON_MONO_32x32.
     * @return An image object representing the requested icon. May return null
     * if no suitable icon is available.
     */
    @Override
    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {         // Get the appropriate icon to return
            case ICON_COLOR_16x16:  // If this is getting a 16x16 color icon
                if (iconColor16 == null)//If the icon has not been generated yet
                    iconColor16 = generateIcon(iconNameC16,1,false);
                return iconColor16;
            case ICON_COLOR_32x32:  // If this is getting a 32x32 color icon
                if (iconColor32 == null)//If the icon has not been generated yet
                    iconColor32 = generateIcon(iconNameC32,2,false);
                return iconColor32;
            case ICON_MONO_16x16:  // If this is getting a 16x16 monochrome icon
                if (iconMono16 == null)// If the icon has not been generated yet
                    iconMono16 = generateIcon(iconNameM16,1,true);
                return iconMono16;
            case ICON_MONO_32x32:  // If this is getting a 32x32 monochrome icon
                if (iconMono32 == null)// If the icon has not been generated yet
                    iconMono32 = generateIcon(iconNameM32,2,true);
                return iconMono32;
            default:
                return null;
        }
    }
}
