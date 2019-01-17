package com.pieisnotpi.game;

import com.pieisnotpi.engine.ui.text.font.SystemFont;

public class Constants
{
    public static final int

            // The number of triangles per rounded corner. Ex. value of 4 = 4 sides per corner, 16 triangles per tile
            CORNER_SIDES = 4;

    public static float

            // Tile size
            size,

            // Radii of tile corners
            radii;

    public static final float

            // Space between game tiles
            TILE_SPACE = 0.04f,

            // Space between outer tiles and the background pane's outer edge
            BG_SPACE = 0.04f,

            // Determines whitespace around game board
            BORDER = 2.5f,

            // Determines tile corner radii. Higher values mean smaller corners. Must be > 2
            // Radii = (size/RADII_DIV)
            RADII_DIV = 6,

            // Tile speed multiplier. Hire values mean faster tile movements
            TILE_SPEED = 8f,

            // Depth values for all the rendered elements
            // Used to prevent depth clipping/other various issues
            BG_Z = -0.5f,
            BG_TILE_Z = -0.45f,
            TILE_Z = -0.05f,
            MENU_BG_Z = 0.2f,
            MENU_Z = 0.4f;


    public static final SystemFont

            LOSE_FONT = SystemFont.getFont("Arial", 72, SystemFont.BOLD, true),
            TILE_FONT = SystemFont.getFont("Arial", 48, SystemFont.BOLD, true),
            SCORE_FONT = SystemFont.getFont("Arial", 48, SystemFont.BOLD, true);
}
