package com.pieisnotpi.game;

import com.pieisnotpi.engine.ui.text.font.SystemFont;

public class Constants
{
    public static final int

            // The number of triangles per rounded corner. Ex. value of 4 = 4 sides per corner, 16 triangles per tile
            cornerSides = 4;

    public static float

            // Tile size
            size,

            // Radii of tile corners
            radii;

    public static final float

            // Space between game tiles
            tileSpace = 0.04f,

            // Space between outer tiles and the background pane's outer edge
            bgSpace = 0.04f,

            // Determines whitespace around game board
            border = 2.5f,

            // Determines tile corner radii. Ex. value of 6 = a radius 1/6th the size of the tile
            radiiDiv = 6,

            // Depth values for all the rendered elements
            // Used to prevent depth clipping/other various issues
            bgZ = -0.5f,
            bgTileZ = -0.45f,
            tileZ = -0.05f,
            menuBgZ = 0.2f,
            menuZ = 0.4f;


    public static final SystemFont

            loseFont    = SystemFont.getFont("Arial", 72, SystemFont.BOLD, true),
            tileFont    = SystemFont.getFont("Arial", 48, SystemFont.BOLD, true),
            scoreFont   = SystemFont.getFont("Arial", 48, SystemFont.BOLD, true);
}
